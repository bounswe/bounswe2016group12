package com.ahmet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Ahmet Ercan Tekden on 20.04.2016.
 */
@WebServlet(name = "MyServlet")
public class MyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        ArrayList<String> words= new ArrayList<>();

        Connection conn=null;
        String url = "jdbc:mysql://bounswe12.cjhuwoqwajkm.eu-central-1.rds.amazonaws.com:3306/bounswe12";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection (url,"bounswe12","123456789");
            Statement stmt = conn.createStatement();
            if(request.getParameter("word")!=""&& request.getParameter("word")!=null){
                String sqlEntry="INSERT INTO myTable (myTablecol) VALUES(\""+
                        request.getParameter("word")+"\")";
                stmt.execute(sqlEntry);
            }
            ResultSet rs = stmt.executeQuery("SELECT * FROM myTable");
            while (rs.next()) {
                int id= rs.getInt("idmyTable");
                String Name = rs.getString("myTablecol");
                words.add(Name);
            }
            conn.close();
        } catch (SQLException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (PrintWriter writer = response.getWriter()) {

            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<title>MyServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<h1>This is a simple java servlet of Ahmet Ercan Tekden.</h1>");
            writer.println("Enter A Word<br>");
            writer.println("<form name=\"Entry\" method=\"get\">");
            writer.println("Word: <input type=\"text\" name=\"word\"><br>");
            writer.println("<input type=\"submit\">");
            writer.println("</form>");
            for (int i=0;i<words.size();i++) {
                writer.println("Entry "+(i+1)+":"+ words.get(i)+"<br>");
            }
            writer.println("</body>");
            writer.println("</html>");
        }
    }
}
