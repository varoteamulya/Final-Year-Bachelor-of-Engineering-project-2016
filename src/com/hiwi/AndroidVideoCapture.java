package com.hiwi;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import org.opencv.javacv.facerecognition.R;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AndroidVideoCapture extends Activity{
	
	private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;
    public static boolean timeflag=true;
	Button myButton;
	SurfaceHolder surfaceHolder;
	boolean recording;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        recording = false;
       
        setContentView(R.layout.main);
        
        //Get Camera for preview
        myCamera = getCameraInstance();
        if(myCamera == null){
        	Toast.makeText(AndroidVideoCapture.this, 
        			"Fail to get Camera", 
        			Toast.LENGTH_LONG).show();
        }

        myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
        FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
        myCameraPreview.addView(myCameraSurfaceView);
        
        myButton = (Button)findViewById(R.id.mybutton);
        myButton.setOnClickListener(myButtonOnClickListener);
//        if(!prepareMediaRecorder()){
//        	Toast.makeText(AndroidVideoCapture.this, 
//        			"Fail in prepareMediaRecorder()!\n - Ended -", 
//        			Toast.LENGTH_LONG).show();
//        	//finish();
//        }
        
        //releaseCamera();
		
    
//        mediaRecorder.start();
        new CountDownTimer(16000, 1000) {

            public void onTick(long millisUntilFinished) {
            	
            	System.out.println(""+millisUntilFinished);
            	if(millisUntilFinished < 14000 && timeflag){
            		myButton.performClick();
            		timeflag = false;
            	}
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
            	System.out.println("finished");
            	try{
            		myButton.performClick();
            		finish();
//            		SendMail sMail = new SendMail("Fall Detection Video", "Vide0", 2);
            	}catch(Exception e){
            		System.out.println(e);
            	}
            	 //mediaRecorder.stop(); 
               // mTextField.setText("done!");
            	
            }
         }.start();
   }
    
    Button.OnClickListener myButtonOnClickListener
    = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(recording){
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                myButton.setText("REC");
                //Exit after saved
                //finish();
			}else{
				
				//Release Camera before MediaRecorder start
				releaseCamera();
				
		        if(!prepareMediaRecorder()){
		        	Toast.makeText(AndroidVideoCapture.this, 
		        			"Fail in prepareMediaRecorder()!\n - Ended -", 
		        			Toast.LENGTH_LONG).show();
		        	//finish();
		        }
				try{
					mediaRecorder.start();
				}catch(Exception e){
					System.out.println(e);
				}
				recording = true;
				myButton.setText("STOP");
			}
		}};
    
    private Camera getCameraInstance(){
		// TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
	}
	
	private boolean prepareMediaRecorder(){
	    myCamera = getCameraInstance();
	    mediaRecorder = new MediaRecorder();

	    myCamera.unlock();
	    mediaRecorder.setCamera(myCamera);

	    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

	    mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));

	    mediaRecorder.setOutputFile("/sdcard/Fall Detection Video.mp4");
        mediaRecorder.setMaxDuration(6000000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(250000000); // Set max file size 5M

	    mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

	    try {
	        mediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        releaseMediaRecorder();
	        return false;
	    } catch (IOException e) {
	        releaseMediaRecorder();
	        return false;
	    }
	    return true;
		
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (myCamera != null){
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }
	
	public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

		private SurfaceHolder mHolder;
	    private Camera mCamera;
		
		public MyCameraSurfaceView(Context context, Camera camera) {
	        super(context);
	        mCamera = camera;

	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mHolder = getHolder();
	        mHolder.addCallback(this);
	        // deprecated setting, but required on Android versions prior to 3.0
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int weight,
				int height) {
	        // If your preview can change or rotate, take care of those events here.
	        // Make sure to stop the preview before resizing or reformatting it.

	        if (mHolder.getSurface() == null){
	          // preview surface does not exist
	          return;
	        }

	        // stop preview before making changes
	        try {
	            mCamera.stopPreview();
	        } catch (Exception e){
	          // ignore: tried to stop a non-existent preview
	        }

	        // make any resize, rotate or reformatting changes here

	        // start preview with new settings
	        try {
	            mCamera.setPreviewDisplay(mHolder);
	            mCamera.startPreview();

	        } catch (Exception e){
	        }
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			// The Surface has been created, now tell the camera where to draw the preview.
	        try {
	            mCamera.setPreviewDisplay(holder);
	            mCamera.startPreview();
	        } catch (IOException e) {
	        }
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}
	}
}