package com.hiwi;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.opencv.javacv.facerecognition.R;

@SuppressWarnings("rawtypes")
public class BleDeviceAdapter extends ArrayAdapter {

	private ArrayList<BleDevice> modelsArrayList;
	private Context context;

	@SuppressWarnings("unchecked")
	public BleDeviceAdapter(Context context,
			ArrayList<BleDevice> modelsArrayList) {

		super(context, R.layout.scan_list_item, modelsArrayList);

		this.context = context;
		this.modelsArrayList = modelsArrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater
		View rowView = null;
		if (modelsArrayList.size() != 0) {
			rowView = inflater.inflate(R.layout.scan_list_item, parent, false);
			TextView nameView = (TextView) rowView
					.findViewById(R.id.s_NameTextView);
			TextView addressView = (TextView) rowView
					.findViewById(R.id.s_AddressTextView);
			TextView pairView = (TextView) rowView
					.findViewById(R.id.s_PairTextView);

			String sensorName = modelsArrayList.get(position).getName();

			nameView.setText("Name: " + sensorName);
			addressView.setText("Protocol Type: "
					+ modelsArrayList.get(position).getProtocolType());

			if (modelsArrayList.get(position).getPairStatus() == 1) {
				pairView.setText("PairingStatus");
				pairView.setBackgroundColor(Color.RED);
			} else
				pairView.setText("NormalStatus");
		}

		// 5. retrn rowView
		return rowView;
	}

}
