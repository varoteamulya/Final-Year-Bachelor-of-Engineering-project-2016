package com.hiwi;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lifesense.ble.bean.LsDeviceInfo;
import org.opencv.javacv.facerecognition.R;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LsDeviceAdapter extends ArrayAdapter {

	private Context mContext;
	private List<DeviceInfoWithDate> modelsList;

	public LsDeviceAdapter(Context context, List<DeviceInfoWithDate> deviceList) {
		super(context, R.layout.device_list_item, deviceList);
		this.mContext = context;
		this.modelsList = deviceList;
	}

	public List<DeviceInfoWithDate> getModelsList() {
		return modelsList;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater
		View rowView = null;
		if (modelsList.size() != 0) {
			rowView = inflater
					.inflate(R.layout.device_list_item, parent, false);
			TextView nameView = (TextView) rowView
					.findViewById(R.id.d_NameTextView);
			TextView addressView = (TextView) rowView
					.findViewById(R.id.d_AddressTextView);
			TextView recordView = (TextView) rowView
					.findViewById(R.id.d_RecordTextView);
			TextView dateView = (TextView) rowView
					.findViewById(R.id.d_DateTextView);
			LsDeviceInfo lsDavice = modelsList.get(position).getLsDeviceInfo();
			// String sensorName=;

			nameView.setText("Name: " + lsDavice.getDeviceName() + ",NO:"
					+ lsDavice.getDeviceUserNumber());
			addressView.setText("Protocol Type: " + lsDavice.getProtocolType());

			dateView.setText("Date: " + modelsList.get(position).getDate());

			recordView.setText("No New Record");
		}

		// 5. retrn rowView
		return rowView;
	}

	public void removeView(int position) {

		if (modelsList.size() != 0) {

			modelsList.remove(position);

		}

	}

}
