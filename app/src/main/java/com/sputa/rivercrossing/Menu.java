package com.sputa.rivercrossing;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Menu extends AppCompatActivity {
    int screenWidth = 0;
    int screenHeight = 0;
    String
            font_name = "";
    Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        font_name = "fonts/BYekan.ttf";
        tf = Typeface.createFromAsset(getAssets(),font_name );
        TextView lbl_level1 = findViewById(R.id.lbl_level1);


        lbl_level1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level1.setTypeface(tf);

        TextView lbl_level2 = findViewById(R.id.lbl_level2);
        lbl_level2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level2.setTypeface(tf);

        TextView lbl_level3 = findViewById(R.id.lbl_level3);
        lbl_level3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level3.setTypeface(tf);

        TextView lbl_level4 = findViewById(R.id.lbl_level4);
        lbl_level4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level4.setTypeface(tf);

        TextView lbl_level5 = findViewById(R.id.lbl_level5);
        lbl_level5.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level5.setTypeface(tf);
        TextView lbl_level6 = findViewById(R.id.lbl_level6);
        lbl_level6.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level6.setTypeface(tf);
        TextView lbl_level7 = findViewById(R.id.lbl_level7);
        lbl_level7.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level7.setTypeface(tf);
        TextView lbl_level8 = findViewById(R.id.lbl_level8);
        lbl_level8.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level8.setTypeface(tf);


        LinearLayout.LayoutParams lp_img_move_button = new LinearLayout.LayoutParams((int) (screenWidth * 0.34), (int) (screenHeight * 0.56));

        RelativeLayout lay_level1=findViewById(R.id.lay_level1);
        lay_level1.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level2=findViewById(R.id.lay_level2);
        lay_level2.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level3=findViewById(R.id.lay_level3);
        lay_level3.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level4=findViewById(R.id.lay_level4);
        lay_level4.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level5=findViewById(R.id.lay_level5);
        lay_level5.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level6=findViewById(R.id.lay_level6);
        lay_level6.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level7=findViewById(R.id.lay_level7);
        lay_level7.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level8=findViewById(R.id.lay_level8);
        lay_level8.setLayoutParams(lp_img_move_button);




        LinearLayout.LayoutParams lp_img_level1 = new LinearLayout.LayoutParams((int) (screenWidth * 0.2), (int) (screenHeight * 0.25));
        ImageView img_level1 = findViewById(R.id.img_level1);
        img_level1.setLayoutParams(lp_img_level1);
        ImageView img_level2 = findViewById(R.id.img_level2);
        img_level2.setLayoutParams(lp_img_level1);
        ImageView img_level3 = findViewById(R.id.img_level3);
        img_level3.setLayoutParams(lp_img_level1);
        ImageView img_level4 = findViewById(R.id.img_level4);
        img_level4.setLayoutParams(lp_img_level1);


        LinearLayout.LayoutParams lp_img_level2 = new LinearLayout.LayoutParams((int) (screenWidth * 0.15), (int) (screenHeight * 0.25));
        ImageView img_level5 = findViewById(R.id.img_level5);
        img_level5.setLayoutParams(lp_img_level2);
        ImageView img_level6 = findViewById(R.id.img_level6);
        img_level6.setLayoutParams(lp_img_level1);
        ImageView img_level7 = findViewById(R.id.img_level7);
        img_level7.setLayoutParams(lp_img_level1);
        ImageView img_level8 = findViewById(R.id.img_level8);
        img_level8.setLayoutParams(lp_img_level1);





        LinearLayout.LayoutParams lp_star11 = new LinearLayout.LayoutParams((int) (screenWidth * 0.08), (int) (screenHeight * 0.08));

        ImageView star11 = findViewById(R.id.star11);
        star11.setLayoutParams(lp_star11);
        ImageView star13 = findViewById(R.id.star12);
        star13.setLayoutParams(lp_star11);
        ImageView star12 = findViewById(R.id.star13);
        star12.setLayoutParams(lp_star11);

        ImageView star21 = findViewById(R.id.star21);
        star21.setLayoutParams(lp_star11);
        ImageView star23 = findViewById(R.id.star22);
        star23.setLayoutParams(lp_star11);
        ImageView star22 = findViewById(R.id.star23);
        star22.setLayoutParams(lp_star11);

        ImageView star31 = findViewById(R.id.star31);
        star31.setLayoutParams(lp_star11);
        ImageView star33 = findViewById(R.id.star32);
        star33.setLayoutParams(lp_star11);
        ImageView star32 = findViewById(R.id.star33);
        star32.setLayoutParams(lp_star11);


        ImageView star41 = findViewById(R.id.star41);
        star41.setLayoutParams(lp_star11);
        ImageView star43 = findViewById(R.id.star42);
        star43.setLayoutParams(lp_star11);
        ImageView star42 = findViewById(R.id.star43);
        star42.setLayoutParams(lp_star11);

        ImageView star51 = findViewById(R.id.star51);
        star51.setLayoutParams(lp_star11);
        ImageView star53 = findViewById(R.id.star52);
        star53.setLayoutParams(lp_star11);
        ImageView star52 = findViewById(R.id.star53);
        star52.setLayoutParams(lp_star11);

        ImageView star61 = findViewById(R.id.star61);
        star61.setLayoutParams(lp_star11);
        ImageView star63 = findViewById(R.id.star62);
        star63.setLayoutParams(lp_star11);
        ImageView star62 = findViewById(R.id.star63);
        star62.setLayoutParams(lp_star11);

        ImageView star71 = findViewById(R.id.star71);
        star71.setLayoutParams(lp_star11);
        ImageView star73 = findViewById(R.id.star72);
        star73.setLayoutParams(lp_star11);
        ImageView star72 = findViewById(R.id.star73);
        star72.setLayoutParams(lp_star11);

        ImageView star81 = findViewById(R.id.star81);
        star81.setLayoutParams(lp_star11);
        ImageView star83 = findViewById(R.id.star82);
        star83.setLayoutParams(lp_star11);
        ImageView star82 = findViewById(R.id.star83);
        star82.setLayoutParams(lp_star11);












    }
}
