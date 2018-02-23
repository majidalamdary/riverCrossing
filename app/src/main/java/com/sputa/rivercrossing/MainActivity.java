package com.sputa.rivercrossing;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int screenWidth  = 0;
    int screenHeight = 0;
    String
        boat_side ="down";
    int
        boat_passenger_count=0;
    ImageView[] boat_passengers = new ImageView[4];
    ImageView[] img_objects = new ImageView[11];
    int[] img_objects_start = new int[11];
    int[] img_objects_top = new int[11];


    int
            boat_passenger1_start =0,boat_passenger1_top =0;
    int
            boat_passenger2_start =0,boat_passenger2_top =0;

    int
            obj1_top=0,obj1_start=0;
    int
            obj2_top=0,obj2_start=0;
    int
            obj3_top=0,obj3_start=0;
    int
            obj4_top=0,obj4_start=0;
    int
            obj5_top=0,obj5_start=0;
    int
            obj6_top=0,obj6_start=0;
    int
            obj7_top=0,obj7_start=0;
    int
            obj8_top=0,obj8_start=0;


    String
            obj1_name="",obj2_name="",obj3_name="",obj4_name="",obj5_name="",obj6_name="",obj7_name="",obj8_name="";
    int
        obj_count=0;
    ImageView
            img_obj1,img_obj2,img_obj3,img_obj4,img_obj5,img_obj6,img_obj7,img_obj8;
    ImageView img_boat;
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
        RelativeLayout.LayoutParams lp_img_boat = new RelativeLayout.LayoutParams((int)(screenWidth*0.3),(int)(screenHeight*0.3));
        lp_img_boat.topMargin = (int)(screenHeight*0.56);
        lp_img_boat.setMarginStart(((int)(screenWidth*0.33)));
        img_boat.setLayoutParams(lp_img_boat);

        level1_set();



    }
    private void level1_set()
    {


        obj_count=3;

        img_obj1 = findViewById(R.id.img_obj1);
   //     img_obj1.setBackgroundColor(Color.parseColor("#000000"));
        img_obj1.setImageResource(R.drawable.farmer);
        img_obj1.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_farmer = new RelativeLayout.LayoutParams((int)(screenWidth*.08),(int)(screenHeight*0.25));
        img_objects_top[1] = (int)(screenHeight*0.6);
        img_objects_start[1] = (int)(screenWidth*0.82);
        lp_img_farmer.topMargin = (img_objects_top[1]);
        lp_img_farmer.setMarginStart(img_objects_start[1]);
        img_obj1.setLayoutParams(lp_img_farmer);
        img_obj1.setContentDescription("farmer");
        img_objects[1]=img_obj1;


        img_obj2 = findViewById(R.id.img_obj2);
//        img_obj2.setBackgroundColor(Color.parseColor("#000000"));
        img_obj2.setImageResource(R.drawable.cabbage);
        img_obj2.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_cabbage = new RelativeLayout.LayoutParams((int)(screenWidth*0.08),(int)(screenHeight*0.12));
        img_objects_start[2] = (int)(screenWidth*0.725);
        img_objects_top[2] = ((int)(screenHeight*0.74));
        lp_img_cabbage.topMargin = (img_objects_top[2]);
        lp_img_cabbage.setMarginStart(img_objects_start[2]);
        img_obj2.setLayoutParams(lp_img_cabbage);
        img_obj2.setContentDescription("cabbage");
        img_objects[2]=img_obj2;


        img_obj3 = findViewById(R.id.img_obj3);
//        img_obj3.setBackgroundColor(Color.parseColor("#000000"));
        img_obj3.setImageResource(R.drawable.wolf);
        img_obj3.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp_img_wolf = new RelativeLayout.LayoutParams((int)(screenWidth*0.10),(int)(screenHeight*0.16));
        img_objects_start[3] = (int)(screenWidth*0.61);
        img_objects_top[3] = (int)(screenHeight*0.715);
        lp_img_wolf.topMargin = (img_objects_top[3]);
        lp_img_wolf.setMarginStart(img_objects_start[3]);
        img_obj3.setLayoutParams(lp_img_wolf);
        img_obj3.setContentDescription("wolf");
        img_objects[3]=img_obj3;

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

        int
                hight = img_boat.getHeight();
        int
                width = img_boat.getWidth();

        int new_top =  ((int)(screenHeight*0.37));
        int new_strt =((int)(screenWidth*0.55));
        img_boat.animate().x(new_strt).y(new_top).setDuration(500).start();

        if(boat_passengers[1]!=null) {
            hight = boat_passengers[1].getHeight();
            width = boat_passengers[1].getWidth();
            new_top =  ((int)(screenHeight*0.550))-hight;
            new_strt =((int)(screenWidth*0.8301))-width;
            boat_passengers[1].animate().x(new_strt).y(new_top).setDuration(500).start();
        }

        if(boat_passengers[2]!=null) {
            hight = boat_passengers[2].getHeight();
            width = boat_passengers[2].getWidth();
            new_top =  ((int)(screenHeight*0.550))-hight;
            new_strt =((int)(screenWidth*0.7301))-width;
            boat_passengers[2].animate().x(new_strt).y(new_top).setDuration(500).start();
        }
    }

    private int get_top_obj_1(String obj_name)
    {
    //    Toast.makeText(this, obj_name, Toast.LENGTH_SHORT).show();
        if(obj_name.equals("farmer"))
        {
            return (int) (screenHeight * 0.49);
        }
        if(obj_name.equals("cabbage"))
        {
            return (int) (screenHeight * 0.62);
        }
        if(obj_name.equals("wolf"))
        {
            return (int) (screenHeight * 0.59);
        }

        return 0;
    }

    public void clk_obj(View view) {
        ImageView
                img_my_obj = (ImageView) view;
        int
            is_passenger =0;
        for(int i=1;i<=2;i++)
        {
            if((boat_passengers[i]!=null)) {
                if ((boat_passengers[i]).equals(img_my_obj)) {
                    is_passenger = i;
                }
            }
        }
        int
                flag=0;
        boat_passenger_count=0;
        if(is_passenger==0) {
            if(boat_passengers[1]==null)
            {
                RelativeLayout.LayoutParams lp_img_my_obj;
                lp_img_my_obj = (RelativeLayout.LayoutParams) img_my_obj.getLayoutParams();
                if (boat_side.equals("down")) {
                    lp_img_my_obj.topMargin =((int) (screenHeight * 0.74))-img_my_obj.getHeight() ; //(get_top_obj_1(String.valueOf(img_my_obj.getContentDescription())));
                    lp_img_my_obj.setMarginStart((int) (screenWidth * 0.35));
                }
                img_my_obj.setLayoutParams(lp_img_my_obj);
                boat_passengers[1] = img_my_obj;
                flag=1;
            }
            if(boat_passengers[2]==null && flag==0)
            {
                RelativeLayout.LayoutParams lp_img_my_obj;
                lp_img_my_obj = (RelativeLayout.LayoutParams) img_my_obj.getLayoutParams();
                if (boat_side.equals("down")) {
                    lp_img_my_obj.topMargin =((int) (screenHeight * 0.74))-img_my_obj.getHeight() ; //(get_top_obj_1(String.valueOf(img_my_obj.getContentDescription())));
                    lp_img_my_obj.setMarginStart((int) (screenWidth * 0.45));
                }
                img_my_obj.setLayoutParams(lp_img_my_obj);
                boat_passengers[2] = img_my_obj;
                flag=1;
            }

            if(flag==0)
            {
                Toast.makeText(this, "ظرفیت خالی نیست", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            boat_passengers[is_passenger]=null;
            for(int i=1;i<=obj_count;i++)
            {
                if(img_objects[i].getContentDescription().toString().equals(img_my_obj.getContentDescription().toString()))
                {
                    //Toast.makeText(this, String.valueOf(img_objects[i].), Toast.LENGTH_SHORT).show();
                   // img_my_obj.setLayoutParams(lp_img_objects[i]);
                    RelativeLayout.LayoutParams lp_img_my_obj;

                    lp_img_my_obj = (RelativeLayout.LayoutParams) img_my_obj.getLayoutParams();

                    lp_img_my_obj.topMargin = img_objects_top[i];
                    lp_img_my_obj.setMarginStart(img_objects_start[i]);
                    img_my_obj.setLayoutParams(lp_img_my_obj);
                }
            }
        }





    }


}
