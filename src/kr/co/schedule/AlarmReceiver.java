package kr.co.schedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * AlarmService 의 알람발생시 처리 리시버 클래스
 */
public class AlarmReceiver extends BroadcastReceiver {
	private int YOURAPP_NOTIFICATION_ID = 1;	// 앱 아이디값

	@Override
	public void onReceive(Context context, Intent intent) {
	//	Toast.makeText(context, "알림 내용음 적을수있다", Toast.LENGTH_SHORT).show();
		Log.i("service", "broadcast catch!!");
		// 진동설정
		/*
		SharedPreferences ps = PreferenceManager.getDefaultSharedPreferences(context);
		if(ps.getBoolean("vibration", true)){
			Vibrator m_clsVibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
			m_clsVibrator.vibrate(1000);
		}
		*/
		
		//if(ps.getBoolean("sound", true)){
			// 알림음

		// 사운드풀 생성하기
		// ** prameters **
		// 1: 동시출력 가능 스트림수
		// 2: AudioManager에 정의된 스트림 타입
		// 3: sample rate 컨버터의 품질. default로 0을 줌
	//	SoundPool sound_pool = new  SoundPool(5, AudioManager.STREAM_MUSIC, 0);

		// 사운드풀 로드하기
		// ** prameters **
		// 1: context
		// 2: resource
		// 3: priority
	//	int sound_correct = sound_pool.load(context.getApplicationContext(), R.raw.sound, 1);

		// 미디어 재생하기
		// ** prameters **
		// 1: 리소스 식별 
		// 2-3: 소리크기 
		// 4: 우선순위
		// 5: 파라미터 반복정보(0:반복x, 1:1번반복(총2번), -1:무한반복)
		// 6: 재생속도(1:1x, 2:2x)
	//	sound_pool.play(sound_correct, 1.0f, 1.0f, 1, 3, 1.0f);
		//}
		
		// 알림설정
		showNotification(context, R.drawable.alarm);
	}

	/**
	 *	상태바에 알람을 알리고 확인시 MyScheduleActivity로 이동시킨다.
	 * @param context
	 * @param statusBarIconID
	 * 		상태바에 나타낼 아이콘
	 */
	private void showNotification(Context context, int statusBarIconID) {
		// MyScheduleActivity 로 엑티비티 설정
		Intent contentIntent = new Intent(context, MyScheduleActivity.class);
		// 알림클릭시 이동할 엑티비티 설정
		PendingIntent theappIntent = PendingIntent.getActivity(context, 0,contentIntent, 0);
		CharSequence title = "시간표 알리미"; // 알림 타이틀
		CharSequence message = "수업시간 10분전 입니다."; // 알림 내용

		Notification notif = new Notification(statusBarIconID, null,
				System.currentTimeMillis());
		
		notif.flags |= Notification.FLAG_AUTO_CANCEL;	// 클릭시 사라지게
		notif.defaults |= Notification.DEFAULT_SOUND;	// 클릭시 사라지게
		
		
		long[] vibrate = {1000, 1000, 1000};  
		notif.defaults |= Notification.DEFAULT_VIBRATE;  
		notif.vibrate = vibrate;  	
		
		notif.setLatestEventInfo(context, title, message, theappIntent);
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		
		nm.notify(this.YOURAPP_NOTIFICATION_ID, notif);
	}
}
