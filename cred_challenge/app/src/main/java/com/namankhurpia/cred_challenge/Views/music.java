package com.namankhurpia.cred_challenge.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.namankhurpia.cred_challenge.Adapters.MusicRecyclerViewAdapter;
import com.namankhurpia.cred_challenge.Handlers.checkForInternet;
import com.namankhurpia.cred_challenge.R;
import com.namankhurpia.cred_challenge.Services.MusicDataService;
import com.namankhurpia.cred_challenge.Services.RetrofitInstance;
import com.namankhurpia.cred_challenge.models.Schema;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class music extends AppCompatActivity {

    List<Schema> data;
    MusicRecyclerViewAdapter musicadapter;
    RecyclerView recyclerView;
    LinearLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_music);


        relativeLayout=(LinearLayout) findViewById(R.id.relativelayout);

        if (checkForInternet.getInstance(getApplicationContext()).isOnline())
        {
            getpapers();

        }
        else
        {
            RelativeLayout relativeLayout=new RelativeLayout(this);
            ImageView imageView=new ImageView(this);
            imageView.setImageResource(R.mipmap.noaccess);

            imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)imageView.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            relativeLayout.addView(imageView);


            setContentView(relativeLayout);
            Toast.makeText(getApplicationContext(), "No internet access", Toast.LENGTH_SHORT).show();
        }

        //recyclerView.setAdapter(musicadapter);

    }



    private Object getpapers() {

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(music.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("loading....");
        progressDoalog.setTitle("Fetching songs");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog.setCancelable(false);

        progressDoalog.setCanceledOnTouchOutside(false);
        // show it
        progressDoalog.show();

        final MusicDataService musicDataService= RetrofitInstance.getService();
        Call<List<Schema>> call = musicDataService.GetAllMusic();
        Log.i("code ran","/// /////////");
        call.enqueue(new Callback<List<Schema>>() {
            @Override
            public void onResponse(Call<List<Schema>> call, Response<List<Schema>> response) {

                try{


                    if(response.isSuccessful())
                    {
                        Log.i("on response","success");
                    }
                    else
                    {
                        Log.i("on response","failed");
                    }
                    data=response.body();

                    for(Schema m:data)
                    {
                        Log.i("on response","***************"+m.getSong()+m.getArtists());
                    }

                    viewData();
                    progressDoalog.dismiss();

                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Kindly check your connection/permissions",Toast.LENGTH_LONG).show();
                    Log.d("onResponse", "There is an error"+e.getMessage());
                    e.printStackTrace();

                    progressDoalog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<List<Schema>> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Kindly check your connection/permissions",Toast.LENGTH_LONG).show();
                Log.d("%%%%","EROR");
                Log.d("%%",t.getCause()+""+t.getMessage()+"-hula-"+t.getStackTrace());
            }
        });

        return  data;

    }



    private void viewData() {
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);

        //changed
        musicadapter = new MusicRecyclerViewAdapter(getApplicationContext(),data);
        //ended with get app context

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(music.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(musicadapter);


    }


}
