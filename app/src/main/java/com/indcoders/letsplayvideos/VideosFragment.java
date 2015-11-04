package com.indcoders.letsplayvideos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "para4";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;

    StaggeredGridView grid;

    ArrayList<String> names;
    Bitmap[] imgs;

    ProgressDialog pd;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *               @param param3 Parameter 3.
     *                             @param param4 Parameter 4.
     * @return A new instance of fragment VideosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideosFragment newInstance(String param1, String param2,String param3,String param4) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);

        fragment.setArguments(args);
        return fragment;
    }

    public VideosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_videos, container, false);
        grid = (StaggeredGridView) v.findViewById(R.id.grid_view);


        names = new ArrayList<>();
        pd = new ProgressDialog(getActivity());
        switch (mParam1){
            case "Videos":
                new LoadRecents().execute(mParam3);
                break;
            case "Playlist":
                break;
        }
        return v;
    }

    public class LoadRecents extends AsyncTask<String,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading channels ...");
            pd.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+params[0]+"&maxResults=10&key=AIzaSyCLColf59PzgJq02TJuud3NW3kNqfsZzQk";
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

                        imgs = new Bitmap[dataArray.length()];
                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject snippetData = dataArray.getJSONObject(i).getJSONObject("snippet");

                        Log.e("Channel name : ", snippetData.getString("title"));
                        names.add(snippetData.getString("title"));

                        JSONObject thumbData = snippetData.getJSONObject("thumbnails");
                        JSONObject deafaultThumbData = thumbData.getJSONObject("default");

                        imgs[i] = Picasso.with(getActivity()).load(deafaultThumbData.getString("url")).get();

                    }
                }catch (Exception e){

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


                LayoutInflater inflater = (LayoutInflater) getActivity()
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
