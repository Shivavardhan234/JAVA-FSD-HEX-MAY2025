CREATE DATABASE hex_asnmt;
use hex_asnmt;
drop  database hex_asnmt;
CREATE TABLE book(
id INT PRIMARY KEY,
price DECIMAL(15,2) NOT NULL default 0,
title VARCHAR(255) NOT NULL,
author VARCHAR(255) NOT NULL,
publication_house VARCHAR(255) NOT NULL,
category VARCHAR(255) NOT NULL,
book_count INT NOT NULL default 0,
status ENUM("IN_STOCK", 'OUT_OF_STOCK')
);
drop table book;

truncate table book;
INSERT INTO book (id, price, title, author, publication_house, category, book_count, status) VALUES
(1, 499.99, 'The Last Warrior', 'Ravi Subramanian', 'Mcgraw Hill', 'war', 10, 'IN_STOCK'),
(2, 299.50, 'Laugh Out Loud', 'Chetan Bhagat', 'DreamFolks', 'comedy', 0, 'OUT_OF_STOCK'),
(3, 699.00, 'Cricket Fever', 'Boria Majumdar', 'Warner Bros', 'sports', 5, 'IN_STOCK'),
(4, 599.25, 'The Fictional Mind', 'Anuja Chauhan', 'Mcgraw Hill', 'fiction', 3, 'IN_STOCK'),
(5, 459.75, 'Battle Cry', 'Vikram Chandra', 'DreamFolks', 'war', 0, 'OUT_OF_STOCK'),
(6, 250.00, 'The Comedian', 'Durjoy Datta', 'Warner Bros', 'comedy', 7, 'IN_STOCK'),
(7, 799.99, 'Field of Glory', 'Ashwin Sanghi', 'Mcgraw Hill', 'sports', 9, 'IN_STOCK'),
(8, 399.99, 'Dreamcatchers', 'Preeti Shenoy', 'DreamFolks', 'fiction', 0, 'OUT_OF_STOCK'),
(9, 349.00, 'Silent Guns', 'Arundhati Roy', 'Warner Bros', 'war', 4, 'IN_STOCK'),
(10, 289.50, 'Funny Bones', 'Savi Sharma', 'Mcgraw Hill', 'comedy', 6, 'IN_STOCK'),
(11, 520.00, 'Champions League', 'Harsha Bhogle', 'DreamFolks', 'sports', 0, 'OUT_OF_STOCK'),
(12, 310.10, 'Imaginary Lines', 'Amish Tripathi', 'Warner Bros', 'fiction', 2, 'IN_STOCK'),
(13, 445.45, 'Wartime Diaries', 'Kiran Desai', 'Mcgraw Hill', 'war', 0, 'OUT_OF_STOCK'),
(14, 275.00, 'Jokes Apart', 'Twinkle Khanna', 'DreamFolks', 'comedy', 8, 'IN_STOCK'),
(15, 620.00, 'The Goalpost', 'MS Dhoni', 'Warner Bros', 'sports', 11, 'IN_STOCK'),
(16, 380.90, 'Fantasy Realm', 'Rujuta Diwekar', 'Mcgraw Hill', 'fiction', 5, 'IN_STOCK'),
(17, 560.00, 'War & Wisdom', 'Shashi Tharoor', 'DreamFolks', 'war', 0, 'OUT_OF_STOCK'),
(18, 295.75, 'Haha Universe', 'Barkha Dutt', 'Warner Bros', 'comedy', 4, 'IN_STOCK'),
(19, 710.00, 'Tennis Titans', 'Sania Mirza', 'Mcgraw Hill', 'sports', 0, 'OUT_OF_STOCK'),
(20, 415.00, 'Fiction Files', 'Ankit Singh', 'DreamFolks', 'fiction', 6, 'IN_STOCK');


DELIMITER $$
CREATE PROCEDURE fetch_books_by_condition(IN p_price DECIMAL(15,2) )
BEGIN
SELECT * FROM book WHERE status = 'IN_STOCK' AND  price<p_price;
END $$

CALL fetch_books_by_condition(500 );


DELIMITER $$
CREATE PROCEDURE updt_price(IN inc INT , IN cat VARCHAR(255))
BEGIN
DECLARE i INT default 0;
DECLARE total_rows INT default 0;
DECLARE c_id INT default 0;
SELECT COUNT(id) into total_rows FROM book WHERE category=cat;

WHILE i<total_rows Do
SELECT id into c_id FROM book WHERE category=cat LIMIT i,1;
UPDATE book SET price= (price + (price*(inc/100))) WHERE id=c_id;
SET i=i+1;
END WHILE;
END $$
drop procedure updt_price;
CALL updt_price(2, "WAR");

DELIMITER $$
CREATE PROCEDURE dlt_book(IN cat VARCHAR(255))
BEGIN
DECLARE i INT default 0;
DECLARE total_rows INT default 0;
DECLARE c_id INT default 0;
SELECT COUNT(id) into total_rows FROM book WHERE category=cat;

WHILE i<total_rows Do
SELECT id into c_id FROM book WHERE category=cat LIMIT 1;
DELETE FROM book WHERE id= c_id;
SET i=i+1;
END WHILE;
END $$
drop procedure dlt_book;

call dlt_book('sports');