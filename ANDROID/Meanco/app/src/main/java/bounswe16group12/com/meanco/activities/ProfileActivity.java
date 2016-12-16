package bounswe16group12.com.meanco.activities;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.fragments.profile.CommentsFragment;

public class ProfileActivity {//extends AppCompatActivity implements CommentsFragment.OnFragmentInteractionListener, FollowFragment.OnFragmentInteractionListener {
/*    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTracker = ((MeancoApplication) getApplication()).getDefaultTracker();
        mTracker.setScreenName("PROFILE_ACTIVITY");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_profile);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ProfileAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class ProfileAdapter extends FragmentPagerAdapter {

        public ProfileAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BioFragment();
                case 1:
                default:
                    return new NotificationsFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "BIO";
                case 1:
                default:
                    return "NOTIFICATIONS";
            }
        }


    }*/
}