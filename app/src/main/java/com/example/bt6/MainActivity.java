package com.example.bt6;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnShowAllContact;
    private Button btnAccessCallLog;
    private Button btnShowMess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowAllContact = findViewById(R.id.btnshowallcontact);
        btnShowAllContact.setOnClickListener(this);
        btnAccessCallLog = findViewById(R.id.btnaccesscalllog);
        btnAccessCallLog.setOnClickListener(this);
        btnShowMess = findViewById(R.id.btnShowMess);
        btnShowMess.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == btnShowAllContact) {
            Intent intent = new Intent(this, ShowAllContactActivity.class);
            startActivity(intent);
        } else if (v == btnAccessCallLog) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            } else {
                Intent intent = new Intent(this, ShowCallLogActivity.class);
                startActivity(intent);
            }
        } else if (v == btnShowMess) {
        Intent intent = new Intent(this, ShowMessageActivity.class);
        startActivity(intent);}
    }

    public void accessTheCallLog() {
        String[] projection = new String[]{
                CallLog.Calls.DATE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DURATION
        };
        Cursor c = getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                projection,
                CallLog.Calls.DURATION + "<?", new String[]{"30"},
                CallLog.Calls.DATE + " ASC");

        if (c != null) {
            StringBuilder s = new StringBuilder();
            while (c.moveToNext()) {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    s.append(c.getString(i)).append(" - ");
                }
                s.append("\n");
            }
            Toast.makeText(this, s.toString(), Toast.LENGTH_LONG).show();
            c.close();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessTheCallLog();
            } else {
                Toast.makeText(this, "Permission denied to read call log", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
