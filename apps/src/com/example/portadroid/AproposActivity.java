package com.example.portadroid;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class AproposActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apropos);
	}
	 public void fb(View view){
	        Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/euroi"));
	        startActivity(fbIntent);
	    }

	    public void tweet(View view){
	        Intent tweetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/euroi/"));
	        startActivity(tweetIntent);
	    }

	    public void insta(View view){
	        Intent instaIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/euroi/"));
	        startActivity(instaIntent);
	    }
}
