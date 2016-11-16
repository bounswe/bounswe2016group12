package bounswe16group12.com.meanco.fragments.home;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopicDetailActivityFragment extends Fragment {
    private ArrayAdapter<String> mCommentsAdapter;

    public TopicDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_topic_detail, container, false);

        String topicName = getActivity().getTitle().toString();
        ArrayList<Tag> tg = new ArrayList<>();
        for(Topic t: HomeActivityFragment.getTopics()){
            if(t.getTopicName().equals(topicName)){
                tg.addAll(t.getTags());
            }
        }



        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linearlayout_detail);


        for(int i=0; i<tg.size(); i++){

            TextView tagView = new TextView(getContext());

            tagView.setText(tg.get(i).getTagName());
            tagView.setBackgroundResource(R.drawable.tagbg);
            tagView.setTextColor(Color.WHITE);
            tagView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMarginEnd(10);
            tagView.setLayoutParams(lp);
            tagView.setPadding(15, 15, 15, 15);
            linearLayout.addView(tagView);
        }

        List<String> comments = new ArrayList<String>();

        Comment c1 = new Comment("After a video from 2005 was released of Trump making obscene comments and bragging about sexually abusing women was released, the Republican hopeful responded by writing the episode off as just ‘locker room banter’.\n" +
                "Read more at http://www.marieclaire.co.uk/entertainment/people/donald-trump-quotes-57213#iuslG35o6fl228Wo.99");
        Comment c2 = new Comment("Donald was born on the 14th of June, 1946 in New York, to Fred and Elizabeth Trump. His father, who ended up being one of New York’s biggest property developers, was American-German. Fred Trump was once arrested at a KKK rally and was sued by the US Justice Department for refusing to rent flats to African-American people. His mother was Scottish and had left poverty in Scotland to live in America. So Trump does actually have a family, which might come as a surprise to those of us who’d assumed he was forged in a cave, like an orc. Trump was expelled from school at the age of 13 and sent to the New York military academy.\n" +
                "Read more at http://www.marieclaire.co.uk/entertainment/people/donald-trump-quotes-57213#iuslG35o6fl228Wo.99");
        Comment c3 = new Comment("nooooooo not again.");
        Comment c4 = new Comment("The main thing is he is not handsome.");
        Comment c5 = new Comment("Then deciding that insulting Hillary’s husband on national television simply wasn’t enough, Trump went on to also throw some pretty serious accusations at the Democrat presidential candidate too: ‘I’ve said some foolish things but there’s a big difference between the words and actions of other people,’ he went on. ‘Bill Clinton has actually abused women and Hillary has bullied, attacked, shamed and intimidated his victims.’\n" +
                "Read more at http://www.marieclaire.co.uk/entertainment/people/donald-trump-quotes-57213#iuslG35o6fl228Wo.99");
        Comment c6 = new Comment("He is awesome.");


        comments.add(c1.getContent());
        comments.add(c2.getContent());
        comments.add(c3.getContent());
        comments.add(c4.getContent());
        comments.add(c5.getContent());
        comments.add(c6.getContent());

        mCommentsAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_detail_comment, // The name of the layout ID.
                R.id.list_item_detail_comment_textview, // The ID of the textview to populate.
                comments);

        ListView listView = (ListView) rootView.findViewById(R.id.listView_topic_comments);
        listView.setAdapter(mCommentsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity(), "Comment is Upvoted.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
