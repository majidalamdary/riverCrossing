package com.sputa.rivercrossing;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.drm.DrmStore;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AndroidException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sputa.rivercrossing.app.Config;
import com.sputa.rivercrossing.util.NotificationUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import ir.adad.client.Adad;

public class Menu extends AppCompatActivity {
    int screenWidth = 0;
    int screenHeight = 0;
    String
            font_name = "";

    public MyAsyncTask mm;

    Typeface tf;
    SQLiteDatabase mydatabase;
    int
            coin_cnt;
    int
            star_cnt;
    int
            finished_level;
    public BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Adad.initialize(getApplicationContext());
        setContentView(R.layout.activity_menu);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, intent.getAction(), Toast.LENGTH_SHORT).show();
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    //  Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //  Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                }
            }
        };

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        font_name = "fonts/BYekan.ttf";
        tf = Typeface.createFromAsset(getAssets(),font_name );

         mydatabase = openOrCreateDatabase(getResources().getString(R.string.database_name), MODE_PRIVATE, null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS stars (id INT,star_count INT,star_desc VARCHAR)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS coins (id INT,coin_count INT,coin_desc VARCHAR)");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS finished_level (id INT,lvl_id INT,lvl_desc VARCHAR)");


        Cursor resultSet = mydatabase.rawQuery("Select * from stars", null);
        Log.d("majid",String.valueOf(resultSet.getCount()));

        if (resultSet.getCount() == 0) {
            for(int i=1;i<=15;i++)
            {
                mydatabase.execSQL("insert into stars (id, star_count,star_desc) values ("+String.valueOf(i)+",0,'') ");
            }
        }

         resultSet = mydatabase.rawQuery("Select * from coins", null);
        Log.d("majid",String.valueOf(resultSet.getCount()));

        if (resultSet.getCount() == 0) {

                mydatabase.execSQL("insert into coins (id, coin_count,coin_desc) values (1,200,'') ");
        }
        resultSet = mydatabase.rawQuery("Select * from finished_level", null);
        Log.d("majid",String.valueOf(resultSet.getCount()));

        if (resultSet.getCount() == 0) {

            mydatabase.execSQL("insert into finished_level (id, lvl_id,lvl_desc) values (1,0,'') ");
        }




        coin_cnt=get_coin_count();

        resultSet = mydatabase.rawQuery("Select sum(star_count) as sm from stars ", null);
        star_cnt = 0;
        if (resultSet.getCount() == 1) {
            resultSet.moveToFirst();
            star_cnt = (resultSet.getInt(0));

        }
        resultSet = mydatabase.rawQuery("Select * from finished_level where id=1", null);
        finished_level = 0;
        if (resultSet.getCount() == 1) {
            resultSet.moveToFirst();
            finished_level = (resultSet.getInt(1));
           // Toast.makeText(this, String.valueOf(finished_level)+"ee1", Toast.LENGTH_SHORT).show();

        }
        TextView txt_coin = findViewById(R.id.txt_coin);
        txt_coin.setText(String.valueOf(coin_cnt));

        TextView txt_star_count = findViewById(R.id.txt_star_count);
        txt_star_count.setText(String.valueOf(star_cnt));

        set_content();







        displayFirebaseRegId();

        mm =  new MyAsyncTask();

        {

            mm.url =  getResources().getString(R.string.site_url) +"do.php?param=logs&reg_id="+ URLEncoder.encode(regId)+"&type=log_in";

            mm.execute("");
        }

    }
    static boolean
            music_playing=true;
    String regId;
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
         regId = pref.getString("regId", null);

        Log.e("majid", "Firebase reg id: " + regId);
//        EditText ed1 = findViewById(R.id.editText2);
//        ed1.setText(regId);
        if (!TextUtils.isEmpty(regId)) {
      //       Toast.makeText(this, "Firebase Reg Id: " + regId, Toast.LENGTH_SHORT).show();
            mm =  new MyAsyncTask();

            {

                mm.url =  getResources().getString(R.string.site_url) +"do.php?param=new_user&gcm_id="+ URLEncoder.encode(regId)+"&user_info="+URLEncoder.encode(getDeviceName());

                mm.execute("");
            }
        }
//        else
//            Toast.makeText(this,"Firebase Reg Id is not received yet!", Toast.LENGTH_SHORT).show();
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        model += ("---"+sdkVersion + " (" + release +")");
        if (model.startsWith(manufacturer)) {
            return (model);
        } else {
            return (manufacturer) + " " + model;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

        coin_cnt=get_coin_count();
        TextView txt_coin = findViewById(R.id.txt_coin);
        txt_coin.setText(String.valueOf(coin_cnt));

        Cursor resultSet = mydatabase.rawQuery("Select sum(star_count) as sm from stars ", null);
        star_cnt = 0;
        if (resultSet.getCount() == 1) {
            resultSet.moveToFirst();
            star_cnt = (resultSet.getInt(0));

        }
        resultSet = mydatabase.rawQuery("Select * from finished_level where id=1", null);
        finished_level = 0;
        if (resultSet.getCount() == 1) {
            resultSet.moveToFirst();
            finished_level = (resultSet.getInt(1));

        }


        TextView txt_star_count = findViewById(R.id.txt_star_count);
        txt_star_count.setText(String.valueOf(star_cnt));
        set_content();
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {


            LinearLayout lay_main=findViewById(R.id.lay_main);
            enableDisableView(lay_main,false);
        LinearLayout lay_cover=findViewById(R.id.lay_cover);
        lay_cover.setVisibility(View.VISIBLE);


    }
    public  void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

        @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    int[] img_status = new int[16];
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
    private void set_content()
    {



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
        TextView lbl_level9 = findViewById(R.id.lbl_level9);
        lbl_level9.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level9.setTypeface(tf);
        TextView lbl_level10 = findViewById(R.id.lbl_level10);
        lbl_level10.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level10.setTypeface(tf);
        TextView lbl_level11 = findViewById(R.id.lbl_level11);
        lbl_level11.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level11.setTypeface(tf);
        TextView lbl_level12 = findViewById(R.id.lbl_level12);
        lbl_level12.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level12.setTypeface(tf);
        TextView lbl_level13 = findViewById(R.id.lbl_level13);
        lbl_level13.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level13.setTypeface(tf);
        TextView lbl_level14 = findViewById(R.id.lbl_level14);
        lbl_level14.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level14.setTypeface(tf);
        TextView lbl_level15 = findViewById(R.id.lbl_level15);
        lbl_level15.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        lbl_level15.setTypeface(tf);

        TextView txt_coin = findViewById(R.id.txt_coin);
        txt_coin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        txt_coin.setTypeface(tf);

        TextView txt_star_count = findViewById(R.id.txt_star_count);
        txt_star_count.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.03));
        txt_star_count.setTypeface(tf);

        ImageView img_coin = findViewById(R.id.img_coin);
        ConstraintLayout.LayoutParams lp_img_coin = (ConstraintLayout.LayoutParams) img_coin.getLayoutParams();
        lp_img_coin.width = (int) (screenWidth * 0.06);
        lp_img_coin.height = (int) (screenHeight * 0.1);
        img_coin.setLayoutParams(lp_img_coin);

        ImageView img_add = findViewById(R.id.img_add);
        ConstraintLayout.LayoutParams lp_img_add = (ConstraintLayout.LayoutParams) img_add.getLayoutParams();
        lp_img_add.width = (int) (screenWidth * 0.04);
        lp_img_add.height = (int) (screenHeight * 0.04);
        img_add.setLayoutParams(lp_img_add);


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
        RelativeLayout lay_level9=findViewById(R.id.lay_level9);
        lay_level9.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level10=findViewById(R.id.lay_level10);
        lay_level10.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level11=findViewById(R.id.lay_level11);
        lay_level11.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level12=findViewById(R.id.lay_level12);
        lay_level12.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level13=findViewById(R.id.lay_level13);
        lay_level13.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level14=findViewById(R.id.lay_level14);
        lay_level14.setLayoutParams(lp_img_move_button);
        RelativeLayout lay_level15=findViewById(R.id.lay_level15);
        lay_level15.setLayoutParams(lp_img_move_button);

        LinearLayout.LayoutParams lp_img_heart = new LinearLayout.LayoutParams((int) (screenWidth * 0.09), (int) (screenHeight * 0.09));
        ImageView img_heart = findViewById(R.id.img_heart);
        img_heart.setLayoutParams(lp_img_heart);


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
        ImageView img_level9 = findViewById(R.id.img_level9);
        img_level9.setLayoutParams(lp_img_level1);
        ImageView img_level10 = findViewById(R.id.img_level10);
        img_level10.setLayoutParams(lp_img_level1);
        ImageView img_level11 = findViewById(R.id.img_level11);
        img_level11.setLayoutParams(lp_img_level1);
        ImageView img_level12 = findViewById(R.id.img_level12);
        img_level12.setLayoutParams(lp_img_level1);
        ImageView img_level13 = findViewById(R.id.img_level13);
        img_level13.setLayoutParams(lp_img_level1);
        ImageView img_level14 = findViewById(R.id.img_level14);
        img_level14.setLayoutParams(lp_img_level1);
        ImageView img_level15 = findViewById(R.id.img_level15);
        img_level15.setLayoutParams(lp_img_level1);



        LinearLayout.LayoutParams lp_lay_message = new LinearLayout.LayoutParams((int) (screenWidth * 0.62), (int) (screenHeight * 0.6));

        LinearLayout lay_message=findViewById(R.id.lay_message);
        lay_message.setLayoutParams(lp_lay_message);
        TextView txt_exit_message = findViewById(R.id.txt_exit_message);
        txt_exit_message.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.034));
        txt_exit_message.setTypeface(tf);


        Button btn_stay=findViewById(R.id.btn_stay);
        LinearLayout.LayoutParams lp_btn_stay = new LinearLayout.LayoutParams((int) (screenWidth * 0.16), (int) (screenHeight * 0.12));
        btn_stay.setLayoutParams(lp_btn_stay);
        btn_stay.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.028));
        btn_stay.setTypeface(tf);

        Button btn_comment=findViewById(R.id.btn_comment);
        LinearLayout.LayoutParams lp_btn_comment = new LinearLayout.LayoutParams((int) (screenWidth * 0.16), (int) (screenHeight * 0.12));
        btn_comment.setLayoutParams(lp_btn_comment);
        btn_comment.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.028));
        btn_comment.setTypeface(tf);


        Button btn_exit=findViewById(R.id.btn_exit);
        LinearLayout.LayoutParams lp_btn_exit = new LinearLayout.LayoutParams((int) (screenWidth * 0.16), (int) (screenHeight * 0.12));
        btn_exit.setLayoutParams(lp_btn_exit);
        btn_exit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.028));
        btn_exit.setTypeface(tf);

        LinearLayout.LayoutParams lp_star11 = new LinearLayout.LayoutParams((int) (screenWidth * 0.08), (int) (screenHeight * 0.08));



         ImageView star_count= findViewById(R.id.img_star);
        ConstraintLayout.LayoutParams lp_img_star = (ConstraintLayout.LayoutParams) star_count.getLayoutParams();
        lp_img_star.width = (int) (screenWidth * 0.06);
        lp_img_star.height = (int) (screenHeight * 0.1);
        star_count.setLayoutParams(lp_img_star);


        ImageView[] star = new ImageView[160];

        star[11]= findViewById(R.id.star11);
        star[11].setLayoutParams(lp_star11);
        star[13]= findViewById(R.id.star12);
        star[13].setLayoutParams(lp_star11);
        star[12]= findViewById(R.id.star13);
        star[12].setLayoutParams(lp_star11);

        star[21]= findViewById(R.id.star21);
        star[21].setLayoutParams(lp_star11);
        star[23]= findViewById(R.id.star22);
        star[23].setLayoutParams(lp_star11);
        star[22]= findViewById(R.id.star23);
        star[22].setLayoutParams(lp_star11);

        star[31]= findViewById(R.id.star31);
        star[31].setLayoutParams(lp_star11);
        star[33]= findViewById(R.id.star32);
        star[33].setLayoutParams(lp_star11);
        star[32]= findViewById(R.id.star33);
        star[32].setLayoutParams(lp_star11);


        star[41]= findViewById(R.id.star41);
        star[41].setLayoutParams(lp_star11);
        star[43]= findViewById(R.id.star42);
        star[43].setLayoutParams(lp_star11);
        star[42]= findViewById(R.id.star43);
        star[42].setLayoutParams(lp_star11);

        star[51]= findViewById(R.id.star51);
        star[51].setLayoutParams(lp_star11);
        star[53]= findViewById(R.id.star52);
        star[53].setLayoutParams(lp_star11);
        star[52]= findViewById(R.id.star53);
        star[52].setLayoutParams(lp_star11);

        star[61]= findViewById(R.id.star61);
        star[61].setLayoutParams(lp_star11);
        star[63]= findViewById(R.id.star62);
        star[63].setLayoutParams(lp_star11);
        star[62]= findViewById(R.id.star63);
        star[62].setLayoutParams(lp_star11);

        star[71]= findViewById(R.id.star71);
        star[71].setLayoutParams(lp_star11);
        star[73]= findViewById(R.id.star72);
        star[73].setLayoutParams(lp_star11);
        star[72]= findViewById(R.id.star73);
        star[72].setLayoutParams(lp_star11);

        star[81]= findViewById(R.id.star81);
        star[81].setLayoutParams(lp_star11);
        star[83]= findViewById(R.id.star82);
        star[83].setLayoutParams(lp_star11);
        star[82]= findViewById(R.id.star83);
        star[82].setLayoutParams(lp_star11);

        star[91]= findViewById(R.id.star91);
        star[91].setLayoutParams(lp_star11);
        star[93]= findViewById(R.id.star92);
        star[93].setLayoutParams(lp_star11);
        star[92]= findViewById(R.id.star93);
        star[92].setLayoutParams(lp_star11);

        star[101]= findViewById(R.id.star101);
        star[101].setLayoutParams(lp_star11);
        star[103]= findViewById(R.id.star102);
        star[103].setLayoutParams(lp_star11);
        star[102]= findViewById(R.id.star103);
        star[102].setLayoutParams(lp_star11);

        star[111]= findViewById(R.id.star111);
        star[111].setLayoutParams(lp_star11);
        star[113]= findViewById(R.id.star112);
        star[113].setLayoutParams(lp_star11);
        star[112]= findViewById(R.id.star113);
        star[112].setLayoutParams(lp_star11);

        star[121]= findViewById(R.id.star121);
        star[121].setLayoutParams(lp_star11);
        star[123]= findViewById(R.id.star122);
        star[123].setLayoutParams(lp_star11);
        star[122]= findViewById(R.id.star123);
        star[122].setLayoutParams(lp_star11);

        star[131]= findViewById(R.id.star131);
        star[131].setLayoutParams(lp_star11);
        star[133]= findViewById(R.id.star132);
        star[133].setLayoutParams(lp_star11);
        star[132]= findViewById(R.id.star133);
        star[132].setLayoutParams(lp_star11);

        star[141]= findViewById(R.id.star141);
        star[141].setLayoutParams(lp_star11);
        star[143]= findViewById(R.id.star142);
        star[143].setLayoutParams(lp_star11);
        star[142]= findViewById(R.id.star143);
        star[142].setLayoutParams(lp_star11);

        star[151]= findViewById(R.id.star151);
        star[151].setLayoutParams(lp_star11);
        star[153]= findViewById(R.id.star152);
        star[153].setLayoutParams(lp_star11);
        star[152]= findViewById(R.id.star153);
        star[152].setLayoutParams(lp_star11);


        SQLiteDatabase mydatabase = openOrCreateDatabase(getResources().getString(R.string.database_name), MODE_PRIVATE, null);

        Cursor resultSet = mydatabase.rawQuery("Select * from stars", null);
        resultSet.moveToFirst();
        for(int i=1;i<=15;i++)
        {
            int
                    val= Integer.valueOf(resultSet.getString(1));
            if(val==1)
            {
                int
                    id= (i*10)+1;
                    star[id].setImageResource(android.R.drawable.btn_star_big_on);
            }
            if(val==2)
            {
                int
                        id= (i*10)+1;
                star[id].setImageResource(android.R.drawable.btn_star_big_on);
                id= (i*10)+2;
                star[id].setImageResource(android.R.drawable.btn_star_big_on);

            }
            if(val==3)
            {
                int
                        id= (i*10)+1;
                star[id].setImageResource(android.R.drawable.btn_star_big_on);
                id= (i*10)+2;
                star[id].setImageResource(android.R.drawable.btn_star_big_on);
                id= (i*10)+3;
                star[id].setImageResource(android.R.drawable.btn_star_big_on);

            }
            resultSet.moveToNext();

        }


    }
    boolean
            flag=true;
    private void clk_go_level_main(int lvl_id)
    {
        flag=true;
//        star_cnt=100;
//        finished_level=100;
        mm =  new MyAsyncTask();

        {

            mm.url =  getResources().getString(R.string.site_url) +"do.php?param=logs&reg_id="+ URLEncoder.encode(regId)+"&type=go_level-"+String.valueOf(lvl_id);

            mm.execute("");
        }
        if(lvl_id==2)
        {
            if(star_cnt<1 || finished_level<(lvl_id-1))
            {
                if(img_status[lvl_id]!=1) {
                    ImageView img_leve2 = findViewById(R.id.img_level2);
                    img_leve2.setImageResource(R.drawable.needs2);
                    img_status[lvl_id]=1;
                }
                else
                {
                    ImageView img_leve2 = findViewById(R.id.img_level2);
                    img_leve2.setImageResource(R.drawable.blue_knight);
                    img_status[lvl_id]=0;
                }
                flag=false;
            }

        }
        if(lvl_id==3)
        {
            if(star_cnt<4 || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level3);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs3);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.worker);
                    img_status[lvl_id]=0;
                }
                flag=false;
            }
        }
        if(lvl_id==4)
        {
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {

                ImageView img = findViewById(R.id.img_level4);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs4);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.man1);
                    img_status[lvl_id]=0;
                }
                flag=false;
            }
        }

        if(lvl_id==5)
        {
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
              //  Toast.makeText(this, String.valueOf(finished_level), Toast.LENGTH_SHORT).show();

                ImageView img = findViewById(R.id.img_level5);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs5);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.father1);
                    img_status[lvl_id]=0;
                }
                flag=false;
            }
        }
        if(lvl_id==6)
        {
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level6);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs6);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.man_red);
                    img_status[lvl_id]=0;
                }
                flag=false;
            }
        }
        if(lvl_id==7)
        {
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level7);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs7);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.train);
                    img_status[lvl_id]=0;
                }
                flag=false;
            }
        }
        if(lvl_id==8)
        {
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level8);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs8);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.robber);
                    img_status[lvl_id]=0;
                }
                flag=false;
            }
        }
        if(lvl_id==9)
        {
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level9);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs9);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.blue_coach);
                    img_status[lvl_id]=0;
                }
                flag=false;
            }
        }
        if(lvl_id==10)
        {
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level10);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs10);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.mother1);
                    img_status[lvl_id]=0;
                }
                flag=false;
            }
        }
        if(lvl_id==11)
        {
            flag=false;
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level11);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs11);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.father1);
                    img_status[lvl_id]=0;
                }

            }
            else
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String
                        lvl_state = pref.getString("level11",null);
                String
                        lvl_state_temp ="";
                if(lvl_state!=null)
                    lvl_state_temp=lvl_state;
                if(lvl_state_temp.equals("1"))
                {
                    Intent i = new Intent(this,MainActivity.class);
                    i.putExtra("lvl_id",String.valueOf(lvl_id));
                    startActivity(i);
                }
                else {
                    flag = false;
                    if (get_coin_count() >= 500) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                          set_coint_count(500,"sub");
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("level11", "1");
                                        editor.commit();
                                        Intent i = new Intent(getBaseContext(),MainActivity.class);
                                        i.putExtra("lvl_id","11");
                                        startActivity(i);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        flag = false;
                                        break;

                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("آیا می خواهید در ازای 500 سکه این مرحله را باز کنید").setPositiveButton("بله", dialogClickListener)
                                .setNegativeButton("خیر", dialogClickListener).show();
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked

                                        startActivity(new Intent(getBaseContext()
                                                , Store.class));

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked

                                        break;

                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("سکه های شما کافی نیست آیا می خواهید برای خرید سکه به فروشگاه بروید").setPositiveButton("بله", dialogClickListener)
                                .setNegativeButton("خیر", dialogClickListener).show();
                    }
                }
            }
        }
        if(lvl_id==12)
        {
            flag=false;
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level12);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs12);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.rabit);
                    img_status[lvl_id]=0;
                }

            }
            else
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String
                        lvl_state = pref.getString("level12",null);
                String
                        lvl_state_temp ="";
                if(lvl_state!=null)
                    lvl_state_temp=lvl_state;
                if(lvl_state_temp.equals("1"))
                {
                    Intent i = new Intent(this,MainActivity.class);
                    i.putExtra("lvl_id",String.valueOf(lvl_id));
                    startActivity(i);
                }
                else {
                    flag = false;
                    if (get_coin_count() >= 500) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        set_coint_count(500,"sub");
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("level12", "1");
                                        editor.commit();
                                        Intent i = new Intent(getBaseContext(),MainActivity.class);
                                        i.putExtra("lvl_id","12");
                                        startActivity(i);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        flag = false;
                                        break;

                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("آیا می خواهید در ازای 500 سکه این مرحله را باز کنید").setPositiveButton("بله", dialogClickListener)
                                .setNegativeButton("خیر", dialogClickListener).show();
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked

                                        startActivity(new Intent(getBaseContext()
                                                , Store.class));

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked

                                        break;

                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("سکه های شما کافی نیست آیا می خواهید برای خرید سکه به فروشگاه بروید").setPositiveButton("بله", dialogClickListener)
                                .setNegativeButton("خیر", dialogClickListener).show();
                    }
                }
            }
        }
        if(lvl_id==13)
        {
            flag=false;
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level13);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs13);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.bag);
                    img_status[lvl_id]=0;
                }

            }
            else
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String
                        lvl_state = pref.getString("level13",null);
                String
                        lvl_state_temp ="";
                if(lvl_state!=null)
                    lvl_state_temp=lvl_state;
                if(lvl_state_temp.equals("1"))
                {
                    Intent i = new Intent(this,MainActivity.class);
                    i.putExtra("lvl_id",String.valueOf(lvl_id));
                    startActivity(i);
                }
                else {
                    flag = false;
                    if (get_coin_count() >= 500) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        set_coint_count(500,"sub");
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("level13", "1");
                                        editor.commit();
                                        Intent i = new Intent(getBaseContext(),MainActivity.class);
                                        i.putExtra("lvl_id","13");
                                        startActivity(i);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        flag = false;
                                        break;

                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("آیا می خواهید در ازای 500 سکه این مرحله را باز کنید").setPositiveButton("بله", dialogClickListener)
                                .setNegativeButton("خیر", dialogClickListener).show();
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked

                                        startActivity(new Intent(getBaseContext()
                                                , Store.class));

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked

                                        break;

                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("سکه های شما کافی نیست آیا می خواهید برای خرید سکه به فروشگاه بروید").setPositiveButton("بله", dialogClickListener)
                                .setNegativeButton("خیر", dialogClickListener).show();
                    }
                }
            }
        }
        if(lvl_id==14)
        {
            flag=false;
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level14);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs14);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.cat);
                    img_status[lvl_id]=0;
                }

            }
            else
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String
                        lvl_state = pref.getString("level14",null);
                String
                        lvl_state_temp ="";
                if(lvl_state!=null)
                    lvl_state_temp=lvl_state;
                if(lvl_state_temp.equals("1"))
                {
                    Intent i = new Intent(this,MainActivity.class);
                    i.putExtra("lvl_id",String.valueOf(lvl_id));
                    startActivity(i);
                }
                else {
                    flag = false;
                    if (get_coin_count() >= 500) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        set_coint_count(500,"sub");
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("level14", "1");
                                        editor.commit();
                                        Intent i = new Intent(getBaseContext(),MainActivity.class);
                                        i.putExtra("lvl_id","14");
                                        startActivity(i);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        flag = false;
                                        break;

                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("آیا می خواهید در ازای 500 سکه این مرحله را باز کنید").setPositiveButton("بله", dialogClickListener)
                                .setNegativeButton("خیر", dialogClickListener).show();
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked

                                        startActivity(new Intent(getBaseContext()
                                                , Store.class));

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked

                                        break;

                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("سکه های شما کافی نیست آیا می خواهید برای خرید سکه به فروشگاه بروید").setPositiveButton("بله", dialogClickListener)
                                .setNegativeButton("خیر", dialogClickListener).show();
                    }
                }
            }

        }
        if(lvl_id==15)
        {
            flag=false;
            if(star_cnt<((lvl_id-1)*2) || finished_level<(lvl_id-1))
            {
                ImageView img = findViewById(R.id.img_level15);
                if(img_status[lvl_id]!=1) {

                    img.setImageResource(R.drawable.needs15);
                    img_status[lvl_id]=1;
                }
                else
                {
                    img.setImageResource(R.drawable.black_king);
                    img_status[lvl_id]=0;
                }

            }
            else
            {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String
                        lvl_state = pref.getString("level15",null);
                String
                        lvl_state_temp ="";
                if(lvl_state!=null)
                    lvl_state_temp=lvl_state;
                if(lvl_state_temp.equals("1"))
                {
                    Intent i = new Intent(this,MainActivity.class);
                    i.putExtra("lvl_id",String.valueOf(lvl_id));
                    startActivity(i);
                }
                else {
                    flag = false;
                    if (get_coin_count() >= 1000) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        set_coint_count(500,"sub");
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("level15", "1");
                                        editor.commit();
                                        Intent i = new Intent(getBaseContext(),MainActivity.class);
                                        i.putExtra("lvl_id","15");
                                        startActivity(i);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        flag = false;
                                        break;

                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("آیا می خواهید در ازای 1000 سکه این مرحله را باز کنید").setPositiveButton("بله", dialogClickListener)
                                .setNegativeButton("خیر", dialogClickListener).show();
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked

                                        startActivity(new Intent(getBaseContext()
                                                , Store.class));

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked

                                        break;

                                }
                            }
                        };


                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("سکه های شما کافی نیست آیا می خواهید برای خرید سکه به فروشگاه بروید").setPositiveButton("بله", dialogClickListener)
                                .setNegativeButton("خیر", dialogClickListener).show();
                    }
                }
            }
        }
        if(flag)
        {
            Intent i = new Intent(this,MainActivity.class);
            i.putExtra("lvl_id",String.valueOf(lvl_id));
            startActivity(i);
        }
    }

    public void clk_go_level1(View view) {
        clk_go_level_main(1);
    }
    public void clk_go_level2(View view) {
        clk_go_level_main(2);
    }
    public void clk_go_level3(View view) {
        clk_go_level_main(3);
    }
    public void clk_go_level4(View view) {
        clk_go_level_main(4);
    }
    public void clk_go_level5(View view) {
        clk_go_level_main(5);
    }
    public void clk_go_level6(View view) {
        clk_go_level_main(6);
    }
    public void clk_go_level7(View view) {
        clk_go_level_main(7);
    }
    public void clk_go_level8(View view) {
        clk_go_level_main(8);
    }
    public void clk_go_level9(View view) {
        clk_go_level_main(9);
    }
    public void clk_go_level10(View view) {
        clk_go_level_main(10);
    }
    public void clk_go_level11(View view) {
        clk_go_level_main(11);
    }

    public void clk_go_level12(View view) {
        clk_go_level_main(12);
    }
    public void clk_go_level13(View view) {
        clk_go_level_main(13);
    }
    public void clk_go_level14(View view) {
        clk_go_level_main(14);
    }
    public void clk_go_level15(View view) {
        clk_go_level_main(15);
    }

    public void clk_store(View view) {
        mm =  new MyAsyncTask();

        {

            mm.url =  getResources().getString(R.string.site_url) +"do.php?param=logs&reg_id="+ URLEncoder.encode(regId)+"&type=go_store";

            mm.execute("");
        }
        startActivity(new Intent(this,Store.class));
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
//        TextView txt_coin = findViewById(R.id.txt_coin);
//        txt_coin.setText(String.valueOf(new_cnt));

    }

    public void clk_heart(View view) {
        mm =  new MyAsyncTask();

        {

            mm.url =  getResources().getString(R.string.site_url) +"do.php?param=logs&reg_id="+ URLEncoder.encode(regId)+"&type=heart";

            mm.execute("");
        }
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setData(Uri.parse("bazaar://details?id=com.sputa.rivercrossing"));
        intent.setPackage("com.farsitel.bazaar");
        startActivity(intent);
    }

    public void clk_stay(View view) {
        LinearLayout lay_main=findViewById(R.id.lay_main);
        enableDisableView(lay_main,true);
        LinearLayout lay_cover=findViewById(R.id.lay_cover);
        lay_cover.setVisibility(View.GONE);

    }

    public void clk_exit(View view) {
        finish();
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

