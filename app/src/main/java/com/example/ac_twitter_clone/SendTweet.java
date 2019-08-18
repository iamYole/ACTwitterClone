package com.example.ac_twitter_clone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweet extends AppCompatActivity {

    private EditText tweetMessage;
    private ListView listOfTweets;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        tweetMessage=findViewById(R.id.txtMessage);
        listOfTweets=findViewById(R.id.tweetList);

        dialog=new ProgressDialog(this);

        loadTweets();
    }

    public void sendTweet(View view){
        try {
            ParseObject parseObject = new ParseObject("MyTweets");
            parseObject.put("tweet", tweetMessage.getText().toString());
            parseObject.put("user", ParseUser.getCurrentUser().getUsername());

            dialog.setMessage("Loading...");
            dialog.show();

            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        loadTweets();
                        FancyToast.makeText(SendTweet.this,"Sent", Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                        tweetMessage.setText("");
                        tweetMessage.setHint("Send a tweet");
                    }else
                    {
                        FancyToast.makeText(SendTweet.this,e.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }
                    dialog.dismiss();
                }
            });
        }
        catch(Exception ex){
            FancyToast.makeText(SendTweet.this,ex.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
            dialog.dismiss();
        }
    }

    private void loadTweets(){
        final ArrayList<HashMap<String,String>> tweets=new ArrayList<>();
        final SimpleAdapter adapter=new SimpleAdapter(SendTweet.this,tweets,android.R.layout.simple_list_item_2,new String[]{"tweetUserName","tweetValue"},
                new int[]{android.R.id.text1,android.R.id.text1});

        try
        {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweets");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size()>0 && e==null){
                        for(ParseObject tweetObject:objects){
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUserName", tweetObject.getString("user"));
                            userTweet.put("tweetValue", tweetObject.getString("tweet"));
                            tweets.add(userTweet);
                        }
                        listOfTweets.setAdapter(adapter);
                    }
                }
            });
        }catch (Exception ex){
            FancyToast.makeText(SendTweet.this,ex.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
        }
    }
}
