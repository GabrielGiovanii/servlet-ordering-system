DROP DATABASE IF EXISTS ordering_system;

CREATE DATABASE ordering_system;
USE ordering_system;

CREATE TABLE tb_user (
    id BIGINT AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(100) NOT NULL,
    role INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tb_category (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE tb_product (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(100),
    img_url VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES tb_category(id)
);

CREATE TABLE tb_order (
    id BIGINT AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    order_status INT NOT NULL,
    moment TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES tb_user(id)
);

CREATE TABLE tb_order_item (
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES tb_order(id),
    FOREIGN KEY (product_id) REFERENCES tb_product(id)
);

CREATE TABLE tb_payment (
    id BIGINT AUTO_INCREMENT,
    order_id BIGINT NOT NULL UNIQUE,
    payment_method INT NOT NULL,
    moment TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES tb_order(id)
);

INSERT INTO tb_user (name, email, phone, password, role)
VALUES
    ('Gabriel Giovani', 'gabriel@teste.com', '18997081452', '$2a$10$yh0Lrnlk533v35EE2Ujt1up9hlcOwJblJws7YJkoNGBA7Uy5bzo62', 1),
    ('Maria Silva', 'maria@teste.com', '11987654321', '$2a$10$ZU6gWe0rIBQbNMeF2Rt3ieNSO47CKa0qW.1Y7H3QuT2rm1yImx/bW', 2),
    ('João Oliveira', 'joao@teste.com', '11999998888', '$2a$10$YhTEmiiLmSDTa3GYmhbU7ugoepJP1UZK8TEVi3qXKGZbkQCRokF5S', 2);

INSERT INTO tb_category (name) VALUES
('Eletrônicos'),
('Livros'),
('Vestuário'),
('Eletrodomésticos');

INSERT INTO tb_product (name, description, img_url, price, category_id) VALUES
    ('Dvd Player Karaokê',
     'Dvd Player com função karaokê, ideal para festas e reuniões.',
     'https://product-hub-prd.madeiramadeira.com.br/1605458/images/63d1cc38-ffed-4826-8c9b-1cc085ba3d9217425048dvdplayerkaraokebritaniafama6pentradausbcopiacddiretoparamp3eacompanha1microfoneean78913560558501500x500.jpg?width=700&canvas=1:1&bg-color=FFF',
     299.99,
     1),
    ('Livro de Receitas',
     'Um livro cheio de receitas deliciosas.',
     'https://quantobaste.com.br/wp-content/uploads/2022/11/capa-de-livro-de-receitas-basico.jpg',
     49.99,
     2),
    ('Camisa Polo',
     'Camisa polo de algodão confortável.',
     'https://hering.vtexassets.com/arquivos/ids/1519951-800-auto/036H-8GEN-C1.jpg?v=638575319863270000',
     79.99,
     3),
    ('Liquidificador 500w',
     'Liquidificador de 500w.',
     'https://images-americanas.b2w.io/produtos/7490617160/imagens/liquidificador-500w-1-9l-mondial-l500-b-2vel-easy-power-preto-110v-110v/7490617160_1_xlarge.jpg',
     259.99,
     4),
    ('Liquidificador 400w',
     'Liquidificador de 400w.',
     'https://images-americanas.b2w.io/produtos/7483338250/imagens/liquidificador-semp-toshiba-400w-2-5l-220v-branco/7483338251_1_xlarge.jpg',
     199.99,
     4);


#Pedido 1
INSERT INTO tb_order (client_id, order_status, moment)
VALUES (2, 1, '2024-10-01 14:30:00');

INSERT INTO tb_order_item (order_id, product_id, quantity, price)
VALUES
    (LAST_INSERT_ID(), 1, 2, 299.99),
    (LAST_INSERT_ID(), 2, 1, 49.99);

#Pedido 2
INSERT INTO tb_order (client_id, order_status, moment)
VALUES (2, 1, '2024-10-02 09:00:00');

INSERT INTO tb_order_item (order_id, product_id, quantity, price)
VALUES
    (LAST_INSERT_ID(), 3, 1, 79.99);

#Pedido 3
INSERT INTO tb_order (client_id, order_status, moment)
VALUES (3, 1, '2024-10-15 10:00:00');

INSERT INTO tb_order_item (order_id, product_id, quantity, price)
VALUES
    (LAST_INSERT_ID(), 1, 1, 299.99),
    (LAST_INSERT_ID(), 4, 2, 199.99);