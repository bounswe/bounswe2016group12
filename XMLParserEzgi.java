package com.company;
        import java.io.*;
        import java.net.URL;
        import java.nio.charset.Charset;

        import com.sun.org.apache.xerces.internal.parsers.DOMParser;
        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.NodeList;
        import org.xml.sax.InputSource;
        import org.xml.sax.SAXException;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;
        import javax.xml.parsers.ParserConfigurationException;

public class XMLParser {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readXML(String url) throws IOException {
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

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException{

        String query = "https://query.wikidata.org/bigdata/namespace/wdq/sparql?query=%23Books%20by%20a%20given%20Author%20including%20genres%2C%20series%2C%20and%20publication%20year%0ASELECT%20%3FauthorLabel%20%3FbookLabel%20%3Fgenre_label%20%3FpublicationDate%0AWHERE%0A%7B%0A%09%3Fauthor%20%3Flabel%20%22Douglas%20Adams%22%40en%20.%0A%09%3Fbook%20wdt%3AP31%20wd%3AQ571%20.%0A%09%3Fbook%20wdt%3AP50%20%3Fauthor%20.%0A%09OPTIONAL%20%7B%0A%09%09%3Fbook%20wdt%3AP136%20%3Fgenre%20.%0A%09%09%3Fgenre%20rdfs%3Alabel%20%3Fgenre_label%20filter%20(lang(%3Fgenre_label)%20%3D%20%22en%22).%0A%09%7D%0A%09OPTIONAL%20%7B%0A%09%09%3Fbook%20wdt%3AP577%20%3FpublicationDate%20.%0A%09%7D%0A%09SERVICE%20wikibase%3Alabel%20%7B%0A%09%09bd%3AserviceParam%20wikibase%3Alanguage%20%22en%22%20.%0A%09%7D%0A%7D";
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
                System.out.println(e.getAttribute("name") + " : " + e.getFirstChild().getTextContent());
            }

        }

    }




    }



