package com.example.berfinkeleshomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsDetailsActivity extends AppCompatActivity {
    String news_id;
    TextView txttitle;
    TextView txtdate;
    TextView txttext;
    ImageView imgnews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txttitle = findViewById(R.id.txttitle);
        txtdate = findViewById(R.id.txtdate);
        txttext = findViewById(R.id.txttext);
        imgnews = findViewById(R.id.imgnews);
        Intent i = getIntent();
        NewsItem news = (NewsItem) i.getSerializableExtra("news");
        news_id = String.valueOf(news.getId());
        txttitle.setText(news.getTitle().toString());
        txtdate.setText(new SimpleDateFormat("dd/MM/yyy").format(news.getNewsDate()));
        txttext.setText(news.getText().toString());
        if(news.getBitmap()==null){
            new ImageDownloadTask(imgnews).execute(news);
        }else{
            imgnews.setImageBitmap(news.getBitmap());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_details_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        else if(id == R.id.comment){
            Intent i = new Intent(NewsDetailsActivity.this,CommentsActivity.class);
            i.putExtra("newsID",news_id);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
