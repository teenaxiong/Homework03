package com.example.android.homework03;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class SortingAsyncTask extends AsyncTask<ArrayList<Music>, Void, ArrayList<Music>> {

    ResultAdapter adapter;
    Switch aSwitch;
    public SortingAsyncTask(ResultAdapter adapter, Switch aSwitch) {
        this.adapter = adapter;
        this.aSwitch = aSwitch;
    }

    public SortingAsyncTask(Switch aSwitch) {
        this.aSwitch = aSwitch;
    }

    ArrayList<Music> musicArray;
    @Override
    protected ArrayList<Music> doInBackground(ArrayList<Music>... arrayLists) {
        musicArray = arrayLists[0];
        if(aSwitch.isChecked()) {
            Collections.sort(musicArray, new Comparator<Music>() {
                @Override
                public int compare(Music music, Music t1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                    Date start = null;
                    Date end = null;
                    try {
                        start = sdf.parse(music.getReleaseDate());
                        end = sdf.parse(t1.getReleaseDate());
                        return start.compareTo(end);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
        }else{
            Collections.sort(musicArray, new Comparator<Music>() {
                @Override
                public int compare(Music music, Music t1) {
                    if (music.getTrackPrice() < t1.getTrackPrice()) return -1;
                    if (music.getTrackPrice() > t1.getTrackPrice()) return 1;
                    return 0;
                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Music> music) {
        if(adapter != null){
            adapter.NotifyDataSetChanged();
        }

        super.onPostExecute(music);
    }
}
