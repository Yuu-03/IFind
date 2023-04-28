package com.example.ifind;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    //move to main screen after splashscreen
    private static int SPLASH_SCREEN = 6000;

    //Variables for splashscreen animation
    Animation topAnim, botAnim;

    ImageView logo;
    TextView tagline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Hide status bar and action bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.splash_screen);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //hooks the animation to logo and tagline
        logo =  findViewById(R.id.cnscLogo);
        tagline = findViewById(R.id.tagline);

        //assign the animations
        logo.setAnimation(topAnim);
        tagline.setAnimation(botAnim);


        //call the next main activity inside the delay method
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);

                //transition with animation,define
                Pair[] pairs=new Pair[2];
                pairs[0] = new Pair<View,String>(logo,"imageLogo");
                pairs[1] = new Pair<View,String>(tagline,"tagLine");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this, pairs);
                startActivity(intent,options.toBundle());

            }
        },SPLASH_SCREEN);

    }
}