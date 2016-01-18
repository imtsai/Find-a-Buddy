package com.example.testing.findbuddies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class results extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefsFile";
    ArrayList<String> results = new ArrayList<>();
    HashMap<String, String> cells = new HashMap<>();
    Set<String> chosenClasses = new HashSet<>();
    String currUN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        runOnUiThread(updateTextView);
    }
    public Runnable updateTextView = new Runnable() {
        @Override
        public void run() {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            chosenClasses = settings.getStringSet("allClasses", null);
            String currLocation = settings.getString("location", "");
            currUN = settings.getString("username","");
            if (chosenClasses!=null && !currLocation.matches("")) {
                Spinner spinner = (Spinner) findViewById(R.id.textSpinner);
                final ArrayAdapter<CharSequence> adapter = new ArrayAdapter(results.this, android.R.layout.simple_spinner_item, new ArrayList<CharSequence>());
                for (String key : chosenClasses) {
                    final String currKey = key;
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(key);
                    query.whereEqualTo("location", currLocation);
                    query.whereNotEqualTo("username", currUN);
                    query.whereEqualTo("date", "" + Calendar.MONTH + ", " + Calendar.DAY_OF_MONTH);
                    query.whereGreaterThanOrEqualTo("hour", Calendar.HOUR - 1);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> possiblePartners, ParseException e) {
                            if (e == null) {
                                for (ParseObject currPossibility : possiblePartners) {
                                    cells.put(currKey+":"+currPossibility.getString("name"),currPossibility.getString("cell"));
                                    adapter.add(currKey+":"+currPossibility.getString("name"));
                                }
                            }
                        }
                    });
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }
    };

    public void goText(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.textSpinner);
        String currentChoice = spinner.getSelectedItem().toString();
        String getCell = cells.get(currentChoice);
        SmsManager smsManager = SmsManager.getDefault();
        String[] className = currentChoice.split(":");
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//
        String mssg= " Hello"+className[1] +", would you like to study " + className[0] + " with me @ " + settings.getString("location","") + " ? ";
        mssg+="My name is " + settings.getString("name","");
        smsManager.sendTextMessage(getCell, null, mssg, null, null);
    }

    public void quit(View view) {
        invalidate(view);
    }
    public void invalidate(View view) {
        for (String key : chosenClasses) {
            final String currKey = key;
            ParseQuery<ParseObject> query = ParseQuery.getQuery(key);
            query.whereEqualTo("username", currUN);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> ownObjects, ParseException e) {
                    if (e == null) {
                        for (ParseObject currObject : ownObjects) {
                            try {
                                currObject.delete();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }
}
