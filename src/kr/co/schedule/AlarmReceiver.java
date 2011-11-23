package kr.co.schedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * AlarmService 의 알람발생시 처리 리시버 클래스
 */
public class AlarmReceiver extends BroadcastReceiver {
	private int YOURAPP_NOTIFICATION_ID = 1;	// 앱 아이디값

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "알림 내용음 적을수있다", Toast.LENGTH_SHORT).show();
		Log.i("service", "broadcast catch!!");
		// 진동설정
		Vibrator m_clsVibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		m_clsVibrator.vibrate(1000);
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
		notif.setLatestEventInfo(context, title, message, theappIntent);

		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(this.YOURAPP_NOTIFICATION_ID, notif);
	}
}
