package com.allagowf.randomx;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Board extends AppCompatActivity {
    AQuery aq;
    //***************seek bar declaration *************************
   SeekBar seek_bar;
   TextView testprice;
    int min=5,current =10, max=Integer.valueOf(SaveSettings.Match_Price); //set the max to the match price
    //***************seek bar declaration end *************************


     MyCustomAdapter myadapter;// match adapter
     ListView lsplaying; //
      public   ArrayList<Playing_Ticket_Item> ListplayingData = new ArrayList<Playing_Ticket_Item>();// match data


    //*************** declaration *************************************
    TextView playername;
    TextView playerTT;
  //  TextView playerstars;
    EditText TestingNb;
    TextView playmony;
    String MatchID ;
    String OtherID;
    String OtherN;
    String Othertotale;
    int Player;

    int OtherRemamount;
    int winprize=0;
    int lossamount = 0;

    int[] TestingNum = new int[4];
    int[] OtherNum = new int[4];
    boolean IsPay = false;
    int Totaltestingprice = 0;
    int testingprice = 0 ;
    int PlaymonyInt = 0 ;
    int playertotalint = 0;
    int cptRight = 0;
    int cptPosition = 0;
    String testednum = "0";
    int playmoneyint = 0;
    int otherTotalint = 0;

    //*************** declaration *************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        aq=new  AQuery(this);



         lsplaying = findViewById(R.id.LVplaying);
         playername=findViewById(R.id.player_name);
        playerTT=findViewById(R.id.playertt);
        TestingNb =findViewById(R.id.etTest_num);
         playmony =findViewById(R.id.playermony);
         MatchID =SaveSettings.Match_ID;
        //playerTT.setText(SaveSettings.Player_Total, TextView.BufferType.EDITABLE);
        GetPlayerTotal();
//*****************************************************assigning values according to calling activity *************
        int callingActivity = getIntent().getIntExtra("calling-activity", 0);
        switch (callingActivity) {
            case SaveSettings.Create_ACTIVITY:
             // Define other player num as GuestNum
                playername.setText(SaveSettings.Player_Name, TextView.BufferType.EDITABLE);

                OtherID=SaveSettings.Guest_ID;
                OtherN = SaveSettings.Guest_Num;
                Othertotale = SaveSettings.Guest_Total;
                OtherRemamount = 2;
                Player = 1;
                playeRemamountUp(1,PlaymonyInt);


                Toast.makeText(this,"Create_ACTIVITY",Toast.LENGTH_LONG).show();
                break;

                case SaveSettings.Join_ACTIVITY:
                    // Define other player num as HostNum

                    playername.setText(SaveSettings.Player_Name, TextView.BufferType.EDITABLE);

                    OtherID = SaveSettings.Host_ID;
                    OtherN = SaveSettings.Host_Num;
                    Othertotale = SaveSettings.Host_Total;
                    OtherRemamount = 1;
                    Player = 2;
                    playeRemamountUp(2,PlaymonyInt);
                    Toast.makeText(this, "Join_ACTIVITY", Toast.LENGTH_LONG).show();

                break;
            default:
                Toast.makeText(this,"There Is No Data !",Toast.LENGTH_LONG).show();
                break;
        }
//*****************************************************assigning values according to calling activity *************

        OtherNum = StrArray(OtherN);
        playmony.setText(SaveSettings.Match_Price,TextView.BufferType.EDITABLE);
        PlaymonyInt=Integer.valueOf(SaveSettings.Match_Price);
        playeRemamountUp(Player,PlaymonyInt);
        boolean temp = otherplayerloss();

        //*****************************************************************************

        seekbarr();

      //  ListplayingData.add(0, new Playing_Ticket_Item("","",""));

        myadapter=new MyCustomAdapter(ListplayingData);
        View defaulticket = getLayoutInflater().inflate(R.layout.playing_list_title,null);
        lsplaying.addHeaderView(defaulticket);
        lsplaying.setAdapter(myadapter);//intisal with data





    }

    //********************************On create End *************************************


    public void seekbarr(){
        testprice =findViewById(R.id.txtdisplayprice);
        seek_bar = findViewById(R.id.SBPrice);

       seek_bar.setMax(max-min);
        seek_bar.setProgress(current-min);
        testprice.setText(""+current);


        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                current = progress+min;
                testprice.setText(""+current);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void Butry(View view) {
        if (!(otherplayerloss())) {
            cptRight = 0;
            cptPosition = 0;
            winprize = 0;
            testednum = TestingNb.getText().toString();
            boolean numvalid = Operations.NumIsValide(testednum);
            if (!numvalid) {
                Toast.makeText(this, "You Number is not valid", Toast.LENGTH_LONG).show();
                IsPay = true;
                return;
            }

            if (IsPay) {
                TestingNum = StrArray(testednum);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (TestingNum[i] == OtherNum[j]) {
                            cptRight++;
                            if (i == j) {
                                cptPosition++;
                            }
                        }
                    }
                }
                ListplayingData.add(new Playing_Ticket_Item(String.valueOf(testednum), String.valueOf(cptRight), String.valueOf(cptPosition)));
                // setDAta(String.valueOf(testednum), String.valueOf(cptRight), String.valueOf(cptPosition));
                myadapter.notifyDataSetChanged();
                IsPay = false;

                if (cptRight == 4 && cptPosition == 4) {
                    //player win ******** case 1
                    playertotalint = Integer.valueOf(playerTT.getText().toString());
                    playmoneyint = Integer.valueOf(playmony.getText().toString());
                    Totaltestingprice = Integer.valueOf(SaveSettings.Match_Price) - playmoneyint;
                    winprize = Totaltestingprice * 10;


                    otherTotalint = Integer.valueOf(Othertotale);
                    if (otherTotalint <= winprize) {
                        winprize = otherTotalint;

                     //   playerTT.setText("" + winprize);
                    } // here the winner update his and the other profile "the loser" also : he know how match he win
                    PlayerWin();
                  //  Toast.makeText(this, "winprize=="+winprize, Toast.LENGTH_LONG).show();
                    otherplayerlossDataUp(); // it receive the winprize negatiflly insteaad of lossamount!
                    playerDataUp(1); //player win ******** case 2
                    gotowin(1);



                    //end match

                }

            }else  if (!IsPay) {
                Toast.makeText(this, "you need to pay for this test!", Toast.LENGTH_LONG).show();

            }
            } else if (otherplayerloss()) {
                PlayerWin(); // not necessary
                winprize = Integer.valueOf(Othertotale);
                playerDataUp(1);
                gotowin(1);
            }





    }

    public void Bupay(View view) {
      testingprice = Integer.valueOf(testprice.getText().toString());
       PlaymonyInt = Integer.valueOf(this.playmony.getText().toString());
        lossamount = 0;
        if (!otherplayerwin()){
        if (PlaymonyInt == 0) {
            //player loss ********* case 1
             playertotalint = Integer.valueOf(playerTT.getText().toString());
            lossamount = playertotalint; //here winpriz is for the other player
            //send it with negatif value to deceament it

            playerDataUp(0); // zero its mean he loss
            gotowin(0);
            // uploadplayersData(playerId,total,win||loss);
           // playerTT.setText("0");

            //end match
            return;
        }else if (testingprice <= PlaymonyInt) {          //***************here******

            PlaymonyInt -= testingprice;
            this.playmony.setText(""+PlaymonyInt);
            Totaltestingprice = +Totaltestingprice;
            IsPay = true;
            playeRemamountUp(Player,PlaymonyInt);
        } else if (testingprice > PlaymonyInt){
            Toast.makeText(this, "value more than \n what you can Pay !", Toast.LENGTH_LONG).show();
        }

        }else if (otherplayerwin()){
            //call getotherRemmaining amount ******************************* here *****************
           /*
            int othertestingprice =Integer.valueOf(SaveSettings.Match_Price) - Integer.valueOf(SaveSettings.Other_Rem_amount);
            lossamount = othertestingprice*10;
            playertotalint = Integer.valueOf(playerTT.getText().toString());
            if(lossamount >=playertotalint ){
                lossamount = playertotalint;
            }
            */
           // playerDataUp(0); the update is done by the winner !
            gotowin(0);
        }

    }




    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<Playing_Ticket_Item> listplayingDataAdpater;

        public MyCustomAdapter(ArrayList<Playing_Ticket_Item>  listPlayingDataAdpater) {
            this.listplayingDataAdpater =listPlayingDataAdpater;
        }

        @Override
        public int getCount() {
            return listplayingDataAdpater.size();
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
            final Playing_Ticket_Item ls= listplayingDataAdpater.get(position);


            if( ls.num.equals("No_new_data"))
            {
                View myView = mInflater.inflate(R.layout.match_ticket_no_match, null);

                return myView;
            }

            else {

                View myView = mInflater.inflate(R.layout.playing_ticket, null);
                TextView num = myView.findViewById(R.id.txt_num);
                num.setText(ls.num, TextView.BufferType.EDITABLE);
                TextView right =  myView.findViewById(R.id.txt_right);
                right.setText(ls.right, TextView.BufferType.EDITABLE);
                TextView positon =  myView.findViewById(R.id.txt_position);
                positon.setText(ls.positon, TextView.BufferType.EDITABLE);


                return myView;

            }


        }

    }
    public void setDAta(String num,String right,String position){

       // ListplayingData.add(cpt, new Playing_Ticket_Item("loading_ticket","loading_ticket"," error 2"));

        ListplayingData.add(new Playing_Ticket_Item(num,right,position));

    }
    public int[] StrArray(String stremp){

        int[] newGuess = new int[stremp.length()];
        for (int i = 0; i < stremp.length(); i++)
        {
            newGuess[i] = stremp.charAt(i) - '0';
        }
        return newGuess ;
    }


    /******************************************************************************************/
    public boolean otherplayerwin(){
        String url = SaveSettings.ServerURL + "Playersws.asmx/GetMatchData?Match_ID=" + SaveSettings.Match_ID ;
        aq.ajax(url, JSONObject.class, this, "jsonCallbackwinnerID");

        if (!(SaveSettings.Winner_ID.equals("0"))){
            if (OtherID.equals(SaveSettings.Winner_ID)){
               return true;
            }

        }
        return false;
    }

    public void jsonCallbackwinnerID(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call

             SaveSettings.Winner_ID = String.valueOf(json.getLong("Winner_ID"));
         } else{
        Toast.makeText(getApplicationContext(), "json is null ", Toast.LENGTH_LONG).show();
    }


    }

    public boolean otherplayerloss(){
        String url = SaveSettings.ServerURL + "Playersws.asmx/GetMatchData?Match_ID=" + SaveSettings.Match_ID ;
        aq.ajax(url, JSONObject.class, this, "jsonCallbackRem_other_amount");
        if (SaveSettings.Other_Rem_amount.equals("0")){
                return true;
        }else{
            return false;
        }

    }

    public void jsonCallbackRem_other_amount(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call
            switch (OtherRemamount) {
                case 1:
                    SaveSettings.Other_Rem_amount = String.valueOf(json.getLong("Host_Rem_amount"));
                    break;
                case 2:
                    SaveSettings.Other_Rem_amount = String.valueOf(json.getLong("Guest_Rem_amount"));
                    break;
                default:
                    SaveSettings.Other_Rem_amount ="0";
                    break;
            }
        }else{
            Toast.makeText(getApplicationContext(), "json is null ", Toast.LENGTH_LONG).show();
        }

    }


    public void playeRemamountUp(int palyer, int playeRemamount){
        String url="";
        switch (palyer){
            case 1:
                url = SaveSettings.ServerURL + "/Playersws.asmx/MatchDataHostRemUpdate?Match_ID="+SaveSettings.Match_ID+"&Host_Rem_amount="+playeRemamount;
                aq.ajax(url, JSONObject.class, this, "jsonCallbackplayeRemamountUp");
                break;

            case 2:
                url = SaveSettings.ServerURL + "/Playersws.asmx/MatchDataGuestRemUpdate?Match_ID="+SaveSettings.Match_ID+"&Guest_Rem_amount="+playeRemamount;
                aq.ajax(url, JSONObject.class, this, "jsonCallbackplayeRemamountUp");
                break;

            default:
                Toast.makeText(getApplicationContext(),"no such a case",Toast.LENGTH_LONG).show();
                break;

        }
    }

    public void jsonCallbackplayeRemamountUp(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call

            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getApplicationContext(), "json is null ", Toast.LENGTH_LONG).show();
        }

    }


    public void PlayerWin(){

        String url = SaveSettings.ServerURL + "/Playersws.asmx/MatchDataWinnerUpdate?Match_ID="+SaveSettings.Match_ID+"&Winner_ID="+SaveSettings.Player_ID;
            aq.ajax(url, JSONObject.class, this, "jsonCallbackWinnerUpdate");

    }

    public void jsonCallbackWinnerUpdate(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call

            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext(), "json is null ", Toast.LENGTH_LONG).show();
        }

    }


    public void playerDataUp(int palyerstatus){
         String url;
         //if he win
        switch (palyerstatus) {

            case 1:
                // 2 stars to add a star and 0 to delete a star ** newamount to add it to the player total ** 1 or 0 to win and loss
                // newamount parameter accept negative values **
             url = SaveSettings.ServerURL + "/Playersws.asmx/PlayerDataUpdate?Player_ID=" + SaveSettings.Player_ID + "&NewAmount=" + winprize + "&Stars=2&Win=1&Loss=0";
            aq.ajax(url, JSONObject.class, this, "jsonCallbackplayerDataUp");
          //  SaveSettings.Player_Total = String.valueOf(Integer.valueOf(SaveSettings.Player_Total) + winprize);
            break;
            //if he lose
            case 0:
             url = SaveSettings.ServerURL + "/Playersws.asmx/PlayerDataUpdate?Player_ID=" + SaveSettings.Player_ID + "&NewAmount=" + (-lossamount) + "&Stars=0&Win=0&Loss=1";
            aq.ajax(url, JSONObject.class, this, "jsonCallbackplayerDataUp");
          //  SaveSettings.Player_Total = String.valueOf(Integer.valueOf(SaveSettings.Player_Total) - lossamount);
                break;
                default:
                    Toast.makeText(getApplicationContext(), "no case like this ", Toast.LENGTH_LONG).show();
                    break;
        }

    }

    public void jsonCallbackplayerDataUp(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call

                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext(), "json is null ", Toast.LENGTH_LONG).show();
        }

    }

    public void otherplayerlossDataUp(){
        // 2 stars to add a star and 0 to delete a star ** newamount to add it to the player total ** 1 or 0 to win and loss
        // newamount parameter accept negative values **

        String url = SaveSettings.ServerURL + "/Playersws.asmx/PlayerDataUpdate?Player_ID="+OtherID+"&NewAmount="+(-winprize)+"&Stars=0&Win=0&Loss=1";
            aq.ajax(url, JSONObject.class, this, "jsonCallbackplayerlossDataUp");

    }

    public void jsonCallbackplayerlossDataUp(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call

            Toast.makeText(getApplicationContext(), "done! ", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getApplicationContext(), "json is null ", Toast.LENGTH_LONG).show();
        }

    }

    public void gotowin(int PlayerStatus){
        Intent intent=new Intent(this,WinActivity.class);
        intent.putExtra("PlayerStatus",PlayerStatus);
        startActivity(intent);
    }

    public void GetPlayerTotal(){
        String url = SaveSettings.ServerURL + "Playersws.asmx/GetPlayerData?Player_ID=" + SaveSettings.Player_ID ;
        aq.ajax(url, JSONObject.class, this, "jsonCallbackPlayerData");
    }

    public void jsonCallbackPlayerData(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if(json != null){
            //successful ajax call
            //successful ajax call

            long IsAny=json.getInt("IsAny");
            if(IsAny!=0){

                playerTT.setText(String.valueOf(json.getLong("PlayerTotal")));

            }
            else {
                String msg=json.getString("Message");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }else{
            //json null
        }

    }

    public void Delay (){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

            }
        }, 5000);
    }
}

