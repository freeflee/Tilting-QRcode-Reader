package com.haomi.TiltingQRcodeReader;


import android.util.Log;
import android.util.Patterns;

import com.journeyapps.barcodescanner.BarcodeResult;


/**
 * Created by Haomi on 25/6/2015.
 */
public class MessageHierarchy {

    public String[] MessageGroup;
    public int messageAmount;
    public double[] AngleBoarder = new double[180];
    public double UnitAngle;
    public double AngleRange;
    private String url;



    public void MessageSorting (BarcodeResult result) {

        MessageGroup = result.getText().split("\\|");
        Log.d("Haomi", "split finish");
        if (MessageGroup.length == 1) {
            messageAmount = 1;
        }
        if (MessageGroup.length > 1) {
            messageAmount = MessageGroup.length - 1;
        }
        if (messageAmount > 1) {
            AngleRange = Double.parseDouble(MessageGroup[MessageGroup.length - 1]);
            UnitAngle = AngleRange / messageAmount;
            Log.d("Haomi", Double.toString(UnitAngle));

            for (int i = 0; i <= messageAmount; i++) {
                AngleBoarder[i] = UnitAngle * i;
                Log.d("Haomi", Double.toString(i));
                Log.d("Haomi", Double.toString(AngleBoarder[i]));

            }
        }
    }

    public String Messagehandle(String message) {
        String uri = message;
        if (Patterns.WEB_URL.matcher(message).matches()) {
            if (!(message.startsWith("http") || message.startsWith("HTTP"))) {
                uri = "http://" + message;
            }
            return uri;
        }
        else if (Patterns.PHONE.matcher(message).matches()) {
            uri = "tel:" + message;
            return uri;
        }
        else if (Patterns.EMAIL_ADDRESS.matcher(message).matches()) {
            uri = "mailto:" + message;
            return uri;
        }
        else {
            return uri;
        }

    }





}



