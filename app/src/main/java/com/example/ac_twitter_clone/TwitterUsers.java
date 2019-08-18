package com.example.ac_twitter_clone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        FancyToast.makeText(TwitterUsers.this,"Welcome "+ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_LONG,FancyToast.SUCCESS,false).show();

       listView=findViewById(R.id.listView);
       tUsers=new ArrayList<>();
       adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,tUsers);
       listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
       listView.setOnItemClickListener(this);

        getUsers();
    }

    private void getUsers(){
        try
        {
            ParseQuery<ParseUser> query=ParseUser.getQuery();
            query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            query.orderByAscending("username");
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(objects.size()>0 && e==null){
                        for(ParseUser twitterUsers:objects){
                            tUsers.add(twitterUsers.getUsername());
                        }
                        listView.setAdapter(adapter);

                        for (String twitterUser: tUsers) {
                            if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)) {
                                    listView.setItemChecked(tUsers.indexOf(twitterUser), true);
                                }
                            }
                        }
                    }
                }
            });
        }catch(Exception ex){
            FancyToast.makeText(TwitterUsers.this,ex.getMessage(), Toast.LENGTH_LONG,FancyToast.ERROR,false).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.LogOut:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent=new Intent(TwitterUsers.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long ID) {
        CheckedTextView checkedTextView=(CheckedTextView)view;

        if(checkedTextView.isChecked()){
            FancyToast.makeText(TwitterUsers.this,"You are now following "+tUsers.get(position),Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
            ParseUser.getCurrentUser().add("fanOf",tUsers.get(position));
        }
        else
        {
            FancyToast.makeText(TwitterUsers.this,"You unfollowed "+tUsers.get(position),Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
            ParseUser.getCurrentUser().getList("fanOf").remove(tUsers.get(position));

            List userFanList=ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",userFanList);
        }
        try {
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                }
            });
        }
        catch (Exception ex){
            FancyToast.makeText(TwitterUsers.this,ex.getMessage(),Toast.LENGTH_LONG,FancyToast.INFO,false).show();
        }
    }
}
