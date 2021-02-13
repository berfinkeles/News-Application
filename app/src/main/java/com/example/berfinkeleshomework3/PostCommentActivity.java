package com.example.berfinkeleshomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PostCommentActivity extends AppCompatActivity {

    EditText txtname;
    EditText txttext;
    String newsid;
    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtname = findViewById(R.id.nameInput);
        txttext = findViewById(R.id.textInput);
        Intent i = getIntent();
        newsid = i.getStringExtra("newsID");
    }


    public void taskCallClicked(View v){

        JsonTask tsk = new JsonTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment",txtname.getText().toString(),
                txttext.getText().toString(),newsid);
        this.finish();
    }

    class JsonTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(PostCommentActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder strBuilder = new StringBuilder();
            String urlStr = strings[0];
            String name = strings[1];
            String text = strings[2];
            String newsid = strings[3];

            JSONObject obj = new JSONObject();
            try {
                obj.put("name",name);
                obj.put("text",text);
                obj.put("news_id",newsid);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json");
                conn.connect();

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(obj.toString());

                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line="";
                    while((line= reader.readLine())!=null){
                        strBuilder.append(line);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return strBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            prgDialog.dismiss();
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

        return super.onOptionsItemSelected(item);
    }



}
