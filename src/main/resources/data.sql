INSERT INTO USER (id, username, email, birth_date, password, active) VALUES (1, 'user1', 'mail1@gmail.com', '2001-01-21', '12345', 1);
INSERT INTO USER (id, username, email, birth_date, password, active) VALUES (2, 'user2', 'mail2@gmail.com', '2002-02-22', '123456', 1);
INSERT INTO USER (id, username, email, birth_date, password, active) VALUES (3, 'user3', 'mail3@gmail.com', '2003-03-23', '1234567', 1);
INSERT INTO USER (id, username, email, birth_date, password, active) VALUES (4, 'user4', 'mail4@gmail.com', '2004-04-24', '12345678', 1);
INSERT INTO USER (id, username, email, birth_date, password, active) VALUES (5, 'user5', 'mail5@gmail.com', '2005-05-25', '123456789', 1);

INSERT INTO CHANNEL (userid, channelname, description, creation_date) VALUES (1, 'HowTo', 'Description1', '2025-01-21');

INSERT INTO role (description) VALUES ('ADMIN');
INSERT INTO role (description) VALUES ('CREATOR');

INSERT INTO authority (description) VALUES ('ADMIN_DASHBOARD');
INSERT INTO authority (description) VALUES ('LIST_STUDENT');
INSERT INTO authority (description) VALUES ('REGISTRATION');


INSERT INTO userrole(iduser, idrole) VALUES (1,1);
INSERT INTO userrole(iduser, idrole) VALUES (1,2);
INSERT INTO userrole(iduser, idrole) VALUES (2,2);
 

INSERT INTO roleauthority(idrole, idauthority) VALUES (1,1);
INSERT INTO roleauthority(idrole, idauthority) VALUES (1,3);
INSERT INTO roleauthority(idrole, idauthority) VALUES (2,2);