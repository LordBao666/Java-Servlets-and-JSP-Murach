package com.lordbao.http;

import com.lordbao.data.ConnectionPool;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/userTableSpreadsheet")
public class UserTableSpreadsheetServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, 
            HttpServletResponse response)
            throws IOException, ServletException {

        // create the workbook, its worksheet, and its title row
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("User table");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("The User table");
        
        // create the header row
        row = sheet.createRow(2);
        row.createCell(0).setCellValue("UserID");
        row.createCell(1).setCellValue("LastName");
        row.createCell(2).setCellValue("FirstName");
        row.createCell(3).setCellValue("Email");
        
        try {
            // read database rows
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection connection = pool.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM User ORDER BY UserID";    
            ResultSet results = statement.executeQuery(query);
            
            // create the spreadsheet rows
            int i = 3;
            while (results.next()) {
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(results.getInt("UserID"));
                row.createCell(1).setCellValue(results.getString("LastName"));
                row.createCell(2).setCellValue(results.getString("FirstName"));
                row.createCell(3).setCellValue(results.getString("Email"));
                i++;
            }
            results.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            this.log(e.toString());
        }

        // set the response headers
        response.setHeader("content-disposition", 
                "attachment; filename=users.xls");
        response.setHeader("cache-control", "no-cache");

        // get the output stream and send the workbook to the browser
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();
    }
}