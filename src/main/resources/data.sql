INSERT INTO USER (id, username, email, birth_date, password, active) VALUES (1, 'user1', 'mail1@gmail.com', '2001-01-21', '12345', 1);
INSERT INTO USER (id, username, email, birth_date, password, active) VALUES (2, 'user2', 'mail2@gmail.com', '2002-02-22', '123456', 1);
INSERT INTO USER (id, username, email, birth_date, password, active) VALUES (3, 'user3', 'mail3@gmail.com', '2003-03-23', '1234567', 1);
INSERT INTO USER (id, username, email, birth_date, password, active) VALUES (4, 'user4', 'mail4@gmail.com', '2004-04-24', '12345678', 1);
INSERT INTO USER (id, username, email, birth_date, password, active) VALUES (5, 'user5', 'mail5@gmail.com', '2005-05-25', '123456789', 1);

INSERT INTO CHANNEL (userid, channelname, description, creation_date) VALUES (1, 'HowTo1', 'Description1', '2025-01-21');
INSERT INTO CHANNEL (userid, channelname, description, creation_date) VALUES (2, 'HowTo2', 'Description2', '2025-02-22');
INSERT INTO CHANNEL (userid, channelname, description, creation_date) VALUES (3, 'HowTo3', 'Description3', '2025-03-23');
INSERT INTO CHANNEL (userid, channelname, description, creation_date) VALUES (4, 'HowTo4', 'Description4', '2025-04-24');
INSERT INTO CHANNEL (userid, channelname, description, creation_date) VALUES (5, 'HowTo5', 'Description5', '2025-05-25');

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