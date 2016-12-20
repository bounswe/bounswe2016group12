package bounswe16group12.com.meanco.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.fragments.profile.CommentsFragment;
import bounswe16group12.com.meanco.fragments.profile.FollowFragment;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Profile page specific to user. User can view her followed topics and the topics that she commented on on
 * her profile page.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Google analytics data.
         */
        Tracker mTracker = ((MeancoApplication) getApplication()).getDefaultTracker();
        mTracker.setScreenName("PROFILE_ACTIVITY");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAutoActivityTracking(true);

        /**
         * Set title to username.
         */
        setTitle(Functions.getUsername(ProfileActivity.this));

        setContentView(R.layout.activity_profile);

        /**
         * Set tab layout for two pages (Comments and followed topics).
         */
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new ProfileAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        ActionBar ab = getSupportActionBar();

        //Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Set fragments on tab layout.
     */
    public class ProfileAdapter extends FragmentPagerAdapter {

        public ProfileAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FollowFragment.newInstance();
                case 1:
                    return CommentsFragment.newInstance();
                default:
                    return FollowFragment.newInstance();
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
                    return "FOLLOWED TOPICS";
                case 1:
                default:
                    return "CONTRIBUTIONS";
            }
        }


    }
}