# GA-SUM-03 PE-U2 — Sistema de Microservicios SGA

## Descripcion
Sistema de microservicios REST con autenticacion JWT, API Gateway (Nginx) y Docker Compose.
Proyecto Fin de Curso — Escuela Provincias Unidas.

## Microservicios
- auth-service (puerto 8001) — autenticacion JWT
- resource-service (puerto 8002) — CRUD matriculas
- notification-service (puerto 8003) — registro de eventos

## Bases de datos
Cada microservicio tiene su propia base de datos PostgreSQL (patron Database per Service):
- auth-db
- resource-db
- notification-db

## Como correr el sistema
1. Copia el archivo de variables de entorno:
   cp .env.example .env

2. Levanta todos los servicios:
   docker-compose up --build

3. Verifica que todo esta corriendo:
   docker-compose ps

## URLs
- API Gateway: http://localhost:8080
- Portainer: http://localhost:9000
- auth-service docs: http://localhost:8001/docs
- resource-service docs: http://localhost:8002/docs
- notification-service docs: http://localhost:8003/docs

## Repositorio
https://github.com/BedonViteri/GA-SUM-03-PE-U2-Arquitectura-API-y-Contenerizaci-n-del-Sistema