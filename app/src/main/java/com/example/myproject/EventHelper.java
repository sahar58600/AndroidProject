package com.example.myproject;

public class EventHelper
{
    private String organizer;
    private String code ;
    private int actualParticipants ;
    private String place ;
    private DateHelper date ;

    public EventHelper(String organizer,String code,int actualParticipants,String place,DateHelper date)
    {
        this.organizer = organizer ;
        this.code = code ;
        this.actualParticipants = actualParticipants ;
        this.place = place ;
        this.date = date ;
    }

    public String getOrganizer() {
        return organizer;
    }

    public int getActualParticipants() {
        return actualParticipants;
    }

    public String getCode() {
        return code;
    }

    public String getPlace() {
        return place;
    }

    public DateHelper getDateEventHelper()
    {
        return  this.date ;
    }

    public DateHelper getDate() {
        return date;
    }
}
