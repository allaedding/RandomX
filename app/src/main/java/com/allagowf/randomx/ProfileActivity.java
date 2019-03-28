package com.allagowf.randomx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    AQuery aq;
    TextView Playername;
    TextView PlayeTotal;
    TextView Playerwin;
    TextView Playerloss;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        aq=new  AQuery(this);
         Playername=findViewById(R.id.txt_PlayerName);
         PlayeTotal=findViewById(R.id.txt_Totale);
         Playerwin=findViewById(R.id.txt_wins);
         Playerloss=findViewById(R.id.txt_loss);

        String url = SaveSettings.ServerURL + "Playersws.asmx/GetPlayerData?Player_ID=" + SaveSettings.Player_ID ;
        aq.ajax(url, JSONObject.class, this, "jsonCallback");

    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call
            //successful ajax call

            long IsAny=json.getInt("IsAny");
            if(IsAny!=0){
                Playername.setText( json.getString("PlayerName"));
                PlayeTotal.setText(String.valueOf(json.getLong("PlayerTotal")));
                Playerwin.setText( String.valueOf(json.getLong("PlayerWins")));
                Playerloss.setText( String.valueOf(json.getLong("PlayerLoss")));


            }
            else {
                String msg=json.getString("Message");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }else{
            //json null
        }

    }

    public void bugohome(View view) {
        Intent intent=new Intent(this,XMenu.class);
        startActivity(intent);
    }
}
