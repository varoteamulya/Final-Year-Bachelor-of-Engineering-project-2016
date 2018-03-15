package com.hiwi;
import org.opencv.javacv.facerecognition.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifesense.ble.LsBleManager;
import com.lifesense.ble.bean.LsDeviceInfo;
import com.lifesense.ble.bean.PersonalUserInfo;
import com.lifesense.ble.bean.SexType;
import com.lifesense.ble.bean.WeightData_A2;

public class ShowDetailsActivity extends Activity {

	private LsBleManager lsBleManager;
	private TextView infoTextView;
	private int genericFatScaleDataCount;
	private TextView titleTextView;
	private Button connectBtn;
	private ProgressDialog ringProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_details);
		infoTextView = (TextView) findViewById(R.id.infoTextView);
		titleTextView = (TextView) findViewById(R.id.showDetailsheader);
		infoTextView.setMovementMethod(new ScrollingMovementMethod());

		LsDeviceInfo lsDevice = (LsDeviceInfo) getIntent().getParcelableExtra(
				"object");

		// set Activity title
		titleTextView.setText(lsDevice.getDeviceName());

		// init ble
		lsBleManager = LsBleManager.newInstance();
		connectBtn = (Button) findViewById(R.id.connectBtn);

		// bleManager.addMeasureDevice(lsDevice); // add single measure device
		initButtonOnClickEvent(lsDevice);
		genericFatScaleDataCount = 0;

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		returnPreviousScreen();
	}

	private void initButtonOnClickEvent(final LsDeviceInfo lsDevice) {

		connectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (connectBtn.getText().equals("Connect")) {
					infoTextView.setText("");
					showProgressDialog("Prompt",
							"device connecting, please Wait...");
					connectGenericFatScale(lsDevice);
				} else if (connectBtn.getText().equals("Disconnect")) {
					if (lsBleManager != null) {
						lsBleManager.disconnectDevice();
					}
				}

			}
		});

		ImageView backBtn = (ImageView) findViewById(R.id.showDetailsActivity_backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				returnPreviousScreen();

			}
		});
	}

	private void showProgressDialog(String title, String message) {
		ringProgressDialog = ProgressDialog.show(ShowDetailsActivity.this,
				title, message, true);
		ringProgressDialog.setCancelable(true);
	}

	private void returnPreviousScreen() {
		Intent intent = new Intent(ShowDetailsActivity.this,
				AddDeviceActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void connectGenericFatScale(LsDeviceInfo lsDevice) {
		PersonalUserInfo userInfo = new PersonalUserInfo();
		userInfo.setAge(45);
		userInfo.setHeight(175);
		userInfo.setWeight(65);
		userInfo.setSex(SexType.MALE);
		/*
		 * lsBleManager.connectDevice(lsDevice,userInfo, new
		 * OnLsDeviceConnectListener() {
		 * 
		 * @Override public void onReceiveMeasuredData(WeightData_A2 fatData) {
		 * printlnTemperatureData(fatData,0);
		 * 
		 * }
		 * 
		 * @Override public void onReceiveDeviceInfo(LsDeviceInfo lsDeviceInfo)
		 * { printlnDeviceInfo(lsDeviceInfo);
		 * 
		 * }
		 * 
		 * @Override public void onConnectStateChange(DeviceConnectState
		 * connectState) { printlnMessage(connectState);
		 * 
		 * } });
		 */

	}

	private void printlnTemperatureData(final WeightData_A2 wData,
			final int dataType) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				genericFatScaleDataCount++;
				infoTextView.append("---------------------" + "\n");
				if (dataType == 0) {
					infoTextView.append("Measurement records："
							+ genericFatScaleDataCount + "\n");
				}

				infoTextView.append("Measuring time：" + wData.getDate() + "\n");
				infoTextView.append("DeviceSn：" + wData.getDeviceSn() + "\n");
				infoTextView.append("Resistance_1：" + wData.getResistance_1()
						+ "\n");
				infoTextView.append("Resistance_2：" + wData.getResistance_2()
						+ "\n");
				infoTextView.append("UserNumber：" + wData.getUserNo() + "\n");
				infoTextView.append("isAppendMeasurement："
						+ wData.isHasAppendMeasurement() + "\n");
				infoTextView.append("Battery：" + wData.getBattery() + "\n");
				infoTextView.append("WeightStatus：" + wData.getWeightStatus()
						+ "\n");
				infoTextView.append("ImpedanceStatus："
						+ wData.getImpedanceStatus() + "\n");
				infoTextView.append("Weight：" + wData.getWeight() + "\n");
				infoTextView.append("BodyFatRatio：" + wData.getBodyFatRatio()
						+ "\n");
				infoTextView.append("BodyWaterRatio："
						+ wData.getBodyWaterRatio() + "\n");
				infoTextView.append("VisceralFatLevel："
						+ wData.getVisceralFatLevel() + "\n");
				infoTextView.append("MuscleMassRatio："
						+ wData.getMuscleMassRatio() + "\n");
				infoTextView.append("BoneDensity：" + wData.getBoneDensity()
						+ "\n");
				infoTextView.append("BasalMetabolism："
						+ wData.getBasalMetabolism() + "\n");
				infoTextView.append("Unit：" + wData.getDeviceSelectedUnit()
						+ "\n");
				infoTextView
						.append("----------------------------------" + "\n");

			}
		});

	}

	/*
	 * private void printlnMessage(final DeviceConnectState connectState) {
	 * runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { if(ringProgressDialog.isShowing()) {
	 * ringProgressDialog.dismiss(); }
	 * 
	 * String
	 * stateString="<font color='red'>Current connection status-"+connectState
	 * +"</font>";
	 * 
	 * infoTextView.append(Html.fromHtml(stateString));
	 * infoTextView.append("\n"); //
	 * if(connectState==DeviceConnectState.CONNECTED_SUCCESS) // { //
	 * connectBtn.setText("Disconnect"); // } // else
	 * if(connectState==DeviceConnectState.DISCONNECTED) // { //
	 * connectBtn.setText("Connect"); // }
	 * 
	 * } });
	 * 
	 * }
	 */

	private void printlnDeviceInfo(final LsDeviceInfo device) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				infoTextView.append("---Device Information---" + "\n");

				infoTextView.append("device name: " + device.getDeviceName()
						+ "\n");
				infoTextView.append("broadcast id: " + device.getBroadcastID()
						+ "\n");
				infoTextView.append("protocol type: "
						+ device.getProtocolType() + "\n");

				infoTextView.append("device type: " + device.getDeviceType()
						+ "\n");
				infoTextView
						.append("device id: " + device.getDeviceId() + "\n");
				infoTextView
						.append("device SN: " + device.getDeviceSn() + "\n");
				infoTextView.append("manufacture name:"
						+ device.getManufactureName() + "\n");
				infoTextView.append("Model Number:" + device.getModelNumber()
						+ "\n");
				infoTextView.append("FirmwareVersion: "
						+ device.getFirmwareVersion() + "\n");
				infoTextView.append("HardwareVersion: "
						+ device.getHardwareVersion() + "\n");
				infoTextView.append("SoftwareVersion: "
						+ device.getSoftwareVersion() + "\n");

			}
		});

	}

}
