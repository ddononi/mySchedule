package kr.co.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 *	기본 베이스 클래스
 */
public class BaseActivity extends Activity {
	private boolean isTwoClickBack;	// 두번 뒤로버튼 클릭 여부
	public final static String APP_NAME = "";					// 앱 이름
	public final static String PUBLISH_VERSION = "ver 1.01";	// 버젼

    /** 옵션 메뉴 만들기 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,1,0, "추가");
    	menu.add(0,2,0, "옵션"); 
    	//item.setIcon();
    	return true;
    }
    
    /** 옵션 메뉴 선택에 따라 해당 처리를 해줌 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	Intent intent = null;
    	switch(item.getItemId()){

			case 1:		// 시간표 추가 엑티비티
				intent = new Intent(getBaseContext(), AddActivity.class);
				startActivity(intent);
				return true;

			case 2:		// 설정 엑티비티
				intent = new Intent(getBaseContext(), SettingActivity.class);
				startActivity(intent);
				return true;			
    	}
    	return false;
    }	
	
    
    /** back 버튼 두번 누르면 종료 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*
		 * back 버튼이면 타이머(2초)를 이용하여 다시한번 뒤로 가기를 
		 * 누르면 어플리케이션이 종료 되도록한다.
		 */
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (!isTwoClickBack) {
					Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.",
							Toast.LENGTH_SHORT).show();
					CntTimer timer = new CntTimer(2000, 1);
					timer.start();
				} else {
					moveTaskToBack(true);
	                finish();
					return true;
				}

			}
		}
		return false;
	}

	// 뒤로가기 종료를 위한 타이머
	class CntTimer extends CountDownTimer {
		public CntTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			isTwoClickBack = true;
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			isTwoClickBack = false;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			Log.i("Test", " isTwoClickBack " + isTwoClickBack);
		}

	}   
}
