package com.example.berfinkeleshomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    ProgressDialog prgDialog;
    RecyclerView comRecView;
    List<CommentItem> data = new ArrayList<CommentItem>();
    CommentAdapter adp;
    String news_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //data = new ArrayList<>();
        comRecView = findViewById(R.id.commentsrec);
        adp = new CommentAdapter(data,this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        comRecView.setLayoutManager(new LinearLayoutManager(this));
        comRecView.setAdapter(adp);

        Intent i = getIntent();
        news_id = i.getStringExtra("newsID");


    }

    @Override
    protected void onStart() {
        super.onStart();
        String url = "http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid/";
        StringBuilder str = new StringBuilder(url);
        str.append(news_id);
        CommentsTask tsk = new CommentsTask();
        tsk.execute(str.toString());
    }

    class CommentsTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(CommentsActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String urlStr = strings[0];
            StringBuilder buffer = new StringBuilder();

            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) !=null){

                    buffer.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            data.clear();
            Log.i("DEV",s);
            try {
                JSONObject obj = new JSONObject(s);

                if(obj.getInt("serviceMessageCode")==1){

                    JSONArray arr = obj.getJSONArray("items");

                    for (int i=0; i<arr.length();i++){
                        JSONObject current = (JSONObject) arr.get(i);

                        CommentItem item = new CommentItem(current.getInt("id"),
                                current.getString("name"),
                                current.getString("text")
                                );
                        data.add(item);
                    }
                }else{
                    //there's a problem
                }
                Log.i("DEV",String.valueOf(data.size()));
                adp.notifyDataSetChanged();
                prgDialog.dismiss();

            } catch (JSONException e) {
                Log.e("DEV",e.getMessage());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comments_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        else if(id == R.id.comment){
            Intent i = new Intent(CommentsActivity.this,PostCommentActivity.class);
            i.putExtra("newsID",news_id);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


}
