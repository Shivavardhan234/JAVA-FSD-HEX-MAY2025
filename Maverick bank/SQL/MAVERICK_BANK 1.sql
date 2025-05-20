CREATE DATABASE maverick_bank;
USE  maverick_bank;


CREATE TABLE Customer(
customer_id INT PRIMARY KEY,
customer_name VARCHAR(255) NOT NULL,
CHECK(customer_name REGEXP '^[A-Za-z ]+$'),
contact_number VARCHAR(45) NOT NULL,
CHECK(contact_number REGEXP '^[0-9 +]+$'),
address VARCHAR(255) NOT NULL,
pancard_number VARCHAR(10) ,
CHECK(pancard_number REGEXP '^[A-Z]{3}[PCAFHT]{1}[A-Z]{1}[0-9]{4}[A-Z]{1}$'),
credit_score INT

);
drop table customer;

CREATE TABLE employee(
employee_id INT PRIMARY KEY,
employee_name VARCHAR(255) NOT NULL,
CHECK(employee_name REGEXP '^[A-Za-z ]+$'),
contact_number VARCHAR(45) NOT NULL,
CHECK(contact_number REGEXP '^[0-9 +]+$'),
address VARCHAR(255) NOT NULL,
branch VARCHAR(10) NOT NULL ,
FOREIGN KEY (branch) REFERENCES branch(branch_code) ON UPDATE CASCADE ON DELETE CASCADE

);
drop table employee;

CREATE TABLE bank_admin(
admin_id INT PRIMARY KEY,
admin_name VARCHAR(255) NOT NULL,
CHECK(admin_name REGEXP '^[A-Za-z ]+$'),
contact_number VARCHAR(45) NOT NULL,
CHECK(contact_number REGEXP '^[0-9 +]+$'),
address VARCHAR(255) NOT NULL
);

CREATE TABLE branch(
branch_code VARCHAR(10) PRIMARY KEY,
branch_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE bank_user(
username VARCHAR(20) PRIMARY KEY,
login_password VARCHAR(255) NOT NULL,
account_type VARCHAR(50)
);



CREATE TABLE bank_account(
account_no VARCHAR(11) PRIMARY KEY,
ifsc_code VARCHAR(10) UNIQUE NOT NULL,
CHECK(ifsc_code REGEXP '^[A-Z]{4}[0]{1}[0-9]{5}$'),
account_name VARCHAR(255) ,
account_type VARCHAR(255) NOT NULL,
balance DECIMAL(20,2) NOT NULL DEFAULT 0,
min_balance DECIMAL(20,2) NOT NULL DEFAULT 0,
intrest_rate DECIMAL(5,2) NOT NULL DEFAULT 0,
branch VARCHAR(10) NOT NULL,
account_status ENUM( 'OPEN', 'CLOSED', 'SUSPENDED') NOT NULL,
transaction_amount_limit DECIMAL(20,2),
transaction_limit INT ,
withdraw_limit INT,
customer_id INT NOT NULL ,
foreign key (customer_id) references customer (customer_id) ON DELETE CASCADE ON UPDATE CASCADE,
customer_id2 INT ,
customer_id3 INT ,
customer_id4 INT ,
foreign key (customer_id2) references customer (customer_id) ON DELETE CASCADE ON UPDATE CASCADE,
foreign key (customer_id3) references customer (customer_id) ON DELETE CASCADE ON UPDATE CASCADE,
foreign key (customer_id4) references customer (customer_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE report(
report_id INT PRIMARY KEY,
account_number VARCHAR(11),
FOREIGN KEY (account_number) REFERENCES bank_account(account_no) ON DELETE CASCADE ON UPDATE CASCADE
);
drop table report;


CREATE TABLE LOAN(
loan_id INT primary key,
account_number VARCHAR(11),
FOREIGN KEY (account_number) REFERENCES bank_account(account_no)ON DELETE CASCADE ON UPDATE CASCADE,
principal_amount Decimal(20,2)  default 0,
intrest_rate Decimal(5,2) default 0,
loan_start_date Date NOT NULL,
loan_end_date Date NOT NULL,
loan_type VARCHAR(255) NOT NULL,
loan_status VARCHAR(50) NOT NULL,
disbursement_date Date NOT NULL,
penality Decimal(20,2) default 0,
due_date Date ,
installment_amount Decimal(20,2) NOT NULL,
repayment_frequency INT NOT NULL
);
drop table loan;