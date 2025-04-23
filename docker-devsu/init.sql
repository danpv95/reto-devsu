-- 1. Base para el micro-servicio de clientes
CREATE DATABASE clientesdb
  WITH ENCODING = 'UTF8'
       LC_COLLATE = 'en_US.utf8'
       LC_CTYPE   = 'en_US.utf8'
       TEMPLATE   = template0
       CONNECTION LIMIT = -1;

-- 2. Base para el micro-servicio de cuentas y movimientos
CREATE DATABASE cuentasdb
  WITH ENCODING = 'UTF8'
       LC_COLLATE = 'en_US.utf8'
       LC_CTYPE   = 'en_US.utf8'
       TEMPLATE   = template0
       CONNECTION LIMIT = -1;
