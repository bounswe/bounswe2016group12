package bounswe16group12.com.meanco;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Topic;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DbUnitTests {

    List<Topic> topics;

    @Before
    public void setUp(){
        topics = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getAllTopics();
    }

    @After
    public void finish() {

    }

    @Test
    public void testDeleteAllTopics() {
        DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).clearTable(0);
        List<Topic> newTopics = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getAllTopics();;

        assertThat(newTopics.size(), is(0));
    }


    /*
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("bounswe16group12.com.meanco", appContext.getPackageName());
    }*/


}
