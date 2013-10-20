
/*
    Copyright (c) 2013 Suyash Srijan
    suyashsrijan@outlook.com

    Permission is hereby granted, free of charge, to any person obtaining
    a copy of this application and associated documentation files (the "Application"),
    to deal in the Application without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Application, and to permit persons to whom the Application
    is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Application.

    THE APPLICATION IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
    EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
    OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
    CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE APPLICATION
    OR THE USE OR OTHER DEALINGS IN THE APPLICATION.
*/

package com.ssrij.qrmag;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScanActivity extends Activity
{
	// Some variables
    private Camera sCam;
    boolean funnylmsg;
    private QrScanClass qrPrev;
    private Handler handlerAF;
    Button retryButton;
    ImageScanner imgScanner;
    private boolean isQrScanned = false;
    private boolean isPreviewing = true;
   
    static {
    System.loadLibrary("iconv"); // Load the QR scanning library
    } 

    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        
        try {
    		funnylmsg = getIntent().getBooleanExtra("useFunnyLoadingMessage", false);
    		} catch (Exception e) {
    		 // Looks like the user didn't enable easter egg
    			funnylmsg = false;
    		}
    		
        // Initialize animations
        Animation a = new TranslateAnimation(1000,0,0,0); 
		Animation a1 = new TranslateAnimation(1000,0,0,0);
		Animation a2 = new TranslateAnimation(1000,0,0,0);
		
		// Set animation durations (ms)
		a.setDuration(1000);
		a1.setDuration(1200);
		a2.setDuration(1400);

		// Get a reference to the objects we want to apply the animation to
		TextView v = (TextView)findViewById(R.id.scanText);
		RelativeLayout v1 = (RelativeLayout)findViewById(R.id.cameraPreview);
		Button v3 = (Button)findViewById(R.id.ScanButton);
		
		// Clear existing animations, just in case...
		v.clearAnimation();
		v1.clearAnimation();
		v3.clearAnimation();
		
		// Start animating
        v.startAnimation(a);
        v1.startAnimation(a1);
        v3.startAnimation(a2);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Set orientation

        handlerAF = new Handler(); // Create handler
        sCam = fetchCamInst(); // Get camera instance

        // Initialize QR scanning
        imgScanner = new ImageScanner();
        imgScanner.setConfig(0, Config.X_DENSITY, 3);
        imgScanner.setConfig(0, Config.Y_DENSITY, 3);
        
        qrPrev = new QrScanClass(this, sCam, previewCb, autoFocusCB); // Initialize QR scanning class
        
        // Get a reference to the layout where we will be displaying the preview
        final RelativeLayout preview = (RelativeLayout)findViewById(R.id.cameraPreview);
        
        // Animation event handlers
        a1.setAnimationListener(new AnimationListener() {    
            @Override
            public void onAnimationStart(Animation animation) {  
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            	// Moved this line to here in order to make animations smoother// 
            	
                preview.addView(qrPrev); // Add the preview to the layout
            }
        });
        
        // Well, you should be now knowing what the next two lines of code do
        retryButton = (Button)findViewById(R.id.ScanButton);

        // Listen for retry button click
        retryButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (isQrScanned) {
                    	// Reset scanning
                        isQrScanned = false;
                        sCam.setPreviewCallback(previewCb);
                        sCam.startPreview();
                        isPreviewing = true;
                        sCam.autoFocus(autoFocusCB);
                    }
                }
            });
    }

    public void onPause() {
        super.onPause();
        liberateCam(); // Uninit camera to prevent crashing
    }

    //Get instance of the camera object safely
    public static Camera fetchCamInst(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    // Function that uninits the camera
    private void liberateCam() {
        if (sCam != null) {
        	RelativeLayout preview1 = (RelativeLayout)findViewById(R.id.cameraPreview);
            isPreviewing = false;
            sCam.setPreviewCallback(null);
            sCam.release();
            sCam = null;
            preview1.removeView(qrPrev);
        }
    }

    // Start autofocusing
    private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (isPreviewing)
                    sCam.autoFocus(autoFocusCB);
            }
        };
      
      

    // Preview callback which is called when QR code is recognize
    PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image qrcode = new Image(size.width, size.height, "Y800");
                qrcode.setData(data);

                int result = imgScanner.scanImage(qrcode);
                
                if (result != 0) {
                    isPreviewing = false;
                    sCam.setPreviewCallback(null);
                    sCam.stopPreview();
                    
                    SymbolSet syms = imgScanner.getResults();
                    for (Symbol sym : syms) {
                    	String scantag = new String();
                    	scantag = sym.getData().toString();
                    	isQrScanned = true;
                        Intent a = new Intent(ScanActivity.this, LoadingActivity.class);
                        a.putExtra("tag", scantag);
                        if (funnylmsg == true) { a.putExtra("useFunnyLoadingMessage", true); }
                        a.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(a);
                    } 
                }
            }
        };

    // Mimic continuous auto focus lol
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                handlerAF.postDelayed(doAutoFocus, 1000);
            }
        };
}
