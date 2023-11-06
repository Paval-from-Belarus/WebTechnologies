CREATE TABLE auction
(
    auction_Id         INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    event_date         TIMESTAMP                                           NULL,
    last_register_date TIMESTAMP                                           NULL,
    price_step         DECIMAL(10, 2)                                      NOT NULL,
    auction_type       TINYINT                                             NOT NULL,
    duration           TIMESTAMP                                           NULL,
    members_limit      INT                                                 NULL
);
CREATE TABLE auction_bet
(
    auction_bet_id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    bet            DECIMAL(10, 2)                                      NOT NULL,
    time           TIMESTAMP                                           NOT NULL,
    lot_id         INT                                                 NOT NULL,
    client_id      INT                                                 NOT NULL,
    auction_id     INT                                                 NOT NULL
);
CREATE TABLE auction_type_blind
(
    auction_id INT       NOT NULL PRIMARY KEY,
    bet_limit  INT       NOT NULL,
    timeout    TIMESTAMP NOT NULL
);
CREATE TABLE auction_type_blitz
(
    auction_id            INT       NOT NULL PRIMARY KEY,
    iteration_time        TIMESTAMP NOT NULL,
    members_exclude_limit INT       NOT NULL
);
CREATE TABLE auction_type_english_lot
(
    lot_id           INT            NOT NULL PRIMARY KEY,
    redemption_price DECIMAL(10, 2) NULL
);
CREATE TABLE user
(
    user_id       INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    name          VARCHAR(30)                                         NULL,
    phone_number  VARCHAR(12)                                         NULL,
    email         VARCHAR(320)                                        NULL,
    role          TINYINT                                             NULL,
    password_hash VARCHAR(60)                                         NULL,
    security_salt VARCHAR(16)                                         NULL
);
CREATE TABLE client
(
    user_id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    account DECIMAL(10, 2)                                      NULL,
    ranking DECIMAL(10, 1)                                      NULL,
    status  INT                                                 NULL
);
CREATE TABLE client_feedback
(
    client_feedback_id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    ranking            DECIMAL(10, 1)                                      NOT NULL,
    text               VARCHAR(200)                                        NOT NULL,
    lot_id             INT                                                 NOT NULL,
    client_author_id   INT                                                 NOT NULL,
    client_target_id   INT                                                 NOT NULL
);
CREATE TABLE delivery_point
(
    delivery_point_id   INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    city_code           VARCHAR(12)                                         NULL,
    street_name         VARCHAR(50)                                         NULL,
    house_number        VARCHAR(10)                                         NULL,
    delivery_point_name VARCHAR(50)                                         NULL
);
CREATE TABLE lot
(
    lot_id             INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) NOT NULL PRIMARY KEY,
    title              VARCHAR(50)                                         NULL,
    start_price        DECIMAL(10, 2)                                      NULL,
    actual_price       DECIMAL(10, 2)                                      NULL,
    auction_type       TINYINT                                             NULL,
    status             TINYINT                                             NULL,
    client_seller_id   INT                                                 NULL,
    client_customer_id INT                                                 NULL,
    delivery_point_id  INT                                                 NULL,
    auction_id         INT                                                 NULL
);
ALTER TABLE auction_bet
    ADD CONSTRAINT FK_auction_bet_auction
        FOREIGN KEY (auction_id) REFERENCES auction (auction_id) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE auction_bet
    ADD CONSTRAINT FK_auction_bet_client
        FOREIGN KEY (client_id) REFERENCES client (user_id) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE auction_bet
    ADD CONSTRAINT FK_auction_bet_lot
        FOREIGN KEY (lot_id) REFERENCES lot (lot_id) ON DELETE Restrict ON UPDATE Restrict
;
ALTER TABLE auction_type_blind
    ADD CONSTRAINT FK_blind_auction_auction
        FOREIGN KEY (auction_id) REFERENCES auction (auction_id) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE auction_type_blitz
    ADD CONSTRAINT FK_blitz_auction_auction
        FOREIGN KEY (auction_id) REFERENCES auction (auction_id) ON DELETE Restrict ON UPDATE Restrict
;
ALTER TABLE auction_type_english_lot
    ADD CONSTRAINT FK_auction_type_english_lot_lot
        FOREIGN KEY (lot_id) REFERENCES lot (lot_id) ON DELETE Restrict ON UPDATE Restrict
;
ALTER TABLE client
    ADD CONSTRAINT FK_client_user
        FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE client_feedback
    ADD CONSTRAINT FK_client_feedback_client
        FOREIGN KEY (client_author_id) REFERENCES client (user_id) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE client_feedback
    ADD CONSTRAINT FK_client_feedback_client_02
        FOREIGN KEY (client_target_id) REFERENCES client (user_id) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE client_feedback
    ADD CONSTRAINT FK_client_feedback_lot
        FOREIGN KEY (lot_id) REFERENCES lot (lot_id) ON DELETE Restrict ON UPDATE Restrict
;
ALTER TABLE lot
    ADD CONSTRAINT FK_lot_auction
        FOREIGN KEY (auction_id) REFERENCES auction (auction_id) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE lot
    ADD CONSTRAINT FK_lot_client
        FOREIGN KEY (client_seller_id) REFERENCES client (user_id) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE lot
    ADD CONSTRAINT FK_lot_client_02
        FOREIGN KEY (client_customer_id) REFERENCES client (user_id) ON DELETE Restrict ON UPDATE Restrict
;

ALTER TABLE lot
    ADD CONSTRAINT FK_lot_delivery_point
        FOREIGN KEY (delivery_point_id) REFERENCES delivery_point (delivery_point_id) ON DELETE Restrict ON UPDATE Restrict
;