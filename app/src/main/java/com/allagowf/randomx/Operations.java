package com.allagowf.randomx;


import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by guissous on 04-05-2018.
 */
public class Operations {
    public static boolean isConnectingToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            return isConnected;


        }
        return false;
    }

    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader=new BufferedReader( new InputStreamReader(inputStream));
        String line ;
        String linereultcal="";

        try{
            while((line=bureader.readLine())!=null) {

                linereultcal+=line;

            }
            inputStream.close();


        }catch (Exception ex){}

        return linereultcal;
    }

    public  static void DisplayMessage(Context context, String msg){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static boolean NumIsValide(String playernum){
        //boolean valide=true;
        int[] num = strtoarray(playernum);
        int[] tempnum = num;
        for (int n = 0; n < 4; n++)
        {
            for (int m = n+1; m < 4; m++)
            {
                if (num[n] == tempnum[m]) {

                    return false ;
            }

            }
        }
        return true;
    }

    public  static int[] strtoarray(String num){
        int[] playernum = new int[4];
        for (int i = 0; i < 4; i++)
        {
            playernum[i] = num.charAt(i) - '0';
        }
        return playernum;
    }
}
