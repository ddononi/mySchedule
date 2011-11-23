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
 * AlarmService �� �˶��߻��� ó�� ���ù� Ŭ����
 */
public class AlarmReceiver extends BroadcastReceiver {
	private int YOURAPP_NOTIFICATION_ID = 1;	// �� ���̵�

	@Override
	public void onReceive(Context context, Intent intent) {
	//	Toast.makeText(context, "�˸� ������ �������ִ�", Toast.LENGTH_SHORT).show();
		Log.i("service", "broadcast catch!!");
		// ��������
		/*
		SharedPreferences ps = PreferenceManager.getDefaultSharedPreferences(context);
		if(ps.getBoolean("vibration", true)){
			Vibrator m_clsVibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
			m_clsVibrator.vibrate(1000);
		}
		*/
		
		//if(ps.getBoolean("sound", true)){
			// �˸���

		// ����Ǯ �����ϱ�
		// ** prameters **
		// 1: ������� ���� ��Ʈ����
		// 2: AudioManager�� ���ǵ� ��Ʈ�� Ÿ��
		// 3: sample rate �������� ǰ��. default�� 0�� ��
	//	SoundPool sound_pool = new  SoundPool(5, AudioManager.STREAM_MUSIC, 0);

		// ����Ǯ �ε��ϱ�
		// ** prameters **
		// 1: context
		// 2: resource
		// 3: priority
	//	int sound_correct = sound_pool.load(context.getApplicationContext(), R.raw.sound, 1);

		// �̵�� ����ϱ�
		// ** prameters **
		// 1: ���ҽ� �ĺ� 
		// 2-3: �Ҹ�ũ�� 
		// 4: �켱����
		// 5: �Ķ���� �ݺ�����(0:�ݺ�x, 1:1���ݺ�(��2��), -1:���ѹݺ�)
		// 6: ����ӵ�(1:1x, 2:2x)
	//	sound_pool.play(sound_correct, 1.0f, 1.0f, 1, 3, 1.0f);
		//}
		
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
		
		notif.flags |= Notification.FLAG_AUTO_CANCEL;	// Ŭ���� �������
		notif.defaults |= Notification.DEFAULT_SOUND;	// Ŭ���� �������
		
		
		long[] vibrate = {1000, 1000, 1000};  
		notif.defaults |= Notification.DEFAULT_VIBRATE;  
		notif.vibrate = vibrate;  	
		
		notif.setLatestEventInfo(context, title, message, theappIntent);
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		
		nm.notify(this.YOURAPP_NOTIFICATION_ID, notif);
	}
}
