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
 *	�⺻ ���̽� Ŭ����
 */
public class BaseActivity extends Activity {
	private boolean isTwoClickBack;	// �ι� �ڷι�ư Ŭ�� ����
	public final static String APP_NAME = "";					// �� �̸�
	public final static String PUBLISH_VERSION = "ver 1.01";	// ����

    /** �ɼ� �޴� ����� */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,1,0, "�߰�");
    	menu.add(0,2,0, "�ɼ�"); 
    	//item.setIcon();
    	return true;
    }
    
    /** �ɼ� �޴� ���ÿ� ���� �ش� ó���� ���� */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	Intent intent = null;
    	switch(item.getItemId()){

			case 1:		// �ð�ǥ �߰� ��Ƽ��Ƽ
				intent = new Intent(getBaseContext(), AddActivity.class);
				startActivity(intent);
				return true;

			case 2:		// ���� ��Ƽ��Ƽ
				intent = new Intent(getBaseContext(), SettingActivity.class);
				startActivity(intent);
				return true;			
    	}
    	return false;
    }	
	
    
    /** back ��ư �ι� ������ ���� */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*
		 * back ��ư�̸� Ÿ�̸�(2��)�� �̿��Ͽ� �ٽ��ѹ� �ڷ� ���⸦ 
		 * ������ ���ø����̼��� ���� �ǵ����Ѵ�.
		 */
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (!isTwoClickBack) {
					Toast.makeText(this, "'�ڷ�' ��ư�� �ѹ� �� ������ ����˴ϴ�.",
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

	// �ڷΰ��� ���Ḧ ���� Ÿ�̸�
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
