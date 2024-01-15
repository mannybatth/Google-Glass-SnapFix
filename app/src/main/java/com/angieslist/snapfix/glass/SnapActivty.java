package com.angieslist.snapfix.glass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.angieslist.snapfix.glass.net.ServiceRequest;
import com.glasscamerasnapshot.GlassSnapshotActivity;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.io.File;


public class SnapActivty extends Activity {
    private static final String TAG = "SnapFixGlass";

    private static final int TAKE_PHOTO_CODE = 1;
    private static final String IMAGE_FILE_NAME = "/sdcard/ImageTest.jpg";

    private boolean picTaken = false; // flag to indicate if we just returned from the picture taking intent
    private String theImageFile = ""; // this holds the name of the image that was returned by the camera

    private TextView text1;
    private TextView text2;

    private ProgressBar myProgressBar;
    protected boolean mbActive;

    private String inputQueryString;
    private String queryCategory;

    final Handler myHandler = new Handler(); // handles looking for the returned image file
    private int numberOfImageFileAttempts = 0;

    private String responseBody = "";

    private TextToSpeech mSpeech;

    private boolean readyForMenu = false;
    private boolean gotImageMatch = false;

    private GestureDetector mGestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_activty);

       // text1 = (TextView) findViewById(R.id.text1);
//        text2 = (TextView) findViewById(R.id.text2);
//        text1.setText("");
//        text2.setText("");
       // myProgressBar = (ProgressBar) findViewById(R.id.my_progressBar);
     //   LinearLayout llResult = (LinearLayout) findViewById(R.id.resultLinearLayout);
        //TextView tvResult = (TextView) findViewById(R.id.tap_instruction);
      //  llResult.setVisibility(View.INVISIBLE);
        //tvResult.setVisibility(View.INVISIBLE);
      //  myProgressBar.setVisibility(View.INVISIBLE);

        // Even though the text-to-speech engine is only used in response to a menu action, we
        // initialize it when the application starts so that we avoid delays that could occur
        // if we waited until it was needed to start it up


//        mSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                // Do nothing.
//            }
//        });
//
//        mGestureDetector = createGestureDetector(this);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!picTaken) {
            Intent intent = new Intent(this, GlassSnapshotActivity.class);
            intent.putExtra("imageFileName",IMAGE_FILE_NAME);
            intent.putExtra("previewWidth", 640);
            intent.putExtra("previewHeight", 360);
            intent.putExtra("snapshotWidth", 1280);
            intent.putExtra("snapshotHeight", 720);
            intent.putExtra("maximumWaitTimeForCamera", 2000);
            startActivityForResult(intent,1);
        }
        else {
            // do nothing
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        picTaken = true;
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    Log.d(TAG,"onActivityResult");

                    File f = new File(IMAGE_FILE_NAME);
                    if (f.exists()) {
                        Log.d(TAG,"image file from camera was found");

                        Bitmap b = BitmapFactory.decodeFile(IMAGE_FILE_NAME);
                        Log.d(TAG,"bmp width=" + b.getWidth() + " height=" + b.getHeight());
                        ImageView image = (ImageView) findViewById(R.id.bgPhoto);
                        image.setImageBitmap(b);

                     //   text1 = (TextView) findViewById(R.id.text1);
//                        text2 = (TextView) findViewById(R.id.text2);
//                        text1.setText("The image shown was saved successfully to a file named:");
//                        text2.setText("\n" + IMAGE_FILE_NAME);


                       // voiceInput();
//                        Intent intent = new Intent(this, DescriptionActivity.class);
//
//                        IntentFilter filter = new IntentFilter();
//                        {
//                            filter.addAction("com.google.android.glass.action.VOICE_TRIGGER");
//                        }
//
//                        intent.putExtra("image", IMAGE_FILE_NAME);
//                        startActivity(intent);


                               Log.d(TAG, "Uploading media");

                        ServiceRequest.getInstance().submitServiceRequest(getApplicationContext(), IMAGE_FILE_NAME);
//        ServiceRequest.getInstance().uploadMedia(getApplicationContext(), "3", "image", "image.jpg", IMAGE_FILE_NAME);

                      //  LinearLayout llResult = (LinearLayout) findViewById(R.id.resultLinearLayout);
                      //  llResult.setVisibility(View.VISIBLE);
                      //  TextView line1 = (TextView) findViewById(R.id.titleOfWork);
                      //  TextView line2 = (TextView) findViewById(R.id.Singer);
                        //TextView tap = (TextView) findViewById(R.id.tap_instruction);
                      //  line1.setText("");
                      //  line2.setText("");
                        //tap.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Log.v(TAG,"onActivityResult returned bad result code");
                    finish();
                }
                break;
            }
        }
    }

    void voiceInput() {
        Intent intent = new Intent(this, DescriptionActivity.class);
        // ???

        //startActivityForResult(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        startActivity(intent.setFlags(    // Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    // do something on tap
                    Log.v(TAG,"tap");
                    //if (readyForMenu) {
                    openOptionsMenu();
                    //}
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    return true;
                }
                return false;
            }
        });
        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
            }
        });
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling
                return false;
            }
        });
        return gestureDetector;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.snap_activty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
