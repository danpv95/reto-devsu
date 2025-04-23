
# DEVSU Microservicios - Prueba Técnica

Este proyecto contiene dos microservicios construidos en Spring Boot:
- **cliente-persona-service**: gestiona clientes.
- **cuenta-movimiento-service**: gestiona cuentas y movimientos, validando clientes vía REST.

## 🚀 Despliegue Local

### Requisitos

- Java 17
- Maven 3.8+
- Docker + Docker Compose

### Docker

```bash
cd docker-postgres
docker-compose up -d
```

La base de datos PostgreSQL quedará disponible en `localhost:5432`.

## 🛠️ Perfiles

- `dev`: usa base H2 en memoria.
- `qa`: se conecta a PostgreSQL dockerizado.

## 🧪 Pruebas

### Unitarias & Integración (MockMvc)

```bash
./mvnw test
```

### Karate (Java)

```bash
./mvnw test -Dtest=karate.KarateTestRunner
```

## 🧪 Postman

Colección disponible en `/postman/DEVSU.postman_collection.json`.

## 🔗 Comunicación entre servicios

`cuenta-movimiento-service` llama a `cliente-persona-service` vía RestTemplate para validar cliente existente.

## 🐳 Puertos

| Servicio | Puerto |
|----------|--------|
| cliente-persona-service | 8081 |
| cuenta-movimiento-service | 8082 |
| PostgreSQL | 5432 |

## 🧾 Generación de datos

Script SQL en `docker-postgres/BaseDatos.sql`.
