package com.hiwi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class  ReadMailSample extends AsyncTask<String,String,Void>
{
	static String command, phoneNumber, type, priority, name, time_stamp, imei, opt1 = "Future use", opt2 = "Future use", opt3 = "Future use", fromSubString;
    Properties properties = null;
    private Session session = null;
    private Store store = null;
    private Folder inbox = null;
    static SQLiteDatabase db;
	public static boolean loogin=false;
    boolean flag = false;
	static boolean stop=true;
	public static boolean start = true;
	public static String sos1, sos2, sos3;
    
    protected Void doInBackground(String...params)
    {
		try
		{
			Log.e("ASD","ASD");
			isConnected();
		} 
		catch(Exception e)
		{	
		}
		return null;
	}
    
    public void isConnected() {
		try
		{
			//Log.e("ASD", "ASDASDASDASDASD");
			final ConnectivityManager connMgr = (ConnectivityManager) HeartbeatsActivity.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
		    //Log.e("ASD1", "ASDASDASDASDASD");
		    boolean isReachable = false;

		    if (netInfo != null && netInfo.isConnected()) {
		    	//Log.e("ASD2", "ASDASDASDASDASD");
		        // Some sort of connection is open, check if server is reachable
		        try {
		            URL url = new URL("http://www.google.com");
		            //URL url = new URL("http://10.0.2.2");
		            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
		            urlc.setRequestProperty("User-Agent", "Android Application");
		            urlc.setRequestProperty("Connection", "close");
		            urlc.setConnectTimeout(10 * 1000);
		            urlc.connect();
		            //Log.e("ASD3", "ASDASDASDASDASD");
		            isReachable = (urlc.getResponseCode() == 200);
		            if(isReachable == true){
		            	readMails();
		            }
		        } catch (IOException e) {
		            //Log.e(TAG, e.getMessage());
		        }
		    }
		    //Log.d("status","ds"+isReachable);
		}
		catch(Exception e){
			//Log.d("status",e.toString());
		}
	}
    
    ReadMailSample(SQLiteDatabase db)
    {
    	this.db = db;
    }
    
    ReadMailSample(){
    	Log.e("ASD","ASD");
    	//readMails();
    	isConnected();
    }
    
    public void readMails() 
    {
        properties = new Properties();      
        properties.setProperty("mail.host", "mail.gdv.com.au");
        Log.d("hi","comin to RS");
        properties.setProperty("mail.port", "993");
        properties.setProperty("mail.transport.protocol", "imaps");
        stop=true;
        loogin=false;
        while(stop)
        {
        	final String userName =HeartbeatsActivity.emaill+"@gdv.com.au";					
        	final String password =HeartbeatsActivity.emaill;	
        	//final String userName =HeartbeatsActivity.mail+"@gdv.com.au";					
        	//final String password =HeartbeatsActivity.mail;
            session = Session.getInstance(properties,new javax.mail.Authenticator() 
            			{
                        	protected PasswordAuthentication getPasswordAuthentication() 
                        	{
                        		return new PasswordAuthentication(userName, password);
                        	}
            			});
	        try 
	        {
	            store = session.getStore("imaps");	
	            store.connect();
	            inbox = store.getFolder("INBOX");			// ACCESS THE INBOX OF THE RECEPIENT'S EMAIL ID
	            inbox.open(Folder.READ_WRITE);				// OPEN THE INBOX IN READ-WRITE MODE
	            Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
	            System.out.println("Number of mails in Anand = " + messages.length);
	            for (int i = 0; i < messages.length; i++) 
	            {
	                Message message = messages[i];
	                Address[] from = message.getFrom();
	                String from1 = from[0].toString();	
	                if(from1.contains("<"))
	                {
		                int start = from1.indexOf("<");
		                int end = from1.indexOf(">");
		                fromSubString = from1.substring(start+1,end); 	// RETRIEVE THE SENDER'S EMAIL ID
	                }
	                else
	                {
	                	fromSubString = from1;
	                }
	                System.out.println("1"+fromSubString);
	          //    if(fromSubString.equals(cloud))
	              //  {	
	                	String[] subject = message.getSubject().split(","); // SPLIT THE SUBJECT
	                	System.out.println("2"+subject[1]);
	                	System.out.println("hi");
	                	type = subject[0];									// STORE THE DETAILS IN RESPECTIVE VARIABLES
	                	phoneNumber = subject[1];
	                	name = subject[2];
	                	System.out.println(subject[2]);
	                	System.out.println("hi");
		                processMessageBody(message);
		                System.out.println("--------------------------------");
	            //    }
	            }
	            inbox.close(true);
	            store.close();
	        }
	        catch (NoSuchProviderException e) 
	        {
	            e.printStackTrace();
	        }
	        catch (MessagingException e) 
	        {
	            e.printStackTrace();
	        }
        }
    }

    public void processMessageBody(Message message) 
    {
        try 
        {
            Object content = message.getContent();
           if (content instanceof Multipart) 
           {		
                Multipart multiPart = (Multipart) content;
                procesMultiPart(multiPart);
            }
           else if (content instanceof String) 
           {
                System.out.println("Content = "+content);
                processSinglepart(content.toString());
            } 
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        catch (MessagingException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void processSinglepart(String content)
    {
    	String[] body = content.split(",");
    	 String boddy=content.toString();
		  if(boddy.contains("DONE"))
		  {
			  loogin=true;
			  stop=false;
			  start = false;
		  }
		  else if(boddy.contains("USER")||boddy.contains("user")||boddy.contains("User"))
		  {
			  loogin=true;
			  stop=false;
			  start = false;
			  sos1 = body[1].substring(4);
			 // Toast.makeText(context, "your device not support BLE!", Toast.LENGTH_SHORT).show();
			  System.out.println("sos no is"+sos1);
			  sos2 = body[2].substring(4);
			  sos3 = body[3].substring(4);
		  }
		  else if(boddy.equals("404"))
		  {
			  //Toast.makeText(context, "your device not support BLE!", Toast.LENGTH_SHORT).show();
			  loogin=false;
			  stop=false;
		  }
    
    }

    public void procesMultiPart(Multipart content) 
    {
        try
        {
            BodyPart bodyPart = content.getBodyPart(0);				// RETRIEVE THE CONTENTS FROM THE BODY
            Object o;
            o = bodyPart.getContent();
            if (o instanceof String)
            {
            	System.out.println("Content Multipart= "+o.toString());
            	System.out.println("11");
            	processSinglepart(o.toString());
            }
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        catch (MessagingException e) 
        {
            e.printStackTrace();
        }
    }
}