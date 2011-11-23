package kr.co.schedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * AlarmService �� �˶��߻��� ó�� ���ù� Ŭ����
 */
public class AlarmReceiver extends BroadcastReceiver {
	private int YOURAPP_NOTIFICATION_ID = 1;	// �� ���̵�
	private String before, subject, whenDay;				// �����ٿ� ��Ÿ�� �����, �����,  ���� ��¥ ����
	private SharedPreferences sp;				// �Ҹ� �� ���� ����
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("service", "broadcast catch!!");
		before = intent.getStringExtra("before").substring(1);	// ���� ��ȣ�� ����
		subject = intent.getStringExtra("subject");				// ������ �����´�.
		whenDay = intent.getStringExtra("whenDay");				// ������ �����´�.
		sp = PreferenceManager.getDefaultSharedPreferences(context);	// ȯ�漳���� ��������
		showNotification(context, R.drawable.alarm);	// �����ϱ�
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
		contentIntent.putExtra("whenDay", whenDay);	// ������ �������� ���� �ƾ� ������.
		// �˸�Ŭ���� �̵��� ��Ƽ��Ƽ ����
		PendingIntent theappIntent = PendingIntent.getActivity(context, 0,contentIntent, 0);
		CharSequence title = "�ð�ǥ �˸���"; // �˸� Ÿ��Ʋ
		CharSequence message = subject + "���� "+ before + "���� �Դϴ�."; // �˸� ����

		Notification notif = new Notification(statusBarIconID, null,
				System.currentTimeMillis());
		
		notif.flags |= Notification.FLAG_AUTO_CANCEL;	// Ŭ���� �������
		notif.defaults |= Notification.DEFAULT_LIGHTS;	// led�� Ű��
		//notif.defaults |= Notification.DEFAULT_SOUND;	// �⺻ sound�� Ű��
		
		//	�����˶��� ���������� ������ �︰��.
		if( sp.getBoolean("vabration", true) ){
			long[] vibrate = {1000, 1000, 1000, 1000, 1000};  // 1�ʰ� 5�� 
			notif.vibrate = vibrate;  
		}
		
		//	�Ҹ��˶��� ���������� �Ҹ��� �︰��.
		if( sp.getBoolean("sound", true) ){		
			notif.sound = Uri.parse("android.resource://"+context.getPackageName()+"/raw/sound");
		}
		notif.flags |= Notification.FLAG_INSISTENT;	// ��� �˶� �߻�
		notif.setLatestEventInfo(context, title, message, theappIntent);	// ������ ����
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);		
		nm.notify(this.YOURAPP_NOTIFICATION_ID, notif);	// �����ϱ�
	}
}
