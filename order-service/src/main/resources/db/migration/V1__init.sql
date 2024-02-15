CREATE TABLE IF NOT EXISTS categories
(
    id        uuid         NOT NULL primary key,
    name      VARCHAR(255) NOT NULL,
    icon_path VARCHAR(255) NOT NULL
);


create table languages
(
    id      bigserial
        primary key,
    chinese varchar(255),
    english varchar(255),
    kazakh  varchar(255),
    russian varchar(255)
);



CREATE TABLE IF NOT EXISTS statuses
(
    id          uuid                NOT NULL PRIMARY KEY,
    language_id bigint, FOREIGN KEY (language_id) REFERENCES languages (id)
);


CREATE TABLE IF NOT EXISTS orders
(
    id         uuid NOT NULL PRIMARY KEY,
    order_name VARCHAR(255),
    track_code VARCHAR(255) UNIQUE,
    user_id    uuid,
    status_id  uuid REFERENCES statuses (id),
    creation_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS containers
(
    id         uuid NOT NULL PRIMARY KEY,
    created_at TIMESTAMP(6),
    name       VARCHAR(255) UNIQUE,
    status_id  uuid REFERENCES statuses (id)
);

CREATE TABLE IF NOT EXISTS container_orders
(
    container_id uuid NOT NULL REFERENCES containers (id),
    order_id     uuid NOT NULL UNIQUE REFERENCES orders (id)
);


CREATE TABLE IF NOT EXISTS order_status
(
    id        uuid NOT NULL PRIMARY KEY,
    order_id  uuid REFERENCES orders (id),
    status_id uuid REFERENCES statuses (id)
);



INSERT INTO languages(id, kazakh, english, russian, chinese)
VALUES (1, 'Тапсырыс алынды', 'Order received', 'Заказ получен', '收到的订单');

INSERT INTO languages(id, kazakh, english, russian, chinese)
VALUES (2, 'Қоймадан жөнелтілді', 'Shipped from the warehouse', 'Отгружено со склада', '从仓库发货');

INSERT INTO languages(id, kazakh, english, russian, chinese)
VALUES (3, 'Кедендік ресімдеу кезінде', 'Under customs clearance', 'При таможенном оформлении', '海关清关服务');

INSERT INTO languages(id, kazakh, english, russian, chinese)
VALUES (4, 'Алматы сұрыптау пунктінде', 'In Almaty sorting point', 'В Алматинском сортировочном пункте', '在阿拉木图分拣点');

INSERT INTO languages(id, kazakh, english, russian, chinese)
VALUES (5, 'Алып кетуге дайын', 'Ready to pick up', 'Готовы забрать', '准备接电话');

INSERT INTO languages(id, kazakh, english, russian, chinese)
VALUES (6, 'Жеткізілді', 'Delivered', 'Доставлен', '交付');

INSERT INTO statuses(id, language_id)
VALUES ('7050543b-b370-4dfd-bc6b-888542aa26ca', 1);
INSERT INTO statuses(id, language_id)
VALUES ('813a14f5-265f-4bb0-8dad-879d93e836aa', 2);
INSERT INTO statuses(id, language_id)
VALUES ('258697fa-21b5-4e65-80f9-c287ec77a722', 3);
INSERT INTO statuses(id, language_id)
VALUES ('3a7b1136-2a5f-41a6-bedf-1f7968b2a781', 4);
INSERT INTO statuses(id, language_id)
VALUES ('9a2f6db1-cf73-4d7b-bbf5-93632a8f070a', 5);
INSERT INTO statuses(id, language_id)
VALUES ('1cbd354c-341a-43ac-9136-94d2068896db', 6);