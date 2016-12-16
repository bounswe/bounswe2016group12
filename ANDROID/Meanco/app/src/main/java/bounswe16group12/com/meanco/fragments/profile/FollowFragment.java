package bounswe16group12.com.meanco.fragments.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FollowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FollowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowFragment extends Fragment {

    private Tracker mTracker;

    public FollowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTracker = ((MeancoApplication) getActivity().getApplication()).getDefaultTracker();
        mTracker.setScreenName("FOLLOW_FRAGMENT");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAutoActivityTracking(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followed, container, false);
    }

}
