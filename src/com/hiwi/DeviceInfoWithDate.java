package com.hiwi;

import android.os.Parcel;
import android.os.Parcelable;

import com.lifesense.ble.bean.LsDeviceInfo;

public class DeviceInfoWithDate implements Parcelable {

	private LsDeviceInfo lsDeviceInfo;

	private String date;

	public DeviceInfoWithDate() {

	}

	public DeviceInfoWithDate(Parcel source) {

		lsDeviceInfo = (LsDeviceInfo) source.readValue(LsDeviceInfo.class
				.getClassLoader());
		date = source.readString();

	}

	public static final Parcelable.Creator<DeviceInfoWithDate> CREATOR = new Parcelable.Creator<DeviceInfoWithDate>() {

		@Override
		public DeviceInfoWithDate createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new DeviceInfoWithDate(source);
		}

		@Override
		public DeviceInfoWithDate[] newArray(int size) {
			// TODO Auto-generated method stub
			return new DeviceInfoWithDate[size];
		}
	};

	public LsDeviceInfo getLsDeviceInfo() {
		return lsDeviceInfo;
	}

	public void setLsDeviceInfo(LsDeviceInfo lsDeviceInfo) {
		this.lsDeviceInfo = lsDeviceInfo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeValue(lsDeviceInfo);
		dest.writeString(date);

	}

}
