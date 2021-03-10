DELETE FROM category_products;
DELETE FROM category;
DELETE FROM product;

INSERT INTO category (id, name) VALUES (1, 'Mountain Bike');
INSERT INTO category (id, name) VALUES (2, 'City Bike');

INSERT INTO product (name, price, id) VALUES ('King boa', 1000, 1);
INSERT INTO product (name, price, id) VALUES ('Contrail', 2466, 2);
INSERT INTO product (name, price, id) VALUES ('Beltdrive', 1749, 3);
INSERT INTO product (name, price, id) VALUES ('Metropoli Uno Acera', 595, 4);

INSERT INTO category_products (categories_id, products_id) VALUES (1, 1);
INSERT INTO category_products (categories_id, products_id) VALUES (1, 2);
INSERT INTO category_products (categories_id, products_id) VALUES (2, 3);
INSERT INTO category_products (categories_id, products_id) VALUES (2, 4);