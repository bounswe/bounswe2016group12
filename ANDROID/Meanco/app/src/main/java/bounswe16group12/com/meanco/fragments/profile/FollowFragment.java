package bounswe16group12.com.meanco.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.adapters.ProfileTopicAdapter;
import bounswe16group12.com.meanco.adapters.TopicSearchAdapter;

/**
 * Fragment that will populate tab layout of profile activity.
 */

public class FollowFragment extends Fragment {

    private Tracker mTracker;
    public static ProfileTopicAdapter mTopicsAdapter;

    public static final FollowFragment newInstance()
    {
        FollowFragment sInstance = new FollowFragment();
        return sInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_followed, container, false);
        mTracker = ((MeancoApplication) getActivity().getApplication()).getDefaultTracker();
        mTracker.setScreenName("FOLLOW_FRAGMENT");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAutoActivityTracking(true);

        mTopicsAdapter = new ProfileTopicAdapter(getActivity(),R.id.list_profile_followed);

        ListView listView = (ListView) rootView.findViewById(R.id.list_profile_followed);
        listView.setAdapter(mTopicsAdapter);

        return rootView;
    }

}
