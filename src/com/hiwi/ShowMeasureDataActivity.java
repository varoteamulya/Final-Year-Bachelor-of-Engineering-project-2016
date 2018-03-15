package com.hiwi;

import java.math.BigDecimal;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.lifesense.ble.LsBleManager;
import com.lifesense.ble.bean.BloodPressureData;
import com.lifesense.ble.bean.DeviceTypeConstants;
import com.lifesense.ble.bean.HeightData;
import com.lifesense.ble.bean.LsDeviceInfo;
import com.lifesense.ble.bean.PedometerData;
import com.lifesense.ble.bean.SleepInfo_A4;
import com.lifesense.ble.bean.WeightData_A2;
import com.lifesense.ble.bean.WeightData_A3;
import org.opencv.javacv.facerecognition.R;

public class ShowMeasureDataActivity extends Activity {

	private TextView showTextView;
	private List<BloodPressureData> mBloodPressureDataList;
	private List<WeightData_A3> mWeightDataList;
	private List<WeightData_A2> weightData_A2s;
	private List<PedometerData> pedometerDatas;
	private List<HeightData> heightDatas;
	private List<SleepInfo_A4> sleepDatas;
	public static boolean isCurrentPage = false;
	private DeviceInfoWithDate deviceInfo ;
	public static Handler dataReceiveHandler=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isCurrentPage=true;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_measure_data);
		showTextView = (TextView) findViewById(R.id.measureDataView);
		showTextView.setMovementMethod(new ScrollingMovementMethod());
		final String deviceType = getIntent().getStringExtra("DeviceType");
		final String protocolType = getIntent().getStringExtra("ProtocolType");
		deviceInfo = getIntent().getParcelableExtra(
				"pairedDeviceInfo");
		if(deviceInfo!=null)
		{
			runOnUiThread(new Runnable(){

				@Override
				public void run() {

					if(!"A4".equals(protocolType)){

						LsBleManager.newInstance().stopDataReceiveService();

					}
					showMeasureData(deviceType, protocolType, deviceInfo.getLsDeviceInfo());

				}});

		}
		else 
		{
			System.out.println("is null=================");
		}
		Looper looper=Looper.getMainLooper();
		dataReceiveHandler=new Handler(looper){

			@Override
			public void handleMessage(Message msg) {

				Log.e("data message "+msg.what+" from main", "showing data to measurement data page");
				switch (msg.what) {
				case MainActivity1.MAIN_SEND_DATA_BLOOD_PRESSURE:
					if(!deviceInfo.getLsDeviceInfo().getBroadcastID().equals(msg.obj)){

						break;

					}
					mBloodPressureDataList = MainActivity1.bpDataList;
					showBloodPressureMeasureData(mBloodPressureDataList);
					break;
				case MainActivity1.MAIN_SEND_DATA_HEIGHT:
					if(!deviceInfo.getLsDeviceInfo().getBroadcastID().equals(msg.obj)){

						break;

					}
					heightDatas = MainActivity1.hDataList;
					showHeightMeasureData(heightDatas);
					break;
				case MainActivity1.MAIN_SEND_DATA_PEDOMETER:
					if(!deviceInfo.getLsDeviceInfo().getBroadcastID().equals(msg.obj)){

						break;

					}
					pedometerDatas = MainActivity1.pDataListMap.get(deviceInfo.getLsDeviceInfo().getBroadcastID());
					showPedometerMeasureData(pedometerDatas);
					break;
				case MainActivity1.MAIN_SEND_DATA_PEDOMETER_A4:
					if(!deviceInfo.getLsDeviceInfo().getBroadcastID().equals(msg.obj)){

						break;

					}
					pedometerDatas = MainActivity1.pDataListMap.get(deviceInfo.getLsDeviceInfo().getBroadcastID());
					showPedometerMeasureData(pedometerDatas);
					break;
				case MainActivity1.MAIN_SEND_DATA_SLEEP_A4:
					if(!deviceInfo.getLsDeviceInfo().getBroadcastID().equals(msg.obj)){

						break;

					}
					sleepDatas=MainActivity1.sDataList_A4Map.get(deviceInfo.getLsDeviceInfo().getBroadcastID());
					showSleepInfoMeasureData_A4(sleepDatas);
				case MainActivity1.MAIN_SEND_DATA_WEIGHT_A2:
					if(!deviceInfo.getLsDeviceInfo().getBroadcastID().equals(msg.obj)){

						break;

					}
					weightData_A2s = MainActivity1.wDataList_A2;
					showWeightMeasureData_A2(weightData_A2s);
					break;
				case MainActivity1.MAIN_SEND_DATA_WEIGHT_A3:
					if(!deviceInfo.getLsDeviceInfo().getBroadcastID().equals(msg.obj)){

						break;

					}
					mWeightDataList = MainActivity1.wDataList_A3;
					showWeightMeasureData(mWeightDataList);
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}

		};

	}

	@Override
	protected void onRestart() {
		isCurrentPage=true;
		super.onRestart();
	}

	@Override
	protected void onResume() {
		isCurrentPage=true;
		super.onResume();
	}



	@Override
	protected void onPause() {
		isCurrentPage=false;
		super.onPause();
	}

	@Override
	public void finish() {
		isCurrentPage=false;
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		isCurrentPage=false;
		dataReceiveHandler.removeCallbacksAndMessages(null);
		dataReceiveHandler=null;
		super.onDestroy();
	}

	private void showMeasureData(String deviceType, String protocolType,
			LsDeviceInfo deviceInfo) {
		if (deviceType != null) {
			if (deviceType.equals(DeviceTypeConstants.SPHYGMOMAN_METER)) {
				mBloodPressureDataList = MainActivity1.bpDataList;
				showBloodPressureMeasureData(mBloodPressureDataList);
			} else if (deviceType.equals(DeviceTypeConstants.FAT_SCALE)) {
				mWeightDataList = MainActivity1.wDataList_A3;
				showWeightMeasureData(mWeightDataList);
			} else if (deviceType.equals(DeviceTypeConstants.WEIGHT_SCALE)) {
				weightData_A2s = MainActivity1.wDataList_A2;
				showWeightMeasureData_A2(weightData_A2s);
			} else if (deviceType.equals(DeviceTypeConstants.PEDOMETER)) {

				Log.i("ShowMeasureDataActivity-->showMeasureData-->protocolType",
						protocolType);
				if ("A4".equals(protocolType)) {

					pedometerDatas = MainActivity1.pDataListMap.get(deviceInfo.getBroadcastID());
					showPedometerMeasureData(pedometerDatas);


				} else {

					pedometerDatas = MainActivity1.pDataListMap.get(deviceInfo.getBroadcastID());
					showPedometerMeasureData(pedometerDatas);

				}

			} else if (deviceType.equals(DeviceTypeConstants.HEIGHT_RULER)) {
				heightDatas = MainActivity1.hDataList;
				showHeightMeasureData(heightDatas);
			} else if (deviceType.equals(DeviceTypeConstants.SPHYGMOMAN_METER)) {
				mBloodPressureDataList = MainActivity1.bpDataList;
				showBloodPressureMeasureData(mBloodPressureDataList);
			}

		} else
			showTextView.setText("no measure data now!");

	}

	private void showHeightMeasureData(final List<HeightData> hDatas) {

		if (hDatas != null) {
			showTextView.append("------The total number of records:"
					+ hDatas.size() + "----" + "\n");
			for (HeightData hData : hDatas) {
				showTextView.append("Record Number:" + hDatas.indexOf(hData)
						+ "\n");
				showTextView.append("Measuring time：" + hData.getDate() + "\n");
				showTextView.append("DeviceSn：" + hData.getDeviceSn() + "\n");
				showTextView.append("UserNumber：" + hData.getUserNo() + "\n");
				showTextView.append("Height：" + hData.getHeight() + "\n");
				showTextView.append("Unit ：" + hData.getUnit() + "\n");
				showTextView.append("Battery：" + hData.getBattery() + "\n");
				showTextView
				.append("----------------------------------" + "\n");
			}

		} else {
			showTextView.append("No measured Record" + "\n");
		}





	}

	private void showPedometerMeasureData(final List<PedometerData> pDatas) {

		if (pDatas != null) {
			showTextView.append("------The total number of records:"
					+ pDatas.size() + "----" + "\n");
			for (PedometerData pData : pDatas) {
				showTextView.append("Record Number:"
						+ pDatas.indexOf(pData) + "\n");
				showTextView.append("Measuring time：" + pData.getDate()
						+ "\n");
				showTextView.append("DeviceSN：" + pData.getDeviceSn()
						+ "\n");
				showTextView.append("UserNumber：" + pData.getUserNo()
						+ "\n");
				showTextView.append("WalkSteps：" + pData.getWalkSteps()
						+ "\n");
				showTextView.append("RunSteps ：" + pData.getRunSteps()
						+ "\n");
				showTextView.append("Calories：" + pData.getCalories()
						+ "\n");
				showTextView.append("ExerciseTime："
						+ pData.getExerciseTime() + "\n");
				showTextView.append("Distance：" + pData.getDistance()
						+ "\n");
				showTextView.append("Battery：" + pData.getBattery()
						+ "\n");
				showTextView.append("SleepStatus："
						+ pData.getSleepStatus() + "\n");
				showTextView.append("IntensityLevel："
						+ pData.getIntensityLevel() + "\n");
				showTextView
				.append("----------------------------------"
						+ "\n");
			}


		} else {
			showTextView.append("No measured Record" + "\n");
		}
		Log.i("ShowMeasureDataActivity-->showPedometerMeasureData()-->deviceInfo", deviceInfo==null?"null":deviceInfo.toString());
		Log.i("ShowMeasureDataActivity-->showPedometerMeasureData()-->deviceInfo in map", MainActivity1.sDataList_A4Map.get(deviceInfo.getLsDeviceInfo().getBroadcastID())==null?"null":MainActivity1.sDataList_A4Map.get(deviceInfo.getLsDeviceInfo().getBroadcastID()).toString());
		if (MainActivity1.sDataList_A4Map.get(deviceInfo.getLsDeviceInfo().getBroadcastID()) != null && !MainActivity1.sDataList_A4Map.get(deviceInfo.getLsDeviceInfo().getBroadcastID()).isEmpty()) {

			showTextView.append("------The total number of records:"
					+ MainActivity1.sDataList_A4Map.get(deviceInfo.getLsDeviceInfo().getBroadcastID()).size() + "----\n");
			for (SleepInfo_A4 sleepInfo : MainActivity1.sDataList_A4Map.get(deviceInfo.getLsDeviceInfo().getBroadcastID())) {

				showTextView.append("Rrecord Number: "
						+ MainActivity1.sDataList_A4Map.get(sleepInfo.getBroadcastId()).indexOf(sleepInfo)
						+ "\n");
				showTextView.append("DeviceId: "
						+ sleepInfo.getDeviceId() + "\n");
				showTextView.append("BroadcastId: "
						+ sleepInfo.getBroadcastId() + "\n");
				showTextView.append("SleepStatus: "
						+ sleepInfo.getSleepStatus() + "\n");
				showTextView.append("Date: " + sleepInfo.getDate()
						+ "\n");
				showTextView
				.append("Utc: " + sleepInfo.getUtc() + "\n");
				showTextView
				.append("----------------------------------\n");

			}

		} else {
			showTextView.append("No measured Record" + "\n");
		}




	}

	private void showSleepInfoMeasureData_A4(final List<SleepInfo_A4> sDatas) {



		if (sDatas != null) {

			showTextView.append("------The total number of records:"
					+ sDatas.size() + "----\n");
			for (SleepInfo_A4 sleepInfo : sDatas) {

				showTextView.append("Rrecord Number: "
						+ sDatas.indexOf(sleepInfo)
						+ "\n");
				showTextView.append("DeviceId: "
						+ sleepInfo.getDeviceId() + "\n");
				showTextView.append("BroadcastId: "
						+ sleepInfo.getBroadcastId() + "\n");
				showTextView.append("SleepStatus: "
						+ sleepInfo.getSleepStatus() + "\n");
				showTextView.append("Date: " + sleepInfo.getDate()
						+ "\n");
				showTextView
				.append("Utc: " + sleepInfo.getUtc() + "\n");
				showTextView
				.append("----------------------------------\n");

			}

		}



	}

	@SuppressWarnings("unused")
	private void showWeightMeasureData_A2(final WeightData_A2 wData) {

		if (wData != null) {
			showTextView.append("------new measured data----" + "\n");
			// for(WeightData_A2 wData:wData_A2s)
			// {
			// showTextView.append("Record Number:"+wData_A2s.indexOf(wData)+"\n");
			showTextView.append("Measuring time：" + wData.getDate()
					+ "\n");
			showTextView.append("DeviceSN：" + wData.getDeviceSn()
					+ "\n");
			showTextView.append("UserNumber：" + wData.getUserNo()
					+ "\n");
			showTextView.append("Weight：" + wData.getWeight() + "\n");
			showTextView.append("Fat rate：" + wData.getPbf() + "\n");
			showTextView.append("Resistance_1："
					+ wData.getResistance_1() + "\n");
			showTextView.append("Resistance_2："
					+ wData.getResistance_2() + "\n");
			showTextView.append("Unit：" + wData.getDeviceSelectedUnit()
					+ "\n");
			showTextView.append("VoltageData：" + wData.getVoltageData()
					+ "\n");
			showTextView.append("Battery level：" + wData.getBattery()
					+ "\n");
			showTextView.append("----------------------------------"
					+ "\n");
			// }

		} else {
			showTextView.append("No measured Record" + "\n");
		}


	}

	private void showWeightMeasureData_A2(final List<WeightData_A2> wData_A2s) {


		if (wData_A2s != null) {
			showTextView.append("------The total number of records:"
					+ wData_A2s.size() + "----" + "\n");
			for (WeightData_A2 wData : wData_A2s) {
				double weight = new BigDecimal(wData.getWeight()).setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				showTextView.append("Record Number:" + wData_A2s.indexOf(wData)
						+ "\n");
				showTextView.append("Measuring time：" + wData.getDate() + "\n");
				showTextView.append("DeviceSN：" + wData.getDeviceSn() + "\n");
				showTextView.append("UserNumber：" + wData.getUserNo() + "\n");
				showTextView.append("Weight：" + weight + "\n");
				showTextView.append("Fat rate：" + wData.getPbf() + "\n");
				showTextView.append("Resistance_1：" + wData.getResistance_1()
						+ "\n");
				showTextView.append("Resistance_2：" + wData.getResistance_2()
						+ "\n");
				showTextView.append("Unit：" + wData.getDeviceSelectedUnit()
						+ "\n");
				showTextView.append("VoltageData：" + wData.getVoltageData()
						+ "\n");
				showTextView.append("Battery level：" + wData.getBattery()
						+ "\n");
				showTextView
				.append("----------------------------------" + "\n");
			}

		} else {
			showTextView.append("No measured Record" + "\n");
		}




	}

	private void showWeightMeasureData(final List<WeightData_A3> wDataList) {



		if (wDataList != null && wDataList.size() > 0) {
			showTextView.append("------The total number of records:"
					+ wDataList.size() + "----" + "\n");
			for (WeightData_A3 wData : wDataList) {
				showTextView.append("Record Number:" + wDataList.indexOf(wData)
						+ "\n");
				showTextView.append("Measuring time：" + wData.getDate() + "\n");
				showTextView.append("DeviceSn：" + wData.getDeviceSn() + "\n");
				showTextView.append("mpedance：" + wData.getImpedance() + "\n");
				showTextView.append("UserNumber：" + wData.getUserId() + "\n");
				showTextView.append("isAppendMeasurement："
						+ wData.isAppendMeasurement() + "\n");
				showTextView.append("Battery：" + wData.getBattery() + "\n");
				showTextView.append("WeightStatus：" + wData.getWeightStatus()
						+ "\n");
				showTextView.append("ImpedanceStatus："
						+ wData.getImpedanceStatus() + "\n");
				showTextView.append("AccuracyStatus："
						+ wData.getAccuracyStatus() + "\n");
				showTextView.append("Weight：" + wData.getWeight() + "\n");
				showTextView.append("BodyFatRatio：" + wData.getBodyFatRatio()
						+ "\n");
				showTextView.append("BodyWaterRatio："
						+ wData.getBodyWaterRatio() + "\n");
				showTextView.append("VisceralFatLevel："
						+ wData.getVisceralFatLevel() + "\n");
				showTextView.append("MuscleMassRatio："
						+ wData.getMuscleMassRatio() + "\n");
				showTextView.append("BoneDensity：" + wData.getBoneDensity()
						+ "\n");
				showTextView.append("BasalMetabolism："
						+ wData.getBasalMetabolism() + "\n");
				showTextView.append("Unit：" + wData.getDeviceSelectedUnit()
						+ "\n");
				showTextView
				.append("----------------------------------" + "\n");
			}
		} else {
			showTextView.append("No measured records" + "\n");
		}



	}

	private void showBloodPressureMeasureData(final List<BloodPressureData> bpDataList) {

		if (bpDataList != null && bpDataList.size() > 0) {
			showTextView.append("------The total number of records:"
					+ bpDataList.size() + "----" + "\n");
			for (BloodPressureData bData : bpDataList) {
				showTextView.append("Record Number:"
						+ bpDataList.indexOf(bData) + "\n");
				showTextView.append("DeviceSn：" + bData.getDeviceSn() + "\n");
				showTextView.append("BroadcastID：" + bData.getBroadcastId()
						+ "\n");
				showTextView.append("Measuring time：" + bData.getDate() + "\n");
				showTextView.append("userNumber：" + bData.getUserId() + "\n");
				showTextView.append("Systolic：" + bData.getSystolic() + "\n");
				showTextView
				.append("Diastolic ：" + bData.getDiastolic() + "\n");
				showTextView.append("PulseRate：" + bData.getPulseRate() + "\n");
				showTextView.append("MeanArterialPressure："
						+ bData.getMeanArterialPressure() + "\n");
				showTextView.append("Unit：" + bData.getDeviceSelectedUnit()
						+ "\n");
				showTextView.append("Battery：" + bData.getBattery() + "\n");
				showTextView
				.append("----------------------------------" + "\n");
			}
		} else {
			showTextView.append("No measured record" + "\n");
		}



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_measure_data, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		isCurrentPage=false;
		overridePendingTransition(R.anim.dync_in_from_right,
				R.anim.dync_out_to_left);
	}

	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.backBtn: {
			onBackPressed();
		}
		break;
		}
	}

}
