package com.allagowf.randomx;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WinActivity extends AppCompatActivity {
    ConstraintLayout layoutb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        layoutb = findViewById(R.id.ConLayout);



        int PlayerStatus = getIntent().getIntExtra("PlayerStatus", 0);
        switch (PlayerStatus) {
            case 1:

                layoutb.setBackgroundResource(R.drawable.backgroundwin);
                break;
            case 0:


                layoutb.setBackgroundResource(R.drawable.backgroundlose);
                break;
            default:
                Toast.makeText(getApplicationContext(),"default",Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void buGoprofile(View view) {
        Intent intent=new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }
}
