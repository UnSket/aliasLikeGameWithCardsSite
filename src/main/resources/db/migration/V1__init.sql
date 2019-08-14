CREATE TABLE users (
  id bigint NOT NULL AUTO_INCREMENT,
  firstName varchar(255),
  lastName varchar(255),
  emailId varchar(255),
  password varchar(255),
  active BOOLEAN,
  PRIMARY KEY (id)
)
