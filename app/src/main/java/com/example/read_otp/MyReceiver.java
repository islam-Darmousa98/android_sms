package com.example.read_otp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MyReceiver extends BroadcastReceiver  {
    final SmsManager sms = SmsManager.getDefault();
    private  static EditText editText_ip,editText_port,editText_mobile,editText_plate,edit_url;
    public static String url="",ip,port;
    public void setEditText(EditText editText)
    {
        MyReceiver.editText_mobile=editText;
    }
    public void setEditText1(EditText editText)
    {
        MyReceiver.editText_plate=editText;
    }
    public void setEditText2(EditText edit_url)
    {
        MyReceiver.edit_url=edit_url;
    }
    public void setURL(EditText txt_url) {
        MyReceiver.url=txt_url.getText().toString();
    }
    public void setUrl(String url) {
        MyReceiver.url=url;
    }
    public void setIP(Editable text) {
        MyReceiver.ip=text.toString();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        String mobile=null,msg=null;
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for(SmsMessage sms : messages)
        {
             mobile=sms.getOriginatingAddress();
            if (mobile.startsWith("+972") || mobile.startsWith("+970")) {
                mobile = mobile.replace("+972", "0");
                mobile = mobile.replace("+970", "0");
            }
             msg = sms.getMessageBody();
            String data=mobile+"\n"+msg;
            // here we are spliting the sms using " : " symbol

            editText_mobile.setText(mobile);
            editText_plate.setText(msg);


        }

        url += "http://"+ip+":"+port+"/smsQuery?"+"phoneNo=" +String.valueOf(mobile) + "&plateNo=" + String.valueOf(msg.toUpperCase());
       // url+="http://"+editText_ip.getText()+":"+editText_port.getText()+"/smsQuery?"+"phoneNo=" +String.valueOf(mobile) + "&plateNo=" + String.valueOf(msg.toUpperCase());
        edit_url.setText(url);
        new Excetion().execute(url);
        url="";
        //edit_url.setText("");
    }

    public void setPort(Editable text) {
        MyReceiver.port=text.toString();
    }


    class Excetion extends AsyncTask<String, Void, String> {

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
            Log.d("TAG","String retrived:" );


            edit_url.setText(url);

        }
    }
}
