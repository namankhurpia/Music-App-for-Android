package com.namankhurpia.cred_challenge.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.namankhurpia.cred_challenge.Handlers.OnSwipeTouchListener;
import com.namankhurpia.cred_challenge.R;
import com.namankhurpia.cred_challenge.Services.MusicDataService;
import com.namankhurpia.cred_challenge.Services.RetrofitInstance;
import com.namankhurpia.cred_challenge.models.Schema;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;

public class music_playing extends AppCompatActivity {

    TextView textView;
    List<Schema> data, musiclist;
    String position;

    String cur_song_name, cur_artist_name, cur_album_logo, cur_music_url;

    ImageView cover_img,loading;
    ImageButton play, pause, next, previous;
    TextView song_name, artist_name;

    SeekBar seekBar;
    TextView seekBarHint;

    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean wasPlaying = false;
    ProgressDialog progressDoalog;


    //try catch have been included in all

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_playing);
        String value = getIntent().getExtras().getString("url");
        position = getIntent().getExtras().getString("position");

        cover_img = (ImageView) findViewById(R.id.cover_img);
        play = (ImageButton) findViewById(R.id.playbtn);
        pause = (ImageButton) findViewById(R.id.pausebtn);
        next = (ImageButton) findViewById(R.id.next);
        previous = (ImageButton) findViewById(R.id.previous);
        song_name = (TextView) findViewById(R.id.songname);
        artist_name = (TextView) findViewById(R.id.artistname);
        loading = (ImageView)findViewById(R.id.loading);


        musiclist = fetchagain();

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.INVISIBLE);
                clearMediaPlayer();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    playCurrentsong();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        cover_img.setOnTouchListener(new OnSwipeTouchListener(music_playing.this)  {

            public void onSwipeRight() {
                //previous song
                clearMediaPlayer();
                Toast.makeText(music_playing.this, "previous song", Toast.LENGTH_SHORT).show();
                if(Integer.parseInt(position)>0)
                {
                    (position) = Integer.parseInt(position)-1+"";
                    fetchagain();
                }
                else
                {
                    Toast.makeText(music_playing.this, "Reached beginning of list", Toast.LENGTH_SHORT).show();
                }


            }
            public void onSwipeLeft() {
                //next song
                clearMediaPlayer();
                Toast.makeText(music_playing.this, "next song", Toast.LENGTH_SHORT).show();
                (position) = Integer.parseInt(position)+1+"";
                fetchagain();
            }

        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMediaPlayer();
                Toast.makeText(music_playing.this, "next song", Toast.LENGTH_SHORT).show();
                (position) = Integer.parseInt(position)+1+"";
                fetchagain();

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMediaPlayer();
                Toast.makeText(music_playing.this, "previous song", Toast.LENGTH_SHORT).show();
                if(Integer.parseInt(position)>0)
                {
                    (position) = Integer.parseInt(position)-1+"";
                    fetchagain();
                }
                else
                {
                    Toast.makeText(music_playing.this, "Reached beginning of list", Toast.LENGTH_SHORT).show();
                }
            }
        });


        seekBar = findViewById(R.id.seekbar);
        seekBarHint = findViewById(R.id.seekbarhint);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                seekBarHint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
                seekBarHint.setVisibility(View.VISIBLE);
                int x = (int) Math.ceil(progress / 1000f);

                if (x < 10)
                    seekBarHint.setText("0:0" + x);
                else
                    seekBarHint.setText("0:" + x);

                double percent = progress / (double) seekBar.getMax();
                int offset = seekBar.getThumbOffset();
                int seekWidth = seekBar.getWidth();
                int val = (int) Math.round(percent * (seekWidth - 2 * offset));
                int labelWidth = seekBarHint.getWidth();
                seekBarHint.setX(offset + seekBar.getX() + val
                        - Math.round(percent * offset)
                        - Math.round(percent * labelWidth / 2));

                if (progress > 0 && mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    clearMediaPlayer();

                    music_playing.this.seekBar.setProgress(0);
                }

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });


    }

    private void playCurrentsong() throws IOException {

        play.setVisibility(View.GONE);
        pause.setVisibility(View.VISIBLE);
        Glide
                .with(getApplicationContext())
                .load(R.drawable.giphy)
                .into(loading);
        loading.setVisibility(View.VISIBLE);

        playSong();


    }


    private void setnames_load_img() {
        Glide.with(getApplicationContext())
                .load(cur_album_logo)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.logo)
                        .fitCenter())
                .into(cover_img);

        song_name.setText(cur_song_name);
        artist_name.setText(cur_artist_name);


    }


    //fetching the music again using retrofit 2
    private List<Schema> fetchagain() {


        progressDoalog = new ProgressDialog(music_playing.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("loading....");
        progressDoalog.setTitle("Fetching songs");
        progressDoalog.setCancelable(false);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        final MusicDataService musicDataService = RetrofitInstance.getService();
        Call<List<Schema>> call = musicDataService.GetAllMusic();
        Log.i("code ran", "/// /////////");
        call.enqueue(new Callback<List<Schema>>() {
            @Override
            public void onResponse(Call<List<Schema>> call, Response<List<Schema>> response) {

                try {


                    if (response.isSuccessful()) {
                        Log.i("on music playing", "success");
                    } else {
                        Log.i("on music playing", "failed");
                    }
                    data = response.body();

                    for (Schema m : data) {
                        Log.i("on music playing", "***************" + m.getSong() + m.getArtists());
                    }

                    //getting the particular clicked music item
                    Schema naman = response.body().get(Integer.parseInt(position));

                    cur_song_name = naman.getSong();
                    cur_artist_name = naman.getArtists();
                    cur_album_logo = naman.getCoverImage();
                    cur_music_url = naman.getUrl();

                    progressDoalog.dismiss();


                    setnames_load_img();


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "please try again", Toast.LENGTH_SHORT).show();
                    Log.d("onResponse", "There is an error" + e.getMessage());
                    e.printStackTrace();

                }

            }

            //display all exceptions in logs
            @Override
            public void onFailure(Call<List<Schema>> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Kindly check your connection",Toast.LENGTH_LONG).show();

                Log.d("%%%%", "EROR");
                Log.d("%%", t.getCause() + "" + t.getMessage() + "-hula-" + t.getStackTrace());
                progressDoalog.dismiss();

            }
        });

        return data;

    }

    //running the async task
    public void playSong() throws IOException {

        new songthread().execute();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMediaPlayer();
    }


    //function that pauses any media player when clicked on pause button
    private void clearMediaPlayer() {
        try {
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
            mediaPlayer.stop();

            loading.setVisibility(View.INVISIBLE);
        }
        catch (Exception e)
        {

        }

    }



    //an asynctask used for playing music so as to optimize the code
    class songthread extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {


                mediaPlayer.setDataSource(cur_music_url);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Log.d("^^^^","start called");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        //post execute allows us to make changes once the asynctask is finished
        @Override
        protected void onPostExecute(String result) {
            try {
                if (mediaPlayer.isPlaying()) {
                    loading.setVisibility(View.INVISIBLE);
                }

                Log.d("&&&", "dismissed now");
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"Internet issue",Toast.LENGTH_SHORT).show();
                    }
                }
    }



}

