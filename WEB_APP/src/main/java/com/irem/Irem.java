package main.java.com.irem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Irem Zog on 10.05.2016.
 */
@WebServlet("/IremZog")
public class Irem extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {

            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
            writer.println("<title>IremsServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
           /* writer.println("<body>");
            writer.println("<a href = 'IremZog'>Click Here to Previous Page</a>");
            writer.println("</body>");*/
            writer.println("</html>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {

            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
            writer.println("<title>IremsServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<form action='"+request.getRequestURI()+"' method='GET'>");
            String query="";
            if(request.getParameter("query")!=null){
                query=request.getParameter("query");
            }
            writer.println("Search Term: <input type=\"text\" name=\"query\" value='"+query+"' >");
            writer.println("<button type='submit'>Submit</button><br>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }
}
