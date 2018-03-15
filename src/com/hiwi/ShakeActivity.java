package com.hiwi;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import org.opencv.javacv.facerecognition.R;

public class ShakeActivity extends Activity implements AccelerometerListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accelerometer_example_main);
		
		moveTaskToBack(true);
		Intent intent = new Intent(HeartbeatsActivity.context, com.hiwi.PedometerActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		
		// Check onResume Method to start accelerometer listener
	}
   
	public void onAccelerationChanged(float x, float y, float z) {
		// TODO Auto-generated method stub
		
	}

	public void onShake(float force) {
		
//		 Called when Motion Detected
//		Toast.makeText(getBaseContext(), "Motion detected", Toast.LENGTH_SHORT).show();
		
		//PowerManager pm = null;
		//PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
		//wl.acquire();
		turnOnScreen();
		
	}
	
	 private PowerManager mPowerManager;
	 private PowerManager.WakeLock mWakeLock;

	 @SuppressWarnings("deprecation")
	public void turnOnScreen(){
	     // turn on screen
//	     Log.v("ProximityActivity", "ON!");
//	     mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
//	     mWakeLock.acquire();
		 WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
			     PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
			screenLock.acquire();
			//Log.d("hi","1");
			screenLock.release();
	}
	
	@Override
    public void onResume() {
            super.onResume();
//            Toast.makeText(getBaseContext(), "onResume Accelerometer Started", 
//            		Toast.LENGTH_LONG).show();
            
            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isSupported(this)) {
            	
            	//Start Accelerometer Listening
    			AccelerometerManager.startListening(this);
            }
    }
	
//	@Override
//    public void onStop() {
//            super.onStop();
//            
//            //Check device supported Accelerometer senssor or not
//            if (AccelerometerManager.isListening()) {
//            	
//            	//Start Accelerometer Listening
//    			AccelerometerManager.stopListening();
//    			
//    			Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped", 
//    					Toast.LENGTH_LONG).show();
//            }
//           
//    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Sensor", "Service  distroy");
		
		//Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isListening()) {
			
			//Start Accelerometer Listening
			AccelerometerManager.stopListening();
        }
			
	}

}
