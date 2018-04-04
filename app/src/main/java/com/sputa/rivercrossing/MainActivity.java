package com.sputa.rivercrossing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    ImageView[] img_in_top = new ImageView[9];
    SQLiteDatabase mydatabase;
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

    public int level_id=15;

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
    public TextView txt_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        set_level_rule_and_help();


        mydatabase = openOrCreateDatabase(getResources().getString(R.string.database_name), MODE_PRIVATE, null);


        Intent ii=getIntent();
        level_id = Integer.valueOf(ii.getStringExtra("lvl_id"));


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
        lp_img_move_button.topMargin = (int) (screenHeight * 0.45);
        lp_img_move_button.setMarginStart(((int) (screenWidth * 0.01)));
        img_move_button.setLayoutParams(lp_img_move_button);

        ImageView img_music = findViewById(R.id.img_music);
        RelativeLayout.LayoutParams lp_img_music = new RelativeLayout.LayoutParams((int) (screenWidth * 0.14), (int) (screenHeight * 0.11));
        lp_img_music.topMargin = (int) (screenHeight * 0.8);
        lp_img_music.setMarginStart(((int) (screenWidth * 0.01)));
        img_music.setLayoutParams(lp_img_music);



        ImageView img_coin = findViewById(R.id.img_coin);
        RelativeLayout.LayoutParams lp_img_coin = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.1));
        lp_img_coin.topMargin = (int) (screenHeight * 0.005);
        lp_img_coin.setMarginStart(((int) (screenWidth * 0.9)));
        img_coin.setLayoutParams(lp_img_coin);

        LinearLayout lay_coin = findViewById(R.id.lay_coin);
        RelativeLayout.LayoutParams lp_lay_coin = new RelativeLayout.LayoutParams((int) (screenWidth * 0.24), (int) (screenHeight * 0.1));
        lp_lay_coin.topMargin = (int) (screenHeight * 0.005);
        lp_lay_coin.setMarginStart(((int) (screenWidth * 0.81)));
        lay_coin.setLayoutParams(lp_lay_coin);


        font_name = "fonts/BYekan.ttf";
        tf = Typeface.createFromAsset(getAssets(),font_name );
        TextView txt_back = findViewById(R.id.txt_back);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, (int) (screenHeight * 0.005), (int) (screenWidth * 0.37), 0);
        txt_back.setLayoutParams(layoutParams);
        txt_back.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.035));
        txt_back.setTypeface(tf);

        TextView txt_coin = findViewById(R.id.txt_coin);
        RelativeLayout.LayoutParams layoutParams_txt_coin = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams_txt_coin.setMargins(0, (int) (screenHeight * 0.007), (int) (screenWidth * 0.82), 0);
        txt_coin.setLayoutParams(layoutParams_txt_coin);
        txt_coin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.035));
        txt_coin.setTypeface(tf);
        txt_coin.setText(String.valueOf(get_coin_count()));

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
        layoutParams3.setMargins(0, (int) (screenHeight * 0.635), (int) (screenWidth * 0.01), 0);
        txt_timer.setLayoutParams(layoutParams3);
        txt_timer.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        txt_timer.setTypeface(tf);

        txt_move = findViewById(R.id.txt_move);
        RelativeLayout.LayoutParams layoutParams3_txt_move = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams3_txt_move.setMargins(0, (int) (screenHeight * 0.71), (int) (screenWidth * 0.01), 0);
        txt_move.setLayoutParams(layoutParams3_txt_move);
        txt_move.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        txt_move.setTypeface(tf);


        int message_text_size=(int) (screenWidth * 0.022);
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

        ImageView img_help = findViewById(R.id.img_help);
        RelativeLayout.LayoutParams layoutParams44 = new RelativeLayout.LayoutParams(
                (int) (screenWidth * 0.155),(int) (screenHeight * 0.17));
        layoutParams44.setMargins(0, (int) (screenHeight * 0.78), (int) (screenWidth * 0.142), 0);
        img_help.setLayoutParams(layoutParams44);

        for (int i = 1; i <= 10; i++) {
            img_location[i] = 1;
        }

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
                return true;
            }
        });



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
        if(level_id==7)
            level7_set();
        if(level_id==8)
            level8_set();
        if(level_id==9)
            level9_set();
        if(level_id==10)
            level10_set();
        if(level_id==11)
            level11_set();
        if(level_id==12)
            level12_set();
        if(level_id==13)
            level13_set();
        if(level_id==14)
            level14_set();
        if(level_id==15)
            level15_set();



    }

    private int get_coin_count()
    {
        Cursor resultSet = mydatabase.rawQuery("Select * from coins where id=1", null);
        int
                cnt_coin = 0;
        if (resultSet.getCount() == 1) {
            resultSet.moveToFirst();
            cnt_coin = (resultSet.getInt(1));

        }
        return cnt_coin;
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
        level_rules[1]+="چطور کشاورز گرگ، گوسفند و کلم را به سمت دیگر رودخانه ببرد بدون اینکه یکی از آنها خورده شود؟"+'\n';
        level_rules[1]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[1]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[1]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[1]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


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
        level_rules[2]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[2]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[2]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[2]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';

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
        level_rules[3]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[3]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[3]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[3]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';

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
        level_rules[4]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[4]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[4]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[4]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';

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
        level_rules[5]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[5]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[5]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[5]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';

        level_helps[5]="";
        level_helps[5]+="1ـ 40کیلو + 60 کیلو عبور می کنند."+'\n';
        level_helps[5]+="2- 40کیلو بر می گردد."+'\n';
        level_helps[5]+="3- 90کیلو عبور می کند."+'\n';
        level_helps[5]+="4- 60کیلو برمیگردد."+'\n';
        level_helps[5]+="5- 40کیلو + 60 کیلو عبور می کنند."+'\n';
        level_helps[5]+="6- 40کیلو برمیگردد."+'\n';
        level_helps[5]+="7- 80کیلو + 20 کیلو عبور می کنند."+'\n';
        level_helps[5]+="8- 60کیلو بر میگردد."+'\n';
        level_helps[5]+="9- 40کیلو + 60 کیلو عبور می کنند."+'\n';

        level_helps[5]+="تمام"+'\n';

        ///////////////////////////////////////level6
        level_rules[6]="";
        level_rules[6]+="3 مسافر می خواهند از رودخانه عبور کنند. اولی 2 کیف، دومی یک کیف و سومی کیفی ندارد."+'\n';
        level_rules[6]+="قوانین : "+'\n'+'\n';
        level_rules[6]+="حداکثر ظرفیت قایق 2 مسافر یا یک مسافر با یک کیف می باشد."+'\n';
        level_rules[6]+="هیچ مسافری نباید با کیف سایر مسافران تنها بماند."+'\n';
        level_rules[6]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';
        level_rules[6]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[6]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[6]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[6]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


        level_helps[6]="";
        level_helps[6]+="1-مسافر دوم با کیفش عبور می کند."+'\n';
        level_helps[6]+="2- مسافر دوم برمیگردد."+'\n';
        level_helps[6]+="3-مسافران دوم و سوم عبور می کنند."+'\n';
        level_helps[6]+="4-مسافر سوم بر میگردد."+'\n';
        level_helps[6]+="5-مسافران اول و سوم عبور می کنند."+'\n';
        level_helps[6]+="6- مسافر اول برمیگردد."+'\n';
        level_helps[6]+="7- مسافر اول با یکی از کیف هایش عبور می کند."+'\n';
        level_helps[6]+="8- مسافر اول بر میگردد."+'\n';
        level_helps[6]+="9- مسافر اول با دومین کیفش عبور می کند."+'\n';
        level_helps[6]+="تمام"+'\n';

        ///////////////////////////////////////level7
        level_rules[7]="";
        level_rules[7]+="یک خانواده می خواهند به قطاری که در آن سمت رودخانه بعد از 30 دقیقه حرکت می کند برسند. یک پلیس به همراه آن هاست تا به آنها کمک کند تا از رودخانه عبور کنند."+'\n';
        level_rules[7]+="قوانین : "+'\n'+'\n';
        level_rules[7]+="1- پلیس در عرض 1 دقیقه از رودخانه عبور می کند."+'\n';
        level_rules[7]+="2- پدر در عرض 3 دقیقه از رودخانه عبور می کند."+'\n';
        level_rules[7]+="3- مادر در عرض 6 دقیقه از رودخانه عبور می کند."+'\n';
        level_rules[7]+="4- دختر در عرض 8 دقیقه از رودخانه عبور می کند."+'\n';
        level_rules[7]+="5- پسر در عرض 12 دقیقه از رودخانه عبور می کند."+'\n';
        level_rules[7]+="6- اگر 2 نفر باهم سوار شده باشند به اندازه فرد آهسته تر زمان حساب خواهد شد."+'\n';
        level_rules[7]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';
        level_rules[7]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[7]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[7]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[7]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


        level_helps[7]="";
        level_helps[7]+="1- پلیس با پدر عبور می کنند."+'\n';
        level_helps[7]+="2- پلیس برمیگردد."+'\n';
        level_helps[7]+="3-پلیس با مادر عبور می کنند."+'\n';
        level_helps[7]+="4-پلیس بر میگردد."+'\n';
        level_helps[7]+="5-پسر با دختر عبور می کنند."+'\n';
        level_helps[7]+="6- پدر برمیگردد."+'\n';
        level_helps[7]+="7- پدر با پلیس عبور می کنند."+'\n';
        level_helps[7]+="تمام"+'\n';

        ///////////////////////////////////////level8
        level_rules[8]="";
        level_rules[8]+="سه پلیس و سه دزد می خواهند از رودخانه عبور کنند."+'\n';
        level_rules[8]+="قوانین : "+'\n'+'\n';
        level_rules[8]+="1- ظرفیت قایق حداکثر 2 مسافر می باشد و هرکس می تواند قایق را براند."+'\n';
        level_rules[8]+="2- تعداد دزدها نباید بیشتر از تعداد پلیس ها در هر سمت از رودخانه باشد."+'\n';
        level_rules[8]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';
        level_rules[8]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[8]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[8]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[8]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


        level_helps[8]="";
        level_helps[8]+="1- دو دزد عبور می کنند."+'\n';
        level_helps[8]+="2- یکی از دزدها برمیگردد."+'\n';
        level_helps[8]+="3-دو دزد عبور می کنند."+'\n';
        level_helps[8]+="4-یک دزد برمیگردد."+'\n';
        level_helps[8]+="5-دو پلیس عبور میکنند."+'\n';
        level_helps[8]+="6- یک پلیس با یک دزد برمیگردد."+'\n';
        level_helps[8]+="7- دو پلیس عبور میکنند."+'\n';
        level_helps[8]+="8- یک دزد برمیگردد."+'\n';
        level_helps[8]+="9- دو دزد عبور میکنند."+'\n';
        level_helps[8]+="10- یک دزد برمیگردد."+'\n';
        level_helps[8]+="11- دو دزد عبور می کنند."+'\n';
        level_helps[8]+="تمام"+'\n';

        ///////////////////////////////////////level9
        level_rules[9]="";
        level_rules[9]+="سه مربی همراه با ورزشکارانشان می خواهند از رودخانه عبور کنند."+'\n';
        level_rules[9]+="قوانین : "+'\n'+'\n';
        level_rules[9]+="1- هیچ کدام از مربیان به ورزشکارنشان اعتماد ندارند که با سایر مربیان تنها بمانند."+'\n';
        level_rules[9]+="2- ورزشکاران می توانند تنها بمانند و با سایر ورزشکاران مشکلی ندارند که تنها باشند."+'\n';
        level_rules[9]+="3- قایق ظرفیت حداکثر ۲ نفر را دارد و هرکس می تواند قایق را براند."+'\n';
        level_rules[9]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';
        level_rules[9]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[9]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[9]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[9]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


        level_helps[9]="";
        level_helps[9]+="1- یک مربی با ورزشکارش عبور می کنند."+'\n';
        level_helps[9]+="2-مربی  برمیگردد."+'\n';
        level_helps[9]+="3-دو ورزشکار باقی مانده عبور می کنند."+'\n';
        level_helps[9]+="4-یکی از ورزشکاران برمیگرد."+'\n';
        level_helps[9]+="5- دو مربی باقیمانده عبور میکنند."+'\n';
        level_helps[9]+="6- یک مربی با ورزشکارش برمیگردد."+'\n';
        level_helps[9]+="7-مربیان عبور میکنند."+'\n';
        level_helps[9]+="8-ورزشکاران باقیمانده برمیگردد."+'\n';
        level_helps[9]+="9- دو ورزشکار عبور میکنند."+'\n';
        level_helps[9]+="10-مربی ورزشکار باقیمانده عبور برمیگردد."+'\n';
        level_helps[9]+="11- آخرین مربی با ورزشکارش عبور می کنند."+'\n';
        level_helps[9]+="تمام"+'\n';

        ///////////////////////////////////////level10
        level_rules[10]="";
        level_rules[10]+="دو مادر با فرزندانشان می خواهند از رودخانه عبور کنند."+'\n';
        level_rules[10]+="قوانین : "+'\n'+'\n';
        level_rules[10]+="1- هیچ کودکی نمی تواند با مادر بچه های دیگر تنها بماند یا سوار قایق شود."+'\n';
        level_rules[10]+="2- بچه ها می توانند خودشان با خودشان یا با سایر بچه ها تنها بمانند."+'\n';
        level_rules[10]+="3- قایق ظرفیت حداکثر ۲ نفر را دارد و فقط مادر و پسر سرخ پوش می توانند قایق را برانند."+'\n';

        level_rules[10]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';
        level_rules[10]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[10]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[10]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[10]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


        level_helps[10]="";
        level_helps[10]+="1-پسر اولی با پسر دومی عبور می کنند."+'\n';
        level_helps[10]+="2-پسر اولی  برمیگردد."+'\n';
        level_helps[10]+="3-پسر اولی با دختر دومی عبور می کنند."+'\n';
        level_helps[10]+="4-پسر اولی برمیگردد."+'\n';
        level_helps[10]+="5-مادر اولی با مادر دومی  عبور میکنند."+'\n';
        level_helps[10]+="6-مادر اولی  برمیگردد."+'\n';
        level_helps[10]+="7-مادر اولی با پسرش عبور میکنند."+'\n';
        level_helps[10]+="8-پسر اولی برمیگردد."+'\n';
        level_helps[10]+="9-پسر اولی با خواهرش عبور میکنند."+'\n';
        level_helps[10]+="تمام"+'\n';

        ///////////////////////////////////////level11
        level_rules[11]="";
        level_rules[11]+="دو خانواده می خواهند از رودخانه عبور کنند."+'\n';
        level_rules[11]+="قوانین : "+'\n'+'\n';
        level_rules[11]+="1- هیچ دختر نمی تواند بدون حضور یکی از والدینش با والدین دیگر تنها بماند."+'\n';
        level_rules[11]+="2- دخترها نمی توانند در دو سمت رودخانه تنها بمانند."+'\n';
        level_rules[11]+="3- قایق ظرفیت حداکثر ۲ نفر را دارد و فقط پدرها می توانند قایق را برانند."+'\n';

        level_rules[11]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';
        level_rules[11]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[11]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[11]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[11]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


        level_helps[11]="";
        level_helps[11]+="1-پدرها عبور می کنند."+'\n';
        level_helps[11]+="2-یکی از پدرها برمیگردد."+'\n';
        level_helps[11]+="3-پدر با دخترش عبور می کنند."+'\n';
        level_helps[11]+="4-پدر دیگر برمیگردد."+'\n';
        level_helps[11]+="5-پدر با مادر دیگری عبور میکنند."+'\n';
        level_helps[11]+="6-همان پدر برمیگردد."+'\n';
        level_helps[11]+="7-پدر با دخترش عبور می کنند."+'\n';
        level_helps[11]+="8-پدر دیگری برمیگردد."+'\n';
        level_helps[11]+="9-پدر با مادر دیگری عبور میکنند."+'\n';
        level_helps[11]+="تمام"+'\n';

        ///////////////////////////////////////level12
        level_rules[12]="";
        level_rules[12]+="یک کشاورز به همراه خانواده و حیواناتشان می خواهند از رودخانه عبور کنند."+'\n';
        level_rules[12]+="قوانین : "+'\n'+'\n';
        level_rules[12]+="1- اگر کشاورز در اطراف نباشد سگ بقیه را گاز خواهد گرفت."+'\n';
        level_rules[12]+="2- اگر پسر در اطراف نباشد دختر خرگوش ها را اذیت خواهد کرد."+'\n';
        level_rules[12]+="3- اگر دختر در اطراف نباشد پسر سنجاب ها را اذیت خواهد کرد."+'\n';

        level_rules[12]+="4- قایق ظرفیت حداکثر ۲ نفر یا یک نفر به همراه یک حیوان را دارد و فقط اعضای خوانواده می توانند قایق را برانند."+'\n';

        level_rules[12]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';
        level_rules[12]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[12]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[12]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[12]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


        level_helps[12]="";
        level_helps[12]+="1-کشاورز و سگ عبور می کنند."+'\n';
        level_helps[12]+="2-کشاورز برمیگردد."+'\n';
        level_helps[12]+="3-کشاورز و خرگوش عبور می کنند."+'\n';
        level_helps[12]+="4-کشاورز و سگ برمیگردد."+'\n';
        level_helps[12]+="5-پسر و خرگوش باقیمانده عبور میکنند."+'\n';
        level_helps[12]+="6-پسر برمیگردد."+'\n';
        level_helps[12]+="7-دختر و پسر عبور می کنند."+'\n';
        level_helps[12]+="8-دختر برمیگردد."+'\n';
        level_helps[12]+="9-سگ و کشاورز عبور میکنند."+'\n';
        level_helps[12]+="10-پسر برمیگردد"+'\n';
        level_helps[12]+="11-دختر و پسر عبور میکنند"+'\n';
        level_helps[12]+="12-دختر برمیگردد"+'\n';
        level_helps[12]+="13-دختر و سنجاب عبور میکنند"+'\n';
        level_helps[12]+="14-کشاورز و سگ برمیگردد"+'\n';
        level_helps[12]+="15-کشاورز و سنجاب باقیمانده عبور میکنند"+'\n';
        level_helps[12]+="16-کشاورز برمیگردد"+'\n';
        level_helps[12]+="17-کشاورز و سگ عبور میکنند"+'\n';
        level_helps[12]+=""+'\n';

        level_helps[12]+="تمام"+'\n';



        ///////////////////////////////////////level13
        level_rules[13]="";
        level_rules[13]+="سه دزد می خواهند با پول هایی که دزدیده اند از رودخانه عبور کنند."+'\n';
        level_rules[13]+="قوانین : "+'\n'+'\n';
        level_rules[13]+="1- ظرفیت قایق دو نفر دزد یا یک دزد با یک کیف پول است."+'\n';
        level_rules[13]+="2- هیچ دزدی نمی تواند با کیف پولی بیشتر از مبلغ دزدیده خودش است تنها بماند."+'\n';
        level_rules[13]+="3- دو دزد نمی توانند با پول هایی که مجموعشان بیشتر از پول هایی است که دزدیده اند تنها بمانند."+'\n';
        level_rules[13]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';
        level_rules[13]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[13]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[13]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[13]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


        level_helps[13]="";
        level_helps[13]+="1-دزد 5000تایی با کیفش عبور می کنند."+'\n';
        level_helps[13]+="2-دزد برمیگردد."+'\n';
        level_helps[13]+="3-دزد8000تایی با کیف 3000تایی عبور می کنند."+'\n';
        level_helps[13]+="4-دزد برمیگردد."+'\n';
        level_helps[13]+="5-دزد 3000تایی و 5000تایی عبور میکنند."+'\n';
        level_helps[13]+="6-دزد 3000تایی با کیفش برمیگردد."+'\n';
        level_helps[13]+="7-دزد8000تایی با کیفش عبور می کنند."+'\n';
        level_helps[13]+="8-دزد5000تایی با کیفش برمیگردد."+'\n';
        level_helps[13]+="9-دزد3000تایی و 5000تایی عبور میکنند."+'\n';
        level_helps[13]+="10-دزد8000تایی تنها برمیگردد"+'\n';
        level_helps[13]+="11-دزد8000تایی با کیف 3000تایی عبور میکنند"+'\n';
        level_helps[13]+="12-دزد5000تایی تنها برمیگردد"+'\n';
        level_helps[13]+="13-دزد 5000تایی با کیفش عبور میکنند"+'\n';
        level_helps[13]+=""+'\n';


        level_helps[13]+="تمام"+'\n';

        ///////////////////////////////////////level14
        level_rules[14]="";
        level_rules[14]+="سه سگ و سه گربه می خواهند از رودخانه عبور کنند یک گربه بزرگ، یک گربه متوسط و یک گربه کوچک; یک سگ بزرگ، یک سگ متوسط و یک سگ کوچک."+'\n';
        level_rules[14]+="قوانین : "+'\n'+'\n';
        level_rules[14]+="1- ظرفیت قایق دو حیوان می باشد و هرکدام از آن ها می تواند قایق را براند."+'\n';
        level_rules[14]+="2- حیوانات متوسط نمی توانند در دو سمت رودخانه یا روی قایق با سایر حیوانات از هم نوعانشان تنها بمانند."+'\n';
        level_rules[14]+="3- تعداد سگ ها نباید بیشتر از تعداد گربه ها در دو سمت رودخانه باشد."+'\n';
        level_rules[14]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';
        level_rules[14]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[14]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[14]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[14]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


        level_helps[14]="";
        level_helps[14]+="1-گربه متوسط با سگ متوسط عبور می کنند."+'\n';
        level_helps[14]+="2-گربه متوسط برمیگردد."+'\n';
        level_helps[14]+="3-سگ بزرگ با گربه کوچک عبور می کنند."+'\n';
        level_helps[14]+="4-سگ متوسط برمیگردد."+'\n';
        level_helps[14]+="5-گربه بزرگ با گربه کوچک عبور میکنند."+'\n';
        level_helps[14]+="6-گربه کوچک با سگ کوچک برمیگردد."+'\n';
        level_helps[14]+="7-سگ متوسط با گربه متوسط عبور می کنند."+'\n';
        level_helps[14]+="8-سگ بزرگ با گربه بزرگ برمیگردد."+'\n';
        level_helps[14]+="9-گربه بزرگ با گربه کوچک عبور میکنند."+'\n';
        level_helps[14]+="10-سگ متوسط برمیگردد"+'\n';
        level_helps[14]+="11-سگ بزرگ با سگ کوچک عبور میکنند"+'\n';
        level_helps[14]+="12-گربه متوسط برمیگردد"+'\n';
        level_helps[14]+="13-گربه متوسط با سگ متوسط عبور میکنند"+'\n';
        level_helps[14]+=""+'\n';


        level_helps[14]+="تمام"+'\n';

        ///////////////////////////////////////level15
        level_rules[15]="";
        level_rules[15]+="دو شاه شطرنج (سفید و سیاه) به همراه همراهانشان می خواهند از رودخانه عبور کنند."+'\n';
        level_rules[15]+="قوانین : "+'\n'+'\n';
        level_rules[15]+="1- شاه نباید بدون همراهانش در روی قایق یا دو سمت رودخانه تنها بماند."+'\n';
        level_rules[15]+="2- وزیر می تواند قایق را براند ولی به تنهایی!."+'\n';
        level_rules[15]+="3- سربازان می توانند قایق را برانند."+'\n';
        level_rules[15]+="4- ظرفیت قایق حداکثر دو مهره می باشد."+'\n';
        level_rules[15]+="آنها چگونه می توانند از رودخانه عبور کنند؟"+'\n'+'\n';
        level_rules[15]+="جایزه اتمام مرحله (فقط در اولین اتمام) :"+'\n';
        level_rules[15]+="1- اتمام با 3 ستاره برابر با 300 سکه"+'\n';
        level_rules[15]+="1- اتمام با 2 ستاره برابر با 200 سکه"+'\n';
        level_rules[15]+="1- اتمام با 1 ستاره برابر با 100 سکه"+'\n';


        level_helps[15]="";
        level_helps[15]+="1- سرباز سفید با سرباز سیاه عبور می کنند."+'\n';
        level_helps[15]+="2-سرباز سفید برمیگردد."+'\n';
        level_helps[15]+="3-سرباز سفید با شاه سفید عبور می کنند."+'\n';
        level_helps[15]+="4-سرباز سیاه برمیگردد."+'\n';
        level_helps[15]+="5-وزیر سفید عبور میکند."+'\n';
        level_helps[15]+="6-سرباز سفید برمیگردد."+'\n';
        level_helps[15]+="7-سرباز سفید با سرباز سیاه عبور می کنند."+'\n';
        level_helps[15]+="8-سرباز سیاه برمیگردد."+'\n';
        level_helps[15]+="9-وزیر سیاه عبور میکند."+'\n';
        level_helps[15]+="10-سرباز سفید برمیگردد"+'\n';
        level_helps[15]+="11-سرباز سیاه با شاه سیاه عبور میکنند"+'\n';
        level_helps[15]+="12-سرباز سیاه برمیگردد"+'\n';
        level_helps[15]+="13-سرباز سیاه با سرباز سفید عبور میکنند"+'\n';
        level_helps[15]+=""+'\n';
        level_helps[15]+="تمام"+'\n';





    }
    private void clk_next()
    {
        finish();
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




        max_move_count=7;
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




        max_move_count=7;
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
        img_obj[1].setContentDescription("man_blue");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.bag_blue);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.15));
        img_objects_start[2] = (int) (screenWidth * 0.83);
        img_objects_top[2] = (int) (screenHeight * 0.73);
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("bag_blue1");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.bag_blue);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.10), (int) (screenHeight * 0.15));
        img_objects_start[3] = (int) (screenWidth * 0.77);
        img_objects_top[3] = (int) (screenHeight * 0.73);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("bag_blue2");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.man_red);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_start[4] = (int) (screenWidth * 0.69);
        img_objects_top[4] = (int) (screenHeight * 0.69);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("man_ted");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.bag_red);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.15));
        img_objects_start[5] = (int) (screenWidth * 0.632);
        img_objects_top[5] = (int) (screenHeight * 0.73);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("bag_red");
        img_obj[5].setTextDirection(1);

        img_obj[6] = findViewById(R.id.img_obj6);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[6].setImageResource(R.drawable.man_yellow);
        img_obj[6].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man6 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_start[6] = (int) (screenWidth * 0.56);
        img_objects_top[6] = (int) (screenHeight * 0.69);
        lp_img_man6.topMargin = (img_objects_top[6]);
        lp_img_man6.setMarginStart(img_objects_start[6]);
        img_obj[6].setLayoutParams(lp_img_man6);
        img_obj[6].setContentDescription("man_yellow");
        img_obj[6].setTextDirection(1);



        max_move_count=9;
        max_game_time=30;






    }


    private void level7_set() {
        obj_count = 5;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.police_man);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_top[1]= (int) (screenHeight * 0.69);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("police_man");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.father1);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_start[2] = (int) (screenWidth * 0.81);
        img_objects_top[2] = (int) (screenHeight * 0.69);
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("father1");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.mother1);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_start[3] = (int) (screenWidth * 0.74);
        img_objects_top[3] = (int) (screenHeight * 0.69);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("mother1");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.girl1);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[4] = (int) (screenWidth * 0.68);
        img_objects_top[4] = (int) (screenHeight * 0.73);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("girl1");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.boy1);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.19));
        img_objects_start[5] = (int) (screenWidth * 0.612);
        img_objects_top[5] = (int) (screenHeight * 0.715);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("boy1");
        img_obj[5].setTextDirection(1);




        max_move_count=7;
        max_game_time=30;






    }

    private void level8_set() {
        obj_count = 6;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.police_man);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_top[1]= (int) (screenHeight * 0.69);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("police_man1");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.police_man);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_start[2] = (int) (screenWidth * 0.82);
        img_objects_top[2] = (int) (screenHeight * 0.69);
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("police_man2");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.police_man);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_start[3] = (int) (screenWidth * 0.76);
        img_objects_top[3] = (int) (screenHeight * 0.69);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("police_man3");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.robber);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[4] = (int) (screenWidth * 0.705);
        img_objects_top[4] = (int) (screenHeight * 0.73);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("robber1");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.robber);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[5] = (int) (screenWidth * 0.625);
        img_objects_top[5] = (int) (screenHeight * 0.73);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("robber2");
        img_obj[5].setTextDirection(1);


        img_obj[6] = findViewById(R.id.img_obj6);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[6].setImageResource(R.drawable.robber);
        img_obj[6].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man6 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[6] = (int) (screenWidth * 0.545);
        img_objects_top[6] = (int) (screenHeight * 0.73);
        lp_img_man6.topMargin = (img_objects_top[6]);
        lp_img_man6.setMarginStart(img_objects_start[6]);
        img_obj[6].setLayoutParams(lp_img_man6);
        img_obj[6].setContentDescription("robber3");
        img_obj[6].setTextDirection(1);

        max_move_count=11;
        max_game_time=40;






    }
    private void level9_set() {
        obj_count = 6;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.pink_coach);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_top[1]= (int) (screenHeight * 0.7);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("pink_coach");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.pink_athlet);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_top[2]= (int) (screenHeight * 0.7);
        img_objects_start[2] = (int) (screenWidth * 0.82);

        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("pink_athlet");
        img_obj[2].setTextDirection(1);






        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.green_coach);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_start[3] = (int) (screenWidth * 0.745);
        img_objects_top[3] = (int) (screenHeight * 0.7);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("green_coach");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.green_athlet);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_start[4] = (int) (screenWidth * 0.69);
        img_objects_top[4]= (int) (screenHeight * 0.7);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("green_athlet");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.blue_coach);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_start[5] = (int) (screenWidth * 0.62);
        img_objects_top[5] = (int) (screenHeight * 0.7);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("blue_coach");
        img_obj[5].setTextDirection(1);


        img_obj[6] = findViewById(R.id.img_obj6);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[6].setImageResource(R.drawable.blue_athlet);
        img_obj[6].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man6 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_start[6] = (int) (screenWidth * 0.57);
        img_objects_top[6] = (int) (screenHeight * 0.7);
        lp_img_man6.topMargin = (img_objects_top[6]);
        lp_img_man6.setMarginStart(img_objects_start[6]);
        img_obj[6].setLayoutParams(lp_img_man6);
        img_obj[6].setContentDescription("blue_athlet");
        img_obj[6].setTextDirection(1);

        max_move_count=11;
        max_game_time=40;
        game_is_running=true;




    }
    private void level10_set() {
        obj_count = 6;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.mother1);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_top[1]= (int) (screenHeight * 0.7);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("mother1");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.boy_red);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_top[2]= (int) (screenHeight * 0.7);
        img_objects_start[2] = (int) (screenWidth * 0.82);

        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("boy_red");
        img_obj[2].setTextDirection(1);






        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.girl_red);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[3] = (int) (screenWidth * 0.76);
        img_objects_top[3] = (int) (screenHeight * 0.73);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("girl_red");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.mother_yellow);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_start[4] = (int) (screenWidth * 0.68);
        img_objects_top[4]= (int) (screenHeight * 0.7);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("mother_yellow");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.boy_yellow);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_start[5] = (int) (screenWidth * 0.63);
        img_objects_top[5] = (int) (screenHeight * 0.7);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("boy_yellow");
        img_obj[5].setTextDirection(1);


        img_obj[6] = findViewById(R.id.img_obj6);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[6].setImageResource(R.drawable.girl_yellow);
        img_obj[6].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man6 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[6] = (int) (screenWidth * 0.57);
        img_objects_top[6] = (int) (screenHeight * 0.73);
        lp_img_man6.topMargin = (img_objects_top[6]);
        lp_img_man6.setMarginStart(img_objects_start[6]);
        img_obj[6].setLayoutParams(lp_img_man6);
        img_obj[6].setContentDescription("blue_athlet");
        img_obj[6].setTextDirection(1);

        max_move_count=9;
        max_game_time=40;
        game_is_running=true;




    }
    private void level11_set() {
        obj_count = 6;

        img_obj[1] = findViewById(R.id.img_obj1);
        //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[1].setImageResource(R.drawable.father_red);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_top[1]= (int) (screenHeight * 0.7);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("father_red");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[2].setImageResource(R.drawable.mother1);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_top[2]= (int) (screenHeight * 0.7);
        img_objects_start[2] = (int) (screenWidth * 0.82);

        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("mother1");
        img_obj[2].setTextDirection(1);






        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.girl_red);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[3] = (int) (screenWidth * 0.76);
        img_objects_top[3] = (int) (screenHeight * 0.73);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("girl_red");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.father_yellow);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_start[4] = (int) (screenWidth * 0.68);
        img_objects_top[4]= (int) (screenHeight * 0.7);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("father_yellow");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.mother_yellow);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_start[5] = (int) (screenWidth * 0.63);
        img_objects_top[5] = (int) (screenHeight * 0.7);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("mother_yellow");
        img_obj[5].setTextDirection(1);


        img_obj[6] = findViewById(R.id.img_obj6);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[6].setImageResource(R.drawable.girl_yellow);
        img_obj[6].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man6 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[6] = (int) (screenWidth * 0.57);
        img_objects_top[6] = (int) (screenHeight * 0.73);
        lp_img_man6.topMargin = (img_objects_top[6]);
        lp_img_man6.setMarginStart(img_objects_start[6]);
        img_obj[6].setLayoutParams(lp_img_man6);
        img_obj[6].setContentDescription("blue_athlet");
        img_obj[6].setTextDirection(1);

        max_move_count=9;
        max_game_time=40;
        game_is_running=true;




    }
    private void level12_set() {
        obj_count = 8;

        img_obj[1] = findViewById(R.id.img_obj1);
        img_obj[1].setImageResource(R.drawable.farmer);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.2));
        img_objects_top[1]= (int) (screenHeight * 0.7);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("farmer");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
        img_obj[2].setImageResource(R.drawable.dog);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_top[2]= (int) (screenHeight * 0.73);
        img_objects_start[2] = (int) (screenWidth * 0.82);

        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("dog");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.girl_red);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[3] = (int) (screenWidth * 0.74);
        img_objects_top[3] = (int) (screenHeight * 0.73);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("girl_red");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.boy_red);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.2));
        img_objects_start[4] = (int) (screenWidth * 0.685);
        img_objects_top[4]= (int) (screenHeight * 0.7);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("boy_red");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.rabit);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[5] = (int) (screenWidth * 0.63);
        img_objects_top[5] = (int) (screenHeight * 0.73);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("rabit1");
        img_obj[5].setTextDirection(1);


        img_obj[6] = findViewById(R.id.img_obj6);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[6].setImageResource(R.drawable.rabit);
        img_obj[6].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man6 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.16));
        img_objects_start[6] = (int) (screenWidth * 0.57);
        img_objects_top[6] = (int) (screenHeight * 0.73);
        lp_img_man6.topMargin = (img_objects_top[6]);
        lp_img_man6.setMarginStart(img_objects_start[6]);
        img_obj[6].setLayoutParams(lp_img_man6);
        img_obj[6].setContentDescription("rabit2");
        img_obj[6].setTextDirection(1);


        img_obj[7] = findViewById(R.id.img_obj7);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[7].setImageResource(R.drawable.squirral);
        img_obj[7].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man7 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.14));
        img_objects_start[7] = (int) (screenWidth * 0.37);
        img_objects_top[7] = (int) (screenHeight * 0.77);
        lp_img_man7.topMargin = (img_objects_top[7]);
        lp_img_man7.setMarginStart(img_objects_start[7]);
        img_obj[7].setLayoutParams(lp_img_man7);
        img_obj[7].setContentDescription("squirral1");
        img_obj[7].setTextDirection(1);


        img_obj[8] = findViewById(R.id.img_obj8);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[8].setImageResource(R.drawable.squirral);
        img_obj[8].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man8 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.14));
        img_objects_start[8] = (int) (screenWidth * 0.3);
        img_objects_top[8] = (int) (screenHeight * 0.77);
        lp_img_man8.topMargin = (img_objects_top[8]);
        lp_img_man8.setMarginStart(img_objects_start[8]);
        img_obj[8].setLayoutParams(lp_img_man8);
        img_obj[8].setContentDescription("squirral2");
        img_obj[8].setTextDirection(1);


        max_move_count=17;
        max_game_time=60;
        game_is_running=true;




    }
    private void level13_set() {
        obj_count = 6;

        img_obj[1] = findViewById(R.id.img_obj1);
        img_obj[1].setImageResource(R.drawable.blue_robber);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.18));
        img_objects_top[1]= (int) (screenHeight * 0.7);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("blue_robber");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
        img_obj[2].setImageResource(R.drawable.blue_bag);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.12));
        img_objects_top[2]= (int) (screenHeight * 0.75);
        img_objects_start[2] = (int) (screenWidth * 0.82);

        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("blue_bag");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.green_robber);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.18));
        img_objects_start[3] = (int) (screenWidth * 0.755);
        img_objects_top[3] = (int) (screenHeight * 0.7);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("green_robber");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.green_bag);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.12));
        img_objects_start[4] = (int) (screenWidth * 0.685);
        img_objects_top[4]= (int) (screenHeight * 0.75);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("green_bag");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.robber);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.18));
        img_objects_start[5] = (int) (screenWidth * 0.4);
        img_objects_top[5] = (int) (screenHeight * 0.73);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("robber");
        img_obj[5].setTextDirection(1);


        img_obj[6] = findViewById(R.id.img_obj6);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[6].setImageResource(R.drawable.black_bag);
        img_obj[6].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man6 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.12));
        img_objects_start[6] = (int) (screenWidth * 0.32);
        img_objects_top[6] = (int) (screenHeight * 0.79);
        lp_img_man6.topMargin = (img_objects_top[6]);
        lp_img_man6.setMarginStart(img_objects_start[6]);
        img_obj[6].setLayoutParams(lp_img_man6);
        img_obj[6].setContentDescription("black_bag");
        img_obj[6].setTextDirection(1);



        max_move_count=13;
        max_game_time=45;
        game_is_running=true;




    }
    private void level14_set() {
        obj_count = 6;

        img_obj[1] = findViewById(R.id.img_obj1);
        img_obj[1].setImageResource(R.drawable.dog);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.18));
        img_objects_top[1]= (int) (screenHeight * 0.7);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("dog_big");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
        img_obj[2].setImageResource(R.drawable.dog);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.14));
        img_objects_top[2]= (int) (screenHeight * 0.74);
        img_objects_start[2] = (int) (screenWidth * 0.8);

        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("dog_ave");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.dog);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.11));
        img_objects_start[3] = (int) (screenWidth * 0.73);
        img_objects_top[3] = (int) (screenHeight * 0.76);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("dog_small");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.cat);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.17));
        img_objects_start[4] = (int) (screenWidth * 0.65);
        img_objects_top[4]= (int) (screenHeight * 0.71);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("cat_big");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.cat);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.132));
        img_objects_start[5] = (int) (screenWidth * 0.57);
        img_objects_top[5] = (int) (screenHeight * 0.745);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("cat_ave");
        img_obj[5].setTextDirection(1);


        img_obj[6] = findViewById(R.id.img_obj6);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[6].setImageResource(R.drawable.cat);
        img_obj[6].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man6 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.11));
        img_objects_start[6] = (int) (screenWidth * 0.32);
        img_objects_top[6] = (int) (screenHeight * 0.79);
        lp_img_man6.topMargin = (img_objects_top[6]);
        lp_img_man6.setMarginStart(img_objects_start[6]);
        img_obj[6].setLayoutParams(lp_img_man6);
        img_obj[6].setContentDescription("cat_small");
        img_obj[6].setTextDirection(1);



        max_move_count=13;
        max_game_time=35;
        game_is_running=true;




    }
    private void level15_set() {
        obj_count = 6;

        img_obj[1] = findViewById(R.id.img_obj1);
        img_obj[1].setImageResource(R.drawable.white_king);
        img_obj[1].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.12), (int) (screenHeight * 0.18));
        img_objects_top[1]= (int) (screenHeight * 0.7);
        img_objects_start[1] = (int) (screenWidth * 0.880);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj[1].setLayoutParams(lp_img_farmer);
        img_obj[1].setContentDescription("white_king");
        img_obj[1].setTextDirection(1);

        img_obj[2] = findViewById(R.id.img_obj2);
        img_obj[2].setImageResource(R.drawable.white_quein);
        img_obj[2].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage  = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.14));
        img_objects_top[2]= (int) (screenHeight * 0.732);
        img_objects_start[2] = (int) (screenWidth * 0.82);

        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj[2].setLayoutParams(lp_img_cabbage);
        img_obj[2].setContentDescription("white_quein");
        img_obj[2].setTextDirection(1);


        img_obj[3] = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[3].setImageResource(R.drawable.white_pawn);
        img_obj[3].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.11));
        img_objects_start[3] = (int) (screenWidth * 0.76);
        img_objects_top[3] = (int) (screenHeight * 0.76);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj[3].setLayoutParams(lp_img_wolf);
        img_obj[3].setContentDescription("white_pawn");
        img_obj[3].setTextDirection(1);


        img_obj[4] = findViewById(R.id.img_obj4);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[4].setImageResource(R.drawable.black_king);
        img_obj[4].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_sheep = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.17));
        img_objects_start[4] = (int) (screenWidth * 0.68);
        img_objects_top[4]= (int) (screenHeight * 0.71);
        lp_img_sheep.topMargin = (img_objects_top[4]);
        lp_img_sheep.setMarginStart(img_objects_start[4]);
        img_obj[4].setLayoutParams(lp_img_sheep);
        img_obj[4].setContentDescription("black_king");
        img_obj[4].setTextDirection(1);

        img_obj[5] = findViewById(R.id.img_obj5);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[5].setImageResource(R.drawable.black_quien);
        img_obj[5].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man5 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.132));
        img_objects_start[5] = (int) (screenWidth * 0.62);
        img_objects_top[5] = (int) (screenHeight * 0.752);
        lp_img_man5.topMargin = (img_objects_top[5]);
        lp_img_man5.setMarginStart(img_objects_start[5]);
        img_obj[5].setLayoutParams(lp_img_man5);
        img_obj[5].setContentDescription("black_quien");
        img_obj[5].setTextDirection(1);


        img_obj[6] = findViewById(R.id.img_obj6);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj[6].setImageResource(R.drawable.black_pawn);
        img_obj[6].setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_man6 = new RelativeLayout.LayoutParams((int) (screenWidth * 0.1), (int) (screenHeight * 0.11));
        img_objects_start[6] = (int) (screenWidth * .555);
        img_objects_top[6] = (int) (screenHeight * 0.773);
        lp_img_man6.topMargin = (img_objects_top[6]);
        lp_img_man6.setMarginStart(img_objects_start[6]);
        img_obj[6].setLayoutParams(lp_img_man6);
        img_obj[6].setContentDescription("black_pawn");
        img_obj[6].setTextDirection(1);



        max_move_count=13;
        max_game_time=35;
        game_is_running=true;




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
    int
        remaining_time_level7=30;
    public boolean check_level7_cross()
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
            boot_passnger1=3;
        if(boat_passengers[2]==img_obj[2])
            boot_passnger2=3;

        if(boat_passengers[1]==img_obj[3])
            boot_passnger1=6;
        if(boat_passengers[2]==img_obj[3])
            boot_passnger2=6;

        if(boat_passengers[1]==img_obj[4])
            boot_passnger1=8;
        if(boat_passengers[2]==img_obj[4])
            boot_passnger2=8;

        if(boat_passengers[1]==img_obj[5])
            boot_passnger1=12;
        if(boat_passengers[2]==img_obj[5])
            boot_passnger2=12;


        int
            max_time = 0;
        if(boot_passnger1>=boot_passnger2)
            max_time=boot_passnger1;
        else
            max_time=boot_passnger2;
        if(remaining_time_level7>0)
        {
            remaining_time_level7-=max_time;

            if (remaining_time_level7>0) {
                result = true;
                show_message("زمان باقیمانده "+String.valueOf(remaining_time_level7)+" دقیقه می باشد ");
            } else
            {
                result = false;
                show_message("زمان تمام شده لطفا بازی مجدد را انتخاب کنید");
            }
        }
        else
        {
            result = false;
            show_message("زمان تمام شده لطفا بازی مجدد را انتخاب کنید");
        }












        return result;
    }

    public boolean check_level6_cross()
    {
        boolean result=true;


        //     Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();


        if(result)
        {
            if( boat_passengers[1]==img_obj[1] || boat_passengers[1]==img_obj[4] || boat_passengers[1]==img_obj[6] || boat_passengers[2]==img_obj[1] || boat_passengers[2]==img_obj[4] || boat_passengers[2]==img_obj[6] )
            {

            }
            else
            {
                result = false;
                show_message("کیف ها نمی توانند به تنهایی عبور کند");
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

            if ((img_obj[1].getTextDirection() == 1 && img_obj[4].getTextDirection() != 1 && img_obj[6].getTextDirection() !=1) || (img_obj[1].getTextDirection() == 2 && img_obj[4].getTextDirection() != 2 && img_obj[6].getTextDirection() !=2) ) {
                if(img_obj[5].getTextDirection() == img_obj[1].getTextDirection()) {
                    cant_cros_message = "مرد 1 نمی تواند با کیف سایرین تنها بماند";
                    show_message(cant_cros_message);
                    cant = true;
                }
            }

            if ((img_obj[1].getTextDirection() != 1 && img_obj[4].getTextDirection() == 1 && img_obj[6].getTextDirection() !=1) || (img_obj[1].getTextDirection() != 2 && img_obj[4].getTextDirection() == 2 && img_obj[6].getTextDirection() !=2) ) {
                if(img_obj[2].getTextDirection() == img_obj[4].getTextDirection() || img_obj[3].getTextDirection() == img_obj[4].getTextDirection()) {
                    cant_cros_message = "مرد 2 نمی تواند با کیف سایرین تنها بماند";
                    show_message(cant_cros_message);
                    cant = true;
                }
            }
            if ((img_obj[1].getTextDirection() != 1 && img_obj[4].getTextDirection() != 1 && img_obj[6].getTextDirection() ==1) || (img_obj[1].getTextDirection() != 2 && img_obj[4].getTextDirection() != 2 && img_obj[6].getTextDirection() ==2) ) {
                if(img_obj[2].getTextDirection() == img_obj[6].getTextDirection() || img_obj[3].getTextDirection() == img_obj[6].getTextDirection() || img_obj[5].getTextDirection() == img_obj[6].getTextDirection()) {
                    cant_cros_message = "مرد 3 نمی تواند با کیف سایرین تنها بماند";
                    show_message(cant_cros_message);
                    cant = true;
                }
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

    public boolean check_level8_cross()
    {
        boolean result=true;


        //     Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();




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

            int
                    police_count=0;
            int
                    robber_count=0;

            if(img_obj[1].getTextDirection()==1)
            {
                police_count++;
            }
            if(img_obj[2].getTextDirection()==1)
            {
                police_count++;
            }
            if(img_obj[3].getTextDirection()==1)
            {
                police_count++;
            }
            if(img_obj[4].getTextDirection()==1)
            {
                robber_count++;
            }
            if(img_obj[5].getTextDirection()==1)
            {
                robber_count++;
            }
            if(img_obj[6].getTextDirection()==1)
            {
                robber_count++;
            }

            if(robber_count>police_count && police_count!=0)
            {
                cant=true;
                show_message("تعداد دزدها بیشتر از پلیس ها می شود  ");
            }
            if(!cant)
            {
                police_count=0;
                robber_count=0;

                if(img_obj[1].getTextDirection()==2)
                {
                    police_count++;
                }
                if(img_obj[2].getTextDirection()==2)
                {
                    police_count++;
                }
                if(img_obj[3].getTextDirection()==2)
                {
                    police_count++;
                }
                if(img_obj[4].getTextDirection()==2)
                {
                    robber_count++;
                }
                if(img_obj[5].getTextDirection()==2)
                {
                    robber_count++;
                }
                if(img_obj[6].getTextDirection()==2)
                {
                    robber_count++;
                }

                if(robber_count>police_count && police_count!=0)
                {
                    cant=true;
                    show_message("تعداد دزدها بیشتر از پلیس ها می شود  ");
                }
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
    public boolean check_level9_cross()
    {
        boolean result=true;


        //     Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();





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




                //if(robber_count>police_count && police_count!=0)
            if ( img_obj[1].getTextDirection() != img_obj[2].getTextDirection() && (img_obj[3].getTextDirection() ==img_obj[2].getTextDirection() || img_obj[5].getTextDirection() == img_obj[2].getTextDirection() ))
                {
                    cant=true;
                    show_message("ورزشکار ۱ نمی تواند با سایر مربیان تنها بماند");
                }
                if ( img_obj[3].getTextDirection() != img_obj[4].getTextDirection() && (img_obj[1].getTextDirection() ==img_obj[4].getTextDirection() || img_obj[5].getTextDirection() == img_obj[4].getTextDirection() ))
                    {
                        cant=true;
                        show_message("ورزشکار 2 نمی تواند با سایر مربیان تنها بماند");
                    }
                    if ( img_obj[5].getTextDirection() != img_obj[6].getTextDirection() && (img_obj[1].getTextDirection() ==img_obj[6].getTextDirection() || img_obj[3].getTextDirection() == img_obj[6].getTextDirection() ))
                        {
                            cant=true;
                            show_message("ورزشکار 3 نمی تواند با سایر مربیان تنها بماند");
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






        return result;
    }
    public boolean check_level10_cross()
    {
        boolean result=true;


        //     Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();


        if(result)
        {
            if( boat_passengers[1]==img_obj[1] || boat_passengers[1]==img_obj[2] || boat_passengers[2]==img_obj[1] || boat_passengers[2]==img_obj[2]  )
            {

            }
            else
            {
                result = false;
                show_message("مادر و پسر سرخ پوش می توانند قایق را برانند");
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

            if ( img_obj[2].getTextDirection() != img_obj[1].getTextDirection() && img_obj[2].getTextDirection() ==img_obj[4].getTextDirection() )
            {
                cant=true;
                show_message("پسر 1 نمی تواند با مادر دیگری تنها بماند");
            }
            if ( img_obj[3].getTextDirection() != img_obj[1].getTextDirection() && img_obj[3].getTextDirection() ==img_obj[4].getTextDirection() && !cant)
            {
                cant=true;
                show_message("دختر 1 نمی تواند با مادر دیگری تنها بماند");
            }
            if ( img_obj[5].getTextDirection() != img_obj[4].getTextDirection() && img_obj[5].getTextDirection() ==img_obj[4].getTextDirection() )
            {
                cant=true;
                show_message("پسر 2 نمی تواند با مادر دیگری تنها بماند");
            }
            if ( img_obj[6].getTextDirection() != img_obj[4].getTextDirection() && img_obj[6].getTextDirection() ==img_obj[4].getTextDirection() && !cant)
            {
                cant=true;
                show_message("دختر 2 نمی تواند با مادر دیگری تنها بماند");
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
    public boolean check_level11_cross()
    {
        boolean result=true;




        if(result)
        {
            if( boat_passengers[1]==img_obj[1] || boat_passengers[1]==img_obj[4] || boat_passengers[2]==img_obj[1] || boat_passengers[2]==img_obj[4]  )
            {

            }
            else
            {
                result = false;
                show_message("فقط پدرها می توانند قایق را برانند");
            }
            if((boat_passengers[1]==img_obj[1] && boat_passengers[2]==img_obj[6]) || (boat_passengers[1]==img_obj[4] && boat_passengers[2]==img_obj[3]))
            {
                result = false;
                show_message("دخترها نمی توانند با پدر دیگری سوار قایق شوند");
            }
            if((boat_passengers[2]==img_obj[1] && boat_passengers[1]==img_obj[6]) || (boat_passengers[2]==img_obj[4] && boat_passengers[1]==img_obj[3]))
            {
                result = false;
                show_message("دخترها نمی توانند با پدر دیگری سوار قایق شوند");
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

            if  (img_obj[3].getTextDirection() != img_obj[1].getTextDirection() && img_obj[3].getTextDirection() !=img_obj[2].getTextDirection() && (img_obj[3].getTextDirection() ==img_obj[4].getTextDirection() || img_obj[3].getTextDirection() ==img_obj[5].getTextDirection() )  )
            {
                cant=true;
                show_message("دختر 1 نمی تواند با والدین دیگری تنها بماند");
            }
            if  (img_obj[6].getTextDirection() != img_obj[4].getTextDirection() && img_obj[6].getTextDirection() !=img_obj[5].getTextDirection() && (img_obj[6].getTextDirection() ==img_obj[2].getTextDirection() || img_obj[6].getTextDirection() ==img_obj[1].getTextDirection() ) && !cant )
            {
                cant=true;
                show_message("دختر 2 نمی تواند با والدین دیگری تنها بماند");
            }




            if (img_obj[3].getTextDirection() != img_obj[1].getTextDirection() && img_obj[3].getTextDirection() !=img_obj[2].getTextDirection() && !cant)
            {
                cant=true;
                show_message("دختر 1 نمی تواند تنها بماند");
            }
            if (img_obj[6].getTextDirection() != img_obj[4].getTextDirection() && img_obj[6].getTextDirection() !=img_obj[5].getTextDirection() && !cant)
            {
                cant=true;
                show_message("دختر 2 نمی تواند تنها بماند");
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

    public boolean check_level12_cross()
    {
        boolean result=true;




        if(result)
        {
            if( boat_passengers[1]==img_obj[1] || boat_passengers[1]==img_obj[3] || boat_passengers[1]==img_obj[4] || boat_passengers[2]==img_obj[1] || boat_passengers[2]==img_obj[3]|| boat_passengers[2]==img_obj[4]  )
            {

            }
            else
            {
                result = false;
                show_message("فقط اعضای خوانواده می توانند قایق را برانند");
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

            if  (img_obj[1].getTextDirection() != img_obj[2].getTextDirection() &&  (img_obj[2].getTextDirection() ==img_obj[3].getTextDirection() || img_obj[2].getTextDirection() ==img_obj[4].getTextDirection() || img_obj[2].getTextDirection() ==img_obj[5].getTextDirection() || img_obj[2].getTextDirection() ==img_obj[6].getTextDirection() || img_obj[2].getTextDirection() ==img_obj[7].getTextDirection() || img_obj[2].getTextDirection() ==img_obj[8].getTextDirection() )  )
            {
                cant=true;
                show_message("سگ بدون کشاورز با سایرین تنها نمی ماند");
            }
            if  (img_obj[3].getTextDirection() != img_obj[4].getTextDirection() &&  (img_obj[3].getTextDirection() ==img_obj[5].getTextDirection() || img_obj[3].getTextDirection() ==img_obj[6].getTextDirection()  )  )
            {
                cant=true;
                show_message("دختر در غیاب پسر خرگوش ها را اذیت می کند");
            }
            if  (img_obj[3].getTextDirection() != img_obj[4].getTextDirection() &&  (img_obj[4].getTextDirection() ==img_obj[7].getTextDirection() || img_obj[4].getTextDirection() ==img_obj[8].getTextDirection()  )  )
            {
                cant=true;
                show_message("پسر در غیاب دختر سنجاب ها را اذیت می کند");
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
    public boolean check_level13_cross()
    {
        boolean result=true;




        if(result)
        {
            if( boat_passengers[1]==img_obj[1] || boat_passengers[1]==img_obj[3] || boat_passengers[1]==img_obj[5] || boat_passengers[2]==img_obj[1] || boat_passengers[2]==img_obj[3]|| boat_passengers[2]==img_obj[5]  )
            {

            }
            else
            {
                result = false;
                show_message("فقط دزدان می توانند قایق را برانند");
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
            int
                    down_side_worth =0;
            if(img_obj[2].getTextDirection()==1)
                down_side_worth+=3000;
            if(img_obj[4].getTextDirection()==1)
                down_side_worth+=5000;
            if(img_obj[6].getTextDirection()==1)
                down_side_worth+=8000;
            int
                    up_side_worth =0;
            if(img_obj[2].getTextDirection()==2)
                up_side_worth+=3000;
            if(img_obj[4].getTextDirection()==2)
                up_side_worth+=5000;
            if(img_obj[6].getTextDirection()==2)
                up_side_worth+=8000;

            int
                    down_side_stolen =0;
            if(img_obj[1].getTextDirection()==1)
                down_side_stolen+=3000;
            if(img_obj[3].getTextDirection()==1)
                down_side_stolen+=5000;
            if(img_obj[5].getTextDirection()==1)
                down_side_stolen+=8000;
            int
                    up_side_stolen =0;
            if(img_obj[1].getTextDirection()==2)
                up_side_stolen+=3000;
            if(img_obj[3].getTextDirection()==2)
                up_side_stolen+=5000;
            if(img_obj[5].getTextDirection()==2)
                up_side_stolen+=8000;


//            Toast.makeText(this, String.valueOf(up_side_stolen), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, String.valueOf(up_side_worth), Toast.LENGTH_SHORT).show();

            if( down_side_stolen<down_side_worth && (img_obj[1].getTextDirection()==1 || img_obj[3].getTextDirection()==1 || img_obj[5].getTextDirection()==1    ))
            {
                cant=true;
                show_message("پول در پایین بیشتر از مبالغ دزدیده شده است");
            }
            if( up_side_stolen<up_side_worth && (img_obj[1].getTextDirection()==2 || img_obj[3].getTextDirection()==2 || img_obj[5].getTextDirection()==2    ) && !cant)
            {
                cant=true;
                show_message("پول در بالا بیشتراز مبالغ دزدیده شده است");
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
    public boolean check_level14_cross()
    {
        boolean result=true;




        if(result)
        {
            if( (boat_passengers[1]==img_obj[2] && (boat_passengers[2]==img_obj[1] || boat_passengers[2]==img_obj[3])) || (boat_passengers[2]==img_obj[2] && (boat_passengers[1]==img_obj[1] || boat_passengers[1]==img_obj[3])))
            {


                result = false;
                show_message("سگ متوسط باهمنوعانش در قایق نمی ماند");
            }
            if( (boat_passengers[1]==img_obj[5] && (boat_passengers[2]==img_obj[4] || boat_passengers[2]==img_obj[6])) || (boat_passengers[2]==img_obj[5] && (boat_passengers[1]==img_obj[4] || boat_passengers[1]==img_obj[6])))
            {


                result = false;
                show_message("گربه متوسط با همنوعانش در قایق نمی ماند");
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


            int down_cat=0,up_cat=0;
            int down_dog=0,up_dog=0;
            if(img_obj[1].getTextDirection()==1)
                down_dog++;
            else
                up_dog++;
            if(img_obj[2].getTextDirection()==1)
                down_dog++;
            else
                up_dog++;
            if(img_obj[3].getTextDirection()==1)
                down_dog++;
            else
                up_dog++;

            if(img_obj[4].getTextDirection()==1)
                down_cat++;
            else
                up_cat++;

            if(img_obj[5].getTextDirection()==1)
                down_cat++;
            else
                up_cat++;

            if(img_obj[6].getTextDirection()==1)
                down_cat++;
            else
                up_cat++;


            if(down_dog>down_cat && down_cat>0)
            {
                show_message("سگ ها گربه ها را در پایین خواهند خورد");
                cant=true;
            }
            if(up_dog>up_cat && !cant && up_cat>0)
            {
                show_message("سگ ها گربه ها را در بالا خواهند خورد");
                cant=true;
            }
            if(((img_obj[2].getTextDirection()==img_obj[1].getTextDirection() && img_obj[2].getTextDirection()!=img_obj[3].getTextDirection() && img_obj[2].getTextDirection()!=img_obj[4].getTextDirection() && img_obj[2].getTextDirection()!=img_obj[5].getTextDirection() && img_obj[2].getTextDirection()!=img_obj[6].getTextDirection()) || (img_obj[2].getTextDirection()==img_obj[3].getTextDirection() && img_obj[2].getTextDirection()!=img_obj[1].getTextDirection() && img_obj[2].getTextDirection()!=img_obj[4].getTextDirection() && img_obj[2].getTextDirection()!=img_obj[5].getTextDirection() && img_obj[2].getTextDirection()!=img_obj[6].getTextDirection())) && !cant)
            {
                show_message("گربه متوسط با همنوعانش تنها نمی ماند");
                cant=true;
            }
            if(((img_obj[5].getTextDirection()==img_obj[4].getTextDirection() && img_obj[5].getTextDirection()!=img_obj[6].getTextDirection() && img_obj[5].getTextDirection()!=img_obj[1].getTextDirection() && img_obj[5].getTextDirection()!=img_obj[2].getTextDirection() && img_obj[5].getTextDirection()!=img_obj[3].getTextDirection()) || (img_obj[5].getTextDirection()==img_obj[6].getTextDirection() && img_obj[5].getTextDirection()!=img_obj[4].getTextDirection() && img_obj[5].getTextDirection()!=img_obj[1].getTextDirection() && img_obj[5].getTextDirection()!=img_obj[2].getTextDirection() && img_obj[5].getTextDirection()!=img_obj[3].getTextDirection())) && !cant)
            {
                show_message("سگ متوسط با همنوعانش تنها نمی ماند");
                cant=true;
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
    public boolean check_level15_cross()
    {
        boolean result=true;




        if(result) {
            if ((boat_passengers[1] == img_obj[1] && (boat_passengers[2] != img_obj[2] && boat_passengers[2] != img_obj[3])) || (boat_passengers[2] == img_obj[1] && (boat_passengers[1] != img_obj[2] && boat_passengers[1] != img_obj[3]))) {


                result = false;
                show_message("شاه سفید بدون همراهانش حرکت نمی کند");
            }
            if ((result && boat_passengers[1] == img_obj[4] && (boat_passengers[2] != img_obj[5] && boat_passengers[2] != img_obj[6])) || (boat_passengers[2] == img_obj[4] && result && (boat_passengers[1] != img_obj[5] && boat_passengers[1] != img_obj[6]))) {


                result = false;
                show_message("شاه سیاه بدون همراهانش حرکت نمی کند");
            }
            if ((boat_passengers[1] == img_obj[2] && boat_passengers[2] != null && result) || (boat_passengers[2] == img_obj[2] && boat_passengers[1] != null && result)) {
                result = false;
                show_message("وزیر سفید باید تنها عبور کند");
            }
//            if ((boat_passengers[1] == img_obj[5]))
//            {
//                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
//            }
        
            if( (boat_passengers[1]==img_obj[5] && boat_passengers[2] !=null && result )|| (boat_passengers[2]==img_obj[5] && boat_passengers[1] !=null && result) )
            {
                result = false;
                show_message("وزیر سفید سیاه تنها عبور کند");
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



            if(img_obj[1].getTextDirection() != img_obj[2].getTextDirection() && img_obj[1].getTextDirection() != img_obj[3].getTextDirection() )
            {
                cant=true;
                show_message("شاه سفید نمی تواند تنها بماند");

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
        if(level_id==6) {
            result =check_level6_cross();
        }
        if(level_id==7) {
            result =check_level7_cross();
        }
        if(level_id==8) {
            result =check_level8_cross();
        }
        if(level_id==9) {
            result =check_level9_cross();
        }
        if(level_id==10) {
            result =check_level10_cross();
        }
        if(level_id==11) {
            result =check_level11_cross();
        }
        if(level_id==12) {
            result =check_level12_cross();
        }
        if(level_id==13) {
        result =check_level13_cross();
            }
        if(level_id==14) {
            result =check_level14_cross();
        }
        if(level_id==15) {
            result =check_level15_cross();
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
                txt_move.setText("حرکت "+String.valueOf(move_count));
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
                txt_move.setText("حرکت "+String.valueOf(move_count));
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
        if(level_id==6)
        {
            result = check_level6_finished();

        }
        if(level_id==7)
        {
            result = check_level4_finished();

        }
        if(level_id==8)
        {
            result = check_level6_finished();

        }
        if(level_id==9)
        {
            result = check_level6_finished();

        }
        if(level_id==10)
        {
            result = check_level6_finished();

        }
        if(level_id==11)
        {
            result = check_level6_finished();

        }
        if(level_id==12)
        {
            result = check_level12_finished();

        }
        if(level_id==13)
        {
            result = check_level6_finished();

        }
        if(level_id==14)
        {
            result = check_level6_finished();

        }
        if(level_id==15)
        {
            result = check_level6_finished();

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
            int
                str_cnt=1;
            ImageView img_star2=findViewById(R.id.img_star2);
            img_star2.setVisibility(View.VISIBLE);
            if(move_count>max_move_count)
                img_star2.setImageResource(R.drawable.question);
            else {
                img_star2.setImageResource(R.drawable.star);
                str_cnt++;
            }
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
            else {
                img_star3.setImageResource(R.drawable.star);
                str_cnt++;
            }
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


            Cursor resultSet = mydatabase.rawQuery("Select * from stars where id="+String.valueOf(level_id), null);

            if(resultSet.getCount()==1)
            {
                resultSet.moveToFirst();

                int
                    stars=resultSet.getInt(1);
                if(stars==0)
                {
                    if(str_cnt==1) {
                        set_coint_count(100, "add");
                        Toast.makeText(this, "100 سکه به سکه های شما افزوده شد", Toast.LENGTH_SHORT).show();
                    }
                    if(str_cnt==2) {
                        set_coint_count(200, "add");
                        Toast.makeText(this, "200 سکه به سکه های شما افزوده شد", Toast.LENGTH_SHORT).show();
                    }
                    if(str_cnt==3) {
                        set_coint_count(300, "add");
                        Toast.makeText(this, "300 سکه به سکه های شما افزوده شد", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            mydatabase.execSQL("update stars set star_count="+String.valueOf(str_cnt)+" where id="+String.valueOf(level_id));


            resultSet = mydatabase.rawQuery("Select * from finished_level where id=1", null);
            int finished_level = 0;
            if (resultSet.getCount() == 1) {
                resultSet.moveToFirst();
                finished_level = (resultSet.getInt(1));
             //   Toast.makeText(this, String.valueOf(finished_level)+"ee", Toast.LENGTH_SHORT).show();

            }
            mydatabase.execSQL("update stars set star_count="+String.valueOf(str_cnt)+" where id="+String.valueOf(level_id));
            if(level_id>finished_level)
            {
                mydatabase.execSQL("update finished_level set lvl_id="+String.valueOf(level_id)+" where id=1");
              //  Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();

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
    private boolean check_level6_finished()
    {
        boolean result=false;
        if(img_obj[1].getTextDirection()==2)
            if(img_obj[2].getTextDirection()==2)
                if(img_obj[3].getTextDirection()==2)
                    if(img_obj[4].getTextDirection()==2)
                        if(img_obj[5].getTextDirection()==2)
                            if(img_obj[6].getTextDirection()==2)
                                if(boat_passengers[1]==null)
                                    if(boat_passengers[2]==null)
                                        result=true;
        return result;
    }
    private boolean check_level12_finished()
    {
        boolean result=false;
        if(img_obj[1].getTextDirection()==2)
            if(img_obj[2].getTextDirection()==2)
                if(img_obj[3].getTextDirection()==2)
                    if(img_obj[4].getTextDirection()==2)
                        if(img_obj[5].getTextDirection()==2)
                            if(img_obj[6].getTextDirection()==2)
                                if(img_obj[7].getTextDirection()==2)
                                    if(img_obj[8].getTextDirection()==2)
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
                           //Toast.makeText(this, "o1k", Toast.LENGTH_SHORT).show();

                            lp_img_my_obj.topMargin = ((int) (screenHeight * 0.74)) - img_my_obj.getHeight(); //(get_top_obj_1(String.valueOf(img_my_obj.getContentDescription())));
                            lp_img_my_obj.setMarginStart((int) (screenWidth * 0.35));
                        } else if (boat_side.equals("up")) {

                            lp_img_my_obj.topMargin = ((int) (screenHeight * 0.74)) - img_my_obj.getHeight(); //(get_top_obj_1(String.valueOf(img_my_obj.getContentDescription())));
                            lp_img_my_obj.setMarginStart((int) (screenWidth * 0.35));
                            for (int j = 1; j <= 8; j++) {
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
                                for (int j = 1; j <= 8; j++) {
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
                            for (int j = 1; j <= 8; j++) {
                                if (img_in_top[j] == null) {
                                    RelativeLayout.LayoutParams lp_img_my_obj;
                                    lp_img_my_obj = (RelativeLayout.LayoutParams) img_my_obj.getLayoutParams();
                                    lp_img_my_obj.topMargin = ((int) (screenHeight * 0.60)) - img_my_obj.getHeight();
                                    if(level_id==12)
                                        lp_img_my_obj.setMarginStart(((int) (screenWidth * 0.1)) + ((int) (screenWidth * 0.07)) * j);
                                    else
                                        lp_img_my_obj.setMarginStart(((int) (screenWidth * 0.1)) + ((int) (screenWidth * 0.1)) * j);
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
    public void set_coint_count(int coint_cnt,String typ)
    {
        SQLiteDatabase mydatabase = openOrCreateDatabase(getResources().getString(R.string.database_name), MODE_PRIVATE, null);

        Cursor resultSet = mydatabase.rawQuery("Select * from coins where id=1", null);
        //Log.d("majid",String.valueOf(resultSet.getCount())+"1");
        if (resultSet.getCount() > 0) {
            resultSet.moveToFirst();
            //   et_guild_name.setText(resultSet.getString(1));
        }
        int
                new_cnt = Integer.parseInt(resultSet.getString(1));
        if(typ.equals("add"))
            new_cnt+=coint_cnt;
        else
            new_cnt-=coint_cnt;
        mydatabase.execSQL("update coins set coin_count="+String.valueOf(new_cnt)+" where id=1");
        TextView txt_coin = findViewById(R.id.txt_coin);
        txt_coin.setText(String.valueOf(new_cnt));

    }

    @Override
    protected void onResume() {
        super.onResume();
        int
                coin_cnt=get_coin_count();
        TextView txt_coin = findViewById(R.id.txt_coin);
        txt_coin.setText(String.valueOf(coin_cnt));


    }

    public void clk_help(View view) {


        if(get_coin_count()>=100) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            set_coint_count(100,"sub");
                            LinearLayout lay_rules = findViewById(R.id.lay_rules);
                            lay_rules.setBackground(getResources().getDrawable(R.drawable.help_panel));
                            lay_rules.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams lp_lay_rules = new RelativeLayout.LayoutParams((int) (screenWidth * .63), (int) (screenHeight * .8));
                            lp_lay_rules.topMargin = (int) (screenHeight * .050);
                            //lp_lay_finished.leftMargin =(int)(screenWidth*.5);
                            lp_lay_rules.setMarginStart((int) (screenWidth * .16));
                            lay_rules.setLayoutParams(lp_lay_rules);
                            LinearLayout lay_cover = findViewById(R.id.lay_cover);
                            lay_cover.setVisibility(View.VISIBLE);
                            TextView txt_rules = findViewById(R.id.txt_rules_body);

                            txt_rules.setText(level_helps[level_id]);
                            txt_rules.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.026));

                            txt_rules.setTypeface(tf);

                            lay_rules.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {

                                    float x = event.getX();
                                    float y = event.getY();

                                    switch (event.getAction()) {
                                        case MotionEvent.ACTION_DOWN:
                                            float zx = ((x / screenWidth));
                                            float zy = ((y / screenHeight));
                                            //  Toast.makeText(MainActivity.this, String.valueOf(zx)+"down"+String.valueOf(zy), Toast.LENGTH_SHORT).show();

                                            if (zx > .5674 && zx < 6178)
                                                if (zy > .04314 && zy < .1703) {
                                                    // Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                                    LinearLayout lay_rules = findViewById(R.id.lay_rules);
                                                    lay_rules.setVisibility(View.GONE);
                                                    LinearLayout lay_cover = findViewById(R.id.lay_cover);
                                                    lay_cover.setVisibility(View.GONE);
                                                    if (!game_is_running)
                                                        game_is_running = true;
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


                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked

                            break;

                    }
                }
            };


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("آیا می خواهید در ازای 100 سکه راهنمایی را ببینید").setPositiveButton("بله", dialogClickListener)
                    .setNegativeButton("خیر", dialogClickListener).show();
        }
        else
        {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked

                            startActivity(new Intent(getBaseContext()
                                    ,Store.class));

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked

                            break;

                    }
                }
            };


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("سکه های شما کافی نیست آیا می خواهید برای دریافت سکه رایگان و یا خرید سکه به فروشگاه بروید").setPositiveButton("بله", dialogClickListener)
                    .setNegativeButton("خیر", dialogClickListener).show();
        }

    }


    public void clk_again1(View view) {

        finish();
        startActivity(getIntent());
    }

    public void clk_back(View view) {
        finish();
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



                                txt_timer.setText(" زمان  "+String.valueOf((int)(time/60)) +":"+((int)(time%60)) );




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

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {



        public  String ss="",url="";





        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub

            //  dd=params[0];
            try {
                postData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Double result){

            //  pb.setVisibility(View.GONE);
            //   Toast.makeText(MainActivity.this, ss, Toast.LENGTH_SHORT).show();
            int
                    start=ss.indexOf("<output>");
            int
                    end=ss.indexOf("</output>");
            String
                    output_str="";
            if(end>0 && ss.length()>0) {
                output_str = ss.substring(start + 8, end);
                int
                        start1 = ss.indexOf("<param>");
                int
                        end1 = ss.indexOf("</param>");
                String
                        param_str = "";
                param_str = ss.substring(start1 + 7, end1);

//                if (param_str.equals("get_version")) {
//                    int
//                            i = 0;
//                    try {
//                        i = Integer.valueOf(output_str);
//                    } catch (Exception e1) {
//                        //    Log.d("majid", e1.getMessage()+"---"+ss+"---");
//                    }
//                    // Log.d("majid",String.valueOf(i));
//                    if (i > 0) {
//
//                        if (i != BuildConfig.VERSION_CODE) {
//                            Toast.makeText(getBaseContext(), getResources().getString(R.string.need_update_message), Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//                }
                if (param_str.equals("new_user")) {
                    start1 = ss.indexOf("<result>");
                    end1 = ss.indexOf("</result>");
                    String
                            result1 = "";
                    result1 = ss.substring(start1 + 8, end1);
                    //     Toast.makeText(getBaseContext(),result1,Toast.LENGTH_SHORT).show();
                }
                // Toast.makeText(getBaseContext(),param_str+"////"+output_str,Toast.LENGTH_SHORT).show();


            }





            //Toast.makeText(getBaseContext(),"ver="+http_result,Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(getBaseContext());
//            builder1.setTitle(getResources().getString(R.string.update));
//            builder1.setMessage(getResources().getString(R.string.need_update_message));
//            builder1.setCancelable(true);
//            builder1.setNeutralButton(android.R.string.ok,
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//



//            AlertDialog alert11 = builder1.create();
//            alert11.show();


        }

        protected void onProgressUpdate(Integer... progress){
            //pb.setProgress(progress[0]);
        }

        public void postData() throws IOException {
            HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
            HttpGet httpget = new HttpGet(url); // Set the action you want to do
            HttpResponse response = httpclient.execute(httpget); // Executeit
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) // Read line by line
                sb.append(line);

            String resString = sb.toString(); // Result is here
            ss = resString;
            //Log.d("majid", resString);
            is.close();
        }

    }

}
