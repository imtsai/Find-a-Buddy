package com.example.testing.findbuddies;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.app.PendingIntent.getActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    Button login, register;
    EditText un, pw;
    private static final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        login = (Button) findViewById(R.id.login);
        un = (EditText) findViewById(R.id.un);
        pw = (EditText) findViewById(R.id.pw);
        register = (Button) findViewById(R.id.register);
    }
    public void goLogin(View v) {
        final String checkUN = un.getText().toString();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Users");
        query.whereEqualTo("username",checkUN);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject currPerson, ParseException e) {
                if (currPerson == null) {
                    Log.d("score", "The getFirst request failed.");
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Username does not exist")
                            .setTitle("Login error");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Log.d("score", "Retrieved the object.");
                    String checkPW = pw.getText().toString();
                    String realPW = currPerson.getString("password");
                    if (realPW.matches(checkPW)) {
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("username", checkUN);
                        editor.putString("name", currPerson.getString("name"));
                        editor.putInt("maxSB", currPerson.getInt("maxSB"));
                        editor.putString("maxSB", currPerson.getString("cell"));

                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, prefs.class);
                        LoginActivity.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Password incorrect")
                                .setTitle("Login error");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
        });
    }
    public void goRegister(View v) {
        Intent intent = new Intent(this, register.class);
        this.startActivity(intent);
//                finish();
    }
}

