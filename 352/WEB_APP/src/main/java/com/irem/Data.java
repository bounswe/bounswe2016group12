package main.java.com.irem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Irem Zog on 10.05.2016.
 */
public class Data {
    public ArrayList<String> Columns;
    public ArrayList<String[]> rows;
    /**
     * Creates a Query Objects by processing xml file acquired by connecting to endpoint of sparql link.
     * @param URL Rest Endpoint of query which gives xml file
     */
    public Data(String URL){
        Columns=new ArrayList<>();
        rows=new ArrayList<>();

        processXML(URL);

    }

    /**
     * Processes xml of given url to to create table data.
     * @param URL Url of Xml of sparql query.
     */
    public void processXML(String URL){
        String s=getURLContent(URL);
        List<String> colMatches = new ArrayList<>();
        List<String> rowMatches = new ArrayList<>();
        Matcher mRows = Pattern.compile("<result>.*?</result>")
                .matcher(s);
        while (mRows.find()) {
            rowMatches.add(mRows.group());
        }
        Matcher mCols = Pattern.compile("<variable name=.*?>")
                .matcher(s);
        while (mCols.find()) {
            colMatches.add(mCols.group());
        }
        for(int i=0;i<colMatches.size();i++){
            Columns.add(colMatches.get(i).substring(16,colMatches.get(i).length()-3));
        }
        for(int j=0;j<rowMatches.size();j++){
            String[] k=new String[Columns.size()];
            for(int i=0;i<k.length;i++){
                k[i]="";
                //TODO: Remove empty spaces inside row matches.
                String pattern="<binding name=\'"+Columns.get(i)+"\'>.+?ding>";
                Matcher m2= Pattern.compile(pattern).matcher(rowMatches.get(j));
                while (m2.find())
                    k[i]=m2.group();
            }
            rows.add(k);
        }

    }

    /**
     * Calculates points according to my selected data. This is not generic function, it works with my sparql query.
     *
     * @param query term entered by user.
     * @return number of rows corresponding to searched user term.
     */
    public int calculatePointsAhmet(String query){
        ArrayList<Integer> points=new ArrayList<>();
        String[] queryWords;
        queryWords = query.toLowerCase().split(" ");
        for(int i=0;i<rows.size();i++){
            int rowPoints=0;
            for(int j=1;j<rows.get(i).length;j++){
                int labelPoint=0;
                String shortLabel="";
                Matcher m=Pattern.compile("<literal.*literal>").matcher(rows.get(i)[j]);
                while(m.find()){
                    shortLabel=m.group().substring(23,m.group().length()-10);
                    System.out.println(shortLabel);
                }
                for(int k=0;k<queryWords.length;k++){
                    //TODO: Find a Better Fix.
                    if(queryWords[k].equalsIgnoreCase("male")){
                        if(shortLabel.equalsIgnoreCase("male"))
                            labelPoint+=100;
                    }
                    else {
                        if (shortLabel.toLowerCase().contains(queryWords[k])) {
                            labelPoint += 100;
                        }
                    }
                }
                rowPoints+=labelPoint;
            }
            points.add(rowPoints);
        }
        //TODO: Add Better Sorting Algorithm
        for (int i = 0; i < rows.size(); i++) {
            for (int j = i+1; j < rows.size(); j++) {
                if (points.get(j)>points.get(i)) {
                    Collections.swap(points,i,j);
                    Collections.swap(rows,i,j);
                }
            }
        }
        int zeroIndex=points.size();
        for(int i=points.size()-1;i>=0;i--){
            if(points.get(i)==0)zeroIndex=i;

        }
        return zeroIndex;
    }

    /**
     * Gets Source Code of Url
     * @param p_sURL Url of web address
     * @return String containing source code.
     */
    public String getURLContent(String p_sURL)
    {
        URL oURL;
        URLConnection oConnection;
        BufferedReader oReader;
        String sLine;
        StringBuilder sbResponse;
        String sResponse = null;

        try
        {
            oURL = new URL(p_sURL);
            oConnection = oURL.openConnection();
            oReader = new BufferedReader(new InputStreamReader(oConnection.getInputStream(),"UTF8"));
            sbResponse = new StringBuilder();

            while((sLine = oReader.readLine()) != null)
            {
                sbResponse.append(sLine);
            }

            sResponse = sbResponse.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return sResponse;
    }
}
