package kr.co.schedule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 *	시작 엑티비티
 */
public class StartActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        /*
        DDLOCK lock = DDLOCK.getInstance();
        if( !lock.lockCheck(2011, 11, 7) ){
        	finish();
        }
        */
    }

	/** 화면을 터치하면 다음 엑티비티로 이동 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {	//화면 터치시
		// TODO Auto-generated method stub
		  if ( event.getAction() == MotionEvent.ACTION_DOWN ){
			  
			 Intent intent = null;  
			 // 메인 엑티비티로 이동
			 intent =  new Intent(StartActivity.this, MyScheduleActivity.class);
			 
			 startActivity(intent);
			 return true;
		  }
		  
		  return super.onTouchEvent(event);
		  
	}	
	

}
