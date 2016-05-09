package com.ahmet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
    /*
        Connection conn=null;
        String url = "jdbc:mysql://bounswe12.cjhuwoqwajkm.eu-central-1.rds.amazonaws.com:3306/bounswe12";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection (url,"User","pw");
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
    */
        try (PrintWriter writer = response.getWriter()) {

            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta charset=\"UTF-8\" />");
            writer.println("<title>MyServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("hello");
            String sparqlEndpoint = "https://query.wikidata.org/sparql?query=";
            String sparqlQuery =""+
            "SELECT ?item ?itemLabel\n"+
            "WHERE\n" +
            "\tSERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }\n" +
            "{\n" +
            "\t?item wdt:P31 wd:Q146 . \n" +
            "}"        ;

            QueryData q=new QueryData("https://query.wikidata.org/bigdata/namespace/wdq/sparql?query=%23Cats%0ASELECT%20%3Fyoutuber%20%3FyoutuberLabel%20%3Fyoutuberpicture%0AWHERE%0A%7B%0A%09%3Fyoutuber%20wdt%3AP106%20wd%3AQ17125263%20.%0A%20%20%09%3Fyoutuber%20wdt%3AP106%20wd%3AQ36180%20.%0A%20%20%09%3Fyoutuber%20wdt%3AP21%20wd%3AQ6581097.%0A%20%20%20%20%20OPTIONAL%7B%0A%20%20%20%20%3Fyoutuber%20wdt%3AP18%20%3Fyoutuberpicture%20.%0A%20%20%20%09%20%7D%0A%09SERVICE%20wikibase%3Alabel%20%7B%20bd%3AserviceParam%20wikibase%3Alanguage%20%22en%22%20%7D%0A%7D");
            writer.println("<form action=\"AhmetTekden\" method=\"GET\"> ");
            writer.println("<table style=\"width:100%\">");
            writer.println("<tr>");
            for(int i=0;i<3;i++){
                writer.println("<th>"+q.Columns.get(i)+"</th>");
            }
            writer.println("<th>Save</th>");
            writer.println("</tr>");

            for(int i=0;i<q.rows.size();i++){
                writer.println("<tr>");
                for(int j=0;j<q.rows.get(i).length;j++){
                    writer.println("<td>"+q.rows.get(i)[j]+"</td>");
                }
                writer.println("<td><input type=\"checkbox\" name=\"item"+i+"\"> </td>");
                writer.println("</tr>");
            }
            writer.println("</table>");
            writer.println("<input type=\"submit\">");
            writer.println("</form>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }
}
