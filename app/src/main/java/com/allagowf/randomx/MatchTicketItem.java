package com.allagowf.randomx;
/**
 * Created by guissous on 03-27-2018.
 */
public class MatchTicketItem {


    public String MatchID;
    public String HostID;

    public String HostName;
    public String HostTotal;
    public String Match_Price;


    MatchTicketItem(String MatchID , String HostName,String HostTotal, String HostID,String Match_Price){
        this.MatchID=MatchID;
        this.HostID=HostID;
        this.HostName=HostName;
        this.HostTotal=HostTotal;
        this.Match_Price=Match_Price;

    }
}
