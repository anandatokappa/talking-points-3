package talkingpoints.guoer;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.widget.TextView;
import android.widget.Toast;

public class Content extends GestureUI implements OnInitListener {

	private TextView text;
	// private TextView title;
	private String content;
	private static final String TAG = "MAC = ";
	private TextToSpeech mTts;
	private GestureDetector gestureScanner;
//	private static final int SWIPE_MIN_DISTANCE = 120;
//	private static final int SWIPE_MAX_OFF_PATH = 250;
//	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	
	private static final int SWIPE_MIN_DISTANCE = 10; //120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private static final int CHECK_DISTANCE =100; 
	private static final int SWIPE_MIN_DISTANCE_RIGHT_LEFT=100; //more distance require for left and right gesture 
  	private static final int CHECK_DISTANCE_2=10;
	private static boolean flag=false; 
	private static boolean flag2=false; 
	private static boolean flag3=false; 

    private static boolean flagForScrolling=false; 

    private float FirstX;
    private float FirstY;
    private float LastX;
    private float LastY;
    
    private static int count1=0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gestureScanner = new GestureDetector(this);
		setContentView(R.layout.poi_view);
		// get MAC address from picked device
		// MAC = getIntent().getStringExtra("MAC");
		content = getIntent().getStringExtra("content");

		// title = (TextView) findViewById(R.id.poi_detail);
		text = (TextView) findViewById(R.id.poi_info);

	//	count1=0;
	
		// retrieve data from server

		text.setText(content);
		// title.setText(p.getName());
		mTts = new TextToSpeech(this, this);
		// findViewById(R.id.poi_detail).setVisibility(View.VISIBLE);
		findViewById(R.id.poi_info).setVisibility(View.VISIBLE);
		setResult(Activity.RESULT_OK);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {

		int action = e.getAction();
    	//down 
		if(action == MotionEvent.ACTION_DOWN||action==MotionEvent.ACTION_MOVE)
		{	
			flagForScrolling=true;
			 

		}
        if(action == MotionEvent.ACTION_DOWN)
        {
         	FirstX=e.getX();
        	FirstY=e.getY();
        }
    	else if(action == MotionEvent.ACTION_UP)
    	{
    		LastX=e.getX();
    		LastY=e.getY();
    		
     		
    		if(FirstX>0||FirstY>0)
    		{
    			final float xD=Math.abs(FirstX-LastX);
    			final float yD=Math.abs(FirstY-LastY);
    			
    			try{
    				if(FirstX-LastX>SWIPE_MIN_DISTANCE_RIGHT_LEFT&&yD< CHECK_DISTANCE)
    				{    // this.mTts.speak("LEFT MOTION", TextToSpeech.QUEUE_FLUSH,null);
    					
    						vibrate();
    						releaseSoundEffect();
							playSound(NEXT_PAGE);
							finish();

    				}
    				else if(LastX - FirstX >SWIPE_MIN_DISTANCE_RIGHT_LEFT&& yD< CHECK_DISTANCE) 
    				{	
    					
    					vibrate();
    					this.sayPageName();

    				}
     				   //   this.mTts.speak("Right motion", TextToSpeech.QUEUE_FLUSH,null);
     				else if(FirstY - LastY > SWIPE_MIN_DISTANCE&& xD< CHECK_DISTANCE)  
     				{
//     					 // this.mTts.speak("UP Motion", TextToSpeech.QUEUE_FLUSH,null);
//     					 if(flag||flagForScrolling)
//     					 {
//     						 this.options.clear();
//     						 for(int i=0;i<BTlist.getPOInamesWithDistance().size();i++)
//     		 						this.options.add(BTlist.getPOInamesWithDistance().get(i));
//     						 
//     						 vibrate();
//     						 upMotion();
//     						flagForScrolling=false;
//     					 }
//     					
     				}
     				else if(LastY - FirstY > SWIPE_MIN_DISTANCE && xD< CHECK_DISTANCE)  
     				{	
//     					//this.mTts.speak("down motion", TextToSpeech.QUEUE_FLUSH,null);
//     					
//     					 if(flag||flagForScrolling)
//     					 {
//     						 
//     						 this.options.clear();
//     						 for(int i=0;i<BTlist.getPOInamesWithDistance().size();i++)
//     		 						this.options.add(BTlist.getPOInamesWithDistance().get(i));
//     						  
//     						 
//     						vibrate();
//     						downMotion();
//     						flagForScrolling = false; 
//     					 }
     				}//missed
     				else if(xD>CHECK_DISTANCE_2&&yD>CHECK_DISTANCE_2&&this.options.size()>0)
     				{
     					releaseSoundEffect();
						playSound(MISSED_IT);
//     					this.mTts.speak("Please move your thumb in right direction", TextToSpeech.QUEUE_FLUSH,null);
     				}
     				 

    		 

    			}
    			catch (Exception e0) {
    				// nothing
    			}
    			
    		}
    	}
		
		gestureScanner.onTouchEvent(e);
		return true;

	}
	 

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
//flag = true;
		final float xDistance = Math.abs(e1.getX() - e2.getX());
		final float yDistance = Math.abs(e1.getY() - e2.getY());
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE_RIGHT_LEFT
				&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY&&yDistance< CHECK_DISTANCE) {
			releaseSoundEffect();
			playSound(NEXT_PAGE);

			finish();
		}else if(e2.getX() - e1.getX() >SWIPE_MIN_DISTANCE_RIGHT_LEFT
				&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY&&yDistance< CHECK_DISTANCE) {
			mTts.speak(content, TextToSpeech.QUEUE_FLUSH, null);
			
		}else if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY&&xDistance< CHECK_DISTANCE) {
		 
			}else if(e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY&&xDistance< CHECK_DISTANCE) {
			}
		return false;
	}
	
	@Override
	// disable scroll gesture
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
	 
		return true;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// if we get any key, clear the Splash Screen
 
 	 
			if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
				AngleCalculator oc = new AngleCalculator(byCoordinateParser.getLatitude(), byCoordinateParser
						.getlongitude(),BTlist.LAC1,
						BTlist.LNG1);

			   		oc.getAngle();
				   
					Intent intent0 = new Intent(Content.this,POIsAhead.class);
//		 			intent0.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent0.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		 			Content.this.startActivity(intent0);
//		 			Content.this.finish();
		 			
 			}else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){

 
			}else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
		
		  
			}

		return true;// return super.onKeyDown(keyCode, event);
	}
	@Override
	// override onInit to read the content once start
	public void onInit(int status) {
		// status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
		if (status == TextToSpeech.SUCCESS) {
			// Set preferred language to US english.
			// Note that a language may not be available, and the result will
			// indicate this.
			int result = mTts.setLanguage(Locale.US);
			// Try this someday for some interesting results.
			// int result mTts.setLanguage(Locale.FRANCE);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				// Lanuage data is missing or the language is not supported.
				Log.e(TAG, "Language is not available.");
			} else
				mTts.speak(content, TextToSpeech.QUEUE_FLUSH, null);
		} else {
			// Initialization failed.
			Log.e(TAG, "Could not initialize TextToSpeech.");
		}
	}

}
