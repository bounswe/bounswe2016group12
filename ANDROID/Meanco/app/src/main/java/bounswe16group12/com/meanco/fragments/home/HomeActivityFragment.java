package bounswe16group12.com.meanco.fragments.home;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.adapters.CustomHomeAdapter;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment{
    static CustomHomeAdapter adapter;
    static List<Topic> topics;

    public static List<Topic> getTopics() {
        return topics;
    }

    public static void setTopics(List<Topic> topics) {
        HomeActivityFragment.topics = topics;
    }

    public HomeActivityFragment() {
        topics=new ArrayList<>();
        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("tag1"));
        tagList.add(new Tag("tag2"));
        tagList.add(new Tag("tag3"));
        tagList.add(new Tag("tag4"));
        topics.add(new Topic("Donald Trump", tagList));
        topics.add(new Topic("Hillary Clinton", tagList));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        adapter = new CustomHomeAdapter(getContext(), R.layout.fragment_home, topics);



        ListView listView = (ListView) rootView.findViewById(R.id.content_home);
        listView.setAdapter(adapter);


        return rootView;
    }




}
