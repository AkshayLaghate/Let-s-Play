package com.indcoders.letsplayvideos;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends ActionBarActivity {


    @Bind(R.id.grid_view)
    StaggeredGridView grid;
    ArrayList<String> ids,names,descs;
    Bitmap[] imgs;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        ids = new ArrayList<>();
        names  =  new ArrayList<>();
        descs = new ArrayList<>();
        ids = getIds();
        pd = new ProgressDialog(this);
        imgs = new Bitmap[ids.size()];
        new getChannelInfo().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public ArrayList<String> getIds(){
        ArrayList<String> ids = new ArrayList<>();
        ids.add("UCYzPXprvl5Y-Sf0g4vX-m6g");
        ids.add("UC-lHJZR3Gqxm24_Vd_AJ5Yw");
        ids.add("UC7_YxT-KID8kRbqZo7MyscQ");
        ids.add("UCJ2ZDzMRgSrxmwphstrm8Ww");
        ids.add("UCkxctb0jr8vwa4Do6c6su0Q");
        ids.add("UCKqH_9mk1waLgBiL2vT5b9g");
        ids.add("UCj5i58mCkAREDqFWlhaQbOw");
        ids.add("UCpqXJOEqGS-TCnazcHCo0rA");
        ids.add("UCshoKvlZGZ20rVgazZp5vnQ");
        ids.add("UCq54nlcoX-0pLcN5RhxHyug");
        ids.add("UCzH3iADRIq1IJlIXjfNgTpA");

        return ids;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class getChannelInfo extends AsyncTask<Void,Void,Void> {

        int index = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading channels ...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            for (String s : ids) {
                String url = "https://www.googleapis.com/youtube/v3/channels?part=snippet&id=" + s + "&key=AIzaSyCLColf59PzgJq02TJuud3NW3kNqfsZzQk";
                String jsonStr = null;

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = null;

                try {
                    response = client.newCall(request).execute();
                    jsonStr = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Response", "Error: " + e);
                }
                Log.e("Response: ", "> " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray dataArray = jsonObj.getJSONArray("items");

                        JSONObject snippetData = dataArray.getJSONObject(0).getJSONObject("snippet");

                        Log.e("Channel name : ", snippetData.getString("title"));
                        names.add(snippetData.getString("title"));

                        JSONObject thumbData = snippetData.getJSONObject("thumbnails");
                        JSONObject deafaultThumbData = thumbData.getJSONObject("default");

                        imgs[index] = Picasso.with(getApplicationContext()).load(deafaultThumbData.getString("url")).get();

                        index++;
                    } catch (Exception e) {

                    }
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            grid.setAdapter(new GridAdapter());
            grid.invalidateViews();
        }
    }

        public class GridAdapter extends BaseAdapter {

            // references to our images


            public GridAdapter() {

            }

            public int getCount() {
                return names.size();
            }

            public Object getItem(int position) {
                return null;
            }

            public long getItemId(int position) {
                return 0;
            }


            // create a new ImageView for each item referenced by the Adapter
            public View getView(final int position, View convertView, ViewGroup parent) {

                convertView = null;

                if (convertView == null) {
                    // if it's not recycled, initialize some attributes


                    LayoutInflater inflater = (LayoutInflater) HomeActivity.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.cardlib_card, parent, false);

                }



                ImageView ivThumb = (ImageView) convertView.findViewById(R.id.ivThumb);
                TextView tvName = (TextView) convertView.findViewById(R.id.tvTitle);


                ivThumb.setImageBitmap(imgs[position]);
                tvName.setText(names.get(position));


                return convertView;

            }

        }

    }

