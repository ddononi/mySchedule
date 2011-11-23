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
	private int selectedOrder; // 몇교시인지
	private String day; // 추가할 요일이 무슨 요일인지
	private TimePicker startPicker, endPicker;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_schedule);

		// 수정완료로 이름 변경
		((Button) findViewById(R.id.btn)).setText("수정완료");

		initElementValue();
	}

	/**
	 * db에서 내용을 불러와 엘리먼트에 채워준다.
	 */
	private void initElementValue() {
		// TODO Auto-generated method stub
		// 과목, 교수명, 강의실, 메모, 시작시간, 종료시간
		String subject, professor, classroom, memo, sTime, eTime;
		int sHour, eHour, sMin, eMin; // 시작 및 종료 시, 분
		MyDBHelper mydb = new MyDBHelper(this);
		SQLiteDatabase db = mydb.getReadableDatabase();
		Cursor cursor = null;

		// 이전 엑티비에서 넘오온 요일값 및 교시값
		Intent intent = getIntent();
		day = intent.getStringExtra("day");
		selectedOrder = intent.getIntExtra("orderNum", 0); // 교시순번

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
			
			//	엘리먼트에 값을 넣어준다.
			((EditText) findViewById(R.id.subject)).setText(subject);
			((EditText) findViewById(R.id.professor)).setText(professor);
			((EditText) findViewById(R.id.classroom)).setText(classroom);
			((EditText) findViewById(R.id.memo)).setText(memo);
			
			sHour = Integer.valueOf(sTime.substring(0,2));	// 시작 시 빼오기
			eHour = Integer.valueOf(eTime.substring(0,2));	// 종료 시 빼오기
	
			sMin = Integer.valueOf(sTime.substring(3));	// 시작 분 빼오기
			eMin = Integer.valueOf(eTime.substring(3));	// 종료 분 빼오기
		
			// 엘리먼트 후킹
			startPicker = (TimePicker) findViewById(R.id.s_time);
			endPicker = (TimePicker) findViewById(R.id.e_time);
			// 타임 피커 이벤트 설정
			startPicker.setOnTimeChangedListener(this);
			endPicker.setOnTimeChangedListener(this);
			
			// 타임피커에 저장된 시간 넣어주기
			startPicker.setCurrentHour(sHour);
			startPicker.setCurrentMinute(sMin);			
			endPicker.setCurrentHour(eHour);
			endPicker.setCurrentMinute(eMin);

		}
		cursor.close();
		db.close();

	}

	/** 타임피커 */
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
	}

	/** 수정 버튼 클릭 */
	public void mOnclick(View v) {
		if (v.getId() == R.id.btn) {
			updateSchedule();
		}
	}

	/**
	 * db에 스케쥴 업데이트
	 */
	private void updateSchedule() {
		// 시간가져오기
		int startHour = ((TimePicker) findViewById(R.id.s_time))
				.getCurrentHour();
		int endHour = ((TimePicker) findViewById(R.id.e_time)).getCurrentHour();
		int startMin = ((TimePicker) findViewById(R.id.s_time))
				.getCurrentMinute();
		int endMin = ((TimePicker) findViewById(R.id.e_time))
				.getCurrentMinute();
		//	두자리씩 채우기
		String startTime = String.format("%02d:%02d", startHour, startMin); // 시작
																			// 시간
		String endTime = String.format("%02d:%02d", endHour, endMin); // 종료 시간
		// 과목명등 입력 내용 가져오기
		String subject = ((EditText) findViewById(R.id.subject)).getText()
				.toString();
		String professor = ((EditText) findViewById(R.id.professor)).getText()
				.toString();
		String memo = ((EditText) findViewById(R.id.memo)).getText().toString();
		String classroom = ((EditText) findViewById(R.id.classroom)).getText()
				.toString();
		
		// 과목명은 필수로 입력하게 함
		if(TextUtils.isEmpty(subject)){
			Toast.makeText(this, "과목명을 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// TODO Auto-generated method stub
		MyDBHelper dbhp = new MyDBHelper(this); // 도우미 클래스
		SQLiteDatabase db = dbhp.getReadableDatabase(); // 읽기모도로 해주자
		ContentValues cv = new ContentValues();

		cv.put("subject", subject);
		cv.put("professor", professor);
		cv.put("classroom", classroom);
		cv.put("memo", memo);
		cv.put("s_time", startTime);
		cv.put("e_time", endTime);
		// db에 정상적으로 업데이트 되었으면 토스트를 굽는다.
		if (db.update(MyDBHelper.DATABASE_TABLE, cv, "day=? and order_num=?",
				new String[] { day, String.valueOf(selectedOrder) }) > 0) {
			Toast.makeText(this, "스케쥴이 수정되었습니다.", Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK, null); // 이전엑티비티에 정상종료 메시지
			Intent intent = new Intent(ModifyActivity.this, MyScheduleActivity.class);
			startActivityForResult(intent, 0);
			finish();
			
		}
		db.close();

	}

}
