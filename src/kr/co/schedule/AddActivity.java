package kr.co.schedule;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddActivity extends BaseActivity implements TimePicker.OnTimeChangedListener {
	private int maxiumOrder;	// 현재 몇교시까지 있는지 
	private String day; 		// 추가할 요일이 무슨 요일인지
	private TimePicker startPicker, endPicker;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_schedule);
        
        // 추가로 이름 변경
        ((Button)findViewById(R.id.btn)).setText("추가하기");        
        // 엘리먼트 후킹
        startPicker = (TimePicker)findViewById(R.id.s_time);
        endPicker = (TimePicker)findViewById(R.id.e_time);
        // 타임 피커 이벤트 설정
        startPicker.setOnTimeChangedListener(this);
        endPicker.setOnTimeChangedListener(this);
        
        Calendar calender = Calendar.getInstance();
        endPicker.setCurrentHour(calender.get(Calendar.HOUR_OF_DAY));
        endPicker.setCurrentMinute(calender.get(Calendar.MINUTE));
        startPicker.setCurrentHour(calender.get(Calendar.HOUR_OF_DAY));
        startPicker.setCurrentMinute(calender.get(Calendar.MINUTE));
        
        // 이전 엑티비에서 넘오온 요일값
        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        Log.i("aaa", day);
		maxiumOrder = getMaxOrder(day);
	}
	
	private int getMaxOrder(String day) {
		// TODO Auto-generated method stub
		int count = 0;
		MyDBHelper dbhp =  new MyDBHelper(this);
		SQLiteDatabase db = dbhp.getReadableDatabase();	// 읽기모도로 해주자
   		Cursor cursor = db.query(MyDBHelper.DATABASE_TABLE, 
   				null, "day = ? ", new String[]{day,}, null, null, null);
   		count = cursor.getCount();	// 날짜의  갯수를 얻어온다.
    	// 디비는 꼭 닫아준다.
		db.close();
		return count;
	}
    
    
    /** 타임피커  */
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		//startPicker.setText(String.format("%d : %d", hourOfDay, minute));
		//Toast.makeText(this, String.format("%d : %d", hourOfDay, minute), Toast.LENGTH_SHORT).show();
	}
	
	/** 추가 버튼 클릭 */
	public void mOnclick(View v){
		if(v.getId() == R.id.btn){
			addSchedule();
		}
	}


	/**
	 * db에 스케쥴 추가
	 */
	private void addSchedule() {
		// 시간가져오기
		int startHour = ((TimePicker)findViewById(R.id.s_time)).getCurrentHour();
		int endHour = ((TimePicker)findViewById(R.id.e_time)).getCurrentHour();
		int startMin = ((TimePicker)findViewById(R.id.s_time)).getCurrentMinute();
		int endMin = ((TimePicker)findViewById(R.id.e_time)).getCurrentMinute();
		String startTime = String.format("%02d:%02d", startHour, startMin);	// 시작 시간
		String endTime = String.format("%02d:%02d", endHour, endMin);		// 종료 시간
		// 과목명등 입력 내용 가져오기
		String subject = ((EditText)findViewById(R.id.subject)).getText().toString();
		String professor = ((EditText)findViewById(R.id.professor)).getText().toString();
		String memo = ((EditText)findViewById(R.id.memo)).getText().toString();
		String classroom = ((EditText)findViewById(R.id.classroom)).getText().toString();
		
		// 과목명은 필수로 입력하게 함
		if(TextUtils.isEmpty(subject)){
			Toast.makeText(this, "과목명을 입력하세요", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// TODO Auto-generated method stub
		MyDBHelper dbhp =  new MyDBHelper(this);	// 도우미 클래스
		SQLiteDatabase db = dbhp.getReadableDatabase();	// 읽기모도로 해주자
		ContentValues cv = new ContentValues();
		
		
		cv.put("day", day);
		cv.put("order_num", maxiumOrder);
		cv.put("subject", subject);
		cv.put("professor", professor);
		cv.put("classroom", classroom);
		cv.put("memo", memo);
		cv.put("s_time", startTime);
		cv.put("e_time", endTime);
		// db에 정상적으로 추가 되었으면 토스트를 굽는다.
		if(	db.insert(MyDBHelper.DATABASE_TABLE, null, cv) > 0 ){
			Toast.makeText(this, "스케쥴이 추가되었습니다.", Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK,null);
			finish();
		}
		db.close();
		
	}
    
    

}
