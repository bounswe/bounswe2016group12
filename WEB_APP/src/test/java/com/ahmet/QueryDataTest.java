package test.java.com.ahmet;
import main.java.com.ahmet.QueryData;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Created by Ahmet Ercan Tekden on 6.05.2016.
 */
public class QueryDataTest {


    private static QueryData tester;

    @BeforeClass
    public static void testSetup() {
        tester=new QueryData("https://query.wikidata.org/bigdata/namespace/wdq/sparql?query=" +
                "%23Cats%0" +
                "ASELECT%20%3Fitem%20%3FitemLabel%0A" +
                "WHERE%0A" +
                "%7B%0A" +
                    "%09%3Fitem%20wdt%3AP31%20wd%3AQ146%20.%20%0A" +
                    "%09SERVICE%20wikibase%3Alabel%20%7B%20bd%3AserviceParam%20wikibase%3Alanguage%20%22en%22%20%7D%0A" +
                "%7D");
        /* Query is=
        #Cats
        SELECT ?item ?itemLabel
        WHERE
        {
	        ?item wdt:P31 wd:Q146 .
	        SERVICE wikibase:label { bd:serviceParam wikibase:language "en" }
        }
         */
    }

    @Test
    public void testColumn() {

        assertEquals("item",tester.Columns.get(0));
        assertEquals("itemLabel",tester.Columns.get(1));
    }
    @Test
    public void testRow(){
        assertEquals("<binding name='item'>\t\t\t\t<uri>http://www.wikidata.org/entity/Q434529</uri>\t\t\t</binding>",tester.rows.get(0)[0]);
    }

}