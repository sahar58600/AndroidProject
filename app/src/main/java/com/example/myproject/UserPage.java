package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class UserPage extends AppCompatActivity {

    String selectedCountry, selectedGender;
    TextView tvTitle ;
    EditText edEventCode;
    DataBaseHelper db;
    ListView listview_User_Events ;
    Button button_Add_Details ;

    //ListView
    ArrayList<EventBriefInformation> arrayList;
    EventAdapter eventAdapter ;
    ArrayList<EventHelper> arrayEvents ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        tvTitle = findViewById(R.id.tvTitle) ;
        edEventCode = findViewById(R.id.edEventCode) ;
        listview_User_Events = findViewById(R.id.listview_User_Events) ;
        button_Add_Details = findViewById(R.id.button_Add_Details) ;


        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails" , Context.MODE_PRIVATE) ;
        tvTitle.setText("Hello" + System.getProperty("line.separator") +sharedPreferences.getString("FirstAndLastName" , "")); //השמת כותרת בהתאם למה שנבחר


        db = new DataBaseHelper(getBaseContext());



        arrayEvents =db.retreiveAllYUsersEvents(sharedPreferences.getString("UserName","")) ;

        int differentEvents = arrayEvents.size() ;
        arrayList =new ArrayList<>();
        eventAdapter = new EventAdapter(this,arrayList) ;
        listview_User_Events.setAdapter(eventAdapter);


            while(differentEvents > 0)
            {
                try
                {
                    arrayList.add(new EventBriefInformation(arrayEvents.get(differentEvents-1).getPlace(),arrayEvents.get(differentEvents-1).getDateEventHelper().getDate(),
                            arrayEvents.get(differentEvents-1).getCode()));
                    eventAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    Log.e("TicketsListError",e.getMessage());
                }
                differentEvents--;
            }




            try
            {
                if(db.checkIfUserFilledAllDetails(sharedPreferences.getString("UserName" , "")) == true) //יש מידע על הcountry Gender City של המשתמש
                {
                    button_Add_Details.setVisibility(View.GONE);
                }
                else
                {
                    button_Add_Details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            SharedPreferences sharedPreferences1 = getSharedPreferences("UserDetails" , Context.MODE_PRIVATE) ;
                            dialogMakerToAddDetails(sharedPreferences1.getString("UserName" , "")) ;
                        }
                    });
                }
            }
            catch (Exception e)
            {
                Log.e("ButtonAddDetailsError",e.getMessage());
            }






    }

    public void onClickAddDetails(View view)
    {
    }


    public class EventBriefInformation
    {
        private String list_item_place;
        private String list_item_date;
        private String list_item_code ;

        public EventBriefInformation(String list_item_place, String list_item_date,String list_item_code)
        {
            this.list_item_place = list_item_place;
            this.list_item_date = list_item_date;
            this.list_item_code = list_item_code ;
        }
        public String getPlace() {
            return this.list_item_place;
        }

        public String getCode()
        {
            return this.list_item_code;
        }

        public String getDate()
        {
            return this.list_item_date;
        }
    }

    public class EventAdapter extends ArrayAdapter<EventBriefInformation>
    {

        public int positionTicketClicked;

        public EventAdapter(Context context, ArrayList<EventBriefInformation> arrayList) {
            super(context, 0, arrayList);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.event_inflate, parent, false);
            }

            TextView list_item_place = (TextView) listItemView.findViewById(R.id.list_item_place);
            TextView list_item_date = (TextView) listItemView.findViewById(R.id.list_item_date);
            TextView list_item_code = (TextView) listItemView.findViewById(R.id.list_item_code);


            list_item_place.setText(arrayList.get(position).getPlace());
            list_item_date.setText(arrayList.get(position).getDate());
            list_item_code.setText(arrayList.get(position).getCode());


            list_item_place.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    //מה שבראש הlistview הוא שווה לposition=0
                    db = new DataBaseHelper(getBaseContext());
                    dialogMakerToPresentInformation(arrayEvents.get(arrayEvents.size()-position-1));
                }
            });
            return listItemView;
        }
    }


    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(getBaseContext(),LoginPage.class));
        super.onBackPressed();
    }

    public void dialogMakerToPresentInformation(EventHelper eventHelper)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        View myView = getLayoutInflater().inflate(R.layout.dynamic_dialog, null);
        TextView dialog_organizerName = myView.findViewById(R.id.dialog_organizerName);
        TextView dialog_code = myView.findViewById(R.id.dialog_code);
        TextView dialog_numberOfParticipants = myView.findViewById(R.id.dialog_numberOfParticipants);
        TextView dialog_location = myView.findViewById(R.id.dialog_location);
        TextView dialog_date = myView.findViewById(R.id.dialog_date);
        TextView dialog_time = myView.findViewById(R.id.dialog_time);

        dialog_organizerName.setText(eventHelper.getOrganizer());
        dialog_code.setText(eventHelper.getCode());
        String participants = eventHelper.getActualParticipants()+ "/"+db.findMaxParticipantsByCode(eventHelper.getCode());
        dialog_numberOfParticipants.setText(participants);
        dialog_location.setText(eventHelper.getPlace());
        dialog_date.setText(eventHelper.getDate().getDate());
        dialog_time.setText(eventHelper.getDate().getTime());



        builder.setView(myView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void dialogMakerToAddDetails(final String userName)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        db = new DataBaseHelper(getBaseContext()) ;

        View myView = getLayoutInflater().inflate(R.layout.dialog_additional_details, null);
        final EditText dialog_EditText_City = myView.findViewById(R.id.dialog_EditText_City);
        Spinner dialog_spinner_gender = myView.findViewById(R.id.dialog_spinner_gender);
        Spinner dialog_spinner_country = myView.findViewById(R.id.dialog_spinner_country);
        Button dialog_add_button = myView.findViewById(R.id.dialog_add_button);
        TextView dialog_TextView_title = myView.findViewById(R.id.dialog_TextView_title) ;

        dialog_TextView_title.setText("Add more details" + System.getProperty("line.separator")+ db.getFirstNameAndLastNameByUserName(userName));


        //טיפול בdialog_spinner_country
        String[] locales = Locale.getISOCountries();
        final ArrayList<String> countries = new ArrayList<>();
        countries.add("Select Country");

        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            countries.add(obj.getDisplayCountry());

        }
        Collections.sort(countries);

        ArrayAdapter<String> spinnerCountryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        dialog_spinner_country.setAdapter(spinnerCountryAdapter);

        AdapterView.OnItemSelectedListener countryListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = countries.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        dialog_spinner_country.setOnItemSelectedListener(countryListener);


        //טיפול dialog_spinner_gender
        final String[] genders = {"Select Gender", "Male", "Female"};
        ArrayAdapter<String> spinnerGenderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, genders);
        dialog_spinner_gender.setAdapter(spinnerGenderAdapter);

        AdapterView.OnItemSelectedListener genderListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = genders[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        dialog_spinner_gender.setOnItemSelectedListener(genderListener);



        dialog_add_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(dialog_EditText_City.length() !=0)
                {
                    if(selectedCountry.equals("Select Country") == false)
                    {
                        if(selectedGender.equals("Select Gender") == false)
                        {
                            boolean status = db.addAdditionalUserDetails(userName,selectedGender,selectedCountry,dialog_EditText_City.getText().toString()) ;
                            if(status == true)
                            {
                                Toast.makeText(getBaseContext(),"Succeeded to add details",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(),UserPage.class) ;
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(),"Something went wrong...",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(),"You have to select Gender",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(),"You have to select Country",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(),"You have fill City",Toast.LENGTH_SHORT).show();
                }
            }
        });


        builder.setView(myView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    //כאשר מזינים Code ולחוצים "Join" בשביל להצטרף לEvent
    public void onClickJoinPartyViaCode(View view)
    {
        db = new DataBaseHelper(getBaseContext());
        Toast.makeText(getBaseContext(),"Click One More Time",Toast.LENGTH_SHORT).show();
        if(edEventCode.length() !=0)
        {
            if(edEventCode.length() == 4)
            {
                if(db.checkIfCodeExist(edEventCode.getText().toString()))
                {

                    try
                    {
                        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE) ;
                        db.joinEventViaCode(edEventCode.getText().toString(),sharedPreferences.getString("UserName","")) ;
                        Toast.makeText(getBaseContext(),"Joined Event",Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(getBaseContext(),UserPage.class);
                        startActivity(myIntent);
                    }
                    catch (Exception e)
                    {
                        Log.e("JoinEvenError",e.getMessage());
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Code does not exist",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getBaseContext(),"Code must be consist of 4 digits",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getBaseContext(),"You have to type a code first",Toast.LENGTH_SHORT).show();
        }
    }


    public void onClickMoveActivityToCreate(View view)
    {
        Intent myIntent = new Intent(getBaseContext(),CreateEventActivity.class) ;
        startActivity(myIntent);
    }
}

/*
//.......טיפול בSpinnerCountry
        String[] locales = Locale.getISOCountries();
        final ArrayList<String> countries = new ArrayList<>();
        countries.add("**Select Country**");

        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            countries.add(obj.getDisplayCountry());

        }
        Collections.sort(countries);

        ArrayAdapter<String> spinnerCountryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        spinnerCountry.setAdapter(spinnerCountryAdapter);

        AdapterView.OnItemSelectedListener countryListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = countries.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinnerCountry.setOnItemSelectedListener(countryListener);


        //טיפול בSpinnerGender
        final String[] genders = {"**Select Gender**", "Male", "Female"};
        ArrayAdapter<String> spinnerGenderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, genders);
        spinnerGender.setAdapter(spinnerGenderAdapter);

        AdapterView.OnItemSelectedListener genderListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = genders[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinnerGender.setOnItemSelectedListener(genderListener);
 */


/*
<TableLayout
        android:layout_width="match_parent"
        android:layout_height="400dp"
        android:paddingLeft="20dp"
        android:paddingRight="20dp">


        <TableRow
            android:layout_marginTop="10dp"
            android:layout_width="wrap_content"
            android:weightSum="8"
            android:layout_height="wrap_content" >

            <TextView
                android:layout_width="30dp"
                android:layout_height="match_parent"
                android:layout_weight="3"
                android:text="City:"
                android:textColor="@color/colorAccent"
                android:textSize="20dp"
                android:textStyle="bold" />

            <EditText
                android:id="@+id/edCity"
                android:layout_width="50dp"
                android:layout_height="match_parent"
                android:textSize="20dp"
                android:gravity="center"
                android:textStyle="bold"
                android:layout_weight="5"
                android:digits="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                />
        </TableRow>

        <TableRow
            android:layout_marginTop="10dp"
            android:layout_width="wrap_content"
            android:weightSum="8"
            android:layout_height="wrap_content" >

            <TextView
                android:layout_width="30dp"
                android:layout_height="match_parent"
                android:layout_weight="3"
                android:text="Gender:"
                android:textColor="@color/colorAccent"
                android:textSize="20dp"
                android:textStyle="bold" />

            <Spinner
                android:id="@+id/spinnerGender"
                android:layout_width="50dp"
                android:layout_height="match_parent"
                android:textSize="20dp"
                android:gravity="center"
                android:textStyle="bold"
                android:layout_weight="5" />
        </TableRow>

        <TableRow
            android:layout_marginTop="10dp"
            android:layout_width="wrap_content"
            android:weightSum="8"
            android:layout_height="wrap_content" >

            <TextView
                android:layout_width="30dp"
                android:layout_height="match_parent"
                android:layout_weight="3"
                android:text="Country:"
                android:textColor="@color/colorAccent"
                android:textSize="20dp"
                android:textStyle="bold" />

            <Spinner
                android:id="@+id/spinnerCountry"
                android:layout_width="50dp"
                android:layout_height="match_parent"
                android:textSize="20dp"
                android:gravity="center"
                android:textStyle="bold"
                android:layout_weight="5" />
        </TableRow>



    </TableLayout>
 */
