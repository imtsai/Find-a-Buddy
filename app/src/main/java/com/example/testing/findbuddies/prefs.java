package com.example.testing.findbuddies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class prefs extends AppCompatActivity{
    Set<String> allClasses = new HashSet<>();
    private static final String PREFS_NAME = "MyPrefsFile";
    String currUN;
    String currLoc;
    String currName;
    String currNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        currUN = settings.getString("username","");
        currName = settings.getString("name","");
        currNum = settings.getString("cell","");
        Spinner spinner = (Spinner) findViewById(R.id.todaySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.classChoices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner = (Spinner) findViewById(R.id.locSpinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.locChoices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    public void addClass(View view){
        Spinner spinner = (Spinner) findViewById(R.id.todaySpinner);
        String newClass = spinner.getSelectedItem().toString();
        EditText chosenClasses = (EditText)findViewById(R.id.chosenClasses);
        String chosenClassesList = chosenClasses.getText().toString();
        Spinner locSpinner = (Spinner) findViewById(R.id.locSpinner);
        currLoc = locSpinner.getSelectedItem().toString();
        if (currLoc==null) {
            Toast.makeText(getApplicationContext(), "please select a location", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("location", currLoc);
        editor.apply();
        if (chosenClassesList.contains(newClass)) {
            Toast.makeText(this, "already added this class", Toast.LENGTH_SHORT).show();
        } else {
            chosenClassesList+=newClass + "\n";
            chosenClasses.setText(chosenClassesList);
            allClasses.add(newClass);
            ParseObject newUser = new ParseObject(newClass);
            newUser.put("username", currUN);
            newUser.put("location",currLoc);
            newUser.put("name", currName);
            newUser.put("cell", currNum);
            newUser.put("date", "" + Calendar.MONTH +", " + Calendar.DAY_OF_MONTH);
            newUser.put("hour", Calendar.HOUR);
            newUser.saveInBackground();
        }
    }

    public void seeResults(View view){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet("allClasses", allClasses);
        editor.apply();
        Intent intent = new Intent(this, results.class);
        this.startActivity(intent);
    }
}
