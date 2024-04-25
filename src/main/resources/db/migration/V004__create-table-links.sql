CREATE TABLE links (
   id INTEGER(11) NOT NULL AUTO_INCREMENT,
   status BOOLEAN DEFAULT FALSE,
   link_order INTEGER(6) NOT NULL,
   title VARCHAR(100) NOT NULL,
   href VARCHAR(100) NOT NULL,
   background_color VARCHAR(7) DEFAULT '#cacaca',
   text_color VARCHAR(7) DEFAULT '#212121',
   border_type VARCHAR(8) DEFAULT 'square',
   page_id INTEGER(11) NOT NULL,

   PRIMARY KEY (id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE links ADD CONSTRAINT fk_page_id FOREIGN KEY(page_id) REFERENCES pages(id);