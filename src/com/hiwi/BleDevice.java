package com.hiwi;

import android.os.Parcel;
import android.os.Parcelable;

public class BleDevice implements Parcelable {
	public String name;
	public String address;
	public String pairFlags;
	private int key_id;
	private int rssi;
	private String scanRecord;
	private String deviceModel;
	private String deviceType;
	private String broadcastId;
	private int pairStatus;
	private String protocolType;
	private String modelNumber;

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public String getPairFlags() {
		return pairFlags;
	}

	public void setPairFlags(String pairFlags) {
		this.pairFlags = pairFlags;
	}

	public BleDevice(String mname, String deType, String broCastId, int status,
			String proType, String model) {
		name = mname;
		setPairStatus(status);
		deviceType = deType;
		broadcastId = broCastId;
		protocolType = proType;
		modelNumber = model;
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized String getAddress() {
		return address;
	}

	public synchronized void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeString(address);
		out.writeString(pairFlags);
		out.writeInt(key_id);
		out.writeInt(rssi);
		out.writeString(scanRecord);
		out.writeString(deviceModel);
		out.writeString(deviceType);
		out.writeString(broadcastId);
		out.writeInt(pairStatus);
		out.writeString(protocolType);
		out.writeString(modelNumber);
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<BleDevice> CREATOR = new Parcelable.Creator<BleDevice>() {
		@Override
		public BleDevice createFromParcel(Parcel in) {
			return new BleDevice(in);
		}

		@Override
		public BleDevice[] newArray(int size) {
			return new BleDevice[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated
	// with it's values
	private BleDevice(Parcel in) {
		name = in.readString();
		address = in.readString();
		pairFlags = in.readString();
		key_id = in.readInt();
		rssi = in.readInt();
		scanRecord = in.readString();
		deviceModel = in.readString();
		deviceType = in.readString();
		broadcastId = in.readString();
		pairStatus = in.readInt();
		protocolType = in.readString();
		modelNumber = in.readString();
	}

	public int getKey_id() {
		return key_id;
	}

	public void setKey_id(int key_id) {
		this.key_id = key_id;
	}

	public String getScanRecord() {
		return scanRecord;
	}

	public void setScanRecord(String scanRecord) {
		this.scanRecord = scanRecord;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getBroadcastId() {
		return broadcastId;
	}

	public void setBroadcastId(String broadcastId) {
		this.broadcastId = broadcastId;
	}

	public int getPairStatus() {
		return pairStatus;
	}

	public void setPairStatus(int pairStatus) {
		this.pairStatus = pairStatus;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

}
