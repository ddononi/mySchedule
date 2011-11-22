package kr.co.schedule;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class AlarmService extends Service {
	private String[] days = {"��", "��", "ȭ", "��", "��", "��", "��"};
	private Calendar calendar = null;
	private long alarmTime;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		//calendar.add(Calendar.SECOND, 10);

		int whenDay = calendar.get(Calendar.DAY_OF_WEEK);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		// ��ε��ɽ�Ʈ ���ù��� ���� �ҵ�����Ʈ
		PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),
				0, new Intent(getBaseContext(), AlarmReceiver.class), 0);
		// ǥ�ؽð��� �������� �ϴ� ������� �ð��� intent�� �߻�, ��ġ�� ����
		MyDBHelper mydb = new MyDBHelper(getBaseContext());
		SQLiteDatabase db = mydb.getReadableDatabase();
		Cursor cursor = null;
		
		cursor = db.query(MyDBHelper.DATABASE_TABLE, null, null, null, null, null, "order_num asc");
		String day, alarmTime;
		if( cursor.moveToFirst() ){	// cursor�� row�� 1�� �̻� ������ 
			do{
				Log.i("service", cursor.getString(cursor.getColumnIndex("s_time") ));
				day = cursor.getString(cursor.getColumnIndex("day"));
				alarmTime = cursor.getString(cursor.getColumnIndex("s_time"));
				Log.i("service", "day-->"+  day);
				Log.i("service", "whenDay-->"+  days[whenDay-1]);
				if( /* day.trim() days[whenDay-1] && */ checkAlarmHour(alarmTime) ){
					setAlarmTime(alarmTime);	
					Log.i("service", "now setting!");
					am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
					break;
				}
				
				
				
			}while( cursor.moveToNext() );	// ���� Ŀ���� ������ ������ �����´�.
		}			
		
		//am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		Log.i("service", "onstartCommand");
		return 0;
	}
	
	private void setAlarmTime(String alaramTime) {
		// TODO Auto-generated method stub
		int alarmHour = Integer.valueOf(alaramTime.substring(0, 2));
		int alarmMin = Integer.valueOf(alaramTime.substring(3));
		calendar.set(Calendar.HOUR_OF_DAY, alarmHour);	// �˶��ð� �� ����
		calendar.set(Calendar.MINUTE, alarmMin);	// �˶��ð� �� ����
		Log.i("service", calendar.get(Calendar.HOUR_OF_DAY) +":"+ calendar.get(Calendar.MINUTE));
	}

	private boolean checkAlarmHour(String alaramTime){
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);	// ���� �ð� ��������
		int alarmHour = Integer.valueOf(alaramTime.substring(0, 2));
		Log.i("service", alarmHour +"zzz" + currentHour);
		if(alarmHour > currentHour){
			return true;
		}else if(currentHour == alarmHour){	// ����ð��� �˶��ð��� ������ �б��� üũ
			int alarmMin = Integer.valueOf(alaramTime.substring(3));
			return checkAlarmMinute(alarmMin);
		}
		
		return false;
	}
	
	private boolean checkAlarmMinute(int alarmMin){
		int min = calendar.get(Calendar.MINUTE);	// ���� �� ��������
		if(alarmMin > min){	// ���� �к��� ũ�� 
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
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}	

	

}
