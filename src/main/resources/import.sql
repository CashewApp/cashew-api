INSERT INTO role(authority) VALUES("USER");
INSERT INTO role(authority) VALUES("EMPLOYEE");
INSERT INTO role(authority) VALUES("OWNER");
INSERT INTO partner(partnerid, sign_up_date, partner_public_key, name, cpf, email, password) VALUES (2, now(), UNHEX(REPLACE(UUID(), '-', '')), "Teste teste", "46718760862", "teste.teste@teste.com", "senha123");
INSERT INTO owner(owner_partner_id) VALUES(2);
INSERT INTO university(universityid, university_public_key, acronym, name, state) VALUES (1, UNHEX(REPLACE(UUID(), '-', '')), "USCS", "Universidade Municipal de Sao Caetano do Sul", "SP");
INSERT into campus(campusid, universityid, name, public_key) values (1, 1, "Conceicao", UNHEX(REPLACE(UUID(), '-', '')));
INSERT into campus(campusid, universityid, name, public_key) values (2, 1, "Centro", UNHEX(REPLACE(UUID(), '-', '')));
INSERT into campus(campusid, universityid, name, public_key) values (3, 1, "Barcelona", UNHEX(REPLACE(UUID(), '-', '')));
INSERT into cafeteria(campusid, ownerid, cafeteriaid, cnpj, public_key, description, name, phone) VALUES(1, 2, 1, "76455365000172", UNHEX(REPLACE(UUID(), '-', '')), "Cantina da biblioteca", "Cantina Teste", "(11) 3391-0031");
