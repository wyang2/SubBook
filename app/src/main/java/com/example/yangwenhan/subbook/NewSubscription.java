/* Copyright © 2018. CMPUT301 Wi18, University of Alberta - All Rights Reserved.
* You may use, distribute or modify this code under terms and conditions of Code of Student Behavior at
* University of Alberta.
* You can find a copy of the license in this project. Otherwise, please contact contact wyang2@ualberta.ca
 */


/**
 * Provide an class for user to add a new subscription and store the info in a public arraylist.
 * Also able to handle invalid input.  Press save button to save subscription and back to mainactivity
 * class. An invalid input will display an error message and drive you back to the mainActivity class.
 */
package com.example.yangwenhan.subbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.yangwenhan.subbook.Subscription.Subscriptions;

public class NewSubscription extends AppCompatActivity {

    private static final String filename = "filename";
    private Intent returnIntent;
    private Intent intent;
    private EditText name;
    private EditText charge;
    private EditText comment;
    private EditText date;
    private String sub_name;
    private String sub_comment;
    private String sub_charge;
    private String sub_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subscription);
        Button exit = (Button) findViewById(R.id.exit);
        name = (EditText)findViewById(R.id.name);
        charge = (EditText)findViewById(R.id.charge);
        comment = (EditText)findViewById(R.id.comment);
        date = (EditText)findViewById(R.id.date);
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });
                Button add = (Button)findViewById(R.id.add);
                add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub_name = name.getText().toString();

                if (sub_name.matches("")) {  //check if name is empty
                    Toast.makeText(NewSubscription.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    sub_charge = charge.getText().toString();
                    if (sub_charge.matches("")) {  //check if charge is empty
                        Toast.makeText(NewSubscription.this, "Charge cannot be empty you may type 0", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        if (Float.parseFloat(sub_charge) < 0) {  //check if charge < 0
                            Toast.makeText(NewSubscription.this, "Charge cannot be negative", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            sub_date = date.getText().toString();
                            if (sub_date.matches("")) {  //check if date is empty
                                Toast.makeText(NewSubscription.this, "Date cannot be empty, default to current date", Toast.LENGTH_LONG).show();
                                Date temp_date = new Date();
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                sub_date = df.format(temp_date);
                                sub_comment = comment.getText().toString();
                                Subscription subscription = new Subscription(sub_name, Float.parseFloat(sub_charge), sub_date, sub_comment);
                                Subscriptions.add(subscription);
                                saveInFile();
                                Toast.makeText(NewSubscription.this, "New Subscription Saved", Toast.LENGTH_LONG).show();
                                intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Date temp_date = null;
                                try {
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    temp_date = df.parse(sub_date);
                                    if (!sub_date.equals(df.format(temp_date))) {  //check if date format is correct
                                        Toast.makeText(NewSubscription.this, "Date format must be yyyy-MM-dd", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        sub_comment = comment.getText().toString();
                                        Subscription subscription = new Subscription(sub_name, Float.parseFloat(sub_charge), sub_date, sub_comment);
                                        Subscriptions.add(subscription);
                                        saveInFile();
                                        Toast.makeText(NewSubscription.this, "New Subscription Saved", Toast.LENGTH_LONG).show();
                                        intent = new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                } catch (ParseException pe) { //handle invalid date format error
                                    Toast.makeText(NewSubscription.this, "Invalid date format", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        }
                    }

                }
            }
        });

    }
    public void saveInFile() throws NumberFormatException{
        try{
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter out = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(Subscriptions,out);
            out.flush();
            fos.close();

        }catch (FileNotFoundException e){
            throw new RuntimeException();
        }catch (IOException e){
            throw new RuntimeException();
        }
    }
}
