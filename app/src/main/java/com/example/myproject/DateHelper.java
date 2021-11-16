package com.example.myproject;

public class DateHelper
{
    private int day ;
    private int month ;
    private int year ;
    private int hour;
    private int minute ;

    public DateHelper(int day,int month,int year,int hour,int minute)
    {
        this.day = day;
        this.month = month ;
        this.year = year;
        this.hour =hour;
        this.minute = minute ;
    }

    public String getDate()
    {
        String date = this.day + "." + this.month + "." + this.year ;
        return date;
    }
    public String getTime()
    {
        String time = "" ;
        if(this.minute < 10 && this.hour <10)
        {
            time = "0"+this.hour +":" + "0"+ this.minute ;
        }
        else if(this.minute > 10 && this.hour <10)
        {
            time = "0"+this.hour +":" + this.minute ;
        }
        else if(this.minute < 10 && this.hour >10)
        {
            time = this.hour +":" + "0"+ this.minute ;
        }
        else
        {
            time = this.hour +":"+ this.minute ;
        }
        return time;
    }
}
