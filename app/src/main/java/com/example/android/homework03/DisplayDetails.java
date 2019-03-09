package com.example.android.homework03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DisplayDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);

        TextView resultGenre = findViewById(R.id.resultGenre);
        TextView resultArtist = findViewById(R.id.resultArtist);
        TextView resultAlbum = findViewById(R.id.resultAlbum);
        TextView resultTrackPrice = findViewById(R.id.resultTrackPrice);
        TextView resultAlbumPrice = findViewById(R.id.resultAlbumPrice);
        TextView resultTrack = findViewById(R.id.resultTrack);
        ImageView imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.finishButton);

        if(getIntent() != null && getIntent().getExtras() != null){
            Music music = (Music) getIntent().getExtras().getSerializable(MainActivity.MUSIC_KEY);
            resultGenre.setText(music.getPrimaryGenreName());
            resultArtist.setText(music.getArtistName());
            resultAlbum.setText(music.getCollectionName());
            resultTrackPrice.setText(getString(R.string.money) + String.valueOf(music.getTrackPrice()));
            resultAlbumPrice.setText(getString(R.string.money) + String.valueOf(music.getCollectionPrice()));
            resultTrack.setText(music.getTrackName());
            Picasso.get().load(music.getImage()).into(imageView);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
