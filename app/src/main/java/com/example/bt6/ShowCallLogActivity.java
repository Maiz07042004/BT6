package com.example.bt6;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;

public class ShowCallLogActivity extends AppCompatActivity {

    private Button btnBack;
    private static final int REQUEST_READ_CALL_LOG = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_call_log);

        btnBack = findViewById(R.id.btnback);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Kiểm tra và yêu cầu quyền truy cập Call Log
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_READ_CALL_LOG);
        } else {
            showAllCallLogs();
        }
    }

    public void showAllCallLogs() {
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[] {
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        };
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                @SuppressLint("Range") long dateMillis = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));

                String callType;
                switch (type) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "Cuộc gọi đi";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "Cuộc gọi đến";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "Cuộc gọi nhỡ";
                        break;
                    default:
                        callType = "Không xác định";
                        break;
                }

                Date date = new Date(dateMillis);
                list.add("Số: " + number + "\nLoại: " + callType + "\nNgày: " + date);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
        }

        ListView listView = findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }




}