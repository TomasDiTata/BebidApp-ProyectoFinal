DROP database IF EXISTS Bebidapp;
CREATE database Bebidapp;
USE Bebidapp;

CREATE TABLE usuarios(
	id_usuario INT, 
    dni VARCHAR(30) NOT NULL UNIQUE, 
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    contrasena VARCHAR(100) NOT NULL UNIQUE, 
    email VARCHAR(50) NOT NULL UNIQUE, /*Usuario*/
    telefono VARCHAR(15) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL
);

ALTER TABLE usuarios
MODIFY id_usuario INT AUTO_INCREMENT PRIMARY KEY;

CREATE TABLE roles(
	id_rol INT,
    rol enum('Administrador','Vendedor','Cliente'),
    descripcion VARCHAR(50) NOT NULL
);

ALTER TABLE roles
MODIFY id_rol INT AUTO_INCREMENT PRIMARY KEY;

CREATE TABLE usuarioRol(
	id_usuarioRol INT,
    id_usuario INT,
    id_rol INT
);

ALTER TABLE usuarioRol
MODIFY id_usuarioRol INT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE usuarioRol ADD foreign key (id_usuario)
REFERENCES usuarios (id_usuario);

ALTER TABLE usuarioRol ADD foreign key (id_rol)
REFERENCES roles (id_rol);

CREATE TABLE productos(
	id_producto INT,
    nombre VARCHAR(20) NOT NULL,
    descripcion VARCHAR(50) NOT NULL,
    categoria VARCHAR(20) NOT NULL,
    stock INT NOT NULL
    check (stock >= 0), 
    precio_unitario DECIMAL(10,2) NOT NULL
    check (precio_unitario >= 0)		
);

ALTER TABLE productos
MODIFY id_producto INT AUTO_INCREMENT PRIMARY KEY;

CREATE TABLE ventas(
	id_venta INT,
    fecha DATETIME NOT NULL,
    id_cliente INT NOT NULL,
    total DECIMAL(30,2) NOT NULL
);

ALTER TABLE ventas
MODIFY id_venta INT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE ventas ADD foreign key (id_cliente)
REFERENCES usuarios (id_usuario);


CREATE TABLE detalle_ventas(
	id_venta INT,
    id_producto INT, 
    cantidad INT,
    precio_unitario DECIMAL(10,2),
    subtotal DECIMAL(15,2)
);

ALTER TABLE detalle_ventas ADD PRIMARY KEY (id_venta, id_producto);

ALTER TABLE detalle_ventas ADD foreign key (id_venta)
REFERENCES ventas (id_venta);

ALTER TABLE detalle_ventas ADD foreign key (id_producto)
REFERENCES productos (id_producto);

CREATE TABLE pagos(
	id_pago INT,
    id_venta INT,
    metodo_pago enum('EFECTIVO', 'TARJETA_DEBITO', 'TARJETA_CREDITO', 'TRANSFERENCIA'),
    monto DECIMAL(30,2),
    recargo_descuento DECIMAL(10,2),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE pagos
MODIFY id_pago INT AUTO_INCREMENT PRIMARY KEY;

ALTER TABLE pagos ADD foreign key (id_venta)
REFERENCES ventas (id_venta);

/* Insercion de roles base necesarios para que la app funcione */
INSERT INTO roles (rol, descripcion) VALUES ('Administrador', 'Administrador del sistema');
INSERT INTO roles (rol, descripcion) VALUES ('Vendedor', 'Empleado encargado de los productos');
INSERT INTO roles (rol, descripcion) VALUES ('Cliente', 'Cliente registrado');

/*Insercion de nuestros productos*/

INSERT INTO productos (nombre, descripcion, categoria, stock, precio_unitario) VALUES 
('Coca Cola Original', 'Gaseosa sabor cola 2.25L', 'Gaseosas', 250, 2500.00),
('Sprite Lima Limón', 'Gaseosa sabor lima limón 2.25L', 'Gaseosas', 140, 2400.00),
('Fernet Branca', 'Aperitivo de hierbas 750ml', 'Aperitivos', 100, 9500.00),
('Cerveza Quilmes', 'Cerveza rubia clásica 1L', 'Cervezas', 200, 5800.00),
('Vino Malbec Rutini', 'Vino tinto cosecha especial', 'Vinos', 130, 15000.00),
('Agua Mineral', 'Agua sin gas villavicencio 2L', 'Agua', 360, 1200.00),
('Jugo de Naranja', 'Jugo natural exprimido 1L', 'Jugos', 220, 3000.00),
('Smirnoff clasico', 'Vodka Smirnoff 700ml', 'Vodkas', 140, 9000.00);

