package com.example.berfinkeleshomework3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    ProgressDialog prgDialog;
    RecyclerView newsRecView;
    List<NewsItem> data;
    NewsAdapter adp;
    Spinner spcategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spcategories = findViewById(R.id.spcategory);
        data = new ArrayList<>();
        newsRecView = findViewById(R.id.newsrec);
        adp = new NewsAdapter(data, this, new NewsAdapter.NewsItemClickListener() {
            @Override
            public void newItemClicked(NewsItem selectedNewsItem) {
                Intent i = new Intent(MainActivity.this,NewsDetailsActivity.class);
                i.putExtra("news",selectedNewsItem);
                startActivity(i);
                //Toast.makeText(MainActivity.this,selectedNewsItem.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
        newsRecView.setLayoutManager(new LinearLayoutManager(this));
        newsRecView.setAdapter(adp);

        spcategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = spcategories.getSelectedItem().toString();
                if(selectedCategory.equals("Politics")){
                    NewsTask tsk = new NewsTask();
                    tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/6");
                }
                else if(selectedCategory.equals("Sports")){
                    NewsTask tsk = new NewsTask();
                    tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/5");

                }else if(selectedCategory.equals("Economics")){
                    NewsTask tsk = new NewsTask();
                    tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/4");

                }else {
                    NewsTask tsk = new NewsTask();
                    tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                NewsTask tsk = new NewsTask();
                tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
            }
        });


    }




    class NewsTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(MainActivity.this);
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
                        long date = current.getLong("date");
                        Date objDate = new Date(date);
                        NewsItem item = new NewsItem(current.getInt("id"),
                                current.getString("title"),
                                current.getString("text"),
                                current.getString("image"),
                                objDate);
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


}
