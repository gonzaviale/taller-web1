INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
INSERT INTO Mascota(nombre, tipo, sangre) VALUES ('tobi', 'perrito', 'rojitaxd'), ('masi', 'donante', 'bordoboe');
INSERT INTO Banco (
    nombre_banco,
    direccion,
    ciudad,
    pais,
    telefono,
    email,
    password,
    horario
) VALUES (
             'Banco Test',
             '123 Calle Falsa',
             'Ciudad Test',
             'País Test',
             '+123456789',
             'banco@test.com',
             'password123',
             '09:00 - 18:00'
         );