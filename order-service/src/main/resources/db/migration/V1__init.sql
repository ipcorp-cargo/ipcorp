CREATE TABLE IF NOT EXISTS categories(
    id uuid NOT NULL primary key,
    name VARCHAR(255) NOT NULL,
    icon_path VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS statuses(
    id uuid NOT NULL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);


CREATE TABLE IF NOT EXISTS containers(
    id uuid NOT NULL PRIMARY KEY,
    created_at TIMESTAMP(6),
    name VARCHAR(255) UNIQUE,
    status_id uuid REFERENCES statuses(id) UNIQUE
);

CREATE TABLE IF NOT EXISTS orders(
    id uuid NOT NULL PRIMARY KEY,
    order_name VARCHAR(255),
    track_code VARCHAR(255),
    user_id uuid,
    status_id uuid REFERENCES statuses(id)
);

CREATE TABLE IF NOT EXISTS container_orders(
    container_id uuid NOT NULL REFERENCES containers(id),
    order_id uuid NOT NULL UNIQUE REFERENCES orders(id)
);

CREATE TABLE IF NOT EXISTS order_status(
    id uuid NOT NULL PRIMARY KEY,
    order_id uuid REFERENCES orders(id),
    status_id uuid REFERENCES statuses(id)
);

INSERT INTO statuses(id, name) VALUES ('7050543b-b370-4dfd-bc6b-888542aa26ca','ORDER_RECEIVED');
INSERT INTO statuses(id, name) VALUES ('813a14f5-265f-4bb0-8dad-879d93e836aa','SHIPPED_FROM_WAREHOUSE');
INSERT INTO statuses(id, name) VALUES ('258697fa-21b5-4e65-80f9-c287ec77a722','UNDER_CUSTOMS_CLEARANCE');
INSERT INTO statuses(id, name) VALUES ('3a7b1136-2a5f-41a6-bedf-1f7968b2a781','ALMATY_SORTING_POINT');
INSERT INTO statuses(id, name) VALUES ('9a2f6db1-cf73-4d7b-bbf5-93632a8f070a','READY_TO_PICKUP');
INSERT INTO statuses(id, name) VALUES ('1cbd354c-341a-43ac-9136-94d2068896db','DELIVERED');