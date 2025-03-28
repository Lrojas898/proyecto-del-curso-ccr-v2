INSERT INTO eps (nit, name) VALUES
                                ('123456789', 'Salud Total'),
                                ('987654321', 'Coomeva'),
                                ('456789123', 'Sanitas'),
                                ('555666777', 'Sura'),
                                ('222333444', 'Compensar'),
                                ('225588999', 'Nueva EPS');

INSERT INTO prepaid_medicine (nit, name) VALUES
                                             ('111222333', 'Compensar Medicina Prepagada'),
                                             ('444555666', 'Sura Medicina Prepagada'),
                                             ('101424794', 'Salud Total Medicina Prepagada'),
                                             ('553451745', 'Coomeva Medicina Prepagada'),
                                             ('227894265', 'Sanitas Medicina Prepagada'),
                                             ('777989879', 'Nueva EPS Medicina Prepagada');

INSERT INTO role (id, name) VALUES
                                ('ROLE001', 'Patient'),
                                ('ROLE002', 'Doctor'),
                                ('ROLE003', 'Medical Assistant');

INSERT INTO PERMISSION (id, name, description) VALUES
    ('PERM001', 'Manage Users', 'Allows creating, editing, and deleting users');

INSERT INTO PERMISSION (id, name, description) VALUES
    ('PERM002', 'View Medical Records', 'Allows viewing clinical histories');

INSERT INTO PERMISSION (id, name, description) VALUES
    ('PERM003', 'Schedule Appointments', 'Allows scheduling appointments');

INSERT INTO assistance_act_type (id, name) VALUES
                                               ('ACT001', 'General Consultation'),
                                               ('ACT002', 'Surgical Procedure'),
                                               ('ACT003', 'Diagnostic Exam');

INSERT INTO exam_type (id, name) VALUES
                                     ('EXAM001', 'X-Ray'),
                                     ('EXAM002', 'Blood Test'),
                                     ('EXAM003', 'Ultrasound');

INSERT INTO location (id, name, address, description) VALUES
                                                          ('LOC001', 'Central Clinic', '123 Street #45-67', 'Main clinic in the city center'),
                                                          ('LOC002', 'North Office', '89 Avenue #12-34', 'Specialized consultation office');

INSERT INTO myuser (id, username, password, email, first_name, last_name, address, phone, sex, date_of_birth, eps_nit, prepaid_medicine_nit) VALUES
                                                                                                                                                 ('USER001', 'johnperez', 'hashedpassword1', 'john.perez@email.com', 'John', 'Perez', '10th Street #20-30', '3001234567', 'M', '1990-05-15', '123456789', '111222333'),
                                                                                                                                                 ('USER002', 'marygomez', 'hashedpassword2', 'mary.gomez@email.com', 'Mary', 'Gomez', '15th Street #25-40', '3002345678', 'F', '1985-08-22', '987654321', '444555666'),
                                                                                                                                                 ('USER003', 'drcarlos', 'hashedpassword3', 'carlos.lopez@email.com', 'Carlos', 'Lopez', '30th Avenue #50-60', '3003456789', 'M', '1975-03-10', '456789123', '111222333');

INSERT INTO patient (id, blood_type, weight, height, myuser_id) VALUES
                                                                  ('PAT001', 'O+', 70.5, 1.75, 'USER001'),
                                                                  ('PAT002', 'A-', 65.0, 1.60, 'USER002');

INSERT INTO staff (id, profession_id, undergraduate, phone_extension, speciality, myuser_id) VALUES
    ('STAFF001', 'PROF001', 'General Medicine - National University', '1234', 'Cardiology', 'USER003');

INSERT INTO clinical_history (id, date, patient_id, hour, general_observations) VALUES
                                                                                    ('CH001', '2025-03-01', 'PAT001', '2025-03-01 09:00:00', 'Patient with a history of hypertension'),
                                                                                    ('CH002', '2025-03-02', 'PAT002', '2025-03-02 10:30:00', 'Patient with no significant history');

INSERT INTO role_permission (role_id, permission_id) VALUES
                                                         ('ROLE001', 'PERM001'), -- Admin can manage users
                                                         ('ROLE002', 'PERM002'), -- Doctor can view medical records
                                                         ('ROLE003', 'PERM003'); -- Patient can schedule appointments


INSERT INTO user_role (myuser_id, role_id) VALUES
                                             ('USER001', 'ROLE003'), -- John is a patient
                                             ('USER002', 'ROLE003'), -- Mary is a patient
                                             ('USER003', 'ROLE002'); -- Carlos is a doctor


INSERT INTO appointment (id, date, start_time, description, patient_id, staff_id, location_id) VALUES
                                                                                                   ('APP001', '2025-03-25', '2025-03-25 14:00:00', 'Follow-up consultation', 'PAT001', 'STAFF001', 'LOC001'),
                                                                                                   ('APP002', '2025-03-26', '2025-03-26 15:30:00', 'General check-up', 'PAT002', 'STAFF001', 'LOC002');

INSERT INTO attention_episode (id, creation_date, clinical_history_id) VALUES
                                                                           ('EP001', '2025-03-01', 'CH001'),
                                                                           ('EP002', '2025-03-02', 'CH002');

INSERT INTO assistance_act (id, issue_date, description, attention_episode_id, assistance_act_type_id) VALUES
                                                                                                           ('ACTREC001', '2025-03-01', 'Consultation for chest pain', 'EP001', 'ACT001'),
                                                                                                           ('ACTREC002', '2025-03-02', 'Blood test requested', 'EP002', 'ACT003');


INSERT INTO exam_result (id, result_date, description, attached, exam_type_id, patient_id) VALUES
    ('EXRES001', '2025-03-03', 'Normal results', 'Attached PDF file', 'EXAM002', 'PAT002');