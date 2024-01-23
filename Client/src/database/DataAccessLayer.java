package database;

import java.sql.*;

import client.Client;
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

}

























