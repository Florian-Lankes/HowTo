INSERT INTO USER (username, email, birth_date, password, active, enabled, secret, IS_USING2FA) VALUES ('user1', 'mail1@gmail.com', '2001-01-21', '$2a$10$oj0oxo.tkF6eObfqbocmYuskIBRylrBTjYOZSz71bMExzpPsE18PW', 1, 1, 'IJQXGZJTGIXHEYLOMRXW2KBJ', 0);
--pw 12345
INSERT INTO USER (username, email, birth_date, password, active, enabled, secret, IS_USING2FA) VALUES ('user2', 'mail2@gmail.com', '2002-02-22', '$2a$10$ARb16PjKyNdkIVUbJs2PHeH5i92BNMpLHmvmyM6UYaEUIF/KtQpNK', 1, 1, '3SLSWZPQQBB7HBRYDAQZ5J77W5D7I6GU', 1);
--pw 123456
INSERT INTO USER (username, email, birth_date, password, active, enabled, secret, IS_USING2FA) VALUES ('user3', 'mail3@gmail.com', '2003-03-23', '$2a$10$g7Meu5.Z1ojL852dRGBcmeQYxS/n3Ck9Wew1MC3PzjuSFE59tHoj.', 1, 1, '3SLSWZPQQBB7AERYDAQZ5J77W5D7I6GU', 0);
--pw 1234567
INSERT INTO USER (username, email, birth_date, password, active, enabled, secret, IS_USING2FA) VALUES ('user4', 'mail4@gmail.com', '2004-04-24', '$2a$10$vls0KEorXPKgFtyhB27kCe9LZaqrtpp.ptMRhq0t2Fp97oP6AREnO', 1, 1, '3SLSWZPQQBB7WBRYDAQZ5J77W5D7I6GU', 0);
--pw 12345678
INSERT INTO USER (username, email, birth_date, password, active, enabled, secret, IS_USING2FA) VALUES ('user5', 'mail5@gmail.com', '2005-05-25', '$2a$10$MKgzpchWnSQk9NjWgvt2hu1FXTamk6c.JwWCWy.j6Ic2lKPnWn7Ti', 1, 1, '3SLSWZPQQBBGWBRYDAQZ5J77W5D7I6GU', 0);
--pw 123456789
INSERT INTO CHANNEL (channel_id, channelname, description, creation_date) VALUES (1, 'HowTo1', 'Description1', '2025-01-21');
--INSERT INTO CHANNEL (channelname, description, creation_date) VALUES ('HowTo2', 'Description2', '2025-02-22');
--INSERT INTO CHANNEL (channelname, description, creation_date) VALUES ('HowTo3', 'Description3', '2025-03-23');
--INSERT INTO CHANNEL (channelname, description, creation_date) VALUES ('HowTo4', 'Description4', '2025-04-24');
--INSERT INTO CHANNEL (channelname, description, creation_date) VALUES ('HowTo5', 'Description5', '2025-05-25');

INSERT INTO role (description) VALUES ('ADMIN');
INSERT INTO role (description) VALUES ('CREATOR');
INSERT INTO role (description) VALUES ('USER');

INSERT INTO authority (description) VALUES ('ADMIN_RIGHTS');
INSERT INTO authority (description) VALUES ('CREATOR_RIGHTS');
INSERT INTO authority (description) VALUES ('VIEW');


INSERT INTO userrole(iduser, idrole) VALUES (1,1);
INSERT INTO userrole(iduser, idrole) VALUES (1,2);
INSERT INTO userrole(iduser, idrole) VALUES (2,2);
INSERT INTO userrole(iduser, idrole) VALUES (3,3);
 

INSERT INTO roleauthority(idrole, idauthority) VALUES (1,1);
INSERT INTO roleauthority(idrole, idauthority) VALUES (1,2);
INSERT INTO roleauthority(idrole, idauthority) VALUES (1,3);
INSERT INTO roleauthority(idrole, idauthority) VALUES (2,2);
INSERT INTO roleauthority(idrole, idauthority) VALUES (2,3);
INSERT INTO roleauthority(idrole, idauthority) VALUES (3,3);