package com.indcoders.letsplayvideos;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.etsy.android.grid.StaggeredGridView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends ActionBarActivity {


    @Bind(R.id.grid_view)
    StaggeredGridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
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
        ids.add("UCxubOASK0482qC5psq89MsQ");
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

    public class getChannelInfo extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {



            String url = "https://www.googleapis.com/youtube/v3/channels?part=auditDetails&id=UC-lHJZR3Gqxm24_Vd_AJ5Yw&key=AIzaSyCLColf59PzgJq02TJuud3NW3kNqfsZzQk";
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


            return null;
        }
    }
}
