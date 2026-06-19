#!/bin/bash
BASE="http://localhost:8080"

echo "=============================="
echo "FLUJO 1: register -> login -> JWT"
echo "=============================="
curl.exe -s -X POST "$BASE/api/auth/register" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"demo\",\"password\":\"Demo123*\"}"
echo ""

RESPONSE=$(curl.exe -s -X POST "$BASE/api/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"demo\",\"password\":\"Demo123*\"}")
echo $RESPONSE
TOKEN=$(echo $RESPONSE | python -c "import sys,json; print(json.load(sys.stdin)['data']['access_token'])")
echo "Token obtenido: $TOKEN"

echo ""
echo "=============================="
echo "FLUJO 2: sin token (401) -> con token (200) -> POST -> PUT -> DELETE"
echo "=============================="
echo "--- GET sin token (debe dar 401) ---"
curl.exe -s -o /dev/null -w "HTTP %{http_code}\n" "$BASE/api/resources"

echo "--- GET con token (debe dar 200) ---"
curl.exe -s -o /dev/null -w "HTTP %{http_code}\n" "$BASE/api/resources" \
  -H "Authorization: Bearer $TOKEN"

echo "--- POST crear matricula ---"
curl.exe -s -X POST "$BASE/api/resources" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"estudiante\":\"Juan Perez\",\"grado\":\"5to\",\"ano_lectivo\":\"2026-2027\"}"
echo ""

echo "--- PUT editar matricula ---"
curl.exe -s -X PUT "$BASE/api/resources/1" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"estudiante\":\"Juan Perez Actualizado\",\"grado\":\"5to\",\"ano_lectivo\":\"2026-2027\"}"
echo ""

echo "--- DELETE eliminar matricula ---"
curl.exe -s -X DELETE "$BASE/api/resources/1" \
  -H "Authorization: Bearer $TOKEN"
echo ""

echo ""
echo "=============================="
echo "FLUJO 3: crear recurso -> verificar notificacion"
echo "=============================="
curl.exe -s -X POST "$BASE/api/resources" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"estudiante\":\"Maria Lopez\",\"grado\":\"3ro\",\"ano_lectivo\":\"2026-2027\"}"
echo ""

echo "--- Verificar notificacion generada ---"
curl.exe -s "$BASE/api/notifications"
echo ""
echo "=============================="
echo "FLUJOS COMPLETADOS"
echo "=============================="