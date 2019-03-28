package com.allagowf.randomx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        aq = new AQuery(this);

    }

    public void busaveaccount(View view) {
        EditText PlayerName = (EditText) findViewById(R.id.EDTPlayerName);
        EditText Email = (EditText) findViewById(R.id.EDemail);
        EditText Password = (EditText) findViewById(R.id.EDTpassword);
        EditText PasswordR = (EditText) findViewById(R.id.EDTpasswordR);


        if (!Password.getText().toString().equals(PasswordR.getText().toString())) {

            Operations.DisplayMessage(this, getResources().getString(R.string.PasswordNotCorrect));
            return;
        }

        if (!isEmailValid(Email.getText().toString())) {

            Operations.DisplayMessage(this, getResources().getString(R.string.EmailNotCorrect));
            return;
        }

        if ((PlayerName.getText().length() < 2) || (Email.getText().length() < 2) || (Password.getText().length() < 2)) {

            Operations.DisplayMessage(this, getResources().getString(R.string.AddAllinfo));

            return;
        }

        SaveSettings.Player_Name = PlayerName.getText().toString();

        String url = SaveSettings.ServerURL + "Playersws.asmx/Register?Player_Name=" + PlayerName.getText() +"&Player_Email=" + Email.getText()  + " &Player_Password=" + Password.getText() ;

        aq.ajax(url, JSONObject.class, this, "jsonCallback");





    }

    public void jsonCallback(String url, JSONObject json, AjaxStatus status) throws JSONException {

        if (json != null) {
            //successful ajax call
            //successful ajax call
            String msg = json.getString("Message");

            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            int Isadd = json.getInt("IsAdded");
            if (Isadd == 1) {

                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
            }
        } else {
            //ajax error
        }

    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
