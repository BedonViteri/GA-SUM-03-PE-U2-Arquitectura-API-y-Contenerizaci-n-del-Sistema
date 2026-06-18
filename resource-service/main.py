import os
import datetime as dt
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from sqlalchemy import create_engine, Column, Integer, String, Date
from sqlalchemy.orm import declarative_base, sessionmaker

DB_URL = os.getenv("DATABASE_URL", "sqlite:///./resources.db")

connect_args = {"check_same_thread": False} if DB_URL.startswith("sqlite") else {}
engine = create_engine(DB_URL, connect_args=connect_args)
Session = sessionmaker(bind=engine)
Base = declarative_base()


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


@app.get("/resources/matriculas")
def listar_matriculas():
    db = Session()
    matriculas = db.query(Matricula).order_by(Matricula.id_matricula.desc()).all()
    data = [serialize(m) for m in matriculas]
    return envelope(data, "Matriculas obtenidas")


@app.post("/resources/matriculas")
def crear_matricula(m: MatriculaInput):
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
    return envelope(serialize(nueva), "Matricula registrada")


@app.put("/resources/matriculas/{id_matricula}")
def editar_matricula(id_matricula: int, m: MatriculaUpdate):
    db = Session()
    matricula = db.query(Matricula).filter_by(id_matricula=id_matricula).first()
    if not matricula:
        raise HTTPException(status_code=404, detail="Matricula no encontrada")

    datos = m.dict(exclude_unset=True)
    for campo, valor in datos.items():
        setattr(matricula, campo, valor)

    db.commit()
    db.refresh(matricula)
    return envelope(serialize(matricula), "Matricula actualizada")


@app.delete("/resources/matriculas/{id_matricula}")
def eliminar_matricula(id_matricula: int):
    db = Session()
    matricula = db.query(Matricula).filter_by(id_matricula=id_matricula).first()
    if not matricula:
        raise HTTPException(status_code=404, detail="Matricula no encontrada")

    db.delete(matricula)
    db.commit()
    return envelope({"id_matricula": id_matricula}, "Matricula eliminada")


@app.get("/health")
def health():
    return envelope({"service": "resource-service", "status": "UP"})