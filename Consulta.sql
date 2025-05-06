CREATE DATABASE IF NOT EXISTS dados_y_tragos;
USE dados_y_tragos;

CREATE TABLE juegos (
id_juego INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
descripcion TEXT,
categoria VARCHAR(50),
min_jugadores INT,
max_jugadores INT,
edad_minima INT,
disponible BOOLEAN DEFAULT TRUE,
fecha_adquisicion DATE,
valor_adquisicion DECIMAL(5,2) DEFAULT 0.00,
ruta_imagen VARCHAR(255)
);

CREATE TABLE comidas (
id_comida INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
tipo VARCHAR(50),
precio DECIMAL(5,2),
stock INT,
descripcion TEXT,
es_vegetariana TINYINT(1) DEFAULT 0,
fecha_registro DATE
);

CREATE TABLE usuarios (
id_usuario INT AUTO_INCREMENT PRIMARY KEY,
usuario VARCHAR(50) NOT NULL UNIQUE,
role TINYINT DEFAULT 0,
contrasena VARCHAR(100) NOT NULL,
apellido VARCHAR(100),
email VARCHAR(150) UNIQUE,
fecha_registro DATE,
fecha_nacimiento DATE,
edad INT,
total_consumo DECIMAL(8,2) DEFAULT 0.00
);

CREATE TABLE bebidas (
id_bebida INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
tipo VARCHAR(50),
precio DECIMAL(5,2),
stock INT,
graduacion_alcoholica DECIMAL(5,2),
es_alcoholica TINYINT(1) DEFAULT 0,
fecha_registro DATE
);

CREATE TABLE reservas (
id_reserva INT AUTO_INCREMENT PRIMARY KEY,
id_usuario INT NOT NULL,
id_juego INT,
id_bebida INT,
id_comida INT,
fecha_reserva DATETIME,
num_personas INT,
total DECIMAL(7,2),
estado VARCHAR(20) DEFAULT 'pendiente',
notas TEXT COMMENT 'Información adicional de la reserva',
puede_alcohol TINYINT(1) DEFAULT 0,
FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
FOREIGN KEY (id_juego) REFERENCES juegos(id_juego),
FOREIGN KEY (id_bebida) REFERENCES bebidas(id_bebida),
FOREIGN KEY (id_comida) REFERENCES comidas(id_comida)
);

INSERT INTO juegos (nombre, descripcion, categoria, min_jugadores, max_jugadores, edad_minima, disponible, fecha_adquisicion, valor_adquisicion, ruta_imagen) VALUES
('Munchkin Clasico', 'Juego de cartas de humor y parodia, donde debes armarte para derrotar monstruos y traicionar amigos para poder ganar', 'Cartas', 3, 6, 12, TRUE, '2024-05-15', 25.99, NULL),
('Virus!', 'Juego de cartas donde debes conseguir un cuerpo sano e inmunizado, mientras tanto evita los virus de otros jugadores', 'Cartas', 6, 2, 8, TRUE, '2024-02-20', 14.95, NULL),
('Exploding Kittens', 'Juego de cartas con humor absurdo y gatos explosivos, la unica mision es no ser eliminado', 'Cartas', 2, 5, 10, TRUE, '2024-11-10', 19.99, NULL),
('Spoilers', 'Juego de retos que lleva el cine a la mesa', 'Party Game', 3, 6, 14, TRUE, '2024-08-05', 14.95, NULL),
('Catan', 'Juego de estrategia, construccion y comercio', 'Estrategia', 3, 4, 10, TRUE, '2024-01-15', 45.00, NULL),
('Dixit Clasico', 'Juego de imaginacion y creación de historias', 'Familiar', 3, 6, 8, TRUE, '2024-06-22', 32.99, NULL),
('Cthulhu Realms', 'Juego de construccion demazos derrota a tus rivales uno a uno dentro del mundo de lovecragt ', 'Construccion de mazos', 2, 4, 14, TRUE, '2024-03-10', 19.95, NULL),
('Unstable Unicorns', 'Juego de cartas que te hara crear un ejercito de unicornios peara derrotar a tu rivales', 'Cartas', 2, 8, 14, FALSE, '2024-01-05', 19.99, NULL),
('Sushi Go!', 'Juego de cartas donde los jugadores recolectan diferentes platos de sushi para obtener puntos', 'Cartas', 2, 5, 8, TRUE, '2024-09-01', 12.99, NULL),
('Carcassonne', 'Juego de colocación de losetas donde los jugadores construyen paisajes medievales', 'Estrategia', 2, 5, 7, TRUE, '2024-04-20', 34.95, NULL),
('Love Letter', 'Juego de cartas de deducción social con pocas cartas pero mucha estrategia', 'Cartas', 2, 4, 10, TRUE, '2024-06-03', 9.99, NULL);

INSERT INTO bebidas (nombre, tipo, precio, stock, graduacion_alcoholica, es_alcoholica, fecha_registro) VALUES
('Coca Cola', 'Sin alcohol', 2.50, 180, 0.00, FALSE, '2024-11-15'),
('Kas Limon', 'Sin alcohol', 2.50, 180, 0.00, FALSE, '2024-12-15'),
('Nestea', 'Sin alcohol', 2.50, 180, 0.00, FALSE, '2024-11-15'),
('Ambar', 'Cerveza', 1.50, 150, 7.00, TRUE, '2024-11-15'),
('Radler', 'Cerveza', 1.50, 150, 2.50, TRUE, '2024-11-15'),
('Tinto de Verano', 'Coctel', 3.00, 100, 5.00, TRUE, '2025-04-23'),
('Ron Brugal', 'Coctel', 7.50, 70, 38.00, TRUE, '2025-04-23'),
('Cacaolat', 'Sin alcohol', 2.50, 80, 0.00, FALSE, '2025-04-23'),
('Fanta Naranja', 'Sin alcohol', 2.50, 180, 0.00, FALSE, '2025-04-23'),
('Mahou Clásica', 'Cerveza', 1.50, 150, 5.50, TRUE, '2025-04-23');

INSERT INTO `comidas` (`id_comida`, `nombre`, `tipo`, `precio`, `stock`, `descripcion`, `es_vegetariana`, `fecha_registro`) VALUES
(1, 'Tortilla de patata', 'Racion', 5.00, 18, 'Tortilla de patata con cebolla', 1, '2025-03-31'),
(2, 'Bacanal de salchichas', 'Racion', 6.00, 20, 'Salchichas con salsa BBQ casera y mayonesa Sriracha', 0, '2025-03-31'),
(3, 'Tacos de pulled pork', 'Tacos', 8.00, 15, 'Tacos de masa de maíz con pulled pork, brotes tiernos y pico de gallo', 0, '2025-03-31'),
(4, 'Tacos veganos', 'Tacos', 8.00, 15, 'Tacos con dados de tofu, guacamole y pico de gallo', 1, '2025-03-31'),
(5, 'Pizza salchichas y bacon', 'Pizza', 11.00, 10, 'Pizza con salchichas y bacon sobre base de mozzarella y tomate', 0, '2025-03-31'),
(6, 'Pizza 4 quesos', 'Pizza', 12.00, 10, 'Pizza con mezcla de cuatro quesos sobre base de mozzarella y tomate', 1, '2025-03-31'),
(7, 'Pizza vegetariana', 'Pizza', 11.00, 10, 'Pizza con tomate cherry, cebolla morada, pimiento verde y queso curado de Mahón', 1, '2025-03-31'),
(8, 'Nachos con salsas', 'Racion', 10.50, 20, 'Nachos con pico de gallo y tres salsas: chili de carne, queso fresco y guacamole', 0, '2025-03-31'),
(9, 'Ensalada de burrata', 'Ensalada', 13.00, 10, 'Ensalada con burrata, jamón de pato, tomates cherry y vinagreta de miel y frutos rojos', 0, '2025-03-31'),
(10, 'Tarta de queso casera', 'Postre', 4.00, 15, 'Tarta de queso casera con mermelada de fresa', 1, '2025-03-31'),
(11, 'Tiramisú casero', 'Postre', 5.00, 15, 'Tiramisú casero', 1, '2025-03-31'),
(12, 'Patatas bravas', 'Ración', 4.50, 25, 'Patatas fritas en dados con salsa brava ligeramente picante y alioli', 1, '2025-03-31'),
(13, 'Bola de carne', 'Ración', 6.00, 15, 'Bola de patata con carne en su interior acompañada de un cacho de pan y salsa de lac asa', 0, '2025-03-31'),
(14, 'Ensalada aragonesa de la huerta', 'Ensalada', 7.50, 12, 'Tomate rosa de Barbastro, lechuga romana, cebolla dulce', 1, '2025-03-31'),
(15, 'Tarta de zanahoria casera', 'Postre', 4.50, 16, 'Bizcocho esponjoso de zanahoria con cobertura de crema de queso y nueces', 1, '2025-03-31');

INSERT INTO usuarios (usuario, contrasena, role, apellido, email, fecha_registro, fecha_nacimiento, edad, total_consumo) VALUES
('admin', SHA1('123'), '1', 'administrador', 'dyt@gmail.com','2024-04-01','1997-10-31', 27, 0.0 ),
('nerea', SHA1('123'), '0', 'administrador', 'nerea@gmail.com','2024-04-01','1997-10-31', 27, 0.0 ),
('Carlos', SHA1('123'), '0' , 'Garcia', 'carlos@gmail.com', '2025-04-05', '1995-06-15', 29, 125.50),
('Lucia', SHA1('123'), '0', 'Sancho', 'luciao@gmail.com', '2025-04-06', '1992-03-22', 33, 80.00),
('Javier', SHA1('123'), '0', 'Gallardo', 'javier@gmail.com', '2025-04-06', '1988-11-30', 36, 210.25),
('Marta', SHA1('123'), '0', 'Paesa', 'marta@gmail.com', '2025-04-09', '1999-04-10', 26, 65.30),
('Ana', SHA1('123'), '0', 'Torres', 'ana@gmail.com', '2025-04-10', '1990-12-01', 34, 150.75);