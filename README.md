
<p align="center">
  <img src="https://img.shields.io/badge/Java-17-blue.svg" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring_Boot-3.4.4-green.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Docker–Compose-blue.svg" alt="Docker Compose">
  <img src="https://img.shields.io/badge/JaCoCo–80%25-green.svg" alt="JaCoCo Coverage">
</p>

# 🏦 DEVSU Microservicios Transaccionales – Prueba Técnica

> **“Simulamos un mini-banco: gestión de clientes, cuentas y operaciones financieras, con dos microservicios Spring Boot y una base de datos PostgreSQL en contenedores.”**

Este proyecto representa la simulación de una entidad financiera a pequeña escala, estructurada bajo principios de microservicios. Cada servicio cumple un rol crítico en el flujo de datos:

- `cliente-persona-service`: administra el ciclo de vida del cliente.
- `cuenta-movimiento-service`: gestiona cuentas, transacciones y validación con clientes mediante REST.

---

## 🧰 Stack Tecnológico

- **Java 17**: Versión LTS que habilita modernizaciones como `records`, `pattern matching`, y `sealed classes`.
- **Spring Boot 3.4.4**: Framework que simplifica el desarrollo de servicios REST y configuración empresarial.
- **PostgreSQL 14+**: Motor de base de datos relacional para entornos QA (dockerizado).
- **H2 Database**: Base embebida para pruebas en perfil `dev`.
- **Maven 3.8+**: Sistema de gestión de dependencias y ciclo de vida del proyecto.
- **Docker + Docker Compose**: Contenedores para base de datos y aislamiento de infraestructura.
- **Karate**: Framework E2E para pruebas de integración tipo BDD.
- **JaCoCo**: Medición de cobertura de pruebas.

---

## 📐 Arquitectura & Flujo

```mermaid
flowchart LR
  subgraph Cliente-Persona-Service [Cliente-Persona-Service (8081)]
    A1[REST API] --> A2[Entidad Cliente]
  end

  subgraph Cuenta-Movimiento-Service [Cuenta-Movimiento-Service (8082)]
    B1[REST API] --> B2[Entidad Cuenta]
    B1 --> B3[Entidad Movimiento]
    B1 --> B4[Valida Cliente vía REST]
  end

  subgraph PostgreSQL [PostgreSQL (5432)]
    DB[(Base de Datos)]
  end

  Cliente-Persona-Service --> DB
  Cuenta-Movimiento-Service --> DB
  Cuenta-Movimiento-Service --> Cliente-Persona-Service
```

---

## 📂 Estructura del Proyecto

```
DEVSU/
├── docker-devsu/
│   ├── docker-compose.yml         # PostgreSQL + init.sql + healthcheck
│   └── init.sql                   # DDL + datos semilla
│
├── cliente-persona-service/       # Service A
│   ├── src/
│   │   ├── main/java/...          # Controllers, Services, Repos, Entities, DTOs
│   │   ├── main/resources/
│   │   │   ├── application-dev.properties   # H2 in-memory (dev)
│   │   │   ├── application-qa.properties    # PostgreSQL (qa)
│   │   │   └── application.properties       # common
│   │   └── test/resources/e2e/
│   │       ├── health.feature
│   │       └── karate-config.js
│   └── pom.xml
│
├── cuenta-movimiento-service/     # Service B (estructura similar)
│
├── DEVSU.postman_collection.json  # Colección para Postman
└── README.md
```

---

## 🛠️ Perfiles y Configuración

| Perfil | Base de Datos | Descripción |
|--------|----------------|-------------|
| `dev`  | H2 (memoria)   | Ideal para pruebas locales rápidas |
| `qa`   | PostgreSQL     | Dockerizado, se conecta vía JDBC |

Se selecciona con:  
```bash
-Dspring.profiles.active=dev
```

---

## 🚀 Despliegue Paso a Paso

```bash
# 1. Clonar el repositorio
git clone https://github.com/danpv95/reto-devsu.git
cd reto-devsu

# 2. Levantar infraestructura con Docker Compose
cd docker-devsu
docker compose up -d

# 3. Configurar JDK 17 (macOS)
export JAVA_HOME=$(/usr/libexec/java_home -v17)

# 4. Ejecutar cliente-persona-service (8081)
cd ../cliente-persona-service
mvn clean verify -Dspring.profiles.active=qa -DbaseUrl=http://localhost:8081

# 5. Ejecutar cuenta-movimiento-service (8082)
cd ../cuenta-movimiento-service
mvn clean verify -Dspring.profiles.active=qa -DbaseUrl=http://localhost:8082
```

---

## 📊 Endpoints REST

### Servicio 1: Clientes (8081)

- `POST /api/clientes` – Crear cliente
- `GET /api/clientes` – Listar clientes
- `GET /api/clientes/{id}` – Consultar por ID
- `PUT /api/clientes/{id}` – Actualizar
- `DELETE /api/clientes/{id}` – Eliminar

### Servicio 2: Cuentas y Movimientos (8082)

- `POST /api/cuentas` – Crear cuenta
- `POST /api/movimientos` – Crear movimiento
- `GET /api/movimientos/cuenta/{id}` – Movimientos por cuenta
- `GET /api/movimientos/cuenta/{id}/reporte?inicio=...&fin=...` – Reporte financiero

---

## 🧪 Pruebas y Cobertura

### A. Unitarias + MockMvc
```bash
mvn test
```

### B. Health Check con Karate
```bash
mvn verify -DbaseUrl=http://localhost:8081
mvn verify -DbaseUrl=http://localhost:8082
```

### C. JaCoCo Coverage
```bash
mvn verify
# Ver HTML: target/site/jacoco/index.html
```

---

## 📦 init.sql

Contiene:

- Creación de tablas: `clientes`, `cuentas`, `movimientos`
- Datos iniciales para pruebas (semillas)

---

## 🔄 Comunicación Interservicios

`cuenta-movimiento-service` usa `RestTemplate` para validar clientes antes de permitir movimientos o creación de cuentas.

- Retorna `404` si el cliente no existe.
- Manejo de errores con `IllegalArgumentException`.

---

## 📬 Postman

Importa `DEVSU.postman_collection.json` para probar todos los endpoints:

- CRUD de Clientes
- Movimientos
- Reportes

---

## ⚙️ CI/CD (GitHub Actions)

Incluye `.github/workflows/ci.yml` para:

```yaml
- Ejecutar `mvn verify` en push/pull-request
- Validar construcción en ambos servicios
- Generar reporte de cobertura
```

---

## ✅ Checklist de Entrega

- [x] CRUD de Clientes
- [x] CRUD de Cuentas
- [x] Registro de Movimientos con validación
- [x] Reporte por fechas
- [x] Docker Compose + PostgreSQL
- [x] Pruebas unitarias y Karate health
- [x] Reporte JaCoCo ≥ 80%
- [x] Documentación técnica completa
- [x] CI/CD automatizado con GitHub Actions
