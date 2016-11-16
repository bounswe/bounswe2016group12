package bounswe16group12.com.meanco.fragments.home;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.activities.TopicDetailActivity;
import bounswe16group12.com.meanco.adapters.CustomHomeAdapter;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment{
    public static CustomHomeAdapter adapter;
    static List<Topic> topics;
    static List<Relation> relations;
    public static ListView listView;

    public static List<Topic> getTopics() {
        return topics;
    }
    public static List<Relation> getRelations() {
        return relations;
    }


    public HomeActivityFragment() {
            topics = new ArrayList<>();
            relations = new ArrayList<>();
            ArrayList<Tag> tagList = new ArrayList<>();
            tagList.add(new Tag("tag1"));
            tagList.add(new Tag("tag1"));
            tagList.add(new Tag("tag1"));
            tagList.add(new Tag("tag1"));
            ArrayList<Tag> tagList2 = new ArrayList<>();
            tagList2.add(new Tag("tag2"));
            tagList2.add(new Tag("tag2"));
            tagList2.add(new Tag("tag2"));
            tagList2.add(new Tag("tag2"));
            topics.add(new Topic("Donald Trump", tagList));
            topics.add(new Topic("Hillary Clinton", tagList2));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        adapter = new CustomHomeAdapter(getContext(), R.layout.fragment_home, topics);

        listView = (ListView) rootView.findViewById(R.id.content_home);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String message = adapter.getItem(position).getTopicName();
                Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                intent.putExtra("activityTitle", message);
                startActivity(intent);
            }
        });

        return rootView;
    }




}

