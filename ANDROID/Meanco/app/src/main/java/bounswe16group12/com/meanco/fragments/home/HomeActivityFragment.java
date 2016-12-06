package bounswe16group12.com.meanco.fragments.home;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.activities.HomeActivity;
import bounswe16group12.com.meanco.activities.TopicDetailActivity;
import bounswe16group12.com.meanco.adapters.CustomHomeAdapter;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.GetTopicDetail;
import bounswe16group12.com.meanco.tasks.GetTopicList;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment{
    public static CustomHomeAdapter adapter;
    public static ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());
        List<Topic> topics = databaseHelper.getAllTopics();


        adapter = new CustomHomeAdapter(getContext(), R.layout.fragment_home, topics);

        listView = (ListView) rootView.findViewById(R.id.content_home);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Task before intent fires
                new GetTopicDetail(MeancoApplication.SITE_URL,""+adapter.getItem(position).topicId, getContext()).execute();
                String message = adapter.getItem(position).topicName;
               // String topicId = adapter.getItem(position).topicId+"";
                Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                intent.putExtra("activityTitle", message);
               // intent.putExtra("topicId", topicId);
                startActivity(intent);
            }
        });

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)  rootView.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        adapter.clear();
                        new GetTopicList(MeancoApplication.SITE_URL, getContext()).execute();
                        adapter.updateArray();
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                }
        );



        return rootView;
    }




}

