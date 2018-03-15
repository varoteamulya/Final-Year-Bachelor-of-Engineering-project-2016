package com.hiwi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.opencv.javacv.facerecognition.R;
import com.hiwi.MultiSpinner.MultiSpinnerListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.lifesense.ble.LsBleManager;
import com.lifesense.ble.SearchCallback;
import com.lifesense.ble.bean.DeviceUserInfo;
import com.lifesense.ble.bean.LsDeviceInfo;
import com.lifesense.ble.bean.SexType;
import com.lifesense.ble.bean.UnitType;
import com.lifesense.ble.bean.VibrationVoice;
import com.lifesense.ble.bean.WeightUserInfo;
import com.lifesense.ble.commom.BroadcastType;
import com.lifesense.ble.commom.DeviceType;

@SuppressLint("NewApi")
public class AddDeviceActivity extends Activity {

	private String TAG = "AddDeviceActivity";
	private ProgressDialog scanProgressDialog;
	private ListView scanResultsListView;
	private ArrayList<BleDevice> mBleDeviceList;
	private LsBleManager mLsBleManager;
	private BleDeviceAdapter mBleDeviceAdapter;
	protected LsDeviceInfo mDevice;
	private Handler scanHandler = new Handler();
	private Handler changeHanlder;
	private boolean scanResult = false;

	private AlertDialog userListDialog;
	private ArrayList<BleDevice> tempList;
	private ProgressDialog ringProgressDialog;
	private List<DeviceUserInfo> deUserInfos;
	private BroadcastType broadcastType;
	private List<String> deviceTypeList;
	private StringBuffer scanDeviceBuffer;
	private final static Map<String, DeviceType> deviceMap;
	static {
		Map<String, DeviceType> map = new HashMap<String, DeviceType>();
		map.put("Sphygmometer", DeviceType.SPHYGMOMANOMETER);
		map.put("FatScale", DeviceType.FAT_SCALE);
		map.put("WeightScale", DeviceType.WEIGHT_SCALE);
		map.put("Pedometer", DeviceType.PEDOMETER);
		map.put("HeightRuler", DeviceType.HEIGHT_RULER);
		deviceMap = Collections.unmodifiableMap(map);
	}

	private SearchCallback mLsScanCallback = new SearchCallback() {
		// Scan Results
		@Override
		public void onSearchResults(final LsDeviceInfo lsDevice) {
			runOnUiThread(new Runnable() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					
					if (!deviceExists(lsDevice.getDeviceName())) {
						scanResult = true;
						scanProgressDialog.dismiss();
						BleDevice bleDevice = getBleDevice(
							lsDevice.getDeviceName(),
							lsDevice.getDeviceType(),
							lsDevice.getBroadcastID(),
							lsDevice.getPairStatus(),
							lsDevice.getProtocolType(),
							lsDevice.getModelNumber());
						tempList.add(bleDevice);
						mBleDeviceAdapter.add(bleDevice);
						mBleDeviceAdapter.notifyDataSetChanged();
//						startPairDevice(bleDevice);

					} else {
						//startPairDevice(bleDevice);
						updateListViewBackground(lsDevice.getDeviceName());
					}
				}
			});
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_device);
		mLsBleManager = LsBleManager.newInstance();
		scanResultsListView = (ListView) findViewById(R.id.scanResultsView);
		mBleDeviceList = new ArrayList<BleDevice>();
		deviceTypeList = new ArrayList<String>();
		changeHanlder = new Handler();
		scanDeviceBuffer = new StringBuffer();
		tempList = new ArrayList<BleDevice>();
		new HashMap<String, Integer>();
		initListView();
		mLsBleManager.stopDataReceiveService();

		// set user information when pairing device,no deviceID
		WeightUserInfo weightUserInfo = new WeightUserInfo();
		weightUserInfo.setAge(63);
		weightUserInfo.setAthleteActivityLevel(2);
		weightUserInfo.setHeight(1.35f);
		weightUserInfo.setSex(SexType.FEMALE);
		weightUserInfo.setAthlete(true);
		weightUserInfo.setProductUserNumber(2);
		weightUserInfo.setUnit(UnitType.UNIT_KG);
		weightUserInfo.setGoalWeight(75);
		weightUserInfo.setWaistline(89);
		mLsBleManager.setProductUserInfo(weightUserInfo);
		

		mBleDeviceAdapter.clear();
		tempList.clear();
		if (mLsBleManager != null && mLsBleManager.isOpenBluetooth()) {
			if (mLsBleManager.isSupportLowEnergy()) {

				// List<String> enableScanBroadcastName=new
				// ArrayList<String>();
				// enableScanBroadcastName.add("305A0");
				// mLsBleManager.setEnableScanBroadcastName(enableScanBroadcastName);
				
				// To start Scan -- Vijay
				if (!mLsBleManager.searchLsDevice(mLsScanCallback,
						getScanDeviceList(), getBroadcastType())) {
					showPromptDialog(
							"Warning",
							"Other tasks are running, please try again later",
							-1);
				} else {
					showScanDialog();
				}

			} else {
				showPromptDialog("Prompt",
						"Not support Bluetooth Low Energy", -1);
			}

		} else {
			showPromptDialog("Prompt",
					"Please turn on the Bluetooth function", -1);
		}

	}


//	public void doClick(View view) {
//		switch (view.getId()) {
//		case R.id.backBtn: {
//			onBackPressed();
//		}
//			break;
//		}
//	}

	private BroadcastType getBroadcastType() {
		// return broadcastType=BroadcastType.PAIR;
		if (broadcastType != null) {
			return broadcastType;
		} else
			return broadcastType = BroadcastType.ALL;
	}

//	private void setScanConitions() {
//		// get prompts.xml view
//		LayoutInflater li = LayoutInflater.from(AddDeviceActivity.this);
//		View promptsView = li.inflate(R.layout.activity_spinner_test, null);
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//				AddDeviceActivity.this);
//		// set prompts.xml to alertdialog builder
//		alertDialogBuilder.setView(promptsView);
//		alertDialogBuilder.setTitle("Setting Scanning Conditions");
//		Spinner broSpinner = (Spinner) promptsView
//				.findViewById(R.id.broadcastSpinner);
//		// Prepar adapter
//		// HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
//		final List<String> broStrings = new ArrayList<String>();
//		broStrings.add("All Broadcast");
//		broStrings.add("Normal Broadcast");
//		broStrings.add("Pair Broadcast");
//
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//				AddDeviceActivity.this, android.R.layout.simple_spinner_item,
//				broStrings);
//		adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
//		broSpinner.setAdapter(adapter);
//
//		broSpinner
//				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//					@Override
//					public void onItemSelected(AdapterView<?> parent,
//							View view, int position, long id) {
//
//						if (broStrings.get(position).equals("All Broadcast"))
//							broadcastType = BroadcastType.ALL;
//						if (broStrings.get(position).equals("Normal Broadcast"))
//							broadcastType = BroadcastType.NORMAL;
//						if (broStrings.get(position).equals("Pair Broadcast"))
//							broadcastType = BroadcastType.PAIR;
//					}
//
//					@Override
//					public void onNothingSelected(AdapterView<?> parent) {
//					}
//				});
//		final List<String> test = new ArrayList<String>();
//		test.add("Sphygmometer");
//		test.add("FatScale");
//		test.add("WeightScale");
//		test.add("Pedometer");
//		test.add("HeightRuler");
//
//		MultiSpinner multiSpinner = (MultiSpinner) promptsView
//				.findViewById(R.id.deviceTypeSpinner);
//		multiSpinner.setItems(test, "Please select",
//				new MultiSpinnerListener() {
//
//					@Override
//					public void onItemsSelected(boolean[] selected,
//							List<String> chooseList) {
//						deviceTypeList = chooseList;
//
//					}
//				});
//
//		// set dialog message
//		alertDialogBuilder
//				.setCancelable(false)
//				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int id) {
//
//					}
//				})
//				.setNegativeButton("Cancel",
//						new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int id) {
//								dialog.cancel();
//							}
//						});
//
//		// create alert dialog
//		AlertDialog alertDialog = alertDialogBuilder.create();
//		// show it
//		alertDialog.show();
//	}

	private void initListView() {
		mBleDeviceAdapter = new BleDeviceAdapter(this.getApplicationContext(),
				mBleDeviceList);
		scanResultsListView.setAdapter(mBleDeviceAdapter);
		scanResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent,
							final View view, int position, long id) {

						if (mLsBleManager != null) {
							mLsBleManager.stopSearch();
						}
						// showProgressDialog("Pair Device","Pairing,please wait......");
						BleDevice de = (BleDevice) parent.getAdapter().getItem(
								position);
						startPairDevice(de);

					}
				});
	}

	private List<DeviceType> getScanDeviceList() {
		scanDeviceBuffer.setLength(0);
		List<DeviceType> typeList = new ArrayList<DeviceType>();
		if (deviceTypeList.size() == 0) {
			typeList.add(DeviceType.SPHYGMOMANOMETER);
			typeList.add(DeviceType.FAT_SCALE);
			typeList.add(DeviceType.WEIGHT_SCALE);
			typeList.add(DeviceType.HEIGHT_RULER);
			typeList.add(DeviceType.PEDOMETER);
			scanDeviceBuffer.append("scan all device type");
		} else {
			for (String type : deviceTypeList) {
				typeList.add(deviceMap.get(type));
				scanDeviceBuffer.append(type);
				scanDeviceBuffer.append("  ");
			}
		}
		return typeList;

	}

	private VibrationVoice readVibrationVoiceSettingInfo() {
		SharedPreferences mPrefs = getSharedPreferences(
				getApplicationInfo().name, Context.MODE_PRIVATE);
		String vibrationVoiceInfoJson = mPrefs.getString(
				VibrationVoice.class.getName(), null);
		Gson gson = new Gson();
		return (VibrationVoice) gson.fromJson(vibrationVoiceInfoJson, VibrationVoice.class);

	}

	private void startPairDevice(BleDevice de) {
		// set vibration voice setting info
		VibrationVoice viVoiceInfo = readVibrationVoiceSettingInfo();
		if (viVoiceInfo != null) {
			mLsBleManager.setVibrationVoice(viVoiceInfo);
		}
		System.out.println(de.getName());
		LsDeviceInfo lsDevice = new LsDeviceInfo();
		lsDevice.setDeviceName(de.getName());
		lsDevice.setBroadcastID(de.getBroadcastId());
		lsDevice.setDeviceType(de.getDeviceType());
		lsDevice.setProtocolType(de.getProtocolType());
		lsDevice.setModelNumber(de.getModelNumber());

		Intent intent = new Intent(AddDeviceActivity.this,
				TestMainActivity.class);
		intent.putExtra("object", lsDevice);
		startActivity(intent);

		/*
		 * mLsBleManager.startPairing(lsDevice,new PairCallback() { //
		 * public void onDiscoverUserInfo(final List
		 * userList) { runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() { deUserInfos=userList;
		 * ringProgressDialog.dismiss(); showDeviceUserInfo(userList); } }); }
		 * 
		 * public void onPairResults(final LsDeviceInfo lsDevice,final int
		 * status) { if(status!=0) { runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() { mBleDeviceAdapter.clear();
		 * tempList.clear(); ringProgressDialog.dismiss();
		 * showPromptDialog("Prompt",
		 * "Failed to paired device,please try again",-1); } }); } else
		 * if(status==0) { runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() { ringProgressDialog.dismiss(); Intent
		 * resultsIntent=new Intent(
		 * AddDeviceActivity.this,PairResultsActivity.class);
		 * resultsIntent.putExtra("LsDeviceInfo",lsDevice);
		 * startActivity(resultsIntent); } }); } }
		 * 
		 * 
		 * });
		 */

	}

	private void showProgressDialog(String title, String message) {
		ringProgressDialog = ProgressDialog.show(AddDeviceActivity.this, title,
				message, true);
		ringProgressDialog.setCancelable(true);
	}

	@SuppressWarnings("null")
	private void showDeviceUserInfo(List<DeviceUserInfo> userList) {
		// Strings to Show In Dialog with Radio Buttons
		final CharSequence[] items;
		if (userList != null && userList.size() > 0) {
			items = new String[userList.size()];
			for (DeviceUserInfo user : userList) {
				if (user != null) {
					user.getDeviceId();
					int index = userList.indexOf(user);
					if (user.getUserName().isEmpty()) {
						items[index] = user.getUserNumber() + "   " + "unknow";
					} else
						items[index] = user.getUserNumber() + "   "
								+ user.getUserName();
				}
			}
		} else
			items = new String[2];
		// Creating and Building the Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select User Number");
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 
						userListDialog.dismiss();
						showInputDialog(which + 1);

					}

				});
		userListDialog = builder.create();
		userListDialog.show();
	}

	private void showInputDialog(final int userNumber) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter your user name(Less than 16 characters)");
		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input
		// as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);
		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				writeUserAccountToDevice(userNumber, input.getText().toString());

			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						showDeviceUserInfo(deUserInfos);

					}
				});
		builder.create();
		builder.show();
	}

	private void writeUserAccountToDevice(int userNumber, String name) {
		mLsBleManager.bindDeviceUser(userNumber, name.trim());
		showProgressDialog("Binding Device User",
				"Binding,please wait...........");

	}

	private void setListViewBackgroundColor(final View view) {
		view.setBackgroundColor(Color.GREEN);
		changeHanlder.postDelayed(new Runnable() {
			@Override
			public void run() {
				view.setBackgroundColor(Color.WHITE);
			}
		}, 1000);
	}

	@SuppressWarnings("unchecked")
	private void updateListViewBackground(String name) {
		scanResult = true;
		if (!tempList.isEmpty()) {
			for (BleDevice dev : tempList) {
				if (dev != null && dev.getName() != null
						&& dev.getName().equals(name)) {
					final View view = scanResultsListView
							.getChildAt(mBleDeviceAdapter.getPosition(dev));
					if (view != null) {
						setListViewBackgroundColor(view);
					}
				}
			}
		}
		scanResultsListView.performItemClick(scanResultsListView.getChildAt(0), 0, scanResultsListView.getItemIdAtPosition(0));
	}

	private BleDevice getBleDevice(String name, String deviceType,
			String broCastId, int pairStatus, String protocolType,
			String modelNumber) {
		BleDevice device = new BleDevice(name, deviceType, broCastId,
				pairStatus, protocolType, modelNumber);
		return device;
	}

	private boolean deviceExists(String name) {
		if (name == null || name.length() == 0) {
			return false;
		}
		if (tempList != null && tempList.size() > 0) {
			for (int i = 0; i < tempList.size(); i++) {
				if (tempList.get(i) != null
						&& tempList.get(i).getName() != null
						&& tempList.get(i).getName().equals(name)) {
					return true;
				}
			}
			return false;
		} else
			return false;

	}

	private void showPromptDialog(String title, String message,
			final int userNumber) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				AddDeviceActivity.this).setTitle(title)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (userNumber != -1) {
							showInputDialog(userNumber);
						}
					}

				}).setMessage(message);
		builder.create().show();
	}

	private void showScanDialog() {
		scanProgressDialog = ProgressDialog.show(AddDeviceActivity.this,
				"BroadcastType:" + getBroadcastType(broadcastType), "Scanning:"
						+ scanDeviceBuffer + "............", true);
		scanProgressDialog.setCancelable(true);
		scanHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				scanProgressDialog.dismiss();
				if (!scanResult) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							AddDeviceActivity.this)
							.setTitle("Prompt")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											mLsBleManager.stopSearch();

										}

									})
							.setMessage(
									"No results!Please change scanning conditions and retry");

					builder.create().show();
				}
			}
		}, 15000);
		scanResult = false;

	}

	private String getBroadcastType(BroadcastType broType) {
		if (broType != null) {
			if (broType.equals(BroadcastType.ALL))
				return "All Broadcast";
			if (broType.equals(BroadcastType.PAIR))
				return "Pair Broadcast";
			if (broType.equals(BroadcastType.NORMAL))
				return "Normal Broadcast";
			else
				return broType.toString();
		} else
			return "All Broadcast";

	}
	/*
	 * private void checkUserNumber(int userNumber) {
	 * 
	 * if(!MainActivity.deviceUserNumberMap.isEmpty() &&
	 * MainActivity.deviceUserNumberMap.get(deviceId)!=null
	 * &&MainActivity.deviceUserNumberMap.get(deviceId)!=userNumber) {
	 * userListDialog.dismiss(); showPromptDialog("�??示", "是�?�更新用户编�?�?",userNumber);
	 * } else { int number=-1; if(!MainActivity.deviceUserNumberMap.isEmpty()&&
	 * MainActivity.deviceUserNumberMap.get(deviceId)!=null) {
	 * number=MainActivity.deviceUserNumberMap.get(deviceId);
	 * System.out.println("数�?�库当�?绑定的用户编�?�："+number);
	 * System.out.println("选择的用户编�?�："+userNumber); userListDialog.dismiss();
	 * showInputDialog(userNumber); } else { userListDialog.dismiss();
	 * showInputDialog(userNumber); } }
	 * 
	 * }
	 */
}
