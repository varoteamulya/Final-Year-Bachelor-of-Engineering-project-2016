package com.hiwi;

import org.apache.cordova.DroidGap;
import org.opencv.javacv.facerecognition.R;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;


public class  HeartbeatsActivity extends DroidGap {
	public static Context context;
	public static String macAddrp,macAddr,emaill,emaillp;
	BluetoothAdapter adapter = null;
	JSInterface js;
	public static SQLiteDatabase db;
	
    /** Called when the activity is first created. */
	 CustomNativeAccess cna;
	 //face recognition activity
	    @Override 
	    public void onCreate(Bundle savedInstanceState) {
	    	context= this.getApplicationContext();
//	    	macAddrp = BluetoothAdapter.getDefaultAdapter().getAddress();
//	    	macAddr = macAddrp.toLowerCase();
	    	WifiManager wifiMan = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
	    	WifiInfo wifiInf = wifiMan.getConnectionInfo();
	    	macAddr = wifiInf.getMacAddress();
	    	String[] var = macAddr.split(":");
	    	emaillp=var[0]+var[1]+var[2]+var[3]+var[4]+var[5];
	    	emaill=emaillp.toLowerCase();
	    	
	    	db = openOrCreateDatabase("SEND_MAIL", Context.MODE_PRIVATE, null);
			db.execSQL("CREATE TABLE IF NOT EXISTS PendingMail(subject VARCHAR,body VARCHAR);");
			
			
	    	//emaill = "14dda9a8f3e1";
	    	System.out.println("Email address:-pekka-:"+emaill+"@gdv.com.au");
	    	
	    	super.onCreate(savedInstanceState);       
	        super.init(); 
	        super.setIntegerProperty("splashscreen", R.drawable.splashscreen);
	        js=new JSInterface(this,appView);
			appView.addJavascriptInterface(js, "JSI");
			cna = new CustomNativeAccess(this, appView);
	        appView.addJavascriptInterface(cna, "CustomNativeAccess");
	        super.loadUrl("file:///android_asset/www/login.html",4000);
	    }
} 



    
    