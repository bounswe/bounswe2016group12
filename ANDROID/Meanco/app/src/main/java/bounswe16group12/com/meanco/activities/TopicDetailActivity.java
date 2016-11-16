package bounswe16group12.com.meanco.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bounswe16group12.com.meanco.R;

public class TopicDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        setTitle(getIntent().getStringExtra("activityTitle").toString());
    }
}
