package com.example.msgserviceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements OnClickListener {

    CheckBox checkBox;
    public static EditText editTextML;
    public static EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1000);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.SEND_SMS}, 1);
        }

        startService(new Intent(this, MyService.class));
        startForegroundService(new Intent(this, MyService.class));

        checkBox = (CheckBox) findViewById(R.id.checkBox1);
        editText = (EditText) findViewById(R.id.editText1);
        editTextML = (EditText) findViewById(R.id.editTextML);
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
        loadSavedPreferences();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000 || requestCode == 1) {
            if(this.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS SENDING PERMISSION GRANTED!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "SMS SENDING PERMISSION DENIED!", Toast.LENGTH_SHORT).show();
            }
            if(this.checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS RECEIVING PERMISSION GRANTED!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "SMS RECEIVING PERMISSION DENIED!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadSavedPreferences() {
        // REMOVE ANY PREVIOUS SAVED DATA ON THE DEVICE FIRST (IN-CASE)
        // SharedPreferences settings = this.getSharedPreferences("PreferencesName", this.MODE_PRIVATE);
        // settings.edit().remove("storedSenders").commit();
        // settings.edit().remove("storedReceiver").commit();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean checkBoxValue = sharedPreferences.getBoolean("CheckBox_Value", false);
        String senders = sharedPreferences.getString("storedSenders", "Enter Senders on Each Line!");
        String receiver = sharedPreferences.getString("storedReceiver", "Enter a Receiver!");
        checkBox.setChecked(checkBoxValue);

        editTextML.setText(senders);
        editText.setText(receiver);
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        savePreferences("CheckBox_Value", checkBox.isChecked());
        if (checkBox.isChecked()) {
            savePreferences("storedSenders", editTextML.getText().toString());
            savePreferences("storedReceiver", editText.getText().toString());

        }
//        startService(new Intent(this, MyService.class));

        finish();
    }

}
