CREATE TABLE USERS (
  id bigint NOT NULL AUTO_INCREMENT,
  first_name varchar(255),
  last_name varchar(255),
  email_id varchar(255),
  password varchar(255),
  active BOOLEAN,
  PRIMARY KEY (id)
);

CREATE sequence group_seq;

CREATE TABLE DECK (
    id bigint NOT NULL AUTO_INCREMENT,
    backside_key varchar(255),
    visible_as_public BOOLEAN,
    name varchar(255),
    description varchar(255),
    images_on_card bigint,
    owner bigint,
  PRIMARY KEY (id),
  FOREIGN KEY (owner) REFERENCES USERS(id)
);

CREATE TABLE IMAGE (
    id bigint NOT NULL AUTO_INCREMENT,
    url varchar(255) UNIQUE NOT NULL,
    deck_id bigint,
    FOREIGN KEY (deck_id) REFERENCES DECK(id)
);

ALTER TABLE DECK ADD FOREIGN KEY (backside_key) REFERENCES IMAGE(url);


CREATE TABLE CARD (
    id bigint NOT NULL AUTO_INCREMENT,
    deckId bigint,
  PRIMARY KEY (id),
  FOREIGN KEY (deckId) REFERENCES DECK(id)
);

CREATE TABLE CARD_IMAGE (
    id bigint NOT NULL AUTO_INCREMENT,
    scale_factor bigint,
    position_x bigint,
    position_y bigint,
    rotation_angle bigint,
    card_id bigint,
    image_id bigint,
  PRIMARY KEY (id),
  FOREIGN KEY (card_id) REFERENCES CARD(id),
  FOREIGN KEY (image_id) REFERENCES IMAGE(id)

);