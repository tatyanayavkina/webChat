CREATE TABLE IF NOT EXISTS user(
  id int(11) NOT NULL AUTO_INCREMENT,
  login varchar(256),
  password varchar(256),
  nickname varchar(256) NOT NULL,
  last_request DATETIME,
  deleted BOOLEAN DEFAULT FALSE,
  PRIMARY KEY(id)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;

INSERT INTO user (login, password, nickname) VALUES ("admin@mail.ru","root","admin");

CREATE TABLE IF NOT EXISTS room(
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(256) NOT NULL,
  type TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS user_link_room(
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  room_id int(11) NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT fk_user_room_id FOREIGN KEY (user_id)
    REFERENCES user(id) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT fk_room_user_id FOREIGN KEY (room_id)
    REFERENCES room(id) ON DELETE CASCADE ON UPDATE NO ACTION
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS message(
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  room_id int(11) NOT NULL,
  creation_time DATETIME NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT fk_user_message_id FOREIGN KEY (user_id)
    REFERENCES user(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_room_message_id FOREIGN KEY (room_id)
    REFERENCES room(id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS role(
  id int(11) NOT NULL AUTO_INCREMENT,
  code varchar(256) NOT NULL,
  PRIMARY KEY(id)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;

INSERT INTO role (code) VALUES ("ROLE_GUEST");
INSERT INTO role (code) VALUES ("ROLE_USER");
INSERT INTO role (code) VALUES ("ROLE_ADMIN");

CREATE TABLE IF NOT EXISTS user_link_role(
  id int(11) NOT NULL AUTO_INCREMENT,
  user_id int(11) NOT NULL,
  role_id int(11) NOT NULL,
  PRIMARY KEY(id),
  CONSTRAINT fk_user_role_id FOREIGN KEY (user_id)
  REFERENCES user(id) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT fk_role_user_id FOREIGN KEY (role_id)
  REFERENCES role(id) ON DELETE CASCADE ON UPDATE NO ACTION
)ENGINE=InnoDB  DEFAULT CHARSET=utf8;

INSERT INTO user_link_role (user_id, role_id) VALUES (1,1);
INSERT INTO user_link_role (user_id, role_id) VALUES (1,2);
INSERT INTO user_link_role (user_id, role_id) VALUES (1,3);