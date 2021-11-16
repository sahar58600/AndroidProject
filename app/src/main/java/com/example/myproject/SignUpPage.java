package com.example.myproject;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpPage extends AppCompatActivity {
    EditText edFirstName, edLastName, edAge, edPhoneNumber, edEmail, edUserName, edPassword;
    TextView textViewPasswordStrengthIndiactor;
    Button button_sign_up;

    DataBaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);


        edFirstName = findViewById(R.id.edFirstName);
        edLastName = findViewById(R.id.edLastName);
        edAge = findViewById(R.id.edAge);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        edEmail = findViewById(R.id.edEmail);
        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);
        textViewPasswordStrengthIndiactor = findViewById(R.id.textViewPasswordStrengthIndiactor);


        //בדיקת חוזק של הסיסמא
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0)
                    textViewPasswordStrengthIndiactor.setText("Not Entered");
                else if (s.length() < 6)
                    textViewPasswordStrengthIndiactor.setText("EASY");
                else if (s.length() < 10)
                    textViewPasswordStrengthIndiactor.setText("MEDIUM");
                else if (s.length() < 15)
                    textViewPasswordStrengthIndiactor.setText("STRONG");
                if (s.length() == 15)
                    textViewPasswordStrengthIndiactor.setText("STRONGEST");
            }
        });

        //סידור מספר הפלאפון
        edPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());



        /*
        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edFirstName.length() != 0) {
                    if (edLastName.length() != 0) {
                        if (edAge.length() != 0) {
                            if (edPhoneNumber.length() != 0) {
                                if (isEmailValid(edEmail.getText().toString())) {
                                    if (edUserName.length() != 0) {
                                        db = new DataBaseHelper(getBaseContext());
                                        if (db.checkIfUserNameExist(edUserName.getText().toString()) == false) {
                                            if (edPassword.length() != 0) {
                                                boolean status = db.addNewUser(edFirstName.getText().toString(), edLastName.getText().toString(), Integer.valueOf(edAge.getText().toString()),
                                                        edPhoneNumber.getText().toString(), edEmail.getText().toString(), edUserName.getText().toString(), edPassword.getText().toString());

                                                if (status == true) {
                                                    Toast.makeText(getBaseContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getBaseContext(), "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getBaseContext(), "Choose Another UserName", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getBaseContext(), "UserName must not be null", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                Toast.makeText(getBaseContext(), "Not Vailed Email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Phone Number Must Not Be Null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Age Must Not Be Null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "LastName Must Not Be Null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "FirstName Must Not Be Null", Toast.LENGTH_SHORT).show();
                }
            }
        });


         */

    }

    public void onClickSignUp(View view)
    {
        try
        {
            if (edFirstName.length() != 0) {
                if (edLastName.length() != 0) {
                    if (edAge.length() != 0) {
                        if (edPhoneNumber.length() != 0) {
                            if (edEmail.length() !=0)
                            {
                                if (edUserName.length() != 0) {
                                    db = new DataBaseHelper(getBaseContext());
                                    if (db.checkIfUserNameExist(edUserName.getText().toString()) == false) {
                                        if (edPassword.length() != 0) {
                                            boolean status = db.addNewUser(edFirstName.getText().toString(), edLastName.getText().toString(), Integer.valueOf(edAge.getText().toString()),
                                                    edPhoneNumber.getText().toString(), edEmail.getText().toString(), edUserName.getText().toString(), edPassword.getText().toString());

                                            if (status == true)
                                            {
                                                Toast.makeText(getBaseContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                                Intent myIntent = new Intent(getBaseContext(),MainActivity.class);
                                                startActivity(myIntent);
                                            } else {
                                                Toast.makeText(getBaseContext(), "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else
                                        {
                                        Toast.makeText(getBaseContext(), "Choose Another UserName", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                    {
                                    Toast.makeText(getBaseContext(), "UserName must not be null", Toast.LENGTH_SHORT).show();
                                }
                            }
                            Toast.makeText(getBaseContext(), "Email Must Not Be Null", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Phone Number Must Not Be Null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Age Must Not Be Null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "LastName Must Not Be Null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "FirstName Must Not Be Null", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Log.e("SignUpError",e.getMessage());
        }

    }










}



