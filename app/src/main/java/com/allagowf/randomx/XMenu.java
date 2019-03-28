package com.allagowf.randomx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class XMenu extends AppCompatActivity {
int DayOfWeek=0;
int currentDayOfWeek;
Calendar cal;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmenu);
        aq=new  AQuery(this);
        SaveSettings save=new SaveSettings(this);
        save.LoadData();
        DaillyTask();




    }


    public void DaillyTask(){
        cal = Calendar.getInstance();
        currentDayOfWeek = cal.get(Calendar.DAY_OF_MONTH);

        SharedPreferences sharedPreferences= this.getSharedPreferences("appInfo", 0);
        DayOfWeek = sharedPreferences.getInt("DayOfWeek", 0);

        if(DayOfWeek != currentDayOfWeek){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("DayOfWeek", currentDayOfWeek);
            editor.commit();
            // Your once a week code here

            TotaldailyUp();
        }
        else {return;}

    }



    public void TotaldailyUp(){
        // 2 stars to add a star and 0 to delete a star ** newamount to add it to the player total ** 1 or 0 to win and loss
        // newamount parameter accept negative values **

       String url = SaveSettings.ServerURL + "/Playersws.asmx/PlayerDataUpdate?Player_ID=" + SaveSettings.Player_ID + "&NewAmount=" + 500 + "&Stars=1&Win=0&Loss=0";
        aq.ajax(url, JSONObject.class, this, "jsonCallbackTotaldailyUp");

    }

    public void jsonCallbackTotaldailyUp(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call
            Toast.makeText(this,"successful Total update",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"error Total Not update",Toast.LENGTH_SHORT).show();
        }
    }



    public void buseeonline(View view) {
        Intent intent=new Intent(this,RandomX.class);
        startActivity(intent);
    }

    public void bucreatematch(View view) {
        Intent intent=new Intent(this,CreateMatch.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {

        moveTaskToBack(true);

    }

    public void bugoprofile(View view) {
        Intent intent=new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }


}
