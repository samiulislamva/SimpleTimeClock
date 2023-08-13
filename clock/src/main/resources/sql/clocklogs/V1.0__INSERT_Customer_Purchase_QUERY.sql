create table Employee(
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar NOT NULL ,
   PRIMARY KEY (`ID`));

insert into Employee (name) values
('Steve'),
('Erik');


create table Employee_Time_Clock_Log(
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_Id` int NOT NULL ,
  `clock_Type` varchar NOT NULL,
  `shift_Start_Time` datetime NOT NULL,
  `shift_End_Time` datetime DEFAULT NULL,
  `break_Start_Time` datetime DEFAULT NULL,
  `break_End_Time` datetime DEFAULT NULL,
  `lunch_Start_Time` datetime DEFAULT NULL,
  `lunch_End_Time` datetime DEFAULT NULL,
   PRIMARY KEY (`ID`),
   FOREIGN KEY (employee_Id) REFERENCES Employee(id));
