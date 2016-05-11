package com.utkan;

import com.sun.javafx.sg.prism.web.NGWebView;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import com.sun.org.apache.xerces.internal.parsers.CachingParserPool;
import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;
import org.apache.jena.reasoner.rulesys.builtins.Table;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by utkan on May 9.
 */
public class UtkanServlet extends HttpServlet {

    /**
     * Supposed to do the database insertions. Fails to do so.
     *
     * @param request request made via Post
     * @param response response from our servlet
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] databaseActions = request.getParameterValues("databaseAction");
        if (databaseActions != null) {
            Connection conn = null;
            String databaseURL = "jdbc:mysql://bounswe12.cjhuwoqwajkm.eu-central-1.rds.amazonaws.com:3306/bounswe12";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(databaseURL, "bounswe12", "123456789");
                Statement stmt = conn.createStatement();
                for (int i = 0; i < databaseActions.length; i++) {
                    String query = "INSERT INTO UtkanData2 (uri, tim) VALUES('" + databaseActions[i] + "', now())";
                    stmt.execute(query);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Class for describing a date.
     */
    class Date {
        int year;
        int month;
        int day;

        Date(String datestring) {
            datestring = datestring.split("T")[0];
            String[] ymd = datestring.split("-");
            year = Integer.parseInt(ymd[0]);
            month = Integer.parseInt(ymd[1]);
            day = Integer.parseInt(ymd[2]);
        }

        int distance(Date other) {
            int dayDiff = (year - other.year) * 365 + (month - other.month) * 30 + (day - other.day);
            return Math.abs(dayDiff);
        }

        public String toString() {
            return day + " / " + month + " / " + year;
        }
    }

    /**
     * Class for describing a coordinate.
     */
    class Coord {
        double lat;
        double lon;

        Coord(String coordstring) {
            coordstring = coordstring.split("\\(")[1];
            coordstring = coordstring.split("\\)")[0];
            String[] coordstrings = coordstring.split(" ");
            lat = Double.parseDouble(coordstrings[0]);
            lon = Double.parseDouble(coordstrings[1]);
        }

        double distance(Coord other) {
            return Math.abs(lat - other.lat) + Math.abs(lon - other.lon);
        }

        public String toString() {
            DecimalFormat two = new DecimalFormat("#0.00");
            return "(" + two.format(lat) + ", " + two.format(lon) + ")";
        }
    }

    /**
     * Class for describing a database record.
     */
    class Record {
        String uri;
        String name;
        Coord coord;
        Date date;

        Record(Node uriNode, Node nameNode, Node coordNode, Node dateNode) {
            uri = uriNode.getTextContent();
            name = nameNode.getTextContent();
            coord = new Coord(coordNode.getTextContent());
            date = new Date(dateNode.getTextContent());
        }

        double distance(Record other) {
            return coord.distance(other.coord) * 1.0 + date.distance(other.date) * 0.01;
        }

        public String toString() {
            return "<td>" + name + "</td>" +"<td>" + coord + "</td>" +"<td>" + date + "</td>";
        }
    }

    /**
     * List of records from our query.
     */
    ArrayList<Record> records;

    /**
     * Main method of this servlet.
     *
     * Displays the content to the user depending on the Get parameter present in the URL. It either displays the
     * previously saved records, or the current search results.
     *
     * @param request post request that was made
     * @param response response from our servlet
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<H1>Hello from Utkan's Servlet</H1>");
        out.println("<h5>Linked-data search for Astronomer Physicists who were born between years 1800 and 1950. The results are sorted according to relevancy. Relevancy is calculated according to how close they are born in the space-time!</h5>");

        if (records == null) {
            String restEndpoint = "https://query.wikidata.org/bigdata/namespace/wdq/sparql?query=SELECT%20DISTINCT%20%3Fperson%20(SAMPLE(%3Fpersonlab)%20AS%20%3Fpersonlabsampl)%20(SAMPLE(%3Fbirthcoord)%20AS%20%3Fbirthcoordsampl)%20(SAMPLE(%3Fbirthdate)%20AS%20%3Fbirthdatesampl)%0AWHERE%0A%7B%0A%20%20%09%3Fperson%20wdt%3AP106%20wd%3AQ169470%20.%0A%20%20%09%3Fperson%20wdt%3AP106%20wd%3AQ11063%20.%0A%20%20%09SERVICE%20wikibase%3Alabel%0A%20%20%20%20%7B%0A%20%20%20%20%20%20bd%3AserviceParam%20wikibase%3Alanguage%20%22en%2Cde%2Ces%2Cit%2Cru%22%20.%0A%20%20%20%20%20%20%3Fperson%20rdfs%3Alabel%20%3Fpersonlab%0A%20%20%20%20%7D%0A%20%20%20%20%0A%20%20%09%3Fperson%20wdt%3AP19%20%3Fbirthplace%20.%0A%20%20%09%3Fperson%20wdt%3AP569%20%3Fbirthdate%20filter(%3Fbirthdate%20%3E%20%221800-01-01T00%3A00%3A00%2B00%3A00%22%5E%5Exsd%3AdateTime)%20filter(%3Fbirthdate%20%3C%20%221951-01-01T00%3A00%3A00%2B00%3A00%22%5E%5Exsd%3AdateTime)%20.%0A%20%20%09%0A%20%20%09%3Fbirthplace%20wdt%3AP625%20%3Fbirthcoord%20.%0A%7D%0AGROUP%20BY%20%3Fperson%0AORDER%20BY%20%3Fbirthdatesampl";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document document = null;
            try {
                document = builder.parse(restEndpoint);
            } catch (SAXException e) {
                e.printStackTrace();
            }

            NodeList uriNodes = document.getElementsByTagName("uri");
            NodeList literalNodes = document.getElementsByTagName("literal");

            records = new ArrayList<>();
            for (int i = uriNodes.getLength() - 1; i >= 0; i--)
                records.add(new Record(uriNodes.item(i), literalNodes.item(i * 3), literalNodes.item(i * 3 + 1), literalNodes.item(i * 3 + 2)));
        }

        String paramQuery = request.getParameter("q");

        out.println("<form action='"+request.getRequestURI()+"' method='GET'>");
        out.println("Case insensitive search on names: <input type='text' name='q'>");
        out.println("<button type='submit'>Go!</button><br>");
        out.println("</form>");
        if (paramQuery == "" || paramQuery == null) {

        }
        else {
            out.println("<form action='database' method='POST'>");

            /*
            Our search results table.
             */
            List<ScoredRecord> queryResult = relatedRecords(paramQuery);
            out.println("<table style='width:80%'>");
            out.println("<tr><th>Name</th><th>Birth Coordinates</th><th>Birth Date</th><th>Save?</th></tr>");
            for (ScoredRecord qr : queryResult) {
                out.println("<tr>" + qr.toString() + "<td><input type='checkbox' name='databaseAction' value='" + qr.record.uri + "'></td></tr>");
            }
            out.println("</table>");
            out.println("<input type='submit'>");
            out.println("</form>");
        }
    }

    /**
     * Class for the records along with their scores of relevancy.
     * 0 is the highest score possible.
     */
    class ScoredRecord {
        Record record;
        double score;

        ScoredRecord(Record rec, ArrayList<Record> reference) {
            record = rec;
            score = -1000;
            for (Record ref : reference) {
                score = Math.max(-1 * rec.distance(ref), score);
            }
        }

        public String toString() {
            return record.toString(); // + "<td>" + score + "</td>";
        }
    }

    /**
     * Finds the related records to a query.
     * @param query
     * @return
     */
    List<ScoredRecord> relatedRecords(String query) {
        if (records == null)
            return new ArrayList<ScoredRecord>();

        ArrayList<Record> directMatches = new ArrayList<>();
        for (Record r : records) {
            if (r.name.toLowerCase().contains(query.toLowerCase())) {
                directMatches.add(r);
            }
        }

        ArrayList<ScoredRecord> scoredRecords = new ArrayList<>();
        for (Record record : records) {
            scoredRecords.add(new ScoredRecord(record, directMatches));
        }

        Collections.sort(scoredRecords, new Comparator<ScoredRecord>() {
            @Override
            public int compare(ScoredRecord o1, ScoredRecord o2) {
                return -Double.compare(o1.score, o2.score);
            }
        });

        return scoredRecords.subList(0, 20);
    }
}