package com.lordbao.testDatabasePool;

import com.lordbao.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Lord_Bao
 * @Date 2024/6/4 14:42
 * @Version 1.0
 */
public class TestDatabasePool {
    private static void testSelect() throws SQLException {
        String sql = "select * from user";
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet resultSet = pst.executeQuery();

        while (resultSet.next()){
            int userID = resultSet.getInt("UserID");
            String email = resultSet.getString("Email");
            String firstName = resultSet.getString("FirstName");
            String lastName = resultSet.getString("LastName");
            User user = new User(userID, email, firstName, lastName);
            System.out.println(user);
        }

        DBUtil.closeResultSet(resultSet);
        DBUtil.closePreparedStatement(pst);
        pool.freeConnection(connection);

    }

    public static void main(String[] args) throws SQLException {
        testSelect();
    }
}
