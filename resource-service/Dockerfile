# ETAPA 1: builder
FROM python:3.11-slim AS builder

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir --user -r requirements.txt


# ETAPA 2: runtime
FROM python:3.11-slim AS runtime

WORKDIR /app

COPY --from=builder /root/.local /root/.local
COPY main.py .

ENV PATH=/root/.local/bin:$PATH
ENV PYTHONUNBUFFERED=1

EXPOSE 8002

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8002"]