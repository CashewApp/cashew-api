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
INSERT INTO category(categoryid, name, cafeteriaid) VALUES(1, "salgados", 1);
INSERT INTO stock(stockid) VALUES(1);
INSERT INTO product(productid, name, description, price, categoryid, cafeteriaid, stockid, public_key) VALUES(1, "coxinha", "Coxinha deliciosa de frango", 5, 1, 1, 1, UNHEX(REPLACE(UUID(), '-', '')));
INSERT INTO stock(stockid) VALUES(2);
INSERT INTO product(productid, name, description, price, categoryid, cafeteriaid, stockid, public_key) VALUES(2, "pastel", "Pastel de frango", 10, 1, 1, 2, UNHEX(REPLACE(UUID(), '-', '')));
INSERT INTO stock(stockid) VALUES(3);
INSERT INTO product(productid, name, description, price, categoryid, cafeteriaid, stockid, public_key, status) VALUES(3, "pastel", "Pastel de frango", 10, 1, 1, 3, UNHEX(REPLACE(UUID(), '-', '')), true);
INSERT INTO stock(stockid) VALUES(4);
INSERT INTO product(productid, name, description, price, categoryid, cafeteriaid, stockid, public_key, status) VALUES(4, "pao de queijo", "Grande bola de queijo", 10, 1, 1, 4, UNHEX(REPLACE(UUID(), '-', '')), true);
INSERT INTO stock(stockid) VALUES(5);
INSERT INTO product(productid, name, description, price, categoryid, cafeteriaid, stockid, public_key, status) VALUES(5, "Hamburgao", "Carne deliciosa de pombo", 10, 1, 1, 5, UNHEX(REPLACE(UUID(), '-', '')), true);
INSERT INTO stock(stockid) VALUES(6);
INSERT INTO product(productid, name, description, price, categoryid, cafeteriaid, stockid, public_key, status) VALUES(6, "Pizza", "Pizza deliciosa de atum", 10, 1, 1, 6, UNHEX(REPLACE(UUID(), '-', '')), true)

