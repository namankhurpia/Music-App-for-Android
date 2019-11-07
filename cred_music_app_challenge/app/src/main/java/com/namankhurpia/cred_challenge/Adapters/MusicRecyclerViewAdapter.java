package com.namankhurpia.cred_challenge.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.namankhurpia.cred_challenge.R;
import com.namankhurpia.cred_challenge.Views.Payment_subscription;
import com.namankhurpia.cred_challenge.Views.music_playing;
import com.namankhurpia.cred_challenge.models.Schema;

import java.util.List;




public class MusicRecyclerViewAdapter extends RecyclerView.Adapter<MusicRecyclerViewAdapter.adapterviewholder>{
    private List<Schema> musiclist;
    public Context context;
    String url_music , url_album_logo;

    public MusicRecyclerViewAdapter(Context applicationContext, List<Schema> data) {
        this.context = applicationContext;
        this.musiclist = data;
    }


    @NonNull
    @Override
    public adapterviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_recycler_view, parent, false);

        return new adapterviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final adapterviewholder holder, final int position) {
        holder.SongName.setText(musiclist.get(position).getSong());
        holder.ArtistName.setText(musiclist.get(position).getArtists());

        url_music = musiclist.get(position).getUrl();
        url_album_logo = musiclist.get(position).getCoverImage();


        Glide.with(context)
                .load(url_album_logo)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.logo)
                        .fitCenter())
                .into(holder.imageView);



    }



    @Override
    public int getItemCount() {
        return musiclist.size();
    }


    //child class
    class adapterviewholder extends RecyclerView.ViewHolder {
        TextView SongName;
        TextView ArtistName;
        ImageView imageView;
        RelativeLayout item_recyclerview;
        ImageView download_btn;



        public adapterviewholder(final View itemview) {

            super(itemview);
             SongName = itemview.findViewById(R.id.songname);
             ArtistName = itemview.findViewById(R.id.artistname);
             imageView = itemview.findViewById(R.id.album_logo);
             item_recyclerview = itemview.findViewById(R.id.item_recyclerview);
             download_btn = itemview.findViewById(R.id.download_btn);


             item_recyclerview.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {


                     Intent i=new Intent(context, music_playing.class);
                     String finalurl=url_music;
                     Log.i("TAG","$$$"+ finalurl);
                     //Toast.makeText(context, finalurl, Toast.LENGTH_SHORT).show();
                     i.putExtra("url", finalurl);


                     i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                     int pos = getAdapterPosition();
                     if(pos!=RecyclerView.NO_POSITION)
                     {
                         Log.d("TAGGG",(pos)+"");
                         i.putExtra("position",pos+"");
                     }
                     context.startActivity(i);

                 }
             });

             download_btn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     Intent i = new Intent(context, Payment_subscription.class);
                     i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     context.startActivity(i);

                 }
             });


        }
    }
}











