package kr.co.schedule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 *	���� ��Ƽ��Ƽ
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

	/** ȭ���� ��ġ�ϸ� ���� ��Ƽ��Ƽ�� �̵� */
	@Override
	public boolean onTouchEvent(MotionEvent event) {	//ȭ�� ��ġ��
		// TODO Auto-generated method stub
		  if ( event.getAction() == MotionEvent.ACTION_DOWN ){
			  
			 Intent intent = null;  
			 // ���� ��Ƽ��Ƽ�� �̵�
			 intent =  new Intent(StartActivity.this, MyScheduleActivity.class);
			 
			 startActivity(intent);
			 return true;
		  }
		  
		  return super.onTouchEvent(event);
		  
	}	
	

}
