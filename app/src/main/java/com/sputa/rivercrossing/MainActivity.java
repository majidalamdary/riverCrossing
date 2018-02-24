package com.sputa.rivercrossing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int screenWidth = 0;
    int screenHeight = 0;
    String
            boat_side = "down";
    int
            boat_passenger_count = 0;
    ImageView[] boat_passengers = new ImageView[4];
    int[] img_objects_start = new int[11];
    int[] img_objects_top = new int[11];

    int[] img_location = new int[11];

    ImageView[] img_in_top = new ImageView[7];

    String
            font_name = "";
    Typeface tf;
    int
            obj_count = 0;
    ImageView[] img_obj = new ImageView[11];
    ImageView img_boat;
    ImageView img_move_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        img_boat = findViewById(R.id.img_boat);
        RelativeLayout.LayoutParams lp_img_boat = new RelativeLayout.LayoutParams((int) (screenWidth * 0.3), (int) (screenHeight * 0.3));
        lp_img_boat.topMargin = (int) (screenHeight * 0.56);
        lp_img_boat.setMarginStart(((int) (screenWidth * 0.33)));
        img_boat.setLayoutParams(lp_img_boat);

        img_move_button = findViewById(R.id.img_move_button);
        RelativeLayout.LayoutParams lp_img_move_button = new RelativeLayout.LayoutParams((int) (screenWidth * 0.15), (int) (screenHeight * 0.15));
        lp_img_move_button.topMargin = (int) (screenHeight * 0.41);
        lp_img_move_button.setMarginStart(((int) (screenWidth * 0.01)));
        img_move_button.setLayoutParams(lp_img_move_button);

        ImageView img_music = findViewById(R.id.img_music);
        RelativeLayout.LayoutParams lp_img_music = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.1));
        lp_img_music.topMargin = (int) (screenHeight * 0.8);
        lp_img_music.setMarginStart(((int) (screenWidth * 0.01)));
        img_music.setLayoutParams(lp_img_music);



        font_name = "fonts/BYekan.ttf";
        tf = Typeface.createFromAsset(getAssets(),font_name );
        TextView txt_back = findViewById(R.id.txt_back);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, (int) (screenHeight * 0.005), (int) (screenWidth * 0.37), 0);
        txt_back.setLayoutParams(layoutParams);
        txt_back.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.035));
        txt_back.setTypeface(tf);

        TextView txt_play_again = findViewById(R.id.txt_play_again);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(0, (int) (screenHeight * 0.005), (int) (screenWidth * 0.20), 0);
        txt_play_again.setLayoutParams(layoutParams1);
        txt_play_again.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.035));
        txt_play_again.setTypeface(tf);

        TextView txt_rules = findViewById(R.id.txt_rules);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(0, (int) (screenHeight * 0.005), (int) (screenWidth * 0.07), 0);
        txt_rules.setLayoutParams(layoutParams2);
        txt_rules.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.035));
        txt_rules.setTypeface(tf);

        TextView txt_timer = findViewById(R.id.txt_timer);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams3.setMargins(0, (int) (screenHeight * 0.228), (int) (screenWidth * 0.649), 0);
        txt_timer.setLayoutParams(layoutParams3);
        txt_timer.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.027));
        txt_timer.setTypeface(tf);

        TextView txt_helps = findViewById(R.id.txt_helps);
        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams4.setMargins(0, (int) (screenHeight * 0.805), (int) (screenWidth * 0.142), 0);
        txt_helps.setLayoutParams(layoutParams4);
        txt_helps.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.033));
        txt_helps.setTypeface(tf);



        for (int i = 1; i <= 10; i++) {
            img_location[i] = 1;
        }
        level1_set();


    }

    private void level1_set() {
        obj_count = 3;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.farmer);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer = new RelativeLayout.LayoutParams((int) (screenWidth * .08), (int) (screenHeight * 0.25));
        img_objects_top[1] = (int) (screenHeight * 0.6);
        img_objects_start[1] = (int) (screenWidth * 0.82);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("farmer");


        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.cabbage);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage = new RelativeLayout.LayoutParams((int) (screenWidth * 0.08), (int) (screenHeight * 0.12));
        img_objects_start[2] = (int) (screenWidth * 0.725);
        img_objects_top[2] = ((int) (screenHeight * 0.74));
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("cabbage");


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.wolf);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.10), (int) (screenHeight * 0.16));
        img_objects_start[3] = (int) (screenWidth * 0.61);
        img_objects_top[3] = (int) (screenHeight * 0.715);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("wolf");


//        img_obj[4] = findViewById(R.id.img_obj4);
////        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
//        img_obj[4].setImageResource(R.drawable.cabbage);
//        img_obj[4].setVisibility(View.VISIBLE);
//        RelativeLayout.LayoutParams lp_img_cabbage_new = new RelativeLayout.LayoutParams((int) (screenWidth * 0.08), (int) (screenHeight * 0.12));
//        img_objects_start[4] = (int) (screenWidth * 0.51);
//        img_objects_top[4] = (int) (screenHeight * 0.74);
//        lp_img_cabbage_new.topMargin = (img_objects_top[4]);
//        lp_img_cabbage_new.setMarginStart(img_objects_start[4]);
//        img_obj[4].setLayoutParams(lp_img_cabbage_new);
//        img_obj[4].setContentDescription("cabbage_new");

    }

    public void clk_img(View view) {


//       TranslateAnimation trans=new TranslateAnimation(0 ,-100 ,0 , 100);
//
//        trans.setDuration(500);
//        ImageView img_view = findViewById(R.id.imageView2);
//        trans.setFillAfter(true);
//        trans.setFillEnabled(true);
//        img_view.startAnimation(trans);
//        Toast.makeText(this, "majid", Toast.LENGTH_SHORT).show();
    }

    public void clk_btn(View view) {
//        TranslateAnimation trans=new TranslateAnimation(0 ,-100 ,0 , 100);
//
//        trans.setDuration(500);
//        ImageView img_view = findViewById(R.id.imageView2);
//        trans.setFillAfter(true);
//        trans.setFillEnabled(true);
//        img_view.startAnimation(trans);
//        Toast.makeText(this, "majid", Toast.LENGTH_SHORT).show();
//
//        ImageView img_view = findViewById(R.id.imageView2);
//        img_view.animate().x((int)(screenWidth/2)).y(100).setDuration(500).start();
        if (boat_passengers[1] != null || boat_passengers[2] != null) {
            if (boat_side.equals("down")) {
                int
                        hight = img_boat.getHeight();
                int
                        width = img_boat.getWidth();

                int new_top = ((int) (screenHeight * 0.37));
                int new_strt = ((int) (screenWidth * 0.55));
                img_boat.animate().x(new_strt).y(new_top).setDuration(500).start();

                if (boat_passengers[1] != null) {
                    hight = boat_passengers[1].getHeight();
                    width = boat_passengers[1].getWidth();
                    new_top = ((int) (screenHeight * 0.550)) - hight;
                    new_strt = ((int) (screenWidth * 0.8301)) - width;
                    boat_passengers[1].animate().x(new_strt).y(new_top).setDuration(500).start();
                }


                if (boat_passengers[2] != null) {
                    hight = boat_passengers[2].getHeight();
                    width = boat_passengers[2].getWidth();
                    new_top = ((int) (screenHeight * 0.550)) - hight;
                    new_strt = ((int) (screenWidth * 0.7301)) - width;
                    boat_passengers[2].animate().x(new_strt).y(new_top).setDuration(500).start();
                }

                if (boat_side.equals("down")) {
                    boat_side = "up";
                } else {
                    boat_side = "down";
                }
            } else if (boat_side.equals("up")) {
                int
                        hight = img_boat.getHeight();
                int
                        width = img_boat.getWidth();

                int new_top = ((int) (screenHeight * 0.55));
                int new_strt = ((int) (screenWidth * 0.371));
                img_boat.animate().x(new_strt).y(new_top).setDuration(500).start();

                if (boat_passengers[1] != null) {
                    hight = boat_passengers[1].getHeight();
                    width = boat_passengers[1].getWidth();
                    new_top = ((int) (screenHeight * 0.74)) - boat_passengers[1].getHeight();
                    new_strt = ((int) (screenWidth * 0.65)) - width;
                    boat_passengers[1].animate().x(new_strt).y(new_top).setDuration(500).start();
                }


                if (boat_passengers[2] != null) {
                    hight = boat_passengers[2].getHeight();
                    width = boat_passengers[2].getWidth();
                    new_top = ((int) (screenHeight * 0.74)) - boat_passengers[2].getHeight();
                    new_strt = ((int) (screenWidth * 0.55)) - width;
                    boat_passengers[2].animate().x(new_strt).y(new_top).setDuration(500).start();
                }

                if (boat_side.equals("down")) {
                    boat_side = "up";
                } else {
                    boat_side = "down";
                }
            }
        }

    }


    public void clk_obj(View view) {
        ImageView
                img_my_obj = (ImageView) view;
        int
                is_passenger = 0;
        int
                img_obj_id = 0;
        for (int i = 1; i <= 2; i++) {
            if ((boat_passengers[i] != null)) {
                if ((boat_passengers[i]).equals(img_my_obj)) {
                    is_passenger = i;
                }
            }
        }
        for (int i = 1; i <= 10; i++) {
            if (img_obj[i] != null)
                if (img_obj[i].equals(img_my_obj))
                    img_obj_id = i;
        }
        int
                flag = 0;
        boat_passenger_count = 0;
        if (is_passenger == 0) {
            if (boat_passengers[1] == null) {
                if ((boat_side.equals("down") && img_location[img_obj_id] == 1) || (boat_side.equals("up") && img_location[img_obj_id] == 2)) {
                    RelativeLayout.LayoutParams lp_img_my_obj;
                    lp_img_my_obj = (RelativeLayout.LayoutParams) img_my_obj.getLayoutParams();
                    if (boat_side.equals("down")) {
                        lp_img_my_obj.topMargin = ((int) (screenHeight * 0.74)) - img_my_obj.getHeight(); //(get_top_obj_1(String.valueOf(img_my_obj.getContentDescription())));
                        lp_img_my_obj.setMarginStart((int) (screenWidth * 0.35));
                    } else if (boat_side.equals("up")) {
                        lp_img_my_obj.topMargin = ((int) (screenHeight * 0.74)) - img_my_obj.getHeight(); //(get_top_obj_1(String.valueOf(img_my_obj.getContentDescription())));
                        lp_img_my_obj.setMarginStart((int) (screenWidth * 0.35));
                        for (int j = 1; j <= 6; j++) {
                            if (img_in_top[j] != null)
                                if (img_in_top[j].equals(img_my_obj))
                                    img_in_top[j] = null;
                        }
                    }
                    img_my_obj.setLayoutParams(lp_img_my_obj);
                    boat_passengers[1] = img_my_obj;
                    flag = 1;
                }
            }
            if (boat_passengers[2] == null && flag == 0) {
                if ((boat_side.equals("down") && img_location[img_obj_id] == 1) || (boat_side.equals("up") && img_location[img_obj_id] == 2)) {
                    {
                        RelativeLayout.LayoutParams lp_img_my_obj;
                        lp_img_my_obj = (RelativeLayout.LayoutParams) img_my_obj.getLayoutParams();
                        if (boat_side.equals("down")) {
                            lp_img_my_obj.topMargin = ((int) (screenHeight * 0.74)) - img_my_obj.getHeight(); //(get_top_obj_1(String.valueOf(img_my_obj.getContentDescription())));
                            lp_img_my_obj.setMarginStart((int) (screenWidth * 0.45));
                        } else if (boat_side.equals("up")) {
                            lp_img_my_obj.topMargin = ((int) (screenHeight * 0.74)) - img_my_obj.getHeight(); //(get_top_obj_1(String.valueOf(img_my_obj.getContentDescription())));
                            lp_img_my_obj.setMarginStart((int) (screenWidth * 0.45));
                            for (int j = 1; j <= 6; j++) {
                                if (img_in_top[j] != null)
                                    if (img_in_top[j].equals(img_my_obj))
                                        img_in_top[j] = null;
                            }
                        }
                        img_my_obj.setLayoutParams(lp_img_my_obj);
                        boat_passengers[2] = img_my_obj;
                        flag = 1;
                    }

                }
            }
            if (flag == 0) {
                if ((boat_side.equals("down") && img_location[img_obj_id] == 1) || (boat_side.equals("up") && img_location[img_obj_id] == 2))
                    Toast.makeText(this, "ظرفیت خالی نیست", Toast.LENGTH_SHORT).show();
            }
        } else {
            boat_passengers[is_passenger] = null;
            for (int i = 1; i <= obj_count; i++) {
                if (img_obj[i].getContentDescription().toString().equals(img_my_obj.getContentDescription().toString())) {
                    if (boat_side.equals("down")) {
                        RelativeLayout.LayoutParams lp_img_my_obj;

                        lp_img_my_obj = (RelativeLayout.LayoutParams) img_my_obj.getLayoutParams();

                        lp_img_my_obj.topMargin = img_objects_top[i];
                        lp_img_my_obj.setMarginStart(img_objects_start[i]);
                        img_my_obj.setLayoutParams(lp_img_my_obj);
                        img_location[i] = 1;
                    } else {
                        for (int j = 1; j <= 6; j++) {

                            if (img_in_top[j] == null) {
                                RelativeLayout.LayoutParams lp_img_my_obj;

                                lp_img_my_obj = (RelativeLayout.LayoutParams) img_my_obj.getLayoutParams();
                                    lp_img_my_obj.topMargin = ((int) (screenHeight * 0.60)) - img_my_obj.getHeight();

                                    lp_img_my_obj.setMarginStart(((int) (screenWidth * 0.09)) + ((int) (screenWidth * 0.1)) * j);

                                img_my_obj.setLayoutParams(lp_img_my_obj);
                                img_in_top[j] = img_my_obj;
                                img_location[i] = 2;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


}
