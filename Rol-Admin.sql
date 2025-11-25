USE bebidapp;
SET SQL_SAFE_UPDATES = 0;

-- 1. Asegurar que los roles existan con el nombre correcto (MAYÚSCULAS) 
-- Si ya existen, esto no hará daño. Si están mal escritos, los arregla. 
UPDATE roles SET rol = 'Administrador' WHERE rol LIKE 'Admin%';
UPDATE roles SET rol = 'Cliente' WHERE rol LIKE 'Client%' OR rol LIKE 'User%';
UPDATE roles SET rol = 'Vendedor' WHERE rol LIKE 'Vende%';

-- 2. Limpiar tus roles actuales para evitar duplicados 
-- Reemplaza 'tu_email@ejemplo.com' por tu email REAL con el que te registraste 
DELETE FROM usuario_rol 
WHERE id_usuario = (SELECT id_usuario FROM usuarios WHERE email = 'tobi2005bonanno@gmail.com');

-- 3. Asignarte el rol de ADMINISTRADOR 
INSERT INTO usuario_rol (id_usuario, id_rol)
SELECT 
    (SELECT id_usuario FROM usuarios WHERE email = 'tobi2005bonanno@gmail.com'), -- Tu Usuario
    (SELECT id_rol FROM roles WHERE rol = 'Administrador');                -- El Rol Admin

SET SQL_SAFE_UPDATES = 1;

-- 4. Verificar resultado 
SELECT u.email, u.nombre, r.rol 
FROM usuarios u
JOIN usuario_rol ur ON u.id_usuario = ur.id_usuario
JOIN roles r ON r.id_rol = ur.id_rol
WHERE u.email = 'tobi2005bonanno@gmail.com';