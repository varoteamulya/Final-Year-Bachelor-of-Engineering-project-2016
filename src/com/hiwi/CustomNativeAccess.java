package com.hiwi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Set;

import android.content.IntentFilter;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.DataOutputStream;				// =================== Added PU 17/6
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.cordova.DroidGap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.MSG.Common;
import com.elecfreaks.ble.BluetoothHandler;
import com.taidoc.pclinklibrary.connection.AndroidBluetoothConnection;
import com.taidoc.pclinklibrary.exceptions.CommunicationTimeoutException;

import zephyr.android.HxMBT.BTClient;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;


public class CustomNativeAccess {

	String bpNew = "Not Connected";
	String bpNewGluco = "Not Connected";
	String bpNewCholest = "Not Connected";
	String bpNewUric = "Not Connected";
	String Name,Phone;
	String bpNew1 = "Not Connected";
	int result =  1;
	int nRecv11 = 0;
	int nRecv12 = 0;
	int result2 = 2;
	public static int modeNew = 10;
	public static int LastDevice = 10;
	int modeNew1 = 0;
	int modeNewGluco = 0;
	int modeNewDyn = 10;
	int rxCount = 0;
	int intres = 0;
	
	// PU Code End Added 14/06/14

//================================================================ 23/06
	
	public static String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	//static final UUID UUID_RFCOMM_GENERIC = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	String resNew = "NotConnectedNC";											// Added on 20/6 PU
	String resNew1 = "NotConnectedNC";
	String resNewoxy = "NotConnectedNC";										// Added PU 10 Sept'14
	String resNewscale = "NotConnectedNC";										// Added PU 10 Sept'14
	private InputStream mmInStream;
	private OutputStream mmOutStream;
	
	BluetoothAdapter btAdapt = null;
	BluetoothSocket btSocket;

	Boolean bConnect = false;
	String strName = null;
	String MyDevice = null;
	String strAddress = null;
	int nNeed = 0;
//===========================================PU 17/6
	public static final String HEX_DIGITS = "0123456789ABCDEF";
	byte[] arrayOfByte = {0,0,0,0,0,0,0,0};
	byte[] bufRecv1 = {0,0,0,0,0,0,0,0};
	String res = "No result";
	int nRecv1 = 0;
	int temprinit =0;
	int rxTimes = 0;
	int txTime = 0;    //Change to 1 for Oxymeter
//===========================================PU 17/6	
	byte[] bRecv = new byte[4096];						// Changed from 1024 to 4096 17/7
	int nRecved = 0;
	int decimalTest = 0;
	int decimalTest1 = 0;
	  int sysValuenew = 0;
	  int diaValuenew = 0;
	  int pulseValuenew = 0;
	
	
//================================================================ 23/06	

  int initheart=0;
  int count=0;
  //String heart="000";
  //String dist="000";
  //String speed="000";
  //String strid="010";
  //String bat="011";
  //String rr="012";
  //String bp="0";
  //String bs="0";


  BluetoothAdapter adapter;
  BTClient _bt;
  //ZephyrProtocol _protocol;
  NewConnectedListener _NConnListener;
  //private final int HEART_RATE = 0x100;
  //private final int INSTANT_SPEED = 0x101;
  //private final int DISTANCE = 0x102;
  //private final int STRIDES = 0x103;
 // private final int BATTERY = 0x104;
  //private final int RR_HEART_BEATS = 0x105;
  WebView mAppView;
    private DroidGap mGap;
    public CustomNativeAccess(DroidGap gap, WebView view)
    {
        mAppView = view;
        mGap = gap;
    }
    
    public CustomNativeAccess()
    {
        
    }
    
    @JavascriptInterface
    public String callBP(){
    	Intent enableIntent = new Intent(HeartbeatsActivity.context,MainActivity1.class);
    	mGap.getActivity().startActivityForResult(enableIntent, 3);
    	//HeartbeatsActivity.context.startActivity(enableIntent);
    	enableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return "asdad";  

    }
    
    @JavascriptInterface
    public String startBLEScan(){
    	Intent enableIntent = new Intent(HeartbeatsActivity.context,com.elecfreaks.bleexample.MainActivity.class);
    	mGap.getActivity().startActivityForResult(enableIntent, 3);
    	//HeartbeatsActivity.context.startActivity(enableIntent);
    	enableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return "asdad";  

    }
    
    @JavascriptInterface
    public String startBP(){
    	Intent enableIntent = new Intent(HeartbeatsActivity.context,com.hiwi.MainActivity1.class);
    	mGap.getActivity().startActivityForResult(enableIntent, 3);
    	//HeartbeatsActivity.context.startActivity(enableIntent);
    	enableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return "asdad";  

    }
    
    
    @JavascriptInterface
    public String callSPO2(){
    	Intent enableIntent = new Intent(HeartbeatsActivity.context, com.elecfreaks.bleexample.MainActivity.class);
    	mGap.getActivity().startActivityForResult(enableIntent, 3);
    	//HeartbeatsActivity.context.startActivity(enableIntent);
    	enableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return "asdad"; 
    }

    @JavascriptInterface
    public String getNo(){

        return "2bv10cs008";
    }
	
    @JavascriptInterface
    public String senddata()
    {
		 System.out.println("fdd");
		//14:dd:a9:a8:f3:e1
		//14dda9a8f3e1
		String bodyy=HeartbeatsActivity.macAddr;
    	String Subject=HeartbeatsActivity.macAddr;
		SendMail sendma=new SendMail(bodyy,bodyy,1);
    	ReadMailSample sample = new ReadMailSample();
    	
		try {
			//sample.execute();
			while(ReadMailSample.start);
			Intent enableIntent = new Intent(HeartbeatsActivity.context, com.hiwi.ReciveMail.class);
	    	mGap.getActivity().startActivityForResult(enableIntent, 3);
	    	//HeartbeatsActivity.context.startActivity(enableIntent);
	    	enableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        
			 System.out.println("fdd");
			 return "asdad"; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "drag";
    }
	 
	 
	 @JavascriptInterface
   public String senddatalogin()
   {
		//Toast.makeText(HeartbeatsActivity.context, "Vijay the great", Toast.LENGTH_SHORT).show();
		String Subject=HeartbeatsActivity.macAddr;
		String bodyy="Username:"+HeartbeatsActivity.macAddr+"gdv.com.au:password:"+HeartbeatsActivity.macAddr;
		SendMail sendma=new SendMail(Subject,Subject,0);
		//Toast.makeText(HeartbeatsActivity.context, "11", Toast.LENGTH_SHORT).show();
		
		ReadMailSample sample = new ReadMailSample();
		
	    try {
			//sample.execute();
			while(ReadMailSample.start);
			Intent enableIntent = new Intent(HeartbeatsActivity.context, com.hiwi.ReciveMail.class);
	    	mGap.getActivity().startActivityForResult(enableIntent, 3);
	    	//HeartbeatsActivity.context.startActivity(enableIntent);
	    	enableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        
			 System.out.println("fdd");
			 return "asdad"; 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//sample.execute();
		return "drag";
   }
	 
	 
	 @JavascriptInterface
	 public String checkdata()
	 {
		 if(ReadMailSample.loogin==true)
		 {
			 System.out.println("checking in checkdata");
			 return "drag";
		 }
		 else
		 {
			 return "1"; 
		 }		 
	 }
      
      public String Timestamp()
  	{
  		Date date= new Date();
  		long time = date.getTime();
  		Timestamp ts = new Timestamp(time);
  		String[] var=ts.toString().split(" ");
  		String[] da=var[0].split("-");
  		String[] ti=var[1].split(":");
  		String[] ti2=ti[2].split("\\.");
  		String time2=da[0]+da[1]+da[2]+ti[0]+ti[1]+ti2[0];
  		return time2;
  	}
      
      public String Findmac()
      {
    	  String mac=HeartbeatsActivity.macAddr;
    	  return mac;
      }


    @JavascriptInterface
    public String disconnect(){

      if(_NConnListener!=null){
      _bt.removeConnectedEventListener(_NConnListener);
    /*Close the communication with the device & throw an exception if failure*/
    _bt.Close();
      }
    return "Disconnected";
    }


    @JavascriptInterface
    public void exitapp(){
       mGap.finish();

    }


    @JavascriptInterface
    public String getTest(){
      return("tested");

   }



    @JavascriptInterface
   public void bluetoothconnectioncheck(){
      Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        mGap.getActivity().startActivityForResult(enableIntent, 3);
  //   return "bluetooth check";

     }


    protected void connectdevice(final String macAddress,final AndroidBluetoothConnection connection,final BluetoothAdapter adapter) {

      BluetoothDevice device = adapter.getRemoteDevice(macAddress);
        // Attempt to connect to the device, Type I

        connection.connect(device);
        long startConnectTime = System.currentTimeMillis();

       while (connection.getState() != AndroidBluetoothConnection.STATE_CONNECTED)
        {
          long conntectTime = System.currentTimeMillis();

        if ((conntectTime - startConnectTime) > AndroidBluetoothConnection.BT_CONNECT_TIMEOUT)
      { // throw a CommunicationTimeoutException and break the loop.

          throw new CommunicationTimeoutException();
       //break;
       } /// end of if /
        }/// end of while /

    }
    
//New Bluetooth Thermometer Added by START Pramod 08/07/15

    @JavascriptInterface
    public String readTemp()
    {
      resNew = "Not ConnectedNC";   
      decimalTest1 = 0;       //    Added by PU 10 Sept'14
      result = 1;
      modeNew = 10;
      modeNewDyn = 10;      // Added PU 27/1/15
      bufRecv1[6] = 0;
      nRecved = 0;      //    Added by PU 9 Sept'14
      txTime = 0;
	  temprinit = 0;
      nRecv1=0;
      rxCount = 0;
		String Name="Pramod 7 S8 BT";
    	String Phone="+6112345678912";
  	String time=Timestamp();
  	String mac=Findmac();
  	String type="Medium";
  	String bodyy=time+","+mac+","+type+","+"Joe Black TEMPERATURE:"+bpNew+" OK,Future use,Future use,Future use";
  	//String bodyy=time+","+mac+","+type+","+"Joe Black WEIGHT:"+var[0]+"."+var[1]" OK,Future use,Future use,Future use";
  	String Subject="TEMPERATURE Mode1,"+Phone+","+Name;
	SendMail sendma=new SendMail(Subject,bodyy,0);
	LastDevice = 13;
      return bpNew;
    }

  @JavascriptInterface
    public String connectTempnew() throws InterruptedException
    {
        
    Log.e(Common.TAG, "Loops to CNA Thermometer: " + decimalTest1);
    
    decimalTest1 = decimalTest1+1;
      
    
        int counter = 0;
        
        if (modeNew == 10) { 
        
          //Log.d(Common.TAG, "modeNew: " + modeNew);
          
        while(modeNew != 11 ) // pause until modeNew becomes 11 _ UPDATED AKASH
        {
          
         // Log.d(Common.TAG, "modeNew: " + modeNew);
          counter++;
          try{
            Thread.sleep(10);
          }catch(Exception E){}
          if(counter > 100) return "Not Connected";
        }
      
  		try{
  			Thread.sleep(1500);
  		}catch(Exception E){} 
        }
  		try{
  			Thread.sleep(10);
  		}catch(Exception E){}
         // --------------------------------------By PU 11 Sept'14  
      
      
        if (temprinit == 0)
        {
          
          
        //Log.d(Common.TAG, "RX Data 1: " + modeNew);


        // ======================================================= TX Routine
        
        //Log.d(Common.TAG, "Number of RX Data: " + String.valueOf(nRecved));
        

        
      //  if (bufRecv1[0] == 0x25) {
        
         if (txTime < 1)     
            {
            try {
               Log.e(Common.TAG, "TX CMD Start: " + txTime);
              Thread.sleep(50);

              char[] test = { 0xfe, 0xfd, 0xaa, 0xa0, 0x0d, 0x0a };

              for(int k=0; k < test.length; k++){
            	  new DataOutputStream(btSocket.getOutputStream()).writeByte(test[k]);
              	}
              
              
              //Thread.sleep(50);				//changed from 550 PU 09Jul'15
      
              
              txTime = txTime + 1;
              //Log.e(Common.TAG, "TX CMD Sent: " + txTime);

             
              Thread.sleep(50);			//changed from 550 PU 09Jul'15

            } 
            
            
            catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            } catch (InterruptedException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            }

        }
         
   		try{
  			Thread.sleep(40);
  		}catch(Exception E){}
          
          bConnect = true;
          new Thread(new Runnable() {
            public void run() {
              byte[] bufRecv = new byte[1024];    
              int nRecv = 0;
              while (bConnect) {
                try {
                  Log.e(Common.TAG, "Start Recv" + String.valueOf(mmInStream.available()));
                  nRecv = mmInStream.read(bufRecv);
                  if (nRecv < 1) {
                    //Log.e(Common.TAG, "Recving Short");
                    Thread.sleep(100);          
                    continue;
                    
                  }
                  
                  System.arraycopy(bufRecv, 0, bRecv, nRecved, nRecv);  
                  
                      
                  //bufRecv1[0] = (byte) (bRecv[0] - 0x80);
                  //Log.d(Common.TAG, "RX Data 1: " + bufRecv1[0]);
                  rxTimes = rxTimes + 1;
                    
                          
                  //Log.e(Common.TAG, "Recv:" + String.valueOf(nRecv));

                  nRecved += nRecv;
                  //Log.d(Common.TAG, "RX Data 1: " + bufRecv1[nRecv]);
                  
                  if(nRecved < nNeed)
                  {
                    Thread.sleep(100);          
                    continue;
                  }
          
          
                    } catch (Exception e) {
                  Log.e(Common.TAG, "Recv thread:" + e.getMessage());
                        
                  break;
                }
              }
              Log.e(Common.TAG, "Exit while");
            }
          }).start();
           
        
    //======================================================================= Code Rx Data
  		int counter1=0;
  		while (nRecved < 8)
		{
			//Log.e(Common.TAG, "Number of nRecved: " + String.valueOf(nRecved));
			counter1++;
			try{
				Thread.sleep(100);
			}catch(Exception E){}
			if(counter1 > 02) 
			{
			  	txTime = 0;
			  	nRecved = 0;
			  	nRecv1 = 0;
				resNew = "0";
				resNew1 = "Not Connected";
				return resNew1;
			}
		}
          
       //Log.d(Common.TAG, "Number of RX Data: " + String.valueOf(nRecved));   
          
       if ((nRecved > 7) || (temprinit == 1))
       {               
    //    bufRecv1[8] = (byte) (bRecv[nRecved-8] & 0x7f);
        bufRecv1[7] = (byte) (bRecv[nRecved-7] & 0x7f);
        bufRecv1[6] = (byte) (bRecv[nRecved-6] & 0x7f);
        bufRecv1[5] = (byte) (bRecv[nRecved-5] & 0x7f);
        bufRecv1[4] = (byte) (bRecv[nRecved-4] & 0x7f);
        bufRecv1[3] = (byte) (bRecv[nRecved-3] & 0x7f);
        bufRecv1[2] = (byte) (bRecv[nRecved-2] & 0x7f);
        bufRecv1[1] = (byte) (bRecv[nRecved-1] & 0x7f);
    //  Log.d(Common.TAG, "Result Data int Buf 8: " + String.valueOf(bufRecv1[8]));
        //Log.d(Common.TAG, "Result Data int Buf 7: " + String.valueOf(bufRecv1[7]));
        //Log.d(Common.TAG, "Result Data int Buf 6: " + String.valueOf(bufRecv1[6]));
        //Log.d(Common.TAG, "Result Data int Buf 5: " + String.valueOf(bufRecv1[5]));
        //Log.d(Common.TAG, "Result Data int Buf 4: " + String.valueOf(bufRecv1[4]));
        //Log.d(Common.TAG, "Result Data int Buf 3: " + String.valueOf(bufRecv1[3]));
        //Log.d(Common.TAG, "Result Data int Buf 2: " + String.valueOf(bufRecv1[2]));
        //Log.d(Common.TAG, "Result Data int Buf 1: " + String.valueOf(bufRecv1[1]));
        rxCount = 9;
        temprinit = 1;
	   //}
		
        //ReciveMail.speakOut3();

        if (bufRecv1[5] < 0x02) 

		{
        sysValuenew = bRecv[nRecved-4];
        diaValuenew = bRecv[nRecved-3];

//        String sysValuenewTest = (bRecv[nRecved-4] & 0x7f) + "";
        
            if (bRecv[nRecved-4] < 0) {
        
              decimalTest = (bRecv[nRecved-4]& 0x7f);
              sysValuenew = decimalTest + 128;
                }
            if (bRecv[nRecved-3] < 0) {
                
                decimalTest = (bRecv[nRecved-3]& 0x7f);
                diaValuenew = decimalTest + 128;
                  }
     //         diaValuenew = (bRecv[nRecved-3]);
               	String temper = "C";
              	if (bRecv[nRecved-6] == 21)  temper = "F";
              
              	float datat=(float) ((sysValuenew * 256 + diaValuenew))/10;
 
              	//Log.e(Common.TAG, "Results: " + sysValuenew+"|"+diaValuenew+"|"+temper);
              
 
              //Log.d(Common.TAG, "Results: " + sysValuenew+"|"+diaValuenew+"|"+pulseValuenew);
				              resNew = "connected2new";
              
                
                bpNew= datat +"|"+ temper;
                Log.e(Common.TAG, "TEMP RES: " + datat+"|"+temper);

          
                
                // ======================= Disconnect the device after measurement PU 24/06
                
                //Log.d(Common.TAG, "Device Disconnected: " + bufRecv1[0]);
            
                
                
                try {
              if (mmInStream != null)
                mmInStream.close();
              if (mmOutStream != null)
                mmOutStream.close();
              if (btSocket != null)
                btSocket.close();
            } catch (IOException e) {
              Log.e(Common.TAG, "Close Error");
              e.printStackTrace();
            } finally {
              mmInStream = null;
              mmOutStream = null;
              btSocket = null;
              bConnect = false;
			  temprinit = 0;
              result = 1;
              modeNew = 10;
              rxTimes = 0;        // ================= Added PU 17/6
//              byte[] bufRecv1 = {0,0,0,0,0,0,0,0};
              res = "No result";
              nRecved = 0;      //    Added by PU 9 Sept'14
              nRecv1 = 0;
              txTime = 0;
              rxCount = 0;

			}
  
          }
    	}else
          {
				resNew1 = "Not Connected";
	            nRecved = 0;
	            nRecv1 = 0;
				return resNew1;
          }

        
  
      return resNew;
    }
    


// =============================================== Bluetooth Disconnect start
      
      @JavascriptInterface
      public String BLdisconnect()
      {
    	  		Log.e(Common.TAG, "ENTERED BP DISCONNECT 1: " + bpNew);
    	  		decimalTest1 = 0;				//		Added by PU 10 Sept'14
          		resNewoxy = "0";
            	result = 1;
            	nRecved = 0;			//		Added by PU 9 Sept'14
            	modeNew = 10;
            	temprinit = 0;
            	modeNewDyn = 10;			// Added PU 27/1/15
    			nRecv1 = 0;
    			txTime = 0;
            	
            	
                try {
                              if (mmInStream != null)					
                                mmInStream.close();
                              Log.e(Common.TAG, "ENTERED BP DISCONNECT 2: " + bpNew);
                              if (mmOutStream != null)					
                                mmOutStream.close();
                              Log.e(Common.TAG, "ENTERED BP DISCONNECT 3: " + bpNew);
                              if (btSocket != null)						
                                btSocket.close();
                              Log.e(Common.TAG, "ENTERED BP DISCONNECT 4: " + bpNew);
                            } 
                            catch (IOException e) {						
                              Log.e(Common.TAG, "Close Error"); 		
                              e.printStackTrace();
                            } 											 
                            
                            finally {
                              mmInStream = null;	
                              mmOutStream = null;	
                              btSocket = null;		
                              bConnect = false;		
                              result = 1;
                              modeNewDyn = 10;			
                              rxTimes = 0;        // ================= Added PU 17/6
//                              byte[] bufRecv1 = {0,0,0,0,0,0,0,0};
                              res = "No result";
                              nRecv1 = 0;
                                  
                              }
            	
            	return bpNew;
      }
      
      
// =============================================== Bluetooth Disconnect End      


    //========================== New Wrist Oxy Monitor Code Start - PU 26/06/14
      
      
      @JavascriptInterface
      public String readBloodPressurenewOxy()
      {
    	decimalTest1 = 0;				//		Added by PU 10 Sept'14
  //  	resNewoxy = "Not ConnectedNC";	//		Added by PU 10 Sept'14
    	resNewoxy = "0";
      	result = 1;
      	nRecved = 0;			//		Added by PU 9 Sept'14
      	modeNew = 10;
      	modeNewDyn = 10;			// Added PU 27/1/15
		
		//SPO2
		String Name="Pramod 7 S8 BT";
    	String Phone="+6112345678912";
    	String time=Timestamp();
    	String mac=Findmac();
    	String type="Medium";
    	String[] var=bpNew.split("\\|");
    	String bodyy=time+","+mac+","+type+","+"Joe Black SPO2:"+var[1]+"%,"+var[2]+" OK,Future use,Future use,Future use";
    	String Subject="SPO2 Mode1,"+Phone+","+Name;
    	SendMail sendma=new SendMail(Subject,bodyy,0);;
    	LastDevice = 15;
      	return bpNew;
      }
      
      
      boolean oxyIsConnected = false;
      @JavascriptInterface
      public String connectFCnewOxy() throws InterruptedException
      {
    	
    	  Log.e(Common.TAG, "Loops to CNA - OXY: " + decimalTest1);
  		
  		decimalTest1 = decimalTest1+1;
    	  

  		
     // 	if (modeNew == 11) {
      	
      	
        // --------------------------------------By PU 11 Sept'14    	
      	int counter = 0;
      	
      	if (modeNew == 10) { 
      	
      		Log.d(Common.TAG, "modeNew: " + modeNew);
      		
      	while(modeNew != 11 ) // pause until modeNew becomes 11 _ UPDATED AKASH
      	{
      		
      		Log.d(Common.TAG, "modeNew: " + modeNew);
      		counter++;
      		try{
      			Thread.sleep(50);
      		}catch(Exception E){}
      		if(counter > 50) return "Not Connected";
      	}
  		
  		try{
  			Thread.sleep(2000);
  		}catch(Exception E){}
      	}
      	 // --------------------------------------By PU 11 Sept'14  																								
      			
  		try{
  			Thread.sleep(2000);
  		}catch(Exception E){}
      		Log.d(Common.TAG, "RX Data 1: " + modeNew);
      		nRecved = 0;
      		bConnect = true;
      		new Thread(new Runnable() {
      			public void run() {
      				byte[] bufRecv = new byte[4096];		
      				int nRecv = 0;
      				while (bConnect) {
      					try {
      						Log.e(Common.TAG, "Start Recv" + String.valueOf(mmInStream.available()));
      						nRecv = mmInStream.read(bufRecv);
      						if (nRecv < 1) {
      							Log.e(Common.TAG, "Recving Short");
      							Thread.sleep(100);					
      							continue;
      							
      						}
      						
      						System.arraycopy(bufRecv, 0, bRecv, nRecved, nRecv);	
      						
      	   						
      						bufRecv1[0] = (byte) (bRecv[0] - 0x80);
      						Log.d(Common.TAG, "RX Data 1: " + bufRecv1[0]);
      						rxTimes = rxTimes + 1;
      					    
      		    						
      						Log.e(Common.TAG, "Recv:" + String.valueOf(nRecv));
      						nRecved += nRecv;
      						Log.d(Common.TAG, "RX Data 1: " + bufRecv1[nRecv]);
      						
      						if(nRecved < nNeed)
      						{
      							Thread.sleep(100);					
      							continue;
      						}
      		
      		
      	    				} catch (Exception e) {
      						Log.e(Common.TAG, "Recv thread:" + e.getMessage());
      	    						
      						break;
      					}
      				}
      				Log.e(Common.TAG, "Exit while");
      			}
      		}).start();
     	
      		// ======================================================= TX Routine
      		
      		Log.d(Common.TAG, "Number of RX Data: " + nRecved);

      //======================================================================= Code Rx Data		
      		
      		while( !(nRecved > 4) )// PAUSE until data is received _ UPDATED AKASH
      		{
      			counter++;
          		try{
          			Thread.sleep(50);
          		}catch(Exception E){}
          		if(counter > 100) return "Not Connected";
      		}																						
      	
      		bufRecv1[5] = (byte) (bRecv[nRecved-5] & 0x18);
      		
      		if (bufRecv1[5] == 0) {
  				
  				
  					resNewoxy = "connectedOxy";
  		        	
  		        	int sysValuenew = (bufRecv1[5]);

  		            int diaValuenew = (bRecv[nRecved-1]);
  		            int pulseValuenew = (bRecv[nRecved-2]);
  		            
  		            bpNew=sysValuenew+"|"+diaValuenew+"|"+pulseValuenew;
  		          Log.e(Common.TAG, "RESULTS OXYMETER: " + sysValuenew+"|"+diaValuenew+"|"+pulseValuenew);
  		            Log.d(Common.TAG, "Oxymonitor Finish: " + sysValuenew+"|"+diaValuenew+"|"+pulseValuenew);
  		            
  		            // ======================= Disconnect the device after measurement PU 24/06
  		            
  		            Log.d(Common.TAG, "Device Disconnected: " + bufRecv1[0]);
  		    		try {
  		    			Log.e(Common.TAG, "Entered OXY TRY1: " + sysValuenew+"|"+diaValuenew+"|"+pulseValuenew);
  		    			if (mmInStream != null)
  		    				mmInStream.close();
  		    			Log.e(Common.TAG, "Entered OXY TRY2: " + sysValuenew+"|"+diaValuenew+"|"+pulseValuenew);
  		    			if (mmOutStream != null)
  		    				mmOutStream.close();
  		    			Log.e(Common.TAG, "Entered OXY TRY3: " + sysValuenew+"|"+diaValuenew+"|"+pulseValuenew);
  		    			if (btSocket != null)
  		    				btSocket.close();
  		    			Log.e(Common.TAG, "Entered OXY TRY4: " + sysValuenew+"|"+diaValuenew+"|"+pulseValuenew);
  		    		} catch (IOException e) {
  		    			Log.e(Common.TAG, "Close Error");
  		    			e.printStackTrace();
  		    		} finally {
  		    			Log.e(Common.TAG, "Entered FINALLY: " + sysValuenew+"|"+diaValuenew+"|"+pulseValuenew);
  		    			mmInStream = null;
  		    			mmOutStream = null;
  		    			btSocket = null;
  		    			bConnect = false;
  		    			result = 1;
  						modeNew = 10;
  		    			rxTimes = 0;				// ================= Added PU 17/6
//  		    			byte[] bufRecv1 = {0,0,0,0,0,0,0,0};
  		    			res = "No result";
  		    			nRecv1 = 0;
  				    			
  		    			}
  		        	
      	   } 
          	
    //  	}	  

      // New Oxy Monitor Code End - PU 19/06/14     
      	
      	return resNewoxy;
      		
      }

     
      @JavascriptInterface
      public String readBloodSugar()
      {
    	decimalTest1 = 0;				//		Added by PU 10 Sept'14
  //  	resNewscale = "Not ConnectedNC";	//		Added by PU 10 Sept'14
    	resNewscale = "0";
      	result = 1;
      	modeNew = 10;
      	modeNewGluco = 0;
      	modeNewDyn = 10;			// Added PU 27/1/15
      	nRecved = 0;					// Added by PU 8 Sept '14
      	txTime = 0;						// Added by PU 8 Sept '14
		
      	//bloodglucose
		String Name="Pramod 7 S8 BT";
    	String Phone="+6112345678912";
      	String time=Timestamp();
    	String mac=Findmac();
    	String type="Medium";
    	float data=Float.parseFloat(bpNewGluco);
    	data=(float) (data/18.0182);
    	String bodyy=time+","+mac+","+type+","+"Joe Black GLUCO:"+data+" OK,Future use,Future use,Future use";
    	String Subject="GLUCO Mode1,"+Phone+","+Name;
    	SendMail sendma=new SendMail(Subject,bodyy,0);;
    	
      
		
    	Log.e(Common.TAG, "Gluco email sent: " + result);
    	
      	//cholesterol
		Name="Pramod 7 S8 BT";
    	Phone="+6112345678912";
    	String timec=Timestamp();
    	String macc=Findmac();
    	String typec="Medium";
    	float datac=Float.parseFloat(bpNewCholest);
    	datac=(float) (datac/18.0182);
    	String bodyyc=timec+","+macc+","+typec+","+"Joe Black CHOLESTEROL:"+datac+" OK,Future use,Future use,Future use";
    	String Subjectc="CHOLESTEROL Mode1,"+Phone+","+Name;
    	    	
    	SendMail sendmac=new SendMail(Subjectc,bodyyc,0);;

    	Log.e(Common.TAG, "Cholest email sent: " + result);
    	
		
      	//Uric Acid
		Name="Pramod 7 S8 BT";
    	Phone="+6112345678912";
    	String timeu=Timestamp();
    	String macu=Findmac();
    	String typeu="Medium";
    	float datau=Float.parseFloat(bpNew);
    	datau=(float) (datau/59.48);
    	String bodyyu=timeu+","+macu+","+typeu+","+"Joe Black URIC:"+datau+" OK,Future use,Future use,Future use";
    	String Subjectu="URIC Mode1,"+Phone+","+Name;
    	SendMail sendmau=new SendMail(Subjectu,bodyyu,0);;
    	LastDevice = 14;
    	Log.e(Common.TAG, "Uric Acid email sent: " + result);
    	
      	return bpNewGluco;
      }
      
      
      
      @JavascriptInterface
      public String connectFC() throws InterruptedException
      {
      		
  		// ==========================================	23/06
  	
    	  Log.d(Common.TAG, "Loops to CustomNativeAccess: " + decimalTest1);
  		
  		decimalTest1 = decimalTest1+1;	
         
//      		String res="";


      	int counter = 0;
      	
      	if (modeNew == 10) { 
      	
      		Log.d(Common.TAG, "modeNew: " + modeNew);
      		
      	while(modeNew != 11 ) // pause until modeNew becomes 11 _ UPDATED AKASH
      	{
      		
      		Log.d(Common.TAG, "modeNew: " + modeNew);
      		counter++;
      		try{
      			Thread.sleep(50);
      		}catch(Exception E){}
      		if(counter > 50) return "Not Connected";
      	}
  		
      	Thread.sleep(5000);
      	
      	}
      	 // --------------------------------------By PU 11 Sept'14  
      	
      	    	if (modeNew == 11) {				
          		
      	    //		do {
      		// ======================================================= TX Routine
      		    		
      		    		Log.e(Common.TAG, "Number of RX Data - Gluco: " + nRecved);
      		    		
      		    	    Thread.sleep(1000);
      		    	
      		    		 if (txTime < 1)		 
      				        {
      				        try {
      				          Log.e(Common.TAG, "TX CMD Start Gluco: " + txTime);
      				          Thread.sleep(50);

      				          char[] test = { 0x7b, 0x4D, 0x47, 0x23, 0xa9, 0x7d}; // Mode Gluco 7b 4D 47 23 a9 7d

      				          for(int k=0; k < test.length; k++){
      				          new DataOutputStream(btSocket.getOutputStream()).writeByte(test[k]);
      				          }

      				        Thread.sleep(200);
    				          
      				        char[] test2 = { 0x7b, 0x4c, 0x23, 0xef, 0x7d}; // No of Records 7b 4C 23 ef 7d
      				          
      				        for(int k=0; k < test2.length; k++){
        				          new DataOutputStream(btSocket.getOutputStream()).writeByte(test2[k]);
        				          }  
      				        Thread.sleep(200);
      				          
      				        char[] test1 = { 0x7b, 0x44, 0x23, 0xe7, 0x7d}; // Data Gluco 7b 44 23 e7 7d
      				          
      				        for(int k=0; k < test1.length; k++){
        				          new DataOutputStream(btSocket.getOutputStream()).writeByte(test1[k]);
        				          }
      				          
      				          txTime = txTime + 1;
      				          Log.e(Common.TAG, "TX CMD Sent Gluco: " + txTime);

      				         
      				          Thread.sleep(200);

      				        } catch (IOException e) {
      				          // TODO Auto-generated catch block
      				          e.printStackTrace();
      				        } catch (InterruptedException e) {
      				          // TODO Auto-generated catch block
      				          e.printStackTrace();
      				        }
      				        }
      				          		
      		    		
     
      			
      		Log.e(Common.TAG, "RX Data Mode Gluco: " + modeNew);

      //		 Thread.sleep(500);
      		
      		bConnect = true;
      		new Thread(new Runnable() {
      			public void run() {
      				byte[] bufRecv = new byte[100];		
      				int nRecv = 0;
      				do{
      					try {
      						//Log.e(Common.TAG, "Start Recv" + String.valueOf(mmInStream.available()));
      						nRecv = mmInStream.read(bufRecv);
      						if (nRecv < 1) {
      							Log.e(Common.TAG, "Recving Short");
      							Thread.sleep(100);					
      							continue;
      							
      						}
      						
      						System.arraycopy(bufRecv, 0, bRecv, nRecved, nRecv);	
      						
      	   						
      						bufRecv1[0] = (byte) (bRecv[0] - 0x80);
      						Log.d(Common.TAG, "In RX Data Loop 1: " + bufRecv1[0]);
      						rxTimes = rxTimes + 1;
      					    
      		    						
      						//Log.e(Common.TAG, "Recv:" + String.valueOf(nRecv));
      						nRecved += nRecv;
      						Log.d(Common.TAG, "In RX Data Loop 2: " + bufRecv1[nRecv]);
      						
      						if(nRecved < nNeed)
      						{
      							Thread.sleep(100);					
      							continue;
      						}
      		
      		
      	    				} catch (Exception e) {
      						Log.e(Common.TAG, "Recv thread :" + e.getMessage());
      	    						
      						break;
      					}
      				}while(nRecved!=39);
     				
      				Log.e(Common.TAG, "Exit while Gluco");
      			}
      		}).start();
     
      //		}					Removed by PU 12 Sept'14
      //======================================================================= Code Rx Data		
      		
    		int counter1=0;
      		while(nRecved < 39) // pause until modeNew becomes 11 
    		{
    			
    			Log.e(Common.TAG, "Waiting in Gluco RX loop");
    			counter1++;
    			try{
    				Thread.sleep(100);
    			}catch(Exception E){}
    			if(counter1 > 20) 
    			{
    				txTime = 0;	
    				modeNewGluco = 20;
    			  	txTime = 0;
    			  	nRecved = 0;
    			  	nRecv1 = 0;
					resNew = "0";
					resNew1 = "Not Connected";
    				result = 1;
					//strName = "NG";
					//modeNew = 10;
					//Log.e(Common.TAG, "(ERROR)Device Connected: " + strName);
					//Log.e(Common.TAG, "(ERROR)Connect Staus: " + resNew1);
    				return resNew1;
    			}
    		}
      		
      		if (nRecved > 14) {
  		  					
  					resNewscale = "connected1";
  					bpNewGluco = "";
  		        	
 /* 	      			if (nRecved < 39) {
  	        			resNewscale = "connectedNG";
  	        			txTime = 0;
  	        			nRecved = 0;
  	        			return resNewscale;
  	        			}						Removed PU 05FEB16 */
  		        	
  		        	int sysValuenew = ((bRecv[18]-48)*100);

  		            int diaValuenew = ((bRecv[19]-48)*10);
  		            int pulseValuenew = (bRecv[20]-48);
  		            intres = sysValuenew+diaValuenew+pulseValuenew;
  		            Log.e(Common.TAG, "Final Results 39 Gluco: " +"|"+ sysValuenew+"|" + diaValuenew+"|" + pulseValuenew);
  					
  		      		
  		            intres = sysValuenew+diaValuenew+pulseValuenew;
					
								
		        	if (intres < 0) {
					
		        		decimalTest = (intres & 0x7f);
		        		intres = decimalTest + 128;
		        			}
		        	bpNewGluco =""+intres;
		        	
  		            //Log.e(Common.TAG, "New Glucose: " + bRecv[0]+"|"+bRecv[1]+"|"+bRecv[2]+"|"+ bRecv[15]+"|"+bRecv[18]+"|"+bRecv[19]+"|"+bRecv[20]);
  		          //Log.e(Common.TAG, "Final Results Gluco: " +"|"+ sysValuenew+"|" + diaValuenew+"|" + pulseValuenew);
  		        	Log.e(Common.TAG, "Final Results Gluco: " + bpNewGluco);
  		        	
  		        	
  		        	txTime = 0;
  		        	nRecved = 0;
  		        	nRecv1 = 0;
  		        	modeNewGluco = 10;
      		} 
  //    	   }while (modeNewGluco != 10);
  		            
	// ************************************************************************* New Cholest and New Uric
		  
      	    		//do {      	   		
      	    		
      	    			Thread.sleep(100);
     	      		// ======================================================= TX Routine
      	    			Log.e(Common.TAG, "Entered Cholest: " + modeNewGluco);
    		    		Log.e(Common.TAG, "Number of RX Data Cholest: " + nRecved);
    		    		
    		    	    Thread.sleep(100);
    		    	
    		    		 if (txTime < 1)		 
    				        {
    				        try {
    				          Log.e(Common.TAG, "TX CMD Start Cholest: " + txTime);
    				          Thread.sleep(50);

    				          char[] test = { 0x7b, 0x4D, 0x43, 0x23, 0xad, 0x7d}; // Mode Chole 7b 4D 43 23 ad 7d

    				          for(int k=0; k < test.length; k++){
    				          new DataOutputStream(btSocket.getOutputStream()).writeByte(test[k]);
    				          }

    				        Thread.sleep(200);
  				          
    				        char[] test1 = { 0x7b, 0x4c, 0x23, 0xef, 0x7d}; // Rec No 7b 4C 23 ef 7d
    				          
    				        for(int k=0; k < test1.length; k++){
      				          new DataOutputStream(btSocket.getOutputStream()).writeByte(test1[k]);
      				          }  
    				          
    				       
    				        Thread.sleep(200);
    				          
    				        char[] test2 = { 0x7b, 0x44, 0x23, 0xe7, 0x7d}; // Data Chole 7b 44 23 e7 7d
    				          
    				        for(int k=0; k < test2.length; k++){
      				          new DataOutputStream(btSocket.getOutputStream()).writeByte(test2[k]);
      				          }
    				          
    				          txTime = txTime + 1;
    				          Log.e(Common.TAG, "TX CMD Sent1 Cholest: " + txTime);

    				         
    				          Thread.sleep(200);

    				          Log.d(Common.TAG, "TX CMD Sent2: " + txTime);
    				        
    				        } catch (IOException e) {
    				          // TODO Auto-generated catch block
    				          e.printStackTrace();
    				        } catch (InterruptedException e) {
    				          // TODO Auto-generated catch block
    				          e.printStackTrace();
    				        }
    				        }
    				          		
    		    		
			
    		Log.e(Common.TAG, "RX Data Mode Cholest: " + modeNew);

  		//Thread.sleep(200);
    		bConnect = true;
    		new Thread(new Runnable() {
    			public void run() {
    				byte[] bufRecv = new byte[100];		
    				int nRecv = 0;
    				while (bConnect) {
    					try {
    						//Log.e(Common.TAG, "Start Recv" + String.valueOf(mmInStream.available()));
    						nRecv = mmInStream.read(bufRecv);
    						if (nRecv < 1) {
    							Log.e(Common.TAG, "Recving Short");
    							Thread.sleep(100);					
    							continue;
    							
    						}
    						
    						System.arraycopy(bufRecv, 0, bRecv, nRecved, nRecv);	
    						
    	   						
    						bufRecv1[0] = (byte) (bRecv[0] - 0x80);
    						//Log.d(Common.TAG, "In RX Data Loop 1: " + bufRecv1[0]);
    						rxTimes = rxTimes + 1;
    					    
    		    						
    						//Log.e(Common.TAG, "Recv:" + String.valueOf(nRecv));
    						nRecved += nRecv;
    						//Log.d(Common.TAG, "In RX Data Loop 2: " + bufRecv1[nRecv]);
    						
    						if(nRecved < nNeed)
    						{
    							Thread.sleep(100);					
    							continue;
    						}
    		
    		
    	    				} catch (Exception e) {
    						Log.e(Common.TAG, "Recv thread :" + e.getMessage());
    	    						
    						break;
    					}
    				}
   				
    				Log.e(Common.TAG, "Exit while Cholest");
    			}
    		}).start();

    		counter1=0;
    		while(nRecved < 39) // pause until modeNew becomes 11 
    		{
    			Log.e(Common.TAG, "Waiting in Cholest RX loop");
    			counter1++;
    			try{
    				Thread.sleep(100);
    			}catch(Exception E){}
    			if(counter1 > 20) 
    			{
    				txTime = 0;	
    				modeNewGluco = 20;
    			  	txTime = 0;
    			  	nRecved = 0;
    			  	nRecv1 = 0;
					resNew = "0";
					resNew1 = "Not Connected";
    				result = 1;
					//strName = "NG";
					//modeNew = 10;
					//Log.e(Common.TAG, "(ERROR)Device Connected: " + strName);
					//Log.e(Common.TAG, "(ERROR)Connect Staus: " + resNew1);
    				return resNew1;
    			}
    		}
	

	if (nRecved > 14) {  
        
		resNewscale = "connected1";
    	
/*			if (nRecved < 39) {
  			resNewscale = "connectedNG";
        txTime = 0;
        nRecved = 0;
        nRecv1 = 0;
        return resNewscale;
  			}							Removed PU 05FEB16 */
		
			bpNewCholest = "";
      
		 int sysValuenewc = ((bRecv[18]-48)*100);

        int diaValuenewc = ((bRecv[19]-48)*10);
        int pulseValuenewc = (bRecv[20]-48);
        intres = sysValuenewc+diaValuenewc+pulseValuenewc;
        Log.e(Common.TAG, "Final Results Cholest: " +"|"+ sysValuenewc+"|" + diaValuenewc+"|" + pulseValuenew);
		
  		
        
    	if (intres < 0) {
		
    		decimalTest = (intres & 0x7f);
    		intres = decimalTest + 128;
    			}
          intres = intres * 18; 
          intres = intres / 39;
          
          bpNewCholest =""+intres;
          Log.e(Common.TAG, "New Cholest: " + bRecv[0]+"|"+bRecv[1]+"|"+bRecv[2]+"|"+ bRecv[15]+"|"+bRecv[18]+"|"+bRecv[19]+"|"+bRecv[20]);
        
      	Log.e(Common.TAG, "Final Results Cholest: " + bpNewCholest);
          
        	
		}	
		     
	txTime = 0;	
	modeNewGluco = 20;
  	txTime = 0;
  	nRecved = 0;
  	nRecv1 = 0;
      
  //    }while (modeNewGluco != 20);	
      	    		
  		    		Log.e(Common.TAG, "Number of RX Data Uric : " + nRecved);
  		    		
  		    	    Thread.sleep(500);
  		    	
  		    		 if (txTime < 1)		 
  				        {
  				        try {
  				          Log.e(Common.TAG, "TX CMD Start Uric: " + txTime);
  				          Thread.sleep(50);

  				          char[] test = { 0x7b, 0x4D, 0x55, 0x23, 0xbb, 0x7d}; // Mode Uric Acid 7b 4D 55 23 bb 7d

  				          for(int k=0; k < test.length; k++){
  				          new DataOutputStream(btSocket.getOutputStream()).writeByte(test[k]);
  				          }

  				        Thread.sleep(200);
				          
  				        char[] test1 = { 0x7b, 0x4c, 0x23, 0xef, 0x7d}; // Rec No 7b 4C 23 ef 7d
  				          
  				        for(int k=0; k < test1.length; k++){
    				          new DataOutputStream(btSocket.getOutputStream()).writeByte(test1[k]);
    				          }  
  				          
  				        Thread.sleep(200);
  				          
  				        char[] test2 = { 0x7b, 0x44, 0x23, 0xe7, 0x7d}; // Data Uric Acid 7b 44 23 e7 7d
  				          
  				        for(int k=0; k < test2.length; k++){
    				          new DataOutputStream(btSocket.getOutputStream()).writeByte(test2[k]);
    				          }
  				          
  				          txTime = txTime + 1;
  				          Log.e(Common.TAG, "TX CMD Sent Uric : " + txTime);

  				         
  				          Thread.sleep(200);

  				        } catch (IOException e) {
  				          // TODO Auto-generated catch block
  				          e.printStackTrace();
  				        } catch (InterruptedException e) {
  				          // TODO Auto-generated catch block
  				          e.printStackTrace();
  				        }
  				        }
  				          		
  		    		
 
  			
  		Log.e(Common.TAG, "RX Data Mode Uric: " + modeNew);

  //		 Thread.sleep(500);
  		
  		bConnect = true;
  		new Thread(new Runnable() {
  			public void run() {
  				byte[] bufRecv = new byte[100];		
  				int nRecv = 0;
  				while (bConnect) {
  					try {
  						//Log.e(Common.TAG, "Start Recv" + String.valueOf(mmInStream.available()));
  						nRecv = mmInStream.read(bufRecv);
  						if (nRecv < 1) {
  							Log.e(Common.TAG, "Recving Short");
  							Thread.sleep(100);					
  							continue;
  							
  						}
  						
  						System.arraycopy(bufRecv, 0, bRecv, nRecved, nRecv);	
  						
  	   						
  						bufRecv1[0] = (byte) (bRecv[0] - 0x80);
  						//Log.d(Common.TAG, "In RX Data Loop 1: " + bufRecv1[0]);
  						rxTimes = rxTimes + 1;
  					    
  		    						
  						//Log.e(Common.TAG, "Recv:" + String.valueOf(nRecv));
  						nRecved += nRecv;
  						//Log.d(Common.TAG, "In RX Data Loop 2: " + bufRecv1[nRecv]);
  						
  						if(nRecved < nNeed)
  						{
  							Thread.sleep(100);					
  							continue;
  						}
  		
  		
  	    				} catch (Exception e) {
  						Log.e(Common.TAG, "Recv thread :" + e.getMessage());
  	    						
  						break;
  					}
  				}
 				
  				Log.e(Common.TAG, "Exit while");
  			}
  		}).start();
 
  //		}					Removed by PU 12 Sept'14
  //======================================================================= Code Rx Data		
  		counter1 = 0;
		while(nRecved < 39) // pause until modeNew becomes 11 
		{
			Log.e(Common.TAG, "Waiting in Uric RX loop");
			counter1++;
			try{
				Thread.sleep(100);
			}catch(Exception E){}
			if(counter1 > 20) 
			{

				txTime = 0;	
				modeNewGluco = 20;
			  	txTime = 0;
			  	nRecved = 0;
			  	nRecv1 = 0;
				resNew = "0";
				resNew1 = "Not Connected";

				return resNew1;
			}
		}
  		
  		if (nRecved > 14) {
  			
  			resNewscale = "connected1";
  			
/*  			if (nRecved < 39) {
  			resNewscale = "connectedNG";
  			}										Removed PU 05FEB16 */
		  					
  			
        	bpNew = "";
          
    		int sysValuenew = ((bRecv[18]-48)*100);

            int diaValuenew = ((bRecv[19]-48)*10);
            int pulseValuenew = (bRecv[20]-48);
            intres = sysValuenew+diaValuenew+pulseValuenew;
            //Log.e(Common.TAG, "Final Results Uric: " +"|"+ sysValuenew+"|" + diaValuenew+"|" + pulseValuenew);
            
      		
		            
		        intres = sysValuenew+diaValuenew+pulseValuenew;
				
		        //Log.d(Common.TAG, "Final intres: " + intres);
							
	        	if (intres < 0) {
				
	        		decimalTest = (intres & 0x7f);
	        		intres = decimalTest + 128;
	        			}
		            
	        	//Log.e(Common.TAG, "Final intres Uric: " + intres);
	        	intres = intres * 6; 
	  		            
		            bpNew =""+intres;
		
  		}
		        	
		        	// ********************************** New Cholest and New Uric	        	
  		            

  		            
  		            //Log.e(Common.TAG, "New Uric: " + bRecv[0]+"|"+bRecv[1]+"|"+bRecv[2]+"|"+ bRecv[15]+"|"+bRecv[18]+"|"+bRecv[19]+"|"+bRecv[20]);
  		          //Log.e(Common.TAG, "Final Results Uric: " +"|"+ sysValuenew+"|" + diaValuenew+"|" + pulseValuenew);
  		        	Log.e(Common.TAG, "Final Results Uric: " + bpNew);
  		            
  		            // ======================= Disconnect the device after measurement PU 24/06
  		            
  		            Log.e(Common.TAG, "Device Disconnected: " + nRecved);
  		    		try {
  		    			if (mmInStream != null)
  		    				mmInStream.close();
  		    			if (mmOutStream != null)
  		    				mmOutStream.close();
  		    			if (btSocket != null)
  		    				btSocket.close();
  		    		} catch (IOException e) {
  		    			Log.e(Common.TAG, "Close Error");
  		    			e.printStackTrace();
  		    		} finally {
  		    			mmInStream = null;
  		    			mmOutStream = null;
  		    			btSocket = null;
  		    			bConnect = false;
  		    			result = 1;
  						modeNew = 10;
  		    			rxTimes = 0;				// ================= Added PU 17/6
//  		    			byte[] bufRecv1 = {0,0,0,0,0,0,0,0};
  		    			res = "No result";
  		    			nRecv1 = 0;
  		    			txTime = 0;
  		    			modeNewGluco = 0;
  		    			nRecved = 0;				// Added by PU 8 Sept '14
  		    			}
  		        
  			      	    //} Removed by PU 04 Feb'16
          	
      	}										// Removed by PU 11 Sept'14 to insert while

      // New Weigh Scale Code End - PU 19/06/14     
      	result = 1;									// Added by PU 10 NOV 14
      	Log.d(Common.TAG, "Un Connected: " + result);	// Added by PU 10 NOV 14
      	return resNewscale;
      }

//=================End of New Glucose Monitor   
      
      
      //========WeighingScale
   // New Wrist BT Scale Code Start - PU 27/06/14
      
      @JavascriptInterface
      public String readBloodPressurenewScale()
      {
    	decimalTest1 = 0;				//		Added by PU 10 Sept'14
  //  	resNewscale = "Not ConnectedNC";	//		Added by PU 10 Sept'14
    	resNewscale = "0";
      	result = 1;
      	modeNew = 10;
      	modeNewDyn = 10;			// Added PU 27/1/15
      	nRecved = 0;					// Added by PU 8 Sept '14
      	txTime = 0;						// Added by PU 8 Sept '14
		
      	//Weight
		String Name="Pramod 7 S8 BT";
    	String Phone="+6112345678912";
    	String time=Timestamp();
    	String mac=Findmac();
    	String type="Medium";
    	String bodyy=time+","+mac+","+type+","+"Joe Black WEIGHT:"+bpNew+" OK,Future use,Future use,Future use";
    	//String bodyy=time+","+mac+","+type+","+"Joe Black WEIGHT:"+var[0]+"."+var[1]" OK,Future use,Future use,Future use";
    	String Subject="WEIGHT Mode1,"+Phone+","+Name;
    	SendMail sendma=new SendMail(Subject,bodyy,0);;
    	LastDevice = 12;
      	return bpNew;
      }
      
      
      
      @JavascriptInterface
      public String connectFCnewScale() throws InterruptedException
      {
      		
  		// ==========================================	23/06
  	
    	  Log.d(Common.TAG, "Loops to CustomNativeAccess: " + decimalTest1);
  		
  		decimalTest1 = decimalTest1+1;	
         
//      		String res="";

  	
      	int counter = 0;
      	
      	if (modeNew == 10) { 
      	
      		//Log.d(Common.TAG, "modeNew: " + modeNew);
      		
      	while(modeNew != 11 ) // pause until modeNew becomes 11 _ UPDATED AKASH
      	{
      		
      		//Log.d(Common.TAG, "modeNew: " + modeNew);
      		counter++;
      		try{
      			Thread.sleep(50);
      		}catch(Exception E){}
      		if(counter > 100) return "Not Connected";
      	}
  		
      	Thread.sleep(2000);
      	
      	}
      	 // --------------------------------------By PU 11 Sept'14  
      	
     // 	    	if (modeNew == 11) {				
          		
    //  		if (nRecved < 16) {						Removed by PU 12 Sept'14
      		// ======================================================= TX Routine
      		    		
      		    		Log.d(Common.TAG, "Number of RX Data: " + nRecved);
      		    		
      		    	    Thread.sleep(500);
      		    	
      		    		 if (txTime < 1)		 
      				        {
      				        try {
      				          Log.d(Common.TAG, "TX CMD Start: " + txTime);
      				          Thread.sleep(50);

      				          char[] test = { 0xfd, 0x31, 0x00, 0x00, 0x00, 0x00, 0x00, 0x31};

      				          for(int k=0; k < test.length; k++){
      				          new DataOutputStream(btSocket.getOutputStream()).writeByte(test[k]);
      				          }
  
      				          txTime = txTime + 1;
      				          Log.d(Common.TAG, "TX CMD Sent: " + txTime);

      				         
      				          Thread.sleep(200);

      				        } catch (IOException e) {
      				          // TODO Auto-generated catch block
      				          e.printStackTrace();
      				        } catch (InterruptedException e) {
      				          // TODO Auto-generated catch block
      				          e.printStackTrace();
      				        }
      				        }
      				          		
      		    		
     
      			
      		Log.d(Common.TAG, "RX Data Mode: " + modeNew);

      //		 Thread.sleep(500);
      		
      		bConnect = true;
      		new Thread(new Runnable() {
      			public void run() {
      				byte[] bufRecv = new byte[1024];		
      				int nRecv = 0;
      				while (bConnect) {
      					try {
      						Log.e(Common.TAG, "Start Recv" + String.valueOf(mmInStream.available()));
      						nRecv = mmInStream.read(bufRecv);
      						if (nRecv < 1) {
      							Log.e(Common.TAG, "Recving Short");
      							Thread.sleep(100);					
      							continue;
      							
      						}
      						
      						System.arraycopy(bufRecv, 0, bRecv, nRecved, nRecv);	
      						
      	   						
      						bufRecv1[0] = (byte) (bRecv[0] - 0x80);
      						//Log.d(Common.TAG, "In RX Data Loop 1: " + bufRecv1[0]);
      						rxTimes = rxTimes + 1;
      					    
      		    						
      						//Log.e(Common.TAG, "Recv:" + String.valueOf(nRecv));
      						nRecved += nRecv;
      						//Log.d(Common.TAG, "In RX Data Loop 2: " + bufRecv1[nRecv]);
      						
      						if(nRecved < nNeed)
      						{
      							Thread.sleep(100);					
      							continue;
      						}
      		
      		
      	    				} catch (Exception e) {
      						Log.e(Common.TAG, "Recv thread :" + e.getMessage());
      	    						
      						break;
      					}
      				}
     				
      				Log.e(Common.TAG, "Exit while");
      			}
      		}).start();
     
      //		}					Removed by PU 12 Sept'14
      //======================================================================= Code Rx Data		
      		
      		if (nRecved > 14) {
  		//		bufRecv1[1] = (byte) (bRecv[nRecved-16] - 0x80);
  				
  		//		if (bufRecv1[16] == 0x4f) {
  					
  					resNewscale = "connectedWT";
  		        	bpNew = "";
  		        	
  		        	
  		        	bpNew = ""+getWeightFromBytes(bRecv[4],bRecv[5]);
  		        	
  		        	
  		            Log.e(Common.TAG, "Final Results: " + bpNew);
  		            
  		            // ======================= Disconnect the device after measurement PU 24/06
  		            
  		            //Log.d(Common.TAG, "Device Disconnected: " + nRecved);
  		    		try {
  		    			if (mmInStream != null)
  		    				mmInStream.close();
  		    			if (mmOutStream != null)
  		    				mmOutStream.close();
  		    			if (btSocket != null)
  		    				btSocket.close();
  		    		} catch (IOException e) {
  		    			Log.e(Common.TAG, "Close Error");
  		    			e.printStackTrace();
  		    		} finally {
  		    			mmInStream = null;
  		    			mmOutStream = null;
  		    			btSocket = null;
  		    			bConnect = false;
  		    			result = 1;
  						modeNew = 10;
  		    			rxTimes = 0;				// ================= Added PU 17/6
//  		    			byte[] bufRecv1 = {0,0,0,0,0,0,0,0};
  		    			res = "No result";
  		    			nRecv1 = 0;
  		    			txTime = 0;
  		    			nRecved = 0;				// Added by PU 8 Sept '14
  		    			}
  		        
  			      	    }
          	
      	//}										// Removed by PU 11 Sept'14 to insert while

      // New Weigh Scale Code End - PU 19/06/14     
      	
      	return resNewscale;
      }
//=================End of Weighing Scale

//================================================ OXY_END
      private String getWeightFromBytes(byte b2,byte b1)
      {
    	  String out = "";
    	  //if (b1 < 0x10) b1 = 0x10;
    	  String s1 = Integer.toHexString(b1);
    	  if (b1 < 0x10) out = Integer.toHexString(b2)+""+0+""+s1.charAt(s1.length()-1);
    	  else out = Integer.toHexString(b2)+""+s1.charAt(s1.length()-2)+""+s1.charAt(s1.length()-1);
    	  int x = Integer.parseInt(out, 16);
    	  
    	  String t = x+"";
    	  char c = t.charAt(t.length()-1);
    	  t = t.substring(0, t.length()-1);
    	  t += "."+c;
    	  
    	  
    	  
    	  return t+" Kg";
      }
      
      public String Connecttodevice()
      {
    	  LastDevice = 10;
    	  btAdapt = BluetoothAdapter.getDefaultAdapter();
    	//Log.e(Common.TAG, "Loops to connect: " + result);
    	//Log.e(Common.TAG, "Device Name: " + strName);
    	//Log.e(Common.TAG, "Device Address: " + strAddress);
    	//Log.e(Common.TAG, "Connection Result: " + modeNew);
    	  
    	  new Thread(new Runnable() {
    			public void run() {
    				//Log.d("hi123","jkdsgsfdjg"+strAddress);
    				resNew = "0";
    				InputStream tmpIn = null;
    				OutputStream tmpOut = null;
    				try {				
    					if (result < 2)						// Added to protect reconnect PU 17/7
    					{
    					Log.e(Common.TAG, "Try Connecting 1: " + strName);
    					UUID uuid = UUID.fromString(SPP_UUID);
    					//Log.e(Common.TAG, "Try Connecting 2: " + strName);
    					BluetoothDevice btDev = btAdapt.getRemoteDevice(strAddress);
    						//Log.e(Common.TAG, "Try Connecting 3: " + btDev.getAddress());
    						btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
    						//Log.e(Common.TAG, "Try Connecting 4: " + btSocket);
    						result = result + 1;

    						btAdapt.cancelDiscovery();
    						try{
    							Thread.sleep(100);
    						}catch(Exception E){}
    						btSocket.connect();
    						tmpIn = btSocket.getInputStream();
	    					tmpOut = btSocket.getOutputStream();
	    					//Log.d(Common.TAG, "Device Connected: " + strName);
	    					modeNew = 11;
	    					resNew1 = "Device Connected";
    					}

    					tmpIn = btSocket.getInputStream();
    					tmpOut = btSocket.getOutputStream();
    					//Log.d(Common.TAG, "Device Connected: " + strName);
    					modeNew = 11;
    					resNew1 = "Device Connected";
    					
    					} 
    				
    					catch (Exception e) {
    					Log.e(Common.TAG, "Error connected to: "+ strAddress);
    					bConnect = false;
    					mmInStream = null;
    					mmOutStream = null;
    					btSocket = null;
    					resNew = "0";
    					resNew1 = "Not Connected";
    					modeNew1 = 12;
    					result = 1;
    					//strName = "NG";
    					modeNew = 10;
    					e.printStackTrace();
    					return;
    				}
    				mmInStream = tmpIn;
    				mmOutStream = tmpOut;
    				resNew = "0";
    				resNew1 = "Device Connected";
    				modeNew = 11;
    				result = 1;
    				return;
    			}

    		}).start();
    	  	//Log.e(Common.TAG, "modeNew: " + modeNew);
    		int counter1 = 0;
    		while(modeNew != 11 ) // pause until modeNew becomes 11 
    		{
    			
    			//Log.e(Common.TAG, "modeNew: " + modeNew);
    			counter1++;
    			try{
    				Thread.sleep(100);
    			}catch(Exception E){}
    			if((counter1 > 200) || (modeNew1 == 12)) 
    			{
    				Log.e(Common.TAG, "modeNew1: " + modeNew1);					
					bConnect = false;
					mmInStream = null;
					mmOutStream = null;
					btSocket = null;
					resNew = "0";
					resNew1 = "Not Connected";
    				result = 1;
					//strName = "NG";
					modeNew = 10;
					Log.e(Common.TAG, "(ERROR)Device Connected: " + strName);
					//Log.e(Common.TAG, "(ERROR)Connect Staus: " + resNew1);
    				return resNew1;
    			}
    		}
    		Log.e(Common.TAG, "(NORMAL)Device Connected: " + strName);
    		//Log.e(Common.TAG, "(NORMAL)Connect Staus: " + resNew1);
    		return resNew1;
      }
      

}// class end
