
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

public class LoadingActivity extends Activity {
	
	String v_tag = new String();
	boolean funnylmsg;
	String[] loading_msgs = {"Counting to infinity", "Hacking your parent's phone", "Waiting for the stars to align", "Waiting for the world to turn", "Reading your thoughts", "Checking how you look through your front camera", "Installing Symbian OS on your phone", "Donating your money to the poor people in Greece", "Uploading images from your camera roll", "Sending internet history to your mom", "Flushing all the battery life", "I like to keep you waiting"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		
		// Get tag from intent
		v_tag = getIntent().getStringExtra("tag");
		
		 try {
	    		funnylmsg = getIntent().getBooleanExtra("useFunnyLoadingMessage", false);
	    		} catch (Exception e) {
	    		 // Looks like the user didn't enable easter egg
	    			funnylmsg = false;
	    		}
		
        // Initialize animations
		Animation a = new TranslateAnimation(1000,0,0,0);
		Animation a1 = new TranslateAnimation(1000,0,0,0);
		
		// Set animation durations (ms)
		a.setDuration(1000);
		a1.setDuration(1200);

		// Get a reference to the objects we want to apply the animation to
		TextView v = (TextView)findViewById(R.id.Loading);
		TextView v1 = (TextView)findViewById(R.id.Loading1);
		
		// Check if user enabled easter egg
		
		if (funnylmsg == true) {
		
		// Randomly select a loading message and set
        Random rnd = new Random();
        v.setText(loading_msgs[rnd.nextInt(loading_msgs.length)]);
		
		}
		
		// Show normal loading message
		else { 
		v.setText("Loading video, please wait a moment");
		v1.setText("........");
		}
		
		// Clear existing animations, just in case...
		v.clearAnimation();
		v1.clearAnimation();
		
		// Start animating
        v.startAnimation(a);
        v1.startAnimation(a1);
	
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
        	
        	// Animation has ended, let's launch the video now
        	if (v_tag.equals("ad2012_intro")){
        	Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:WJRlcx-yCsI"));
        	i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        	startActivity(i);
        	
        	// More tags will be supported soon
        	
        	}
        	
        	else {
        		// Open the Error page
    			Intent a = new Intent(LoadingActivity.this, OopsActivity.class);
    			a.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	        startActivity(a);
        	}
         }
     });
	}
	
	 protected void onPause(){
	       super.onPause();
	 }
	
}
