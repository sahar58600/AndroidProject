package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity
{
    TextView tvTitle ;
    EditText edSpecialCode,edMaxParticipants,edPlace,edFriendInvite ;
    Spinner spinnerDay,spinnerMonth,spinnerYear,spinnerHour,spinnerMinute;


    String selectedDay,selectedMonth,selectedYear;
    String selectedHour ;
    String selectedMinute;

    DataBaseHelper db ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        tvTitle = findViewById(R.id.tvTitle);
        edSpecialCode = findViewById(R.id.edSpecialCode);
        edMaxParticipants = findViewById(R.id.edMaxParticipants);
        edPlace = findViewById(R.id.edPlace);
        edFriendInvite = findViewById(R.id.edFriendInvite);
        spinnerDay = findViewById(R.id.spinnerDay);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerHour = findViewById(R.id.spinnerHour);
        spinnerMinute = findViewById(R.id.spinnerMinute);


        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails" , Context.MODE_PRIVATE) ;
        tvTitle.setText("Create Event " +sharedPreferences.getString("FirstAndLastName" , "")); //השמת כותרת בהתאם למה שנבחר



        final String[] day = {"Select Day", "1", "2","3", "4","5", "6","7", "8","9", "10","11", "12","13", "14","15", "16","17", "18","19", "20","21", "22","23", "24","25", "26","27", "28","29", "30","31"};
        ArrayAdapter<String> spinnerDayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, day);
        spinnerDay.setAdapter(spinnerDayAdapter);

        AdapterView.OnItemSelectedListener dayListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedDay = day[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinnerDay.setOnItemSelectedListener(dayListener);


        final String[] month = {"Select Month", "1", "2","3", "4","5", "6","7", "8","9", "10","11", "12"};
        ArrayAdapter<String> spinnerMonthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, month);
        spinnerMonth.setAdapter(spinnerMonthAdapter);

        AdapterView.OnItemSelectedListener monthListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedMonth = month[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinnerMonth.setOnItemSelectedListener(monthListener);


        final String[] year = {"Select Year", "2020", "2021","2022", "2023","2024", "2025"};
        ArrayAdapter<String> spinnerYearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, year);
        spinnerYear.setAdapter(spinnerYearAdapter);

        AdapterView.OnItemSelectedListener yearListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedYear = year[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinnerYear.setOnItemSelectedListener(yearListener);



            final String[] hour = {"Select Hour", "00", "01","02", "03","04", "05","06", "07","08", "09","10", "11","12", "13","14", "15","16", "17","18", "19","20", "21","22", "23"};
            ArrayAdapter<String> spinnerHourAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hour);
            spinnerHour.setAdapter(spinnerHourAdapter);
            AdapterView.OnItemSelectedListener hourListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    selectedHour = hour[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };
            spinnerHour.setOnItemSelectedListener(hourListener);


        final String[] minute = {"Select minute","01","02", "03","04", "05","06", "07","08", "09","10", "11","12", "13","14", "15","16", "17","18", "19","20", "21","22", "23"
                ,"24","25", "26","27", "28","29", "30","31", "32","33", "34","35", "36","37", "38","39", "40","41", "42","43", "44","45", "46"
                , "47","48", "49","50", "51","52", "53","54", "55","56", "57","58", "59"};
        ArrayAdapter<String> spinnerMinuteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, minute);
        spinnerMinute.setAdapter(spinnerMinuteAdapter);

        AdapterView.OnItemSelectedListener minuteListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedMinute = minute[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinnerMinute.setOnItemSelectedListener(minuteListener);



    }




    public void onClickCreateEvent(View view)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails" , Context.MODE_PRIVATE) ;
        try
        {
            if(edSpecialCode.length() != 0) {
                if (edSpecialCode.length() == 4) {
                    if (edMaxParticipants.length() != 0) {
                        if (Integer.valueOf(edMaxParticipants.getText().toString()) > 0) {
                            if (edPlace.length() != 0) {
                                if (selectedDay.length() != 0 && !selectedDay.equals("Select Day")) {
                                    if (selectedMonth.length() != 0 && !selectedMonth.equals("Select Month")) {
                                        if (selectedYear.length() != 0 && !selectedYear.equals("Select Year")) {
                                            if (!selectedHour.equals("Select Hour")) {
                                                if (!selectedMinute.equals("Select minute")) {
                                                    db = new DataBaseHelper(getBaseContext());
                                                    int status = db.createEvent(edSpecialCode.getText().toString(), Integer.valueOf(edMaxParticipants.getText().toString()), edPlace.getText().toString(),
                                                            Integer.valueOf(selectedDay), Integer.valueOf(selectedMonth), Integer.valueOf(selectedYear)
                                                            , Integer.valueOf(selectedHour), Integer.valueOf(selectedMinute), edFriendInvite.getText().toString(), sharedPreferences.getString("FirstAndLastName", ""), 1,
                                                            sharedPreferences.getString("UserName", ""));

                                                    if (status == 1) {

                                                        Toast.makeText(getBaseContext(), "Event Has been Created", Toast.LENGTH_SHORT).show();
                                                        Intent myIntent = new Intent(getBaseContext(), UserPage.class);
                                                        startActivity(myIntent);
                                                    }
                                                    else if (status == -1)
                                                    {
                                                        Toast.makeText(getBaseContext(), "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                        {
                                                        Toast.makeText(getBaseContext(), "You have to choose other code", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getBaseContext(), "Select Minute", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getBaseContext(), "Select Hour", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getBaseContext(), "Select Year", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getBaseContext(), "Select Month", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getBaseContext(), "Select Day", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getBaseContext(), "You have to add a place's name", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Max Participants has to be greater than 0", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "You have to add Max Participants ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Code must consist 4 digits", Toast.LENGTH_SHORT).show();
                }
            }
                else
                {
                    Toast.makeText(getBaseContext(),"Choose another Special Code",Toast.LENGTH_SHORT).show();
                }
        }
        catch (Exception e)
        {
            Log.e("SomeErrorOcurred",e.getMessage());
        }

    }
}