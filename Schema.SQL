CREATE TABLE Courses
(
  Code  NUMBER(8) ,
  Name VARCHAR2(100) NOT NULL,
  Duration  NUMBER(8) ,
  PRIMARY KEY (Code)
);

CREATE TABLE CDepartments
(
  ID  NUMBER(8) ,
  Name VARCHAR2(100) NOT NULL,
  Manager_ID NUMBER(8),
  start_date date,
  PRIMARY KEY (ID)
);

CREATE TABLE Instructors
(
  Name VARCHAR2(100) NOT NULL,
  ID  NUMBER(8) ,
  Phone VARCHAR2(100) ,
  Email VARCHAR2(100),
  DOB DATE ,
  Hire_Date DATE ,
  DEP_ID  NUMBER(8) NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (DEP_ID) REFERENCES CDepartments(ID)
);

ALTER TABLE CDepartments
ADD CONSTRAINT fk_manager_department
FOREIGN KEY (Manager_ID)
REFERENCES INSTRUCTORS(ID);

CREATE TABLE Ins_Courses
(
  Code  NUMBER(8) ,
  INS_ID  NUMBER(8) ,
  year DATE,
  PRIMARY KEY (Code, INS_ID, year),
  FOREIGN KEY (Code) REFERENCES Courses(Code),
  FOREIGN KEY (INS_ID) REFERENCES Instructors(ID)
);

CREATE TABLE Dep_Courses
(
  Code  NUMBER(8) ,
  DEP_ID  NUMBER(8) ,
  PRIMARY KEY (Code, DEP_ID),
  FOREIGN KEY (Code) REFERENCES Courses(Code),
  FOREIGN KEY (DEP_ID) REFERENCES CDepartments(ID)
);

CREATE TABLE Prerequisites
(
  Code  NUMBER(8) ,
  PrerequisiteCode  NUMBER(8) ,
  PRIMARY KEY (Code, PrerequisiteCode),
  FOREIGN KEY (Code) REFERENCES Courses(Code),
  FOREIGN KEY (PrerequisiteCode) REFERENCES Courses(Code)
);

CREATE TABLE Student
(
  ID  NUMBER(8) ,
  Name VARCHAR2(100) ,
  DOB date ,
  email VARCHAR2(200),
  City VARCHAR2(200) ,
  Street VARCHAR2(200) ,
  DEP_ID  NUMBER(8) NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (DEP_ID) REFERENCES CDepartments(ID)
);

CREATE TABLE Enrollments
(
  grade VARCHAR2(10) ,
  STU_ID  NUMBER(8) ,
  Code  NUMBER(8) ,
  year Date,
  PRIMARY KEY (STU_ID, Code, year),
  FOREIGN KEY (STU_ID) REFERENCES Student(ID),
  FOREIGN KEY (Code) REFERENCES Courses(Code)
);


CREATE TABLE Student_mobile
(
  mobile VARCHAR2(100) ,
  ID  NUMBER(8) ,
  PRIMARY KEY (mobile, ID),
  FOREIGN KEY (ID) REFERENCES Student(ID)
);