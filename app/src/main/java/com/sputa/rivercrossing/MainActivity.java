package com.sputa.rivercrossing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
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
import android.view.animation.ScaleAnimation;
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




    public int level_id=6;




    LinearLayout[] lay_message = new LinearLayout[6];
    TextView[] txt_message = new TextView[6];


    String[] level_rules = new String[21];
    String[] level_helps = new String[21];

    int
        move_count = 0;
    int
            max_move_count = 0;
    int
            max_game_time = 0;



    boolean
        game_is_running=false;
    public TextView txt_timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        set_level_rule_and_help();


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        img_boat = findViewById(R.id.img_boat);
        RelativeLayout.LayoutParams lp_img_boat = new RelativeLayout.LayoutParams((int) (screenWidth * 0.3), (int) (screenHeight * 0.3));
        lp_img_boat.topMargin = (int) (screenHeight * 0.56);
        lp_img_boat.setMarginStart(((int) (screenWidth * 0.3301)));
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
        if(level_id==2)
            level2_set();
        if(level_id==3)
            level3_set();
        if(level_id==4)
            level4_set();
        if(level_id==5)
            level5_set();
        if(level_id==6)
            level6_set();



        tim = new Timer1("time");
        tim.start();
        //game_is_running = true;
        LinearLayout lay_rules =findViewById(R.id.lay_rules);
        clk_rules(lay_rules);
        for(int i=1;i<=5;i++)
        {
            top[i]=0;
        }



        LinearLayout lay_finised =findViewById(R.id.lay_finished);
        lay_finised.setOnTouchListener(new View.OnTouchListener()    {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x =  event.getX();
                float y =  event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        float zx = ((x/screenWidth));
                        float zy = ((y/screenHeight));
                       // Toast.makeText(MainActivity.this, String.valueOf(zx)+"down"+String.valueOf(zy), Toast.LENGTH_SHORT).show();

                        if(zx>.04013 && zx<.07013)
                            if(zy>.1796 && zy<.2370)
                                clk_next();

                        if(zx>.3751 && zx<.4141)
                            if(zy>.1796 && zy<.2370)
                                clk_again();


                        break;
                    case MotionEvent.ACTION_MOVE:
                      //  Toast.makeText(MainActivity.this, String.valueOf(x)+"move", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                      //  Toast.makeText(MainActivity.this, String.valueOf(x)+"up", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "touched up");
                        break;
                }
//04013
//06013


//                01796
//                        02370
                return true;
            }
        });




    }


    private void set_level_rule_and_help()
    {
        ///////////////////////////////////////level1
        level_rules[1]="";
        level_rules[1]+="کشاورز می خواهد از رودخانه عبور کند او می خواهد گرگ، گوسفند و کلم را با خود ببرد"+'\n';
        level_rules[1]+="قوانین : "+'\n'+'\n';
        level_rules[1]+="1- کشاورز تنها کسی هست که می تواند قایق را براند. او فقط می تواند یک مسافر دیگر سوار کند"+'\n';
        level_rules[1]+="2- اگر گرگ و گوسفند باهم تنها شوند گرگ گوسفند را خواهد خورد."+'\n';
        level_rules[1]+="3- اگر گوسفند و کلم باهم تنها باشند گوسفند کلم را خواهد خورد."+'\n'+'\n';
        level_rules[1]+="چطور کشاورز گرگ، گوسفند و کلم را به سمت دیگر رودخانه ببرد بدون اینکه یکی از آنها خورده شود؟"+'\n'+'\n';
        level_helps[1]="";
        level_helps[1]+="1- کشاورز با گوسفند عبور می کند."+'\n';
        level_helps[1]+="2- کشاورز برمیگردد."+'\n';
        level_helps[1]+="3- کشاورز با گرگ عبور می کند"+'\n';
        level_helps[1]+="4- کشاورز با گوسفند برمیگردد."+'\n';
        level_helps[1]+="5- کشاورز با کلم عبور می کند."+'\n';
        level_helps[1]+="6- کشاورز برمیگردد."+'\n';
        level_helps[1]+="7- کشاورز با گوسفند عبور می کند."+'\n';
        level_helps[1]+="تمام"+'\n';

        ///////////////////////////////////////level2
        level_rules[2]="";
        level_rules[2]+="4 شوالیه می خواهند از رودخانه عبور کنند. یکی عصبانی(قرمز)، یکی تنبل(سیاه) و 2تای دیگر شجاع(آبی) هستند"+'\n';
        level_rules[2]+="قوانین : "+'\n'+'\n';
        level_rules[2]+="1- حداکثر ظرفیت قایق 2 شوالیه است."+'\n';
        level_rules[2]+="2- شوالیه تنبل نمی تواند به تنهایی عبور کند و در دو سمت رودخانه نمی تواند تنها بماند."+'\n';
        level_rules[2]+="3- شوالیه عصبانی با دیگر شوالیه ها سوار قایق نمی شود"+'\n';
        level_rules[2]+="شوالیه ها چطور می توانند از رودخانه عبور کنند."+'\n';

        level_helps[2]="";
        level_helps[2]+="1- شوالیه های شجاع عبور می کنند."+'\n';
        level_helps[2]+="2- یکی از آنها برمیگردد."+'\n';
        level_helps[2]+="3- شوالیه عصبانی عبور می کند"+'\n';
        level_helps[2]+="4- شوالیه شجاع برمیگردد."+'\n';
        level_helps[2]+="5- شوالیه شجاع با شوالیه تنبل عبور می کند."+'\n';
        level_helps[2]+="6- شوالیه شجاع برمیگردد."+'\n';
        level_helps[2]+="7- شوالیه های شجاع عبور می کنند."+'\n';
        level_helps[2]+="تمام"+'\n';

        ///////////////////////////////////////level3
        level_rules[3]="";
        level_rules[3]+="2 کارگر و 2 بچه می خواهند از رودخانه عبور کنند."+'\n';
        level_rules[3]+="قوانین : "+'\n'+'\n';
        level_rules[3]+="1- قایق ظرفیت حداکثر یک کارگر یا ۲ بچه را دارد."+'\n';
        level_rules[3]+="2- هرکس می تواند قایق را براند."+'\n';
        level_rules[3]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';


        level_helps[3]="";
        level_helps[3]+="1- بچه ها از رودخانه عبور می کنند."+'\n';
        level_helps[3]+="2- یکی از بچه ها برمیگردد."+'\n';
        level_helps[3]+="3- یکی از کارگر ها عبور می کند"+'\n';
        level_helps[3]+="4- بچه باقی مانده برمیگردد."+'\n';
        level_helps[3]+="5- بچه ها عبور می کنند."+'\n';
        level_helps[3]+="6- یکی از بچه ها برمیگردد."+'\n';
        level_helps[3]+="7- یک کارگر عبور می کند."+'\n';
        level_helps[3]+="8- بچه باقی مانده برمیگردد."+'\n';
        level_helps[3]+="9- بچه ها عبور می کنند."+'\n';
        level_helps[3]+="تمام"+'\n';

        ///////////////////////////////////////level4
        level_rules[4]="";
        level_rules[4]+="5 مرد می خواهند از رودخانه عبور کنند ولی هیچکدام از آنها همسایه بغل دستی خود را دوست ندارد برای مثال مرد شماره 1 مردهای شماره 2 و 5 را دوست ندارد."+'\n';
        level_rules[4]+="قوانین : "+'\n'+'\n';
        level_rules[4]+="1- حداکثر ظرفیت قایق 2 نفر است."+'\n';
        level_rules[4]+="2- اگر 2 مرد همدیگر را دوست نداشته باشند باهم سوار قایق نمی شوند و در دو سمت رودخانه باهم تنها نمی مانند."+'\n';
        level_rules[4]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';


        level_helps[4]="";
        level_helps[4]+="1- مرد اولی با سومی عبور می کند."+'\n';
        level_helps[4]+="2- مرد اولی برمیگردد."+'\n';
        level_helps[4]+="3- مرد دوم با مرد پنجم عبور می کنند"+'\n';
        level_helps[4]+="4- مرد دوم برمیگردد."+'\n';
        level_helps[4]+="5- مرد دوم و چهارم عبور می کنند."+'\n';
        level_helps[4]+="6- مرد چهارم برمیگردد."+'\n';
        level_helps[4]+="7- مرد اول و چهارم عبور می کنند."+'\n';
        level_helps[4]+="تمام"+'\n';

        ///////////////////////////////////////level5
        level_rules[5]="";
        level_rules[5]+="یک خانواده می خواهند از رودخانه عبور کنند. وزن پدر 90 کیلو، مادر 80 کیلو، پسر 60 کیلو، دختر 40 کیلو هست. همچنین آنها یک کیف به وزن 20 کیلو دارند."+'\n';
        level_rules[5]+="قوانین : "+'\n'+'\n';
        level_rules[5]+="1- قایق وزن بیشتر از 100 کیلو را نمی تواند تحمل کند."+'\n';
        level_rules[5]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';


        level_helps[5]="";
        level_helps[5]+="1ـ 40کیلو + 60 کیلو عبور می کنند."+'\n';
        level_helps[5]+="2- 40کیلو بر می گردد."+'\n';
        level_helps[5]+="3- 90کیلو عبور می کند."+'\n';
        level_helps[5]+="4- 60کیلو برمیگردد."+'\n';
        level_helps[5]+="5- 40کیلو + 60 کیلو عبور می کنند."+'\n';
        level_helps[5]+="6- 40کیلو برمیگردد."+'\n';
        level_helps[5]+="7- 80کیلو + 20 کیلو عبور می کنند.."+'\n';
        level_helps[5]+="8- 60کیلو بر میگردد."+'\n';
        level_helps[5]+="9- 40کیلو + 60 کیلو عبور می کنند."+'\n';

        level_helps[5]+="تمام"+'\n';



    }
    private void clk_next()
    {
        startActivity(new Intent(this,Menu.class));
    }
    private void clk_again()
    {

//        LinearLayout lay_cover =findViewById(R.id.lay_cover);
//        lay_cover.setVisibility(View.GONE);
//        LinearLayout lay_finished =findViewById(R.id.lay_finished);
//        lay_finished.setVisibility(View.GONE);
//
//        ImageView img_finished1 =findViewById(R.id.img_finished1);
//        img_finished1.setVisibility(View.GONE);
//        ImageView img_finished2 =findViewById(R.id.img_finished2);
//        img_finished2.setVisibility(View.GONE);
//
//        ImageView img_star1 =findViewById(R.id.img_star1);
//        img_star1.setVisibility(View.GONE);
//        ImageView img_star2 =findViewById(R.id.img_star2);
//        img_star2.setVisibility(View.GONE);
//        ImageView img_star3 =findViewById(R.id.img_star3);
//        img_star3.setVisibility(View.GONE);
//        time=0;
//        move_count=0;
//        game_is_running=true;
//        if(level_id==1)
//            level1_set();


        finish();
        startActivity(getIntent());



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
        img_objects_start[1] = (int) (screenWidth * 0.870);
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




        max_move_count=6;
        max_game_time=30;






    }

    private void level2_set() {
        obj_count = 4;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.black_knight);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.10), (int) (screenHeight * 0.16));
        img_objects_top[1]= (int) (screenHeight * 0.715);
        img_objects_start[1] = (int) (screenWidth * 0.870);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("black_knight");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.red_knight);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.10), (int) (screenHeight * 0.16));
        img_objects_start[2] = (int) (screenWidth * 0.78);
        img_objects_top[2] = (int) (screenHeight * 0.715);
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("red_knight");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.blue_knight);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.10), (int) (screenHeight * 0.16));
        img_objects_start[3] = (int) (screenWidth * 0.69);
        img_objects_top[3] = (int) (screenHeight * 0.715);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("blue_knight1");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.blue_knight);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.10), (int) (screenHeight * 0.16));
        img_objects_start[4] = (int) (screenWidth * 0.60);
        img_objects_top[4] = (int) (screenHeight * 0.715);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("blue_knight2");
        img_obj[4].setTextDirection(1);




        max_move_count=6;
        max_game_time=30;






    }
    private void level3_set() {
        obj_count = 4;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.worker);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenHeight * 0.22));
        img_objects_top[1]= (int) (screenHeight * 0.65);
        img_objects_start[1] = (int) (screenWidth * 0.850);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("worker1");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.worker);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.13), (int) (screenHeight * 0.22));
        img_objects_start[2] = (int) (screenWidth * 0.76);
        img_objects_top[2] = (int) (screenHeight * 0.65);
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("worker2");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.baby);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.22));
        img_objects_start[3] = (int) (screenWidth * 0.67);
        img_objects_top[3] = (int) (screenHeight * 0.65);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("baby1");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.baby);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.22));
        img_objects_start[4] = (int) (screenWidth * 0.60);
        img_objects_top[4] = (int) (screenHeight * 0.65);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("baby2");
        img_obj[4].setTextDirection(1);




        max_move_count=9;
        max_game_time=30;






    }

    private void level4_set() {
        obj_count = 5;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.man1);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_top[1]= (int) (screenHeight * 0.69);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("man1");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.man2);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[2] = (int) (screenWidth * 0.79);
        img_objects_top[2] = (int) (screenHeight * 0.69);
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("man2");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.man3);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[3] = (int) (screenWidth * 0.7);
        img_objects_top[3] = (int) (screenHeight * 0.69);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("man3");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.man4);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[4] = (int) (screenWidth * 0.61);
        img_objects_top[4] = (int) (screenHeight * 0.73);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("man4");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.man5);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[5] = (int) (screenWidth * 0.36);
        img_objects_top[5] = (int) (screenHeight * 0.75);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("man5");
        img_obj[5].setTextDirection(1);



        max_move_count=7;
        max_game_time=30;






    }
    private void level5_set() {
        obj_count = 5;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.father);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_top[1]= (int) (screenHeight * 0.69);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("father");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.mother);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_start[2] = (int) (screenWidth * 0.81);
        img_objects_top[2] = (int) (screenHeight * 0.69);
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("mother");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.boy);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.10), (int) (screenHeight * 0.2));
        img_objects_start[3] = (int) (screenWidth * 0.75);
        img_objects_top[3] = (int) (screenHeight * 0.7);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("boy");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.girl);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[4] = (int) (screenWidth * 0.68);
        img_objects_top[4] = (int) (screenHeight * 0.73);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("girl");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.bag);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.1));
        img_objects_start[5] = (int) (screenWidth * 0.60);
        img_objects_top[5] = (int) (screenHeight * 0.77);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("bag");
        img_obj[5].setTextDirection(1);



        max_move_count=9;
        max_game_time=30;






    }
    private void level6_set() {
        obj_count = 6;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.man_blue);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_top[1]= (int) (screenHeight * 0.69);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("father");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.mother);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_start[2] = (int) (screenWidth * 0.81);
        img_objects_top[2] = (int) (screenHeight * 0.69);
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("mother");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.boy);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.10), (int) (screenHeight * 0.2));
        img_objects_start[3] = (int) (screenWidth * 0.75);
        img_objects_top[3] = (int) (screenHeight * 0.7);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("boy");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.girl);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[4] = (int) (screenWidth * 0.68);
        img_objects_top[4] = (int) (screenHeight * 0.73);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("girl");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.bag);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.1));
        img_objects_start[5] = (int) (screenWidth * 0.60);
        img_objects_top[5] = (int) (screenHeight * 0.77);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("bag");
        img_obj[5].setTextDirection(1);



        max_move_count=9;
        max_game_time=30;






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
    public boolean check_level2_cross()
    {
        boolean result=true;


                //     Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();


        if((boat_passengers[1]==img_obj[1] && boat_passengers[2]==null) || (boat_passengers[2]==img_obj[1] && boat_passengers[1]==null))
        {
            show_message("شوالیه سیاه نمی تواند تنهایی عبور کند");
            result=false;
        }
        if((boat_passengers[1]==img_obj[2] && boat_passengers[2]!=null) || (boat_passengers[2]==img_obj[2] && boat_passengers[1]!=null))
        {
            show_message("شوالیه قرمز باید تنها عبور کند");
            result=false;
        }


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

        if((img_obj[1].getTextDirection()==1 && img_obj[2].getTextDirection()!=1 && img_obj[4].getTextDirection()!=1 && img_obj[3].getTextDirection()!=1) || (img_obj[1].getTextDirection()==2 && img_obj[2].getTextDirection()!=2 && img_obj[3].getTextDirection()!=2 && img_obj[4].getTextDirection()!=2))
        {
//                Toast.makeText(this, "گوسفند و کلم نمی توانند تنها باشند", Toast.LENGTH_SHORT).show();
            cant_cros_message="شوالیه سیاه نمی تواند تنها بماند";
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


        return result;
    }

    public boolean check_level3_cross()
    {
        boolean result=true;


        //     Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();


        if((boat_passengers[1]==img_obj[1] && boat_passengers[2]!=null) || (boat_passengers[2]==img_obj[1] && boat_passengers[1]!=null))
        {
            show_message("کارگر باید تنهایی عبور کند");
            result=false;
        }
        else if((boat_passengers[1]==img_obj[2] && boat_passengers[2]!=null) || (boat_passengers[2]==img_obj[2] && boat_passengers[1]!=null))
        {
            show_message("کارگر باید تنهایی عبور کند");
            result=false;
        }





        return result;
    }


    public boolean check_level4_cross()
    {
        boolean result=true;


        //     Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        int boot_passnger1=0;
        int boot_passnger2=0;

        if(boat_passengers[1]==img_obj[1])
            boot_passnger1=1;
        if(boat_passengers[2]==img_obj[1])
            boot_passnger2=1;

        if(boat_passengers[1]==img_obj[2])
            boot_passnger1=2;
        if(boat_passengers[2]==img_obj[2])
            boot_passnger2=2;

        if(boat_passengers[1]==img_obj[3])
            boot_passnger1=3;
        if(boat_passengers[2]==img_obj[3])
            boot_passnger2=3;

        if(boat_passengers[1]==img_obj[4])
            boot_passnger1=4;
        if(boat_passengers[2]==img_obj[4])
            boot_passnger2=4;

        if(boat_passengers[1]==img_obj[5])
            boot_passnger1=5;
        if(boat_passengers[2]==img_obj[5])
            boot_passnger2=5;

        if(boot_passnger1>1 && boot_passnger1<5)
        {
            if(boot_passnger1==boot_passnger2+1)
            {
                result=false;
                show_message("مرد"+String.valueOf(boot_passnger1)+" با مرد"+String.valueOf(boot_passnger2)+" نمی تواند حرکت کند ");

            }
            if(boot_passnger1==boot_passnger2-1)
            {
                result =false;
                show_message("مرد"+String.valueOf(boot_passnger1)+" با مرد"+String.valueOf(boot_passnger2)+" نمی تواند حرکت کند ");
            }
        }
        if(boot_passnger1 ==1 )
        {
            if(boot_passnger2==2 || boot_passnger2==5)
            {
                result=false;
                show_message("مرد"+String.valueOf(boot_passnger1)+" با مرد"+String.valueOf(boot_passnger2)+" نمی تواند حرکت کند ");
            }
        }
        if(boot_passnger1==5)
        {
            if(boot_passnger2==4 || boot_passnger2==1)
            {
                result=false;
                show_message("مرد"+String.valueOf(boot_passnger1)+" با مرد"+String.valueOf(boot_passnger2)+" نمی تواند حرکت کند ");
            }
        }
        if(result) {
            if (boot_passnger2 > 1 && boot_passnger2 < 5) {
                if (boot_passnger2 == boot_passnger1 + 1) {
                    result = false;
                    show_message("مرد" + String.valueOf(boot_passnger1) + " با مرد" + String.valueOf(boot_passnger2) + " نمی تواند حرکت کند ");
                }
                if (boot_passnger2 == boot_passnger1 - 1) {
                    result = false;
                    show_message("مرد" + String.valueOf(boot_passnger1) + " با مرد" + String.valueOf(boot_passnger2) + " نمی تواند حرکت کند ");
                }
            }
            if (boot_passnger2 == 1) {
                if (boot_passnger2 == 2 || boot_passnger1 == 5) {
                    result = false;
                    show_message("مرد" + String.valueOf(boot_passnger1) + " با مرد" + String.valueOf(boot_passnger2) + " نمی تواند حرکت کند ");
                }
            }
            if (boot_passnger2 == 5) {
                if (boot_passnger1 == 4 || boot_passnger1 == 1) {
                    result = false;
                    show_message("مرد" + String.valueOf(boot_passnger1) + " با مرد" + String.valueOf(boot_passnger2) + " نمی تواند حرکت کند ");
                }
            }
        }
        if(result) {
            if (boat_passengers[1] != null && boat_side.equals("down")) {
                boat_passengers[1].setTextDirection(2);
            }
            if (boat_passengers[2] != null && boat_side.equals("down")) {
                boat_passengers[2].setTextDirection(2);
            }
            if (boat_passengers[1] != null && boat_side.equals("up")) {
                boat_passengers[1].setTextDirection(1);
            }
            if (boat_passengers[2] != null && boat_side.equals("up")) {
                boat_passengers[2].setTextDirection(1);
            }
            boolean
                    cant = false;
//                Toast.makeText(this,String.valueOf(img_obj[1].getTextDirection()), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, String.valueOf(img_obj[2].getTextDirection()), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, String.valueOf(img_obj[4].getTextDirection()), Toast.LENGTH_SHORT).show();

            if ((img_obj[1].getTextDirection() == 1 && img_obj[2].getTextDirection() == 1 && img_obj[3].getTextDirection() != 1 && img_obj[4].getTextDirection() != 1 && img_obj[5].getTextDirection() != 1) || (img_obj[1].getTextDirection() == 2 && img_obj[2].getTextDirection() == 2 && img_obj[3].getTextDirection() != 2 && img_obj[4].getTextDirection() != 2 && img_obj[5].getTextDirection() != 2)) {
                cant_cros_message = "مرد 1 با مرد 2 نمی تواند تنها بماند";
                show_message(cant_cros_message);
                cant = true;
            }
            if ((img_obj[1].getTextDirection() == 1 && img_obj[2].getTextDirection() != 1 && img_obj[3].getTextDirection() != 1 && img_obj[4].getTextDirection() != 1 && img_obj[5].getTextDirection() == 1) || (img_obj[1].getTextDirection() == 2 && img_obj[2].getTextDirection() != 2 && img_obj[3].getTextDirection() != 2 && img_obj[4].getTextDirection() != 2 && img_obj[5].getTextDirection() == 2)) {
                cant_cros_message = "مرد 1 با مرد 5 نمی تواند تنها بماند";
                show_message(cant_cros_message);
                cant = true;
            }
            if ((img_obj[1].getTextDirection() != 1 && img_obj[2].getTextDirection() == 1 && img_obj[3].getTextDirection() == 1 && img_obj[4].getTextDirection() != 1 && img_obj[5].getTextDirection() != 1) || (img_obj[1].getTextDirection() != 2 && img_obj[2].getTextDirection() == 2 && img_obj[3].getTextDirection() == 2 && img_obj[4].getTextDirection() != 2 && img_obj[5].getTextDirection() != 2)) {
                cant_cros_message = "مرد 2 با مرد 3 نمی تواند تنها بماند";
                show_message(cant_cros_message);
                cant = true;
            }
            if ((img_obj[1].getTextDirection() != 1 && img_obj[2].getTextDirection() != 1 && img_obj[3].getTextDirection() == 1 && img_obj[4].getTextDirection() == 1 && img_obj[5].getTextDirection() != 1) || (img_obj[1].getTextDirection() != 2 && img_obj[2].getTextDirection() != 2 && img_obj[3].getTextDirection() == 2 && img_obj[4].getTextDirection() == 2 && img_obj[5].getTextDirection() != 2)) {
                cant_cros_message = "مرد 3 با مرد 4 نمی تواند تنها بماند";
                show_message(cant_cros_message);
                cant = true;
            }
            if ((img_obj[1].getTextDirection() != 1 && img_obj[2].getTextDirection() != 1 && img_obj[3].getTextDirection() != 1 && img_obj[4].getTextDirection() == 1 && img_obj[5].getTextDirection() == 1) || (img_obj[1].getTextDirection() != 2 && img_obj[2].getTextDirection() != 2 && img_obj[3].getTextDirection() != 2 && img_obj[4].getTextDirection() == 2 && img_obj[5].getTextDirection() == 2)) {
                cant_cros_message = "مرد 4 با مرد 5 نمی تواند تنها بماند";
                show_message(cant_cros_message);
                cant = true;
            }
            if (cant) {
                if (boat_passengers[1] != null && boat_side.equals("down")) {
                    boat_passengers[1].setTextDirection(1);
                }
                if (boat_passengers[2] != null && boat_side.equals("down")) {
                    boat_passengers[2].setTextDirection(1);
                }
                if (boat_passengers[1] != null && boat_side.equals("up")) {
                    boat_passengers[1].setTextDirection(2);
                }
                if (boat_passengers[2] != null && boat_side.equals("up")) {
                    boat_passengers[2].setTextDirection(2);
                }
                //     Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                result = false;
            }
        }







        return result;
    }
    public boolean check_level5_cross()
    {
        boolean result=true;


        //     Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        int boot_passnger1=0;
        int boot_passnger2=0;

        if(boat_passengers[1]==img_obj[1])
            boot_passnger1=90;
        if(boat_passengers[2]==img_obj[1])
            boot_passnger2=90;

        if(boat_passengers[1]==img_obj[2])
            boot_passnger1=80;
        if(boat_passengers[2]==img_obj[2])
            boot_passnger2=80;

        if(boat_passengers[1]==img_obj[3])
            boot_passnger1=60;
        if(boat_passengers[2]==img_obj[3])
            boot_passnger2=60;

        if(boat_passengers[1]==img_obj[4])
            boot_passnger1=40;
        if(boat_passengers[2]==img_obj[4])
            boot_passnger2=40;

        if(boat_passengers[1]==img_obj[5])
            boot_passnger1=20;
        if(boat_passengers[2]==img_obj[5])
            boot_passnger2=20;


                if (boot_passnger1 + boot_passnger2 >100) {
                    result = false;
                    show_message("وزن قایق نباید بیشتر از 100 کیلو باشد");
                }

        if(result)
        {
            if((boat_passengers[1]==img_obj[5] && boat_passengers[2]==null) || (boat_passengers[2]==img_obj[5] && boat_passengers[1]==null))
            {
                result = false;
                show_message("کیف نمی تواند به تنهایی عبور کند");
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
        if(level_id==2) {
        result =check_level2_cross();
        }

        if(level_id==3) {
        result =check_level3_cross();
        }
        if(level_id==4) {
            result =check_level4_cross();
        }
        if(level_id==5) {
            result =check_level5_cross();
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
                move_count++;

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
                move_count++;

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
        if(level_id==2)
        {
            result = check_level1_finished();

        }
        if(level_id==3)
        {
            result = check_level1_finished();

        }
        if(level_id==4)
        {
            result = check_level4_finished();

        }
        if(level_id==5)
        {
            result = check_level4_finished();

        }
     //   result=true;
        if(result) {
          //  Toast.makeText(this, "Its finished", Toast.LENGTH_SHORT).show();
            LinearLayout lay_finished = findViewById(R.id.lay_finished);
            RelativeLayout.LayoutParams lp_lay_finished = new RelativeLayout.LayoutParams((int)(screenWidth*.45),(int)(screenHeight*.7));
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
                lp_img_finished1.setMarginStart((int)(screenWidth*.375));
                img_finished1.setLayoutParams(lp_img_finished1);

                img_finished2.setImageResource(R.drawable.sheep);
                RelativeLayout.LayoutParams lp_img_finished2 = new RelativeLayout.LayoutParams((int)(screenHeight*.13),(int)(screenWidth*.15));
                lp_img_finished2.topMargin = (int)(screenHeight*.535);
                //lp_lay_finished.leftMargin =(int)(screenWidth*.5);
                lp_img_finished2.setMarginStart((int)(screenWidth*.49));
                img_finished2.setLayoutParams(lp_img_finished2);

            }
            ImageView img_star1=findViewById(R.id.img_star1);
            img_star1.setVisibility(View.VISIBLE);
            img_star1.setImageResource(R.drawable.star);
            RelativeLayout.LayoutParams lp_img_star1 = new RelativeLayout.LayoutParams((int)(screenWidth*.07),(int)(screenHeight*.07));
            lp_img_star1.topMargin = (int)(screenHeight*.276);
            //lp_lay_finished.leftMargin =(int)(screenWidth*.5);
            lp_img_star1.setMarginStart((int)(screenWidth*.513));
            img_star1.setLayoutParams(lp_img_star1);
            ScaleAnimation scal=new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float)0.5,Animation.RELATIVE_TO_SELF, (float)0.5);
            scal.setDuration(1000);
        //    scal.setFillAfter(true);

            img_star1.setAnimation(scal);

            ImageView img_star2=findViewById(R.id.img_star2);
            img_star2.setVisibility(View.VISIBLE);
            if(move_count>max_move_count)
                img_star2.setImageResource(R.drawable.question);
            else
                img_star2.setImageResource(R.drawable.star);
            RelativeLayout.LayoutParams lp_img_star2 = new RelativeLayout.LayoutParams((int)(screenWidth*.07),(int)(screenHeight*.07));
            lp_img_star2.topMargin = (int)(screenHeight*.279);
            //lp_lay_finished.leftMargin =(int)(screenWidth*.5);
            lp_img_star2.setMarginStart((int)(screenWidth*.45));
            img_star2.setLayoutParams(lp_img_star2);
            scal=new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float)0.5,Animation.RELATIVE_TO_SELF, (float)0.5);
            scal.setDuration(1000);
            scal.setStartOffset(1000);
           // scal.setFillAfter(true);

            img_star2.setAnimation(scal);

            ImageView img_star3=findViewById(R.id.img_star3);
            img_star3.setVisibility(View.VISIBLE);
            if(time>max_game_time)
                img_star3.setImageResource(R.drawable.question);
            else
                img_star3.setImageResource(R.drawable.star);
            RelativeLayout.LayoutParams lp_img_star3 = new RelativeLayout.LayoutParams((int)(screenWidth*.07),(int)(screenHeight*.07));
            lp_img_star3.topMargin = (int)(screenHeight*.279);
            //lp_lay_finished.leftMargin =(int)(screenWidth*.5);
            lp_img_star3.setMarginStart((int)(screenWidth*.384));
            img_star3.setLayoutParams(lp_img_star3);
            scal=new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float)0.5,Animation.RELATIVE_TO_SELF, (float)0.5);
            scal.setDuration(1000);
            scal.setStartOffset(2000);
           // scal.setFillAfter(true);

            img_star3.setAnimation(scal);





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
    private boolean check_level4_finished()
    {
        boolean result=false;
        if(img_obj[1].getTextDirection()==2)
            if(img_obj[2].getTextDirection()==2)
                if(img_obj[3].getTextDirection()==2)
                    if(img_obj[4].getTextDirection()==2)
                        if(img_obj[5].getTextDirection()==2)
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

    public void clk_img_star2(View view) {
        if(move_count>max_move_count)
        {
            Toast.makeText(this, "بیشتر از "+String.valueOf(max_move_count)+" حرکت انجام داده اید ", Toast.LENGTH_SHORT).show();
        }
    }
    public void clk_img_star3(View view) {
        if(time>max_game_time)
        {
            Toast.makeText(this, " بازی شما بیشتر از"+String.valueOf(max_game_time)+"ثانیه طول کشیده است ", Toast.LENGTH_SHORT).show();
        }
    }

    public void clk_rules(View view) {

        LinearLayout lay_rules = findViewById(R.id.lay_rules);
        lay_rules.setBackground(getResources().getDrawable(R.drawable.rules_panel));
        lay_rules.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_lay_rules = new RelativeLayout.LayoutParams((int)(screenWidth*.63),(int)(screenHeight*.8));
        lp_lay_rules.topMargin = (int)(screenHeight*.05);
        //lp_lay_finished.leftMargin =(int)(screenWidth*.5);
        lp_lay_rules.setMarginStart((int)(screenWidth*.16));
        lay_rules.setLayoutParams(lp_lay_rules);
        LinearLayout lay_cover = findViewById(R.id.lay_cover);
        lay_cover.setVisibility(View.VISIBLE);
        TextView txt_rules = findViewById(R.id.txt_rules_body);

        txt_rules.setText(level_rules[level_id]);
        txt_rules.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.026));

        txt_rules.setTypeface(tf);

        lay_rules.setOnTouchListener(new View.OnTouchListener()    {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x =  event.getX();
                float y =  event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        float zx = ((x/screenWidth));
                        float zy = ((y/screenHeight));
                       //  Toast.makeText(MainActivity.this, String.valueOf(zx)+"down"+String.valueOf(zy), Toast.LENGTH_SHORT).show();

                        if(zx>.5674 && zx<6178)
                            if(zy>.04314 && zy<.1703) {
                               // Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                LinearLayout lay_rules = findViewById(R.id.lay_rules);
                                lay_rules.setVisibility(View.GONE);
                                LinearLayout lay_cover = findViewById(R.id.lay_cover);
                                lay_cover.setVisibility(View.GONE);
                                if(!game_is_running)
                                    game_is_running=true;
                            }




                        break;

                }
//04013
//06013


//                01796
//                        02370
                return true;
            }
        });

    }
    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "11", Toast.LENGTH_SHORT).show();
        return;
    }

    public void clk_help(View view) {

        LinearLayout lay_rules = findViewById(R.id.lay_rules);
        lay_rules.setBackground(getResources().getDrawable(R.drawable.help_panel));
        lay_rules.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_lay_rules = new RelativeLayout.LayoutParams((int)(screenWidth*.63),(int)(screenHeight*.8));
        lp_lay_rules.topMargin = (int)(screenHeight*.05);
        //lp_lay_finished.leftMargin =(int)(screenWidth*.5);
        lp_lay_rules.setMarginStart((int)(screenWidth*.16));
        lay_rules.setLayoutParams(lp_lay_rules);
        LinearLayout lay_cover = findViewById(R.id.lay_cover);
        lay_cover.setVisibility(View.VISIBLE);
        TextView txt_rules = findViewById(R.id.txt_rules_body);

        txt_rules.setText(level_helps[level_id]);
        txt_rules.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.026));

        txt_rules.setTypeface(tf);

        lay_rules.setOnTouchListener(new View.OnTouchListener()    {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x =  event.getX();
                float y =  event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        float zx = ((x/screenWidth));
                        float zy = ((y/screenHeight));
                        //  Toast.makeText(MainActivity.this, String.valueOf(zx)+"down"+String.valueOf(zy), Toast.LENGTH_SHORT).show();

                        if(zx>.5674 && zx<6178)
                            if(zy>.04314 && zy<.1703) {
                                // Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                LinearLayout lay_rules = findViewById(R.id.lay_rules);
                                lay_rules.setVisibility(View.GONE);
                                LinearLayout lay_cover = findViewById(R.id.lay_cover);
                                lay_cover.setVisibility(View.GONE);
                                if(!game_is_running)
                                    game_is_running=true;
                            }




                        break;

                }
//04013
//06013


//                01796
//                        02370
                return true;
            }
        });

    }


    public void clk_again1(View view) {

        finish();
        startActivity(getIntent());
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
