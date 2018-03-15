package com.hiwi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class SendMail {
	
	private String recepientEmailId, senderEmailId, senderPassword, subject, body;
	private static final String    TAG                 = "OCVSample::Activity";
	private int flag_img;
	String url;
	public SendMail(String subject, String body,int flag) {
		this.recepientEmailId = "gdvcloud@gdv.com.au";
		this.senderEmailId = HeartbeatsActivity.emaill+"@gdv.com.au";
		this.senderPassword = HeartbeatsActivity.emaill;
		this.subject = subject;
		this.body = body;
		this.flag_img=flag;
		System.out.println("EMAIL BODY = "+body);
		System.out.println("EMAIL  = "+recepientEmailId);
		new Thread(){				// CREATE A NEW THREAD FOR SENDING EMAIL BACK TO THE CLOUD
			@Override
		    public void run() {
				isConnected();
			}
		}.start();
	}
	
	/*SendMail()
	{
		this.recepientEmailId = "gdvcloud@gdv.com.au";
		this.senderEmailId = "14dda9a8f3e1@gdv.com.au";
		this.senderPassword = "14dda9a8f3e1";
		this.subject = "anand";
		this.body = "PEKKA";
		this.flag_img = 1;
		System.out.println("EMAIL BODY = "+body);
		System.out.println("EMAIL  = "+recepientEmailId);
		new Thread(){				// CREATE A NEW THREAD FOR SENDING EMAIL BACK TO THE CLOUD
			@Override
		    public void run() {
				sendEmail();
			}
		}.start();
	}*/
	
	
	public void isConnected() {
		try
		{
			final ConnectivityManager connMgr = (ConnectivityManager) HeartbeatsActivity.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
		    boolean isReachable = false;

		    if (netInfo != null && netInfo.isConnected()) {
		        // Some sort of connection is open, check if server is reachable
		        try {
		            URL url = new URL("http://www.google.com");
		            //URL url = new URL("http://10.0.2.2");
		            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
		            urlc.setRequestProperty("User-Agent", "Android Application");
		            urlc.setRequestProperty("Connection", "close");
		            urlc.setConnectTimeout(10 * 1000);
		            urlc.connect();
		            isReachable = (urlc.getResponseCode() == 200);
		            if(isReachable == true){
		            	sendEmail();
		            	String cq = "Select * from PendingMail";			
		                Cursor c = HeartbeatsActivity.db.rawQuery(cq, null);
		                int count = c.getCount();
		                for(int i = 0;i < count; i++) {
		                	if(c.moveToNext()){
		                		subject = c.getString(0);
		                		body = c.getString(1);
		                		sendEmail();
		                		HeartbeatsActivity.db.execSQL("DELETE FROM PendingMail where subject = '"+subject+"';");
		                	}
		                }
		            } else {
		            	HeartbeatsActivity.db.execSQL("INSERT INTO PendingMail values('"+subject+"','"+body+"');");
		            }
		            	
		        } catch (IOException e) {
		            Log.e(TAG, e.getMessage());
		        }
		    } else {
		    	HeartbeatsActivity.db.execSQL("INSERT INTO PendingMail values('"+subject+"','"+body+"');");
		    }
		}
		catch(Exception e){
			Log.d("status",e.toString());
		}
	}
	
	public void sendEmail(){
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "mail.gdv.com.au");
		props.put("mail.smtp.port", "25"); 			//AUTHENTICATE AND GET A SESSION OF SERVER
		System.out.println("Username "+senderEmailId);
		System.out.println("PA"+senderPassword);
		Session session = Session.getInstance(props,new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication() 
			{
				return new PasswordAuthentication(senderEmailId, senderPassword);
			}
		});
 
		try 
		{	// COMPOSE A NEW MAIL
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmailId));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recepientEmailId));
			
			if(flag_img==1)
			{	
				String mac=HeartbeatsActivity.macAddr;
				message.setSubject(mac);
				MimeBodyPart p1 = new MimeBodyPart();
				p1.setText(mac);
				MimeBodyPart p2 = new MimeBodyPart();
				//String imgpath=FdActivity.imagepath;
				int id=JSInterface.id_for_img;
				FileDataSource fdsa = new FileDataSource(Environment.getExternalStorageDirectory().getPath()+"/GDVFaces/"+id+".jpg");
				//FileDataSource fdsa = new FileDataSource(imgpath+"/"+id+"-3.jpg");
				p2.setDataHandler(new DataHandler(fdsa));
				p2.setFileName(fdsa.getName());
				Multipart mp = new MimeMultipart();
				mp.addBodyPart(p1);
				mp.addBodyPart(p2);
				message.setContent(mp);	
			}
			else if(flag_img==2)
			{	
				//String mac=HeartbeatsActivity.macAddr;
				message.setSubject(subject);
				MimeBodyPart p1 = new MimeBodyPart();
				p1.setText(body);
				MimeBodyPart p2 = new MimeBodyPart();
				//String imgpath=FdActivity.imagepath;
				//int id=JSInterface.id_for_img;
				FileDataSource fdsa = new FileDataSource(Environment.getExternalStorageDirectory().getPath()+"/Fall Detection Video.mp4");
				//FileDataSource fdsa = new FileDataSource(imgpath+"/"+id+"-3.jpg");
				p2.setDataHandler(new DataHandler(fdsa));
				p2.setFileName(fdsa.getName());
				Multipart mp = new MimeMultipart();
				mp.addBodyPart(p1);
				mp.addBodyPart(p2);
				message.setContent(mp);	
				
			}
			else
			{
				message.setSubject(subject);
				message.setText(body);
				System.out.println(body);
			}
			try
			{			
				Transport.send(message);			// SEND THE EMAIL
			} 
			catch(SendFailedException sfe)
			{
				//by anand
				//Toast.makeText(MainActivity.context, ""+sfe.toString(), Toast.LENGTH_LONG).show();
				Log.i(TAG,sfe.toString());			
				}
			System.out.println("DONE");
		}
		catch (MessagingException e) 
		{
			throw new RuntimeException(e);
		}
		HttpClient client = new DefaultHttpClient();
		if(subject.equals(HeartbeatsActivity.macAddr)){
		    url = "http://ec2-54-153-209-196.ap-southeast-2.compute.amazonaws.com/authentication/authentication.php";
		    try {
				Log.e("ASD","ASDASDASD");
			  client.execute(new HttpGet(url));
			} catch(IOException e) {
			  System.out.println(e);
			}
		}else if(subject.contains("SOS")){
			url = "http://ec2-54-153-209-196.ap-southeast-2.compute.amazonaws.com/authentication/sos.php";
			try {
				Log.e("ASD","ASDASDASD");
			  client.execute(new HttpGet(url));
			} catch(IOException e) {
			  System.out.println(e);
			}
		}else if(subject.contains("Fall Detected")){
			url = "http://ec2-54-153-209-196.ap-southeast-2.compute.amazonaws.com/authentication/fall.php";
			try {
				Log.e("ASD","ASDASDASD");
			  client.execute(new HttpGet(url));
			} catch(IOException e) {
			  System.out.println(e);
			}
		}
		
	}
}


/*
 * 
 * 
 * senderEmailId = gdvtest1@gdv.com.au
 * senderPassword = gdv2010Test@
 * recepientEmailId = gdvcloud@gdv.com.au
 * gdv123interstellar@
 * webmail.au.syrahost.com/
 * 
 * 
 */