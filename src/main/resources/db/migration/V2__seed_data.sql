-- Seed reference data for Pagina Web Pasteleria

INSERT INTO roles (id, name) VALUES
    (1, 'ADMIN'),
    (2, 'CUSTOMER')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO users (
    id, run, first_name, last_name, email, password_hash, role_id, status,
    birth_date, region, commune, address, phone, promo_code, felices50,
    bday_redeemed_year, created_at, updated_at
) VALUES
    ('2b4b4b70-9f3a-4a59-9d2c-6bd7f0ac0ad1', '19011022K', 'Juan', 'Pérez Soto', 'admin@duoc.cl',
     '$2a$10$Dow1YjeRGna43EIBgzHu6e7dZTCDxbFzNw2K6Jqq/RSBksiBeIVHe', 1, 'ACTIVE',
     '1985-06-15', 'Región Metropolitana', 'Santiago', 'Av. Siempre Viva 123',
     NULL, NULL, b'0', NULL, NOW(), NOW()),
    ('52f1a9dd-7b5b-4d75-af0f-e2f3b2a07c3d', '20033044K', 'Luis', 'Ramírez Fuentes', 'cliente@gmail.com',
     '$2a$10$Il8bVsgcF6edSxnl2xe50eFkXUl3Bd6mV9Ezg4QPGVE3d2r0Rk5hm', 2, 'ACTIVE',
     '1998-03-20', 'Biobío', 'Concepción', 'Las Flores 789',
     '+56912345678', NULL, b'0', NULL, NOW(), NOW()),
    ('4c1e2873-2f6c-4d30-8ab7-1b4afab654f9', '18022033K', 'Ana', 'López Díaz', 'vendedor@duoc.cl',
     '$2a$10$Il8bVsgcF6edSxnl2xe50eFkXUl3Bd6mV9Ezg4QPGVE3d2r0Rk5hm', 1, 'ACTIVE',
     '1990-08-12', 'Valparaíso', 'Viña del Mar', 'Calle Mar 456',
     NULL, NULL, b'0', NULL, NOW(), NOW())
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO user_addresses (
    id, user_id, alias, address, region, commune, reference_text, is_primary
) VALUES
    ('3df4a9c0-0a8c-4a3b-b5fd-fc0a9d8c9a01', '52f1a9dd-7b5b-4d75-af0f-e2f3b2a07c3d', 'Dirección principal',
     'Las Flores 789', 'Biobío', 'Concepción', 'Depto 1203', b'1'),
    ('9c98f24b-7bde-4020-9b66-82f84775a3b7', '52f1a9dd-7b5b-4d75-af0f-e2f3b2a07c3d', 'Oficina',
     'Av. Libertad 500', 'Biobío', 'Concepción', NULL, b'0')
ON DUPLICATE KEY UPDATE address = VALUES(address);

INSERT INTO coupons (code, type, value, label, active)
VALUES
    ('ENVIOGRATIS', 'SHIP', 0, 'Envío gratis', b'1'),
    ('5000OFF', 'AMOUNT', 5000, '$5.000 OFF', b'1'),
    ('FELICES50', 'AMOUNT', 0, 'Beneficio FELICES50', b'1')
ON DUPLICATE KEY UPDATE label = VALUES(label);

INSERT INTO products (
    id, name, price, category, attributes, image_url, stock, critical_stock, description
) VALUES
    ('TC001', 'Torta Cuadrada de Chocolate', 45000, 'Tortas Cuadradas', '20 porciones', '/img/Torta Cuadrada de Chocolate.png', 6, 2,
     'Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales.'),
    ('TC002', 'Torta Cuadrada de Frutas', 50000, 'Tortas Cuadradas', 'Frutas frescas', '/img/Torta Cuadrada de Frutas.png', 6, 2,
     'Mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla. Ideal para celebraciones.'),
    ('TT001', 'Torta Circular de Vainilla', 40000, 'Tortas Circulares', '12 porciones', '/img/Torta Circular de Vainilla.png', 6, 2,
     'Bizcocho de vainilla clásico relleno con crema pastelera y glaseado dulce, perfecto para cualquier ocasión.'),
    ('TT002', 'Torta Circular de Manjar', 42000, 'Tortas Circulares', 'Con nueces', '/img/Torta Circular de Manjar.png', 6, 2,
     'Torta tradicional con manjar y nueces, un deleite para los amantes de los sabores clásicos.'),
    ('PI001', 'Mousse de Chocolate', 5000, 'Postres Individuales', 'Individual', '/img/Mousse de Chocolate.png', 6, 2,
     'Postre individual cremoso y suave, hecho con chocolate de alta calidad.'),
    ('PI002', 'Tiramisú Clásico', 5500, 'Postres Individuales', 'Individual', '/img/Tiramisú Clásico.png', 6, 2,
     'Postre italiano individual con capas de café, mascarpone y cacao. Perfecto para finalizar cualquier comida.'),
    ('PSA001', 'Torta Sin Azúcar de Naranja', 48000, 'Productos Sin Azúcar', 'Endulzada naturalmente', '/img/Torta Sin Azúcar de Naranja.png', 6, 2,
     'Ligera y deliciosa, endulzada naturalmente. Ideal para opciones más saludables.'),
    ('PSA002', 'Cheesecake Sin Azúcar', 47000, 'Productos Sin Azúcar', 'Sin azúcar', '/img/Cheesecake.png', 6, 2,
     'Suave y cremoso, opción perfecta para disfrutar sin culpa.'),
    ('PG001', 'Brownie Sin Gluten', 4000, 'Productos Sin Gluten', 'Cacao 70%', '/img/Brownie.png', 6, 2,
     'Rico y denso; ideal para quienes evitan el gluten sin sacrificar el sabor.'),
    ('PG002', 'Pan Sin Gluten', 3500, 'Productos Sin Gluten', 'Pan de molde', '/img/Pan integral.png', 6, 2,
     'Suave y esponjoso, perfecto para sándwiches o acompañar cualquier comida.'),
    ('PV001', 'Torta Vegana de Chocolate', 50000, 'Productos Vegana', 'Vegano', '/img/Torta Vegana de Chocolate.png', 6, 2,
     'Torta húmeda y deliciosa, sin productos de origen animal.'),
    ('PV002', 'Galletas Veganas de Avena', 4500, 'Productos Vegana', 'Pack x 10', '/img/Galletas Veganas de Avena.png', 6, 2,
     'Crujientes y sabrosas; excelente opción de snack.'),
    ('TE001', 'Torta Especial de Cumpleaños', 55000, 'Tortas Especiales', 'Personalizable', '/img/Torta Especial de Cumpleaños.png', 6, 2,
     'Personalizable con decoraciones y mensajes únicos.'),
    ('BDAY001', 'Torta Individual de Cumpleaños DUOC', 25000, 'Beneficios DUOC', 'Porción individual', '/img/torta_individual_cumpleaños.png', 12, 3,
     'Pastel individual con vela y decoración festiva, reservado como beneficio de cumpleaños para estudiantes y colaboradores DUOC.'),
    ('TE002', 'Torta Especial de Boda', 60000, 'Tortas Especiales', 'Diseño elegante', '/img/Torta Especial de Boda.png', 6, 2,
     'Elegante y deliciosa; pensada para ser el centro de tu boda.')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO blog_posts (
    id, slug, title, hero_image, hero_caption, excerpt, body
) VALUES
    ('d81b44c6-7c1f-4ad5-8c0b-0e6cbf53296d', 'caso-curioso-1', 'Caso curioso #1: La torta gigante del récord',
     '/img/blog1.png', 'La torta gigante celebrada en la plaza principal.',
     'Cómo se preparó la torta gigante que nos dio un récord y qué aprendimos para producir a gran escala en días festivos.',
     JSON_ARRAY(
         JSON_OBJECT('type', 'p', 'content', 'En 1995, Mil Sabores sorprendió al país con una de las tortas más grandes jamás preparadas. Fue un desafío histórico que puso a prueba nuestra logística, creatividad y, sobre todo, la pasión por endulzar grandes momentos.'),
         JSON_OBJECT('type', 'p', 'content', 'El proyecto nació como parte de las celebraciones de aniversario de la ciudad. La meta era reunir a miles de personas en torno a un único postre, uniendo tradición y comunidad. Para lograrlo, se necesitaron más de 200 kilos de harina, 150 litros de leche y cientos de horas de trabajo en equipo.'),
         JSON_OBJECT('type', 'p', 'content', 'El día del evento, la plaza principal se llenó de aromas dulces mientras hornos industriales funcionaban sin descanso. La coordinación con voluntarios, estudiantes de gastronomía y maestros pasteleros fue clave para que todo resultara a la perfección.'),
         JSON_OBJECT('type', 'p', 'content', 'Al final, la torta no solo batió un récord local, sino que también quedó en la memoria colectiva de quienes participaron. Este hito marcó un antes y un después en nuestra historia, consolidando a Mil Sabores como un referente en innovación pastelera.')
     )),
    ('a8674db5-6d11-4b2d-82db-edd86ce9c2b4', 'caso-curioso-2', 'Caso curioso #2: Galletas 100% veganas',
     '/img/blog2.png', 'Selección de galletas veganas de temporada.',
     'Secretos de nuestra masa de galletas veganas: textura crujiente con ingredientes 100% de origen vegetal.',
     JSON_ARRAY(
         JSON_OBJECT('type', 'p', 'content', 'En Mil Sabores siempre hemos buscado innovar para atender a todos nuestros clientes. Una de nuestras mayores apuestas fue desarrollar una receta de galletas 100% veganas, sin ingredientes de origen animal, que mantuvieran la textura crujiente y el sabor dulce de las clásicas galletas caseras.'),
         JSON_OBJECT('type', 'p', 'content', 'El proceso no fue sencillo: probamos diferentes combinaciones de harinas, aceites vegetales y endulzantes naturales hasta encontrar la mezcla perfecta. Tras varias pruebas en nuestro taller, logramos un producto que no solo cumplió con nuestras expectativas, sino que encantó a quienes lo probaron en la primera degustación abierta al público.'),
         JSON_OBJECT('type', 'p', 'content', 'Estas galletas no solo son una alternativa deliciosa para clientes veganos, sino también para quienes buscan opciones más saludables y sostenibles. Usamos ingredientes de productores locales, lo que refuerza nuestro compromiso con la comunidad y el medio ambiente.'),
         JSON_OBJECT('type', 'p', 'content', 'Hoy forman parte de nuestro catálogo fijo y se han convertido en uno de los productos más pedidos en nuestra tienda online. ¡Una muestra de que con creatividad y dedicación se pueden lograr sabores sorprendentes sin renunciar a la calidad!')
     ))
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- Regions and communes
INSERT INTO regions (name) VALUES
    ('Arica y Parinacota'),
    ('Tarapacá'),
    ('Antofagasta'),
    ('Atacama'),
    ('Coquimbo'),
    ('Valparaíso'),
    ('Metropolitana'),
    ('O''Higgins'),
    ('Maule'),
    ('Ñuble'),
    ('Biobío'),
    ('La Araucanía'),
    ('Los Ríos'),
    ('Los Lagos'),
    ('Aysén'),
    ('Magallanes')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Communes seeded per region
-- Arica y Parinacota
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Arica' AS name UNION ALL
    SELECT 'Camarones' UNION ALL
    SELECT 'Putre' UNION ALL
    SELECT 'General Lagos'
) c
JOIN regions r ON r.name = 'Arica y Parinacota'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Tarapacá
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Iquique' AS name UNION ALL
    SELECT 'Alto Hospicio' UNION ALL
    SELECT 'Pozo Almonte' UNION ALL
    SELECT 'Pica' UNION ALL
    SELECT 'Huara' UNION ALL
    SELECT 'Camiña' UNION ALL
    SELECT 'Colchane'
) c
JOIN regions r ON r.name = 'Tarapacá'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Antofagasta
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Antofagasta' AS name UNION ALL
    SELECT 'Calama' UNION ALL
    SELECT 'Mejillones' UNION ALL
    SELECT 'Tocopilla' UNION ALL
    SELECT 'Taltal' UNION ALL
    SELECT 'Sierra Gorda' UNION ALL
    SELECT 'San Pedro de Atacama' UNION ALL
    SELECT 'María Elena' UNION ALL
    SELECT 'Ollagüe'
) c
JOIN regions r ON r.name = 'Antofagasta'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Atacama
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Copiapó' AS name UNION ALL
    SELECT 'Caldera' UNION ALL
    SELECT 'Tierra Amarilla' UNION ALL
    SELECT 'Chañaral' UNION ALL
    SELECT 'Diego de Almagro' UNION ALL
    SELECT 'Vallenar' UNION ALL
    SELECT 'Huasco' UNION ALL
    SELECT 'Freirina' UNION ALL
    SELECT 'Alto del Carmen'
) c
JOIN regions r ON r.name = 'Atacama'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Coquimbo
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'La Serena' AS name UNION ALL
    SELECT 'Coquimbo' UNION ALL
    SELECT 'Ovalle' UNION ALL
    SELECT 'Vicuña' UNION ALL
    SELECT 'Illapel' UNION ALL
    SELECT 'Los Vilos' UNION ALL
    SELECT 'Salamanca' UNION ALL
    SELECT 'Andacollo' UNION ALL
    SELECT 'Monte Patria' UNION ALL
    SELECT 'Combarbalá' UNION ALL
    SELECT 'Punitaqui' UNION ALL
    SELECT 'Río Hurtado' UNION ALL
    SELECT 'Paiguano' UNION ALL
    SELECT 'Canela'
) c
JOIN regions r ON r.name = 'Coquimbo'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Valparaíso
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Valparaíso' AS name UNION ALL
    SELECT 'Viña del Mar' UNION ALL
    SELECT 'Quilpué' UNION ALL
    SELECT 'Villa Alemana' UNION ALL
    SELECT 'Concón' UNION ALL
    SELECT 'San Antonio' UNION ALL
    SELECT 'San Felipe' UNION ALL
    SELECT 'Los Andes' UNION ALL
    SELECT 'Quillota' UNION ALL
    SELECT 'La Calera' UNION ALL
    SELECT 'La Ligua' UNION ALL
    SELECT 'Papudo' UNION ALL
    SELECT 'Puchuncaví' UNION ALL
    SELECT 'Limache' UNION ALL
    SELECT 'Olmué' UNION ALL
    SELECT 'Quintero' UNION ALL
    SELECT 'Casablanca' UNION ALL
    SELECT 'Santa María' UNION ALL
    SELECT 'Panquehue' UNION ALL
    SELECT 'Rinconada' UNION ALL
    SELECT 'Calle Larga' UNION ALL
    SELECT 'Juan Fernández' UNION ALL
    SELECT 'Isla de Pascua' UNION ALL
    SELECT 'Zapallar' UNION ALL
    SELECT 'Cartagena' UNION ALL
    SELECT 'El Quisco' UNION ALL
    SELECT 'El Tabo' UNION ALL
    SELECT 'Algarrobo' UNION ALL
    SELECT 'Petorca' UNION ALL
    SELECT 'Nogales' UNION ALL
    SELECT 'Cabildo' UNION ALL
    SELECT 'Putaendo' UNION ALL
    SELECT 'Llay Llay'
) c
JOIN regions r ON r.name = 'Valparaíso'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Metropolitana
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Santiago' AS name UNION ALL
    SELECT 'Puente Alto' UNION ALL
    SELECT 'Maipú' UNION ALL
    SELECT 'Las Condes' UNION ALL
    SELECT 'Providencia' UNION ALL
    SELECT 'Ñuñoa' UNION ALL
    SELECT 'La Florida' UNION ALL
    SELECT 'Peñalolén' UNION ALL
    SELECT 'Recoleta' UNION ALL
    SELECT 'Independencia' UNION ALL
    SELECT 'La Reina' UNION ALL
    SELECT 'Macul' UNION ALL
    SELECT 'Vitacura' UNION ALL
    SELECT 'Lo Barnechea' UNION ALL
    SELECT 'Pudahuel' UNION ALL
    SELECT 'Cerro Navia' UNION ALL
    SELECT 'Quinta Normal' UNION ALL
    SELECT 'Renca' UNION ALL
    SELECT 'Huechuraba' UNION ALL
    SELECT 'Quilicura' UNION ALL
    SELECT 'Estación Central' UNION ALL
    SELECT 'San Miguel' UNION ALL
    SELECT 'San Joaquín' UNION ALL
    SELECT 'La Granja' UNION ALL
    SELECT 'La Cisterna' UNION ALL
    SELECT 'San Ramón' UNION ALL
    SELECT 'El Bosque' UNION ALL
    SELECT 'Pedro Aguirre Cerda' UNION ALL
    SELECT 'Lo Espejo' UNION ALL
    SELECT 'Conchalí' UNION ALL
    SELECT 'Lo Prado' UNION ALL
    SELECT 'San Bernardo' UNION ALL
    SELECT 'Melipilla' UNION ALL
    SELECT 'Talagante' UNION ALL
    SELECT 'Peñaflor' UNION ALL
    SELECT 'Padre Hurtado' UNION ALL
    SELECT 'Buin' UNION ALL
    SELECT 'Paine' UNION ALL
    SELECT 'Colina' UNION ALL
    SELECT 'Lampa' UNION ALL
    SELECT 'Tiltil' UNION ALL
    SELECT 'Isla de Maipo' UNION ALL
    SELECT 'Curacaví' UNION ALL
    SELECT 'María Pinto' UNION ALL
    SELECT 'El Monte' UNION ALL
    SELECT 'Alhué' UNION ALL
    SELECT 'San José de Maipo' UNION ALL
    SELECT 'Pirque'
) c
JOIN regions r ON r.name = 'Metropolitana'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- O'Higgins
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Rancagua' AS name UNION ALL
    SELECT 'Machalí' UNION ALL
    SELECT 'San Fernando' UNION ALL
    SELECT 'Rengo' UNION ALL
    SELECT 'Requínoa' UNION ALL
    SELECT 'Graneros' UNION ALL
    SELECT 'Mostazal' UNION ALL
    SELECT 'San Vicente' UNION ALL
    SELECT 'Chimbarongo' UNION ALL
    SELECT 'Nancagua' UNION ALL
    SELECT 'Santa Cruz' UNION ALL
    SELECT 'Palmilla' UNION ALL
    SELECT 'Peralillo' UNION ALL
    SELECT 'Pichidegua' UNION ALL
    SELECT 'Peumo' UNION ALL
    SELECT 'Codegua' UNION ALL
    SELECT 'Coinco' UNION ALL
    SELECT 'Doñihue' UNION ALL
    SELECT 'Malloa' UNION ALL
    SELECT 'Olivar' UNION ALL
    SELECT 'Quinta de Tilcoco' UNION ALL
    SELECT 'Las Cabras' UNION ALL
    SELECT 'Pichilemu' UNION ALL
    SELECT 'Marchigüe' UNION ALL
    SELECT 'Navidad' UNION ALL
    SELECT 'La Estrella' UNION ALL
    SELECT 'Litueche' UNION ALL
    SELECT 'Paredones' UNION ALL
    SELECT 'Pumanque' UNION ALL
    SELECT 'Chépica' UNION ALL
    SELECT 'Placilla'
) c
JOIN regions r ON r.name = 'O''Higgins'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Maule
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Talca' AS name UNION ALL
    SELECT 'Curicó' UNION ALL
    SELECT 'Linares' UNION ALL
    SELECT 'Cauquenes' UNION ALL
    SELECT 'San Clemente' UNION ALL
    SELECT 'Maule' UNION ALL
    SELECT 'San Javier' UNION ALL
    SELECT 'Constitución' UNION ALL
    SELECT 'Parral' UNION ALL
    SELECT 'Rauco' UNION ALL
    SELECT 'Romeral' UNION ALL
    SELECT 'Hualañé' UNION ALL
    SELECT 'Molina' UNION ALL
    SELECT 'Teno' UNION ALL
    SELECT 'Sagrada Familia' UNION ALL
    SELECT 'Pelarco' UNION ALL
    SELECT 'Pencahue' UNION ALL
    SELECT 'Empedrado' UNION ALL
    SELECT 'Río Claro' UNION ALL
    SELECT 'Colbún' UNION ALL
    SELECT 'Longaví' UNION ALL
    SELECT 'Villa Alegre' UNION ALL
    SELECT 'Yerbas Buenas' UNION ALL
    SELECT 'Retiro' UNION ALL
    SELECT 'Chanco' UNION ALL
    SELECT 'Pelluhue' UNION ALL
    SELECT 'Vichuquén' UNION ALL
    SELECT 'Licantén'
) c
JOIN regions r ON r.name = 'Maule'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Ñuble
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Chillán' AS name UNION ALL
    SELECT 'Chillán Viejo' UNION ALL
    SELECT 'San Carlos' UNION ALL
    SELECT 'Bulnes' UNION ALL
    SELECT 'Quirihue' UNION ALL
    SELECT 'Yungay' UNION ALL
    SELECT 'Quillón' UNION ALL
    SELECT 'Pemuco' UNION ALL
    SELECT 'El Carmen' UNION ALL
    SELECT 'Coihueco' UNION ALL
    SELECT 'San Nicolás' UNION ALL
    SELECT 'Pinto' UNION ALL
    SELECT 'San Fabián' UNION ALL
    SELECT 'Ninhue' UNION ALL
    SELECT 'Ránquil' UNION ALL
    SELECT 'Cobquecura' UNION ALL
    SELECT 'Coelemu' UNION ALL
    SELECT 'Treguaco' UNION ALL
    SELECT 'Portezuelo'
) c
JOIN regions r ON r.name = 'Ñuble'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Biobío
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Concepción' AS name UNION ALL
    SELECT 'Talcahuano' UNION ALL
    SELECT 'Hualpén' UNION ALL
    SELECT 'San Pedro de la Paz' UNION ALL
    SELECT 'Coronel' UNION ALL
    SELECT 'Chiguayante' UNION ALL
    SELECT 'Florida' UNION ALL
    SELECT 'Penco' UNION ALL
    SELECT 'Tomé' UNION ALL
    SELECT 'Lota' UNION ALL
    SELECT 'Hualqui' UNION ALL
    SELECT 'Los Ángeles' UNION ALL
    SELECT 'Cabrero' UNION ALL
    SELECT 'Nacimiento' UNION ALL
    SELECT 'Mulchén' UNION ALL
    SELECT 'Yumbel' UNION ALL
    SELECT 'Laja' UNION ALL
    SELECT 'San Rosendo' UNION ALL
    SELECT 'Santa Bárbara' UNION ALL
    SELECT 'Quilleco' UNION ALL
    SELECT 'Quilaco' UNION ALL
    SELECT 'Alto Biobío' UNION ALL
    SELECT 'Arauco' UNION ALL
    SELECT 'Lebu' UNION ALL
    SELECT 'Curanilahue' UNION ALL
    SELECT 'Los Álamos' UNION ALL
    SELECT 'Contulmo'
) c
JOIN regions r ON r.name = 'Biobío'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- La Araucanía
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Temuco' AS name UNION ALL
    SELECT 'Padre Las Casas' UNION ALL
    SELECT 'Angol' UNION ALL
    SELECT 'Victoria' UNION ALL
    SELECT 'Villarrica' UNION ALL
    SELECT 'Pucón' UNION ALL
    SELECT 'Lautaro' UNION ALL
    SELECT 'Nueva Imperial' UNION ALL
    SELECT 'Carahue' UNION ALL
    SELECT 'Gorbea' UNION ALL
    SELECT 'Loncoche' UNION ALL
    SELECT 'Pitrufquén' UNION ALL
    SELECT 'Freire' UNION ALL
    SELECT 'Saavedra' UNION ALL
    SELECT 'Toltén' UNION ALL
    SELECT 'Teodoro Schmidt' UNION ALL
    SELECT 'Cunco' UNION ALL
    SELECT 'Curacautín' UNION ALL
    SELECT 'Lonquimay' UNION ALL
    SELECT 'Melipeuco' UNION ALL
    SELECT 'Perquenco' UNION ALL
    SELECT 'Galvarino' UNION ALL
    SELECT 'Ercilla' UNION ALL
    SELECT 'Traiguén' UNION ALL
    SELECT 'Renaico' UNION ALL
    SELECT 'Collipulli' UNION ALL
    SELECT 'Purén' UNION ALL
    SELECT 'Los Sauces' UNION ALL
    SELECT 'Lumaco' UNION ALL
    SELECT 'Cholchol'
) c
JOIN regions r ON r.name = 'La Araucanía'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Los Ríos
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Valdivia' AS name UNION ALL
    SELECT 'La Unión' UNION ALL
    SELECT 'Río Bueno' UNION ALL
    SELECT 'Panguipulli' UNION ALL
    SELECT 'Paillaco' UNION ALL
    SELECT 'Lanco' UNION ALL
    SELECT 'Futrono' UNION ALL
    SELECT 'Lago Ranco' UNION ALL
    SELECT 'Mariquina' UNION ALL
    SELECT 'Corral' UNION ALL
    SELECT 'Máfil' UNION ALL
    SELECT 'Los Lagos'
) c
JOIN regions r ON r.name = 'Los Ríos'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Los Lagos
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Puerto Montt' AS name UNION ALL
    SELECT 'Puerto Varas' UNION ALL
    SELECT 'Osorno' UNION ALL
    SELECT 'Castro' UNION ALL
    SELECT 'Ancud' UNION ALL
    SELECT 'Quellón' UNION ALL
    SELECT 'Frutillar' UNION ALL
    SELECT 'Llanquihue' UNION ALL
    SELECT 'Calbuco' UNION ALL
    SELECT 'Fresia' UNION ALL
    SELECT 'Los Muermos' UNION ALL
    SELECT 'Maullín' UNION ALL
    SELECT 'Puerto Octay' UNION ALL
    SELECT 'Puyehue' UNION ALL
    SELECT 'Río Negro' UNION ALL
    SELECT 'San Juan de la Costa' UNION ALL
    SELECT 'San Pablo' UNION ALL
    SELECT 'Chonchi' UNION ALL
    SELECT 'Dalcahue' UNION ALL
    SELECT 'Queilén' UNION ALL
    SELECT 'Quemchi' UNION ALL
    SELECT 'Quinchao' UNION ALL
    SELECT 'Curaco de Vélez' UNION ALL
    SELECT 'Cochamó' UNION ALL
    SELECT 'Hualaihué' UNION ALL
    SELECT 'Futaleufú' UNION ALL
    SELECT 'Chaitén' UNION ALL
    SELECT 'Palena'
) c
JOIN regions r ON r.name = 'Los Lagos'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Aysén
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Coyhaique' AS name UNION ALL
    SELECT 'Aysén' UNION ALL
    SELECT 'Cisnes' UNION ALL
    SELECT 'Chile Chico' UNION ALL
    SELECT 'Río Ibáñez' UNION ALL
    SELECT 'Guaitecas' UNION ALL
    SELECT 'Lago Verde' UNION ALL
    SELECT 'O''Higgins' UNION ALL
    SELECT 'Cochrane' UNION ALL
    SELECT 'Tortel'
) c
JOIN regions r ON r.name = 'Aysén'
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Magallanes
INSERT INTO communes (region_id, name)
SELECT r.id, c.name FROM (
    SELECT 'Punta Arenas' AS name UNION ALL
    SELECT 'Puerto Natales' UNION ALL
    SELECT 'Porvenir' UNION ALL
    SELECT 'Cabo de Hornos' UNION ALL
    SELECT 'Primavera' UNION ALL
    SELECT 'San Gregorio' UNION ALL
    SELECT 'Laguna Blanca' UNION ALL
    SELECT 'Río Verde' UNION ALL
    SELECT 'Timaukel' UNION ALL
    SELECT 'Torres del Paine' UNION ALL
    SELECT 'Antártica'
) c
JOIN regions r ON r.name = 'Magallanes'
ON DUPLICATE KEY UPDATE name = VALUES(name);
