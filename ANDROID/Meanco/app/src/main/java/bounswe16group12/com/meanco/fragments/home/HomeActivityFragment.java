package bounswe16group12.com.meanco.fragments.home;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.fragments.profile.BioFragment;
import bounswe16group12.com.meanco.fragments.profile.NotificationsFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeActivityFragment extends Fragment{

    public HomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        String[] arr = {"ezgi", "furkan", "caner erkin"};
        List<String> topiclist = new ArrayList<String>(Arrays.asList(arr));

        ArrayAdapter<String> mTopicAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.fragment_listitem, R.id.topicitem, topiclist
        );

        ListView listView = (ListView) rootView.findViewById(R.id.content_home);
        listView.setAdapter(mTopicAdapter);
        return rootView;
    }




}
