package kr.co.schedule;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class AlarmService extends Service {
	// 현재 요일을 뽑아낼 배열
	private String[] days = {"일", "월", "화", "수", "목", "금", "토"};
	private Calendar alarmCalendar = null;		// 알람설정시간
	private Calendar calendar = null;			// 현재시간
	private int beforeMin = 0;					// 몇분전 알림
	private String occurrence;					// 알람 발생시 저장할 날짜
	
	/** 서비스가 실행될때  */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		alarmCalendar = Calendar.getInstance();					// 알람설정시간
		calendar = Calendar.getInstance();						// 비교할 현재시간
		calendar.setTimeInMillis(System.currentTimeMillis());	// 현재시간 으로 설정
		
		// 
		SharedPreferences defaultSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		beforeMin = Integer.valueOf("-" + defaultSharedPref.getString("beforeAlarm", "10"));	// minus
		//before.
		Log.i("dservice", beforeMin + "");

		int whenDay = calendar.get(Calendar.DAY_OF_WEEK);		// 현재요일을 받아온다.
		// 시스템서비스에서 알람매니져를 얻어온다.
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		// 디비에서 알람 시간을 가져온다.
		MyDBHelper mydb = new MyDBHelper(getBaseContext());
		SQLiteDatabase db = mydb.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.query(MyDBHelper.DATABASE_TABLE, null, null, null, null, null, null);
		String day, alarmTime;
		// 알람 발생시 저장할 날짜를 오늘 날짜로
		occurrence = String.valueOf(calendar.get(Calendar.YEAR))
							+ String.valueOf(calendar.get(Calendar.MONTH))
							+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		int lastoccurrence;
		if( cursor.moveToFirst() ){	// cursor에 row가 1개 이상 있으면 
			do{
				// 인덱스
				lastoccurrence = cursor.getInt(cursor.getColumnIndex("is_alarming"));		// 이 수업의 마지막 알람 발생날짜
				day = cursor.getString(cursor.getColumnIndex("day"));						// 날짜
				alarmTime = cursor.getString(cursor.getColumnIndex("s_time"));				// 알람시간
				Log.i("dservice", "alarmTime" + cursor.getString(cursor.getColumnIndex("s_time") ));
				// 알람을 발생할지 시간을 체크하고 다음 요일을 체크해준다.
				if( checkAlarmHour(alarmTime) && day.trim().contains(days[whenDay-1]) ){
					/*
					 * 발생시간에 중복 발생을 막기 위해
					 * 마지막 발생시간이 오늘이 아니면 알람설정을 한다.
					 */
					if(lastoccurrence != Integer.valueOf(occurrence) ){
						Log.i("dservice", "now setting!");
						logLastAlarmTime(cursor.getInt(cursor.getColumnIndex("idx")));
						// 브로드케스트 리시버에 보낼 팬딩인텐트, 이전 팬딩인텐트가 있으면 취소하고 새로 실행
						Intent i = new Intent(getBaseContext(), AlarmReceiver.class);
						i.putExtra("before", beforeMin + "" );	// 몇분전하고 과목을 알려준다.

						i.putExtra("before", beforeMin + "" );	// 몇분전하고 과목을 알려준다.
						// -2를 빼면 월요일부터 0이다.
						i.putExtra("whenDay", whenDay-2 + "" );	// 통지바 클릭시 보여줄 화면의 날짜 탭순번
						i.putExtra("subject", cursor.getString(cursor.getColumnIndex("subject")) );
						PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(),
								0,  i, PendingIntent.FLAG_CANCEL_CURRENT);
						// 표준시간을 기준으로 하는 명시적인 시간에 intent를 발생, 장치를 깨움						
						am.set(AlarmManager.RTC_WAKEUP,	alarmCalendar.getTimeInMillis(), sender);	// 알람설정
						break;
					}
				}
			}while( cursor.moveToNext() );	// 다음 커서가 있으면 내용을 가져온다.
		}			
		cursor.close();
		db.close();
		Log.i("dservice", "onstartCommand");
		return 0;
	}
	

	/**
	 * 중복 알람 방지를 위해 마지막 알람 기록
	 */
	private void logLastAlarmTime(int idx) {
		// TODO Auto-generated method stub
		MyDBHelper mydb = new MyDBHelper(getBaseContext());
		SQLiteDatabase db = mydb.getWritableDatabase();
		ContentValues cv = new  ContentValues();
		cv.put("is_alarming", Integer.valueOf(occurrence));	// 오늘 날짜로 update
		db.update(MyDBHelper.DATABASE_TABLE, cv, "idx=?", new String[]{idx+"",});
		db.close();
	}


	/**
	 * 현재 시(Hour) 보다 알람(Hour)시가 더 큰지 비교한후 알람시가 
	 * 더 크면 true를 반환하고 시가 같으면 분까지 체크후 boolean값을 반환한다. 
	 * @param alaramTime
	 * 알람시간 ex) 13:23, 09:30
	 * @return
	 * 	알람시가 더 크면 true
	 */
	private boolean checkAlarmHour(String alaramTime){
		
		int alarmHour = Integer.valueOf(alaramTime.substring(0, 2));
		int alarmMin = Integer.valueOf(alaramTime.substring(3));
		
		// 설정된 시간으로 alarmCalendar를 셋팅후 알람 시간에서 몇분전 알림 빼준다.
		alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmHour);
		alarmCalendar.set(Calendar.MINUTE, alarmMin);
		alarmCalendar.add(Calendar.MINUTE, this.beforeMin);	// 분을 빼준다.

		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);	// 현재 시각 가져오기
		alarmHour =	alarmCalendar.get(Calendar.HOUR_OF_DAY);	// 알람 시각 가져오기
		Log.i("dservice", alarmHour +"====" + currentHour);
		if(alarmHour > currentHour){
			return true;
		}else if(currentHour == alarmHour){	// 현재시간과 알람시간이 같으면 분까지 체크
			alarmMin = alarmCalendar.get(Calendar.MINUTE);	// 분을 잘라낸다.
			return checkAlarmMinute(alarmMin);	// 분 체크
		}
		
		return false;
	}
	
	/**
	 * 현재분보(minute)다 알람 발생분이 더 큰지 비교
	 * @param alarmMin
	 * 	알람 분
	 * @return
	 * 	 알람분이 더크면 true
	 */
	private boolean checkAlarmMinute(int alarmMin){
		int min = calendar.get(Calendar.MINUTE);	// 현재 분 가져오기
		if(alarmMin >= min){	// 현재 분보다 크면 
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
		Log.i("dservice", "stop!");
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	

}
