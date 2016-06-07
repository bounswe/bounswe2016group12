import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import sun.security.jca.GetInstance.Instance;



@WebServlet("/EYServlet")
public class EYServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.getWriter().append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
								+ "<html>"
								+ "<head>"
								+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">"
								+ "<title>Enes YILMAZ</title>"
								+ "</head><body>");
		if ( req.getParameter("get_save") != null )
		{
			Connection connection = mysqlConnection();
			Statement statement;
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
				resp.getWriter().append("SQL error");
				return;
			}
			/* Get results from the db and print them */
			String query = "SELECT * FROM my_favorites;";                           //create save table in the database
			ResultSet rs;
			//System.out.println(query);
			try {
				rs = statement.executeQuery(query);
				//System.out.println(rs.next());
				if (!rs.next()) 
					resp.getWriter().append("NO SAVED ITEMS");
				else 
				{
					String table = "<table border=\"1\" style=\"width:100%\">\n"
									+ "<tr>\n"
									+ "<th>Artist</th>\n"
									+ "<th>Commons</th>\n"
									+ "<th>Gender</th>\n"
								    + "</tr>\n";
					do 
					{
						int h = rs.getInt("h");
						//System.out.println(h);
						String queryItem = "SELECT * FROM my_favorites WHERE h='"
								+ h + "';";
						Statement stmt = connection.createStatement();
						ResultSet rsItem = stmt.executeQuery(queryItem);

						String row = "";
						if (rsItem.next()) {
							row = "<tr>";
							row += "<td>";
							row += "<a href=\"https://www.wikidata.org/wiki/Q"
									+ h + "\">"
									+ "https://www.wikidata.org/wiki/Q" + h;
							row += "</td>\n";
							row += "<td>";
							row += rsItem.getString("commons");
							row += "</td>\n";
							//System.out.println(rsItem.getString("commons"));
							
							if (rsItem.getString("sex").equals("f")) 
								row += "<td>" + "female" + "</td>\n";
							else 
								row += "<td>" + "male" + "</td>\n";
	
							row += "</tr>";
						}
						table += row;
						stmt.close();
					} while (rs.next());
					table += "</table>";
					resp.getWriter().append("<br><br>Please <a href=\"/EY2010400114/EYServlet\"> click here</a> to redirect to main page.");
					resp.getWriter().append(table + "</body></html>");
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				resp.getWriter().append("SQL error");
				return;
			}
		}
		else if ( req.getParameter("csave") != null )
		{
			String[] items = req.getParameterValues("csave");
			//System.out.println(items[0]);
			String[][] results = new String[items.length][3];
			//System.out.println(items.length);
			
			for (int i = 0; i < items.length; ++i) 
			{
				String[] arr = items[i].split(",");
				for (int j = 0; j < arr.length; ++j) 
				{
					results[i][j] = arr[j];
				}
				results[i][0] = results[i][0].substring(results[i][0].indexOf('Q') + 1);// unique item id.
			}
			
			Connection connection = mysqlConnection();
			Statement statement;
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {

				e.printStackTrace();
				resp.getWriter().append("SQL error");
				return;
			}
			
			for (int i = 0; i < items.length; ++i) 
			{
				try {
					String query = "INSERT INTO my_favorites(h, commons, sex)";
					query += "VALUES('" + results[i][0] + "','" + results[i][1] + "','" + results[i][2].charAt(0) + "');";
					statement.executeUpdate(query);

				} catch (SQLException e) {
					e.printStackTrace();
					resp.getWriter().append("SQL error");
					return;
				}
			}
			resp.getWriter().append("Your choices has been saved successfully.<br>Please <a href=\"/EY2010400114/EYServlet\"> click here</a> to redirect.");
			
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if ( req.getParameter("query") == null )
		{
			resp.getWriter().append("<form id=\"queryform\" method=\"get\" name=\"queryform\""+
									"action=\"/EY2010400114/EYServlet\">"+
									"Type Game Of Thrones: <input type=\"text\" name=\"query\" size=\"32\">"+
									"<br> <input type=\"submit\" name=\"submit\""+
									"value=\"Search for something\">"+
									"</form>"+
									"<br>"+
									"<form id=\"retrieveform\" method=\"get\" name=\"getform\""+
									"action=\"/EY2010400114/EYServlet\">"+
									"<input type=\"submit\" name=\"get_save\" value=\"Get Saved\">"+
									"</form>");
		}
		else if ( req.getParameter("query") != null )
		{
			String userQuery = req.getParameter("query");
			String query = "https://query.wikidata.org/sparql?query=";
			query += "SELECT%20%3Fh%20%3Fcommons%20%3FgenderLabel%20" 
					+
					"WHERE{%20"
					+
					"%20%3Fh%20wdt%3AP31%20wd%3AQ5."
					+
					"%20%3Fh%20wdt%3AP106%20wd%3AQ33999."
					+
					"%20%3Fh%20wdt%3AP106%20wd%3AQ177220."
					+
					"%20%3Fh%20wdt%3AP21%20%3Fgender."
					+
					"%20%3Fh%20wdt%3AP373%20%3Fcommons."
					+
					"%20SERVICE%20wikibase%3Alabel%20{%20bd%3AserviceParam%20wikibase%3Alanguage%20\"en\"}"
					+
					"}ORDER%20BY%20(%3Fcommons)";
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			
			try {
				dBuilder = dbFactory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resp.getWriter().append("Internal error.");
				return;
			}
			Document doc;
			try {
				doc = dBuilder.parse(query);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resp.getWriter().append("parsing error");
				return;
			}
			NodeList nList = doc.getElementsByTagName("result");
			String[] h = new String[nList.getLength()];
			String[] commons = new String[nList.getLength()];
			String[] genderLabel = new String[nList.getLength()];
			/* Parse and print the data */
			int count = 0;
			for (int i = 0; i < nList.getLength(); ++i) 
			{
				Node node = (Node) nList.item(i);
				Element element = (Element) node;
				NodeList nl = element.getElementsByTagName("binding");
				for (int j = 0; j < nl.getLength(); ++j) 
				{
					Element e = (Element) nl.item(j);
					
					if (e.getAttribute("name").equalsIgnoreCase("h")) 
						h[i] = e.getTextContent();
					if (e.getAttribute("name").equalsIgnoreCase("commons")) 
						commons[i] = e.getTextContent();
					if (e.getAttribute("name").equalsIgnoreCase("genderLabel")) 
						genderLabel[i] = e.getTextContent();
				}
			}
			userQuery = userQuery.toLowerCase();
			String table = "<form name=\"ftable\" method=\"post\">";
			table += "<table border=\"1\" style=\"width:100%\">\n" + "<tr>\n"
					+ "<th>Save</th>" + "<th>Artist</th>\n"
					+ "<th>Commons</th>\n" + "<th>Gender</th>\n"
					+ "</tr>\n";
			for (int i = 0; i < nList.getLength(); ++i) 
			{
				boolean flag = !h[i].toLowerCase().contains(userQuery);
				boolean temp;
				
				if (commons[i] != null) 
					temp = !commons[i].toLowerCase().contains(userQuery);
				else 
					temp = true;

				flag = flag && temp;
				
				if (genderLabel[i] != null)
					temp = !genderLabel[i].toLowerCase().contains(userQuery);
				else 
					temp = true;
				
				flag = flag && temp;
				if (flag) // skipping the item if it does not contain the given input.		
					continue;
				String row = "<tr>\n";
				row += "<td>";
				row += "<input type=\"checkbox\" name=\"csave\" value=\"";
				row += h[i] + "," + commons[i] + "," + genderLabel[i];
				row += "\">";
				row += "</td>\n";
				row += "<td>";
				row += "<a href=\"" + h[i] + "\">" +  h[i];   //belki hLabel getirebilirim
				row += "</td>\n";
				row += "<td>";
				row += "<a href=\"" + commons[i] + "\">" + commons[i];
				row += "</td>\n";
				row += "<td>";
				row += "<a href=\"" + "\">" + genderLabel[i];
				row += "</td>\n";	
				row += "</tr>\n";
				table += row;
			}
			table += "</table>";
			table += "<table><tr><td><input id=\"submit\" name=\"submit\" type=\"submit\" value=\"save\"/></td></tr></table>";
			table += "</form>";
			resp.getWriter().append(table);
			resp.getWriter().append("</body></html>");
		}
	}
	/**
	 * \brief Connects to database and returns the connection handle
	 * 
	 * @return
	 */
	public Connection mysqlConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		String url = "jdbc:mysql://enesdatabase.cp1nrnmapnf4.us-west-2.rds.amazonaws.com:3306:3306/enesdb";
		String userName = "enes";
		String password = "enesyilmaz";
		Connection connection;
		try {
			connection = DriverManager.getConnection(url, userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return connection;
	}

	/*
	 * ! unique serial number
	 */
	private static final long serialVersionUID = 3818282702335774732L;

	/**
	 * \brief The constructor calls the superclass constructor.
	 */
	public EYServlet() {
		super();
	}
}

