DELETE FROM role;
INSERT INTO role (role_id, role) VALUES
	(1, 'ROLE_ADMIN'),
	(2, 'ROLE_USER');
DELETE FROM user;
INSERT INTO user (user_id, username, password, creation_date, last_login, enabled) VALUES
	(1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', '2017-11-12 12:00:00', NULL, 1);
DELETE FROM user_roles;
INSERT INTO user_roles (user_roles_id, user_id, role_id) VALUES
	(1, 1, 1),
	(2, 1, 2);