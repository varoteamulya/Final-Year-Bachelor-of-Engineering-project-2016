package com.hiwi ;


import java.util.Arrays;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

import zephyr.android.HxMBT.*;

public class NewConnectedListener extends ConnectListenerImpl
{
	private Handler _OldHandler;
	private Handler _aNewHandler; 
	private int GP_MSG_ID = 0x20;
	private int GP_HANDLER_ID = 0x20;
	private int HR_SPD_DIST_PACKET =0x26;
	
	private final int HEART_RATE = 0x100;
	private final int INSTANT_SPEED = 0x101;
	private final int DISTANCE = 0x102;
	private final int STRIDES = 0x103;
	private final int BATTERY = 0x104;
	private final int RR_HEART_BEATS = 0x105;
	
	private HRSpeedDistPacketInfo HRSpeedDistPacket = new HRSpeedDistPacketInfo();
	
	public NewConnectedListener(Handler handler,Handler _NewHandler) {
		super(handler, null);
		_OldHandler= handler;
		_aNewHandler = _NewHandler;

		// TODO Auto-generated constructor stub

	}
	@JavascriptInterface
	public void Connected(ConnectedEvent<BTClient> eventArgs) {
		System.out.println(String.format("Connected to BioHarness %s.", eventArgs.getSource().getDevice().getName()));

	
		
		
		
		
		//Creates a new ZephyrProtocol object and passes it the BTComms object
		ZephyrProtocol _protocol = new ZephyrProtocol(eventArgs.getSource().getComms());
		//ZephyrProtocol _protocol = new ZephyrProtocol(eventArgs.getSource().getComms(), );
		_protocol.addZephyrPacketEventListener(new ZephyrPacketListener() {
			public void ReceivedPacket(ZephyrPacketEvent eventArgs) {
				ZephyrPacketArgs msg = eventArgs.getPacket();
				byte CRCFailStatus;
				byte RcvdBytes;
				
				
				
				CRCFailStatus = msg.getCRCStatus();
				RcvdBytes = msg.getNumRvcdBytes() ;
				if (HR_SPD_DIST_PACKET==msg.getMsgID())
				{
					
					
					byte [] DataArray = msg.getBytes();
					
					//***************Displaying the Heart Rate********************************
					int HRate =  HRSpeedDistPacket.GetHeartRate(DataArray);
					Message text1 = _aNewHandler.obtainMessage(HEART_RATE);
					Bundle b1 = new Bundle();
					b1.putString("HeartRate", String.valueOf(HRate));
					text1.setData(b1);
					_aNewHandler.sendMessage(text1);
					System.out.println("Heart Rate is "+ HRate);

					//***************Displaying the Instant Speed********************************
					double InstantSpeed = HRSpeedDistPacket.GetInstantSpeed(DataArray);
					
					text1 = _aNewHandler.obtainMessage(INSTANT_SPEED);
					b1.putString("InstantSpeed", String.valueOf(InstantSpeed));
					text1.setData(b1);
					_aNewHandler.sendMessage(text1);
					System.out.println("Instant Speed is "+ InstantSpeed);
					
					//***************Displaying the Distance********************************
					double dist = HRSpeedDistPacket.GetDistance(DataArray);
					
					text1 = _aNewHandler.obtainMessage(DISTANCE);
					b1.putString("dist", String.valueOf(dist));
					text1.setData(b1);
					_aNewHandler.sendMessage(text1);
					System.out.println("distance is "+ dist);
					
					//***************Displaying strides********************************
					double strid = HRSpeedDistPacket.GetStrides(DataArray);
					
					text1 = _aNewHandler.obtainMessage(STRIDES);
					b1.putString("strid", String.valueOf(strid));
					text1.setData(b1);
					_aNewHandler.sendMessage(text1);
					
					//***************Displaying battery********************************
					double bat = HRSpeedDistPacket.GetBatteryChargeInd(DataArray);
					text1 = _aNewHandler.obtainMessage(BATTERY);
					b1.putString("bat", String.valueOf(bat));
					text1.setData(b1);
					_aNewHandler.sendMessage(text1);
					
					//***************Displaying R-R interval********************************
					int rr[] = HRSpeedDistPacket.GetHeartBeatTS(DataArray);
					String r=Arrays.toString(rr);
					text1 = _aNewHandler.obtainMessage(RR_HEART_BEATS);
					b1.putString("rrinterval", r);
					text1.setData(b1);
					_aNewHandler.sendMessage(text1);
					
					
				}
			}
		});
	}
	
}