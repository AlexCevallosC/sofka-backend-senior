-- Creación de la tabla persona
CREATE TABLE persona (
    identificacion VARCHAR(255) PRIMARY KEY,
    nombre VARCHAR(255),
    genero VARCHAR(255),
    edad INTEGER,
    direccion VARCHAR(255),
    telefono VARCHAR(255)
);

-- Creación de la tabla clientea
CREATE TABLE cliente (
    contrasena VARCHAR(255),
    estado BOOLEAN NOT NULL,
    identificacion VARCHAR(255) PRIMARY KEY,
    FOREIGN KEY (identificacion) REFERENCES persona(identificacion)
);

-- Inserts iniciales
INSERT INTO persona (identificacion, nombre, genero, edad, direccion, telefono) VALUES
('1054878', 'Jose Lema', 'M', 30, 'Otavalo sn y principal', '098254785'),
('1054879', 'Marianela Montalvo', 'F', 29, 'Amazonas y NNUU', '097898745'),
('1054880', 'Juan Osorio', 'M', 45, '13 de abril y via principal', '097548965');

INSERT INTO cliente (identificacion, contrasena, estado) VALUES
('1054878', '1234', TRUE),
('1054879', '5678', TRUE),
('1054880', '1245', TRUE);