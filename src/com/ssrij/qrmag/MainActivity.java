
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	// Required vars
	int voltimes = 0;
	boolean funnylmsg = false;
	boolean networkAvail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		checkNetworkAvail();    
		// Initialize animations
		Animation a = new TranslateAnimation(1000,0,0,0);
		Animation a1 = new TranslateAnimation(1000,0,0,0);
		Animation a2 = new TranslateAnimation(1000,0,0,0);
		Animation a3 = new TranslateAnimation(1000,0,0,0);
		
		// Set animation durations (ms)
		a.setDuration(1200);
		a1.setDuration(1400);
		a2.setDuration(1600);
		a3.setDuration(1800);

		// Get a reference to the objects we want to apply the animation to
		final TextView v = (TextView)findViewById(R.id.textView1);
		final TextView v1 = (TextView)findViewById(R.id.textView2);
		final TextView v2 = (TextView)findViewById(R.id.TextView3);
		final Button v3 = (Button)findViewById(R.id.tap_scan);
		
		// Clear existing animations, just in case...
		v.clearAnimation();
		v1.clearAnimation();
		v2.clearAnimation();
		v3.clearAnimation();
		
		// Start animating
        v.startAnimation(a);
        v1.startAnimation(a1);
        v2.startAnimation(a2);
        v3.startAnimation(a3);
	
        a3.setAnimationListener(new AnimationListener() {    
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
            if (networkAvail == false) { ShowNetworkError(); }
            }
        });
	}
	
	public void ScanQr(View v) {
		// Check if internet is available and open the QR scan page if it is
		checkNetworkAvail();
		if (!networkAvail == false) { 
		Intent a = new Intent(MainActivity.this, ScanActivity.class);
		a.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if (funnylmsg == true) { a.putExtra("useFunnyLoadingMessage", true); }
        startActivity(a); } 
		// Internet isn't available, alert user
		else { ShowNetworkError(); }
	}
	
	// Easter egg: Show a witty loading message on the loading screen 
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
  	  
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){ voltimes = voltimes + 1; }
      	if (voltimes == 3) { funnylmsg = true;
      	Toast.makeText(getApplicationContext(), "Funny loading message easter egg activated! ", Toast.LENGTH_LONG).show(); }
        return true;
        
    }
    
    @SuppressWarnings("deprecation")
	public void ShowNetworkError() {
    // Tell the user that internet isn't available on the device
    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setTitle("Argh");
    alertDialog.setMessage("You must be connected to the internet in order to use this application: Please make sure WiFi or mobile data is turned on and working.");
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {}});
    alertDialog.setIcon(R.drawable.ic_launcher);
    alertDialog.show();
    }
    
    public void checkNetworkAvail() {
    // Check if internet is available or not
    	ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) { networkAvail = true; } else { networkAvail = false; }
    }

}
