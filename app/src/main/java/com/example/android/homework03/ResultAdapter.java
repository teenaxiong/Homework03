package com.example.android.homework03;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ResultAdapter extends ArrayAdapter<Music> {

    Context context;

    public ResultAdapter( Context context, int resource, ArrayList<Music> objects) {
        super(context, resource, objects);
        this.context = context;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Music music = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.resultinflater, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.trackInflater = convertView.findViewById(R.id.trackInflater);
            viewHolder.artistInflater = convertView.findViewById(R.id.artistInflater);
            viewHolder.dateInflater = convertView.findViewById(R.id.dateInflater);
            viewHolder.priceInflater = convertView.findViewById(R.id.priceInflater);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.trackInflater.setText((Html.fromHtml(context.getString(R.string.track)+ music.getTrackName())));
        viewHolder.priceInflater.setText((Html.fromHtml(context.getString(R.string.price)+ String.valueOf(music.trackPrice))));
        viewHolder.dateInflater.setText((Html.fromHtml(context.getString(R.string.date)+ music.getReleaseDate())));
        viewHolder.artistInflater.setText((Html.fromHtml(context.getString(R.string.artist)+ music.getArtistName())));

        return convertView;
    }

    public void NotifyDataSetChanged() {
        notifyDataSetChanged();
    }

    private static class ViewHolder{
        TextView trackInflater, dateInflater, priceInflater, artistInflater;
    }
}
