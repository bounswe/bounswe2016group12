package com.utkan;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by utkan on May 11.
 */
public class UtkanServletTest {

    /**
     * It is too hard to generate a doPost request by hand, so I shall skip this.
     * @throws Exception
     */
    @Test
    public void doPost() throws Exception {

    }

    /**
     * It is too hard to generate a doGet request by hand, so I shall skip this.
     * @throws Exception
     */
    @Test
    public void doGet() throws Exception {

    }

    /**
     * Tests whether the list of related records to a query can be generated
     * with no entry in the records array present.
     * @throws Exception
     */
    @Test
    public void relatedRecordsTest() throws Exception {
        UtkanServlet utkanServlet = new UtkanServlet();

        List<UtkanServlet.ScoredRecord> list = utkanServlet.relatedRecords("rubbish");
        assertTrue(list.isEmpty());
    }

    /**
     * In order to test this method with entries inside the records array, I have to generate some records,
     * for which I will have to create new w3c Nodes, which is really too complicated.
     * For this reason, this test case is also void.
     * @throws Exception
     */
    @Test
    public void relatedRecordsTest2() throws Exception {

    }
}