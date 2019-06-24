package com.example.portadroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class SplashScreen extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		Thread timerThread = new Thread(){
		
		public void run(){
			try {
				sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				Intent intent = new Intent (SplashScreen.this, DeviceList.class);
				startActivity(intent);
			}
		}
	};
		timerThread.start();
	}
	
	 protected void onPause() {
		 super.onPause();
	     finish();
	 }
}