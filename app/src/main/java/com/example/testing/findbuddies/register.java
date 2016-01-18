package com.example.testing.findbuddies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    public void saveOnParse(View view) {
        final EditText newUN = (EditText) findViewById(R.id.newUN);
        final String checkUN = newUN.getText().toString();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("username",checkUN);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    EditText newPW = (EditText) findViewById(R.id.newPW);
                    EditText newName = (EditText) findViewById(R.id.newName);
                    EditText maxSB = (EditText) findViewById(R.id.maxSB);
                    EditText newPhone = (EditText) findViewById(R.id.newNumber);
                    ParseObject newUser = new ParseObject("Users");
                    String curr = newUN.getText().toString();
                    if (!curr.matches("")){
                        newUser.put("username",curr);
                    }
                    curr = newPW.getText().toString();
                    if (!curr.matches("")){
                        newUser.put("password",curr);
                    }
                    curr = newName.getText().toString();
                    if (!curr.matches("")){
                        newUser.put("name",curr);
                    }
                    curr = maxSB.getText().toString();
                    if (!curr.matches("")){
                        int foo = Integer.parseInt(curr);
                        newUser.put("maxSB",foo);
                    }
                    curr = newPhone.getText().toString();
                    if (!curr.matches("")){
                        newUser.put("cel",curr);
                    }
                    newUser.saveInBackground();
                    Intent intent = new Intent(register.this, prefs.class);
                    register.this.startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                    builder.setMessage("Username already exists")
                            .setTitle("Registration error");
                    AlertDialog dialog = builder.create();
                    dialog.show();                }
            }
        });
    }
}
