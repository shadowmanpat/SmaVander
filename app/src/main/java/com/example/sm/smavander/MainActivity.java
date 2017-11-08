package com.example.sm.smavander;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    Button buttonSend;
    EditText textPhoneNo;
    EditText textSMS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSend = (Button) findViewById(R.id.buttonSend);
        textPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        textSMS = (EditText) findViewById(R.id.editTextSMS);

        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                methodRequiresTwoPermission();


            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    int RC_CAMERA_AND_LOCATION =2;
    @AfterPermissionGranted(2)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.SEND_SMS};
        if (EasyPermissions.hasPermissions(this, perms)) {

            String phoneNo = textPhoneNo.getText().toString();
            String sms = textSMS.getText().toString();

            try {
                SmsManager smsManager = SmsManager.getDefault();
                String numbers[] = {"1223456", "5456456123"};

                for(String number : numbers) {
                    smsManager.sendTextMessage(number, null, sms, null, null);
                }


                Toast.makeText(getApplicationContext(), "SMS Sent!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG).show();
                Log.d("state", "SMS fail "+e.toString());
                e.printStackTrace();
            }

            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,"give me some perms pleaseee",
                    2, perms);
        }
    }
}