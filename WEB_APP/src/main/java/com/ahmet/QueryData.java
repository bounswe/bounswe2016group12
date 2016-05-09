package com.ahmet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ahmet Ercan Tekden on 3.05.2016.
 */
public class QueryData {
    ArrayList<String> Columns;
    ArrayList<String[]> rows;

    /**
     * Creates a Query Objects by processing xml file acquired by connecting to endpoint of sparql link.
     * @param URL Rest Endpoint of query which gives xml file
     */
    public QueryData(String URL){
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
                String pattern="<binding name=\'"+Columns.get(i)+"\'>.+?ding>";
                Matcher m2= Pattern.compile(pattern).matcher(rowMatches.get(j));
                while (m2.find())
                    k[i]=m2.group();
            }
            rows.add(k);
        }

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
            oReader = new BufferedReader(new InputStreamReader(oConnection.getInputStream()));
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
