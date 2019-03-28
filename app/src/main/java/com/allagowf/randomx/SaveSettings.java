package com.allagowf.randomx;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.Calendar;

/**
 * Created by guissous on 03-25-2018.
 */

public class SaveSettings {
    Context context;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs3" ;
    public static   String Player_ID = "0";
    public static   String Player_Num ="";
    public static   String Player_Name ="";
    public static   String Match_ID ="";
    public static   int Match_Status =1;
    public static   String Host_ID ="";
    public static   String Host_Num ="";
    public static   String Host_Name ="";
    public static   String Guest_ID ="";
    public static   String Guest_Num ="";
    public static   String Guest_Name ="";
    public static   String Guest_Total ="";
    public static   String Host_Total ="";
    public static   String Player_Total ="";
    public static   String Match_Price ="";
    public static   String Other_Rem_amount ="";
    public static   String Winner_ID ="";
    public static final int Join_ACTIVITY = 1001;
    public static final int Create_ACTIVITY = 1002;



    public static String ServerURL="http://modnarxppa-001-site1.itempurl.com/";

    public static String  APPURL="com.allaeddine.guissou.RandomX.Login";

    public  SaveSettings(Context context) {
        this.context=context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }


    public void SaveData()  {

        try

        {

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Player_ID",String.valueOf(Player_ID));
            editor.putString("Player_Name",String.valueOf(Player_Name));


            editor.commit();
            LoadData( );
        }

        catch( Exception e){}
    }






    public void LoadData( ) {

        String TempPlayerID=sharedpreferences.getString("Player_ID","empty");
        String TempPlayerName=sharedpreferences.getString("Player_Name","empty");


        if(!TempPlayerID.equals("empty")){
            Player_ID=TempPlayerID;// load user name
             Player_Name=TempPlayerName;}

        else {
            Intent intent=new Intent(context,Login.class);
            context.startActivity(intent);
        }


    }




}
