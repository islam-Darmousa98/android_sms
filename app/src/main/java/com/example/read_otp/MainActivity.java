package com.example.read_otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    //TextView txturl;
    Button send_txt;
    EditText txt_ip;
    EditText txt_port;
    EditText txt_mob;
    EditText txtplate;
    EditText txt_url;
    String url=null;

    //private MyReceiver BR = null;
    private static MainActivity inst;

    public static MainActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_ip=findViewById(R.id.txt_ip);
        txt_port=findViewById(R.id.txt_port);
        txt_mob=findViewById(R.id.txt_mobile);
        txtplate=findViewById(R.id.txt_plate);
        txt_url=findViewById(R.id.txt_url);
        //new UserNameToId().execute(txt_url.getText().toString().trim());
        send_txt=findViewById(R.id.btn_send_sms);

       // txt_url.setText(url);

        requestsmspermission();
       // change_IP_Port();

        url=change_IP_Port( String.valueOf(txt_ip.getText()),String.valueOf(txt_port.getText()));
        //txt_url.setText(url);
        //url="http://"+txt_ip.getText()+":"+txt_port.getText()+"/smsQuery?";

        new MyReceiver().setEditText(txt_mob);//for mobile number
        new MyReceiver().setEditText1(txtplate);//for plate number
        new MyReceiver().setEditText2(txt_url);//for url api
       // new MyReceiver().setURL(txt_url);
        new MyReceiver().setIP(txt_ip.getText());
        new MyReceiver().setPort(txt_port.getText());
        //new MyReceiver().setEdittext3( txt_ip.getText());
       // new MyReceiver().setEdittext4((EditText) txt_port.getText());
        //new MyReceiver();


        send_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // new UserNameToId().execute(txt_url.getText().toString());
               // txt_ip.setText(txt_ip.getText().toString());
               // txt_port.setText(txt_port.getText().toString());
                new MyReceiver().setEditText(txt_mob);//for mobile number
                new MyReceiver().setEditText1(txtplate);//for plate number
                new MyReceiver().setEditText2(txt_url);//for url api
                new MyReceiver().setURL(txt_url);
               txt_mob.setText("");
               txtplate.setText("");
                txt_url.setText("");
            }
        });
        txt_url.setText("");

    }

    public String change_IP_Port(String ip,String port) {
        //txt_ip.setText(txt_ip.getText());
        //txt_port.setText(txt_port.getText());
        return "http://"+ip+":"+port+"/smsQuery?";
    }

    private void requestsmspermission() {
        String smspermission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this,smspermission);
        //check if read SMS permission is granted or not
        if(grant!= PackageManager.PERMISSION_GRANTED)
        {
            String[] permission_list = new String[1];
            permission_list[0]=smspermission;
            ActivityCompat.requestPermissions(this,permission_list,1);
        }
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                txtplate.setText(message);
                // message is the fetching OTP
            }
        }
    };
     class UserNameToId extends AsyncTask<String, Void, String> {

        String str = "";
        @Override
        public String doInBackground(String... url) {
            try {
//                showContacts();
                URL Url = new URL(url[0]);
                URLConnection urlConnection = Url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                str = bufferedReader.readLine();
                return str;


            } catch (Exception e) {
                e.printStackTrace();
            }
            return str;
            // startService(new Intent(MainActivity.this, myService.class));
            //return String.valueOf(new UserNameToId().execute(url[0]).toString());

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),
                    "String retrived:" + txt_url.getText().toString(), Toast.LENGTH_SHORT).show();
           // txtUserid = (TextView) findViewById(R.id.txtUserId);

            txt_url.setText(url);

        }
    }
}