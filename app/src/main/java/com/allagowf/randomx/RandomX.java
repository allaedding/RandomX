package com.allagowf.randomx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;


public class RandomX extends AppCompatActivity {
    AQuery aq;

    MyCustomAdapter myadapter;// match adapter
    ListView lsMatch;
    public   ArrayList<MatchTicketItem> listMatchData = new ArrayList<MatchTicketItem>();// match data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_x);
        aq=new AQuery(this);
        lsMatch=findViewById(R.id.LVMatches);
        listMatchData.add(0, new MatchTicketItem("loading_ticket","loading_ticket"," error 2","No_new_data","No_data"));

        myadapter=new MyCustomAdapter(listMatchData);
        View nooffer = getLayoutInflater().inflate(R.layout.match_ticket_no_match,null);
        lsMatch.addFooterView(nooffer);

        lsMatch.setAdapter(myadapter);//intisal with data
        String url = SaveSettings.ServerURL +"Playersws.asmx/GetOnlineMatches?";
        aq.ajax(url, JSONObject.class, this, "jsonCallbackOnlineMatches");



    }

    public void jsonCallbackOnlineMatches(String url, JSONObject json, AjaxStatus status) throws JSONException {

        if(json != null){

            try {


                int Tag = json.getInt("IsAny") ;
                if (Tag == 1)
                {// there is data

                  JSONArray newData = json.getJSONArray("MatchData");

                   // Toast.makeText(getApplicationContext(), String.valueOf(newData.length()), Toast.LENGTH_LONG).show();
                    for (int i = 0; i < newData.length(); i++) {
                        JSONObject newDataItem = newData.getJSONObject(i);

                        // laod news to list
                        // incomment this part after updating the webservice
                         listMatchData.add(new MatchTicketItem(String.valueOf(newDataItem.getLong("Match_ID")), newDataItem.getString("Host_Name"),String.valueOf(newDataItem.getLong("Host_Total")),String.valueOf(newDataItem.getLong("Host_ID")),String.valueOf(newDataItem.getLong("Match_Price"))));
                      //  ListplayingData.add(new MatchTicketItem(String.valueOf(newDataItem.getLong("Match_ID")), newDataItem.getString("player_Name"),String.valueOf(newDataItem.getLong("Player_Total")),String.valueOf(newDataItem.getLong("Host_ID"))));
                    }
                }
                else { // no more new data
                    listMatchData.add( new MatchTicketItem("No_new_data",getResources().getString(R.string.MsgnoOnlineMatches),"No_data" ,"No_data","No_data" ));
                     }

              //  listMatchData.remove(0);
                myadapter.notifyDataSetChanged();


            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "json error", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "ajax error", Toast.LENGTH_LONG).show();

        }

    }



    private class MyCustomAdapter extends BaseAdapter {
        public  ArrayList<MatchTicketItem>  listmatchsDataAdpater ;

        public MyCustomAdapter(ArrayList<MatchTicketItem>  listmatchsDataAdpater) {
            this.listmatchsDataAdpater=listmatchsDataAdpater;
        }

        @Override
        public int getCount() {
            return listmatchsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final  int position, View convertView, ViewGroup parent) {

            LayoutInflater mInflater = getLayoutInflater();
            final MatchTicketItem s=listmatchsDataAdpater.get(position);


            if( s.MatchID.equals("No_new_data"))
            {
                View myView = mInflater.inflate(R.layout.match_ticket_no_match, null);
                TextView txtMessage=myView.findViewById(R.id.txtMessage);
                txtMessage.setText(s.MatchID, TextView.BufferType.EDITABLE);
                return myView;
            }
            else  if(s.MatchID.equals("loading_ticket")) { //it is loading ticket

                View myView = mInflater.inflate(R.layout.match_ticket_loading, null);
                return myView;
            }
            else
            {

                View myView = mInflater.inflate(R.layout.match_ticket, null);


                TextView Name = myView.findViewById(R.id.txt_Host_Name);
                Name.setText(s.HostName, TextView.BufferType.EDITABLE);
                TextView Total =  myView.findViewById(R.id.txt_Host_Total);
                Total.setText(s.HostTotal, TextView.BufferType.EDITABLE);
                TextView matchPrice =  myView.findViewById(R.id.txt_Match_Price);
                matchPrice.setText(s.Match_Price, TextView.BufferType.EDITABLE);

                Button bujoin=myView.findViewById(R.id.bujoin);
                bujoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveSettings.Match_ID = String.valueOf(s.MatchID) ;
                        SaveSettings.Host_ID = String.valueOf(s.HostID);
                        SaveSettings.Host_Name = String.valueOf(s.HostName);
                        SaveSettings.Match_Price = String.valueOf(s.Match_Price);
                        SaveSettings.Host_Total = String.valueOf(s.HostTotal);
                        Intent intent=new Intent(getApplicationContext(),JoinMatch.class);

                        startActivity(intent);
                    }
                });
            return myView;



            }
        }

    }

    public void buXmenu(View view) {
        Intent intent=new Intent(this,XMenu.class);
        startActivity(intent);
    }

    public void buAddMatch(View view) {
        Intent intent=new Intent(this,CreateMatch.class);
        startActivity(intent);
    }
}
