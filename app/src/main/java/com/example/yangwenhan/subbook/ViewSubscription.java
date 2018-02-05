package com.example.yangwenhan.subbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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


public class ViewSubscription extends AppCompatActivity {

    private static final String filename = "filename";
    private TextView showName;
    private TextView showCharge;
    private TextView showComment;
    private TextView showDate;
    private Button edit, delete, quit;
    private String name, comment, date;
    private Float charge;
    private Integer pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subscription);

        showName = (TextView) findViewById(R.id.name);
        showCharge = (TextView) findViewById(R.id.charge);
        showComment = (TextView) findViewById(R.id.comment);
        showDate = (TextView)findViewById(R.id.date);

        edit = (Button) findViewById(R.id.edit);
        delete = (Button) findViewById(R.id.delete);
        quit = (Button) findViewById(R.id.quit);
        //Reference: https://stackoverflow.com/questions/14876273/simple-example-for-intent-and-bundle
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        comment = bundle.getString("comment");
        charge = bundle.getFloat("charge");
        pos = bundle.getInt("position");
        date = bundle.getString("date");

        showName.setText("Name " + name);
        showComment.setText("Comment " + comment);
        showDate.setText("Date " + date);
        showCharge.setText("Charge "+Float.toString(charge));

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Subscriptions.remove(Subscriptions.get(pos));
                saveInFile();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(ViewSubscription.this,EditSubscription.class);
                editIntent.putExtra("name",name);
                editIntent.putExtra("charge",charge);
                editIntent.putExtra("comment",comment);
                editIntent.putExtra("date",date);
                editIntent.putExtra("position",pos);
                startActivityForResult(editIntent,3);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3){
            if (resultCode == RESULT_OK){
                loadFromFile();
                showName = (TextView) findViewById(R.id.name);
                showCharge = (TextView) findViewById(R.id.charge);
                showComment = (TextView) findViewById(R.id.comment);
                showDate = (TextView)findViewById(R.id.date);
                name = Subscriptions.get(pos).getName();
                charge = Subscriptions.get(pos).getCharge();
                comment = Subscriptions.get(pos).getComment();
                date = Subscriptions.get(pos).getDate();
                showName.setText(name);
                showCharge.setText(charge.toString());
                showComment.setText(comment);
                showDate.setText(date);
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();


            }
        }
    }

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

}
