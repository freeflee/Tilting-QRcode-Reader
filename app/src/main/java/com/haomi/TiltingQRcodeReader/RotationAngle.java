package com.haomi.TiltingQRcodeReader;

import android.util.Log;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.text.DecimalFormat;

/**
 * Created by Haomi on 25/6/2015.
 * Get the rotation angle of QR code. Author:Haomi
 */
public class RotationAngle {

    private double RotationAngle;
    private String AngleWithFormat;
    private DecimalFormat AngleFormat;


    public double GetRawRotationAngle(BarcodeResult result) {
        CalculateRotationAngle(result);
        return RotationAngle;
    }

    public String GetRotationAngleWithFormat(BarcodeResult result) {
        CalculateRotationAngle(result);
        AngleFormatChange(RotationAngle);

        return AngleWithFormat;
    }

    public String AngleFormatChange (Double Angle){
        AngleFormat = new java.text.DecimalFormat("#0");
        AngleWithFormat = AngleFormat.format(Angle);
        return AngleWithFormat;
    }

    private double CalculateRotationAngle(BarcodeResult result) {
        ResultPoint[] points = result.getResultPoints();
        ResultPoint a = points[0];
        ResultPoint b = points[1];
        ResultPoint c = points[2];
            //Find the degree of the rotation that is needed
        double x = Math.abs(b.getX() - c.getX());
        double y = Math.abs(b.getY() - c.getY());
        RotationAngle = Math.toDegrees(Math.atan(y / x));
        if (c.getX() < b.getX()) {//-+
            RotationAngle = 180 - RotationAngle;
        }
        Log.d("Rotation", Double.toString(RotationAngle));
        Log.d("Rotation", "Point a(" + Double.toString(a.getX()) + "," + Double.toString(a.getY()) + ")" +
                "; Point b(" + Double.toString(b.getX()) + "," + Double.toString(b.getY()) + ")" +
                ": Point c(" + Double.toString(c.getX()) + "," + Double.toString(c.getY()) + ")");
        // Log.d("Rotation", "Point b:(" + Double.toString(b.getX()) + "," + Double.toString(b.getY()) + ")" );
        // Log.d("Rotation", "Point c:(" + Double.toString(c.getX()) + "," + Double.toString(c.getY()) + ")" );

        return RotationAngle;
    }
}

