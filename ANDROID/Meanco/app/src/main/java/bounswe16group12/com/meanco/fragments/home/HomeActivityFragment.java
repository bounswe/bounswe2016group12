package bounswe16group12.com.meanco.fragments.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.activities.TopicDetailActivity;
import bounswe16group12.com.meanco.adapters.CustomHomeAdapter;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.adapters.RelationsAdapter;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.GetTopicDetail;
import bounswe16group12.com.meanco.tasks.GetTopicList;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment{
    public static CustomHomeAdapter adapter;
    public static ListView listView;
    public static ListView relationsListView;
    public static RelationsAdapter relationAdapter;
    public static List<Relation> relations;
    private Tracker mTracker;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * Google analytics data.
         */
        mTracker = ((MeancoApplication) getActivity().getApplication()).getDefaultTracker();
        mTracker.setScreenName("HOME_FRAGMENT");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAutoActivityTracking(true);

        new GetTopicList(MeancoApplication.SITE_URL, getContext()).execute();

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        /**
         * Put all topics in local db.
         */
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());
        List<Topic> topics = databaseHelper.getAllTopics();


        adapter = new CustomHomeAdapter(getContext(), R.layout.fragment_home, topics);

        listView = (ListView) rootView.findViewById(R.id.content_home);
        listView.setAdapter(adapter);

        /**
         * Navigate to topic detail page on item click.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Task before intent fires
                new GetTopicDetail(MeancoApplication.SITE_URL,adapter.getItem(position).topicId, getContext()).execute();
                int topicId = adapter.getItem(position).topicId;
                Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                intent.putExtra("topicId", topicId);
                startActivity(intent);
            }
        });

        /**
         * On long press, show relations of a topic as a dialog.
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Topic topic = adapter.getItem(position);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
                relations = databaseHelper.getAllRelations(topic.topicId);
                relationAdapter = new RelationsAdapter(getContext(), R.layout.relation_dialog_view, relations, topic.topicId);

                final View customView = inflater.inflate(R.layout.relation_dialog_view, null, false);


               new AlertDialog.Builder(getContext())
                        .setTitle(topic.topicName + "'s Relations")
                        .setView(customView)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                ListView relationListView = (ListView) customView.findViewById(R.id.relations_list);
                relationListView.setAdapter(relationAdapter);

                relationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getContext(), TopicDetailActivity.class);
                        Relation r = relationAdapter.getItem(i);
                        if(topic.topicId==r.topicFrom)
                            intent.putExtra("topicId", r.topicTo);
                        else
                            intent.putExtra("topicId", r.topicFrom);

                        startActivity(intent);

                    }
                });
                return true;
            }
        });


        /**
         * Get topics from server on refresh of layout.
         */
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)  rootView.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.

                        new GetTopicList(MeancoApplication.SITE_URL, getContext()).execute();
                        refreshLayout.setRefreshing(false);
                    }
                }
        );



        return rootView;
    }
}

