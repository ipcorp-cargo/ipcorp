CREATE TABLE IF NOT EXISTS languages(
    id      SERIAL NOT NULL PRIMARY KEY,
    kazakh  VARCHAR(255),
    english VARCHAR(255),
    russian VARCHAR(255),
    chinese VARCHAR(255),
    description_kazakh VARCHAR(255),
    description_english VARCHAR(255),
    description_russian VARCHAR(255),
    description_chinese VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS statuses(
    id          uuid NOT NULL PRIMARY KEY,
    language_id INTEGER,
    FOREIGN KEY (language_id) REFERENCES languages (id)
);


CREATE TABLE IF NOT EXISTS branches(
    id            uuid NOT NULL PRIMARY KEY,
    language_id bigint, FOREIGN KEY (language_id) REFERENCES languages (id)
);


CREATE TABLE IF NOT EXISTS users(
    id           uuid NOT NULL PRIMARY KEY,
    password     VARCHAR(255),
    phone_number VARCHAR(255) UNIQUE,
    role         VARCHAR(255)
        CONSTRAINT users_role_check
            CHECK ((role)::TEXT = ANY ((ARRAY ['ADMIN'::CHARACTER VARYING, 'USER'::CHARACTER VARYING])::TEXT[])),
    branch_id    uuid REFERENCES branches (id)
);


CREATE TABLE IF NOT EXISTS user_statuses
(
    id        uuid NOT NULL PRIMARY KEY,
    status_id uuid REFERENCES statuses (id),
    user_id   uuid REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS verification
(
    phone_number  VARCHAR(15) NOT NULL PRIMARY KEY,
    code          VARCHAR(6),
    count         INTEGER,
    creation_date TIMESTAMP(6),
    is_valid      BOOLEAN
);



INSERT INTO languages(id, kazakh, english, russian, chinese,
                      description_kazakh, description_english,description_russian, description_chinese)
VALUES (1, 'kazakh', 'Order received', 'kazakh', 'chinese',
        'desc kazakh', 'desc english', 'desc russian', 'desc chinese');

INSERT INTO languages(id, kazakh, english, russian, chinese,
                      description_kazakh, description_english,description_russian, description_chinese)
VALUES (2, 'kazakh', 'Shipped from the warehouse', 'english', 'chinese',
        'desc kazakh', 'desc english', 'desc russian', 'desc chinese');


INSERT INTO languages(id, kazakh, english, russian, chinese,
                      description_kazakh, description_english,description_russian, description_chinese)
VALUES (3, 'kazakh', 'Under customs clearance', 'russian', 'chinese',
        'desc kazakh', 'desc english', 'desc russian', 'desc chinese');

INSERT INTO languages(id, kazakh, english, russian, chinese,
                      description_kazakh, description_english,description_russian, description_chinese)
VALUES (4, 'kazakh', 'In Almaty sorting point', 'russian', 'chinese',
        'desc kazakh', 'desc english', 'desc russian', 'desc chinese');

INSERT INTO languages(id, kazakh, english, russian, chinese,
                      description_kazakh, description_english,description_russian, description_chinese)
VALUES (5, 'kazakh', 'Ready to pick up', 'russian', 'chinese',
        'desc kazakh', 'desc english', 'desc russian', 'desc chinese');

INSERT INTO languages(id, kazakh, english, russian, chinese,
                      description_kazakh, description_english,description_russian, description_chinese)
VALUES (6, 'kazakh', 'Delivered', 'russian', 'chinese',
        'desc kazakh', 'desc english', 'desc russian', 'desc chinese');

INSERT INTO languages(id, kazakh, english, russian, chinese)
VALUES (7, 'Б. Момышұлы 75А', 'B. Momyshuly 75A', 'Б. Момышулы 75А', 'Б. Момышулы 75А');
INSERT INTO languages(id, kazakh, english, russian, chinese)
VALUES (8, 'Абай даңғылы 56в', '56v Abai Avenue', 'проспект Абая 56в', '阿拜道56b号');
INSERT INTO languages(id, kazakh, english, russian, chinese)
VALUES (9, 'Солтүстік Тас Жол Сақинасы', 'The Northern Ring Highway', 'Северное Кольцо Шоссе', '北环公路');


INSERT INTO statuses(id, language_id)
VALUES ('7050543b-b370-4dfd-bc6b-888542aa26ca' , 1);
INSERT INTO statuses(id, language_id)
VALUES ('813a14f5-265f-4bb0-8dad-879d93e836aa', 2);
INSERT INTO statuses(id, language_id)
VALUES ('258697fa-21b5-4e65-80f9-c287ec77a722', 3);
INSERT INTO statuses(id, language_id)
VALUES ('3a7b1136-2a5f-41a6-bedf-1f7968b2a781', 4);
INSERT INTO statuses(id, language_id)
VALUES ('9a2f6db1-cf73-4d7b-bbf5-93632a8f070a',  5);
INSERT INTO statuses(id, language_id)
VALUES ('1cbd354c-341a-43ac-9136-94d2068896db',  6);

INSERT INTO branches(id, language_id)
VALUES ('7050543b-b370-4dfd-bc6b-888542aa26ca', 7);
INSERT INTO branches(id, language_id)
VALUES ('813a14f5-265f-4bb0-8dad-879d93e836aa', 8);
INSERT INTO branches(id, language_id)
VALUES ('258697fa-21b5-4e65-80f9-c287ec77a722', 9);

INSERT INTO users(id, password, phone_number, role)
VALUES ('0c6b9bf0-0c60-4a39-ab3f-0019d2fab9e2', '$2a$12$m0TJD2xZ6/RZysNGQSQ7qeap0gcWkaJELKJEcjYBdMHyUq55evoaq', '111',
        'ADMIN');
INSERT INTO users(id, password, phone_number, role)
VALUES ('3e5e03f7-9fb0-445f-b54d-db7f997afff4', '$2a$12$4caK8vcPX7QQ71/P6XdBpuEucCydOlUnKfWHmoeEOWNVaN.cxkejO', '222',
        'ADMIN');
INSERT INTO users(id, password, phone_number, role)
VALUES ('685c0647-f08c-497f-a0a7-10cfcfa4ec11', '$2a$12$tSzMbOGQUvRr5OUuoYdDcui83o3hZS9/LCEYZjuUqVMe7EISe7aHm', '333',
        'ADMIN');
INSERT INTO users(id, password, phone_number, role)
VALUES ('9e90cec0-fd91-4674-8f83-c43e48498558', '$2a$12$cUk9.j7Hbu3bQKdUBgn8ke.b4mL5a1eGZvvifaKpqqErapIagZFyu', '444',
        'ADMIN');
INSERT INTO users(id, password, phone_number, role)
VALUES ('79c25fb0-8ba9-453e-a2d0-1592faa7de5c', '$2a$12$eMOAuZfiVzgODBZOb.KT2epw7JUXpIf9NakwPwSYDqOW83A8SSk/K', '555',
        'ADMIN');
INSERT INTO users(id, password, phone_number, role)
VALUES ('4a296a48-5717-4e6e-85ec-05cbf7cbcec1', '$2a$12$rkj34Xm07WT.K8OlJf8Ai.K1TtoGIjOrAuN1ZXoBeQyxh9nbg6z9u', '666',
        'ADMIN');

-- for admin_1(Warehouse):
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('a0bc5b98-ed64-45b4-8d10-65aa94c9dc2e', '7050543b-b370-4dfd-bc6b-888542aa26ca',
        '0c6b9bf0-0c60-4a39-ab3f-0019d2fab9e2');
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('28a36a3b-612a-460a-afa5-819c5c0ce23d', '813a14f5-265f-4bb0-8dad-879d93e836aa',
        '0c6b9bf0-0c60-4a39-ab3f-0019d2fab9e2');

-- for admin_2(Through container page):
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('e6a673df-ed85-4ef9-9c45-ddb450c5f803', '7050543b-b370-4dfd-bc6b-888542aa26ca',
        '3e5e03f7-9fb0-445f-b54d-db7f997afff4');
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('3c7d6680-e10c-4d39-a302-c353fdeb0456', '813a14f5-265f-4bb0-8dad-879d93e836aa',
        '3e5e03f7-9fb0-445f-b54d-db7f997afff4');
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('2cc95ee5-724a-482d-840b-2fffdceb7116', '258697fa-21b5-4e65-80f9-c287ec77a722',
        '3e5e03f7-9fb0-445f-b54d-db7f997afff4');
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('7fb9adab-925c-4f3f-9240-0e17f3605019', '3a7b1136-2a5f-41a6-bedf-1f7968b2a781',
        '3e5e03f7-9fb0-445f-b54d-db7f997afff4');

-- for admin_3(Almaty sorting point):
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('77a84f69-3378-4729-ab5b-c63da7b984b5', '258697fa-21b5-4e65-80f9-c287ec77a722',
        '685c0647-f08c-497f-a0a7-10cfcfa4ec11');
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('d452f805-3555-4961-99dd-1dcfa268a999', '3a7b1136-2a5f-41a6-bedf-1f7968b2a781',
        '685c0647-f08c-497f-a0a7-10cfcfa4ec11');

-- for admin_4(stocking the shelves):
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('309cc65f-d325-4a2a-869f-759e703dc6b4', '9a2f6db1-cf73-4d7b-bbf5-93632a8f070a',
        '9e90cec0-fd91-4674-8f83-c43e48498558');

-- for admin_5(Branches):
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('f997c25d-fea3-4201-a287-5a37d72d6214', '9a2f6db1-cf73-4d7b-bbf5-93632a8f070a',
        '79c25fb0-8ba9-453e-a2d0-1592faa7de5c');
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('ea22d3ac-efe4-47ad-b885-0834e2fb1e6e', '1cbd354c-341a-43ac-9136-94d2068896db',
        '79c25fb0-8ba9-453e-a2d0-1592faa7de5c');

-- for admin_6(Order pickup):
INSERT INTO user_statuses(id, status_id, user_id)
VALUES ('f6ffaac6-7b6e-43c0-a299-def6ef42edff', '1cbd354c-341a-43ac-9136-94d2068896db',
        '4a296a48-5717-4e6e-85ec-05cbf7cbcec1');