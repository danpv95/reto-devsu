-- =========================================================
-- ==========  ESQUEMA  clientesdb  ========================
-- =========================================================
\connect clientesdb;

-- Secuencia implícita creada por BIGSERIAL → se llama cliente_id_seq
CREATE TABLE IF NOT EXISTS cliente (
    id              BIGSERIAL PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL,
    apellido        VARCHAR(100) NOT NULL,
    identificacion  VARCHAR(20)  NOT NULL UNIQUE,
    direccion       VARCHAR(255),
    telefono        VARCHAR(30),
    estado          VARCHAR(20)  NOT NULL,
    fecha_registro  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Índice de búsqueda rápida por estado (opcional)
CREATE INDEX IF NOT EXISTS idx_cliente_estado ON cliente(estado);

-- =========================================================
-- ==========  ESQUEMA  cuentasdb  =========================
-- =========================================================
\connect cuentasdb;

CREATE TABLE IF NOT EXISTS cuenta (
    id              BIGSERIAL PRIMARY KEY,
    numero_cuenta   VARCHAR(20)  NOT NULL UNIQUE,
    tipo_cuenta     VARCHAR(20),
    saldo_inicial   NUMERIC(18,2) NOT NULL,
    estado          VARCHAR(20)   NOT NULL,
    cliente_id      BIGINT        NOT NULL,          -- referencia lógica
    fecha_apertura  TIMESTAMP     NOT NULL DEFAULT NOW()
    -- (Opcional) FOREIGN KEY cliente_id REFERENCES cliente(id)
    --  ↳ se omite porque cada micro-servicio maneja su propio esquema
);

CREATE TABLE IF NOT EXISTS movimiento (
    id              BIGSERIAL PRIMARY KEY,
    cuenta_id       BIGINT        NOT NULL REFERENCES cuenta(id) ON DELETE CASCADE,
    tipo_movimiento VARCHAR(20)   NOT NULL,          -- DEPOSITO | RETIRO
    valor           NUMERIC(18,2) NOT NULL,
    saldo           NUMERIC(18,2) NOT NULL,
    fecha           TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- Índices para reportes por fecha y cuenta
CREATE INDEX IF NOT EXISTS idx_movimiento_fecha     ON movimiento(fecha);
CREATE INDEX IF NOT EXISTS idx_movimiento_cuenta_id ON movimiento(cuenta_id);
