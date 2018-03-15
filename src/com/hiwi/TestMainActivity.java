package com.hiwi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lifesense.ble.LsBleManager;
import com.lifesense.ble.PairCallback;
import com.lifesense.ble.bean.LsDeviceInfo;
import com.lifesense.ble.bean.WeightData_A2;
import org.opencv.javacv.facerecognition.R;

public class TestMainActivity extends Activity {

	private Intent mIntent;
	private BleDevice device;
	private TextView infoTextView;
	private LsBleManager lsBleManager;
	private LsDeviceInfo lsDevice;
	private ContentValues contentValues;
	private ContentResolver contentResolver;
	private LsDeviceInfo pairedLsDevice;
	private ProgressDialog ringProgressDialog;
	private boolean hasPairedDeviceInfo;
	private Button getDataBtn;
	private Button pairBtn;
	public static final Map<String, String> dateMap = new HashMap<String, String>();
	private PairCallback pairCallback = new PairCallback() {
		// 
		@SuppressLint("SimpleDateFormat")
		@Override
		public void onPairResults(final LsDeviceInfo lsDevice, int status) {
			Log.d("SADASDSAD",""+lsDevice.getDeviceName());
			Log.i("can show paired device?", "??????????????????");
			if (lsDevice != null) {
				hasPairedDeviceInfo = true;
				if (ringProgressDialog != null) {
					ringProgressDialog.dismiss();
				}
				Log.i("can", "show paired result!!!!!!!!!!");
				//showPairResults(lsDevice);
				pairedLsDevice = lsDevice;
				
				savePairedDevice(pairedLsDevice);
				dateMap.put(pairedLsDevice.getBroadcastID(),
						new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new Date()));
				// 
				Intent intent = new Intent(TestMainActivity.this,
						MainActivity1.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				

			} else {
				if (ringProgressDialog != null) {
					ringProgressDialog.dismiss();
				}
				showPromptDialog("Failed to paired device,please try again");
			}

		}


	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_test_main);

		mIntent = getIntent();
		lsDevice = mIntent.getParcelableExtra("object");
		// getDeviceInfoByName(device.getName());
		System.out.println("HEY"+lsDevice.getDeviceName());
		infoTextView = (TextView) findViewById(R.id.textView);
		infoTextView.setMovementMethod(new ScrollingMovementMethod());
		getDataBtn=(Button) findViewById(R.id.tGetDataBtn);
		pairBtn=(Button) findViewById(R.id.tPairedBtn);
//		if("A4".equals(lsDevice.getProtocolType())){
//
//
//			getDataBtn.setText("Disconnect");
//
//			pairBtn.setText("Connect");
//
//		} else {
//
//			getDataBtn.setText("Upgrade");
//
//			pairBtn.setText("Pair Device");
//
//		}

		TextView name = (TextView) findViewById(R.id.header);
		name.setText(lsDevice.getDeviceName());
		lsBleManager = LsBleManager.newInstance();

		contentResolver = this.getContentResolver();
		contentValues = new ContentValues();
		hasPairedDeviceInfo = false;
		
		
		boolean flags = lsBleManager.startPairing(lsDevice, pairCallback);
		Log.e("click tPairedBtn", "can start pairing " + flags);
		if (flags) {
			if (ringProgressDialog != null) {
				ringProgressDialog.dismiss();
			}
			showProgressDialog("Pair Device", "Pairing,please wait......");

		} else {
			if (ringProgressDialog != null) {
				ringProgressDialog.dismiss();
			}
			showPromptDialog("Failed to paired device,please try again");
		}
		MainActivity1.flag = true;
		Log.d("HERE",""+hasPairedDeviceInfo);
		if (hasPairedDeviceInfo){
			Log.d("ASD","ADASDSADSAD");
			
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
	}

	@SuppressLint("SimpleDateFormat")
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.backBtn: {
//			if (hasPairedDeviceInfo) {
//				showPromptDialog("Prompt",
//						"Do you want to save the paired results?");
//			} else {
//				onBackPressed();
//			}

		}
		break;
		case R.id.tPairedBtn: {

			

		}
		break;

		case R.id.tGetDataBtn: {
			if(getDataBtn.getText().equals("Disconnect")){

				//TODO

			} else {

				showPromptDialog("Not support!now");

			}

		}
		break;
		}
	}

	private void showPromptDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				TestMainActivity.this).setTitle("Prompt")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				}).setMessage(message);

		builder.create().show();
	}

	private void showProgressDialog(String title, String message) {
		ringProgressDialog = ProgressDialog.show(TestMainActivity.this, title,
				message, true);
		ringProgressDialog.setCancelable(true);
	}

	private void showPromptDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				TestMainActivity.this)
		.setTitle(title)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(DialogInterface dialog, int which) {

				savePairedDevice(pairedLsDevice);
				dateMap.put(pairedLsDevice.getBroadcastID(),
						new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new Date()));
				// 
				Intent intent = new Intent(TestMainActivity.this,
						MainActivity1.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}

		})
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {

				onBackPressed();
			}
		}).setMessage(message);
		builder.create().show();
	}

	private void savePairedDevice(LsDeviceInfo device) {
		if (device != null) {
			contentValues.put(DatabaseContentProvider.KEY_DEVICE_NAME,
					device.getDeviceName());
			contentValues.put(DatabaseContentProvider.KEY_DEVICE_BROCASTID,
					device.getBroadcastID());
			contentValues.put(
					DatabaseContentProvider.KEY_DEVICE_FIRMWARE_VERSION,
					device.getFirmwareVersion());
			contentValues.put(
					DatabaseContentProvider.KEY_DEVICE_HARDWARE_VERSION,
					device.getHardwareVersion());
			contentValues.put(DatabaseContentProvider.KEY_DEVICE_ID,
					device.getDeviceId());
			contentValues.put(
					DatabaseContentProvider.KEY_DEVICE_MANUFACTURENAME,
					device.getManufactureName());
			contentValues.put(DatabaseContentProvider.KEY_DEVICE_MODELNUMBER,
					device.getModelNumber());
			contentValues.put(DatabaseContentProvider.KEY_DEVICE_PASSWORD,
					device.getPassword());
			contentValues.put(DatabaseContentProvider.KEY_DEVICE_SN,
					device.getDeviceSn());
			contentValues.put(
					DatabaseContentProvider.KEY_DEVICE_SOFTWARE_VERSION,
					device.getSoftwareVersion());
			contentValues.put(DatabaseContentProvider.KEY_DEVICE_SYSTEMID,
					device.getSystemId());
			contentValues.put(DatabaseContentProvider.KEY_DEVICE_TYPE,
					device.getDeviceType());
			contentValues.put(DatabaseContentProvider.KEY_DEVICE_USER_NUMBER,
					device.getDeviceUserNumber());
			contentValues.put(DatabaseContentProvider.KEY_MAX_USER_QUANTITY,
					device.getMaxUserQuantity());
			contentValues.put(DatabaseContentProvider.KEY_PROTOCOL_TYPE,
					device.getProtocolType());

			//
			contentResolver.insert(DatabaseContentProvider.CONTENT_URI,
					contentValues);
		}
	}


	private void showPairResults(final LsDeviceInfo device) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (device != null) {
					infoTextView
					.append("***********Successfully Paired************"
							+ "\n");
					infoTextView.append("deviceName: " + device.getDeviceName()
							+ "\n");
					infoTextView.append("broadcastID: "
							+ device.getBroadcastID() + "\n");
					infoTextView.append("deviceType: " + device.getDeviceType()
							+ "\n");
					infoTextView.append("password: " + device.getPassword()
							+ "\n");
					infoTextView.append("deviceID: " + device.getDeviceId()
							+ "\n");
					infoTextView.append("deviceSN: " + device.getDeviceSn()
							+ "\n");
					infoTextView.append("manufactureName: "
							+ device.getManufactureName() + "\n");
					infoTextView.append("modelNumber: "
							+ device.getModelNumber() + "\n");
					infoTextView.append("firmwareVersion: "
							+ device.getFirmwareVersion() + "\n");
					infoTextView.append("hardwareVersion: "
							+ device.getHardwareVersion() + "\n");
					infoTextView.append("softwareVersion: "
							+ device.getSoftwareVersion() + "\n");
					infoTextView.append("UserNumber: "
							+ device.getDeviceUserNumber() + "\n");
					infoTextView.append("maxUserQuantity: "
							+ device.getMaxUserQuantity() + "\n");
					infoTextView.append("protocolType: "
							+ device.getProtocolType() + "\n");
				} else {
					infoTextView
					.append("Failed paired!Please try again" + "\n");
				}

			}
		});

	}
}
