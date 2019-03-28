package com.allagowf.randomx;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        aq=new  AQuery(this);
    }

    public void buloginckic(View view) {
        EditText PlayerName=(EditText)findViewById(R.id.EDTUserName);
        EditText Password=(EditText)findViewById(R.id.EDTpassword);
        if((PlayerName.getText().length()<2 )||(Password.getText().length()<2 ) ) {

            Operations.DisplayMessage(this, getResources().getString(R.string.AddAllinfo));

            return;
        }
        SaveSettings.Player_Name = PlayerName.getText().toString();
        String url=SaveSettings.ServerURL +"Playersws.asmx/Login?Player_Name="+ PlayerName.getText().toString() +"&Player_Password="+ Password.getText().toString();
        aq.ajax(url, JSONObject.class, this, "jsonCallback");
    }



    public void jsonCallback(String url, JSONObject json, AjaxStatus status) throws JSONException {

        if(json != null){
            //successful ajax call
            //successful ajax call

            long Player_ID=json.getLong("Player_ID");
            if(Player_ID!=0){
                long Player_Total=json.getLong("Player_Total");
                SaveSettings.Player_Total=String.valueOf(Player_Total);
                SaveSettings.Player_ID=String.valueOf(Player_ID);
                SaveSettings save=new SaveSettings(this);
                save.SaveData();

                Intent intent=new Intent(this,XMenu.class);
                startActivity(intent);

            }
            else {
                String msg=json.getString("Message");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }else{
            //ajax error
        }

    }

    public void burigsterclick(View view) {
        Intent intent=new Intent(this,SignUp.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);

    }


}
