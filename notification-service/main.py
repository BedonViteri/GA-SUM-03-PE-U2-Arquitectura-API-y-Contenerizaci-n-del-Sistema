import os, datetime as dt
from fastapi import FastAPI
from pydantic import BaseModel
from sqlalchemy import create_engine, Column, Integer, String, DateTime
from sqlalchemy.orm import declarative_base, sessionmaker

DB_URL = os.getenv("DATABASE_URL", "postgresql://sga_user:sga_pass@notification-db:5432/notifications_db")

engine = create_engine(DB_URL)
Session = sessionmaker(bind=engine)
Base = declarative_base()


class Notificacion(Base):
    __tablename__ = "notificaciones"
    id = Column(Integer, primary_key=True)
    event = Column(String, nullable=False)
    resource_id = Column(Integer, nullable=True)
    user = Column(String, nullable=True)
    created_at = Column(DateTime, default=dt.datetime.utcnow)


Base.metadata.create_all(engine)

app = FastAPI(title="notification-service")


def envelope(data=None, message="OK", status="success"):
    return {
        "status": status,
        "data": data,
        "message": message,
        "timestamp": dt.datetime.utcnow().isoformat() + "Z"
    }


class NotificacionInput(BaseModel):
    event: str
    resource_id: int | None = None
    user: str | None = None


@app.post("/notifications")
def crear_notificacion(n: NotificacionInput):
    db = Session()
    nueva = Notificacion(event=n.event, resource_id=n.resource_id, user=n.user)
    db.add(nueva)
    db.commit()
    db.refresh(nueva)
    return envelope(
        {
            "id": nueva.id,
            "event": nueva.event,
            "resource_id": nueva.resource_id,
            "user": nueva.user,
            "created_at": nueva.created_at.isoformat() + "Z"
        },
        "Notificacion registrada"
    )


@app.get("/notifications")
def listar_notificaciones():
    db = Session()
    notificaciones = db.query(Notificacion).order_by(Notificacion.created_at.desc()).all()
    data = [
        {
            "id": x.id,
            "event": x.event,
            "resource_id": x.resource_id,
            "user": x.user,
            "created_at": x.created_at.isoformat() + "Z"
        }
        for x in notificaciones
    ]
    return envelope(data, "Notificaciones obtenidas")


@app.get("/health")
def health():
    return envelope({"service": "notification-service", "status": "UP"})