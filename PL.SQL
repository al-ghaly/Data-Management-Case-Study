CREATE OR REPLACE PROCEDURE UPDATE_STUDENT (V_ID     NUMBER,
                                            NAME     VARCHAR2:= NULL,
                                            DOB      DATE:= NULL,
                                            EMAIL    VARCHAR2:= NULL,
                                            CITY     VARCHAR2:= NULL,
                                            STREET   VARCHAR2:= NULL,
                                            DEP_ID   NUMBER:= NULL)
IS
    -- Exceptions
    INSERT_DEPT_EX   EXCEPTION;
    PRAGMA EXCEPTION_INIT (INSERT_DEPT_EX, -02291);
    -- Initialize variables to holf the student's data
    V_NAME           STUDENT.NAME%TYPE;
    V_DOB            STUDENT.DOB%TYPE;
    V_EMAIL          STUDENT.EMAIL%TYPE;
    V_CITY           STUDENT.CITY%TYPE;
    V_STREET         STUDENT.STREET%TYPE;
    V_DEP_ID         STUDENT.DEP_ID%TYPE;
    V_STUDENT        STUDENT%ROWTYPE;
BEGIN
    -- Get the before-update data for the student
    SELECT *
      INTO V_STUDENT
      FROM STUDENT
     WHERE ID = V_ID;

    -- Update any given data from the user
    IF NAME IS NULL
    THEN
        V_NAME := V_STUDENT.NAME;
    ELSE
        V_NAME := NAME;
    END IF;

    IF DOB IS NULL
    THEN
        V_DOB := V_STUDENT.DOB;
    ELSE
        V_DOB := DOB;
    END IF;

    IF EMAIL IS NULL
    THEN
        V_EMAIL := V_STUDENT.EMAIL;
    ELSE
        V_EMAIL := EMAIL;
    END IF;

    IF CITY IS NULL
    THEN
        V_CITY := V_STUDENT.CITY;
    ELSE
        V_CITY := CITY;
    END IF;

    IF STREET IS NULL
    THEN
        V_STREET := V_STUDENT.STREET;
    ELSE
        V_STREET := STREET;
    END IF;

    IF DEP_ID IS NULL
    THEN
        V_DEP_ID := V_STUDENT.DEP_ID;
    ELSE
        V_DEP_ID := DEP_ID;
    END IF;

    -- Update the student's data
    UPDATE STUDENT
       SET NAME = V_NAME,
           DOB = V_DOB,
           EMAIL = V_EMAIL,
           CITY = V_CITY,
           STREET = V_STREET,
           DEP_ID = V_DEP_ID
     WHERE ID = V_ID;
EXCEPTION
    WHEN INSERT_DEPT_EX
    THEN
        DBMS_OUTPUT.put_line ('Invalid Department ID!');
END;

SHOW ERRORS;

CREATE OR REPLACE FUNCTION CALC_GPA (ST_ID NUMBER)
    RETURN NUMBER
IS
    GPA   NUMBER (3, 2);
BEGIN
    WITH
        GRADES
        AS
            (SELECT CASE
                        WHEN E.GRADE > 95 THEN 4
                        WHEN E.GRADE > 90 THEN 3.6
                        WHEN E.GRADE > 80 THEN 3
                        WHEN E.GRADE > 70 THEN 2.4
                        WHEN E.GRADE > 60 THEN 2
                        WHEN E.GRADE IS NULL THEN NULL
                        ELSE 0
                    END                        C_GPA,
                    TRUNC (C.DURATION / 10)    C_HOURS
               FROM ENROLLMENTS E, COURSES C
              WHERE     C.CODE = E.CODE
                    AND E.STU_ID = ST_ID
                    AND E.GRADE IS NOT NULL)
    SELECT ROUND (SUM (C_GPA * C_HOURS) / SUM (C_HOURS), 2)
      INTO GPA
      FROM GRADES;

    RETURN GPA;
END;

SHOW ERRORS;

CREATE OR REPLACE TRIGGER VALIDATE_ENROLLMENT
    BEFORE INSERT
    ON ENROLLMENTS
    FOR EACH ROW
DECLARE
    ATTEMPTS   NUMBER (2);
BEGIN
    SELECT COUNT (*)
      INTO ATTEMPTS
      FROM ENROLLMENTS
     WHERE STU_ID = :NEW.STU_ID AND CODE = :NEW.CODE;

    IF ATTEMPTS > 1
    THEN
        RAISE_APPLICATION_ERROR (
            -20001,
               'Student: '
            || :NEW.STU_ID
            || ' Have already attempted this course twice before!!');
    END IF;
END;

SHOW ERRORS;

BEGIN
    UPDATE_STUDENT (0);
    UPDATE_STUDENT (1, NAME => 'Aly');
    UPDATE_STUDENT (2, NAME => 'Aya', EMAIL => 'Aya@gmail.com');
    UPDATE_STUDENT (3,
                    NAME    => 'Hoda',
                    EMAIL   => 'Hoda@gmail.com',
                    DOB     => SYSDATE - INTERVAL '20' YEAR);
    UPDATE_STUDENT (4,
                    NAME    => 'Omar',
                    EMAIL   => 'Omar@gmail.com',
                    CITY    => 'Alex');
    UPDATE_STUDENT (5,
                    NAME     => 'Emma',
                    EMAIL    => 'Emma@gmail.com',
                    CITY     => 'LA',
                    STREET   => 'CALIFORNIA STREET');
    UPDATE_STUDENT (6,
                    NAME     => 'Emily',
                    EMAIL    => 'Emily@gmail.com',
                    CITY     => 'LF',
                    STREET   => 'CALIFORNIA STREET',
                    DEP_ID   => 10);
    UPDATE_STUDENT (7,
                    NAME     => 'Rach',
                    EMAIL    => 'Rachel@gmail.com',
                    CITY     => 'NYC',
                    STREET   => 'CALIFORNIA STREET',
                    DEP_ID   => 25);
END;

SELECT *
  FROM STUDENT
 WHERE ID BETWEEN 0 AND 7;

 SELECT ID, CALC_GPA (ID) FROM STUDENT;
----------------------------------------------------------------------------


