import os, datetime as dt, requests
from fastapi import FastAPI, HTTPException, Header, Depends
from pydantic import BaseModel
from sqlalchemy import create_engine, Column, Integer, String, Date
from sqlalchemy.orm import declarative_base, sessionmaker
from jose import jwt, JWTError

# ---------- Configuración ----------
SECRET = os.getenv("JWT_SECRET", "change-me")
ALGO = "HS256"
# PostgreSQL en lugar de SQLite
DB_URL = os.getenv("DATABASE_URL", "postgresql://postgres:postgres@resource-db:5432/resource_db")
NOTIF_URL = os.getenv("NOTIFICATION_URL", "http://notification-service:8003")

engine = create_engine(DB_URL)
Session = sessionmaker(bind=engine)
Base = declarative_base()

# ---------- Modelo (matrículas del PFC) ----------
class Matricula(Base):
    __tablename__ = "matriculas"
    id_matricula = Column(Integer, primary_key=True)
    estudiante = Column(String, nullable=False)
    grado = Column(String, nullable=False)
    ano_lectivo = Column(String, nullable=False)
    estado = Column(String, nullable=False, default="ACTIVO")
    observaciones = Column(String, nullable=True)
    fecha_registro = Column(Date, default=dt.date.today)

Base.metadata.create_all(engine)
app = FastAPI(title="resource-service")

def envelope(data=None, message="OK", status="success"):
    return {
        "status": status,
        "data": data,
        "message": message,
        "timestamp": dt.datetime.utcnow().isoformat() + "Z"
    }

# ---------- Verificación JWT ----------
def require_user(authorization: str = Header(default="")):
    token = authorization.replace("Bearer ", "")
    try:
        return jwt.decode(token, SECRET, algorithms=[ALGO])["sub"]
    except JWTError:
        raise HTTPException(status_code=401, detail="Token requerido o invalido")

# ---------- Serializar ----------
def serialize(m: Matricula):
    return {
        "id_matricula": m.id_matricula,
        "estudiante": m.estudiante,
        "grado": m.grado,
        "ano_lectivo": m.ano_lectivo,
        "estado": m.estado,
        "observaciones": m.observaciones,
        "fecha_registro": m.fecha_registro.isoformat() if m.fecha_registro else None
    }

# ---------- Notificar al notification-service ----------
def notify(event, rid, user):
    try:
        requests.post(
            f"{NOTIF_URL}/notifications",
            json={"event": event, "resource_id": rid, "user": user},
            timeout=2
        )
    except requests.RequestException:
        pass

class MatriculaInput(BaseModel):
    estudiante: str
    grado: str
    ano_lectivo: str
    estado: str = "ACTIVO"
    observaciones: str | None = None

class MatriculaUpdate(BaseModel):
    estudiante: str | None = None
    grado: str | None = None
    ano_lectivo: str | None = None
    estado: str | None = None
    observaciones: str | None = None

# ---------- Endpoints ----------
@app.get("/resources")
def listar_matriculas(user: str = Depends(require_user)):
    db = Session()
    matriculas = db.query(Matricula).order_by(Matricula.id_matricula.desc()).all()
    return envelope([serialize(m) for m in matriculas], "Matriculas obtenidas")

@app.post("/resources")
def crear_matricula(m: MatriculaInput, user: str = Depends(require_user)):
    db = Session()
    nueva = Matricula(
        estudiante=m.estudiante,
        grado=m.grado,
        ano_lectivo=m.ano_lectivo,
        estado=m.estado,
        observaciones=m.observaciones
    )
    db.add(nueva)
    db.commit()
    db.refresh(nueva)
    notify("created", nueva.id_matricula, user)
    return envelope(serialize(nueva), "Matricula registrada")

@app.put("/resources/{id_matricula}")
def editar_matricula(id_matricula: int, m: MatriculaUpdate, user: str = Depends(require_user)):
    db = Session()
    matricula = db.query(Matricula).filter_by(id_matricula=id_matricula).first()
    if not matricula:
        raise HTTPException(status_code=404, detail="Matricula no encontrada")
    datos = m.model_dump(exclude_unset=True)
    for campo, valor in datos.items():
        setattr(matricula, campo, valor)
    db.commit()
    db.refresh(matricula)
    notify("updated", id_matricula, user)
    return envelope(serialize(matricula), "Matricula actualizada")

@app.delete("/resources/{id_matricula}")
def eliminar_matricula(id_matricula: int, user: str = Depends(require_user)):
    db = Session()
    matricula = db.query(Matricula).filter_by(id_matricula=id_matricula).first()
    if not matricula:
        raise HTTPException(status_code=404, detail="Matricula no encontrada")
    db.delete(matricula)
    db.commit()
    notify("deleted", id_matricula, user)
    return envelope({"id_matricula": id_matricula}, "Matricula eliminada")

@app.get("/health")
def health():
    return envelope({"service": "resource-service", "status": "UP"})