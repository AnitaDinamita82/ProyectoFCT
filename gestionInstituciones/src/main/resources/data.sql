    -- Insert tabla Alumno --
INSERT INTO instituto.alumnos (id,dni,nombre, apellido1, apellido2) VALUES 
(100,'23048962R','Juan', 'Pérez', 'Gómez'),
(101,'23698742A','María', 'López', 'Martínez'),
(102,'45896320T','Carlos', 'Hernández', 'Sánchez'),
(103,'14789652Q','Laura', 'Ramírez', 'Fernández'),
(104,'23014589S','Isabel', 'Moreno', 'Castro'),
(105,'23698541W','Carmen', 'Bolaños', 'Pérez'),
(106,'23654789W','Fernando', 'Torres', 'Gutiérrez');
/*(107,'23598741','José', 'Jiménez', 'Mendoza'),
(108,'23054892','Ana', 'Vásquez', 'Ríos'),
(109,'21547896','Sofía', 'Serrano', 'Cruz'),
(110,'12547896','Diego', 'Bermúdez', 'Salazar'),
(111,'48796520','Patricia', 'Alarcón', 'Vidal'),
(112,'12036577','Javier', 'Salcedo', 'Palacios');*/


-- Insert tabla Asignaturas --

INSERT INTO instituto.asignaturas (id,codigo,nombre, descripcion) VALUES 
(114,'DAM01','Acceso a datos','Conecta aplicaciones con bases de datos para manipular información.'),
(116,'DAM02','Desarrollo de Interfaces','Crea interfaces de usuario intuitivas y atractivas.'),
(117,'DAM03','Programación multimedia y dispositivos móviles',''),
(118,'DAM04','Programacion de Servicios y Procesos','Crea servicios y procesos que se ejecutan en segundo plano.'),
(119,'DAM05','Sistemas de gestión empresarial',' Conoce y utiliza software para la gestión integral de empresas.'),
(123,'DAW01','Desarrollo web en entorno cliente','Crea interfaces web interactivas usando lenguajes de cliente.'),
(124,'DAW02','Desarrollo web en entorno servidor','Desarrolla la lógica y funcionalidad del lado del servidor de aplicaciones web.'),
(125,'DAW03','Despliegue de aplicaciones web','Aprende a publicar y mantener aplicaciones web en servidores.'),
(126,'DAW04','Diseño de interfaces web', 'Diseña interfaces de usuario óptimas para entornos web.'),
(111,'DAC101','Sistemas Informáticos','Adquiere una visión global de la arquitectura y el funcionamiento de los sistemas informáticos.'),
(112,'DAC102','Base de Datos','Aprende los principios fundamentales del diseño y la manipulación de bases de datos.'),
(113,'DAC103','Programación','Desarrolla lógica y escribe código para crear aplicaciones.'),
(115,'DAC104','Entornos de Desarrollo','Configura y utiliza herramientas para el desarrollo de software.'),
(100,'DAC106','Inglés profesional','Desarrolla habilidades de comunicación en inglés para el ámbito laboral.'),
(128,'DAC107','Digitalización aplicada a los sectores productivos','Implementa soluciones digitales para optimizar procesos en diversos sectores.'),
(105,'DAC108','Lenguajes de marcas y sistemas de gestion de informacion',' Aprende a estructurar y presentar información usando lenguajes de marcas.'),
(129,'DAC201','Sostenibilidad aplicada al sistema productivo',' Integra prácticas sostenibles en la producción y gestión empresarial.'),
(131,'DAC202','Itinerario personal para la Empleabilidad II','Profundiza en el desarrollo personal y profesional para mejorar la empleabilidad.'),
(120,'DAC203','Proyecto intermodular de Desarrollo en Aplicaciones Web/Multiplataforma','Desarrolla un proyecto completo de aplicación web integrando conocimientos.'),
(122,'DAC204','Formacion en empresa','Aplica los conocimientos adquiridos en un entorno laboral real.'),
(130,'DAC105','Itinerario personal para la Empleabilidad I','Desarrolla habilidades y estrategias para la búsqueda activa de empleo.');

--Insert en tabla alumnos_asignaturas
/*INSERT INTO instituto.alumnos_asignaturas (dni_alu, codigo_asig) VALUES 
('23048962','MAT01'),
('23048962','FIS01'),
('23698742','MAT01'),
('45896320','QUI01'),
('23048962','HIS01'),
('23698541','MAT01');*/


/* Insert tabla Usuarios*/

INSERT INTO instituto.Usuarios (id,dni,login,password,rol) VALUES 
(0,'23037927T','anabelen.madrid','11111','ADMIN');
