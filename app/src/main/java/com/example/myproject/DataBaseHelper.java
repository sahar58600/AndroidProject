package com.example.myproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper
{
    // Database Info
    private static final String DB_NAME = "UsersDB";
    private static final int DB_VERSION = 1;


    // Table Name
    private static final String USER_TABLE = "UserTable";
    // USER_TABLE Columns
    private static final String KEY_FIRST_NAME = "FirstName";
    private static final String KEY_LAST_NAME = "LastName";
    private static final String KEY_AGE = "Age";
    private static final String KEY_GENDER = "Gender";
    private static final String KEY_COUNTRY = "Country";
    private static final String KEY_CITY = "City";
    private static final String KEY_PHONE_NUMBER = "PhoneNumber";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_PASWWORD = "Password";
    private static final String KEY_EVENTS_USER_IN = "EventsTheUserParticipant";

    //USER_TABLE Queries
    private static final String CREATE_USER_TABLE_TABLE_QUERY = "CREATE TABLE " + USER_TABLE + " (" + KEY_FIRST_NAME + " TEXT ," + KEY_LAST_NAME + " TEXT ,"  + KEY_AGE + " INTEGER DEFAULT 0 ,"
            + KEY_GENDER + " TEXT ," + KEY_COUNTRY + " TEXT ," + KEY_CITY + " TEXT ," + KEY_PHONE_NUMBER + " TEXT ," + KEY_EMAIL + " TEXT ," + KEY_USER_NAME + " TEXT ," + KEY_PASWWORD + " TEXT ,"
            + KEY_EVENTS_USER_IN + " TEXT)" ;
    private static final String DROP_USER_TABLE_QUERY = "DROP TABLE IF EXISTS " + USER_TABLE + ";";
    private static final String SELECT_USER_TABLE_QUERY = "SELECT * FROM " + USER_TABLE + ";";

    // Table Name
    private static final String EVENT_TABLE = "EventTable";
    // EVENT_TABLE Columns
    private static final String KEY_ORGANIZER_NAME = "OrganizerName";
    private static final String KEY_EVENT_CODE = "EventCode";
    private static final String KEY_MAX_PARTICIPTANTS = "MaxParticipants";
    private static final String KEY_ACTUAL_PARTICIPTANTS = "ActualParticipant";
    private static final String KEY_PLACE = "PlaceOfTheParty";
    private static final String KEY_DAY = "Day";
    private static final String KEY_MONTH = "Month";
    private static final String KEY_YEAR = "Year";
    private static final String KEY_HOUR = "Hour";
    private static final String KEY_MINUTES = "Minute";
    //EVENT_TABLE Queries
    private static final String CREATE_EVENT_TABLE_QUERY = "CREATE TABLE " + EVENT_TABLE + " (" + KEY_ORGANIZER_NAME + " TEXT ," + KEY_EVENT_CODE + " TEXT ,"  + KEY_MAX_PARTICIPTANTS + " INTEGER DEFAULT 1 ,"  + KEY_ACTUAL_PARTICIPTANTS + " INTEGER DEFAULT 1 ,"
            + KEY_PLACE + " TEXT ," + KEY_DAY + " INTEGER DEFAULT 0 ," + KEY_MONTH + " INTEGER DEFAULT 0 ," + KEY_YEAR + " INTEGER DEFAULT 0 ," + KEY_HOUR + " INTEGER DEFAULT 0 ," +
             KEY_MINUTES + " INTEGER DEFAULT 0)" ;
    private static final String DROP_EVENT_TABLE_QUERY = "DROP TABLE IF EXISTS " + EVENT_TABLE + ";";
    private static final String SELECT_EVENT_TABLE_QUERY = "SELECT * FROM " + EVENT_TABLE + ";";


    //פעולה בונה
    public DataBaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_USER_TABLE_TABLE_QUERY);
        db.execSQL(CREATE_EVENT_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion != newVersion)
        {
            db.execSQL(DROP_USER_TABLE_QUERY);
            db.execSQL(DROP_EVENT_TABLE_QUERY);
            onCreate(db);
        }
    }

    public String findMaxParticipantsByCode(String code)
    {
        SQLiteDatabase db = getReadableDatabase();
        String st = "select * from " + EVENT_TABLE + " where " + KEY_EVENT_CODE + " = '" + code + "'";
        Cursor cursor = db.rawQuery(st , null) ;

        if(cursor.moveToFirst())
        {
            return String.valueOf(cursor.getInt(2));
        }
        return "0";
    }


    public ArrayList<EventHelper> retreiveAllYUsersEvents(String userName)
    {
       ArrayList<String> allUsersEvents = getAllEventsUserIn(userName) ;
       ArrayList<EventHelper> eventHelpers = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        if(allUsersEvents != null)
        {
            int arraySize = allUsersEvents.size()-1 ;
            while(arraySize>=0)
            {
                String st = "select * from " + EVENT_TABLE + " where " + KEY_EVENT_CODE + " = '" + allUsersEvents.get(arraySize) + "'";
                Cursor cursor = db.rawQuery(st , null) ;
                if(cursor.moveToFirst())
                {
                    try
                    {
                        EventHelper eventHelper = new EventHelper(cursor.getString(0),cursor.getString(1),cursor.getInt(3),cursor.getString(4),
                                new DateHelper(cursor.getInt(5),cursor.getInt(6),cursor.getInt(7),cursor.getInt(8),cursor.getInt(9))) ;
                        eventHelpers.add(eventHelper);
                    }
                    catch (Exception e)
                    {
                        Log.e("retreiveAllYUsersEvents",e.getMessage());
                    }

                }
                arraySize--;
            }
        }
        return  eventHelpers ;
    }



    public int createEvent(String code , int maxParticiptants,String place ,int day ,int month ,int year ,int hour ,int minute,String userInvited,String nameOrganizer,int actualParticiptants,String userName)
    {

        //בדיקה שהקוד לא קיים. אם הוא קיים יחזיר true אחרת false
        if(checkIfCodeExist(code) == true)
        {
            return 0 ;
        }


        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_EVENT_CODE,code);
        contentValues.put(KEY_MAX_PARTICIPTANTS,maxParticiptants);
        contentValues.put(KEY_PLACE,place);
        contentValues.put(KEY_DAY,day);
        contentValues.put(KEY_MONTH,month);
        contentValues.put(KEY_YEAR,year);
        contentValues.put(KEY_HOUR,hour);
        contentValues.put(KEY_MINUTES,minute);
        contentValues.put(KEY_ORGANIZER_NAME,nameOrganizer);
        contentValues.put(KEY_ACTUAL_PARTICIPTANTS,actualParticiptants);



        //טיפול במקרה שהיוצר הזמין משתמש אחר
        if(userInvited != null) //הוא הזין משהוו
        {
            if(checkIfUserNameExist(userInvited)) //המשתמש קיים במערכת
            {
                String st = "select * from " + USER_TABLE + " where " + KEY_USER_NAME + " = '" + userInvited + "'";
                Cursor cursor = db.rawQuery(st , null) ;

                if(cursor.moveToFirst()) //ידוע שקיים במערכת,עוד בדיקה פשוט
                {
                    //הוספת הCode בעמודה המתאימה בUSER_TABLE בהתאם לuserInvited (ערך)
                    addCodeToUserEvents(userInvited,code);
                    //עדכון contentvalues שעכשיו יש 2 משתמשים בEvent ולא אחד
                    contentValues.put(KEY_ACTUAL_PARTICIPTANTS,actualParticiptants);
                }
            }
        }


        //הכנסת המידע שהתקבל לטבלה
        Long number = db.insertOrThrow(EVENT_TABLE ,null , contentValues) ; //מחזיר את השורה החדשה או -1 אם ההכנסה נכשלה


        if(number !=-1) // אם נוספה שורה חדשה
        {
            try
            {
                //הוספת Code לעמודה 11 (כאשר מחשיבים את העמודה הראשונה) של המשתמש
                //מוכנס לטבלה USER_TABLE ולשורה בהתאם לuserName
                addCodeToUserEvents(userName,code);
            }
            catch (Exception e)
            {
                Log.e("ErrorAddCodeToUserFromCreateEvent",e.getMessage());
            }
            return  1 ;
        }
        return -1 ;
    }


    public boolean addCodeToUserEvents(String userName,String code)
    {
        String[] p= {userName};
        String addCode = "0";

        SQLiteDatabase db = getWritableDatabase();
        String st = "select * from " + USER_TABLE + " where " + KEY_USER_NAME + " = '" + userName + "'";
        Cursor cursor = db.rawQuery(st , null) ;



        ContentValues specialContentValues = new ContentValues();
        if(cursor.moveToFirst())
        {
            if(cursor.getString(10) == null)
            {
                addCode = code ;
            }
            else
            {
                addCode = cursor.getString(10) +code ;
            }
        }
        specialContentValues.put(KEY_EVENTS_USER_IN,addCode);
        int affected1 = db.update(USER_TABLE,specialContentValues,"UserName" + " =?",p) ;


        if(affected1 != 0)
        {
            return true ;
        }
        return false ;
    }


    public boolean checkIfCodeExist(String code)
    {
        SQLiteDatabase db = getReadableDatabase();
        String st = "select * from " + EVENT_TABLE + " where " + KEY_EVENT_CODE + " = '" + code + "'";
        Cursor cursor = db.rawQuery(st , null) ;

        if(cursor.moveToFirst())
        {
            return true ;
        }
        return false ;
    }


    //הפעולה מקבלת שם משתמש ומחזירה רשימת קודים של Events
    public ArrayList<String> getAllEventsUserIn(String userName)
    {
        ArrayList<String> back = new ArrayList<>() ;
        char[] charArray ;
        String addToArray;

        SQLiteDatabase db = getReadableDatabase();
        String st = "select * from " + USER_TABLE + " where " + KEY_USER_NAME + " = '" + userName + "'";
        Cursor cursor = db.rawQuery(st , null) ;

        try
        {
            if(cursor.moveToFirst())//קיים משתמש בעל שם כזה
            {
                String colValue = cursor.getString(10) ;
                charArray = colValue.toCharArray();

                boolean status = true ;
                int count = 0;
                for(int i = 0 ; i< charArray.length/4 ;i++)
                {
                    addToArray ="" ;

                    while(count <= charArray.length && status == true)
                    {
                        addToArray+= charArray[count] ;
                        count++ ;
                        if(count%4 == 0)
                        {
                            status = false ;
                        }
                    }
                    status = true ;
                    back.add(addToArray);
                }
            }
        }
        catch (Exception e)
        {
            Log.e("getAllEventsUserInError",e.getMessage());
        }
        return back ;
    }


    public boolean joinEventViaCode(String code ,String userName)
    {
        SQLiteDatabase db = getWritableDatabase();
        String st = "select * from " + EVENT_TABLE + " where " + KEY_EVENT_CODE + " = '" + code + "'";
        Cursor cursor = db.rawQuery(st , null) ;
        boolean status = false ;


        if(checkIfUserInEvent(code,userName) == false) //המשתמש לו בEvent
        {
                addCodeToUserEvents(userName,code);
                ContentValues contentValues = new ContentValues();
                int numberParticiptantsAfteradd = cursor.getInt(3) +1 ;
                contentValues.put(KEY_ACTUAL_PARTICIPTANTS,numberParticiptantsAfteradd);
                String[] p = {code} ;
                db.update(EVENT_TABLE,contentValues,"EventCode" + " =?",p);
                status = true ;
        }
        return status;
    }

    public boolean checkIfUserInEvent(String code , String userName)
    {
        SQLiteDatabase db = getReadableDatabase();
        String st = "select * from " + USER_TABLE + " where " + KEY_USER_NAME + " = '" + userName + "'";
        Cursor cursor = db.rawQuery(st , null) ;

        boolean found = false ;
        if(cursor.moveToFirst()) //המשתמש קיים
        {
            ArrayList<String> arrayList = getAllEventsUserIn(userName) ;

            if(arrayList.isEmpty() == false)
            {
                int position = arrayList.size() -1 ;
                while(position>= 0 && found == false)
                {
                    if(arrayList.get(position).equals(code))
                    {
                        found = true ;
                    }
                    position--;
                }
            }
        }
        return found ;


    }





    //....................................USER_TABLE



    public boolean checkIfUserFilledAllDetails(String userName)
    {
        SQLiteDatabase db = getReadableDatabase();
        String st = "select * from " + USER_TABLE + " where " + KEY_USER_NAME + " = '" + userName + "'";
        Cursor cursor = db.rawQuery(st , null) ;

        boolean status = true ;

        if(cursor.moveToFirst()) //קיים משתמש כזה
        {
            if(cursor.getString(3).equals("")) //בודק אם למשתמש הזין Country Gender City אם אחד מהם לא הוזן , כולם לא הוזנו
            {
                status = false ;
            }
            else
            {
                status = true ;
            }
        }
        return  status ;
    }

    public boolean addAdditionalUserDetails(String userName,String gender,String country ,String city)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        if(checkIfUserFilledAllDetails(userName) == false)
        {
            contentValues.put(KEY_GENDER,gender);
            contentValues.put(KEY_COUNTRY,country);
            contentValues.put(KEY_CITY,city);
            String[] p = {userName} ;
            int affected =  db.update(USER_TABLE,contentValues,"UserName" + " =?",p);

            if(affected != 0)
            {
                return true ;
            }
        }
        return false ;
    }





    public boolean addNewUser(String firstName, String lastName,int age,String phoneNumber,String email,String userName , String password)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FIRST_NAME,firstName);
        contentValues.put(KEY_LAST_NAME,lastName);
        contentValues.put(KEY_AGE,age);
        contentValues.put(KEY_GENDER,"");
        contentValues.put(KEY_COUNTRY,"");
        contentValues.put(KEY_CITY,"");
        contentValues.put(KEY_PHONE_NUMBER,phoneNumber);
        contentValues.put(KEY_EMAIL,email);
        contentValues.put(KEY_USER_NAME,userName);
        contentValues.put(KEY_PASWWORD,password);
        Long number = db.insertOrThrow(USER_TABLE ,null , contentValues) ; //מחזיר את השורה החדשה או -1 אם ההכנסה נכשלה

        if(number !=-1)
        {
            return  true ;
        }
        return false ;
    }

    public boolean checkIfUserNameExist(String userName)
    {
        SQLiteDatabase db = getReadableDatabase();
        String st = "select * from " + USER_TABLE + " where " + KEY_USER_NAME + " = '" + userName + "'";
        Cursor cursor = db.rawQuery(st , null) ;

        if(cursor.moveToFirst())
        {
            return true ;
        }
        return false ;
    }

    public boolean loginFunction(String userName,String password)
    {
        SQLiteDatabase db = getReadableDatabase();
        String st = "select * from " + USER_TABLE + " where " + KEY_USER_NAME + " = '" + userName + "'";
        Cursor cursor = db.rawQuery(st , null) ;

        if(cursor.moveToFirst())
        {
            if(cursor.getString(9).equals(password))
            {
                return true ;
            }
        }
        return false ;
    }

    public String getFirstNameAndLastNameByUserName(String userName)
    {
        SQLiteDatabase db = getReadableDatabase();
        String st = "select * from " + USER_TABLE + " where " + KEY_USER_NAME + " = '" + userName + "'";
        Cursor cursor = db.rawQuery(st , null) ;

        String fullname = null;

        if(cursor.moveToFirst())
        {
            fullname = cursor.getString(0) + " " + cursor.getString(1) ;
        }

        return fullname ;
    }
}
