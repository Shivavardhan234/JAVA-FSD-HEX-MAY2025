Create database HEX_FSD;

USE HEX_FSD;

Drop table employee;
Create table employee(
eid int primary key auto_increment,
ename Varchar(250) not null,
ebranch varchar(250) not null,
edepartment varchar(250) not null,
esalary decimal(15,2) not null default 0.0);

INSERT INTO EMPLOYEE (eid, ename, ebranch, edepartment, esalary) VALUES
(1, 'Amit Sharma', 'Mumbai', 'HR', 75000.00),
(2, 'Priya Patel', 'Delhi', 'IT', 82000.50),
(3, 'Rajesh Kumar', 'Bangalore', 'Finance', 90000.75),
(4, 'Neha Verma', 'Mumbai', 'HR', 78000.25),
(5, 'Vikram Singh', 'Delhi', 'IT', 85000.00),
(6, 'Anjali Gupta', 'Bangalore', 'Finance', 92000.40),
(7, 'Manish Tiwari', 'Mumbai', 'HR', 76000.90),
(8, 'Kavita Reddy', 'Chennai', 'IT', 89000.30),
(9, 'Arjun Nair', 'Bangalore', 'Finance', 94000.20),
(10, 'Sneha Iyer', 'Chennai', 'HR', 77000.10),
(11, 'Suresh Pillai', 'Mumbai', 'IT', 81000.60),
(12, 'Divya Menon', 'Delhi', 'Finance', 93000.80),
(13, 'Ravi Shankar', 'Chennai', 'HR', 74000.55),
(14, 'Pooja Deshmukh', 'Bangalore', 'IT', 86000.95),
(15, 'Karan Mehta', 'Mumbai', 'Finance', 97000.70),
(16, 'Meera Joshi', 'Delhi', 'HR', 73000.15),
(17, 'Akash Bansal', 'Chennai', 'IT', 88000.45),
(18, 'Rohan Agarwal', 'Delhi', 'Finance', 95000.60),
(19, 'Swati Saxena', 'Bangalore', 'HR', 72000.85),
(20, 'Vivek Choudhary', 'Chennai', 'Finance', 96000.90);

delimiter c;
Create procedure max_sal()
BEGIN 
SELECT * From employee where ( esalary =(Select max(esalary) from employee));
END;
drop procedure max_sal;

/*get emp det by id*/
delimiter cake;
create procedure get_emp_det(in id int)
BEGIN 
SELECT * from employee where eid=id;
END;

call get_emp_det(1);


-- output procedures

delimiter $$
create procedure no_of_emp_in_dept(IN dept varchar(250),OUT number_of_emp int)
BEGIN
if dept = null then
	SET number_of_emp=-1;
else
	SET number_of_emp=(SELECT COUNT(eid) from employee where edepartment= dept);
end if;
END;
drop procedure no_of_emp_in_dept;
SET @no_of_emp=0;
call no_of_emp_in_dept('',@no_of_emp);

SELECT @no_of_emp;

/*-------------------------------------------------------you are an electrition (^o^)----------------------------------------------------------------------------------------*/
Delimiter %
CREATE PROCEDURE electricity_charge(IN units INT , OUT rate DECIMAL(10,2))
BEGIN
	IF units<10 THEN
		SET rate= 25*8;
	ELSEIF units<=200 THEN
		SET rate=units*8;
	ELSE 
		SET rate=units*10;
	END IF;
END%
drop procedure electricity_charge;
SET @Final_bill=0;
CALL electricity_charge(1200,@Final_bill);
SELECT @Final_bill as "Final Bill";

/*-----------------------------------------------------------aipoindi guyzzzz <(`-`)>----------------------------------------------------------------------------------------*/
Delimiter $$
create procedure inc_sal_dept_vise(IN dept varchar(250), In percent_inc decimal)
BEgIN
	UPDATE employee
    SET esalary= esalary + (esalary*percent_inc/100) 
    WHERE edepartment=dept;
End $$

call inc_sal_dept_vise('IT',2);

-- ---------------------------------------------------------------<<>>  loops  <<>>------------------------------------------------------------------------------------------------
Delimiter $$
Create procedure Simple_loop(In final int)
BEGIN
Declare i INT default 1;
	simple_lp:
    Loop
		SELECT i;
		SET  i=i+1;
        
        if i>=final then
        Leave simple_lp;
        END if;
	end loop simple_lp;
    
END $$

CALL Simple_loop(5);



Delimiter $$
CREATE PROCEDURE while_loop (in final INT)
BEGIN 
	Declare i INT default 1;
    WHILE i<final DO
		select i;
		set i=i+1;
	END WHILE;
end $$

call while_loop (6);

/*-------------------------------------------------------getting all ids---------------------------------------------------------------------------*/
Delimiter $$
Create procedure get_ids()
BEGIN 
DECLARE i INT default 1;
DECLARE  total INT default 0;
Declare c_id INT default 0;
DECLARE result varchar(250) default '';
SELECT count(eid) into total from employee;
WHILE i<=total DO
	SELECT eid into c_id from employee where eid=i;
	SET result =concat(result , "  " ,c_id);
    set i= i+1;
END While;
SELECT result;
END$$
drop procedure get_ids;
        
call get_ids();


/*---------------------------------------------------------------------------------------------------------------------------------------------------*/
Delimiter $$
Create procedure update_sal(IN dept varchar(250), In per_inc INT)
BEGIN
DECLARE tot_rows INT default 0;
Declare i INT default 0;
Declare c_id INT default 0;
SELECT count(eid) into tot_rows from employee where edepartment = dept;


While i<tot_rows DO

SELECT eid into c_id From employee WHERE edepartment=dept LIMIT i,1;

Update employee SET esalary= (esalary + (esalary * (per_inc/100))) WHERE eid =c_id;


SET i=i+1;
END WHILE;

END $$


drop procedure update_sal;

call update_sal('IT',5);


/*---------------------------------------------------------------give and return the same element---------------------------------------------------------------------------*/
Delimiter $$
create procedure same_same (INOUT emo INT )
BEGIN
SET emo= emo;
SELECt emo as EDO_okati;
END $$

SET @x=5;
call same_same(@x);

-- --------------------------------------------------------------emp log-------------------------------------------------------------------------------------------------------
CREATE TABLE emp_log (
id int primary key auto_increment,
old_sal decimal(15,2) ,
new_sal decimal(15,2) ,
log_td datetime not null,
log_user Varchar(255) not null,
eid int not null
);

drop table emp_log;
-- --------------------------------------------------------creating a trigger--------------------------------------------------------------------------------------------------
DELIMITER $$
CREATE TRIGGER emp_logg 
BEFORE UPDATE ON employee
FOR EACH ROW
BEGIN
INSERT INTO emp_log(old_sal, new_sal, log_td, log_user , eid) VALUES(OLD.esalary, NEW.esalary , now(), user(), OLD.eid);
END $$


UPDATE employee set esalary=82000 where eid=2;

/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------______________________-----------------------------------------------------------------------------------------
-----------------------------------------------------------------/                       \----------------------------------------------------------------------------------------
----------------------------------------------------------------/   ( o )         ( o )   \---------------------------------------------------------------------------------------
---------------------------------------------------------------/                           \--------------------------------------------------------------------------------------
---------------------------------------------------------------|                           |--------------------------------------------------------------------------------------
---------------------------------------------------------------|             o             |--------------------------------------------------------------------------------------
---------------------------------------------------------------|                           |--------------------------------------------------------------------------------------
----------------------------------------------------------------\      _____________      /---------------------------------------------------------------------------------------
------------------------------------------------------------------\                     /-----------------------------------------------------------------------------------------
--------------------------------------------------------------------\_________________/-------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/


DELIMITER $$
CREATE TRIGGER trg_ins_emp_log 
BEFORE INSERT ON EMployee 
FOR EACH ROW
BEGIN
if NEW.esalary <= 5000000 THEN
	INSERT INTO emp_log(new_sal, log_td,log_user, eid) values( NEW.esalary, now(), user(), NEW.eid);
ELSE 
	SIGNAL sqlstate '45000'
    SET message_text="ERROR: salary should not be more than 5000000, insert again";
end if;
END $$

-- --------------------------------------------views------------------------------------------------------------------------------------
DELIMITER $$
CREATE VIEW view_emp_branch_stats
AS
SELECT ebranch as branch , count(eid) as employee_count from employee 
group by ebranch;

drop VIEW view_emp_branch_stats;
/*                                        -------------ANALYSIS SIMULATION----------------
ebranch='Mumbai'
+----------------------------------------------+
| 1  | 'Amit Sharma'    | 'Mumbai' | 'HR'      |
| 4  | 'Neha Verma'     | 'Mumbai' | 'HR'      | 
| 7  | 'Manish Tiwari'  | 'Mumbai' | 'HR'      | 
| 11 | 'Suresh Pillai'  | 'Mumbai' | 'IT'      | 
| 15 | 'Karan Mehta'    | 'Mumbai' | 'Finance' |
+----------------------------------------------+ 

ebranch='DELHI'
+----------------------------------------------+
| 2  | 'Priya Patel'   | 'Delhi'  | 'IT'       | 
| 5  | 'Vikram Singh'  | 'Delhi'  | 'IT'       | 
| 12 | 'Divya Menon'   | 'Delhi'  | 'Finance'  | 
| 16 | 'Meera Joshi'   | 'Delhi'  | 'HR'       |
| 18 | 'Rohan Agarwal' | 'Delhi'  | 'Finance'  | 
+----------------------------------------------+

ebranch='Banglore'
+-----------------------------------------------------+
| 3  | 'Rajesh Kumar'    | 'Bangalore'  | 'Finance'   |
| 6  | 'Anjali Gupta'    | 'Bangalore'  | 'Finance'   | 
| 9  | 'Arjun Nair'      | 'Bangalore'  | 'Finance'   | 
| 14 | 'Pooja Deshmukh'  | 'Bangalore'  | 'IT'        | 
| 19 | 'Swati Saxena'    | 'Bangalore'  | 'HR'        | 
+-----------------------------------------------------+

ebranch='Chennai'
+-----------------------------------------------------+
| 8  | 'Kavita Reddy'      | 'Chennai'  | 'IT'        |
| 10 | 'Sneha Iyer'        | 'Chennai'  | 'HR'        |
| 13 | 'Ravi Shankar'      | 'Chennai'  | 'HR'        | 
| 17 | 'Akash Bansal'      | 'Chennai'  | 'IT'        | 
| 20 | 'Vivek Choudhary'   | 'Chennai'  | 'Finance'   | 
| 21 | 'Shiva vardhan'     | 'Chennai'  | 'IT'        |
+-----------------------------------------------------+
_______________________________________________________________________________________________________________________________________*/
DELIMITER $$
CREATE FUNCTION highest_sal()
returns decimal
DETERMINISTIC
BEGIN
DECLARE max_sal decimal(15,2) default 0;
SELECT  max(esalary) into max_sal from employee;
return max_sal;
END $$
Set @max_sal=0;
SELECT highest_sal() ;