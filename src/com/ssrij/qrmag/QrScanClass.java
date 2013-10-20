
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

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class QrScanClass extends SurfaceView implements SurfaceHolder.Callback {
	
	// Some variables we need
    private SurfaceHolder sfHold;
    private Camera sCam;
    private PreviewCallback callbackPrev;
    private AutoFocusCallback callbackAF;

    @SuppressWarnings("deprecation")
	public QrScanClass(Context context, Camera camera,
                         PreviewCallback cbPrev,
                         AutoFocusCallback cbAutoF) {
        
    	super(context);
    	
        sCam = camera;
        callbackPrev = cbPrev;
        callbackAF = cbAutoF;
        
        sfHold = getHolder(); // Get SurfaceHolder
        sfHold.addCallback(this); // Add SurfaceHolder callback in order to get notified when surface is destroyed

        sfHold.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  // Required for Honeycomb and below (**deprecated**)
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            sCam.setPreviewDisplay(holder); // Draw the preview on the surface
        } catch (IOException e) {
            Log.d("DEBUG", "An error occured while setting the camera preview: " + e.getMessage()); // Something bad happened
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface destroyed
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (sfHold.getSurface() == null){
          return; // The surface doesn't exist
        }

        try {
            sCam.stopPreview();  // Stop preview to make changes
        } catch (Exception e){
          // Do nothing since we just tried to stop a preview which does not exist
        }

        try {
            sCam.setDisplayOrientation(90);  // Force portrait (90 degrees)
            sCam.setPreviewDisplay(sfHold); // Set the surface that needs to be used for the preview
            sCam.setPreviewCallback(callbackPrev); // Install preview callback
            sCam.startPreview(); // Start previewing
            sCam.autoFocus(callbackAF); // Start auto focus
        } catch (Exception e){
        	Log.d("DEBUG", "An error occured while setting the camera preview: " + e.getMessage()); // Something bad happened
        }
    }
}
