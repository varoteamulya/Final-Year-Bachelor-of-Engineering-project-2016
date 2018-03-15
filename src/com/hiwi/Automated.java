package com.hiwi;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.MSG.Common;
import com.elecfreaks.bleexample.MainActivity;

import android.R.integer;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


public class Automated {
	
	BluetoothAdapter bluetoothAdapter;
	
	//bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	CustomNativeAccess customNativeAccess;
	MainActivity mainActivity;
	String con31 = null;
	String con1 = null;
	int connecttimes = 1;
	private int count = 0;
	public static boolean finished = true;
	String bpNew1 = "Not Connected";
	
	public Automated(){
		customNativeAccess = new CustomNativeAccess();
		//mainActivity = new MainActivity();
		
		poll();
	}

	private void poll() {
		 
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		CheckBlueToothState();
		
		Log.e("DEVICE		","polling Active");
		
		HeartbeatsActivity.context.registerReceiver(ActionFoundReceiver, 
        		new IntentFilter(BluetoothDevice.ACTION_FOUND));
		
		bluetoothAdapter.startDiscovery();
		
		//con31 = customNativeAccess.connectpolling();
		
		
		
	}
	
	private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(BluetoothDevice.ACTION_FOUND.equals(action)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            customNativeAccess.strName = device.getName();
	            customNativeAccess.strAddress = device.getAddress();
	            //Log.e("DEVICE","" + device.getName());
	            
	            if (device.getName().toLowerCase().contains("etb")){
	            	//customNativeAccess = new CustomNativeAccess();
	            	if (CustomNativeAccess.modeNew == 10) bpNew1 = customNativeAccess.Connecttodevice();
	            	if (bpNew1 == "Device Connected") {
	            	MainActivity.startSPO2Scan = false;
	            	Timer t = new Timer();
	    			t.schedule(new TimerTask() {
	    			    public void run() {
	    			    	count = count + 1;
	    			    	try {
	    						con1 = customNativeAccess.connectFC();
	    					} catch (InterruptedException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	    					
	    					if(con1=="connected1") {
	    						customNativeAccess.readBloodSugar();
	    						cancel();
	    						ReciveMail.speakOut1();
	    						customNativeAccess.BLdisconnect();
	    						MainActivity.startSPO2Scan = true;
	    						count = 0;
	    						//connecttimes = 1;
	    						finished = false;
	    					}
	    					if(count == 5){
	    						cancel();
	    						customNativeAccess.BLdisconnect();
	    						MainActivity.startSPO2Scan = true;
	    						count = 0;
	    						//connecttimes = 1;
	    						finished = false;
	    					}
	    					//count++;
	    			    }
	    			    
	    			},0,3000);
	            	} else {
	            		
	            		//bluetoothAdapter.cancelDiscovery();
	            		//customNativeAccess.BLdisconnect();
	            		//count = 0;
	            		//connecttimes = 1;
						//finished = false;
	            	}

	            } else if (device.getName().toLowerCase().contains("noberry")){
	            	//customNativeAccess = new CustomNativeAccess();
	            	if (connecttimes == 1) bpNew1 = customNativeAccess.Connecttodevice();
	            	if (bpNew1 == "Device Connected") {
	            		//connecttimes = 2;
	            	Timer t = new Timer();
	    			t.schedule(new TimerTask() {
	    			    public void run() {
	    			    	count = count + 1;
	    			    	try {
	    						con1 = customNativeAccess.connectFCnewOxy();
	    					} catch (InterruptedException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	    					
	    					if(con1=="connectedOxy") {
	    						customNativeAccess.readBloodPressurenewOxy();
	    						cancel();
	    						customNativeAccess.BLdisconnect();
	    						count = 0;
	    						//connecttimes = 1;
	    						finished = false;
	    					}
	    					if(count == 5){
	    						cancel();
	    						customNativeAccess.BLdisconnect();
	    						count = 0;
	    						//connecttimes = 1;
	    						finished = false;
	    					}
	    					//count++;
	    			    }
	    			    
	    			},0,1500);
	            	} else {
	            		//count = 0;
	            		//bluetoothAdapter.cancelDiscovery();
	            		//customNativeAccess.BLdisconnect();
	            		//connecttimes = 1;
						//finished = false;
	            	}

	            }
	            
	            	else if (device.getName().toLowerCase().contains("scale")) {
	            	
	            	//Log.e("DEVICE","    " + device.getName());
	            	
	            	if ((CustomNativeAccess.modeNew == 10) && (!(CustomNativeAccess.LastDevice == 12))) bpNew1 = customNativeAccess.Connecttodevice();

	            	//if (CustomNativeAccess.modeNew == 10) bpNew1 = customNativeAccess.Connecttodevice();
	            	if (bpNew1 == "Device Connected") {
	            	MainActivity.startSPO2Scan = false;
	            	Timer t = new Timer();
	    			t.schedule(new TimerTask() {
	    			    public void run() {
	    			    	count = count + 1;
	    			    	try {
	    						con1 = customNativeAccess.connectFCnewScale();
	    					} catch (InterruptedException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	    					
	    					if(con1=="connectedWT") {
	    						customNativeAccess.readBloodPressurenewScale();
	    						cancel();
	    						ReciveMail.speakOut4();
	    						customNativeAccess.BLdisconnect();
	    						MainActivity.startSPO2Scan = true;
	    						count = 0;
	    						//connecttimes = 1;
	    						finished = false;
	    					}
	    					if(count == 3){
	    						cancel();
	    						customNativeAccess.BLdisconnect();
	    						MainActivity.startSPO2Scan = true;
	    						count = 0;
	    						//connecttimes = 1;
	    						finished = false;
	    					}
	    					//count++;
	    			    }
	    			    
	    			},0,1500);
	            	} else {
	            		
	            		
	            		//bluetoothAdapter.cancelDiscovery();
	            		//customNativeAccess.BLdisconnect();
	            		//count = 0;
	            		//connecttimes = 1;
						//finished = false;
	            	}
	            	
	            } else if (device.getName().toLowerCase().contains("bp")) {
	            	
	            	//Log.e("DEVICE","    " + device.getName());
	            	//customNativeAccess = new CustomNativeAccess();
	            	//if ((CustomNativeAccess.modeNew == 10) && (!(CustomNativeAccess.LastDevice == 13))) bpNew1 = customNativeAccess.Connecttodevice();
	            	if (CustomNativeAccess.modeNew == 10)  bpNew1 = customNativeAccess.Connecttodevice();
	            	//else customNativeAccess.BLdisconnect();
	            	if (bpNew1 == "Device Connected") {
	            	
	            	MainActivity.startSPO2Scan = false;
	            	Timer t = new Timer();
	    			t.schedule(new TimerTask() {
	    			    public void run() {
	    			    	count = count + 1;
	    			    	try {
	    						con1 = customNativeAccess.connectTempnew();

	    					} catch (InterruptedException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    					}
	    					
	    					if(con1=="connected2new") {
	    						
	    						customNativeAccess.readTemp();
	    						ReciveMail.speakOut1();
	    				          try{
	    				              Thread.sleep(100);
	    				            }catch(Exception E){}
	    						cancel();
	    						MainActivity.startSPO2Scan = true;
	    						customNativeAccess.BLdisconnect();
	    						count = 0;
	    						//connecttimes = 1;
	    						finished = false;
	    					}
	    					if(count == 25){
	    						MainActivity.startSPO2Scan = true;
	    						cancel();
	    						customNativeAccess.BLdisconnect();
	    						count = 0;
	    						//connecttimes = 1;
	    						finished = false;
	    					}
	    					//count++;
	    			    }
	    			    
	    			}, 0, 600);
	            	} else {
	            		
	            		//bluetoothAdapter.cancelDiscovery();
	            		//customNativeAccess.BLdisconnect();
	            		//count = 0;
	            		//connecttimes = 1;
						//finished = false;
	            	} 
	            	
	            } 
	            
	            //btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	           // btArrayAdapter.notifyDataSetChanged();
	        }
		}
	};
	
	private void CheckBlueToothState(){
    	if (bluetoothAdapter == null){
        	//stateBluetooth.setText("Bluetooth NOT support");
        }else{
        	if (bluetoothAdapter.isEnabled()){
        		if(bluetoothAdapter.isDiscovering()){
        			//stateBluetooth.setText("Bluetooth is currently in device discovery process.");
        		}else{
        			//stateBluetooth.setText("Bluetooth is Enabled.");
        			//btnScanDevice.setEnabled(true);
        		}
        	}else{
        		//stateBluetooth.setText("Bluetooth is NOT Enabled!");
        		//Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        	    //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        	}
        }
    }
}
