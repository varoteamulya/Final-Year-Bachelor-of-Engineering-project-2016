package com.hiwi;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.javacv.facerecognition.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class PedometerActivity extends Activity implements SensorEventListener {
	
	private SensorManager sensorManager;
	private Sensor accelerometer;
	public static Context context;
	
	 Toast toast = null;

	private float deltaX = 0;
	private float deltaY = 0;
	private float deltaZ = 0;
	public static int count = 0;
	private static float current_square_value = 0.0f;
	
	private boolean flag = true;
	private TextView stepsCountTextView;
	
	private String number;
	private Vibrator v;


	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pedometer);
		v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		new Alarm();
		
		context = this.getApplicationContext();
		stepsCountTextView = (TextView) findViewById(R.id.steps);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			// success! we have an accelerometer

			accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			// fai! we dont have an accelerometer!
		}
		
		moveTaskToBack(true);
		Intent intent = new Intent(HeartbeatsActivity.context, com.hiwi.SOSActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	 @Override
	    protected void onDestroy() 
	    {
	        if(toast!=null)
	        	toast.cancel();
	        super.onDestroy();
	    }

	//onResume() register the accelerometer for listening the events
	protected void onResume() {
		super.onResume();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	//onPause() unregister the accelerometer for stop listening the events
	protected void onPause() {
		super.onPause();
//		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//		sensorManager.unregisterListener(this);
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
		
		current_square_value =(float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));
		if((current_square_value > 11) && flag ){
			flag = false;
			MainActivity1.numSteps++;
			stepsCountTextView.setText(""+MainActivity1.numSteps);
			//v.vibrate(50);
		} 
		if((current_square_value < 9) && !flag ){
			flag = true;
			MainActivity1.numSteps++;
			stepsCountTextView.setText(""+MainActivity1.numSteps);
			//v.vibrate(50);
		}
	}
}

class Alarm {
    Timer _timer;

    public Alarm() {

        // Create a Date corresponding to 12:00:00 AM today.
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);

        Date alarmTime = calendar.getTime();
        
        _timer = new Timer();
	    _timer.schedule(new AlarmTask(), alarmTime);
    }

    class AlarmTask extends TimerTask {
    	Vibrator vibrator;
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
    	TimerTask timerTask;
    	TelephonyManager telephonyManager;
    	GsmCellLocation cellLocation;
    	String networkOperator;
    	String macAddr;


		@Override
		public void run() {
			Looper.myLooper().prepare();
//	        
//	        if(hour == 0 && min == 0) {
			
				macAddr = HeartbeatsActivity.macAddr;
			    date = new Date();
		  		time = date.getTime();
		  		ts = new Timestamp(time);
		  		varw = ts.toString().split(" ");
		  		da = varw[0].split("-");
		  		ti = varw[1].split(":");
		  		ti2 = ti[2].split("\\.");
		  		timew = da[0]+da[1]+da[2]+ti[0]+ti[1]+ti2[0];
				telephonyManager = (TelephonyManager)PedometerActivity.context.getSystemService(Context.TELEPHONY_SERVICE);
				cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
				networkOperator = telephonyManager.getNetworkOperator();
				mcc = networkOperator.substring(0, 3);
				mnc = networkOperator.substring(3);
				cid = cellLocation.getCid();
				lac = cellLocation.getLac();
				GetGPS gps = new GetGPS(PedometerActivity.context);
				gps = new GetGPS(MainActivity1.context);
				if(gps.canGetLocation()){
					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();
					if(latitude != 0.0 && longitude != 0.0){
						System.out.println("Lat:" +latitude + "\nLong:" + longitude);
						Log.e("GPS", "Lat:" +latitude + "\nLong:" + longitude);
						val2 = "GPS:" + latitude +"/" + longitude;
						getBatteryPercentage();
					}
				} 
	       // }
			
		}
		
		private void getBatteryPercentage() {
			BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
		         public void onReceive(Context context, Intent intent) {
		             context.unregisterReceiver(this);
		             int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		             int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		             int level = -1;
		             if (currentLevel >= 0 && scale > 0) {
		                 level = (currentLevel * 100) / scale;
		             }
		             String val = timew +"," + macAddr +"," + "high" + "," + "LBS:" + mcc + mnc + "," + lac + "," + cid + "," + val2 + ",Battery Percentage is " +level +","+"Steps is "+MainActivity1.numSteps ;
		             System.out.println(val);
		             Log.e("asdas", val);
		             // TODO Auto-generated method stub
		             String Name="Pramod 7 S8 BT";
		             String Phone="+6112345678912";
		             String Subject="LbsTest,"+Phone+","+Name;
		             System.out.println("In Timer");
		             new SendMail(Subject, val, 0);
		             MainActivity1.numSteps = 0;
		         }
		     }; 
		     IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		     PedometerActivity.context.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
		  }
    }
}

