CREATE TABLE pages (
        id INTEGER(11) NOT NULL AUTO_INCREMENT,
        slug VARCHAR(50) NOT NULL UNIQUE,
        title VARCHAR(100),
        description VARCHAR(200),
        photo VARCHAR(100) DEFAULT 'default.png',
        font_color VARCHAR(7) DEFAULT '#212121',
        background_type VARCHAR(5) DEFAULT 'COLOR',
        background_value VARCHAR(50) DEFAULT '#F4F4F4',
        created_at DATETIME NOT NULL,
        updated_at DATETIME NOT NULL,
        account_id INTEGER(11) NOT NULL,

        PRIMARY KEY (id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE pages ADD CONSTRAINT fk_account FOREIGN KEY(account_id) REFERENCES accounts(id);