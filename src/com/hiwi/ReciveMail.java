package com.hiwi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.util.Log;
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
import org.opencv.javacv.facerecognition.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.sax.StartElementListener;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
 

public class ReciveMail extends Activity implements TextToSpeech.OnInitListener{
    private static final int MY_DATA_CHECK_CODE = 1234;
    public static Context c,b;
    public static TextToSpeech tts;
    public static Intent serviceIntent;
    private static int myNotificationId;
    static Context acn;
    public static boolean start = true;
    public static Timer timer;
   
    public static class  ReadMailSample extends AsyncTask<String,String,Void>  implements TextToSpeech.OnInitListener {
        Message message;
        static String command, phoneNumber, type, priority, name, time_stamp, imei, opt1, opt2, opt3, fromSubString;
        Properties properties = null;
        private Session session = null;
        private Store store = null;
        private Folder inbox = null;
        public static TextToSpeech tt;
        String userName = null;                   // PROVIDE RECEPIENT EMAIL ID
        String password = null;                            // PROVIDE RECEPIENT PASSWORD
        static SQLiteDatabase db;
        boolean flag=false;
        
        //private Bundle savedInstanceState;


        protected Void doInBackground(String...params){                     // SEPARATE THREAD TO RUN IN THE BACKGROUND
            try{
            	
                isConnected();
            } 
            catch(Exception e){
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.INFO, "an exception was thrown", e);
            }
            return null;
        }



        ReadMailSample(SQLiteDatabase db){
        	this.db = db;
        }

        ReadMailSample(){ }

        ReadMailSample(Context cn){   
        	acn=cn;
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
    		            Log.e("Internet", e.getMessage());
    		            
    		        }
    		    }
    		    //Log.d("status","ds"+isReachable);
    		}
    		catch(Exception e){
    			Log.d("status",e.toString());
    		}
    	}
        
        @Override
        protected void onPreExecute() {
    	     super.onPreExecute();
    	     //tt = new TextToSpeech(c, ReadMailSample.this); // i think your Context is "acn", make sure its not null when onPreExecute() is called
    	}

        @Override
        protected void onProgressUpdate(String... values) {
            try {
            	showNotification(); 
            }
            catch(Exception e){
                e.printStackTrace();
            }           
        }
       
        public void showNotification() {
            PendingIntent notificationIntent = preparePendingIntent();
            Notification notification = createBasicNotification(notificationIntent);
            displayNotification(notification);
        }


        @SuppressLint("InlinedApi")
        private PendingIntent preparePendingIntent() {
            Intent intent=new Intent(c,MainActivity.class); 
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            return pendingIntent;
        }

        private Notification createBasicNotification(PendingIntent notificationIntent) {
            NotificationCompat.Builder builder = new Builder(c);
            long[] vibrationPattern = {0, 200, 800, 200, 600, 200, 400, 200, 200, 200, 100, 200, 50, 200, 50, 200, 50, 200, 50, 200};

            Notification notification = builder
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Medication Reminder")
                    .setContentText(command)
                    .setAutoCancel(true)
                    .setContentIntent(notificationIntent)
                    .setWhen(Calendar.getInstance().getTimeInMillis() + 1000*60+60)
                    .setVibrate(vibrationPattern)

                    .build();

            return notification;
        }


        private void displayNotification(Notification notification) {
        	NotificationManager notificationManager = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
            myNotificationId=(int) System.currentTimeMillis();
            notificationManager.notify(myNotificationId , notification);
        }

        public void readMails() throws IOException{
      		userName = HeartbeatsActivity.emaill+"@gdv.com.au";					// PROVIDE RECEPIENT EMAIL ID
			password = HeartbeatsActivity.emaill ;	
			System.out.println("Username "+userName);
			System.out.println("PA"+password);
			System.out.println("READMAIL hi");    
//	          properties = new Properties();
//
//	          // SETTING UP AN IMAP SERVER TO ACCESS THE RECEPIENT'S EMAIL
//	          properties.setProperty("mail.host", "mail.gdv.com.au");
//	          properties.setProperty("mail.port", "993");
//	          properties.setProperty("mail.transport.protocol", "imaps");
			properties = new Properties();      
			properties.setProperty("mail.host", "mail.gdv.com.au");
			properties.setProperty("mail.port", "993");
			properties.setProperty("mail.transport.protocol", "imaps");
			// while(true){// CONTINUOUSLY MONITOR INCOMING MAIL'S
			startTimer();
        }
      
        public void startTimer(){
            timer = new Timer();
        	timer.schedule(new TimerTask() {
        		public void run() {
        			// AUTHENTICATE AND GET AN INSTANCE OF THE SESSION FROM THE SERVER
        			session = Session.getInstance(properties,new javax.mail.Authenticator() {
        				protected PasswordAuthentication getPasswordAuthentication() {
        					return new PasswordAuthentication(userName, password);
        				}
        			});
        			try {
			            store = session.getStore("imaps");    
			            store.connect();
			            inbox = store.getFolder("INBOX");         // ACCESS THE INBOX OF THE RECEPIENT'S EMAIL ID
			            inbox.open(Folder.READ_WRITE);                // OPEN THE INBOX IN READ-WRITE MODE
			            Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));     //SEARCH INBOX FOR ANY UNREAD MAILS
			            System.out.println("Number of mails = " + messages.length);
			            for (int i = 0; i < messages.length; i++) {               // PROCESS ALL THE UNREAD MAILS	                        
			          	  	message = messages[i];
			                Address[] from = message.getFrom();
			                String from1 = from[0].toString();
			                System.out.println(from1);
			                if(from1.contains("<")){
			                    int start = from1.indexOf("<");
			                    int end = from1.indexOf(">");
			                    fromSubString = from1.substring(start+1,end);   // RETRIEVE THE SENDER'S EMAIL ID
			                } else{
			                    fromSubString = from1;
			                }
			                System.out.println(fromSubString);
			                //if(fromSubString.equals(cloud)){      // CHECK WHETHER THE MAIL IS FROM THE CLOUD
			                String[] subject = message.getSubject().split(","); // SPLIT THE SUBJECT
			                System.out.println("hi");
			                type = subject[0];                                  // STORE THE DETAILS IN RESPECTIVE VARIABLES
			                phoneNumber =subject[1];
			                name = subject[2];
			                System.out.println(type);
			                System.out.println(phoneNumber);
			                System.out.println(name);
			                //String body=message.getContentType().toString();
			                // System.out.print(body);
			                processMessageBody(message);
			                //System.out.println("--------------------------------");
			            }
			            inbox.close(true);
			            store.close();
        			}
        			catch (NoSuchProviderException e) {
        				e.printStackTrace();
        			}
        			catch (MessagingException e) {
        				e.printStackTrace();
        			}  
        		}
	  		}, 0, 180000);
        }

        public void processMessageBody(Message message) {
        	try {

        		Object content = message.getContent();
        		String msg=content.toString();

        		System.out.println(msg);
        		if (content instanceof Multipart) {                       // IF MAIL HAS MULTIPART MESSAGE
        			Multipart multiPart = (Multipart) content;
        			procesMultiPart(multiPart);
        		}
        		else{
        			System.out.println("Content = "+content);
        			processSinglepart(content.toString());
        		} 
        	}
        	catch (IOException e) {
        		e.printStackTrace();
        	}
        	catch (MessagingException e) {
        		e.printStackTrace();
        	}
        }

        public void processSinglepart(String content){
        	if(!(content.contains("User") || content.contains("user") || content.contains("USER"))){
        		String[] body = content.split(",");         // SPLIT THE CONTENTS OF THE BODY
        		System.out.println('1');

        		time_stamp = body[0];                             // STORE THE DETAILS IN RESPECTIVE VARIABLES
        		command = body[3];
        		System.out.println(time_stamp);
        		speakWords("Pass the String here"); 
        		publishProgress(command);
        	}
        }
      

        private void speakWords(String speech) {
    	    //speak straight away
    	    //myTTS.setLanguage(Locale.US);
    	    System.out.println(speech + " TTSTTTS");
    	    tts.speak(command, TextToSpeech.LANG_COUNTRY_AVAILABLE, null);
    	}

      
 
        public void procesMultiPart(Multipart content) {
        	try {
        		BodyPart bodyPart = content.getBodyPart(0);               // RETRIEVE THE CONTENTS FROM THE BODY
        		Object o;

        		o = bodyPart.getContent();
        		if (o instanceof String) {
        			System.out.println("Content Multipart= "+o.toString());
        			processSinglepart(o.toString());
        		}
        	} 
        	catch (IOException e) {
        		e.printStackTrace();
        	} 
        	catch (MessagingException e) {
        		e.printStackTrace();
        	}
        }

        private void speakOut()	{
        	tts.speak("hello", TextToSpeech.QUEUE_FLUSH, null);
        }

        @Override
        public void onInit(int status) {
        	// TODO Auto-generated method stub
		
        }
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        c = this.getApplicationContext();
        acn=this.getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Added VU 13-04-2016
      	registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
      	registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
      	
        tts = new TextToSpeech(this, this);
        Intent hbt = new Intent(this,com.hiwi.MainActivity1.class);
    	startActivity(hbt);
    }


    @Override
    public void onDestroy() {
    	// Don't forget to shutdown tts!
    	if (tts != null) {
    		tts.stop();
    		tts.shutdown();
    	}
    	super.onDestroy();
    }

    @Override
    public void onInit(int status) {
    	if (status == TextToSpeech.SUCCESS) {
    		int result = tts.setLanguage(Locale.US);
    		if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
    			Log.e("TTS", "This Language is not supported");
    		} else {
    			speakOut();
    			ReadMailSample rd = new ReadMailSample();
    			rd.execute();
    		}

    	} else {
    		Log.e("TTS", "Initilization Failed!");
    	}
    }

	public static void speakOut() {
		tts.speak("Cloud Authentication is complete", TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public static void speakOut1() {
		tts.speak("Please Turnoff the device", TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public static void speakOut2() {
		tts.speak("Please Remove the device from your finger", TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public static void speakOut3() {
		tts.speak("Please Remove the device from your wrist", TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public static void speakOut4() {
		tts.speak("Please step down from the scale", TextToSpeech.QUEUE_FLUSH, null);
	}
	
	BroadcastReceiver mybroadcast = new BroadcastReceiver() {    
        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("[BroadcastReceiver]", "MyReceiver");

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.i("[BroadcastReceiver]", "Screen ON");
                ReadMailSample readMailSample = new ReadMailSample();
                readMailSample.execute();
            }
            else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i("[BroadcastReceiver]", "Screen OFF");
                timer.cancel();
            }

        }
    };
}