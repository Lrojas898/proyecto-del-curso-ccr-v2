-- Insertar EPS (Entidades Promotoras de Salud)
INSERT INTO eps (nit, name) VALUES
                                ('eps123', 'EPS Ficticia'),
                                ('eps124', 'EPS Imaginaria'),
                                ('eps125', 'EPS Real');

-- Insertar Prepaid Medicine (Medicinas Prepagadas)
INSERT INTO prepaid_medicine (nit, name) VALUES
                                             ('prepaid123', 'Medicina Prepagada A'),
                                             ('prepaid124', 'Medicina Prepagada B'),
                                             ('prepaid125', 'Medicina Prepagada C');

-- Insertar Roles
INSERT INTO role (id, name) VALUES
                                ('role_patient', 'PATIENT'),
                                ('role_doctor', 'DOCTOR'),
                                ('role_admin', 'ADMIN');

-- Insertar Permisos
INSERT INTO permission (id, name, description) VALUES
                                                    ('permission_view_appointments', 'Ver citas', 'Permiso para ver citas'),
                                                    ('permission_manage_appointments', 'Gestionar citas', 'Permiso para gestionar citas'),
                                                    ('permission_view_exam_results', 'Ver resultados de exámenes', 'Permiso para ver resultados de exámenes'),
                                                    ('permission_manage_exam_results', 'Gestionar resultados de exámenes', 'Permiso para gestionar resultados de exámenes');

-- Insertar Relaciones entre Roles y Permisos
INSERT INTO role_permission (role_id, permission_id) VALUES
                                                           ('role_patient', 'permission_view_appointments'),
                                                           ('role_patient', 'permission_view_exam_results'),
                                                           ('role_doctor', 'permission_manage_appointments'),
                                                           ('role_doctor', 'permission_manage_exam_results'),
                                                           ('role_doctor', 'permission_view_appointments'),
                                                           ('role_doctor', 'permission_view_exam_results');

-- Insertar usuarios
INSERT INTO myuser (id, username, password, email, first_name, last_name, address, phone, sex, date_of_birth, eps_nit, prepaid_medicine_nit) VALUES
                                                                                                                                                 ('user1', 'jdoe', 'password123', 'jdoe@example.com', 'John', 'Doe', '123 Main St', '555-1234', 'M', '1980-05-15', 'eps123', 'prepaid123'),
                                                                                                                                                 ('user2', 'asmith', 'password123', 'asmith@example.com', 'Alice', 'Smith', '456 Elm St', '555-5678', 'F', '1985-08-25', 'eps124', 'prepaid124'),
                                                                                                                                                 ('user3', 'jgreen', 'password123', 'jgreen@example.com', 'James', 'Green', '789 Oak St', '555-9876', 'M', '1990-03-12', 'eps125', 'prepaid125'),
                                                                                                                                                 ('user4', 'mdavis', 'password123', 'mdavis@example.com', 'Michael', 'Davis', '321 Pine St', '555-2222', 'M', '1975-01-20', 'eps123', 'prepaid123'),
                                                                                                                                                 ('user5', 'lmiller', 'password123', 'lmiller@example.com', 'Laura', 'Miller', '654 Cedar St', '555-3333', 'F', '1982-07-18', 'eps124', 'prepaid124'),
                                                                                                                                                 ('user6', 'rjohnson', 'password123', 'rjohnson@example.com', 'Robert', 'Johnson', '987 Birch St', '555-4444', 'M', '1970-11-05', 'eps125', 'prepaid125'),
                                                                                                                                                 ('user7', 'lrojas', 'hola1234', 'lrojas@gmail.com', 'luis', 'rojas', 'jamuncho', '3102987635', 'M', '1984-12-01', 'eps123', 'prepaid125');

-- Insertar roles para usuarios
INSERT INTO user_role (myuser_id, role_id) VALUES
                                             ('user1', 'role_patient'),  -- John Doe es paciente
                                             ('user2', 'role_patient'),  -- Alice Smith es paciente
                                             ('user3', 'role_patient'),  -- James Green es paciente
                                             ('user4', 'role_doctor'),   -- Michael Davis es médico
                                             ('user5', 'role_doctor'),   -- Laura Miller es médico
                                             ('user6', 'role_doctor'),   -- Robert Johnson es médico
                                             ('user7', 'role_admin');  -- lucho es admin

-- Insertar tipos de asistencia
INSERT INTO assistance_act_type (id, name) VALUES
                                               ('exam1', 'Consulta médica'),
                                               ('exam2', 'Examen de rutina'),
                                               ('exam3', 'Radiografía');

-- Insertar ejemplos de ubicación
INSERT INTO location (id, name, address, description) VALUES
                                                          ('location1', 'Consultorio A', 'Calle Ficticia 123', 'Consultorio de atención general'),
                                                          ('location2', 'Consultorio B', 'Calle Imaginaria 456', 'Consultorio especializado en exámenes');

-- Insertar ejemplos de tipos de examen
INSERT INTO exam_type (id, name) VALUES
                                     ('exam1', 'Examen de sangre'),
                                     ('exam2', 'Radiografía de tórax');

-- Insertar algunos resultados de exámenes (ahora con las relaciones correctas)
INSERT INTO exam_result (id, result_date, description, exam_type_id, myuser_id, registered_by_id) VALUES
                                                                                                      ('exam_result1', '2025-03-25', 'Los resultados son normales', 'exam1', 'user1', 'user4'), -- Examen de sangre de John Doe realizado por Michael Davis
                                                                                                      ('exam_result2', '2025-03-26', 'Radiografía limpia', 'exam2', 'user2', 'user5'); -- Radiografía de Alice Smith realizada por Laura Miller


-- Insertar ejemplos de historia clínica
INSERT INTO clinical_history (id, date, myuser_id, general_observations) VALUES
                                                                             ('clinical_history1', '2025-03-20', 'user1', 'Paciente en buena salud, sin antecedentes importantes'),
                                                                             ('clinical_history2', '2025-03-21', 'user2', 'Paciente con antecedentes de hipertensión, controlando la presión arterial');


-- Insertar ejemplo de episodio de atención
INSERT INTO attention_episode (id, creation_date, clinical_history_id) VALUES
                                                                           ('attention_episode1', '2025-03-20', 'clinical_history1'),
                                                                           ('attention_episode2', '2025-03-21', 'clinical_history2');

-- Insertar ejemplo de actos de asistencia
INSERT INTO assistance_act (id, issue_date, description, attention_episode_id, assistance_act_type_id) VALUES
                                                                                                           ('assistance_act1', '2025-03-20', 'Examen de rutina', 'attention_episode1', 'exam1'),
                                                                                                           ('assistance_act2', '2025-03-21', 'Consulta médica', 'attention_episode2', 'exam2');



-- Insertar citas (appointments)
INSERT INTO appointment (id, date, start_time, description, location_id, user_id_patient, user_id_doctor) VALUES
                                                                                                              ('appointment1', '2025-03-20', '14:30:00', 'Consulta médica general', 'location1', 'user1', 'user4'),
                                                                                                              ('appointment2', '2025-03-21', '09:00:00', 'Examen de rutina', 'location2', 'user2', 'user5'),
                                                                                                              ('appointment3', '2025-03-22', '16:00:00', 'Consulta médica especializada', 'location1', 'user3', 'user6');