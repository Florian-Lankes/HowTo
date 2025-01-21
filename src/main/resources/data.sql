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
INSERT INTO category(category_id, category_name) Values (3, 'Fitness');
INSERT INTO category(category_id, category_name) Values (4, 'Kochen');
INSERT INTO category(category_id, category_name) Values (5, 'Reisen');
INSERT INTO category(category_id, category_name) Values (6, 'Technik');
INSERT INTO category(category_id, category_name) Values (7, 'DIY');
INSERT INTO category(category_id, category_name) Values (8, 'Sprachen');
INSERT INTO category(category_id, category_name) Values (9, 'Garten');
INSERT INTO category(category_id, category_name) Values (10, 'Kunst');
INSERT INTO category(category_id, category_name) Values (11, 'Literatur');
INSERT INTO category(category_id, category_name) Values (12, 'Musik');
INSERT INTO category(category_id, category_name) Values (13, 'Natur');
INSERT INTO category(category_id, category_name) Values (14, 'Wissenschaft');
INSERT INTO category(category_id, category_name) Values (15, 'Geschichte');
INSERT INTO category(category_id, category_name) Values (16, 'Film');
INSERT INTO category(category_id, category_name) Values (17, 'Mode');
INSERT INTO category(category_id, category_name) Values (18, 'Bildung');
INSERT INTO category(category_id, category_name) Values (19, 'Gesundheit');
INSERT INTO category(category_id, category_name) Values (20, 'Ernährung');
INSERT INTO category(category_id, category_name) Values (21, 'Astronomie');
INSERT INTO category(category_id, category_name) Values (22, 'Philosophie');


INSERT INTO tutorial(created_by_channel_channel_id, creation_time,dislikes,likes,tutorial_category_category_id, tutorial_id, title, content_Text) values (1,now(),0,0,1,1,'Do a pullup', 'I started with one pullup a day and everyday one more. Now I can do 50 pullups. Strap your Shoulders and pressure');
INSERT INTO tutorial(created_by_channel_channel_id, creation_time,dislikes,likes,tutorial_category_category_id, tutorial_id, title, content_Text) values (1,now(),0,0,1,2,'Do a backflip','Picture yourself standing at the edge of a pristine lake. The sun is setting, casting a golden glow across the water. The air is crisp and filled with the sound of nature.' );
INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 1, 3, 'Einen Klimmzug machen', 'Ich begann mit einem Klimmzug pro Tag und fügte jeden Tag einen weiteren hinzu. Jetzt kann ich 50 Klimmzüge machen. Schultern straffen und Druck ausüben');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 1, 4, 'Einen Rückwärtssalto machen', 'Stellen Sie sich vor, Sie stehen am Ufer eines unberührten Sees. Die Sonne geht unter und taucht das Wasser in ein goldenes Licht. Die Luft ist frisch und erfüllt von Naturgeräuschen');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 1, 5, 'Einen Handstand lernen', 'Üben Sie täglich, indem Sie sich gegen eine Wand lehnen, um das Gleichgewicht zu trainieren. Stück für Stück werden Sie stärker und stabiler.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 1, 6, 'Einen Marathon laufen', 'Beginnen Sie mit kurzen Läufen und erhöhen Sie schrittweise die Distanz. Ernähren Sie sich gesund und bleiben Sie hydratisiert.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 4, 7, 'Kochen lernen', 'Wählen Sie einfache Rezepte aus, beginnen Sie mit Grundnahrungsmitteln und experimentieren Sie nach und nach mit neuen Zutaten und Techniken.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 7, 8, 'Ein Fahrrad reparieren', 'Lernen Sie, wie man einen platten Reifen wechselt, die Bremsen justiert und die Kette ölt. So bleibt Ihr Fahrrad immer in Top-Zustand.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 18, 9, 'Ein Gedicht schreiben', 'Schreiben Sie Ihre Gedanken und Gefühle auf Papier. Lassen Sie sich von der Natur, Musik oder persönlichen Erlebnissen inspirieren.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 9, 10, 'Einen Garten anlegen', 'Wählen Sie Pflanzen aus, die in Ihrem Klima gedeihen, und bereiten Sie den Boden gut vor. Pflanzen Sie Blumen, Gemüse oder Kräuter und genießen Sie Ihren eigenen kleinen Garten.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 7, 11, 'Einen Schal stricken', 'Lernen Sie die Grundtechniken des Strickens, wie Maschen anschlagen, rechte und linke Maschen stricken. Bald können Sie Ihren eigenen warmen Schal stricken.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 4, 12, 'Einen Kuchen backen', 'Finden Sie ein Rezept, das Ihnen gefällt, und folgen Sie den Anweisungen sorgfältig. Bald können Sie einen leckeren Kuchen aus dem Ofen holen und genießen.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 8, 13, 'Eine Fremdsprache lernen', 'Tägliches Üben ist der Schlüssel. Verwenden Sie Apps, Bücher, Filme und Gespräche, um Ihre Fähigkeiten zu verbessern. Geduld und Beständigkeit führen zum Erfolg.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 19, 14, 'Meditation praktizieren', 'Finden Sie einen ruhigen Ort und setzen Sie sich bequem hin. Konzentrieren Sie sich auf Ihre Atmung und lassen Sie Ihre Gedanken zur Ruhe kommen.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 6, 15, 'Einen Flugzeugmodell bauen', 'Holen Sie sich ein Modellbausatz und folgen Sie den Anweisungen. Geduld und Präzision sind entscheidend, um ein schönes Modell zu bauen.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 1, 16, 'Einen Bogen schießen', 'Lernen Sie die richtige Technik und üben Sie regelmäßig. Bald werden Sie in der Lage sein, Ihr Ziel sicher zu treffen.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 11, 17, 'Ein Buch schreiben', 'Setzen Sie sich Ziele und schreiben Sie regelmäßig. Lassen Sie sich von Ihrer eigenen Geschichte leiten und haben Sie Geduld mit dem Prozess.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 7, 18, 'Einen Tischlerkurs machen', 'Lernen Sie die Grundlagen der Holzbearbeitung und erstellen Sie einfache Möbelstücke. Mit Übung und Geschick können Sie bald Ihre eigenen Projekte realisieren.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 9, 19, 'Einen Komposthaufen anlegen', 'Sammeln Sie organische Abfälle und beginnen Sie mit dem Kompostieren. Ihr Garten wird von den nährstoffreichen Ergebnissen profitieren.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 9, 20, 'Einen Vogelfutterhaus bauen', 'Verwenden Sie einfache Materialien und Bauanleitungen, um ein sicheres und attraktives Futterhaus für Vögel zu schaffen.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 1, 21, 'Einen Berg besteigen', 'Bereiten Sie sich gut vor und trainieren Sie Ihre Ausdauer. Wählen Sie eine Route, die Ihrem Können entspricht, und genießen Sie das Abenteuer.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 7, 22, 'Ein Aquarium einrichten', 'Wählen Sie die richtige Größe und Ausstattung für Ihre Fische. Pflegen Sie das Aquarium regelmäßig, um eine gesunde Umgebung zu gewährleisten.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 6, 23, 'Einen Roboter bauen', 'Nutzen Sie einen Bausatz und lernen Sie die Grundlagen der Robotik. Mit Geduld und Kreativität können Sie Ihren eigenen funktionierenden Roboter erstellen.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 1, 24, 'Einen Survival-Kurs belegen', 'Lernen Sie wichtige Überlebenstechniken wie Feuer machen, Wasser finden und Erste Hilfe leisten. Diese Fähigkeiten können in Notsituationen lebensrettend sein.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 6, 25, 'Eine App programmieren', 'Erlernen Sie die Grundlagen der Programmierung und setzen Sie Ihr Wissen in einem eigenen App-Projekt um. Mit Zeit und Übung können Sie eine nützliche Anwendung erstellen.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 1, 26, 'Einen Fußball trainieren', 'Üben Sie regelmäßig Dribbling, Pässe und Schüsse. Mit konsequentem Training und Hingabe können Sie Ihre Fähigkeiten kontinuierlich verbessern.');

INSERT INTO tutorial(created_by_channel_channel_id, creation_time, dislikes, likes, tutorial_category_category_id, tutorial_id, title, content_Text) 
values (1, now(), 0, 0, 6, 27, 'Eine Website erstellen', 'Nutzen Sie Baukästen oder lernen Sie HTML und CSS. Gestalten Sie eine ansprechende und funktionale Website, die Ihre Inhalte und Ideen präsentiert.');
