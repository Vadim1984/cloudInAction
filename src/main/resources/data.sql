DELETE FROM category_products;
DELETE FROM category;
DELETE FROM product;

INSERT INTO category (id, name) VALUES (1, 'Mountain Bike');
INSERT INTO category (id, name) VALUES (2, 'City Bike');

INSERT INTO product (name, price, id) VALUES ('Bulls - King boa', 1000, 1);
INSERT INTO product (name, price, id) VALUES ('Bergamont - Contrail', 2466, 2);
INSERT INTO product (name, price, id) VALUES ('Santa cruz - bullit', 10499, 3);
INSERT INTO product (name, price, id) VALUES ('Beltdrive', 1749, 4);
INSERT INTO product (name, price, id) VALUES ('Bianch - Metropoli Uno Acera', 595, 5);

INSERT INTO category_products (categories_id, products_id) VALUES (1, 1);
INSERT INTO category_products (categories_id, products_id) VALUES (1, 2);
INSERT INTO category_products (categories_id, products_id) VALUES (1, 3);
INSERT INTO category_products (categories_id, products_id) VALUES (2, 4);
INSERT INTO category_products (categories_id, products_id) VALUES (2, 5);