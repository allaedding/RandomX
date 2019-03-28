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
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinMatch extends AppCompatActivity {
    AQuery aq;
    public Button gotomatch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_match);
        aq=new  AQuery(this);
        gotomatch = findViewById(R.id.Bugomatch);
    }
    public void buGoToMatch(View view) {
        EditText PlayerNum =findViewById(R.id.EDTPlyerNum);
        String num = PlayerNum.getText().toString();
        SaveSettings.Guest_Num = num;

        if (!Operations.NumIsValide(num)){
            Toast.makeText(this, "Duplicate Number  check !!", Toast.LENGTH_LONG).show();
            // PlayerNum.setText(null, TextView.BufferType.EDITABLE);
            // num =null;
            return;
        }

        else {
            gotomatch.setEnabled(false);
            SaveSettings.Player_Num = String.valueOf(num);

            String url = SaveSettings.ServerURL + "Playersws.asmx/JoinMatch?Match_ID=" + SaveSettings.Match_ID + "&Guest_Num=" + PlayerNum.getText()+"&Guest_ID=" + SaveSettings.Player_ID;
            aq.ajax(url, JSONObject.class, this, "jsonCallback");

        }

    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) throws JSONException {

        if(json != null){
            //successful ajax call
            //successful ajax call

            int Host_Num=json.getInt("Host_Num");
            if(Host_Num!=0){
                 SaveSettings.Host_Num=String.valueOf(Host_Num);



                Intent intent=new Intent(this,Board.class);
                intent.putExtra("calling-activity",SaveSettings.Join_ACTIVITY);
                startActivity(intent);

            }
            else {
                String msg=json.getString("Message");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }else{
            //ajax error
            Toast.makeText(getApplicationContext(), "No Match Data !", Toast.LENGTH_LONG).show();
            return;
        }

    }

}
