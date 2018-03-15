package com.hiwi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;


//import jxl.write.WritableSheet;
//import jxl.write.WritableWorkbook;
//import jxl.write.WriteException;
//import jxl.write.biff.RowsExceededException;
import org.apache.cordova.DroidGap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class JSInterface{
	WebView mAppView;
	public static int id_for_img;
	Intent intent;
    private DroidGap mGap;
    public int result_id=1;
	   public JSInterface(DroidGap gap, WebView view){
	        mAppView = view;
	        mGap = gap;
	    }   

	   @JavascriptInterface
	    public String getExternalPath(){
	        return Environment.getExternalStorageDirectory().getPath();
	    }
	   
	   public void SendMsg(int id) {
		   //return "abc";
		    intent = new Intent(this.mGap.getActivity().getApplicationContext(), FdActivity.class);
		    intent.putExtra("uid", ""+id);
		    mGap.getActivity().startActivity(intent);
		  // Toast.makeText(this, "abcdef", 1000).show();
		    
		}
	   
	   @JavascriptInterface
	    public String createapp(int id){
		   id_for_img=id;
		    SendMsg(id);
		    result_id = id;
	        return "created";
	    }
	   
	   public void SendMsg() {
		   //return "abc";
		    intent = new Intent(this.mGap.getActivity().getApplicationContext(), FdActivity.class);
		    intent.putExtra("uid", "Search");
		    mGap.getActivity().startActivity(intent);		   
		    //Log.i("JSInterface createapp", ""+result_id);
		   // mGap.sendJavascript("window.display_activity_result('temp');");
		}
	   
	   
 
	   @JavascriptInterface
	    public String createapp(){
		   //face recognition ID bug resolved code
		    File fr_res_file = new File(Environment.getExternalStorageDirectory().getPath()+"/cache.txt");
		    fr_res_file.delete();
		    SendMsg();
		    //Toast.makeText(getApplicationContext(), "createapp "+result_id, Toast.LENGTH_LONG).show();
		    
		    while(!fr_res_file.exists());
		    while(fr_res_file.length()==0);
		    String res= getId();
		    Log.i("ID val", res);
		    return res;
	    }
	   /*
	   @JavascriptInterface
	   
	    public String resultReturn(){		    
		    //Toast.makeText(getApplicationContext(), "createapp "+result_id, Toast.LENGTH_LONG).show();
	        return "";//+result_id;
	    }
	   */
	   
	   public String getId(){
		   String aDataRow = "";
		   String aBuffer = "";
		   File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/cache.txt");
		   try {
				
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow;
					Log.i("Buffer val", aBuffer);
				}
				//txtData.setText(aBuffer);
				Log.i("Buffer val", aBuffer);
				myReader.close();
				fIn.close();
				
			} catch (Exception e) {
				
				Log.d("Error Condition", "Id is not received");
				
			}
		    /*finally{
		    	if(myFile.exists())myFile.delete();
		    }*/		   
		   int BackPress=-1;
		   if(aBuffer.equals("backpress"))
			   return ""+BackPress;
		   return aBuffer;
	   }
	   
	   @JavascriptInterface
	   public String deletePatientFaces(String id){
		  // String path = getFilesDir()+"/facerecogOCV/"+id+"-"+count+".jpg";
		   //Delete Face recog images
		   Log.d("Delete"," deleting id is "+id);
		   Log.i("Delete", "inside deletePatientFaces");
		   final String fid=id;	   
		  // String del_img_abspath = "/data/data/org.opencv.javacv.facerecognition/files/facerecogOCV/";
			Log.d("Delete",this.mGap.getActivity().getApplicationContext().getFilesDir().getPath()+"/facerecogOCV/");
		   File root = new File(this.mGap.getActivity().getApplicationContext().getFilesDir().getPath()+"/facerecogOCV/");
			Log.i("Delete", "before filename filter");
	        FilenameFilter pngFilter = new FilenameFilter() {
	            public boolean accept(File dir, String n) {
	            	String s=fid;
	            	Log.i("Delete", "inside accept filename filter"+s+" : "+n);
	                return n.startsWith(s+"-");	            
	            };
	        };
	       // Log.d("Delete", "after filename filter");
	        File[] imageFiles = root.listFiles(pngFilter);
	        for (File image : imageFiles) {
	        	image.delete();
	        	Log.d("Delete", "image deleted");
	        }
	        	
			//Delete Face Display Image
			File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GDVFaces/");
			File file = new File(dir, ""+id+".jpg");
			boolean deleted = file.delete();
			if(deleted){
				 Log.i("Delete", "image deleted from GDVFaces");
			}else{
				Log.i("Delete", "image deletion from GDVFaces unsuccessful");
			}
		   
		   return "";
	   }
	   
	  /* @JavascriptInterface
	   public String getTabId(){
		   WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		   WifiInfo wInfo = wifiManager.getConnectionInfo();
		   String mac = wInfo.getMacAddress();
		   return mac;
	   }
	   */
/*	   @JavascriptInterface
	   public void createExcelSheetReport(String pat_details){
		   
		   ReportGenerator RepGen = new ReportGenerator();
		   
		   
		 //Creating workbook
			WritableWorkbook w = RepGen.createWorkbook("HiWi_Report.xls");
			WritableSheet ws = RepGen.createSheet(w, "Patient Details", 0);
			
		//Heading			
			try {
				RepGen.writeCell(0, 0, "id", true, ws);
				RepGen.writeCell(1, 0, "fname", true, ws);
				RepGen.writeCell(2, 0, "mname", true, ws);
				RepGen.writeCell(3, 0, "lname", true, ws);
				RepGen.writeCell(4, 0, "gender", true, ws);
				RepGen.writeCell(5, 0, "dob", true, ws);
				RepGen.writeCell(6, 0, "pcontactno", true, ws);
				RepGen.writeCell(7, 0, "patemail", true, ws);
				RepGen.writeCell(8, 0, "docname", true, ws);
				RepGen.writeCell(9, 0, "docemail", true, ws);
				RepGen.writeCell(10, 0, "docmob", true, ws);				
			} catch (RowsExceededException e) {
				
				e.printStackTrace();
			} catch (WriteException e) {
				
				e.printStackTrace();
			}
			
			//Data
			int k=0;
			for (String retval: pat_details.split(";", 11)){   
				try {
					RepGen.writeCell(k,1, retval, false, ws);
	                k++;
				} catch (RowsExceededException e) {
					
					e.printStackTrace();
				} catch (WriteException e) {
					
					e.printStackTrace();
				}            
					
			}
			
            try {
            	w.write();
                w.close();
            } 
            catch (WriteException e) {
                
                 e.printStackTrace();
            } catch (IOException e) {
				
				e.printStackTrace();
			}
			
	   }
	   
	   @JavascriptInterface
	   public void sendReportMail(String emailID) { 
	     	    
	         Mail m = new Mail("gmail@gmail.com", "password"); 
	    
	         String[] toArr = {emailID,"sunils9996@gmail.com"}; 
	         m.set_to(toArr); 
	         m.set_from("vishwasc20@gmail.com"); 
	         m.set_subject("This is an email sent using my Mail JavaMail wrapper from an Android device."); 
	         m.setBody("Hi,\\n"
	         		+ "\\tThis is an Email with Patient Report Attached.\\n"
	         		+ "Thank You,\\n"
	         		+ "E.T. Team\\n"); 
	    
	         try { 
	        	 File sdCard = Environment.getExternalStorageDirectory();
	             m.addAttachment(sdCard.getAbsolutePath()+"/HiWi_Report/HiWi_Report.xls"); 
	             m.send();
	          // if(m.send()) { 
	          //   Toast.makeText(MailApp.this, "Email was sent successfully.", Toast.LENGTH_LONG).show(); 
	          // } else { 
	          //   Toast.makeText(MailApp.this, "Email was not sent.", Toast.LENGTH_LONG).show(); 
	          // } 
	         } catch(Exception e) { 
	           //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show(); 
	           Log.e("MailApp", "Could not send email", e); 
	         } 
	    
	   } 	Deleted by PU 11/2/15 */
	   
}
