package com.hiwi;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Service;

import org.opencv.javacv.facerecognition.R;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class FallDetectionService extends Service implements SensorEventListener {
	
	private float lastX, lastY, lastZ;

	private SensorManager sensorManager;
	private Sensor accelerometer;

	private float deltaX = 0;
	private float deltaY = 0;
	private float deltaZ = 0;
	
	private boolean flag = false;
	private Stopwatch timer = new Stopwatch();

	private float vibrateThreshold = 0;

	private TextView currentX, currentY, currentZ;

	public Vibrator v;
	private String Subject, body;

	private Date date;

	private long time;

	private Timestamp ts;

	private String[] varw;

	private String[] da;

	private String[] ti;

	private String[] ti2;

	private String timew ;
	public String val2;
	String mcc, mnc;
	int cid, lac;
	private String val = null;
	
	GetGPS gps;
	
	
	String mac;
	String type; 
	//Timer timer;
	TimerTask timerTask;
	TelephonyManager telephonyManager;
	GsmCellLocation cellLocation;
	String networkOperator;

	private String number;

	private Handler handler;

	@Override
	public void onCreate() {
		super.onCreate();
		//setContentView(R.layout.activity_fall_detection);
		//initializeViews();
		 handler = new Handler();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			// success! we have an accelerometer

			accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			vibrateThreshold = accelerometer.getMaximumRange() / 2;
		} else {
			// fai! we dont have an accelerometer!
		}		
		//initialize vibration
		v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		
		//moveTaskToBack(true);
		Intent intent = new Intent(HeartbeatsActivity.context, com.hiwi.ShakeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		
	}
	
	@Override
    public IBinder onBind(Intent intent) {
        return null;
    }

	

	//onResume() register the accelerometer for listening the events
	protected void onResume() {
		//super.onResume();
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	//onPause() unregister the accelerometer for stop listening the events
	protected void onPause() {
		//super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// get the change of the x,y,z values of the accelerometer
		deltaX = event.values[0];
		deltaY = event.values[1];
		deltaZ = event.values[2];
		//Toast.makeText(getApplicationContext(), "Running: " + deltaX + deltaY + deltaZ, Toast.LENGTH_SHORT).show();

		// if the change is below 2, it is just plain noise
		if (deltaX < 2)
			deltaX = 0;
		if (deltaY < 2)
			deltaY = 0;
		if (deltaZ < 2)
			deltaZ = 0;
		
		//vibrate();
		startOfFallDetection(event);
		if(flag == true){
			confirmFallDetection(event);
		}
	}
	
	private void startOfFallDetection(SensorEvent event){
		// TODO Auto-generated method stub
		float checkX, checkY, checkZ;
		checkX = event.values[0];
		checkY = event.values[1];
		checkZ = event.values[2];
		
		if((checkX) < 2 && (checkY) < 2 && (checkZ) < 2 && (checkY) > -1){
			//System.out.println("In ");
			//confirmFallDetection(event);
			//Toast.makeText(getApplicationContext(), "Fall is detected", Toast.LENGTH_SHORT).show();
			timer.start();
			flag = true;
		}
	}

	private void confirmFallDetection(SensorEvent event) {
		// TODO Auto-generated method stub
		float checkX, checkY, checkZ;
		//Thread.currentThread();
		//Thread.sleep(1000);
		checkX = event.values[0];
		checkY = event.values[1];
		checkZ = event.values[2];
		//Toast.makeText(getApplicationContext(), checkX + "/" + checkY + "/" + checkZ, Toast.LENGTH_SHORT).show();
		float optionX = 0, optionY = 0, optionZ = 0;
		String setSensibility = "normal";
		switch(setSensibility){
			case "low_sensibility" : optionX = 18;
									 optionY = 18;
									 optionZ = 18;
									 break;
			case "high_sensibility": optionX = 9;
			 						 optionY = 9;
			 						 optionZ = 9;
									 break;
			case "normal" :			 optionX = 13;
									 optionY = 13;
									 optionZ = 13;
									 break;
		}
		if((checkX) > optionX || (checkY) > optionY || (checkZ) > optionZ){
			timer.stop();
			long fallDuration = timer.getElapsedTimeMili();
			Toast.makeText(getApplicationContext(), "Fall is detected: " + fallDuration, Toast.LENGTH_SHORT).show();
			vibrate();
			flag = false;
//			date= new Date();
//	  		time = date.getTime();
//	  		ts = new Timestamp(time);
//	  		varw=ts.toString().split(" ");
//	  		da=varw[0].split("-");
//	  		ti=varw[1].split(":");
//	  		ti2=ti[2].split("\\.");
//	  		timew = da[0]+da[1]+da[2]+ti[0]+ti[1]+ti2[0];
//	  		number = "+6112345678912";
//			Subject = "Fall Detected,"+number+",Pramod 7 S8 BT";
//			body = ""+timew+","+HeartbeatsActivity.macAddr+",Are you alright?,Future Use,Future Use,Future Use";
//			SendMail mail = new SendMail(Subject, body, 0);
			
			
			
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

					gps = new GetGPS(FallDetectionService.this);
					if(gps.canGetLocation()){
						double latitude = gps.getLatitude();
						double longitude = gps.getLongitude();
						
						// \n is for new line
						//Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
						System.out.println("Lat:" +latitude + "\nLong:" + longitude);
						Log.e("GPS", "Lat:" +latitude + "\nLong:" + longitude);
						val2 = "GPS:" + latitude +"/" + longitude;
					}
				
			
			mac = HeartbeatsActivity.macAddr;
			val = timew +"," + mac +"," + "high" + "," + "LBS:" + mcc + mnc + "," + lac + "," + cid + "," + val2 + ",Future Use" +",Future Use" ;
			System.out.println(val);
			Log.e("asdas", val);
			// TODO Auto-generated method stub
			String Name="Pramod 7 S8 BT";
	    	String Phone="+6112345678912";
	    	String Subject="Fall Detected,"+Phone+","+Name;
			System.out.println("In Timer");
			
			Intent intent = new Intent(HeartbeatsActivity.context,AndroidVideoCapture.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			
			SendMail mail = new SendMail(Subject, val, 2);
			
			
			
			
			
			
			
			
			
			
		}
	}



	// if the change in the accelerometer value is big enough, then vibrate!
	// our threshold is MaxValue/2
	public void vibrate() {
		v.vibrate(50);
	}
}
