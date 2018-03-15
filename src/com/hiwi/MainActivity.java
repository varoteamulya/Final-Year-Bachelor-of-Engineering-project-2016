package com.hiwi;

import org.apache.cordova.DroidGap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import org.opencv.javacv.facerecognition.R;

import com.hiwi.JSInterface;

public class MainActivity extends DroidGap{
JSInterface js;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.init(); 
		js=new JSInterface(this,appView);
		appView.addJavascriptInterface(js, "JSI");
		//setContentView(R.layout.activity_main);
		super.loadUrl("file:///android_asset/www/index.html",1000);
	}
}