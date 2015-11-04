package com.indcoders.letsplayvideos;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends ActionBarActivity {


    @Bind(R.id.grid_view)
    StaggeredGridView grid;
    ArrayList<String> ids,names,descs,playlist;
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
        playlist = new ArrayList<>();
        ids = getIds();
        pd = new ProgressDialog(this);
        imgs = new Bitmap[ids.size()];
        new getChannelInfo().execute();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bag = new Bundle();
                bag.putString("id",ids.get(position));
                bag.putString("playlist",playlist.get(position));
                bag.putString("name",names.get(position));
                Intent i = new Intent(HomeActivity.this, ParentActivity.class);
                i.putExtras(bag);
                startActivity(i);
            }
        } );

        Toast.makeText(this,"IP Address : "+getIPAddress(true),Toast.LENGTH_SHORT).show();

    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
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
        ids.add("UCGmnsW623G1r-Chmo5RB4Yw");
        ids.add("UCH-_hzb2ILSCo9ftVSnrCIQ");
        ids.add("UCClNRixXlagwAd--5MwJKCw");
        ids.add("UCcWl6q7rcJ9WoF_Kwmxg23A");
        ids.add("UC-kOXc3gBwksVfmndSEz7jg");
        ids.add("UC9CuvdOVfMPvKCiwdGKL3cQ");

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
                String url = "https://www.googleapis.com/youtube/v3/channels?part=snippet,contentDetails&id=" + s + "&key=AIzaSyCLColf59PzgJq02TJuud3NW3kNqfsZzQk";
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

                        JSONObject contentData = dataArray.getJSONObject(0).getJSONObject("contentDetails");
                        JSONObject playlistData = contentData.getJSONObject("relatedPlaylists");
                        playlist.add(playlistData.getString("uploads"));

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
            new GetPackage().execute();
        }
    }

    public class GetPackage extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            String url = "https://admin.appnext.com/offerWallApi.aspx?id=4faf7fda-89de-4731-9134-658cff3320d8&cnt=1&cat=Action&type=json&ip="+getIPAddress(true)+"&pbk=app-package";
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
                    JSONArray apps = jsonObj.getJSONArray("apps");

                    final String name = apps.getJSONObject(0).getString("title");
                    final String packageName = apps.getJSONObject(0).getString("androidPackage");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HomeActivity.this,name+packageName,Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {

                }
            }

            return null;
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

