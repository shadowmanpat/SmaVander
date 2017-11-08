package com.example.sm.smavander;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
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

        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String phoneNo = "Phone number to sent";
                String message = "Your message";
                if (phoneNo.length() > 0 && message.length() > 0) {
                    TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    int simState = telMgr.getSimState();
                    switch (simState) {
                        case TelephonyManager.SIM_STATE_ABSENT:
                            textSMS.setText("SIM_STATE_ABSENT");
                            displayAlert();
                            break;
                        case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                            textSMS.setText("SIM_STATE_NETWORK_LOCKED");
                            // do something
                            break;
                        case TelephonyManager.SIM_STATE_PIN_REQUIRED:  textSMS.setText("SIM_STATE_PIN_REQUIRED");
                            // do something
                            break;
                        case TelephonyManager.SIM_STATE_PUK_REQUIRED:  textSMS.setText("SIM_STATE_PUK_REQUIRED");
                            // do something
                            break;
                        case TelephonyManager.SIM_STATE_READY:  textSMS.setText("SIM_STATE_READY");
                            // do something
                            sendSMS(phoneNo, message); // method to send message
                            break;
                        case TelephonyManager.SIM_STATE_UNKNOWN:  textSMS.setText("SIM_STATE_UNKNOWN");
                            // do something
                            break;
                    }

                }

            }

            private void displayAlert() {
                // TODO Auto-generated method stub

                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Sim card not available")
                        .setCancelable(false)
                        // .setIcon(R.drawable.alert)
                        .setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        textSMS.setText("\"I am inside ok\"");
                                        Log.d("I am inside ok", "ok");
                                        dialog.cancel();
                                    }
                                })

                        .show();

            }

        });

    }
    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(MainActivity.this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(MainActivity.this,
                0, new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        final String string = "deprecation";
        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS sent 1",
                                Toast.LENGTH_SHORT).show();
                        textSMS.setText("SMS sent ");

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity.this, "Generic failure",
                                Toast.LENGTH_SHORT).show();textSMS.setText("Generic failure");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this, "No service",
                                Toast.LENGTH_SHORT).show();textSMS.setText("No service ");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainActivity.this, "Null PDU",
                                Toast.LENGTH_SHORT).show();textSMS.setText("Null PDU ");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();textSMS.setText("Radio off ");
                        break;

                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {

                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        Log.d("state","delivered");
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainActivity.this, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        Log.d("state"," not delivered");
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(textPhoneNo.getText().toString(), null, "gieadsafg", sentPI, deliveredPI);

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
        String[] perms = {Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE};
        if (EasyPermissions.hasPermissions(this, perms)) {

            String phoneNo = textPhoneNo.getText().toString();
            String sms = textSMS.getText().toString();
            sendSMS(phoneNo,sms);
//            try {
//                SmsManager smsManager = SmsManager.getDefault();
//                String numbers[] = {"1223456", "5456456123"};
//
//                smsManager.sendTextMessage(phoneNo, null, sms, null, null);
////                for(String number : numbers) {
////                    smsManager.sendTextMessage(number, null, sms, null, null);
////                }
//
//               // smsManager.getCarrierConfigValues();
//
//
//            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(),
//                        "SMS faild, please try again later!",
//                        Toast.LENGTH_LONG).show();
//                Log.d("state", "SMS fail "+e.toString());
//                e.printStackTrace();
//            }

            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,"give me some perms pleaseee",
                    2, perms);
        }
    }


}