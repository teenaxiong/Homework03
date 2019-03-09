package com.example.android.homework03;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchAsync extends AsyncTask<String, Integer, ArrayList<Music>> {
    ProgressBar progressBar;
    MusicInterface musicInterface;
    Switch aSwitch;

    public SearchAsync(ProgressBar progressBar, Switch aSwitch, MusicInterface musicInterface) {
        this.progressBar = progressBar;
        this.musicInterface = musicInterface;
        this.aSwitch = aSwitch;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected ArrayList<Music> doInBackground(String... strings) {
        HttpURLConnection connection = null;
        ArrayList<Music> result = new ArrayList<>();
        String urlString = null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                urlString = IOUtils.toString(connection.getInputStream(), "UTF8");
                JSONObject root = new JSONObject(urlString);
                JSONArray rootArray = root.getJSONArray("results");
                for(int x = 0; x<rootArray.length(); x++){
                    JSONObject resultObject = rootArray.getJSONObject(x);

                    Music music = new Music();
                    if(resultObject.has("trackName")){
                        music.setTrackName(resultObject.getString("trackName"));
                    }else music.setTrackName("Unknown Track Name");

                    if(resultObject.has("primaryGenreName")){
                        music.setPrimaryGenreName(resultObject.getString("primaryGenreName"));
                    }else music.setPrimaryGenreName("Unknown Genre");

                    if(resultObject.has("artistName")){
                        music.setArtistName(resultObject.getString("artistName"));
                    }else music.setArtistName("Unknown Artist");


                    if(resultObject.has("collectionName")){
                        music.setCollectionName(resultObject.getString("collectionName"));
                    }else  music.setCollectionName("Unknown Name"); ;

                    if(resultObject.has("releaseDate")){
                        String string = resultObject.getString("releaseDate");
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
                        Date date = null;
                        try {
                            date = inputFormat.parse(string);
                            String formattedDate = outputFormat.format(date);
                            music.setReleaseDate(String.valueOf(formattedDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }else  music.setReleaseDate(null);


                    if(resultObject.has("trackPrice")){
                         music.setTrackPrice(resultObject.getDouble("trackPrice"));
                    }else   music.setTrackPrice(0);

                    if(resultObject.has("collectionPrice")){
                       music.setCollectionPrice(resultObject.getDouble("collectionPrice"));
                    }else  music.setCollectionPrice(0);

                    if(resultObject.has("artworkUrl100")){
                        music.setImage(resultObject.getString("artworkUrl100"));
                    }else  music.setImage(null);

                    result.add(music);
                    publishProgress(x);
                }
               new SortingAsyncTask(aSwitch).execute(result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Music> music) {
        progressBar.setVisibility(View.INVISIBLE);
        musicInterface.handleMusicResult(music);
    }

    public static interface MusicInterface{
        public void handleMusicResult(ArrayList<Music> music);
    }
}
