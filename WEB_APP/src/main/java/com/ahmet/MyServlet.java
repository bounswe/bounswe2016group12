package main.java.com.ahmet;

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
 * Created by Ahmet Ercan Tekden on 20.04.2016.
 */
@WebServlet(name = "MyServlet")
public class MyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String[] values;
        values = request.getParameterValues("saveData");
        // If no checkbox selected.
        if(values==null){

        }
        //Adds all checkboxes to database.
        else {


            Connection conn = null;
            String url = "jdbc:mysql://bounswe12.cjhuwoqwajkm.eu-central-1.rds.amazonaws.com:3306/bounswe12";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, "bounswe12", "123456789");
                Statement stmt = conn.createStatement();
                for (int i = 0; i < values.length; i++) {
                    String query = "INSERT INTO SaveDataAhmet(SavedURL,SaveTime) VALUES('" + values[i] + "',now()) ";
                    stmt.execute(query);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {

            writer.println("<!DOCTYPE html><html>");
            writer.println("<head>");
            writer.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
            writer.println("<title>MyServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<a href = 'AhmetTekden'>Click Here to Previous Page</a>");
            writer.println("</body>");
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
            writer.println("<title>MyServlet.java:doGet(): Servlet code!</title>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("Search within data.<br> Example Searches: male , Hikmet , ...  <br> ");
            writer.println("<form action='"+request.getRequestURI()+"' method='GET'>");
            String query="";
            //For showing last entered query
            if(request.getParameter("query")!=null){
                query=request.getParameter("query");
            }
            writer.println("Search Term: <input type=\"text\" name=\"query\" value='"+query+"' >");
            writer.println("<button type='submit'>Submit</button><br>");
            writer.println("</form>");
            //If query is not empty shows searched list of data.
            if(request.getParameter("query")==""|| request.getParameter("query")==null){

                writer.println("<h1 align=\"center\">Previous Saved Searches</h1>");
                writer.println("<table style=\"width:100%\">");
                writer.println("<tr>");
                writer.println("<th>PrevUrl</th>");
                writer.println("<th>Time</th>");
                writer.println("</tr>");
                Connection conn = null;
                String url = "jdbc:mysql://bounswe12.cjhuwoqwajkm.eu-central-1.rds.amazonaws.com:3306/bounswe12";
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection(url, "bounswe12", "123456789");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM SaveDataAhmet");
                    while (rs.next()) {
                        String URL = rs.getString("SavedURL");
                        String Time = rs.getString("SaveTime");
                        writer.println("<tr>");
                        writer.println("<td align=\"center\">"+URL+"</td>");
                        writer.println("<td align=\"center\">"+Time+"</td>");
                        writer.println("</tr>");
                    }
                    writer.println("</table>");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                /*
            sparqlEndpoint = https://query.wikidata.org/wdq/sparql?query
            sparqlQuery ="
            SELECT ?Writer ?WriterLabel ?BirthLabel ?GenderLabel
            WHERE
            {
                ?Writer wdt:P106 wd:Q36180.
                ?Writer wdt:P27 wd:Q43.
                ?Writer wdt:P19 ?Birth.
                ?Writer wdt:P21 ?Gender.
                ?Writer wdt:P569 ?year.
                SERVICE wikibase:label { bd:serviceParam wikibase:language "en" }
                FILTER (YEAR(?year) >= 1960)
            }
            */
                QueryData q=new QueryData("https://query.wikidata.org/bigdata/namespace/wdq/sparql?query=SELECT%20%3FWriter%20%3FWriterLabel%20%3FBirthLabel%20%3FGenderLabel%0AWHERE%0A%7B%0A%09%3FWriter%20wdt%3AP106%20wd%3AQ36180.%0A%20%09%3FWriter%20wdt%3AP27%20wd%3AQ43.%0A%09%3FWriter%20wdt%3AP19%20%3FBirth.%0A%20%20%20%20%3FWriter%20wdt%3AP21%20%3FGender.%0A%20%20%09%3FWriter%20wdt%3AP569%20%3Fyear.%0A%09SERVICE%20wikibase%3Alabel%20%7B%20bd%3AserviceParam%20wikibase%3Alanguage%20%22en%22%20%7D%0A%09FILTER%20(YEAR(%3Fyear)%20%3E%3D%201960)%0A%7D%20%0AORDER%20BY%20%3FobjectLabel%20%3Fyear");
                int zeroIndex = q.calculatePointsAhmet(request.getParameter("query"));
                if(zeroIndex==0){
                    writer.println("Nothing has been found related to that query");
                }else{


                    writer.println("<form action=\"AhmetTekden\" method=\"POST\"> ");
                    writer.println("<table style=\"width:100%\">");
                    writer.println("<tr>");
                    for(int i=0;i<q.Columns.size();i++){
                        writer.println("<th>"+q.Columns.get(i)+"</th>");
                    }
                    writer.println("<th>Save</th>");
                    writer.println("</tr>");

                    for(int i=0;i<zeroIndex;i++){
                        writer.println("<tr>");
                        for(int j=0;j<q.rows.get(i).length;j++){
                            writer.println("<td>"+q.rows.get(i)[j]+"</td>");
                        }
                        String URL="";
                        String pattern="<ur.*ri>";
                        Matcher m= Pattern.compile(pattern).matcher(q.rows.get(i)[0]);
                        while (m.find())
                            URL=m.group().substring(5,m.group().length()-6);
                        writer.println("<td><input type=\"checkbox\" name='saveData' value='"+URL+"'> </td>");
                        writer.println("</tr>");
                    }
                    writer.println("</table>");
                    writer.println("<input type=\"submit\">");
                }
            }



            writer.println("</form>");
            writer.println("</body>");
            writer.println("</html>");
        }
    }
}
