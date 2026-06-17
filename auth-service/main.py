import os, datetime as dt
from fastapi import FastAPI, HTTPException, Header
from pydantic import BaseModel
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import declarative_base, sessionmaker
from passlib.context import CryptContext
from jose import jwt, JWTError

SECRET = os.getenv("JWT_SECRET", "change-me")
ALGO = "HS256"
EXP_MIN = int(os.getenv("JWT_EXP_MIN", "60"))
DB_URL = os.getenv("DATABASE_URL", "sqlite:///./users.db")

engine = create_engine(DB_URL, connect_args={"check_same_thread": False})
Session = sessionmaker(bind=engine)
Base = declarative_base()
pwd = CryptContext(schemes=["bcrypt"], deprecated="auto")

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True)
    username = Column(String, unique=True, index=True)
    password_hash = Column(String)

Base.metadata.create_all(engine)


app = FastAPI(title="auth-service")

def envelope(data=None, message="OK", status="success"):
    return {
        "status": status,
        "data": data,
        "message": message,
        "timestamp": dt.datetime.utcnow().isoformat() + "Z"
    }

class Credentials(BaseModel):
    username: str
    password: str


@app.post("/auth/register")
def register(c: Credentials):
    db = Session()
    if db.query(User).filter_by(username=c.username).first():
        raise HTTPException(status_code=409, detail="Usuario ya existe")
    db.add(User(username=c.username, password_hash=pwd.hash(c.password)))
    db.commit()
    return envelope({"username": c.username}, "Usuario registrado")

@app.post("/auth/login")
def login(c: Credentials):
    db = Session()
    u = db.query(User).filter_by(username=c.username).first()
    if not u or not pwd.verify(c.password, u.password_hash):
        raise HTTPException(status_code=401, detail="Credenciales invalidas")
    exp = dt.datetime.utcnow() + dt.timedelta(minutes=EXP_MIN)
    token = jwt.encode({"sub": c.username, "exp": exp}, SECRET, algorithm=ALGO)
    return envelope(
        {"access_token": token, "token_type": "bearer", "expires_in": EXP_MIN * 60},
        "Login correcto"
    )

@app.get("/auth/validate")
def validate(authorization: str = Header(default="")):
    token = authorization.replace("Bearer ", "")
    try:
        payload = jwt.decode(token, SECRET, algorithms=[ALGO])
    except JWTError:
        raise HTTPException(status_code=401, detail="Token invalido o expirado")
    return envelope({"username": payload["sub"]}, "Token valido")

@app.get("/health")
def health():
    return envelope({"service": "auth-service", "status": "UP"})