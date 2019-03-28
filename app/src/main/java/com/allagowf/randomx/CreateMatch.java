package com.allagowf.randomx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateMatch extends AppCompatActivity {
    AQuery aq;
    long playertotalong= 0;
    Button busavematch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);
        aq=new  AQuery(this);
        busavematch = findViewById(R.id.BuSaveSetting);
        busavematch.setEnabled(false);
        if (CreationAbility()){
            busavematch.setEnabled(true);
        }else return;


    }

    public void busavematch(View view) {
        EditText PlayerNum =findViewById(R.id.EDTPlyerNum);
        EditText Matchprice =findViewById(R.id.EDTMatchPrice);
        String num = PlayerNum.getText().toString();
        String mprice = Matchprice.getText().toString();
        if (Long.valueOf(mprice) >= playertotalong){
            Toast.makeText(this, "Invalid Match Price \n check !!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Operations.NumIsValide(num)){
            Toast.makeText(this, "Duplicate Number  check !!", Toast.LENGTH_LONG).show();
            // PlayerNum.setText(null, TextView.BufferType.EDITABLE);
            // num =null;
            return;
        }

        else {

            SaveSettings.Player_Num = String.valueOf(num);
            SaveSettings.Match_Price = String.valueOf(mprice);



            String url = SaveSettings.ServerURL + "Playersws.asmx/CreateMatch?Host_ID=" + SaveSettings.Player_ID + "&Host_Num=" + PlayerNum.getText()+"&Match_Price="+Matchprice.getText();
            aq.ajax(url, JSONObject.class, this, "jsonCallback");


        }



    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) throws JSONException {

        if(json != null){
            //successful ajax call
            //successful ajax call

            long Match_ID=json.getLong("match_ID");
            if(Match_ID!=0){
                //  SaveSettings.Player_ID=String.valueOf(Player_ID);
                SaveSettings.Match_ID=String.valueOf(Match_ID);


                Intent intent=new Intent(this,Creationwait.class);
                intent.putExtra("calling-activity",SaveSettings.Create_ACTIVITY);
                startActivity(intent);

            }
            else {
                String msg=json.getString("Message");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }else{
            //ajax error
            Toast.makeText(getApplicationContext(), "ajax error", Toast.LENGTH_LONG).show();
            return;
        }

    }

    public boolean CreationAbility(){

            String url = SaveSettings.ServerURL + "Playersws.asmx/GetPlayerData?Player_ID=" + SaveSettings.Player_ID ;
            aq.ajax(url, JSONObject.class, this, "jsonCallbackPlayertotal");
            if (SaveSettings.Player_Total.equals("0")){
                Toast.makeText(this, "You can't creat or join \n any match for today comeback tomorrow!", Toast.LENGTH_LONG).show();
                return false;
                 }
            return true;

    }

    public void jsonCallbackPlayertotal(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call
            //successful ajax call



                playertotalong = json.getLong("PlayerTotal");
                SaveSettings.Player_Total=String.valueOf(playertotalong);

            }
            else {

                Toast.makeText(getApplicationContext(),"json is null for player total", Toast.LENGTH_LONG).show();
            }

            //json null
    }
}
