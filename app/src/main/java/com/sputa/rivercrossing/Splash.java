package com.sputa.rivercrossing;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;


        ImageView img_coin = findViewById(R.id.img_splash);
        ConstraintLayout.LayoutParams lp_img_coin = (ConstraintLayout.LayoutParams) img_coin.getLayoutParams();
        lp_img_coin.width = (int) (screenWidth * 0.5);
        lp_img_coin.height = (int) (screenHeight * 0.7);
        img_coin.setLayoutParams(lp_img_coin);


        Thread timer=new Thread()
        {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally
                {
                    Intent i=new Intent(Splash.this,Menu.class);
                    finish();
                    startActivity(i);
                }
            }
        };
        timer.start();

    }
}
