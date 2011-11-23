package kr.co.schedule;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ModifyActivity extends BaseActivity implements
		TimePicker.OnTimeChangedListener {
	private int selectedOrder; // �������
	private String day; // �߰��� ������ ���� ��������
	private TimePicker startPicker, endPicker;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_schedule);

		// �����Ϸ�� �̸� ����
		((Button) findViewById(R.id.btn)).setText("�����Ϸ�");

		initElementValue();
	}

	/**
	 * db���� ������ �ҷ��� ������Ʈ�� ä���ش�.
	 */
	private void initElementValue() {
		// TODO Auto-generated method stub
		// ����, ������, ���ǽ�, �޸�, ���۽ð�, ����ð�
		String subject, professor, classroom, memo, sTime, eTime;
		int sHour, eHour, sMin, eMin; // ���� �� ���� ��, ��
		MyDBHelper mydb = new MyDBHelper(this);
		SQLiteDatabase db = mydb.getReadableDatabase();
		Cursor cursor = null;

		// ���� ��Ƽ�񿡼� �ѿ��� ���ϰ� �� ���ð�
		Intent intent = getIntent();
		day = intent.getStringExtra("day");
		selectedOrder = intent.getIntExtra("orderNum", 0); // ���ü���

		cursor = db.query(MyDBHelper.DATABASE_TABLE, null,
				"day=? and order_num=?",
				new String[] { day, String.valueOf(selectedOrder) }, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			subject = cursor.getString(cursor.getColumnIndex("subject"));
			professor = cursor.getString(cursor.getColumnIndex("professor"));
			classroom = cursor.getString(cursor.getColumnIndex("classroom"));
			memo = cursor.getString(cursor.getColumnIndex("memo"));
			sTime = cursor.getString(cursor.getColumnIndex("s_time"));
			eTime = cursor.getString(cursor.getColumnIndex("e_time"));			
			
			//	������Ʈ�� ���� �־��ش�.
			((EditText) findViewById(R.id.subject)).setText(subject);
			((EditText) findViewById(R.id.professor)).setText(professor);
			((EditText) findViewById(R.id.classroom)).setText(classroom);
			((EditText) findViewById(R.id.memo)).setText(memo);
			
			sHour = Integer.valueOf(sTime.substring(0,2));	// ���� �� ������
			eHour = Integer.valueOf(eTime.substring(0,2));	// ���� �� ������
	
			sMin = Integer.valueOf(sTime.substring(3));	// ���� �� ������
			eMin = Integer.valueOf(eTime.substring(3));	// ���� �� ������
		
			// ������Ʈ ��ŷ
			startPicker = (TimePicker) findViewById(R.id.s_time);
			endPicker = (TimePicker) findViewById(R.id.e_time);
			// Ÿ�� ��Ŀ �̺�Ʈ ����
			startPicker.setOnTimeChangedListener(this);
			endPicker.setOnTimeChangedListener(this);
			
			// Ÿ����Ŀ�� ����� �ð� �־��ֱ�
			startPicker.setCurrentHour(sHour);
			startPicker.setCurrentMinute(sMin);			
			endPicker.setCurrentHour(eHour);
			endPicker.setCurrentMinute(eMin);

		}
		cursor.close();
		db.close();

	}

	/** Ÿ����Ŀ */
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
	}

	/** ���� ��ư Ŭ�� */
	public void mOnclick(View v) {
		if (v.getId() == R.id.btn) {
			updateSchedule();
		}
	}

	/**
	 * db�� ������ ������Ʈ
	 */
	private void updateSchedule() {
		// �ð���������
		int startHour = ((TimePicker) findViewById(R.id.s_time))
				.getCurrentHour();
		int endHour = ((TimePicker) findViewById(R.id.e_time)).getCurrentHour();
		int startMin = ((TimePicker) findViewById(R.id.s_time))
				.getCurrentMinute();
		int endMin = ((TimePicker) findViewById(R.id.e_time))
				.getCurrentMinute();
		//	���ڸ��� ä���
		String startTime = String.format("%02d:%02d", startHour, startMin); // ����
																			// �ð�
		String endTime = String.format("%02d:%02d", endHour, endMin); // ���� �ð�
		// ������ �Է� ���� ��������
		String subject = ((EditText) findViewById(R.id.subject)).getText()
				.toString();
		String professor = ((EditText) findViewById(R.id.professor)).getText()
				.toString();
		String memo = ((EditText) findViewById(R.id.memo)).getText().toString();
		String classroom = ((EditText) findViewById(R.id.classroom)).getText()
				.toString();
		
		// ������� �ʼ��� �Է��ϰ� ��
		if(TextUtils.isEmpty(subject)){
			Toast.makeText(this, "������� �Է��ϼ���", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// TODO Auto-generated method stub
		MyDBHelper dbhp = new MyDBHelper(this); // ����� Ŭ����
		SQLiteDatabase db = dbhp.getReadableDatabase(); // �б�𵵷� ������
		ContentValues cv = new ContentValues();

		cv.put("subject", subject);
		cv.put("professor", professor);
		cv.put("classroom", classroom);
		cv.put("memo", memo);
		cv.put("s_time", startTime);
		cv.put("e_time", endTime);
		// db�� ���������� ������Ʈ �Ǿ����� �佺Ʈ�� ���´�.
		if (db.update(MyDBHelper.DATABASE_TABLE, cv, "day=? and order_num=?",
				new String[] { day, String.valueOf(selectedOrder) }) > 0) {
			Toast.makeText(this, "�������� �����Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK, null); // ������Ƽ��Ƽ�� �������� �޽���
			Intent intent = new Intent(ModifyActivity.this, MyScheduleActivity.class);
			startActivityForResult(intent, 0);
			finish();
			
		}
		db.close();

	}

}
