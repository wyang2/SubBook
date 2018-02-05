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

public class EditSubscription extends AppCompatActivity {
    private EditText reName, reCharge, reComment, reDate;
    private Button save, quit;
    private Bundle bundle;
    private Integer pos;
    private String name;
    private String charge;
    private String comment;
    private String dateString;
    private static final String filename = "filename";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subscription);

        save = (Button)findViewById(R.id.save);
        quit = (Button)findViewById(R.id.quit);

        reName = (EditText)findViewById(R.id.editName);
        reCharge = (EditText)findViewById(R.id.editCharge);
        reComment = (EditText)findViewById(R.id.editComment);
        reDate = (EditText)findViewById(R.id.editDate);

        quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = getIntent().getExtras();

                pos = bundle.getInt("pos");

                name = reName.getText().toString();
                comment = reComment.getText().toString();

                if (name.matches("")){
                    Toast.makeText(EditSubscription.this,"Name cannot be empty", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    charge = reCharge.getText().toString();
                    if (charge.matches("")) {
                        Toast.makeText(EditSubscription.this, "Charge cannot be empty you may type 0", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        if (Float.parseFloat(charge) < 0) {
                            Toast.makeText(EditSubscription.this, "Charge cannot be negative", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        dateString = reDate.getText().toString();
                        if (dateString.matches("")){
                            Toast.makeText(EditSubscription.this,"Date cannot be empty, default to current date",Toast.LENGTH_LONG).show();
                            Date updateDate = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            dateString = dateFormat.format(updateDate);
                            comment = reComment.getText().toString();
                            Subscription subscription = new Subscription(name, Float.parseFloat(charge), dateString, comment);

                            Subscriptions.set(pos, subscription);
                            saveInFile();
                            Toast.makeText(EditSubscription.this,"Proceeding edit data.....",Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            // Learned from https://stackoverflow.com/questions/20231539/java-check-the-date-format-of-current-string-is-according-to-required-format-or
                            Date temp_date = null;
                            try{
                                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                temp_date = sdf.parse(dateString);
                                if (!dateString.equals(sdf.format(temp_date))) {
                                    Toast.makeText(EditSubscription.this,"Wrong date format, deafult to current date",Toast.LENGTH_LONG).show();
                                    Date updateDate = new Date();
                                    dateString = sdf.format(updateDate);
                                }else{
                                    dateString = sdf.format(temp_date);
                                    comment = reComment.getText().toString();
                                    Subscription subscription = new Subscription(name, Float.parseFloat(charge), dateString, comment);

                                    Subscriptions.set(pos, subscription);
                                    saveInFile();
                                    Toast.makeText(EditSubscription.this,"Proceeding edit data.....",Toast.LENGTH_LONG).show();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            } catch (ParseException pe){
                                Toast.makeText(EditSubscription.this,"Invalid date format",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                    }
                }
            }
        });
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
