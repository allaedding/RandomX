package com.allagowf.randomx;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Creationwait extends AppCompatActivity {
    AQuery aq;
    EditText Guestnum;
    EditText Guestname;
    private Button gomatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creationwhite);

        aq=new  AQuery(this);

        Guestname=findViewById(R.id.etGestName);
        gomatch =findViewById(R.id.Bugomatch);
        gomatch.setEnabled(false);

        String host = SaveSettings.Player_ID;
        String match = SaveSettings.Match_ID;
        String playern = SaveSettings.Player_Num;


    }
    private void BringDAta(){

        String url = SaveSettings.ServerURL + "Playersws.asmx/GetMatchData?Match_ID=" + SaveSettings.Match_ID ;
        aq.ajax(url, JSONObject.class, this, "jsonCallback");


    }
    public void jsonCallback(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call
            //successful ajax call

            long GuestID=json.getLong("Guest_ID");
            if(GuestID!=0){
                SaveSettings.Match_Status = json.getInt("Match_Status");
                SaveSettings.Guest_ID = String.valueOf(json.getLong("Guest_ID"));
                SaveSettings.Guest_Name = String.valueOf(json.getString("Guest_Name"));
                SaveSettings.Guest_Num = String.valueOf(json.getInt("Guest_Num"));
                SaveSettings.Guest_Total = String.valueOf(json.getInt("Guest_Total"));

                gomatch.setEnabled(true);
                Guestname.setText(SaveSettings.Guest_Name, TextView.BufferType.EDITABLE);
                Guestnum.setText(SaveSettings.Guest_Num, TextView.BufferType.EDITABLE);


            }
            else {
                String msg=json.getString("Message");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "No Guest ", Toast.LENGTH_LONG).show();
        }

    }

    public void buagain(View view) {
        BringDAta();
    }


    public void buGoToMatch(View view) {
        Intent intent=new Intent(this,Board.class);
        intent.putExtra("calling-activity",SaveSettings.Create_ACTIVITY);
        startActivity(intent);
    }
}
