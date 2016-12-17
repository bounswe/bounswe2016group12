package bounswe16group12.com.meanco.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.adapters.CommentAdapter;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.tasks.GetUserComments;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Fragment that will populate tab layout of profile activity.
 */

public class CommentsFragment extends Fragment {
    private Tracker mTracker;
    public static CommentAdapter mCommentsAdapter;

    public static final CommentsFragment newInstance()
    {
        CommentsFragment sInstance = new CommentsFragment();
        return sInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);

        mTracker = ((MeancoApplication) getActivity().getApplication()).getDefaultTracker();
        mTracker.setScreenName("COMMENT_FRAGMENT");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAutoActivityTracking(true);

        //new GetUserComments(MeancoApplication.GET_USER_COMMENTS_URL,getContext()).execute();
        mCommentsAdapter = new CommentAdapter(
                getActivity(), // The current context (this activity)
                R.layout.comment_listitem, // The name of the layout ID.
                -100);

        ListView listView = (ListView) rootView.findViewById(R.id.list_profile_comments);
        listView.setAdapter(mCommentsAdapter);

        mCommentsAdapter.addAll(DatabaseHelper.getInstance(getContext()).getComments(Functions.getUsername(getContext())));
        mCommentsAdapter.notifyDataSetChanged();
        return rootView;
    }


}
