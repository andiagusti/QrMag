
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

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;

public class OopsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oops);
		
		
        // Initialize animations
		Animation a = new TranslateAnimation(1000,0,0,0);
		Animation a1 = new TranslateAnimation(1000,0,0,0);
		Animation a2 = new TranslateAnimation(1000,0,0,0);
		
		// Set animation durations (ms)
		a.setDuration(1000);
		a1.setDuration(1200);
		a2.setDuration(1400);

		// Get a reference to the objects we want to apply the animation to
		TextView v = (TextView)findViewById(R.id.OopsTitle);
		TextView v1 = (TextView)findViewById(R.id.OopsMessage);
		Button v2 = (Button)findViewById(R.id.OopsTryAgain);
		
		// Clear existing animations, just in case...
		v.clearAnimation();
		v1.clearAnimation();
		v2.clearAnimation();
		
		// Start animating
        v.startAnimation(a);
        v1.startAnimation(a1);
        v2.startAnimation(a2);
	}
	
	 protected void onPause(){
	       super.onPause();
	 }
	 
	 public void TryAgain(View v) {
			// Open the QR Scan page
			Intent a = new Intent(OopsActivity.this, ScanActivity.class);
	        startActivity(a);
		}
	
}
