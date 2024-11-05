package com.example.bt6;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ShowMessageActivity extends Activity {
    private Button btnBack;
    private static final int REQUEST_SMS_PERMISSION = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);

        btnBack = findViewById(R.id.btnback);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS}, REQUEST_SMS_PERMISSION);
        } else {
            readSMSMessages();
        }

    }

    public void readSMSMessages() {


        Uri uri = Uri.parse("content://sms/inbox");
        String[] projection = new String[] {
                "_id",
                "address",
                "body",
                "date"
        };
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                int addressIndex = cursor.getColumnIndex("address");
                int bodyIndex = cursor.getColumnIndex("body");

                if (addressIndex != -1 && bodyIndex != -1) {
                    String address = cursor.getString(addressIndex);
                    String body = cursor.getString(bodyIndex);
                    list.add("Người gửi: " + address + "\nNội dung: " + body);
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "Không tim thấy", Toast.LENGTH_SHORT).show();
        }

        ListView listView = findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSMSMessages();
            } else {
                // Xử lý khi người dùng từ chối cấp quyền
                Toast.makeText(this, "Permission denied to read SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}