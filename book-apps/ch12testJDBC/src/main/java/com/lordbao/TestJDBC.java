package com.lordbao;

import com.lordbao.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @Author Lord_Bao
 * @Date 2024/6/3 22:06
 * @Version 1.0
 */
public class TestJDBC {
    private static final String username;
    private static final String password;
    private static final String url;
    private static final Connection connection;

    static {
        Properties p = new Properties();
        InputStream stream = TestJDBC.class.getClassLoader().getResourceAsStream("database.properties");
        try {
            p.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        username = p.getProperty("username");
        password = p.getProperty("password");
        url = p.getProperty("url");
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static void testDQL() {
        String sql = "select * from user";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            //move to next row.
            while (resultSet.next()) {
                //In MySQL, the column name is case-insensitive
                int userID = resultSet.getInt("UserID");
                String email = resultSet.getString("Email");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                User u = new User(userID, email, firstName, lastName);
                System.out.println(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void testInsert() throws SQLException {
        String checkSql = "select * from user where userid=?";
        String insertSql = "insert into user(userid,email,firstName,lastName) values(?,?,?,?)";
        ResultSet resultSet = null;
        try (PreparedStatement pst1 = connection.prepareStatement(checkSql);
             PreparedStatement pst2 = connection.prepareStatement(insertSql);
        ) {
//            User u =  new User(1,"hack@eamil","tom","no");
            User u = new User(4, "hack@eamil", "tom", "no");
            pst1.setObject(1, u.getUserID());
            resultSet = pst1.executeQuery();

            //if the user does not exist,then insert
            if (!resultSet.next()) {
                pst2.setObject(1, u.getUserID());
                pst2.setObject(2, u.getEmail());
                pst2.setObject(3, u.getFirstName());
                pst2.setObject(4, u.getLastName());
                int changeRows = pst2.executeUpdate();
                if (changeRows > 0) {
                    System.out.println("insert succeeds!");
                } else {
                    System.out.println("insert fails!");
                }
            } else {
                System.out.println("The user already exists");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    private static void testDelete() throws SQLException {
        String checkSql = "select * from user where userid=?";
        String deleteSql = "delete from user where userid = ?";
        ResultSet resultSet = null;
        try (PreparedStatement pst1 = connection.prepareStatement(checkSql);
             PreparedStatement pst2 = connection.prepareStatement(deleteSql);
        ) {
            int userId = 4;
            pst1.setObject(1, userId);
            resultSet = pst1.executeQuery();

            //if the user  exists,then delete
            if (resultSet.next()) {
                pst2.setObject(1, userId);
                int affectRows = pst2.executeUpdate();
                if (affectRows > 0) {
                    System.out.println("delete succeeds");
                } else {
                    System.out.println("delete fails");
                }
            } else {
                System.out.println("The user does not exist");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    //Skip this one, doge...
    private  static void testUpdate(){

    }

    public static void main(String[] args) throws SQLException {
//        testDQL();
//          testInsert();
//        testDelete();
    }
}
