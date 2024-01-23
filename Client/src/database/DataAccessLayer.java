package database;

import java.sql.*;

import client.Client;
import client.Student;
import oracle.jdbc.driver.OracleDriver;


public class DataAccessLayer {
    private static DataAccessLayer instance;
    private Connection connection;

    // Private constructor to prevent instantiation outside the class
    private DataAccessLayer() {
        // Initialize the connection here
        try {
            DriverManager.registerDriver(new OracleDriver());
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "CS", "123");
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    // Method to get the single instance of the class
    public static synchronized DataAccessLayer getInstance() {
        if (instance == null) {
            System.out.println("Data Base Instance Created");
            instance = new DataAccessLayer();
        }
        return instance;
    }

    // Connect method can now be an instance method
    public Connection getConnection() {
        return connection;
    }

    public String getPassword(String username) throws SQLException {
        ResultSet results;
        PreparedStatement stmt = connection.prepareStatement(
                "Select password from users where username = ?");
        stmt.setString(1, username);
        results = stmt.executeQuery();
        results.next();
        return results.getString(1);
    }

    public void addUser(Client client) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(
                "insert into users values(?, ?, ?, ?)");
        stmt.setString(1, client.getUsername());
        stmt.setString(2, client.getPassword());
        stmt.setString(3, client.getEmail());
        stmt.setString(4, client.getPhone());
        stmt.executeUpdate();
    }

    public ResultSet getStudents() throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(
                "select name, email, id, trunc(months_between(sysdate, dob) / 12) as age, city, street, dep_id, status, calc_gpa(id) from student order by id");
        return stmt.executeQuery();
    }

    public ResultSet getCourseList(int id) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT C.CODE,\n" +
                        "       C.NAME,\n" +
                        "       C.DURATION,\n" +
                        "       TRUNC (C.DURATION / 10)    AS HOURS,\n" +
                        "       E.YEAR,\n" +
                        "       E.SEMESTER,\n" +
                        "       E.GRADE,\n" +
                        "       CASE\n" +
                        "           WHEN E.GRADE > 95 THEN 4\n" +
                        "           WHEN E.GRADE > 90 THEN 3.6\n" +
                        "           WHEN E.GRADE > 80 THEN 3\n" +
                        "           WHEN E.GRADE > 70 THEN 2.4\n" +
                        "           WHEN E.GRADE > 60 THEN 2\n" +
                        "           WHEN E.GRADE IS NULL THEN NULL\n" +
                        "           ELSE 0\n" +
                        "       END                        AS GPA,\n" +
                        "       CASE\n" +
                        "           WHEN E.GRADE > 95 THEN 'A+'\n" +
                        "           WHEN E.GRADE > 90 THEN 'A'\n" +
                        "           WHEN E.GRADE > 80 THEN 'B'\n" +
                        "           WHEN E.GRADE > 70 THEN 'C'\n" +
                        "           WHEN E.GRADE > 60 THEN 'D'\n" +
                        "           WHEN E.GRADE IS NULL THEN '---'\n" +
                        "           ELSE 'F'\n" +
                        "       END                        AS LETTER\n" +
                        "  FROM COURSES C, ENROLLMENTS E\n" +
                        " WHERE C.CODE = E.CODE AND E.STU_ID = ? ORDER BY YEAR DESC, SEMESTER DESC, GRADE DESC");
        stmt.setInt(1, id);
        return stmt.executeQuery();
    }

    public int deleteStudent(int id) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(
                "update student set status = 'Archived' where id = ?");
        stmt.setInt(1, id);
        return stmt.executeUpdate();

    }

    public ResultSet getDepartments() throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT D.NAME,\n" +
                        "       TRUNC (MONTHS_BETWEEN (SYSDATE, START_DATE) / 12)     AS AGE,\n" +
                        "       D.ID,\n" +
                        "       I.NAME                                                AS MANAGER\n" +
                        "  FROM CDEPARTMENTS D, INSTRUCTORS I\n" +
                        " WHERE D.MANAGER_ID = I.ID");
        return stmt.executeQuery();
    }

    public int addStudent(Student student) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(
                "insert into student (name, email, city, street, dep_id, status) \n" +
                        "values (?, ?, ?, ?, ?, ?)");
        stmt.setString(1, student.getName());
        stmt.setString(2, student.getEmail());
        stmt.setString(3, student.getCity());
        stmt.setString(4, student.getStreet());
        stmt.setInt(5, student.getDepartment());
        stmt.setString(6, student.getStatus());
        PreparedStatement stmt2 = connection.prepareStatement(
                "INSERT INTO notifications (description) VALUES ('Data for Student ' || ? || ' Inserted Successfully.')");
        stmt2.setString(1, student.getName());
        return stmt.executeUpdate() + stmt2.executeUpdate();
    }

    public void editStudent(Student student) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(
                "BEGIN\n" +
                        "    UPDATE_STUDENT (?, NAME => ?, EMAIL => ?, CITY => ?, STREET => ?, DEP_ID => ?);\n" +
                        "END;");
        stmt.setInt(1, student.getId());
        stmt.setString(2, student.getName());
        stmt.setString(3, student.getEmail());
        stmt.setString(4, student.getCity());
        stmt.setString(5, student.getStreet());
        stmt.setInt(6, student.getDepartment());
        stmt.execute();
        PreparedStatement stmt2 = connection.prepareStatement(
                "INSERT INTO notifications (description) VALUES ('Data for Student ' || ? || ' Updated Successfully.')");
        stmt2.setInt(1, student.getId());
        stmt2.executeUpdate();
    }

    public ResultSet getNotifications() throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(
                "select description from notifications");
        return stmt.executeQuery();

    }
}
