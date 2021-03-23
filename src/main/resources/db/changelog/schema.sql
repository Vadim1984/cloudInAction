--liquibase formatted sql

--changeset vadym.shapoval:0000001
CREATE TABLE product (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(255),
    price decimal(19,2) NOT NULL,
    PRIMARY KEY (id)
);
--rollback DROP TABLE product;

--changeset vadym.shapoval:0000002
CREATE TABLE category (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(255),
    PRIMARY KEY (id)
);
--rollback DROP TABLE category;

--changeset vadym.shapoval:0000003
CREATE TABLE category_products (
    categories_id bigint NOT NULL,
    products_id bigint NOT NULL,
    FOREIGN KEY (categories_id) REFERENCES category(id),
    FOREIGN KEY (products_id) REFERENCES product(id)
);
--rollback DROP TABLE category_products;
