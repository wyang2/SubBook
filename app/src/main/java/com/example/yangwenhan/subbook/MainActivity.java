/* Copyright © 2018. CMPUT301 Wi18, University of Alberta - All Rights Reserved.
* You may use, distribute or modify this code under terms and conditions of Code of Student Behavior at
* University of Alberta.
* You can find a copy of the license in this project. Otherwise, please contact contact wyang2@ualberta.ca
 */


/**
 * Provide an interface to displaying information of all subscriptions and displaying number of subscriptions
 * and total charge of all subscriptions in textviews. Press add button will switch to newSubscription class
 * which enable user to create a new subscription.  Clicking on an item in listview will lead you to view
 * its detailed information. Quit won't clear the information because of a copy of arrayList information is
 * stored locally.
 */
package com.example.yangwenhan.subbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.yangwenhan.subbook.Subscription.Subscriptions;

public class MainActivity extends AppCompatActivity {
    public static String filename = "filename";
    private Button add;
    private ListView subList;
    private TextView num;
    private TextView totalCharge;
    private Intent intent1;
    private Intent intent2;
    private float sum;
    private Integer pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFromFile();
        init();
        final ArrayAdapter adapter = new ArrayAdapter<Subscription>(this, android.R.layout.simple_list_item_1,Subscriptions);
        subList.setAdapter(adapter);
//Click add button will move from main activity to new activity in which user could add a subscription.
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent1 = new Intent(MainActivity.this,NewSubscription.class);
                startActivityForResult(intent1,1);
            }
        });

/**
    SubList can get the get the position of a subscription and put it into variable i, intent will helps pass data of subscription
that you click to ViewSubscription.class and display its details.  StartActivityForResult acts like a trigger which directly lead
to a transaction between activities, where as intent tells which two activities participated in transaction as well as starting activity
and ending activity. This was my initial idea, later I realize I could get any info of a specific subscription stored in arrayist if i have
its position, so i only need variable i to tell me which subscription is that I clicked.
 */


        subList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent2 = new Intent(MainActivity.this,ViewSubscription.class);
                intent2.putExtra("name",Subscriptions.get(i).getName());
                intent2.putExtra("charge",Subscriptions.get(i).getCharge());
                intent2.putExtra("comment",Subscriptions.get(i).getComment());
                intent2.putExtra("date",Subscriptions.get(i).getDate());
                intent2.putExtra("position",i);
                startActivityForResult(intent2, 2);
            }
        });


    }
//set but not used
    protected void OnStart(){
        super.onStart();
    }

    @Override

//Functions in onResume sums up charges for all subscriptions, and print on the Textview
    protected void onResume() {
        super.onResume();
        num = (TextView)findViewById(R.id.num);
        num.setText("Number of subscriptions: " + Integer.toString(Subscriptions.size()));
        float sum = 0;
        for(pos = 0; pos < Subscriptions.size(); pos++) {
            float temp = Subscriptions.get(pos).getCharge();
            sum = sum + temp;

        }
        totalCharge.setText("Total charges: "+Float.toString(sum));

    }


/*
    Used with StartActivityForResult, when came back from other activities it will make some
     adjustment according to the changes in arraylist by specify requestCode and resultCode
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If click add button
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadFromFile();
            subList = (ListView) findViewById(R.id.subscriptions);
            ArrayAdapter adapter = new ArrayAdapter<Subscription>(this, android.R.layout.simple_list_item_1, Subscriptions);
            subList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        // If click items in listview
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                loadFromFile();
                subList = (ListView) findViewById(R.id.subscriptions);
                ArrayAdapter adapter = new ArrayAdapter<Subscription>(this, android.R.layout.simple_list_item_1, Subscriptions);
                subList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

/*
    loadFromFile() and saveInFile() are ideas provided by LonelyTwitter which will store values in
    arraylist in a local file and able to read the subscriptions stored in file when API come online.
 */
    public void loadFromFile(){
        try{
            FileInputStream fis = openFileInput(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            Subscriptions = gson.fromJson(in,listType);
        }catch (FileNotFoundException e){
            Subscriptions = new ArrayList<Subscription>();
        }
    }
    public void saveInFile(){
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
//An unnecessary method used to make coding looks cleaner.
    public void init(){
        add = (Button)findViewById(R.id.add);
        subList = (ListView)findViewById(R.id.subscriptions);
        totalCharge = (TextView) findViewById(R.id.sum);
        num = (TextView)findViewById(R.id.num);

    }
}

/*
    References:
    https://developer.android.com/training/basics/intents/result.html
    https://developer.android.com/guide/components/activities/activity-lifecycle.html
    https://developer.android.com/reference/android/os/Bundle.html
    https://stackoverflow.com/questions/5265913/how-to-use-putextra-and-getextra-for-string-data
    https://developer.android.com/reference/java/util/ArrayList.html
    https://stackoverflow.com/questions/3500197/how-to-display-toast-in-android
    LonelyTwitter
    Lecture Notes
    And a lot of small Q&A in StackOverflow and youtube videos
 */
