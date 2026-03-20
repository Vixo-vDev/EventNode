-- ============================================================
-- EventNode - Script de Limpieza y Datos de Prueba
-- Base de datos: event_node (MySQL 8.0, puerto 3307)
-- ============================================================
-- USO: Ejecutar en MySQL Workbench, DBeaver o terminal:
--   mysql -u vixo_dev -p -P 3307 event_node < sql_test_data.sql
-- ============================================================

-- ============================
-- PARTE 1: ELIMINAR TODA LA DATA
-- ============================
-- Desactivar FK checks y safe update mode
SET FOREIGN_KEY_CHECKS = 0;
SET SQL_SAFE_UPDATES = 0;

-- Tablas dependientes primero
TRUNCATE TABLE diplomas_emitidos;
TRUNCATE TABLE diplomas;
TRUNCATE TABLE asistencias;
TRUNCATE TABLE pre_checkin;

-- Tabla de relación evento-organizador (creada por native query, no JPA entity)
DELETE FROM evento_organizador;

TRUNCATE TABLE eventos;
TRUNCATE TABLE organizadores;
TRUNCATE TABLE categorias;
TRUNCATE TABLE alumnos;
TRUNCATE TABLE administradores;
TRUNCATE TABLE usuarios;
TRUNCATE TABLE roles;

-- Reactivar FK checks y safe update mode
SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;

-- Resetear auto-increments
ALTER TABLE usuarios AUTO_INCREMENT = 1;
ALTER TABLE eventos AUTO_INCREMENT = 1;
ALTER TABLE categorias AUTO_INCREMENT = 1;
ALTER TABLE organizadores AUTO_INCREMENT = 1;
ALTER TABLE asistencias AUTO_INCREMENT = 1;
ALTER TABLE pre_checkin AUTO_INCREMENT = 1;
ALTER TABLE diplomas AUTO_INCREMENT = 1;
ALTER TABLE diplomas_emitidos AUTO_INCREMENT = 1;

-- ============================
-- PARTE 2: INSERTAR DATOS DE PRUEBA
-- ============================

-- --------------------------------------------------------
-- 2.1 ROLES
-- --------------------------------------------------------
INSERT INTO roles (nombre) VALUES
('ALUMNO'),
('ADMINISTRADOR'),
('SUPERADMIN');

-- Obtener IDs de roles
SET @rol_alumno = (SELECT id_rol FROM roles WHERE nombre = 'ALUMNO');
SET @rol_admin = (SELECT id_rol FROM roles WHERE nombre = 'ADMINISTRADOR');
SET @rol_superadmin = (SELECT id_rol FROM roles WHERE nombre = 'SUPERADMIN');

-- --------------------------------------------------------
-- 2.2 USUARIOS
-- --------------------------------------------------------
-- Contraseñas BCrypt:
--   SuperAdmin/Admin: Admin@1234
--   Alumnos: Alumno@123

-- SuperAdmin principal
INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Admin', 'EventNode', 'Principal', 'admin@eventnode.com',
 '$2b$10$MmYhzbhXeim4jHS/Mh3AkupcHJUhPwVaSHURS6nk42aq6pJa.eSDq',
 'ACTIVO', 0, @rol_superadmin, NOW());
SET @id_superadmin = LAST_INSERT_ID();

-- Administrador regular
INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Carlos', 'Hernández', 'López', 'carlos.admin@eventnode.com',
 '$2b$10$MmYhzbhXeim4jHS/Mh3AkupcHJUhPwVaSHURS6nk42aq6pJa.eSDq',
 'ACTIVO', 0, @rol_admin, NOW());
SET @id_admin1 = LAST_INSERT_ID();

-- Alumnos (10 estudiantes)
INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('María', 'García', 'Rodríguez', 'maria.garcia@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'ACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno1 = LAST_INSERT_ID();

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Juan', 'López', 'Martínez', 'juan.lopez@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'ACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno2 = LAST_INSERT_ID();

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Ana', 'Martínez', 'Sánchez', 'ana.martinez@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'ACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno3 = LAST_INSERT_ID();

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Pedro', 'Ramírez', 'Torres', 'pedro.ramirez@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'ACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno4 = LAST_INSERT_ID();

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Laura', 'Díaz', 'Flores', 'laura.diaz@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'ACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno5 = LAST_INSERT_ID();

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Roberto', 'Morales', 'Jiménez', 'roberto.morales@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'ACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno6 = LAST_INSERT_ID();

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Sofía', 'Cruz', 'Hernández', 'sofia.cruz@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'ACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno7 = LAST_INSERT_ID();

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Diego', 'Vargas', 'Mendoza', 'diego.vargas@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'ACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno8 = LAST_INSERT_ID();

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Valentina', 'Reyes', 'Castillo', 'valentina.reyes@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'ACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno9 = LAST_INSERT_ID();

INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Fernando', 'Aguilar', 'Ramos', 'fernando.aguilar@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'ACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno10 = LAST_INSERT_ID();

-- Un alumno INACTIVO para probar filtros
INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, password, estado, intentos_fallidos, rol_id, fecha_creacion) VALUES
('Miguel', 'Ortega', 'Luna', 'miguel.ortega@utez.edu.mx',
 '$2b$10$5O9STBzV4JauGSZgm666NOExtiotq52GMa4fCqRcHW83dTG50yIxa',
 'INACTIVO', 0, @rol_alumno, NOW());
SET @id_alumno_inactivo = LAST_INSERT_ID();

-- --------------------------------------------------------
-- 2.3 ADMINISTRADORES (tabla relación)
-- --------------------------------------------------------
INSERT INTO administradores (id_usuario, es_principal) VALUES
(@id_superadmin, TRUE),
(@id_admin1, FALSE);

-- --------------------------------------------------------
-- 2.4 ALUMNOS (tabla relación)
-- --------------------------------------------------------
INSERT INTO alumnos (id_usuario, matricula, fecha_nac, edad, sexo, cuatrimestre) VALUES
(@id_alumno1,  '20230001', '2003-03-15', 23, 'F', 5),
(@id_alumno2,  '20230002', '2002-07-22', 23, 'M', 6),
(@id_alumno3,  '20230003', '2003-11-08', 22, 'F', 4),
(@id_alumno4,  '20230004', '2002-01-30', 24, 'M', 7),
(@id_alumno5,  '20230005', '2004-05-12', 21, 'F', 3),
(@id_alumno6,  '20230006', '2001-09-25', 24, 'M', 8),
(@id_alumno7,  '20230007', '2003-12-03', 22, 'F', 5),
(@id_alumno8,  '20230008', '2002-06-18', 23, 'M', 6),
(@id_alumno9,  '20230009', '2004-02-28', 22, 'F', 2),
(@id_alumno10, '20230010', '2001-08-14', 24, 'M', 9),
(@id_alumno_inactivo, '20230011', '2003-04-20', 22, 'M', 4);

-- --------------------------------------------------------
-- 2.5 CATEGORIAS
-- --------------------------------------------------------
INSERT INTO categorias (nombre) VALUES
('TECNOLOGÍA'),
('CULTURA'),
('DEPORTES'),
('ACADÉMICO'),
('EMPRENDIMIENTO');

SET @cat_tech = (SELECT id_categoria FROM categorias WHERE nombre = 'TECNOLOGÍA');
SET @cat_cultura = (SELECT id_categoria FROM categorias WHERE nombre = 'CULTURA');
SET @cat_deportes = (SELECT id_categoria FROM categorias WHERE nombre = 'DEPORTES');
SET @cat_academico = (SELECT id_categoria FROM categorias WHERE nombre = 'ACADÉMICO');
SET @cat_emprende = (SELECT id_categoria FROM categorias WHERE nombre = 'EMPRENDIMIENTO');

-- --------------------------------------------------------
-- 2.6 ORGANIZADORES
-- --------------------------------------------------------
INSERT INTO organizadores (nombre, descripcion, correo) VALUES
('Departamento de TI', 'Departamento de Tecnologías de la Información de UTEZ', 'ti@utez.edu.mx'),
('Club de Cultura', 'Club estudiantil de actividades culturales', 'cultura@utez.edu.mx'),
('Coordinación Deportiva', 'Coordinación de deportes y actividades físicas', 'deportes@utez.edu.mx'),
('Dirección Académica', 'Dirección de asuntos académicos', 'academica@utez.edu.mx'),
('Incubadora UTEZ', 'Centro de emprendimiento e innovación', 'incubadora@utez.edu.mx');

SET @org_ti = (SELECT id_organizador FROM organizadores WHERE correo = 'ti@utez.edu.mx');
SET @org_cultura = (SELECT id_organizador FROM organizadores WHERE correo = 'cultura@utez.edu.mx');
SET @org_deportes = (SELECT id_organizador FROM organizadores WHERE correo = 'deportes@utez.edu.mx');
SET @org_academica = (SELECT id_organizador FROM organizadores WHERE correo = 'academica@utez.edu.mx');
SET @org_incubadora = (SELECT id_organizador FROM organizadores WHERE correo = 'incubadora@utez.edu.mx');

-- --------------------------------------------------------
-- 2.7 EVENTOS (6 eventos variados)
-- --------------------------------------------------------

-- Evento 1: ACTIVO - Hackathon (futuro)
INSERT INTO eventos (nombre, descripcion, ubicacion, capacidad_maxima, fecha_inicio, fecha_fin,
  tiempo_cancelacion_horas, tiempo_tolerancia_minutos, estado, id_categoria, creado_por, fecha_creacion) VALUES
('Hackathon UTEZ 2026', 'Competencia de programación de 24 horas. Equipos de 3-5 personas resolverán retos tecnológicos reales propuestos por empresas de la región.',
 'Auditorio Principal UTEZ', 100,
 DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 8 DAY),
 24, 15, 'ACTIVO', @cat_tech, @id_superadmin, NOW());
SET @ev_hackathon = LAST_INSERT_ID();

-- Evento 2: ACTIVO - Festival Cultural (futuro)
INSERT INTO eventos (nombre, descripcion, ubicacion, capacidad_maxima, fecha_inicio, fecha_fin,
  tiempo_cancelacion_horas, tiempo_tolerancia_minutos, estado, id_categoria, creado_por, fecha_creacion) VALUES
('Festival Cultural de Primavera', 'Evento artístico con presentaciones de música, danza, teatro y exposiciones. Participan estudiantes de todas las carreras.',
 'Explanada Central UTEZ', 200,
 DATE_ADD(NOW(), INTERVAL 14 DAY), DATE_ADD(NOW(), INTERVAL 14 DAY) + INTERVAL 6 HOUR,
 48, 30, 'ACTIVO', @cat_cultura, @id_admin1, NOW());
SET @ev_festival = LAST_INSERT_ID();

-- Evento 3: ACTIVO - Torneo Deportivo (futuro)
INSERT INTO eventos (nombre, descripcion, ubicacion, capacidad_maxima, fecha_inicio, fecha_fin,
  tiempo_cancelacion_horas, tiempo_tolerancia_minutos, estado, id_categoria, creado_por, fecha_creacion) VALUES
('Torneo Interuniversitario de Fútbol', 'Torneo de fútbol 7 entre universidades de Morelos. Fase de grupos y eliminación directa.',
 'Canchas Deportivas UTEZ', 60,
 DATE_ADD(NOW(), INTERVAL 21 DAY), DATE_ADD(NOW(), INTERVAL 23 DAY),
 72, 20, 'ACTIVO', @cat_deportes, @id_superadmin, NOW());
SET @ev_torneo = LAST_INSERT_ID();

-- Evento 4: FINALIZADO - Conferencia pasada
INSERT INTO eventos (nombre, descripcion, ubicacion, capacidad_maxima, fecha_inicio, fecha_fin,
  tiempo_cancelacion_horas, tiempo_tolerancia_minutos, estado, id_categoria, creado_por, fecha_creacion) VALUES
('Conferencia de Inteligencia Artificial', 'Conferencia magistral sobre las tendencias actuales en IA, Machine Learning y su impacto en la industria regional.',
 'Sala de Conferencias B', 80,
 DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY) + INTERVAL 4 HOUR,
 24, 10, 'FINALIZADO', @cat_academico, @id_admin1, DATE_SUB(NOW(), INTERVAL 30 DAY));
SET @ev_conferencia = LAST_INSERT_ID();

-- Evento 5: FINALIZADO - Taller pasado
INSERT INTO eventos (nombre, descripcion, ubicacion, capacidad_maxima, fecha_inicio, fecha_fin,
  tiempo_cancelacion_horas, tiempo_tolerancia_minutos, estado, id_categoria, creado_por, fecha_creacion) VALUES
('Taller de Pitch y Modelo de Negocio', 'Aprende a estructurar tu idea de negocio y presentarla ante inversionistas. Incluye ejercicios prácticos y retroalimentación.',
 'Laboratorio de Emprendimiento', 40,
 DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY) + INTERVAL 3 HOUR,
 12, 15, 'FINALIZADO', @cat_emprende, @id_superadmin, DATE_SUB(NOW(), INTERVAL 20 DAY));
SET @ev_taller = LAST_INSERT_ID();

-- Evento 6: CANCELADO - Evento cancelado
INSERT INTO eventos (nombre, descripcion, ubicacion, capacidad_maxima, fecha_inicio, fecha_fin,
  tiempo_cancelacion_horas, tiempo_tolerancia_minutos, estado, id_categoria, creado_por, fecha_creacion) VALUES
('Maratón de Programación Web', 'Competencia individual de desarrollo web frontend y backend en tiempo limitado.',
 'Laboratorio de Cómputo 3', 50,
 DATE_ADD(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY) + INTERVAL 8 HOUR,
 24, 10, 'CANCELADO', @cat_tech, @id_admin1, DATE_SUB(NOW(), INTERVAL 15 DAY));
SET @ev_cancelado = LAST_INSERT_ID();

-- --------------------------------------------------------
-- 2.8 EVENTO-ORGANIZADOR (tabla de relación)
-- --------------------------------------------------------
INSERT INTO evento_organizador (id_evento, id_organizador) VALUES
(@ev_hackathon, @org_ti),
(@ev_festival, @org_cultura),
(@ev_torneo, @org_deportes),
(@ev_conferencia, @org_academica),
(@ev_conferencia, @org_ti),
(@ev_taller, @org_incubadora),
(@ev_cancelado, @org_ti);

-- --------------------------------------------------------
-- 2.9 PRE-CHECKIN (Inscripciones)
-- --------------------------------------------------------

-- Hackathon: 8 inscritos
INSERT INTO pre_checkin (id_usuario, id_evento, fecha_registro, estado) VALUES
(@id_alumno1,  @ev_hackathon, NOW(), 'ACTIVO'),
(@id_alumno2,  @ev_hackathon, NOW(), 'ACTIVO'),
(@id_alumno3,  @ev_hackathon, NOW(), 'ACTIVO'),
(@id_alumno4,  @ev_hackathon, NOW(), 'ACTIVO'),
(@id_alumno5,  @ev_hackathon, NOW(), 'ACTIVO'),
(@id_alumno6,  @ev_hackathon, NOW(), 'ACTIVO'),
(@id_alumno7,  @ev_hackathon, NOW(), 'ACTIVO'),
(@id_alumno8,  @ev_hackathon, NOW(), 'ACTIVO');

-- Festival Cultural: 6 inscritos (1 cancelado)
INSERT INTO pre_checkin (id_usuario, id_evento, fecha_registro, estado) VALUES
(@id_alumno1,  @ev_festival, NOW(), 'ACTIVO'),
(@id_alumno3,  @ev_festival, NOW(), 'ACTIVO'),
(@id_alumno5,  @ev_festival, NOW(), 'ACTIVO'),
(@id_alumno7,  @ev_festival, NOW(), 'ACTIVO'),
(@id_alumno9,  @ev_festival, NOW(), 'ACTIVO'),
(@id_alumno10, @ev_festival, NOW(), 'CANCELADO');

-- Torneo: 5 inscritos
INSERT INTO pre_checkin (id_usuario, id_evento, fecha_registro, estado) VALUES
(@id_alumno2,  @ev_torneo, NOW(), 'ACTIVO'),
(@id_alumno4,  @ev_torneo, NOW(), 'ACTIVO'),
(@id_alumno6,  @ev_torneo, NOW(), 'ACTIVO'),
(@id_alumno8,  @ev_torneo, NOW(), 'ACTIVO'),
(@id_alumno10, @ev_torneo, NOW(), 'ACTIVO');

-- Conferencia IA (finalizado): 7 inscritos
INSERT INTO pre_checkin (id_usuario, id_evento, fecha_registro, estado) VALUES
(@id_alumno1,  @ev_conferencia, DATE_SUB(NOW(), INTERVAL 15 DAY), 'ACTIVO'),
(@id_alumno2,  @ev_conferencia, DATE_SUB(NOW(), INTERVAL 15 DAY), 'ACTIVO'),
(@id_alumno3,  @ev_conferencia, DATE_SUB(NOW(), INTERVAL 14 DAY), 'ACTIVO'),
(@id_alumno4,  @ev_conferencia, DATE_SUB(NOW(), INTERVAL 14 DAY), 'ACTIVO'),
(@id_alumno5,  @ev_conferencia, DATE_SUB(NOW(), INTERVAL 13 DAY), 'ACTIVO'),
(@id_alumno7,  @ev_conferencia, DATE_SUB(NOW(), INTERVAL 12 DAY), 'ACTIVO'),
(@id_alumno9,  @ev_conferencia, DATE_SUB(NOW(), INTERVAL 11 DAY), 'ACTIVO');

-- Taller Pitch (finalizado): 5 inscritos
INSERT INTO pre_checkin (id_usuario, id_evento, fecha_registro, estado) VALUES
(@id_alumno1,  @ev_taller, DATE_SUB(NOW(), INTERVAL 10 DAY), 'ACTIVO'),
(@id_alumno3,  @ev_taller, DATE_SUB(NOW(), INTERVAL 10 DAY), 'ACTIVO'),
(@id_alumno5,  @ev_taller, DATE_SUB(NOW(), INTERVAL 9 DAY), 'ACTIVO'),
(@id_alumno8,  @ev_taller, DATE_SUB(NOW(), INTERVAL 9 DAY), 'ACTIVO'),
(@id_alumno10, @ev_taller, DATE_SUB(NOW(), INTERVAL 8 DAY), 'ACTIVO');

-- Maratón cancelado: 3 inscritos (todos cancelados)
INSERT INTO pre_checkin (id_usuario, id_evento, fecha_registro, estado) VALUES
(@id_alumno2,  @ev_cancelado, DATE_SUB(NOW(), INTERVAL 5 DAY), 'CANCELADO'),
(@id_alumno6,  @ev_cancelado, DATE_SUB(NOW(), INTERVAL 5 DAY), 'CANCELADO'),
(@id_alumno4,  @ev_cancelado, DATE_SUB(NOW(), INTERVAL 4 DAY), 'CANCELADO');

-- --------------------------------------------------------
-- 2.10 ASISTENCIAS (solo eventos finalizados)
-- --------------------------------------------------------

-- Conferencia IA: 5 de 7 asistieron
INSERT INTO asistencias (id_usuario, id_evento, fecha_checkin, metodo, estado) VALUES
(@id_alumno1, @ev_conferencia, DATE_SUB(NOW(), INTERVAL 10 DAY), 'QR', 'PRESENTE'),
(@id_alumno2, @ev_conferencia, DATE_SUB(NOW(), INTERVAL 10 DAY), 'QR', 'PRESENTE'),
(@id_alumno3, @ev_conferencia, DATE_SUB(NOW(), INTERVAL 10 DAY), 'MANUAL', 'PRESENTE'),
(@id_alumno4, @ev_conferencia, DATE_SUB(NOW(), INTERVAL 10 DAY), 'QR', 'PRESENTE'),
(@id_alumno5, @ev_conferencia, DATE_SUB(NOW(), INTERVAL 10 DAY), 'QR', 'PRESENTE');

-- Taller Pitch: 4 de 5 asistieron
INSERT INTO asistencias (id_usuario, id_evento, fecha_checkin, metodo, estado) VALUES
(@id_alumno1,  @ev_taller, DATE_SUB(NOW(), INTERVAL 5 DAY), 'QR', 'PRESENTE'),
(@id_alumno3,  @ev_taller, DATE_SUB(NOW(), INTERVAL 5 DAY), 'QR', 'PRESENTE'),
(@id_alumno5,  @ev_taller, DATE_SUB(NOW(), INTERVAL 5 DAY), 'MANUAL', 'PRESENTE'),
(@id_alumno8,  @ev_taller, DATE_SUB(NOW(), INTERVAL 5 DAY), 'QR', 'PRESENTE');

-- --------------------------------------------------------
-- 2.11 DIPLOMAS (solo eventos finalizados)
-- --------------------------------------------------------

-- Diploma para Conferencia IA
INSERT INTO diplomas (id_evento, nombre_evento, firma, diseno, fecha_creacion, estado) VALUES
(@ev_conferencia, 'Conferencia de Inteligencia Artificial', 'Dr. Roberto Sánchez', 'clasico',
 DATE_SUB(NOW(), INTERVAL 8 DAY), 'ACTIVO');
SET @diploma_conf = LAST_INSERT_ID();

-- Diploma para Taller Pitch
INSERT INTO diplomas (id_evento, nombre_evento, firma, diseno, fecha_creacion, estado) VALUES
(@ev_taller, 'Taller de Pitch y Modelo de Negocio', 'Lic. Patricia Morales', 'moderno',
 DATE_SUB(NOW(), INTERVAL 3 DAY), 'ACTIVO');
SET @diploma_taller = LAST_INSERT_ID();

-- --------------------------------------------------------
-- 2.12 DIPLOMAS EMITIDOS
-- --------------------------------------------------------

-- Conferencia: 5 diplomas emitidos (a los que asistieron)
INSERT INTO diplomas_emitidos (id_diploma, id_usuario, fecha_envio, estado_envio) VALUES
(@diploma_conf, @id_alumno1, DATE_SUB(NOW(), INTERVAL 7 DAY), 'ENVIADO'),
(@diploma_conf, @id_alumno2, DATE_SUB(NOW(), INTERVAL 7 DAY), 'ENVIADO'),
(@diploma_conf, @id_alumno3, DATE_SUB(NOW(), INTERVAL 7 DAY), 'ENVIADO'),
(@diploma_conf, @id_alumno4, DATE_SUB(NOW(), INTERVAL 7 DAY), 'ENVIADO'),
(@diploma_conf, @id_alumno5, DATE_SUB(NOW(), INTERVAL 7 DAY), 'ENVIADO');

-- Taller: 3 de 4 emitidos (1 con error)
INSERT INTO diplomas_emitidos (id_diploma, id_usuario, fecha_envio, estado_envio) VALUES
(@diploma_taller, @id_alumno1, DATE_SUB(NOW(), INTERVAL 2 DAY), 'ENVIADO'),
(@diploma_taller, @id_alumno3, DATE_SUB(NOW(), INTERVAL 2 DAY), 'ENVIADO'),
(@diploma_taller, @id_alumno5, DATE_SUB(NOW(), INTERVAL 2 DAY), 'ENVIADO'),
(@diploma_taller, @id_alumno8, DATE_SUB(NOW(), INTERVAL 2 DAY), 'ERROR');

-- ============================================================
-- RESUMEN DE DATOS INSERTADOS
-- ============================================================
-- Roles:         3 (ALUMNO, ADMINISTRADOR, SUPERADMIN)
-- Usuarios:      13 (1 superadmin + 1 admin + 11 alumnos)
-- Administradores: 2
-- Alumnos:       11 (10 activos + 1 inactivo)
-- Categorías:    5
-- Organizadores: 5
-- Eventos:       6 (3 activos + 2 finalizados + 1 cancelado)
-- Inscripciones: 34
-- Asistencias:   9
-- Diplomas:      2
-- Diplomas Emitidos: 9 (8 enviados + 1 error)
-- ============================================================

-- ============================================================
-- CREDENCIALES DE ACCESO PARA PRUEBAS
-- ============================================================
-- SuperAdmin:  admin@eventnode.com        / Admin@1234
-- Admin:       carlos.admin@eventnode.com / Admin@1234
-- Alumno:      maria.garcia@utez.edu.mx  / Alumno@123
-- Alumno:      juan.lopez@utez.edu.mx    / Alumno@123
-- (todos los alumnos usan: Alumno@123)
-- ============================================================

SELECT '✅ Datos de prueba insertados correctamente' AS resultado;
SELECT CONCAT(COUNT(*), ' usuarios') AS total FROM usuarios
UNION ALL
SELECT CONCAT(COUNT(*), ' eventos') FROM eventos
UNION ALL
SELECT CONCAT(COUNT(*), ' inscripciones') FROM pre_checkin
UNION ALL
SELECT CONCAT(COUNT(*), ' asistencias') FROM asistencias
UNION ALL
SELECT CONCAT(COUNT(*), ' diplomas') FROM diplomas;
