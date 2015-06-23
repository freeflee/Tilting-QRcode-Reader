package com.haomi.TiltingQRcodeReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.zxing.ResultPoint;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousCaptureActivity extends Activity {
    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private CompoundBarcodeView barcodeView;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getResult() != null) {
                //barcodeView.setStatusText(result.getText());
                final String[] messages = result.getText().split("\\|");
                java.text.DecimalFormat AngleFormat = new java.text.DecimalFormat("#0.00");
                if (getRotationAngle(result) >= 0 && getRotationAngle(result) < 45){
                    barcodeView.setStatusText("Result:" + messages[0] + "\nAngle:" + AngleFormat.format(getRotationAngle(result)));
                }
                else if (getRotationAngle(result) >= 45 && getRotationAngle(result) <= 90 ) {
                    if (messages.length > 1) {
                        barcodeView.setStatusText("Result:" + messages[1] + "\nAngle:" + AngleFormat.format(getRotationAngle(result)));
                        if (Patterns.WEB_URL.matcher(messages[1]).matches()) {
                            barcodeView.pause();
                            barcodeView.setStatusText("URL:" + messages[1] + "\nAngle:" + AngleFormat.format(getRotationAngle(result)));
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ContinuousCaptureActivity.this);
                            dialog.setTitle("Prompt");
                            dialog.setMessage("Detected a URL: " + messages[1] + "\nWould you like to visit?");
                            dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    // if(!messages[1].startsWith("www.")&& !messages[1].startsWith("http://")){
                                    //   messages[1] = "www." + messages[1];
                                    // }
                                    if (!messages[1].startsWith("http")) {
                                        messages[1] = "http://" + messages[1];
                                    }
                                    intent.setData(Uri.parse(messages[1]));
                                    startActivity(intent);
                                }
                            });
                            dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    barcodeView.resume();
                                }
                            });
                            dialog.show();
                            //Uri uri = Uri.parse(messages[1]);
                            //Intent it = new Intent(Intent.ACTION_VIEW,uri);
                            //startActivity(it);
                        }
                    } else {
                        barcodeView.setStatusText("Only One Result:" + messages[0] + "\nAngle:" + AngleFormat.format(getRotationAngle(result)));
                    }
                } else {
                    barcodeView.setStatusText("Only One Result:" + messages[0] + "\nAngle:" + AngleFormat.format(getRotationAngle(result)));
                }

                //barcodeView.setStatusText("Result:" + result.getText() + "\nAngle:" + AngleFormat.format(getRotationAngle(result)));
            }
            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));


        }

        //get the rotation angle of QR code. Author:Haomi
        public double getRotationAngle(BarcodeResult result){
            ResultPoint[] points = result.getResultPoints();
            ResultPoint a= points[0];
            ResultPoint b= points[1];
            ResultPoint c= points[2];
            // float distance = Math.abs(a.getX()-b.getX());
            // RectF rect = new RectF(a.getX(), a.getY(), a.getX()+distance, a.getY()+distance);
            //Find the degree of the rotation that is needed
            double x = Math.abs(b.getX() - c.getX());
            double y = Math.abs(b.getY() - c.getY());
            double theta = Math.toDegrees(Math.atan(y / x));
            if(c.getX() < b.getX()){//-+
                theta = 180 - theta;
            }
            Log.d("Rotation", Double.toString(theta));
            Log.d("Rotation", "Point a(" + Double.toString(a.getX()) + "," + Double.toString(a.getY()) + ")" +
                    "; Point b(" + Double.toString(b.getX()) + "," + Double.toString(b.getY()) + ")" +
                    ": Point c(" + Double.toString(c.getX()) + "," + Double.toString(c.getY()) + ")");
            // Log.d("Rotation", "Point b:(" + Double.toString(b.getX()) + "," + Double.toString(b.getY()) + ")" );
            // Log.d("Rotation", "Point c:(" + Double.toString(c.getX()) + "," + Double.toString(c.getY()) + ")" );
            return theta;
        }





        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.continuous_scan);

        barcodeView = (CompoundBarcodeView) findViewById(R.id.barcode_scanner);//barcode scanner points to CompoundBarcodeView.class
        barcodeView.decodeContinuous(callback);
    }


    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
