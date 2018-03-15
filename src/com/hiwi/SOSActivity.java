package com.hiwi;


import java.security.PublicKey;
import java.sql.Timestamp;

import org.opencv.javacv.facerecognition.R;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.view.View.OnTouchListener;

public class SOSActivity extends Activity {

	final Context context = this;
	private Button button;
	long lastDown = 0;
	long timeMillis = 0;
	long timeMillis1 = 0;
	long lastDuration = 0;
	long timeSeconds = 0;
	long timeSeconds1 = 0;
	public String timew;
	public String val2;
	String mcc, mnc;
	int cid, lac;
	private String val = null;
	private boolean sosButtonClicked = false;
	
	GetGPS gps;
	
	Date date;
	long time;
	Timestamp ts;
	String[] varw;
	String[] da;
	String[] ti;
	String[] ti2;
	
	String mac;
	String type; 
	Timer timer;
	TimerTask timerTask;
	TelephonyManager telephonyManager;
	GsmCellLocation cellLocation;
	String networkOperator;
	
	
	private static final String tag = "Main";
	//private CountDownTimerClass countDownTimer;
	private long timeElapsed;
	private boolean timerHasStarted = false;
	private Button startB;
//	private TextView text;
//	private TextView timeElapsedView;

	private final long startTime = 5000;
	private final long interval = 1;
	
	
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sos);

		button = (Button) findViewById(R.id.buttonCall);
		//countDownTimer = new CountDownTimerClass(startTime, interval);
		
		// add PhoneStateListener
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);

		
		button.setOnLongClickListener(new View.OnLongClickListener() {

	        public boolean onLongClick(View v) {
	        	sosButtonClicked = true;
	        	Intent callIntent = new Intent(Intent.ACTION_CALL);
	        	callIntent.setData(Uri.parse("tel:" +ReadMailSample.sos1));
	        	startActivity(callIntent);
	        	return false;
	        }
	    });
		
//		moveTaskToBack(true);
//		Intent intent = new Intent(this, com.hiwi.FallDetectionActivity.class);
//		startActivity(intent);
	}
	private class PhoneCallListener extends PhoneStateListener {

		private boolean isPhoneCalling = false;

		String LOG_TAG = "LOGGING 123";

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// phone ringing
				Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
			}

			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active
				Log.i(LOG_TAG, "OFFHOOK");

				isPhoneCalling = true;
			}

			if (TelephonyManager.CALL_STATE_IDLE == state) {
				// run when class initial and phone call ended, need detect flag
				// from CALL_STATE_OFFHOOK
				Log.i(LOG_TAG, "IDLE");
				if(sosButtonClicked) {

					if (isPhoneCalling) {
	
						date= new Date();
				  		time = date.getTime();
				  		ts = new Timestamp(time);
				  		varw=ts.toString().split(" ");
				  		da=varw[0].split("-");
				  		ti=varw[1].split(":");
				  		ti2=ti[2].split("\\.");
				  		timew=da[0]+da[1]+da[2]+ti[0]+ti[1]+ti2[0];
						telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
						cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
						networkOperator = telephonyManager.getNetworkOperator();
						mcc = networkOperator.substring(0, 3);
						mnc = networkOperator.substring(3);
						cid = cellLocation.getCid();
						lac = cellLocation.getLac();
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								gps = new GetGPS(SOSActivity.this);
								if(gps.canGetLocation()){
									double latitude = gps.getLatitude();
									double longitude = gps.getLongitude();
									
									if(latitude != 0.0 && longitude != 0.0){
									
										System.out.println("Lat:" +latitude + "\nLong:" + longitude);
										Log.e("GPS", "Lat:" +latitude + "\nLong:" + longitude);
										val2 = "GPS:" + latitude +"/" + longitude;
										mac = HeartbeatsActivity.macAddr;
										val = timew +"," + mac +"," + "high" + "," + "LBS:" + mcc + mnc + "," + lac + "," + cid + "," + val2 + ",Future Use" +",Future Use" ;
										System.out.println(val);
										Log.e("asdas", val);
										// TODO Auto-generated method stub
										String Name="Pramod 7 S8 BT";
								    	String Phone="+6112345678912";
								    	String Subject="SOS,"+Phone+","+Name;
										System.out.println("In Timer");
										SendMail mail = new SendMail(Subject, val, 0);
					
										isPhoneCalling = false;
										sosButtonClicked = false;
									}
								}
							}
						});
						
					}
				}

			}
		}
	}

}