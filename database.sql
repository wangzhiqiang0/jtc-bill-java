CREATE TABLE package(
   ID INT PRIMARY KEY     NOT NULL,
   TYPE           varchar(20)    NOT NULL,
   SUBSCRIPTION_FEE   INT     NOT NULL,
   ADDRESS        CHAR(50),
   SALARY         REAL
);