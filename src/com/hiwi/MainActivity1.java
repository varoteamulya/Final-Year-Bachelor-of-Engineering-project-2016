package com.hiwi;

//import android.support.v7.app.ActionBarActivity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lifesense.ble.DeviceConnectState;
import com.lifesense.ble.LsBleManager;
import com.lifesense.ble.ReceiveDataCallback;
import com.lifesense.ble.bean.BloodPressureData;
import com.lifesense.ble.bean.HeightData;
import com.lifesense.ble.bean.LsDeviceInfo;
import com.lifesense.ble.bean.PedometerData;
import com.lifesense.ble.bean.SexType;
import com.lifesense.ble.bean.SleepInfo_A4;
import com.lifesense.ble.bean.UnitType;
import com.lifesense.ble.bean.VibrationVoice;
import com.lifesense.ble.bean.WeightData_A2;
import com.lifesense.ble.bean.WeightData_A3;
import com.lifesense.ble.bean.WeightUserInfo;

import org.opencv.javacv.facerecognition.R;

@SuppressLint("NewApi")
public class MainActivity1 extends Activity implements
LoaderManager.LoaderCallbacks<Cursor> {
	
	String mcc, mnc;
	int cid, lac;
	private String val = null;
	final Handler handler = new Handler();
	Timer timer;
	TimerTask timerTask;
	TelephonyManager telephonyManager;
	GsmCellLocation cellLocation;
	String networkOperator;

	private ListView listview;
	public static List<DeviceInfoWithDate> myDeviceList;
	private LsBleManager mLsBleManager;

	private LsDeviceAdapter lsDeviceAdapter;
	protected LsDeviceInfo mDevice;
	public static boolean flag = false;
	public static String macAddr, macAddrp;
	public String timew;
	public String val2;
	public static Context context;
	public static GetGPS gps;
	
	Date date;
	long time;
	Timestamp ts;
	String[] varw;
	String[] da;
	String[] ti;
	String[] ti2;
	
	String mac;
	String type; 
	public static int numSteps = 0;
	public static SQLiteDatabase db;
	
	
	public static final int MAIN_SEND_DATA_WEIGHT_A2=5000;
	public static final int MAIN_SEND_DATA_WEIGHT_A3=5001;
	public static final int MAIN_SEND_DATA_BLOOD_PRESSURE=5002;
	public static final int MAIN_SEND_DATA_PEDOMETER=5003;
	public static final int MAIN_SEND_DATA_PEDOMETER_A4=5004;
	public static final int MAIN_SEND_DATA_HEIGHT=5005;
	public static final int MAIN_SEND_DATA_SLEEP_A4=5006;
	
	public static Map<String, String> dateMap;
	public static List<BloodPressureData> bpDataList;
	public static List<WeightData_A3> wDataList_A3;
	public static Map<String,List<PedometerData>> pDataListMap;
	public static List<HeightData> hDataList;
	public static List<WeightData_A2> wDataList_A2;
	public static Map<String,List<SleepInfo_A4>> sDataList_A4Map;
	public static Map<String,Integer> deviceUserNumberMap;
	
	private ReceiveDataCallback reDataCallback=new ReceiveDataCallback() 
	{
		//		Connection status change
		@Override
		public void onConnectStateChange(DeviceConnectState connectState)
		{
			if(connectState==DeviceConnectState.CONNECTED_SUCCESS)
			{
				Toast.makeText(MainActivity1.this, "Connected Successfully...", Toast.LENGTH_LONG).show();
				//super.loadUrl("file:///android_asset/www/PatientProfileView.html",4000);
			}
			if(connectState==DeviceConnectState.CONNECTED_FAILED)
			{
				Toast.makeText(MainActivity1.this, "Disconnect...", Toast.LENGTH_LONG).show();
			}
		};
		

		@Override
		public void onReceiveBloodPressureData(BloodPressureData bpData)
		{
			bpDataList.add(bpData);
			
			Toast.makeText(MainActivity1.this,""+bpData.getSystolic() +" "+bpData.getDiastolic()+" "+bpData.getPulseRate(), Toast.LENGTH_LONG).show();
			
			String Name="Pramod 7 S8 BT";
	    	String Phone="+6112345678912";
	    	//String[] var=bpNew.split("\\|");
	    	//timestamp
	    	date= new Date();
	  		time = date.getTime();
	  		ts = new Timestamp(time);
	  		varw=ts.toString().split(" ");
	  		da=varw[0].split("-");
	  		ti=varw[1].split(":");
	  		ti2=ti[2].split("\\.");
	  		timew=da[0]+da[1]+da[2]+ti[0]+ti[1]+ti2[0];
	    	//timestamp
	    	
	    	mac=HeartbeatsActivity.macAddr;
	    	type="Medium";
	    	int sys=(int) bpData.getSystolic();
	    	int dias=(int) bpData.getDiastolic();
	    	int pulse=(int) bpData.getPulseRate();
	    	String bodyy=timew+","+mac+","+type+","+"Joe Black BP:"+sys+","+dias+","+pulse+" OK,Future use,Future use,Future use";
	    	String Subject="BP Mode1,"+Phone+","+Name;
	    	ReciveMail.speakOut3();
	    	SendMail sendma=new SendMail(Subject,bodyy,0);
			
			//updateMeasureRecord(bpData.getBroadcastId(),bpDataList.size());
			if(ShowMeasureDataActivity.isCurrentPage){

				if(ShowMeasureDataActivity.dataReceiveHandler!=null){

					Log.e("weight A2 data receive from device", "sending message to measurement data page");
					Message msg=ShowMeasureDataActivity.dataReceiveHandler.obtainMessage();
					msg.what=MAIN_SEND_DATA_BLOOD_PRESSURE;
					msg.obj=bpData.getBroadcastId();
					msg.setTarget(ShowMeasureDataActivity.dataReceiveHandler);
					msg.sendToTarget();
					
					
				}

			}
		}	

		@Override
		public void onReceiveUserInfo(WeightUserInfo proUserInfo)
		{
			if(proUserInfo!=null)
			{
				System.out.println("ProductUserNumber:"+proUserInfo.getProductUserNumber());
				System.out.println("Sex:"+proUserInfo.getSex());
				System.out.println("Age:"+proUserInfo.getAge());
				System.out.println("Height:"+proUserInfo.getHeight());
				System.out.println("AthleteLevel:"+proUserInfo.getAthleteActivityLevel());
				System.out.println("GoalWeight:"+proUserInfo.getGoalWeight());
				System.out.println("Waistline:"+proUserInfo.getWaistline());
				System.out.println("Unit:"+proUserInfo.getUnit());
			}

		}			
		
		
	};
	private ContentResolver contentResolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main1);	
		
		context = this.getApplicationContext();
		
		//Added VU 13-04-2016
		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		
		deviceUserNumberMap=new HashMap<String, Integer>();
		bpDataList=new ArrayList<BloodPressureData>();
		wDataList_A3=new ArrayList<WeightData_A3>();
		wDataList_A2=new ArrayList<WeightData_A2>();
		pDataListMap=new HashMap<String, List<PedometerData>>();
		hDataList=new ArrayList<HeightData>();
		sDataList_A4Map=new HashMap<String, List<SleepInfo_A4>>();
		dateMap=TestMainActivity.dateMap;
		contentResolver=this.getContentResolver();

		//retrieve a reference to an instance of TelephonyManager
//		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
		
		Date date= new Date();
  		long time = date.getTime();
  		Timestamp ts = new Timestamp(time);
  		String[] varw=ts.toString().split(" ");
  		String[] da=varw[0].split("-");
  		String[] ti=varw[1].split(":");
  		String[] ti2=ti[2].split("\\.");
  	    timew=da[0]+da[1]+da[2]+ti[0]+ti[1]+ti2[0];
  		
  		
//  	    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//		GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
//		//String networkOperator = telephonyManager.getNetworkOperator();
//		String networkOperator = telephonyManager.getNetworkOperator();
//		mcc = networkOperator.substring(0, 3);
//		mnc = networkOperator.substring(3);
//		cid = cellLocation.getCid();
//		lac = cellLocation.getLac();
		WifiManager wifiMan = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
    	WifiInfo wifiInf = wifiMan.getConnectionInfo();
    	macAddr = wifiInf.getMacAddress();
  	    
//  	    macAddrp = BluetoothAdapter.getDefaultAdapter().getAddress();
//  	    macAddr = macAddrp.toLowerCase();
    	
		//val = timew +"," + macAddr +"," + "high" + "," + "LBS:" + mnc + mcc + "," + lac + "," + cid +",Future Use" + ",Future Use" +",Future Use" ;
		//System.out.println(val);
		//Log.e("asdas", val);
  	    startTimer();
		
		initListView();//ListView

		mLsBleManager = LsBleManager.newInstance();
		mLsBleManager.initialize(getApplicationContext());
		
		

		//
		LsDeviceInfo newDevice=new LsDeviceInfo();
		//		newDevice.setBroadcastID("CAECA55C272E");
		//		newDevice.setBroadcastID("D9B385FC3DCB");
		newDevice.setBroadcastID("F901F50D5B4A");
		newDevice.setDeviceType("04");
		newDevice.setDeviceId("4a0407018847");

		WeightUserInfo weight=new WeightUserInfo();
		weight.setAge(89);
		weight.setAthleteActivityLevel(3);
		weight.setHeight(1.95f);
		weight.setSex(SexType.MALE);
		weight.setAthlete(true);
		weight.setProductUserNumber(5);
		weight.setUnit(UnitType.UNIT_KG);
		weight.setGoalWeight(88);
		weight.setWaistline(68);
		weight.setDeviceId("6458874C99B4");//13191 device ID
		mLsBleManager.setProductUserInfo(weight); 

		mLsBleManager.startDataReceiveService(reDataCallback);
		// Loader
		this.getLoaderManager().initLoader(0, null, this);

	}
	
	//Added VU 13-04-2016
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
                //com.elecfreaks.bleexample.MainActivity.timer.cancel();
                
                
            }

        }
    };

	/* Vijay
	private VibrationVoice readVibrationVoiceSettingInfo() 
	{
		SharedPreferences mPrefs=getSharedPreferences(getApplicationInfo().name, Context.MODE_PRIVATE);
		String vibrationVoiceInfoJson=mPrefs.getString(VibrationVoice.class.getName(), null);
		Gson gson = new Gson(); 
		return (VibrationVoice) gson.fromJson(vibrationVoiceInfoJson, VibrationVoice.class);

	} */

	public void doClick(View view) 
	{
		switch (view.getId()) 
		{
		case R.id.addDeviceBtn:
		{
			mLsBleManager.stopDataReceiveService();

			AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity1.this)
			.setTitle("Add device").setPositiveButton("Bluetooth Scanning", new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {

					Intent intent=new Intent(MainActivity1.this, AddDeviceActivity.class);
					cutoverActivity(intent, null,null);

				}
			}).setNegativeButton("QR Code Scanning", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {



					//					ServerAPI.APPNAME = "transhealth";
					//					ServerAPI.CARRIER = "lifesense";
					//
					//					ServerAPI.login(MainActivity.this.getApplicationContext(),
					//							"face@book.com", Md5.MD5("123456"), 0, generateUuid(), new JsonHttpResponseHandler(){
					//
					//						@Override
					//						public void onSuccess(int statusCode, JSONObject response) {
					//
					//							Log.e("login", "status code " + statusCode);
					//							Log.e("login", "response is " + response.toString());
					//							// SessionId, accessToken,NetManager
					//							String responseSessionId = response
					//									.optString("sessionId");
					//							Log.e("login in ", "sessionId IS " + responseSessionId);
					//							NetManager.sessionId = responseSessionId;
					//							NetManager.accessToken = response
					//									.optString("accessToken");

					//						}
					//
					//						@Override
					//						public void onFailure(Throwable e, JSONObject errorResponse) {
					//							Log.e("login error", e.getMessage());
					//							AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this)
					//							.setTitle("server disconnected").setPositiveButton("OK", new OnClickListener() {
					//
					//								@Override
					//								public void onClick(DialogInterface dialog, int which) {
					//
					//								}
					//							}).setMessage("can`t connect to the server!");
					//							builder.create().show();
					//						}
					//
					//
					//
					//					});


				}
			}).setMessage("choose the way to scan device");
			builder.create().show();
		}break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}


	@Override
	public void onResume() {
		super.onResume();

		wDataList_A2.clear();
		wDataList_A3.clear();
		bpDataList.clear();
		hDataList.clear();
		for(Entry<String, List<PedometerData>> entry:pDataListMap.entrySet()){

			entry.getValue().clear();

		}
		for(Entry<String, List<SleepInfo_A4>> entry:sDataList_A4Map.entrySet()){

			entry.getValue().clear();

		}

		WeightUserInfo weight=new WeightUserInfo();
		weight.setAge(89);
		weight.setAthleteActivityLevel(3);
		weight.setHeight(1.95f);
		weight.setSex(SexType.MALE);
		weight.setAthlete(true);
		weight.setProductUserNumber(5);
		weight.setUnit(UnitType.UNIT_KG);
		weight.setGoalWeight(88);
		weight.setWaistline(68);
		weight.setDeviceId("6458874C99B4");//13191 device ID
		mLsBleManager.setProductUserInfo(weight); 

		mLsBleManager.stopDataReceiveService();


		mLsBleManager.startDataReceiveService(reDataCallback);
		
		
		this.getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		CursorLoader loader = new CursorLoader(this,
				DatabaseContentProvider.CONTENT_URI, null, null, null, null);
		return loader;
	}


	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("unchecked")
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		myDeviceList.clear();
		lsDeviceAdapter.clear();

		int key_id = cursor.getColumnIndexOrThrow(DatabaseContentProvider.KEY_ID);

		int nameIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_NAME);
		int brocastIDIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_BROCASTID);
		int firmwareIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_FIRMWARE_VERSION);
		int hardwareIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_HARDWARE_VERSION);
		int deviceIDIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_ID);
		int manufactureNameIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_MANUFACTURENAME);
		int modelNumberIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_MODELNUMBER);
		int passwordIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_PASSWORD);
		int deviceSNIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_SN);
		int softwareIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_SOFTWARE_VERSION);
		int systemIDIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_SYSTEMID);
		int deviceTypeIndex = cursor
				.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_TYPE);
		//		int maxUserNumberIndex=cursor.getColumnIndexOrThrow(DatabaseContentProvider.KEY_MAX_USER_QUANTITY);
		int protocolTypeIndex=cursor.getColumnIndexOrThrow(DatabaseContentProvider.KEY_PROTOCOL_TYPE);
		int userNumberIndex=cursor.getColumnIndexOrThrow(DatabaseContentProvider.KEY_DEVICE_USER_NUMBER);

		while (cursor.moveToNext()) {
			LsDeviceInfo tempLsDevice = new LsDeviceInfo();
			DeviceInfoWithDate tempDeviceWithDate=new DeviceInfoWithDate();
			// tempUser.setId(cursor.getString(0));
			String id = cursor.getString(cursor
					.getColumnIndexOrThrow(DatabaseContentProvider.KEY_ID));
			Log.v("Device ID:", cursor.getString(cursor
					.getColumnIndexOrThrow(DatabaseContentProvider.KEY_ID)));

			tempLsDevice.setBroadcastID(cursor.getString(brocastIDIndex));
			tempLsDevice.setDeviceId(cursor.getString(deviceIDIndex));
			tempLsDevice.setDeviceSn(cursor.getString(deviceSNIndex));
			tempLsDevice.setFirmwareVersion(cursor.getString(firmwareIndex));

			tempLsDevice.setHardwareVersion(cursor.getString(hardwareIndex));
			tempLsDevice.setManufactureName(cursor
					.getString(manufactureNameIndex));
			tempLsDevice.setModelNumber(cursor.getString(modelNumberIndex));
			tempLsDevice.setPassword(cursor.getString(passwordIndex));
			//			tempLsDevice.setDeviceAddress(cursor.getString(addressIndex));
			tempLsDevice.setDeviceName(cursor.getString(nameIndex));
			tempLsDevice.setDeviceType(cursor.getString(deviceTypeIndex));
			tempLsDevice.setSoftwareVersion(cursor.getString(softwareIndex));
			tempLsDevice.setSystemId(cursor.getString(systemIDIndex));
			//			tempLsDevice.setMaxUserQuantity(Integer.parseInt(cursor.getString(maxUserNumberIndex)));
			tempLsDevice.setProtocolType(cursor.getString(protocolTypeIndex));
			tempLsDevice.setDeviceUserNumber(Integer.parseInt(cursor.getString(userNumberIndex)));
			tempDeviceWithDate.setLsDeviceInfo(tempLsDevice);

			if(dateMap.containsKey(tempLsDevice.getBroadcastID())){

				tempDeviceWithDate.setDate(dateMap.get(tempLsDevice.getBroadcastID()));

			} else {

				tempDeviceWithDate.setDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				dateMap.put(tempDeviceWithDate.getLsDeviceInfo().getBroadcastID(), tempDeviceWithDate.getDate());

			}

			deviceUserNumberMap.put(tempLsDevice.getDeviceId(), tempLsDevice.getDeviceUserNumber());
			lsDeviceAdapter.add(tempDeviceWithDate);
			lsDeviceAdapter.notifyDataSetChanged();
			System.out.println("add one measure dvice is ok?"+mLsBleManager.addMeasureDevice(tempLsDevice));
			System.err.println("device info=="+tempLsDevice.toString());
			//mLsBleManager.deleteMeasureDevice(tempLsDevice);
			//System.out.println("delete measure dvice is ok?"+mLsBleManager.deleteMeasureDevice(tempLsDevice));
		}
		if(myDeviceList!=null && myDeviceList.size()==0)
		{
//			showPromptDialog("Prompt", "There is no measuring device");
				Intent intent=new Intent(MainActivity1.this, AddDeviceActivity.class);
				cutoverActivity(intent, null,null);
		} else{
//			moveTaskToBack(true);
//			Intent intent=new Intent(MainActivity1.this, ReciveMail.class);
//			cutoverActivity(intent, null,null);
			moveTaskToBack(true);
			Intent intent=new Intent(MainActivity1.this, com.elecfreaks.bleexample.MainActivity.class);
			cutoverActivity(intent, null,null);
			
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	private void cutoverActivity(Intent myIntent,String deviceType, String protocolType)
	{
		if(deviceType!=null)
		{
			myIntent.putExtra("DeviceType", String.valueOf(deviceType));
			myIntent.putExtra("ProtocolType", protocolType);
		}
		startActivity(myIntent);
		// //
		//		overridePendingTransition(R.anim.dync_in_from_right,
		//				R.anim.dync_out_to_left);
	}

	private void showPromptDialog(String title, String message) 
	{
		AlertDialog.Builder builder = 
				new AlertDialog.Builder(MainActivity1.this)
		.setTitle(title)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

			}

		})
		.setMessage(message);
		builder.create().show();
	}

	private void initListView() 
	{
		//BloodPressureData bData;
		
		myDeviceList = new ArrayList<DeviceInfoWithDate>();
		listview = (ListView) findViewById(R.id.deviceListview);
		
		lsDeviceAdapter = new LsDeviceAdapter(getApplicationContext(),
				myDeviceList);
		listview.setAdapter(lsDeviceAdapter);
		//DeviceInfoWithDate lsDevice = (DeviceInfoWithDate) lsDeviceAdapter.getItem(0);
		//System.out.println(listview.getAdapter().getCount());
		
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view,
					final int position, long id)
			{
				AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity1.this)
				.setTitle("DeviceOperation")
//				.setPositiveButton("GetData", new OnClickListener(){
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						DeviceInfoWithDate lsDevice = (DeviceInfoWithDate) parent.getAdapter()
//								.getItem(position);
//						Intent myIntent = new Intent(MainActivity.this,
//								ShowMeasureDataActivity.class);	
//						myIntent.putExtra("pairedDeviceInfo", lsDevice);
//						//				startActivity(intent);
//						cutoverActivity(myIntent,lsDevice.getLsDeviceInfo().getDeviceType(),lsDevice.getLsDeviceInfo().getProtocolType());
//					}
//				})
				.setNegativeButton("Remove", new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String[] args={lsDeviceAdapter.getModelsList().get(position).getLsDeviceInfo().getBroadcastID()};
						mLsBleManager.deleteMeasureDevice(args[0]);
						contentResolver.delete(DatabaseContentProvider.CONTENT_URI, DatabaseContentProvider.KEY_DEVICE_BROCASTID+"=?", args);
						lsDeviceAdapter.removeView(position);
						dateMap.remove(args[0]);
						lsDeviceAdapter.notifyDataSetChanged();

					}
				}).setMessage("What do you want?");
				builder.create().show();

			}
		});

	}
	
	/*public void onBackPressed(){
		moveTaskToBack(true);
	//	new Finalizer().killApp(false);
		super.onBackPressed();
		Intent intent = new Intent(MainActivity1.this,HeartbeatsActivity.class);
		startActivity(intent);

	}*/

	@SuppressWarnings("unchecked")
	private void updateMeasureRecord(final String broadcastId,final int recordsCount) 
	{
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (!myDeviceList.isEmpty())
				{
					for (DeviceInfoWithDate dev : myDeviceList) {
						if (dev != null&& dev.getLsDeviceInfo().getBroadcastID().equals(broadcastId)) {
							final View view = listview.getChildAt(lsDeviceAdapter
									.getPosition(dev));
							TextView recordTextView = (TextView) view
									.findViewById(R.id.d_RecordTextView);
							recordTextView.setText( recordsCount+ " New Records");
							recordTextView.setBackgroundColor(Color.RED);
							Animation anim = new AlphaAnimation(0.0f, 1.0f);
							anim.setDuration(200); // You can manage the time of the blink with this parameter
							anim.setStartOffset(20);
							anim.setRepeatMode(Animation.REVERSE);
							anim.setRepeatCount(Animation.INFINITE);
							recordTextView.startAnimation(anim);
						}
					}
				}

			}
			
			

		
		});
		
	}
	
	public void startTimer() {
		// TODO Auto-generated method stub
		timer = new Timer();
		initializeTimerTask();
		timer.schedule(timerTask, 15000,300000);
	}

	private void initializeTimerTask() {
		// TODO Auto-generated method stub
		timerTask = new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.post(new Runnable(){

					@Override
					public void run() {
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
								gps = new GetGPS(MainActivity1.this);
								if(gps.canGetLocation()){
									double latitude = gps.getLatitude();
									double longitude = gps.getLongitude();
									
									if(latitude != 0.0 && longitude != 0.0 ){
										// \n is for new line
										//Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
										System.out.println("Lat:" +latitude + "\nLong:" + longitude);
										Log.e("GPS", "Lat:" +latitude + "\nLong:" + longitude);
										val2 = "GPS:" + latitude +"/" + longitude;
										getBatteryPercentage();
									}
								}
							}
						});
						
						
						
						
						
						
					}
					
				});
			}
			
		};
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
		          //   batteryPercent.setText("Battery Level Remaining: " + level + "%");
		            //   Toast.makeText(getApplicationContext(), "Battery % is"+level, Toast.LENGTH_LONG).show();
		             
		                val = timew +"," + macAddr +"," + "high" + "," + "LBS:" + mcc + mnc + "," + lac + "," + cid + "," + val2 + ",Battery Percentage is " +level +",Steps is "+numSteps ;
						System.out.println(val);
						Log.e("asdas", val);
						// TODO Auto-generated method stub
						String Name="Pramod 7 S8 BT";
				    	String Phone="+6112345678912";
				    	String Subject="LbsTest,"+Phone+","+Name;
						System.out.println("In Timer");
						SendMail mail = new SendMail(Subject, val, 0);
		             
		             
		           
		         }
		     }; 
		     IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		     registerReceiver(batteryLevelReceiver, batteryLevelFilter);
		  }
}
