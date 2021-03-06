package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity
{
    EditText edUserName,edPassword ;
    DataBaseHelper db ;

    CheckBox checkBoxShowPassword ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);
        checkBoxShowPassword = findViewById(R.id.checkBoxShowPassword);



        checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }

    public void onClickSignIn(View view)
    {
        db = new DataBaseHelper(this);
        if(db.loginFunction(edUserName.getText().toString(),edPassword.getText().toString()))
        {
            try
            {
                saveUserFullName(db.getFirstNameAndLastNameByUserName(edUserName.getText().toString()),edUserName.getText().toString());
            }
            catch (Exception e)
            {
                Log.e("WTFFFF",e.getMessage());
            }
            Intent myIntent = new Intent(getBaseContext(),UserPage.class) ;
            startActivity(myIntent);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Either UserName or Password is incorrect , might be also both",Toast.LENGTH_SHORT).show();
        }
    }

    //?????????? ?????? ?????????? + ?????????? ???? ???????????? ?????????? sharedPreferences.
    // ?????????? ?????????? ???????????? ???????????? - ?????????? ???????????? ?????????? ???? ??keyword ???????? ???????? ???????? ?????????? ???? ?????????? ???????????? ??????????.
    public void saveUserFullName(String firstAndLastName,String userName)
    {
        //??????????-???????? ???????????????? ???????????? sharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails" , Context.MODE_PRIVATE) ;
        //???????? ??editor ??????.
        SharedPreferences.Editor editor = sharedPreferences.edit() ;
        //???????? ?????????? ??editor
        editor.putString("FirstAndLastName" , firstAndLastName) ;
        editor.putString("UserName" , userName) ;
        //??????????-???????? ?????????? ??sharedPreferences
        editor.commit() ;
    }

    public void onClickMoveToWebView(View view)
    {
        Intent myIntent = new Intent(this,WebViewPage.class) ;
        startActivity(myIntent);
    }
}
