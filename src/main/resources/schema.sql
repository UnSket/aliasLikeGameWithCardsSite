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

CREATE TABLE CARD (
    id bigint NOT NULL AUTO_INCREMENT,
    deckId bigint,
  PRIMARY KEY (id)
);

CREATE TABLE DECK (
    id bigint NOT NULL AUTO_INCREMENT,
    back_image_url bigint,
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
    url varchar(255),
    deck_id bigint,
    FOREIGN KEY (deck_id) REFERENCES DECK(id),
)