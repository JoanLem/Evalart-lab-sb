# Wrapper XML JSON

API Spring Boot para:
- recibir pedidos en JSON,
- transformarlos a XML,
- enviar el XML a un endpoint externo,
- y devolver la respuesta transformada nuevamente a JSON.

## Tecnologias

- Java 21
- Spring Boot 4
- Maven
- Springdoc OpenAPI (Swagger UI)
- Docker / Docker Compose

## Ejecutar local

### Requisitos

- JDK 21
- Maven (o usar `mvnw`)

### Comandos

```bash
./mvnw spring-boot:run
```

La aplicacion inicia por defecto en:
- `http://localhost:8081`

## Variables de entorno

Puedes configurar:

- `SERVER_PORT` (default: `8081`)
- `ENDPOINT_URL` (default: `https://acme-test.free.beeceptor.com/sent-order`)

Ejemplo:

```bash
SERVER_PORT=8082 ENDPOINT_URL=https://mi-endpoint.com/pedido ./mvnw spring-boot:run
```

## Documentacion Swagger

Con la aplicacion encendida:

- UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

## Endpoints principales

### 1) Procesar pedido

- **POST** `/api/pedidos/procesar`
- Request JSON:

```json
{
  "enviarPedido": {
    "numPedido": "P-100",
    "cantidadPedido": 2,
    "codigoEAN": "7701234567890",
    "nombreProducto": "Teclado",
    "numDocumento": "1234567890",
    "direccion": "Calle 123 #45-67"
  }
}
```

- Response JSON (ejemplo):

```json
{
  "enviarPedidoRespuesta": {
    "codigoEnvio": "COD-77",
    "estado": "RECIBIDO"
  }
}
```

### 2) Previsualizar XML

- **POST** `/api/pedidos/preview-xml`
- Request: mismo JSON del endpoint anterior.
- Response: XML generado.

## Ejecutar con Docker

### Build y run con Compose

```bash
docker compose up --build
```

En segundo plano:

```bash
docker compose up -d --build
```

Detener:

```bash
docker compose down
```

## Pruebas

Compilar pruebas:

```bash
./mvnw -q test-compile
```

Ejecutar pruebas:

```bash
./mvnw test
```

Si falla por version de Java, valida que tu `JAVA_HOME` apunte a JDK 21.
