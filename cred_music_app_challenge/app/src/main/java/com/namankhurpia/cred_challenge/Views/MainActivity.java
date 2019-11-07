package com.namankhurpia.cred_challenge.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.namankhurpia.cred_challenge.R;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    TextView head,content;
    ImageButton login,signup;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        head = (TextView)findViewById(R.id.head);
        content =(TextView)findViewById(R.id.content);
        login = (ImageButton)findViewById(R.id.login);
        signup = (ImageButton)findViewById(R.id.signup);
        cardView = (CardView)findViewById(R.id.cardview);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cardView.getVisibility()==View.VISIBLE)
                {
                    Intent i = new Intent(MainActivity.this,music.class);
                    startActivity(i);
                }

                ObjectAnimator animation = ObjectAnimator.ofFloat(head, "translationY", -400f);
                ObjectAnimator animation2 = ObjectAnimator.ofFloat(content, "translationY", -400f);
                animation.setDuration(500);
                animation2.setDuration(500);
                animation.start();
                animation2.start();

                cardView.setVisibility(View.VISIBLE);


            }
        });







    }
}
