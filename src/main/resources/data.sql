INSERT INTO USER (username, email, birth_date, password, active, enabled, secret, IS_USING2FA, IS_USING_OAUTH) VALUES ('user1', 'mail1@gmail.com', '2001-01-21', '$2a$10$oj0oxo.tkF6eObfqbocmYuskIBRylrBTjYOZSz71bMExzpPsE18PW', 1, 1, 'IJQXGZJTGIXHEYLOMRXW2KBJ', 0, 0);
--pw 12345
INSERT INTO USER (username, email, birth_date, password, active, enabled, secret, IS_USING2FA, IS_USING_OAUTH) VALUES ('user2', 'mail2@gmail.com', '2002-02-22', '$2a$10$ARb16PjKyNdkIVUbJs2PHeH5i92BNMpLHmvmyM6UYaEUIF/KtQpNK', 1, 1, '3SLSWZPQQBB7HBRYDAQZ5J77W5D7I6GU', 1, 0);
--pw 123456
INSERT INTO USER (username, email, birth_date, password, active, enabled, secret, IS_USING2FA, IS_USING_OAUTH) VALUES ('user3', 'mail3@gmail.com', '2003-03-23', '$2a$10$g7Meu5.Z1ojL852dRGBcmeQYxS/n3Ck9Wew1MC3PzjuSFE59tHoj.', 1, 1, '3SLSWZPQQBB7AERYDAQZ5J77W5D7I6GU', 0, 0);
--pw 1234567
INSERT INTO USER (username, email, birth_date, password, active, enabled, secret, IS_USING2FA, IS_USING_OAUTH) VALUES ('user4', 'mail4@gmail.com', '2004-04-24', '$2a$10$vls0KEorXPKgFtyhB27kCe9LZaqrtpp.ptMRhq0t2Fp97oP6AREnO', 1, 1, '3SLSWZPQQBB7WBRYDAQZ5J77W5D7I6GU', 0, 0);
--pw 12345678
INSERT INTO USER (username, email, birth_date, password, active, enabled, secret, IS_USING2FA, IS_USING_OAUTH) VALUES ('user5', 'mail5@gmail.com', '2005-05-25', '$2a$10$MKgzpchWnSQk9NjWgvt2hu1FXTamk6c.JwWCWy.j6Ic2lKPnWn7Ti', 1, 1, '3SLSWZPQQBBGWBRYDAQZ5J77W5D7I6GU', 0, 0);
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

INSERT INTO category(category_id,category_name) Values(1,'Sport');
INSERT INTO category(category_id,category_name) Values(2,'Alltag');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time,dislikes,likes,tutorial_category_category_id, tutorial_id, title, content_Text) values (1,now(),0,0,2,1,'Do a pullup', 'I started with one pullup a day and everyday one more. Now I can do 50 pullups. Strap your Shoulders and pressure');
INSERT INTO tutorial(created_by_channel_channel_id, creation_time,dislikes,likes,tutorial_category_category_id, tutorial_id, title, content_Text) values (1,now(),0,0,2,2,'Do a backflip','Picture yourself standing at the edge of a pristine lake. The sun is setting, casting a golden glow across the water. The air is crisp and filled with the sound of nature.' );