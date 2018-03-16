package com.sputa.rivercrossing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sputa.rivercrossing.util.IabHelper;
import com.sputa.rivercrossing.util.Inventory;
import com.sputa.rivercrossing.util.Purchase;
import com.sputa.rivercrossing.util.IabResult;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellAd;
import ir.tapsell.sdk.TapsellAdRequestListener;
import ir.tapsell.sdk.TapsellAdRequestOptions;
import ir.tapsell.sdk.TapsellAdShowListener;
import ir.tapsell.sdk.TapsellRewardListener;
import ir.tapsell.sdk.TapsellShowOptions;

public class Store extends AppCompatActivity {

    int screenWidth = 0;
    int screenHeight = 0;
    String
            font_name = "";

    public MyAsyncTask mm;

    Typeface tf;
    SQLiteDatabase mydatabase;

    static final String TAG = "bazar";

    // SKUs for our products: the premium upgrade (non-consumable)
    static final String SKU_CONS1000 = "coin1000";






    // The helper object
    IabHelper mHelper;
    int saw_ads=0;
    Inventory in;
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            in=inventory;
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
                return;

            }
            else {
                Log.d(TAG, "Query inventory was successful.");
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result+"----");
                Toast.makeText(getBaseContext(),"مشکل در خرید",Toast.LENGTH_SHORT).show();
                return;
            }
            else if (purchase.getSku().equals(SKU_CONS1000)) {
                Log.d(TAG, "Purchas successfull"+purchase.getSku());
                mHelper.consumeAsync(purchase,
                        mConsumeFinishedListener);
                purchase.getDeveloperPayload();
                Toast.makeText(getBaseContext(),"هوراااا... خرید با موفقیت انجام شد...1000 سکه به سکه های شما افزوده شد",Toast.LENGTH_LONG).show();
                set_coint_count(1000,"add");

                TextView txt_coin = findViewById(R.id.txt_coin);
                int coin_cnt = get_coin_count();
                txt_coin.setText(String.valueOf(coin_cnt));

            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        mydatabase = openOrCreateDatabase(getResources().getString(R.string.database_name), MODE_PRIVATE, null);

        font_name = "fonts/BYekan.ttf";
        tf = Typeface.createFromAsset(getAssets(),font_name );
        TextView txt_coin_free = findViewById(R.id.txt_coin_free);
        txt_coin_free.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.027));
        txt_coin_free.setTypeface(tf);

        TextView txt_video = findViewById(R.id.txt_video);
        txt_video.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.027));
        txt_video.setTypeface(tf);

        TextView txt_1000_coin = findViewById(R.id.txt_1000_coin);
        txt_1000_coin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.027));
        txt_1000_coin.setTypeface(tf);

        TextView txt_1000_toman = findViewById(R.id.txt_1000_toman);
        txt_1000_toman.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.027));
        txt_1000_toman.setTypeface(tf);

        TextView txt_coin = findViewById(R.id.txt_coin);
        txt_coin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.031));
        txt_coin.setTypeface(tf);


        LinearLayout.LayoutParams lp_lay_free = new LinearLayout.LayoutParams((int)(screenWidth*.6),(int)(screenHeight*.25));
        LinearLayout lay_free = findViewById(R.id.lay_free);
        lay_free.setLayoutParams(lp_lay_free);
        LinearLayout lay_1000tomani = findViewById(R.id.lay_1000tomani);
        lay_1000tomani.setLayoutParams(lp_lay_free);

        LinearLayout.LayoutParams lp_lay_btn = new LinearLayout.LayoutParams((int)(screenWidth*.16),(int)(screenHeight*.15));
        LinearLayout lay_video = findViewById(R.id.lay_video);
        lay_video.setLayoutParams(lp_lay_btn);
        LinearLayout lay_1000_toman = findViewById(R.id.lay_1000_toman);
        lay_1000_toman.setLayoutParams(lp_lay_btn);

        LinearLayout.LayoutParams lp_image = new LinearLayout.LayoutParams((int)(screenWidth*.13),(int)(screenHeight*.15));
        ImageView img_coin_free = findViewById(R.id.img_coin_free);
        img_coin_free.setLayoutParams(lp_image);
        ImageView img_1000_coin = findViewById(R.id.img_1000_coin);
        img_1000_coin.setLayoutParams(lp_image);

        LinearLayout.LayoutParams lp_img_coin = new LinearLayout.LayoutParams((int)(screenWidth*.09),(int)(screenHeight*.09));
        ImageView img_coin = findViewById(R.id.img_coin);
        img_coin.setLayoutParams(lp_img_coin);

        int coin_cnt = get_coin_count();
        txt_coin.setText(String.valueOf(coin_cnt));

        Tapsell.initialize(this, "dllpeknptfmclclpcdjpodcbcrohkbrhrfrgsqdeshbllqbojetsoqgmsfmigbjlgsbmjg");
        TapsellAdRequestOptions aa = new TapsellAdRequestOptions(2);
        Tapsell.requestAd(this, null, aa, new TapsellAdRequestListener() {
            @Override
            public void onError(String error) {
                Toast.makeText(Store.this, "بروز خطا", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdAvailable(TapsellAd ad) {
                isAvailable =1;
                ad1=ad;
                //    Toast.makeText(StoreActivity.this, "ویدیو آماده است", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNoAdAvailable() {
                Toast.makeText(Store.this, "تبلیغات در دسترس نیست", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoNetwork() {
                Toast.makeText(Store.this, "خطای شبکه", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExpiring(TapsellAd ad) {
                Toast.makeText(Store.this, "تبلیغات منقضی شده است", Toast.LENGTH_SHORT).show();
            }
        });









        try {
            String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwC2OEadTfjhSgG6SH7dvlEZlPZbLuCJ6qKhYqX/1vOWFFsZaS0xatgS9lrEHqsaNXYI8xqy2SeUjwuo4va2BFoaBHvoJ/K7R7L4vJD6b2VCbaMINoUevWi+Vyl11xBiiBjLhrahD4L2ndd6I7/FpyBkEb1Ws8m28rHEYio5KXZi+rV5sFkO86y06XfwmJQj21r+I0VGGGUeCbJ/bFiLl0F7S4jkkeEtd0kWR83XqK0CAwEAAQ==";
// You can find it in your Bazaar console, in the Dealers section.
// It is recommended to add more security than just pasting it in your source code;
            mHelper = new IabHelper(this, base64EncodedPublicKey);

            Log.d(TAG, "Starting setup.");
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.d(TAG, "Setup finished.");

                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    }
                    // Hooray, IAB is fully set up!
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                }
            });
        }
        catch (Exception e2){
            Toast.makeText(getBaseContext(),"خطا در ارتباط با پرداخت درون برنامه ای",Toast.LENGTH_SHORT).show();
        }
    }
    static final int RC_REQUEST = 100;


    private TapsellAd ad1;
    public int isAvailable =0;
    public void clk_1000(View view)
    {
        try {
            if (mHelper != null) mHelper.flagEndAsync();
            mHelper.launchPurchaseFlow(this, SKU_CONS1000, RC_REQUEST, mPurchaseFinishedListener, "payload-string");
        }catch (Exception e2){
            Toast.makeText(getBaseContext(),"خطا در ارتباط با پرداخت درون برنامه ای",Toast.LENGTH_SHORT).show();
        }


    }
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        // provision the in-app purchase to the user
                        // (for example, credit 50 gold coins to player's character)
                    }
                    else {
                        // handle error
                    }
                }
            };
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

    public void clk_seeAd(View view) {
        try {
            TapsellAdRequestOptions aa = new TapsellAdRequestOptions(2);
            Tapsell.requestAd(this, null, aa, new TapsellAdRequestListener() {
                @Override
                public void onError(String error) {
                    Toast.makeText(Store.this, "بروز خطا", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdAvailable(TapsellAd ad) {
                    isAvailable = 1;
                    ad1 = ad;

                }

                @Override
                public void onNoAdAvailable() {
                    Toast.makeText(Store.this, "تبلیغات در دسترس نیست", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNoNetwork() {
                    Toast.makeText(Store.this, "خطای شبکه", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onExpiring(TapsellAd ad) {
                    Toast.makeText(Store.this, "تبلیغات منقضی شده است", Toast.LENGTH_SHORT).show();
                }
            });
            TapsellShowOptions ss = new TapsellShowOptions();
            ss.setBackDisabled(false);
            ss.setRotationMode(TapsellShowOptions.ROTATION_LOCKED_LANDSCAPE);
            ss.setShowDialog(true);
            ad1.show(Store.this, ss, new TapsellAdShowListener() {
                @Override
                public void onOpened(TapsellAd ad) {
                    //      Toast.makeText(StoreActivity.this, "opened", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onClosed(TapsellAd ad) {
                    //       Toast.makeText(StoreActivity.this, "closed", Toast.LENGTH_SHORT).show();

                }
            });
            Tapsell.setRewardListener(new TapsellRewardListener() {
                @Override
                public void onAdShowFinished(TapsellAd ad, boolean completed) {
                    // store user reward if ad.isRewardedAd() and completed is true
                    if (completed) {
                        Toast.makeText(Store.this, "ممنون که ویدیو را نگاه کردید", Toast.LENGTH_SHORT).show();


                        set_coint_count(50, "add");
                        TextView txt_coin = findViewById(R.id.txt_coin);
                        int coin_cnt = get_coin_count();
                        txt_coin.setText(String.valueOf(coin_cnt));
                        //   Toast.makeText(getBaseContext(),"",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Store.this);
                        builder.setMessage("تعداد " + "50" + " سکه به سکه های شما افزوده شد")
                                .setCancelable(false)
                                .setPositiveButton("ممنونم", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }
            });
        }catch (Exception e3)
        {

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
