package main.java.com.ezgi;

import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by Ezgi on 5/6/2016.
 */
public class EzgisServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MyXMLParser data = new MyXMLParser("https://query.wikidata.org/bigdata/namespace/wdq/sparql?query=%23Books%20by%20a%20given%20Author%20including%20genres%2C%20series%2C%20and%20publication%20year%0ASELECT%20%3FauthorLabel%20%3FbookLabel%20%3Fgenre_label%20%3FpublicationDate%0AWHERE%0A%7B%0A%09%3Fauthor%20%3Flabel%20%22Douglas%20Adams%22%40en%20.%0A%09%3Fbook%20wdt%3AP31%20wd%3AQ571%20.%0A%09%3Fbook%20wdt%3AP50%20%3Fauthor%20.%0A%09OPTIONAL%20%7B%0A%09%09%3Fbook%20wdt%3AP136%20%3Fgenre%20.%0A%09%09%3Fgenre%20rdfs%3Alabel%20%3Fgenre_label%20filter%20(lang(%3Fgenre_label)%20%3D%20%22en%22).%0A%09%7D%0A%09OPTIONAL%20%7B%0A%09%09%3Fbook%20wdt%3AP577%20%3FpublicationDate%20.%0A%09%7D%0A%09SERVICE%20wikibase%3Alabel%20%7B%0A%09%09bd%3AserviceParam%20wikibase%3Alanguage%20%22en%22%20.%0A%09%7D%0A%7D");
        try {
            data.getData();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }


        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        Connection conn=null;
        String url = "jdbc:mysql://bounswe12.cjhuwoqwajkm.eu-central-1.rds.amazonaws.com:3306/bounswe12";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection (url,"User","pw");
            Statement stmt = conn.createStatement();

            String SQL = "";
            for (String key : data.ezgistable.keySet()){
                SQL += "INSERT INTO bounswe12.DouglasAdams (" + key + ") VALUES (";
                for(int i=0; i<data.ezgistable.get(key).size(); i++){
                    SQL += data.ezgistable.get(key).get(i) + ",";
                }
                SQL = SQL.replace(SQL.charAt(SQL.lastIndexOf(',')), ')');
            }

            stmt.executeUpdate(SQL);


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
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<h1 style=\"text-align: center;\">WELCOME TO EZGI SEARCH!</h1>");
            writer.println("<h3><span style=\"color: #ff0000;\">Enter a search term:</span>&nbsp;</h3>");
            writer.println("<input type=\"text\" name=\"query\">");
            writer.println("<form name=\"Entry\" method=\"get\"></form>");
            writer.println("<input type=\"submit\">");
            writer.println("<p>&nbsp;</p>");
            writer.println("<table border=\"1\">");
            writer.println("<tbody>");


            writer.println("<tr>");
            for(String key : data.ezgistable.keySet()){
                writer.println("<th>" + "&nbsp;" + key + "&nbsp;" +"</th>");
            }
            writer.println("</tr>");

            for(String key : data.ezgistable.keySet()) {

                for (int j = 0; j < data.ezgistable.get(key).size(); j++) {
                    writer.println("<tr>");
                    for(String key2 : data.ezgistable.keySet())
                        writer.println("<td>" + "&nbsp;" + data.ezgistable.get(key2).get(j) + "&nbsp;" + "</td>");
                    writer.println("</tr>");
                }

            }

            writer.println("</tbody>");
            writer.println("</table>");
            writer.println("</form>");
            writer.println("</body>");
            writer.println("</html>");
        }





    }


}
