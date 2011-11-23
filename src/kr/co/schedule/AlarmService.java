package kr.co.schedule;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class AlarmService extends Service {
	// ���� ������ �̾Ƴ� �迭
	private String[] days = {"��", "��", "ȭ", "��", "��", "��", "��"};
	private Calendar alarmCalendar = null;		// �˶������ð�
	private Calendar calendar = null;			// ����ð�
	private long alarmTime;
	private int beforeMin = 0;					// ����� �˸�
	
	/** ���񽺰� ����ɶ�  */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		alarmCalendar = Calendar.getInstance();					// �˶������ð�
		calendar = Calendar.getInstance();						// ���� ����ð�
		calendar.setTimeInMillis(System.currentTimeMillis());	// ����ð� ���� ����
		
		// 
		SharedPreferences defaultSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		beforeMin = Integer.valueOf("-" + defaultSharedPref.getString("beforeAlarm", "10"));	// minus
		//before.
		Log.i("dservice", beforeMin + "");
		
		
		int whenDay = calendar.get(Calendar.DAY_OF_WEEK);		// ��������� �޾ƿ´�.
		// �ý��ۼ��񽺿��� �˶��Ŵ����� ���´�.
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		// ��ε��ɽ�Ʈ ���ù��� ���� �ҵ�����Ʈ, ���� �ҵ�����Ʈ�� ������ ����ϰ� ���� ����
		PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),
				0, new Intent(getBaseContext(), AlarmReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT);
		// ��񿡼� �˶� �ð��� �����´�.
		MyDBHelper mydb = new MyDBHelper(getBaseContext());
		SQLiteDatabase db = mydb.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.query(MyDBHelper.DATABASE_TABLE, null, null, null, null, null, null);
		String day, alarmTime;
		if( cursor.moveToFirst() ){	// cursor�� row�� 1�� �̻� ������ 
			do{
				day = cursor.getString(cursor.getColumnIndex("day"));			// ��¥
				alarmTime = cursor.getString(cursor.getColumnIndex("s_time"));	// �˶��ð�
				Log.i("dservice", "alarmTime" + cursor.getString(cursor.getColumnIndex("s_time") ));
				// �˶��� �߻����� �ð��� üũ�ϰ� ���� ������ üũ���ش�.
				if( checkAlarmHour(alarmTime) && day.trim().contains(days[whenDay-1]) ){
					Log.i("dservice", "now setting!");
					// ǥ�ؽð��� �������� �ϴ� ������� �ð��� intent�� �߻�, ��ġ�� ����
					am.set(AlarmManager.RTC_WAKEUP,	alarmCalendar.getTimeInMillis(), sender);	// �˶�����
					break;
				}
			}while( cursor.moveToNext() );	// ���� Ŀ���� ������ ������ �����´�.
		}			

		Log.i("dservice", "onstartCommand");
		return 0;
	}
	

	/**
	 * ���� ��(Hour) ���� �˶�(Hour)�ð� �� ū�� ������ �˶��ð� 
	 * �� ũ�� true�� ��ȯ�ϰ� �ð� ������ �б��� üũ�� boolean���� ��ȯ�Ѵ�. 
	 * @param alaramTime
	 * �˶��ð� ex) 13:23, 09:30
	 * @return
	 * 	�˶��ð� �� ũ�� true
	 */
	private boolean checkAlarmHour(String alaramTime){
		
		int alarmHour = Integer.valueOf(alaramTime.substring(0, 2));
		int alarmMin = Integer.valueOf(alaramTime.substring(3));
		
		// ������ �ð����� alarmCalendar�� ������ �˶� �ð����� ����� �˸� ���ش�.
		alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmHour);
		alarmCalendar.set(Calendar.MINUTE, alarmMin);
		alarmCalendar.add(Calendar.MINUTE, this.beforeMin);	// ���� ���ش�.

		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);	// ���� �ð� ��������
		alarmHour =	alarmCalendar.get(Calendar.HOUR_OF_DAY);	// �˶� �ð� ��������
		Log.i("dservice", alarmHour +"====" + currentHour);
		if(alarmHour > currentHour){
			return true;
		}else if(currentHour == alarmHour){	// ����ð��� �˶��ð��� ������ �б��� üũ
			alarmMin = alarmCalendar.get(Calendar.MINUTE);	// ���� �߶󳽴�.
			return checkAlarmMinute(alarmMin);	// �� üũ
		}
		
		return false;
	}
	
	/**
	 * ����к�(minute)�� �˶� �߻����� �� ū�� ��
	 * @param alarmMin
	 * 	�˶� ��
	 * @return
	 * 	 �˶����� ��ũ�� true
	 */
	private boolean checkAlarmMinute(int alarmMin){
		int min = calendar.get(Calendar.MINUTE);	// ���� �� ��������
		if(alarmMin >= min){	// ���� �к��� ũ�� 
			return true;
		}
		
		return false;
	}	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("dservice", "stop!!!!!");
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	

}
