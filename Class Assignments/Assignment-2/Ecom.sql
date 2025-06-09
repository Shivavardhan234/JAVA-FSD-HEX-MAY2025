CREATE DATABASE ecom;
use ecom;


CREATE TABLE Customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100)NOT NULL,
    city VARCHAR(100)NOT NULL
);

CREATE TABLE Category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100)NOT NULL,
    price DECIMAL(10, 2)NOT NULL,
    description TEXT,
    category_id INT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Purchase (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date_of_purchase DATE NOT NULL,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Customer (id,name, city) VALUES 
(1,'Rahul Verma', 'Delhi'),
(2,'Sneha Kapoor', 'Mumbai'),
(3,'Arjun Mehta', 'Bengaluru'),
(4,'Pooja Sharma', 'Kolkata'),
(5,'Ravi Iyer', 'Chennai');

INSERT INTO Category (id,name) VALUES 
(1,'Smartphones'),
(2,'Laptops'),
(3,'Tablets'),
(4,'Accessories'),
(5,'Smart Home Devices');

INSERT INTO Product (id,title, price, description, category_id) VALUES 
(1,'Samsung Galaxy S23', 74999.00, 'Flagship Android smartphone with AMOLED display', 1),
(2,'Dell Inspiron 15', 59999.00, '15-inch laptop with Intel i5 processor and SSD storage', 2),
(3,'iPad Air 5', 54900.00, '10.9-inch tablet with M1 chip and Apple Pencil support', 3),
(4,'boAt Rockerz 255', 1499.00, 'Wireless Bluetooth neckband earphones', 4),
(5,'Amazon Echo Dot (5th Gen)', 4499.00, 'Smart speaker with Alexa voice assistant', 5);

INSERT INTO Purchase (id,date_of_purchase, customer_id, product_id) VALUES 
(1,'2025-05-10', 1, 1),
(2,'2025-05-11', 2, 2),
(3,'2025-05-12', 3, 3),
(4,'2025-05-13', 4, 4),
(5,'2025-05-14', 5, 5);

select*from purchase;
select*from product;
