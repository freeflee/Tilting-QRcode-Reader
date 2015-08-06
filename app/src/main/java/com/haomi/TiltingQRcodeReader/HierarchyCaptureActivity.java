package com.haomi.TiltingQRcodeReader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class HierarchyCaptureActivity extends Activity {

    private static final String TAG = HierarchyCaptureActivity.class.getSimpleName();
    private CompoundBarcodeView barcodeView;
    private RotationAngle rotationAngle = new RotationAngle();
    private MessageHierarchy messageHierarchy = new MessageHierarchy();
    private boolean isPause = true;
    private int messageIndex = 0;
    private View pauseView;
    private View runtimePreview;
    private ImageView imagepauseView;
    private TextView resultpauseView;
    private TextView anglepauseView;
    private TextView messageamountpauseView;
    private String anglepause;
    private Bitmap imagepause;
    private String resultpause;
    private String messageamountpause;
    private Button StartOrPause;
    //private View imageLayout;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            //Added preview of Rotation Angle
            TextView angleView = (TextView) findViewById(R.id.angle_preview);


            //Added preview of Result
            TextView statusView = (TextView) findViewById(R.id.status_preview);
            TextView resultView = (TextView) findViewById(R.id.result_preview);
            //Added preview of scanned barcode
            //ImageView imageView = (ImageView) findViewById(R.id.image_preview);
            //imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));

            //Added preview of pause
            runtimePreview = findViewById(R.id.runtimepreview_layout);
            pauseView = findViewById(R.id.pausepreview_layout);
            //imageLayout = findViewById(R.id.imageLayout);
            anglepauseView = (TextView) findViewById(R.id.angle_pausepreview);
            resultpauseView= (TextView) findViewById(R.id.result_pausepreview);
            messageamountpauseView= (TextView) findViewById(R.id.messageamount_pausepreview);

            imagepauseView= (ImageView) findViewById(R.id.image_pausepreview);
            imagepause = result.getBitmapWithResultPoints(Color.YELLOW);



            Log.d("Haomi","View set OK");

            if (result.getResult() != null && result.getResultPoints().length >= 3) {
                Log.d("Haomi", "Result is not null");
                //resultView.setText(result.getText());
                angleView.setText(rotationAngle.GetRotationAngleWithFormat(result) + String.valueOf((char) 176));
                anglepause = rotationAngle.GetRotationAngleWithFormat(result) + String.valueOf((char) 176);
                messageHierarchy.MessageSorting(result);
                Log.d("Haomi","Sorting finished");
                if (messageHierarchy.messageAmount == 1) {
                    statusView.setText("There's only one message in this QR Code.");
                    resultView.setText("Message:\n" + result.getText());
                    messageamountpause = "Message:";
                    resultpause = result.getText();
                }

                if (messageHierarchy.messageAmount > 1) {
                    statusView.setText("There are " + messageHierarchy.messageAmount + " messages in this QR Code within " + rotationAngle.AngleFormatChange(messageHierarchy.AngleRange) + String.valueOf((char) 176) + ".");
                    while (messageIndex < messageHierarchy.messageAmount) {
                        if (rotationAngle.GetRawRotationAngle(result) >= messageHierarchy.AngleBoarder[messageIndex] && rotationAngle.GetRawRotationAngle(result) < messageHierarchy.AngleBoarder[messageIndex+1]) {
                            resultView.setText("Message "+ (messageIndex+1) +":\n" + messageHierarchy.MessageGroup[messageIndex]);
                            messageamountpause = "Message "+ (messageIndex+1) + ":";
                            resultpause = messageHierarchy.MessageGroup[messageIndex];
                            break;

                        }
                        messageIndex++;
                    }
                    messageIndex = 0;
                    if (rotationAngle.GetRawRotationAngle(result) > messageHierarchy.AngleBoarder[messageHierarchy.messageAmount]){
                        resultView.setText("For this QR Code, Messages are within 0"+ String.valueOf((char) 176) + " and " + rotationAngle.AngleFormatChange(messageHierarchy.AngleRange) + String.valueOf((char) 176) + ".");
                        resultpause = "Sorry, for this QR Code, Messages are within 0"+ String.valueOf((char) 176) + " and " + rotationAngle.AngleFormatChange(messageHierarchy.AngleRange) + String.valueOf((char) 176) + ".";
                    }
                }


            }
        }
        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hierarchy_scan);

        barcodeView = (CompoundBarcodeView) findViewById(R.id.barcode_scanner);//barcode scanner points to CompoundBarcodeView.class
        barcodeView.decodeContinuous(callback);

        StartOrPause = (Button) findViewById(R.id.start_or_pause);
        StartOrPause.setBackgroundResource(R.drawable.pause_circular_button_128x128);
        StartOrPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isPause) {
                    StartOrPause.setBackgroundResource(R.drawable.start_circular_button_128x128);
                    if (resultpause!= null) {
                        pauseView.setVisibility(View.VISIBLE);
                        runtimePreview.setVisibility(View.INVISIBLE);
                    //imageLayout.setVisibility(View.GONE);
                        imagepauseView.setImageBitmap(imagepause);
                        messageamountpauseView.setText(messageamountpause);
                        resultpauseView.setText(resultpause);
                        anglepauseView.setText(anglepause);
                        Log.d("Haomi", "pause preview");
                        Log.d("Haomi",resultpause);
                    }
                    barcodeView.pause();
                }else{
                    barcodeView.resume();
                    StartOrPause.setBackgroundResource(R.drawable.pause_circular_button_128x128);

                    if (resultpause!= null) {
                        pauseView.setVisibility(View.GONE);
                        runtimePreview.setVisibility(View.VISIBLE);
                        //imageLayout.setVisibility(View.VISIBLE);
                        messageamountpauseView.setText(null);
                        imagepauseView.setImageBitmap(null);
                        resultpauseView.setText(null);
                        anglepauseView.setText(null);
                        resultpause = null;
                    }

                }
                isPause = !isPause;
            }
        });
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
