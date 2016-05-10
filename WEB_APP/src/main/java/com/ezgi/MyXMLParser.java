package main.java.com.ezgi;

/**
 * Created by Ezgi on 5/6/2016.
 */
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MyXMLParser {

    String query = "";
    HashMap<String, ArrayList<String>> ezgistable;

    public MyXMLParser(String query){
        this.query = query;
        ezgistable = new HashMap<String, ArrayList<String>>();
    }



    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String readXML(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String xmlText = readAll(rd);
            //System.out.println(xmlText);
            return xmlText;
        } finally {
            is.close();
        }
    }

    public void getData() throws IOException, SAXException, ParserConfigurationException {
        String xml = readXML(query);
        xml = xml.replace("\n", "");
        xml = xml.replace("\t", "");
        System.out.println(xml);


        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        Document doc = db.parse(is);
        doc.getDocumentElement().normalize();
        Element sparql = doc.getDocumentElement();
        NodeList results = (NodeList) sparql.getChildNodes().item(1);

        for (int i=0; i<results.getLength(); i++) {
            NodeList result = (NodeList) results.item(i);
            for (int j=0; j<result.getLength(); j++){
                Element e = (Element) result.item(j);

                String attribute = e.getAttribute("name");
                String value = e.getFirstChild().getTextContent();

                if(i==0)
                    ezgistable.put(attribute, new ArrayList<String>());
                ezgistable.get(attribute).add(value);
            }

        }
    }

}




