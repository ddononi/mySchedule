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
 * AlarmService �� �˶��߻��� ó�� ���ù� Ŭ����
 */
public class AlarmReceiver extends BroadcastReceiver {
	private int YOURAPP_NOTIFICATION_ID = 1;	// �� ���̵�

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "�˸� ������ �������ִ�", Toast.LENGTH_SHORT).show();
		Log.i("service", "broadcast catch!!");
		// ��������
		Vibrator m_clsVibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		m_clsVibrator.vibrate(1000);
		// �˸�����
		showNotification(context, R.drawable.alarm);
	}

	/**
	 *	���¹ٿ� �˶��� �˸��� Ȯ�ν� MyScheduleActivity�� �̵���Ų��.
	 * @param context
	 * @param statusBarIconID
	 * 		���¹ٿ� ��Ÿ�� ������
	 */
	private void showNotification(Context context, int statusBarIconID) {
		// MyScheduleActivity �� ��Ƽ��Ƽ ����
		Intent contentIntent = new Intent(context, MyScheduleActivity.class);
		// �˸�Ŭ���� �̵��� ��Ƽ��Ƽ ����
		PendingIntent theappIntent = PendingIntent.getActivity(context, 0,contentIntent, 0);
		CharSequence title = "�ð�ǥ �˸���"; // �˸� Ÿ��Ʋ
		CharSequence message = "�����ð� 10���� �Դϴ�."; // �˸� ����

		Notification notif = new Notification(statusBarIconID, null,
				System.currentTimeMillis());
		notif.setLatestEventInfo(context, title, message, theappIntent);

		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(this.YOURAPP_NOTIFICATION_ID, notif);
	}
}
