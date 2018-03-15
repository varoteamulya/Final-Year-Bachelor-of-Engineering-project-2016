package com.elecfreaks.bleexample;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.opencv.javacv.facerecognition.R;

import com.elecfreaks.ble.BLEDeviceListAdapter;
import com.elecfreaks.ble.BluetoothHandler;
import com.elecfreaks.ble.BluetoothHandler.OnConnectedListener;
import com.elecfreaks.ble.BluetoothHandler.OnScanListener;
import com.elecfreaks.bleexample.Protocol.OnReceivedRightDataListener;
import com.hiwi.Automated;
import com.hiwi.CustomNativeAccess;
import com.hiwi.FallDetectionService;
import com.hiwi.HeartbeatsActivity;
import com.hiwi.ReciveMail;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	public static Context context;
	public static Button scanButton;
	private ListView bleDeviceListView;
	private BLEDeviceListAdapter listViewAdapter;
	
	private BluetoothHandler bluetoothHandler;
	private boolean isConnected = false;
	private Protocol protocol;
	BluetoothAdapter btAdapt = null;
	
	private static final boolean INPUT = false;
	private static final boolean OUTPUT = true;
	private static final boolean LOW = false;
	private static final boolean HIGH = true;
	
	public static boolean startScan = true;
	public static boolean startSPO2Scan = true;
	public static boolean isPaired = true;
	
	public int count = 0;
	public static String SPO2 = "No Device";
	static CustomNativeAccess customNativeAccess;
	
	private boolean digitalVal[];
	private int analogVal[];
	public static Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ble_example);
		context = this.getApplicationContext();
		scanButton = (Button) findViewById(R.id.scanButton);
		bleDeviceListView = (ListView) findViewById(R.id.bleDeviceListView);
		listViewAdapter = new BLEDeviceListAdapter(this);
		digitalVal = new boolean[14];
		analogVal = new int[14];
		
		//Added VU 13-04-2016
		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		
		bluetoothHandler = new BluetoothHandler(this);
		bluetoothHandler.setOnConnectedListener(new OnConnectedListener() {
			
			@Override
			public void onConnected(boolean isConnected) {
				// TODO Auto-generated method stub
				setConnectStatus(isConnected);
			}
		});
		protocol = new Protocol(this, new Transmitter(this, bluetoothHandler));
		protocol.setOnReceivedDataListener(recListener);
		
		startTimer();
		
//		runOnUiThread(new Runnable() {
//   	     @Override
//   	     public void run() {
//   	    	// if(startScan){
//   	    		 scanButton.performClick();
//   	    	// } else {
//   	    		 //System.out.println("Reading");
//   	    	// }
//   	     	}
//		});
		
		
//		Timer t = new Timer();
//		t.schedule(new TimerTask() {
//		    public void run() {
//		    	runOnUiThread(new Runnable() {
//		    	     @Override
//		    	     public void run() {
//		    	    	// if(startScan){
//		    	    	 	 startScan = false;
//		    	    	 	 if(startSPO2Scan) 
//		    	    	 	 {
//		    	    	 		Log.e("Entered Scan", "Scan Flag = "+startSPO2Scan);
//			    	    		 customNativeAccess = new CustomNativeAccess();
//			    	    		 if (CustomNativeAccess.modeNew == 10) scanButton.performClick();
//			    	    		 startScan = true; 
//		    	    	 		Log.e("Exited Scan", "Scan Flag = "+startSPO2Scan);
//		    	    	 	 }
//		    	    	 	
//		    	    	 	 if (startScan){
//			    	    		 startSPO2Scan = false;
//			    	    		 Log.e("Entered Poll", "StartScan = "+startScan);
//			    	    		 customNativeAccess = new CustomNativeAccess();
//			    	    		 if (CustomNativeAccess.modeNew == 10) 
//			    	    		 {
//			    	    			 Automated automated = new Automated();
//			    	    		 }
//			    	    		 startSPO2Scan = true;
//			    	    		 Log.e("Exited Poll", "StartScan = "+startScan);
//			    	    	 }
//
//		    	    }
//		    	});
//		    }
//		    //Automated.finished = false;
//		}, 0, 12000);
		
		/*
		Timer t1 = new Timer();
		t1.schedule(new TimerTask() {
		    public void run() {
		    	runOnUiThread(new Runnable() {
		    	     @Override
		    	     public void run() {
		    	    	 Log.e("Boolean Automated Timer", "StartScan = "+startScan);
		    	    	 
		    	    	 if(startScan){
		    	    		 startSPO2Scan = false;
		    	    		 customNativeAccess = new CustomNativeAccess();
		    	    		 if (CustomNativeAccess.modeNew == 10) 
		    	    		 {
		    	    			 //Automated automated = new Automated();
		    	    		 }
		    	    		 startSPO2Scan = true;
		    	    	 }
		    	    }
		    	});
		    }
		    //Automated.finished = false;
		}, 30000, 30000);
		
		*/
		
		moveTaskToBack(true);
		Intent mServiceIntent = new Intent(this, FallDetectionService.class);
        // Starts the IntentService
		mServiceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startService(mServiceIntent);
        
		//Intent intent = new Intent(this, ReciveMail.class);
		//startActivity(intent);
		//Intent intent = new Intent(this,com.hiwi.MainActivity1.class);
		//startActivity(intent);
		
	}
	
	BroadcastReceiver mybroadcast = new BroadcastReceiver() {    
        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("[BroadcastReceiver]", "MyReceiver");

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.i("[BroadcastReceiver]", "Screen ON");
                startTimer();
            }
            else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i("[BroadcastReceiver]", "Screen OFF");
                timer.cancel();
            }

        }
    };
	
	public  void startTimer(){
	    timer = new Timer();
		timer.schedule(new TimerTask() {
		    public void run() {
		    	runOnUiThread(new Runnable() {
		    	     @Override
		    	     public void run() {
		    	    	// if(startScan){
		    	    	 	 startScan = false;
		    	    	 	 if(startSPO2Scan) 
		    	    	 	 {
		    	    	 		Log.e("Entered Scan", "Scan Flag = "+startSPO2Scan);
			    	    		 customNativeAccess = new CustomNativeAccess();
			    	    		 if (CustomNativeAccess.modeNew == 10) scanButton.performClick();
			    	    		 startScan = true; 
		    	    	 		Log.e("Exited Scan", "Scan Flag = "+startSPO2Scan);
		    	    	 	 }
		    	    	 	
		    	    	 	 if (startScan){
			    	    		 startSPO2Scan = false;
			    	    		 Log.e("Entered Poll", "StartScan = "+startScan);
			    	    		 customNativeAccess = new CustomNativeAccess();
			    	    		 if (CustomNativeAccess.modeNew == 10) 
			    	    		 {
			    	    			 Automated automated = new Automated();
			    	    		 }
			    	    		 startSPO2Scan = true;
			    	    		 Log.e("Exited Poll", "StartScan = "+startScan);
			    	    	 }

		    	    }
		    	});
		    }
		    //Automated.finished = false;
		}, 0, 12000);
	}
	
	public void onBackPressed(){
		moveTaskToBack(true);
		//anew Finalizer().killApp(false);
		super.onBackPressed();
		Intent intent = new Intent(this,com.hiwi.MainActivity1.class);
		startActivity(intent);
    }
	
	private OnReceivedRightDataListener recListener = new OnReceivedRightDataListener() {
		
		@Override
		public int onReceivedData(String str) {
			// TODO Auto-generated method stub	    	
			try {
				JSONObject readJSONObject = new JSONObject(str);
				int type = readJSONObject.getInt("T");
				int value = readJSONObject.getInt("V");
				
				switch(type){
					case Protocol.ANALOG:{
						int pin = readJSONObject.getInt("P");
						analogVal[pin] = value;
					}break;
					case Protocol.DIGITAL:{
						int pin = readJSONObject.getInt("P");
						digitalVal[pin] = (value>0)?HIGH:LOW;
					}break;
					case Protocol.TEMPERATURE:{
						float temperature = ((float)value)/100;
					}break;
					case Protocol.HUMIDITY:{
						float humidity = ((float)value)/100;
					}break;
					default:break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
			return 0;
		}
	};

	public void scanOnClick(final View v){
		if(!isConnected){
			bleDeviceListView.setAdapter(bluetoothHandler.getDeviceListAdapter());
			btAdapt = BluetoothAdapter.getDefaultAdapter();						
			//Set<BluetoothDevice> pairedDevices = btAdapt.getBondedDevices();
			int val = 0;
			val = bleDeviceListView.getAdapter().getCount();
			Log.e("Number of ", "Paired Devices = "+val);
			for(int i = 0; i < val; i++){
				BluetoothDevice device = bluetoothHandler.getDeviceListAdapter().getItem(i).device;
				 String val1=(String)(bleDeviceListView.getItemAtPosition(i).toString());
				 Log.e("Value of ","Val1 : " + val1);
				 SPO2 = device.getName();
				if (!(SPO2 == null))
				{
					 startScan = false;
					 Log.e("MAC", " "+device.getAddress());
					 if(device.getName().toLowerCase().contains("berry")) bluetoothHandler.connect(device.getAddress());

				 else{
					 startScan = false;
					 try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			    } //else return;
			} 
		//	else {
				
		//		startScan = true;
				
		//	}
//				 try {
//						Thread.sleep(1000);
//				} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//				}
//				Automated automated = new Automated();
//		    	while(Automated.finished);
//			}
//		    	try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		    	runOnUiThread(new Runnable() {
//		    	     @Override
//		    	     public void run() {
//		    	    	// if(startScan){
//		    	    		 scanButton.performClick();
//		    	    	// } else {
//		    	    	//	 System.out.println("Reading");
//		    	    	// }
//		    	    }
//		    	});
			Log.e("ads","asda" + val);
			Log.e("Boolean", "StartScan = "+startScan);
			bleDeviceListView.setOnItemClickListener(new OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						String buttonText = (String) ((Button)v).getText();
						if(buttonText.equals("scanning")){
							showMessage("scanning...");
							startScan = true;
							return ;
						}						
						//BluetoothDevice device = bluetoothHandler.getDeviceListAdapter().getItem(position).device;
						// connect
						//Log.e("MAC", " "+device.getAddress());
						//if(device.getName().toLowerCase().contains("berry")) bluetoothHandler.connect(device.getAddress());
					}
			});
			//bleDeviceListView.performItemClick(bleDeviceListView.getChildAt(1), 1, bleDeviceListView.getItemIdAtPosition(1));
			bluetoothHandler.setOnScanListener(new OnScanListener() {
				@Override
				public void onScanFinished() {
					// TODO Auto-generated method stub
					((Button)v).setText("scan");
					((Button)v).setEnabled(true);
				}
				@Override
				public void onScan(BluetoothDevice device, int rssi, byte[] scanRecord) {}
			});
			((Button)v).setText("scanning");
			((Button)v).setEnabled(false);
			bluetoothHandler.scanLeDevice(true);
			
			
			//scanButton.performClick();
		}else{
			setConnectStatus(false);
			//startScan = true;
			//return;
		}
	}
	
	private void pairDevice(BluetoothDevice device) {
       try {
           Method method = device.getClass().getMethod("createBond", (Class[]) null);
           method.invoke(device, (Object[]) null);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
	
	public void setConnectStatus(boolean isConnected){
		this.isConnected = isConnected;
		if(isConnected){
			showMessage("Connection successful");
			scanButton.setText("break");
		}else{
			bluetoothHandler.onPause();
    		bluetoothHandler.onDestroy();
    		scanButton.setText("scan");
		}
	}
	
	private void showMessage(String str){
		Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
	}
}
