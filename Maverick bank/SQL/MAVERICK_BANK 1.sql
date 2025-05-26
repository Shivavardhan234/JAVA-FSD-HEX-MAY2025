CREATE DATABASE maverick_bank;
USE  maverick_bank;
drop database maverick_bank;

CREATE TABLE Customer(
customer_id INT PRIMARY KEY,
customer_name VARCHAR(255) NOT NULL,
CHECK(customer_name REGEXP '^[A-Za-z ]+$'),
contact_number VARCHAR(45) NOT NULL,
CHECK(contact_number REGEXP '^[0-9 +]+$'),
address VARCHAR(255) NOT NULL,
pancard_number VARCHAR(10) ,
CHECK(pancard_number REGEXP '^[A-Z]{3}[PCAFHT]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}$'),
credit_score INT,
adhaar_number VARCHAR(12),
CHECK(adhaar_number REGEXP '^[0-9]{12}$'),
user_id INT UNIQUE NOT NULL,
FOREIGN KEY (user_id) REFERENCES user(user_id)

);
drop table customer;

CREATE TABLE employee(
employee_id INT PRIMARY KEY,
employee_name VARCHAR(255) NOT NULL,
CHECK(employee_name REGEXP '^[A-Za-z ]+$'),
designation VARCHAR(255),
salary Decimal(15,2) default 0,
contact_number VARCHAR(45) NOT NULL,
CHECK(contact_number REGEXP '^[0-9 +]+$'),
address VARCHAR(255) NOT NULL,
ifsc_code VARCHAR(11) NOT NULL ,
FOREIGN KEY (ifsc_code) REFERENCES branch(ifsc_code) ON UPDATE CASCADE ON DELETE CASCADE,
user_id INT UNIQUE NOT NULL,
FOREIGN KEY (user_id) REFERENCES user(user_id)
);
drop table employee;

CREATE TABLE CIO(
admin_id INT PRIMARY KEY,
admin_name VARCHAR(255) NOT NULL,
CHECK(admin_name REGEXP '^[A-Za-z ]+$'),
contact_number VARCHAR(45) NOT NULL,
CHECK(contact_number REGEXP '^[0-9 +]+$'),
address VARCHAR(255) NOT NULL,
user_id INT UNIQUE NOT NULL,
FOREIGN KEY (user_id) REFERENCES user(user_id)
);
drop table CIO;


CREATE TABLE branch(
ifsc_code VARCHAR(11) PRIMARY KEY,
CHECK(ifsc_code REGEXP '^MVRK0[0-9]{6}$'),
branch_name VARCHAR(255) UNIQUE NOT NULL,
state VARCHAR(255) NOT NULL
);
drop table branch;

CREATE TABLE user(
user_id INT PRIMARY KEY auto_increment,
username VARCHAR(50) UNIQUE NOT NULL ,
login_password VARCHAR(255) NOT NULL,
user_type VARCHAR(50)

);

drop table user;

CREATE TABLE bank_account(
account_no VARCHAR(11) PRIMARY KEY,
ifsc_code VARCHAR(11)NOT NULL,
foreign key (ifsc_code) references branch (ifsc_code) ON DELETE CASCADE ON UPDATE CASCADE,
account_name VARCHAR(255) ,
account_type_id INT ,
foreign key (account_type_id) references account_type (account_type_id) ON DELETE CASCADE ON UPDATE CASCADE,
balance DECIMAL(20,2) NOT NULL DEFAULT 0,
branch VARCHAR(10) NOT NULL,
account_status ENUM( 'OPEN', 'CLOSED', 'SUSPENDED') NOT NULL,
customer_id INT NOT NULL ,
foreign key (customer_id) references customer (customer_id) ON DELETE CASCADE ON UPDATE CASCADE,
pin VARCHAR(6),
CHECK(pin REGEXP '^[0-9]{6}$'),
customer_id2 INT ,
customer_id3 INT ,
customer_id4 INT ,
foreign key (customer_id2) references customer (customer_id) ON DELETE CASCADE ON UPDATE CASCADE,
foreign key (customer_id3) references customer (customer_id) ON DELETE CASCADE ON UPDATE CASCADE,
foreign key (customer_id4) references customer (customer_id) ON DELETE CASCADE ON UPDATE CASCADE
);
drop table bank_account;
CREATE TABLE account_type(
account_type_id INT PRIMARY KEY,
account_type VARCHAR(50) NOT NULL,
intrest_rate DECIMAL(5,2) NOT NULL DEFAULT 0,
min_balance DECIMAL(20,2) NOT NULL DEFAULT 0,
transaction_amount_limit DECIMAL(20,2),
transaction_limit INT ,
withdraw_limit INT

);



CREATE TABLE LOAN(
loan_id INT primary key,
account_number VARCHAR(11),
FOREIGN KEY (account_number) REFERENCES bank_account(account_no)ON DELETE CASCADE ON UPDATE CASCADE,
loan_plan_id INT,
FOREIGN KEY (loan_plan_id) REFERENCES loan_plan(loan_plan_id)ON DELETE CASCADE ON UPDATE CASCADE,
loan_start_date Date NOT NULL,
loan_end_date Date NOT NULL,
loan_status VARCHAR(50) NOT NULL,
disbursement_date Date NOT NULL,
total_penality Decimal(20,2) default 0,
due_date Date 
);
drop table loan;



CREATE TABLE loan_payment_history(
transaction_id INT PRIMARY KEY,
loan_id INT,
FOREIGN KEY (loan_id) REFERENCES loan(loan_id)ON DELETE CASCADE ON UPDATE CASCADE,
due_date DATE NOT NULL ,
payment_date DATE NOT NULL,
penality DECIMAL(15,2) default 0
);
drop table loan_payment_history;


CREATE TABLE transactions(
transaction_id INT PRIMARY KEY,
from_details VARCHAR(100) NOT NULL,
to_details VARCHAR(100)NOT NULL,
transaction_type VARCHAR(50)NOT NULL,
transaction_amount DECIMAL(20,2)NOT NULL,
transaction_date_time datetime NOT NULL,
transaction_description VARCHAR(500)  
);

drop table transactions;



CREATE TABLE loan_plan(
loan_plan_id INT PRIMARY KEY,
loan_type VARCHAR(255) NOT NULL,
principal_amount DECIMAL(15,2) NOT NULL,
loan_term INT NOT NULL,
intrest_rate DECIMAL(5,2) NOT NULL,
installment_amount DECIMAL(15,2) NOT NULL,
repayment_frequency INT NOT NULL,
penality_rate DECIMAL(5,2) NOT NULL,
prepayment_penality DECIMAL(15,2) NOT NULL default 0,
grace_period INT NOT NULL default 0
);

CREATE TABLE loan_opening_application(
loan_opening_application_id INT PRIMARY KEY,
account_number VARCHAR(11),
FOREIGN KEY (account_number) REFERENCES bank_account(account_no)ON DELETE CASCADE ON UPDATE CASCADE,
loan_plan_id INT,
FOREIGN KEY (loan_plan_id) REFERENCES loan_plan(loan_plan_id)ON DELETE CASCADE ON UPDATE CASCADE,
purpose VARCHAR(1000) ,
application_status  enum('PENDING', 'APPROVED', 'REJECTED')
);
drop table loan_opening_application;



CREATE TABLE loan_closure_application(
loan_closure_application_id INT PRIMARY KEY,
loan_id INT,
FOREIGN KEY (loan_id) REFERENCES loan(loan_id)ON DELETE CASCADE ON UPDATE CASCADE,
purpose VARCHAR(1000) ,
application_status  enum('PENDING', 'APPROVED', 'REJECTED')
);
drop table loan_closure_application;

CREATE TABLE account_opening_application(
account_opening_application_id INT PRIMARY KEY,
customer_id INT NOT NULL ,
foreign key (customer_id) references customer (customer_id) ON DELETE CASCADE ON UPDATE CASCADE,
account_type_id INT ,
foreign key (account_type_id) references account_type (account_type_id) ON DELETE CASCADE ON UPDATE CASCADE,
reason VARCHAR(500),
application_status  enum('PENDING', 'APPROVED', 'REJECTED')
);
drop table account_opening_application;

CREATE TABLE account_requests(
account_requests_id INT PRIMARY KEY,
account_number VARCHAR(11),
FOREIGN KEY (account_number) REFERENCES bank_account(account_no)ON DELETE CASCADE ON UPDATE CASCADE,
purpose enum('CLOSE', 'SUSPEND','REOPEN'),
request_status  enum('PENDING', 'APPROVED', 'REJECTED')
);
drop table account_requests;
