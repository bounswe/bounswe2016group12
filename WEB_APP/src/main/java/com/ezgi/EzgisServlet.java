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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ezgi on 5/6/2016.
 */
public class EzgisServlet extends HttpServlet {


    //URL of our database.
    final String url = "jdbc:mysql://bounswe12.cjhuwoqwajkm.eu-central-1.rds.amazonaws.com:3306/bounswe12";
    final String username = "bounswe12";
    final String password = "123456789";
    final String className = "com.mysql.jdbc.Driver";
    final String queryUrl = "https://query.wikidata.org/bigdata/namespace/wdq/sparql?query=%23Books%20by%20a%20given%20Author%20including%20genres%2C%20series%2C%20and%20publication%20year%0ASELECT%20DISTINCT%20%3Fbook%20%20%3FbookLabel%20%3Fgenre_label%20%3FauthorLabel%20%3FpublicationDate%0AWHERE%0A%7B%0A%09%3Fauthor%20%3Flabel%20%22Douglas%20Adams%22%40en%20.%0A%09%3Fbook%20wdt%3AP31%20wd%3AQ571%20.%0A%09%3Fbook%20wdt%3AP50%20%3Fauthor%20.%0A%0A%09%09%3Fbook%20wdt%3AP136%20%3Fgenre%20.%0A%09%09%3Fgenre%20rdfs%3Alabel%20%3Fgenre_label%20filter%20(lang(%3Fgenre_label)%20%3D%20%22en%22).%0A%09%09%3Fbook%20wdt%3AP577%20%3FpublicationDate%20.%0A%0A%09SERVICE%20wikibase%3Alabel%20%7B%0A%09%09bd%3AserviceParam%20wikibase%3Alanguage%20%22en%22%20.%0A%09%7D%0A%7D";
    //Insert/update remote data.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] items = request.getParameterValues("selectedItem"); //Get selected items.

        if (items != null) { //Save items if checkboxes are checked.
            Connection conn = null;

            try {
                Class.forName(className);
                conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();

                for (int i = 0; i < items.length; i++) {
                    String query = "INSERT INTO EzgiSaved (URL,SaveTime) VALUES('" + items[i] + "', now())";
                    stmt.execute(query);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter writer = response.getWriter()) {

                writer.println("<!DOCTYPE html><html><head>");
                writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
                writer.println("<title>EzgisServlet.java:doGet(): Servlet code!</title>");
                writer.println("</head><body>");
                writer.println("Search added to database! <a href = 'EzgiTekdemir'>Click here to continue search.</a>");
                writer.println("</body></html>");
            }

        } else {
            //Do nothing.
        }
    }

    //Retrieve remote data.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MyXMLParser data = new MyXMLParser(queryUrl);

        try {
            data.getData();
        } catch (SAXException e) { e.printStackTrace(); }
        catch (ParserConfigurationException e) { e.printStackTrace(); }

        //Sets content type to HTML.
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");


        try (PrintWriter writer = response.getWriter()) {
            writer.println("<!DOCTYPE html><html><head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<title>EzgisServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head><body>");
            writer.println("<h1 style=\"text-align: center;\">WELCOME TO EZGI SEARCH!</h1>");
            writer.println("<h3><span style=\"color: #ff0000;\">Enter a search term:</span>&nbsp;</h3>");
            writer.println("<form action='" + request.getRequestURI() + "' method='GET'>");
            writer.println("<input type=\"text\" name=\"query\">");
            writer.println("<button type='submit'>Search</button>");
            writer.println("<p>&nbsp;</p>");
            writer.println("</form>");

            String searchterm = "";
            if (request.getParameter("query") != null) {
                searchterm = request.getParameter("query");

                if (request.getParameter("query") != ""){
                    //TODO: Find a better algorithm (no sorting here).
                    //Hold the results of the query.
                    HashMap <String, ArrayList<String>> results = new HashMap <String, ArrayList<String>>();
                    results.put("book", new ArrayList<String>());
                    results.put("bookLabel", new ArrayList<String>());
                    results.put("authorLabel", new ArrayList<String>());
                    results.put("genre_label", new ArrayList<String>());
                    results.put("publicationDate", new ArrayList<String>());




                    for (String key : data.ezgistable.keySet()) {
                        for (int i = 0; i < data.ezgistable.get(key).size(); i++) {
                            //If searchterm exists in any item, show it on the table.
                            if (data.ezgistable.get(key).get(i).toLowerCase().contains(searchterm.toLowerCase())) {

                                for(String key2: data.ezgistable.keySet())
                                    results.get(key2).add(data.ezgistable.get(key2).get(i));

                            } else
                                continue;
                        }
                    }

                if (results.get("book").size() == 0) //size 0 means no such book exists
                    writer.println("No match found!");
                else {
                    writer.println("<form action=\"EzgiTekdemir\" method=\"POST\"> ");
                    writer.println("<table border=\"1\">");
                    writer.println("<tbody>");
                    writer.println("<tr>");
                    //writer.println("<th>" + "&nbsp; link &nbsp;" + "</th>");
                    for (String key : results.keySet()) {
                        writer.println("<th>" + "&nbsp;" + key + "&nbsp;" + "</th>");

                    }
                    writer.println("<th><input type=\"submit\" value=\"Save\"/></th>");

                    writer.println("</tr>");


                        for (int j = 0; j < results.get("book").size(); j++) {
                            writer.println("<tr>");
                           // writer.println("<td>" + "&nbsp;" + (j + 1) + "&nbsp;" + "</td>");

                            for (String key : results.keySet()) {
                                if (key.equals("book")) continue;
                                    writer.println("<td>" + "&nbsp;" + results.get(key).get(j) + "&nbsp;" + "</td>");
                            }

                            writer.println("<td>" + "&nbsp;" + results.get("book").get(j) + "&nbsp;" + "</td>");
                            writer.println("<td><input type=\"checkbox\" id=\"selectedItem\" name=\"selectedItem\" value=\"" + results.get("book").get(j) + "\"</td>");

                            writer.println("</tr>");
                        }



                    writer.println("</tbody>");
                    writer.println("</table>");

                }
                writer.println("</form>");
                writer.println("</body>");
                writer.println("</html>");
                }
            } else {
                writer.println("<h2>Previous Searches:</h2>");
                writer.println("<table border=\"1\">");
                writer.println("<tr>");
                writer.println("<th>URL</th>");
                writer.println("<th>SaveTime</th>");
                writer.println("</tr>");

                Connection conn = null;
                try {
                    Class.forName(className);
                    conn = DriverManager.getConnection(url, username, password);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM EzgiSaved");
                    while (rs.next()) {
                        String URL = rs.getString("URL");
                        String Time = rs.getString("SaveTime");
                        writer.println("<tr>");
                        writer.println("<td>" + URL + "</td>");
                        writer.println("<td>" + Time + "</td>");
                        writer.println("</tr>");
                    }
                    writer.println("</table>");
                } catch (ClassNotFoundException e) { e.printStackTrace(); }
                catch (SQLException e) { e.printStackTrace(); }
            }

        }

    }

}
