CREATE TABLE product (
    id       uuid           PRIMARY KEY NOT NULL ,
    version  INTEGER        NOT NULL,
    name     VARCHAR(255)   UNIQUE NOT NULL,
    quantity INTEGER        NOT NULL
);
