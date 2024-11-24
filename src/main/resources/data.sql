-- Insertar roles si no existen
INSERT INTO roles (name)
SELECT 'ROLE_USER'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name)
SELECT 'ROLE_ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

-- Insertar admin si no existe
INSERT INTO users (user_name, email, password, phone_number, seller, user_image)
SELECT 'admin', 'admin@admin.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920412', true, 'image.png'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_name = 'admin');

INSERT INTO users_roles (user_id, rol_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.user_name = 'admin'
  AND r.name = 'ROLE_ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM users_roles
    WHERE user_id = u.id AND rol_id = r.id
);

--Insertar Categorias de productos
INSERT INTO public.product_categories(
    product_category_id, category_name, category_description, category_image)
VALUES
    (1, 'Tecnología', 'Productos electrónicos, accesorios de computadora, gadgets y más', 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/iPhone+14+Pro+Max+Como+Nuevo.webp'),
    (2, 'Libros y Material Académico', 'Libros de texto, guías de estudio, materiales y útiles escolares', 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/iPhone+14+Pro+Max+Como+Nuevo2.webp'),
    (3, 'Ropa y Accesorios', 'Ropa casual, deportiva, accesorios de moda y calzado', 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/iPhone+14+Pro+Max+Como+Nuevo3.webp'),
    (4, 'Hogar y Decoración', 'Artículos para el hogar, decoración y muebles pequeños', 'home.png'),
    (5, 'Deportes', 'Equipamiento deportivo, ropa deportiva y accesorios para ejercicio', 'sports.png'),
    (6, 'Salud y Belleza', 'Productos de cuidado personal, cosméticos, suplementos y bienestar', 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/iPhone+12+Apple+(128+Gb)+-+Blanco+Audífonos+Cable3.webp'),
    (7, 'Juguetes y Juegos', 'Juguetes educativos, juegos de mesa, videojuegos y artículos para niños', 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/iPhone+12+Apple+(128+Gb)+-+Blanco+Audífonos+Cable2.webp'),
    (8, 'Otros', 'Productos variados que no se ajustan a las categorías anteriores', 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/iPhone+12+Apple+(128+Gb)+-+Blanco+Audífonos+Cable.webp')
ON CONFLICT (product_category_id) DO NOTHING;  -- Evita duplicados

----------------------------------------------------------------------------------------------------------

--Usuarios de prueba
-- Inserción de 10 usuarios de prueba
INSERT INTO users (user_name, email, password, phone_number, seller, user_image)
VALUES
    ('user1', 'user1@example.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920413', false, 'user1.png'),
    ('user2', 'user2@example.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920414', true, 'user2.png'),
    ('user3', 'user3@example.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920415', false, 'user3.png'),
    ('user4', 'user4@example.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920416', true, 'user4.png'),
    ('user5', 'user5@example.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920417', false, 'user5.png'),
    ('user6', 'user6@example.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920418', true, 'user6.png'),
    ('user7', 'user7@example.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920419', false, 'user7.png'),
    ('user8', 'user8@example.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920420', true, 'user8.png'),
    ('user9', 'user9@example.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920421', false, 'user9.png'),
    ('user10', 'user10@example.com', '$2a$10$VReY3QH7imZ29.gAVgiy3.nPWVAMTE6o5t0YaZ32xB05lohtygByO', '3204920422', true, 'user10.png')
    ON CONFLICT (email) DO NOTHING; -- Evita duplicados basados en el email
--Inser de en user_role
-- Asignar rol ROLE_USER a los usuarios de prueba
INSERT INTO users_roles (user_id, rol_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE r.name = 'ROLE_USER'
  AND u.email IN ('user1@example.com', 'user2@example.com', 'user3@example.com', 'user4@example.com', 'user5@example.com',
                  'user6@example.com', 'user7@example.com', 'user8@example.com', 'user9@example.com', 'user10@example.com')
  AND NOT EXISTS (
    SELECT 1 FROM users_roles ur
    WHERE ur.user_id = u.id AND ur.rol_id = r.id
);

-- Insertar productos
INSERT INTO public.products(
    is_new, is_sold, original_price, sale_price, product_category_id, product_id, user_id, product_name, product_description)
VALUES
    (true, false, 799900.00, 699900.00, 1, DEFAULT, (SELECT id FROM users WHERE email = 'user1@example.com'), 'Xiaomi POCO C65 128GB', 'Xiaomi POCO C65 Dual SIM, 128GB, 6GB RAM, color negro. Smartphone con excelente rendimiento y cámara de alta calidad.'),
    (true, false, 89900.00, 79900.00, 1, DEFAULT, (SELECT id FROM users WHERE email = 'user2@example.com'), 'Shield USB Host 2.0 Arduino', 'Shield USB Host 2.0 para proyectos Arduino y Google Android ADK. Compatible con múltiples dispositivos USB, ideal para desarrollo.'),
    (false, false, 159900.00, 129900.00, 2, DEFAULT, (SELECT id FROM users WHERE email = 'user3@example.com'), 'Termodinámica en Ingeniería Química', 'Libro de Termodinámica enfocado en Ingeniería Química. Excelente estado, contenido completo sobre principios termodinámicos y aplicaciones.'),
    (true, false, 79900.00, 69900.00, 4, DEFAULT, (SELECT id FROM users WHERE email = 'user4@example.com'), 'Rodillo Facial de Jade Premium', 'Set de Rodillo Facial Jade Gua Sha X2. Herramienta de masaje facial para relajación y cuidado de la piel. Incluye guía de uso.'),
    (true, false, 49900.00, 39900.00, 4, DEFAULT, (SELECT id FROM users WHERE email = 'user5@example.com'), 'Película Protectora para Vidrios', 'Película adhesiva para vidrios de 60cm de ancho. Ideal para privacidad y control de luz solar. Fácil instalación y durabilidad garantizada.'),
    (true, false, 29900.00, 24900.00, 4, DEFAULT, (SELECT id FROM users WHERE email = 'user6@example.com'), 'Antifaz para Dormir Premium', 'Antifaz suave y cómodo para dormir. Material de alta calidad, ajuste perfecto y diseño ergonómico para un descanso óptimo.'),
    (false, false, 189900.00, 149900.00, 2, DEFAULT, (SELECT id FROM users WHERE email = 'user7@example.com'), 'Histología Médica Ross 8va Edición', 'Libro de Histología Médica Ross, 8va Edición reacondicionado. Perfecto estado, ideal para estudiantes de medicina. Sin marcas ni anotaciones.'),
    (false, false, 199900.00, 169900.00, 6, DEFAULT, (SELECT id FROM users WHERE email = 'user8@example.com'), 'Set Pistolas Nerf Profesional', 'Juego completo de pistolas Nerf en excelente estado. Incluye dardos y accesorios. Ideal para juegos recreativos y actividades grupales.'),
    (false, false, 45900.00, 35900.00, 2, DEFAULT, (SELECT id FROM users WHERE email = 'user9@example.com'), 'Libro Disney Para Colorear - Edición Coleccionista', 'Libro para colorear Disney de Hachette Heroes, edición especial reacondicionada. Perfecto estado, ilustraciones de alta calidad de personajes clásicos.'),
    (false, false, 1299900.00, 1099900.00, 1, DEFAULT, (SELECT id FROM users WHERE email = 'user10@example.com'), 'Laptop ASUS X441N', 'Computador portátil ASUS X441N, perfecto para estudio y trabajo. Procesador eficiente, memoria RAM para multitarea y disco duro amplio.');

-- Insertar stock para los productos
INSERT INTO public.product_stocks(
    quantity, product_id, stock_id)
VALUES
    (10, (SELECT product_id FROM products WHERE product_name = 'Xiaomi POCO C65 128GB'), DEFAULT),
    (15, (SELECT product_id FROM products WHERE product_name = 'Shield USB Host 2.0 Arduino'), DEFAULT),
    (5, (SELECT product_id FROM products WHERE product_name = 'Termodinámica en Ingeniería Química'), DEFAULT),
    (20, (SELECT product_id FROM products WHERE product_name = 'Rodillo Facial de Jade Premium'), DEFAULT),
    (30, (SELECT product_id FROM products WHERE product_name = 'Película Protectora para Vidrios'), DEFAULT),
    (25, (SELECT product_id FROM products WHERE product_name = 'Antifaz para Dormir Premium'), DEFAULT),
    (10, (SELECT product_id FROM products WHERE product_name = 'Histología Médica Ross 8va Edición'), DEFAULT),
    (8, (SELECT product_id FROM products WHERE product_name = 'Set Pistolas Nerf Profesional'), DEFAULT),
    (12, (SELECT product_id FROM products WHERE product_name = 'Libro Disney Para Colorear - Edición Coleccionista'), DEFAULT),
    (6, (SELECT product_id FROM products WHERE product_name = 'Laptop ASUS X441N'), DEFAULT);

-- Insertar imágenes para los productos
INSERT INTO public.product_images(
    image_id, product_id, url)
VALUES
    (DEFAULT, (SELECT product_id FROM products WHERE product_name = 'Xiaomi POCO C65 128GB'), 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/xiaomi_poco_c65.webp'),
    (DEFAULT, (SELECT product_id FROM products WHERE product_name = 'Shield USB Host 2.0 Arduino'), 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/shield_usb_host.webp'),
    (DEFAULT, (SELECT product_id FROM products WHERE product_name = 'Termodinámica en Ingeniería Química'), 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/termodinamica_libro.webp'),
    (DEFAULT, (SELECT product_id FROM products WHERE product_name = 'Rodillo Facial de Jade Premium'), 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/rodillo_facial_jade.webp'),
    (DEFAULT, (SELECT product_id FROM products WHERE product_name = 'Película Protectora para Vidrios'), 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/pelicula_protectora_vidrios.webp'),
    (DEFAULT, (SELECT product_id FROM products WHERE product_name = 'Antifaz para Dormir Premium'), 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/antifaz_dormir.webp'),
    (DEFAULT, (SELECT product_id FROM products WHERE product_name = 'Histología Médica Ross 8va Edición'), 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/histologia_medica_libro.webp'),
    (DEFAULT, (SELECT product_id FROM products WHERE product_name = 'Set Pistolas Nerf Profesional'), 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/pistolas_nerf.webp'),
    (DEFAULT, (SELECT product_id FROM products WHERE product_name = 'Libro Disney Para Colorear - Edición Coleccionista'), 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/libro_disney_colorear.webp'),
    (DEFAULT, (SELECT product_id FROM products WHERE product_name = 'Laptop ASUS X441N'), 'https://aplication-web-storage.s3.us-east-2.amazonaws.com/products/laptop_asus_x441n.webp');

INSERT INTO public.transaction(
    buyer_id, product_id, transaction_date, payment_method, status)
VALUES
    ((SELECT id FROM users WHERE user_name = 'user1'), (SELECT product_id FROM products WHERE product_name = 'Xiaomi POCO C65 128GB'), NOW(), 'CREDIT_CARD', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user2'), (SELECT product_id FROM products WHERE product_name = 'Shield USB Host 2.0 Arduino'), NOW(), 'DEBIT_CARD', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user3'), (SELECT product_id FROM products WHERE product_name = 'Termodinámica en Ingeniería Química'), NOW(), 'TRANSFER', 'PENDING'),
    ((SELECT id FROM users WHERE user_name = 'user4'), (SELECT product_id FROM products WHERE product_name = 'Rodillo Facial de Jade Premium'), NOW(), 'CREDIT_CARD', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user5'), (SELECT product_id FROM products WHERE product_name = 'Película Protectora para Vidrios'), NOW(), 'CASH', 'CANCELLED'),
    ((SELECT id FROM users WHERE user_name = 'user6'), (SELECT product_id FROM products WHERE product_name = 'Antifaz para Dormir Premium'), NOW(), 'CREDIT_CARD', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user7'), (SELECT product_id FROM products WHERE product_name = 'Histología Médica Ross 8va Edición'), NOW(), 'DEBIT_CARD', 'PENDING'),
    ((SELECT id FROM users WHERE user_name = 'user8'), (SELECT product_id FROM products WHERE product_name = 'Set Pistolas Nerf Profesional'), NOW(), 'TRANSFER', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user9'), (SELECT product_id FROM products WHERE product_name = 'Libro Disney Para Colorear - Edición Coleccionista'), NOW(), 'CASH', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user10'), (SELECT product_id FROM products WHERE product_name = 'Laptop ASUS X441N'), NOW(), 'CREDIT_CARD', 'PENDING'),
    ((SELECT id FROM users WHERE user_name = 'user1'), (SELECT product_id FROM products WHERE product_name = 'Rodillo Facial de Jade Premium'), NOW(), 'DEBIT_CARD', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user2'), (SELECT product_id FROM products WHERE product_name = 'Película Protectora para Vidrios'), NOW(), 'TRANSFER', 'CANCELLED'),
    ((SELECT id FROM users WHERE user_name = 'user3'), (SELECT product_id FROM products WHERE product_name = 'Set Pistolas Nerf Profesional'), NOW(), 'CASH', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user4'), (SELECT product_id FROM products WHERE product_name = 'Xiaomi POCO C65 128GB'), NOW(), 'CREDIT_CARD', 'PENDING'),
    ((SELECT id FROM users WHERE user_name = 'user5'), (SELECT product_id FROM products WHERE product_name = 'Laptop ASUS X441N'), NOW(), 'DEBIT_CARD', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user6'), (SELECT product_id FROM products WHERE product_name = 'Termodinámica en Ingeniería Química'), NOW(), 'TRANSFER', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user7'), (SELECT product_id FROM products WHERE product_name = 'Antifaz para Dormir Premium'), NOW(), 'CASH', 'PENDING'),
    ((SELECT id FROM users WHERE user_name = 'user8'), (SELECT product_id FROM products WHERE product_name = 'Libro Disney Para Colorear - Edición Coleccionista'), NOW(), 'CREDIT_CARD', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user9'), (SELECT product_id FROM products WHERE product_name = 'Histología Médica Ross 8va Edición'), NOW(), 'DEBIT_CARD', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user10'), (SELECT product_id FROM products WHERE product_name = 'Set Pistolas Nerf Profesional'), NOW(), 'TRANSFER', 'PENDING'),
    ((SELECT id FROM users WHERE user_name = 'user1'), (SELECT product_id FROM products WHERE product_name = 'Laptop ASUS X441N'), NOW(), 'CASH', 'CANCELLED'),
    ((SELECT id FROM users WHERE user_name = 'user2'), (SELECT product_id FROM products WHERE product_name = 'Antifaz para Dormir Premium'), NOW(), 'CREDIT_CARD', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user3'), (SELECT product_id FROM products WHERE product_name = 'Termodinámica en Ingeniería Química'), NOW(), 'DEBIT_CARD', 'PENDING'),
    ((SELECT id FROM users WHERE user_name = 'user4'), (SELECT product_id FROM products WHERE product_name = 'Rodillo Facial de Jade Premium'), NOW(), 'TRANSFER', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user5'), (SELECT product_id FROM products WHERE product_name = 'Xiaomi POCO C65 128GB'), NOW(), 'CASH', 'CANCELLED'),
    ((SELECT id FROM users WHERE user_name = 'user6'), (SELECT product_id FROM products WHERE product_name = 'Set Pistolas Nerf Profesional'), NOW(), 'CREDIT_CARD', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user7'), (SELECT product_id FROM products WHERE product_name = 'Libro Disney Para Colorear - Edición Coleccionista'), NOW(), 'DEBIT_CARD', 'PENDING'),
    ((SELECT id FROM users WHERE user_name = 'user8'), (SELECT product_id FROM products WHERE product_name = 'Histología Médica Ross 8va Edición'), NOW(), 'TRANSFER', 'COMPLETED'),
    ((SELECT id FROM users WHERE user_name = 'user9'), (SELECT product_id FROM products WHERE product_name = 'Antifaz para Dormir Premium'), NOW(), 'CASH', 'CANCELLED'),
    ((SELECT id FROM users WHERE user_name = 'user10'), (SELECT product_id FROM products WHERE product_name = 'Termodinámica en Ingeniería Química'), NOW(), 'CREDIT_CARD', 'COMPLETED');