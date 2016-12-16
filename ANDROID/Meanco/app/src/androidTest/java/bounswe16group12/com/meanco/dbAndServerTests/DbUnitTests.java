package bounswe16group12.com.meanco.dbAndServerTests;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DbUnitTests {

    List<Topic> topics;



    @Before
    public void setUp(){
        topics = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getAllTopics();
    }



    @Test
    public void test1DeleteAllTopics() {
        DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).clearTable(0);
        List<Topic> newTopics = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getAllTopics();

        assertThat(newTopics.size(), is(0));
    }

    @Test
    public void test2AddTopic() {
        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag(-5, "context of test", "tag of test", "www.google.com"));
        Topic t = new Topic(Integer.MIN_VALUE, "deneme", tagList);
        DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).addTopic(t);

        List<Topic> newTopics = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getAllTopics();
        assertThat(newTopics.size(), is(1));
        assertThat(newTopics.get(0).topicId, is(Integer.MIN_VALUE));
    }

    @Test
    public void test3UpdateTagsOfTopic() {

        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag(-5, "context of test", "tag of test", "www.google.com"));
        Topic t = new Topic(Integer.MIN_VALUE, "deneme", tagList);
        DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).addTopic(t);

        t = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getAllTopics().get(0);

        Log.i("tag", t.topicName);
        List<Tag> tags = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getAllTags(Integer.MIN_VALUE);

        //Log.i("tag", tags.get(0).tagName);
        tags.add(new Tag(-6, "context of test", "tag of test", "www.google2.com"));
        DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).addTopic(t);

        t = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getTopic(Integer.MIN_VALUE);

        assertThat(t.tags.get(0).tagId, is(-5));
        assertThat(t.tags.get(1).tagId, is(-6));


    }

    @Test
    public void test4EditComment() {

        DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).addComment(new Comment(-10, Integer.MIN_VALUE, "deneme", "ezgi"));

        Comment c = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getAllComments(Integer.MIN_VALUE)
                .get(0);
        DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).addComment(new Comment(c.commentId, c.topicId, "changed", "ezgi"));
        Comment cNew = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getComment(c.commentId);

        assertThat(c.content, not(cNew.content));
        assertThat(cNew.content, is("changed"));


    }

    @Test
    public void test5DeleteAllComments() {
        DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).clearTable(2);
        List<Comment> newComments = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).getAllComments(Integer.MIN_VALUE);

        assertThat(newComments.size(), is(0));
    }

    @After
    public void finish() {
        DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext()).clearAll();
    }


}
