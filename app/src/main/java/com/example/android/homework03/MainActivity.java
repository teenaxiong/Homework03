/*
Teena Xiong
Assignment Homework 03

 */

package com.example.android.homework03;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchAsync.MusicInterface {
    TextView limit;
    TextView keywords;
    String keywordString;
    SeekBar seekBar;
    ArrayList<Music> resultArray;
    ProgressBar progressBar;
    ListView listView;
    ResultAdapter adapter;
    TextView limitLabel;
    Button reset;
    Button search;
    TextView searchBar;
    TextView sortLabel;
    TextView priceLabel;
    TextView dateLabel;
    Switch aSwitch;
    static String MUSIC_KEY = "MUSIC";
    TextView resultLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        limit = findViewById(R.id.limitValue);
        progressBar = findViewById(R.id.progressBar);
        seekBar = findViewById(R.id.seekBar);
        limit.setText(String.valueOf(seekBar.getProgress()+10));
        listView = findViewById(R.id.listView);
         limitLabel = findViewById(R.id.limitTextView);
         reset = findViewById(R.id.resetButton);
         search = findViewById(R.id.searchButton);
         searchBar = findViewById(R.id.searchBar);
        sortLabel = findViewById(R.id.sortTextView);
        priceLabel = findViewById(R.id.priceTextView);
        dateLabel = findViewById(R.id.dateTextView);
        aSwitch = findViewById(R.id.switch1);
        resultArray = new ArrayList<>();
        resultLabel = findViewById(R.id.resultLabel);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            i = i + 10;
            limit.setText(String.valueOf(i));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keywords = findViewById(R.id.searchBar);
                keywordString = keywords.getText().toString();


                if(keywordString.isEmpty() || keywordString.equals("")){
                    keywords.setError("Please enter a search word");
                }else if(isConnected()){
                    keywordString.trim();
                    seekBar.setVisibility(View.INVISIBLE);
                    limitLabel.setVisibility(View.INVISIBLE);
                    limit.setVisibility(View.INVISIBLE);
                    search.setVisibility(View.INVISIBLE);
                    reset.setVisibility(View.INVISIBLE);
                    searchBar.setVisibility(View.INVISIBLE);
                    sortLabel.setVisibility(View.INVISIBLE);
                    priceLabel.setVisibility(View.INVISIBLE);
                    dateLabel.setVisibility(View.INVISIBLE);
                    aSwitch.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    resultLabel.setVisibility(View.INVISIBLE);
                    String newKeyword = keywordString.replace(" ", "+");
                    new SearchAsync(progressBar, aSwitch, MainActivity.this).execute("https://itunes.apple.com/search?term=" + newKeyword + "&limit="+limit.getText().toString());
                }else {
                    Toast.makeText(MainActivity.this, "Internet is Not Connected", Toast.LENGTH_SHORT).show();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DisplayDetails.class);
                intent.putExtra(MUSIC_KEY, resultArray.get(i));
                startActivity(intent);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aSwitch.setChecked(true);
                searchBar.setText(null);
                seekBar.setProgress(0);
                limit.setText(String.valueOf(seekBar.getProgress()+10));
                resultArray.clear();
                if(adapter != null){
                adapter.NotifyDataSetChanged();}

            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!resultArray.isEmpty()) {
                    SortingAsyncTask sortingAsyncTask = new SortingAsyncTask(adapter, aSwitch);
                    sortingAsyncTask.execute(resultArray);
                }
            }
        });




    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null || !networkInfo.isConnected()) {
            if ((networkInfo.getType() != ConnectivityManager.TYPE_WIFI) && (networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void handleMusicResult(ArrayList<Music> music) {
        listView.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        limitLabel.setVisibility(View.VISIBLE);
        limit.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        reset.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.VISIBLE);
        sortLabel.setVisibility(View.VISIBLE);
        priceLabel.setVisibility(View.VISIBLE);
        dateLabel.setVisibility(View.VISIBLE);
        aSwitch.setVisibility(View.VISIBLE);
        resultLabel.setVisibility(View.VISIBLE);
        this.resultArray = music;
        Log.d("h" , " " + music.size());
        if(music.size() == 0){
            Toast.makeText(MainActivity.this, "Sorry, no result. Please perform another search.", Toast.LENGTH_SHORT).show();
        }else{
        adapter = new ResultAdapter(this, R.layout.resultinflater, music);
        listView.setAdapter(adapter);}

    }
}
