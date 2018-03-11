package com.sputa.rivercrossing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    int
            time=0;
    boolean
            is_time_start=false;
    Timer1 tim;

    String
            font_name = "";
    Typeface tf;
    int
            obj_count = 0;
    ImageView[] img_obj = new ImageView[11];
    ImageView img_boat;
    ImageView img_move_button;
    public int level_id=1;
    LinearLayout[] lay_message = new LinearLayout[6];
    TextView[] txt_message = new TextView[6];



    boolean
        game_is_running=false;
    public TextView txt_timer;
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
        RelativeLayout.LayoutParams lp_img_move_button = new RelativeLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenHeight * 0.13));
        lp_img_move_button.topMargin = (int) (screenHeight * 0.49);
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

        txt_timer = findViewById(R.id.txt_timer);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams3.setMargins(0, (int) (screenHeight * 0.228), (int) (screenWidth * 0.649), 0);
        txt_timer.setLayoutParams(layoutParams3);
        txt_timer.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.027));
        txt_timer.setTypeface(tf);


        int message_text_size=(int) (screenWidth * 0.026);
        txt_message[1] = findViewById(R.id.txt_message1);
        txt_message[1].setTextSize(TypedValue.COMPLEX_UNIT_PX, message_text_size);
        txt_message[1].setTypeface(tf);

        int lay_message_height=(int)(screenHeight*.1);
        RelativeLayout.LayoutParams layoutParams11 = new RelativeLayout.LayoutParams((int)(screenWidth*.4),lay_message_height);
        lay_message[1] = findViewById(R.id.lay_messsage1);
        layoutParams11.setMargins(0, lay_message_height*-1, (int) (screenWidth *.54), 0);
        lay_message[1].setLayoutParams(layoutParams11);

        txt_message[2]= findViewById(R.id.txt_message2);
        txt_message[2].setTextSize(TypedValue.COMPLEX_UNIT_PX, message_text_size);
        txt_message[2].setTypeface(tf);

        lay_message[2] = findViewById(R.id.lay_messsage2);
        lay_message[2].setLayoutParams(layoutParams11);

        txt_message[3]= findViewById(R.id.txt_message3);
        txt_message[3].setTextSize(TypedValue.COMPLEX_UNIT_PX, message_text_size);
        txt_message[3].setTypeface(tf);

        lay_message[3] = findViewById(R.id.lay_messsage3);
        lay_message[3].setLayoutParams(layoutParams11);

        txt_message[4]= findViewById(R.id.txt_message4);
        txt_message[4].setTextSize(TypedValue.COMPLEX_UNIT_PX, message_text_size);
        txt_message[4].setTypeface(tf);

        lay_message[4] = findViewById(R.id.lay_messsage4);
        lay_message[4].setLayoutParams(layoutParams11);

        txt_message[5]= findViewById(R.id.txt_message5);
        txt_message[5].setTextSize(TypedValue.COMPLEX_UNIT_PX, message_text_size);
        txt_message[5].setTypeface(tf);

        lay_message[5] = findViewById(R.id.lay_messsage5);
        lay_message[5].setLayoutParams(layoutParams11);




//        TextView lbl_timer = findViewById(R.id.lbl_timer);
//        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams4.setMargins(0, (int) (screenHeight * 0.195), (int) (screenWidth * 0.659), 0);
//        lbl_timer.setLayoutParams(layoutParams4);
//        lbl_timer.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.021));
//        lbl_timer.setTypeface(tf);

        TextView txt_helps = findViewById(R.id.txt_helps);
        RelativeLayout.LayoutParams layoutParams44 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams44.setMargins(0, (int) (screenHeight * 0.805), (int) (screenWidth * 0.142), 0);
        txt_helps.setLayoutParams(layoutParams44);
        txt_helps.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.033));
        txt_helps.setTypeface(tf);



        for (int i = 1; i <= 10; i++) {
            img_location[i] = 1;
        }
        if(level_id==1)
            level1_set();



        tim = new Timer1("time");
        tim.start();
        game_is_running = true;
        for(int i=1;i<=5;i++)
        {
            top[i]=0;
        }



        LinearLayout lay_finised =findViewById(R.id.lay_finished);
        lay_finised.setOnTouchListener(new View.OnTouchListener()    {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(MainActivity.this, String.valueOf(x)+"down", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Toast.makeText(MainActivity.this, String.valueOf(x)+"move", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(MainActivity.this, String.valueOf(x)+"up", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "touched up");
                        break;
                }

                return true;
            }
        });




    }

    private void show_message(String message)
    {
        for(int i=1;i<=5;i++)
        {
            if(lay_message[i].getVisibility()==View.GONE)
            {
                txt_message[i].setText(message);
                lay_message[i].setVisibility(View.VISIBLE);
//                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                int lay_message_height=(int)(screenHeight*.1);
                RelativeLayout.LayoutParams layoutParams11 = (RelativeLayout.LayoutParams) lay_message[i].getLayoutParams();
//                layoutParams11.topMargin=50;
//                layoutParams11.leftMargin=50;

                lay_message[i].setLayoutParams(layoutParams11);
                lay_message[i].animate().y(top[i]).setDuration(600).start();

                top[i]+=lay_message_height;

                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                //  fadeOut.setStartOffset(2000);
                fadeOut.setStartOffset(3000);
                fadeOut.setDuration(800);

                AnimationSet animation = new AnimationSet(false); //change to false
                // animation.addAnimation(fadeIn);
                animation.addAnimation(fadeOut);
                lay_message[i].setAnimation(animation);
                final int k=i;
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                        int lay_message_height=(int)(screenHeight*.1);
                        lay_message[k].setVisibility(View.GONE);   // I wants to make the visibility of view to gone,but this is not working
                        top[k]=0;
                        lay_message[k].animate().y(lay_message_height*-1).setDuration(1).start();

                    }
                });
                for(int j=1;j<=5;j++)
                {
                    if(i!=j)
                    {
                        if(lay_message[j].getVisibility()!=View.GONE)
                        {

                            lay_message[j].animate().y(top[j]).setDuration(600).start();

                            top[j]+=lay_message_height;
                        }
                    }
                }


                break;
            }
        }
    }

    private void level1_set() {
        obj_count = 4;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.farmer);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer = new RelativeLayout.LayoutParams((int) (screenWidth * .08), (int) (screenHeight * 0.25));
        img_objects_top[1] = (int) (screenHeight * 0.6);
        img_objects_start[1] = (int) (screenWidth * 0.87);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("farmer");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.cabbage);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage = new RelativeLayout.LayoutParams((int) (screenWidth * 0.08), (int) (screenHeight * 0.12));
        img_objects_start[2] = (int) (screenWidth * 0.8);
        img_objects_top[2] = ((int) (screenHeight * 0.74));
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("cabbage");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.wolf);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.10), (int) (screenHeight * 0.16));
        img_objects_start[3] = (int) (screenWidth * 0.70);
        img_objects_top[3] = (int) (screenHeight * 0.715);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("wolf");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.sheep);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.10), (int) (screenHeight * 0.16));
        img_objects_start[4] = (int) (screenWidth * 0.60);
        img_objects_top[4] = (int) (screenHeight * 0.715);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("sheep");
        img_obj[4].setTextDirection(1);









    }
    public boolean check_level1_cross()
    {
        boolean result=true;

        boolean
                is_farmer_boarder =false;
        if(boat_passengers[1]!=null)
        {
            if(boat_passengers[1].equals(img_obj[1]))
                is_farmer_boarder=true;
        }
        if(boat_passengers[2]!=null)
        {
            if(boat_passengers[2].equals(img_obj[1]))
                is_farmer_boarder=true;
        }

        if (!is_farmer_boarder) {
            cant_cros_message = "کشاورز حتما باید در قایق باشد";
            show_message(cant_cros_message);
//            Toast.makeText(this, cant_cros_message, Toast.LENGTH_SHORT).show();
            result= false;
        }
        else
        {
            if(boat_passengers[1]!=null && boat_side.equals("down"))
            {
                boat_passengers[1].setTextDirection(2);
            }
            if(boat_passengers[2]!=null && boat_side.equals("down"))
            {
                boat_passengers[2].setTextDirection(2);
            }
            if(boat_passengers[1]!=null && boat_side.equals("up"))
            {
                boat_passengers[1].setTextDirection(1);
            }
            if(boat_passengers[2]!=null && boat_side.equals("up"))
            {
                boat_passengers[2].setTextDirection(1);
            }
            boolean
                    cant=false;
//                Toast.makeText(this,String.valueOf(img_obj[1].getTextDirection()), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, String.valueOf(img_obj[2].getTextDirection()), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, String.valueOf(img_obj[4].getTextDirection()), Toast.LENGTH_SHORT).show();

            if((img_obj[1].getTextDirection()!=1 && img_obj[2].getTextDirection()==1 && img_obj[4].getTextDirection()==1) || (img_obj[1].getTextDirection()!=2 && img_obj[2].getTextDirection()==2 && img_obj[4].getTextDirection()==2))
            {
//                Toast.makeText(this, "گوسفند و کلم نمی توانند تنها باشند", Toast.LENGTH_SHORT).show();
                cant_cros_message="گوسفند و کلم نمی توانند تنها باشند";
                show_message(cant_cros_message);
                cant=true;
            }
            else if((img_obj[1].getTextDirection()!=1 && img_obj[3].getTextDirection()==1 && img_obj[4].getTextDirection()==1) || (img_obj[1].getTextDirection()!=2 && img_obj[3].getTextDirection()==2 && img_obj[4].getTextDirection()==2))
            {
//                Toast.makeText(this, "گوسفند و گرگ نمی توانند تنها باشند", Toast.LENGTH_SHORT).show();
                cant_cros_message="گوسفند و گرگ نمی توانند تنها باشند";
                show_message(cant_cros_message);
                cant=true;
            }

            if(cant)
            {
                if(boat_passengers[1]!=null && boat_side.equals("down"))
                {
                    boat_passengers[1].setTextDirection(1);
                }
                if(boat_passengers[2]!=null && boat_side.equals("down"))
                {
                    boat_passengers[2].setTextDirection(1);
                }
                if(boat_passengers[1]!=null && boat_side.equals("up"))
                {
                    boat_passengers[1].setTextDirection(2);
                }
                if(boat_passengers[2]!=null && boat_side.equals("up"))
                {
                    boat_passengers[2].setTextDirection(2);
                }
                //     Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                result= false;
            }


        }

        return result;
    }


    public void clk_img(View view) {
    }
    String
        cant_cros_message="";
    private boolean check_can_cross()
    {
        boolean result=true;
        if(level_id==1) {
            result =check_level1_cross();
        }
        return  result;
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



        if ((boat_passengers[1] != null || boat_passengers[2] != null )&& game_is_running) {

            boolean
                allow_to_cross = false;

            allow_to_cross = check_can_cross();
            if (boat_side.equals("down") && allow_to_cross) {
                int
                        hight = 0;
                int
                        width = 0;

                int new_top = ((int) (screenHeight * 0.37));
                int new_strt = ((int) (screenWidth * 0.55));
                img_boat.animate().x(new_strt).y(new_top).setDuration(500).start();

                if (boat_passengers[1] != null) {
                    hight = boat_passengers[1].getHeight();
                    width = boat_passengers[1].getWidth();
                    new_top = ((int) (screenHeight * 0.550)) - hight;
                    new_strt = ((int) (screenWidth * 0.8301)) - width;
                    boat_passengers[1].animate().x(new_strt).y(new_top).setDuration(500).start();
                    boat_passengers[1].setTextDirection(2);
                }


                if (boat_passengers[2] != null) {
                    hight = boat_passengers[2].getHeight();
                    width = boat_passengers[2].getWidth();
                    new_top = ((int) (screenHeight * 0.550)) - hight;
                    new_strt = ((int) (screenWidth * 0.7301)) - width;
                    boat_passengers[2].animate().x(new_strt).y(new_top).setDuration(500).start();
                    boat_passengers[2].setTextDirection(2);
                }

                if (boat_side.equals("down")) {
                    boat_side = "up";
                } else {
                    boat_side = "down";
                }
            } else if (boat_side.equals("up")  && allow_to_cross) {

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
                    boat_passengers[1].setTextDirection(1);
                }
                if (boat_passengers[2] != null) {
                    hight = boat_passengers[2].getHeight();
                    width = boat_passengers[2].getWidth();
                    new_top = ((int) (screenHeight * 0.74)) - boat_passengers[2].getHeight();
                    new_strt = ((int) (screenWidth * 0.55)) - width;
                    boat_passengers[2].animate().x(new_strt).y(new_top).setDuration(500).start();
                    boat_passengers[2].setTextDirection(1);
                }
                if (boat_side.equals("down")) {
                    boat_side = "up";
                } else {
                    boat_side = "down";
                }
            }

        }
    }
    private void check_is_finished()
    {
        boolean result=false;
        if(level_id==1)
        {
            result = check_level1_finished();

        }
       // result=true;
        if(result) {
          //  Toast.makeText(this, "Its finished", Toast.LENGTH_SHORT).show();
            LinearLayout lay_finished = findViewById(R.id.lay_finished);
            RelativeLayout.LayoutParams lp_lay_finished = new RelativeLayout.LayoutParams((int)(screenHeight*.75),(int)(screenWidth*.47));
            lp_lay_finished.topMargin = (int)(screenHeight*.1);
            //lp_lay_finished.leftMargin =(int)(screenWidth*.5);
            lp_lay_finished.setMarginStart((int)(screenWidth*.25));
            lay_finished.setLayoutParams(lp_lay_finished);
            LinearLayout lay_cover = findViewById(R.id.lay_cover);
            lay_cover.setVisibility(View.VISIBLE);
            lay_finished.setVisibility(View.VISIBLE);
            ImageView img_finished1=findViewById(R.id.img_finished1);
            img_finished1.setVisibility(View.VISIBLE);
            ImageView img_finished2=findViewById(R.id.img_finished2);
            img_finished2.setVisibility(View.VISIBLE);

            if(level_id==1)
            {
                img_finished1.setImageResource(R.drawable.farmer);
                RelativeLayout.LayoutParams lp_img_finished1 = new RelativeLayout.LayoutParams((int)(screenHeight*.25),(int)(screenWidth*.15));
                lp_img_finished1.topMargin = (int)(screenHeight*.49);
                //lp_lay_finished.leftMargin =(int)(screenWidth*.5);
                lp_img_finished1.setMarginStart((int)(screenWidth*.32));
                img_finished1.setLayoutParams(lp_img_finished1);

                img_finished2.setImageResource(R.drawable.cabbage);
                RelativeLayout.LayoutParams lp_img_finished2 = new RelativeLayout.LayoutParams((int)(screenHeight*.13),(int)(screenWidth*.15));
                lp_img_finished2.topMargin = (int)(screenHeight*.535);
                //lp_lay_finished.leftMargin =(int)(screenWidth*.5);
                lp_img_finished2.setMarginStart((int)(screenWidth*.46));
                img_finished2.setLayoutParams(lp_img_finished2);

            }




            game_is_running=false;
        }

    }
    private boolean check_level1_finished()
    {
        boolean result=false;
        if(img_obj[1].getTextDirection()==2)
            if(img_obj[2].getTextDirection()==2)
                if(img_obj[3].getTextDirection()==2)
                    if(img_obj[4].getTextDirection()==2)
                        if(boat_passengers[1]==null)
                            if(boat_passengers[2]==null)
                                result=true;
        return result;
    }
    public void clk_obj(View view) {
        ImageView
                img_my_obj = (ImageView) view;
        int
                is_passenger = 0;
        int
                img_obj_id = 0;
        if (game_is_running) {
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

                        show_message("ظرفیت خالی نیست");
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
            check_is_finished();

        }
    }
    int[]
            top= new int[6];
    public void clk(View view) {



       //final LinearLayout lay_message1 = findViewById(R.id.lay_messsage1);

;


    }

    public void clk1(View view) {
       // final LinearLayout lay_message = findViewById(R.id.lay_messsage1);




        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
      //  fadeOut.setStartOffset(2000);
        fadeOut.setStartOffset(2000);
        fadeOut.setDuration(800);

        AnimationSet animation = new AnimationSet(false); //change to false
       // animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        lay_message[1].setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                lay_message[1].setVisibility(View.GONE);   // I wants to make the visibility of view to gone,but this is not working
            }
        });
    }

    public void clk10(View view) {
        check_is_finished();
    }

    public class Timer1 extends Thread {

        int oneSecond=1000;
        int value=0;
        String TAG="Timer";
        String typ="";
        //@Override
        public Timer1(String type)
        {
            typ = type;
        }
        @Override
        public void run() {

            for(;;){


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {





                            if(game_is_running) {
                                time++;



                                txt_timer.setText(String.valueOf((int)(time/60)) +":"+((int)(time%60)) );

                                Rect bounds = new Rect();
                                Paint textPaint = txt_timer.getPaint();


                                textPaint.getTextBounds("88:88", 0, 4, bounds);

                                // int height = bounds.height();
                                int width_adad = bounds.width();

                                textPaint.getTextBounds(txt_timer.getText().toString(), 0, txt_timer.getText().toString().length(), bounds);

                               // int height = bounds.height();
                                int width = bounds.width();


                                RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams3.setMargins(0, (int) (screenHeight * 0.228), ((int) (screenWidth * 0.649))+((int)((width_adad-width)/2)), 0);
                                txt_timer.setLayoutParams(layoutParams3);


                               // Toast.makeText(MainActivity.this, String.valueOf(width), Toast.LENGTH_SHORT).show();

                            }









                    }
                });


                //   Log.d("majid", String.valueOf(value));
                //Thread.currentThread();
                try {


                    Thread.sleep(oneSecond);
                    //	Log.d(TAG, " " + value);
                } catch (InterruptedException e) {
                    System.out.println("timer interrupted");
                    //value=0;
                    e.printStackTrace();
                }
            }
        }



    }


}
