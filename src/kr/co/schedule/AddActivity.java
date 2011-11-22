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
	private int maxiumOrder;	// ���� ��ñ��� �ִ��� 
	private String day; 		// �߰��� ������ ���� ��������
	private TimePicker startPicker, endPicker;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_schedule);
        
        // �߰��� �̸� ����
        ((Button)findViewById(R.id.btn)).setText("�߰��ϱ�");        
        // ������Ʈ ��ŷ
        startPicker = (TimePicker)findViewById(R.id.s_time);
        endPicker = (TimePicker)findViewById(R.id.e_time);
        // Ÿ�� ��Ŀ �̺�Ʈ ����
        startPicker.setOnTimeChangedListener(this);
        endPicker.setOnTimeChangedListener(this);
        
        Calendar calender = Calendar.getInstance();
        endPicker.setCurrentHour(calender.get(Calendar.HOUR_OF_DAY));
        endPicker.setCurrentMinute(calender.get(Calendar.MINUTE));
        startPicker.setCurrentHour(calender.get(Calendar.HOUR_OF_DAY));
        startPicker.setCurrentMinute(calender.get(Calendar.MINUTE));
        
        // ���� ��Ƽ�񿡼� �ѿ��� ���ϰ�
        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        Log.i("aaa", day);
		maxiumOrder = getMaxOrder(day);
	}
	
	private int getMaxOrder(String day) {
		// TODO Auto-generated method stub
		int count = 0;
		MyDBHelper dbhp =  new MyDBHelper(this);
		SQLiteDatabase db = dbhp.getReadableDatabase();	// �б�𵵷� ������
   		Cursor cursor = db.query(MyDBHelper.DATABASE_TABLE, 
   				null, "day = ? ", new String[]{day,}, null, null, null);
   		count = cursor.getCount();	// ��¥��  ������ ���´�.
    	// ���� �� �ݾ��ش�.
		db.close();
		return count;
	}
    
    
    /** Ÿ����Ŀ  */
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		//startPicker.setText(String.format("%d : %d", hourOfDay, minute));
		//Toast.makeText(this, String.format("%d : %d", hourOfDay, minute), Toast.LENGTH_SHORT).show();
	}
	
	/** �߰� ��ư Ŭ�� */
	public void mOnclick(View v){
		if(v.getId() == R.id.btn){
			addSchedule();
		}
	}


	/**
	 * db�� ������ �߰�
	 */
	private void addSchedule() {
		// �ð���������
		int startHour = ((TimePicker)findViewById(R.id.s_time)).getCurrentHour();
		int endHour = ((TimePicker)findViewById(R.id.e_time)).getCurrentHour();
		int startMin = ((TimePicker)findViewById(R.id.s_time)).getCurrentMinute();
		int endMin = ((TimePicker)findViewById(R.id.e_time)).getCurrentMinute();
		String startTime = String.format("%02d:%02d", startHour, startMin);	// ���� �ð�
		String endTime = String.format("%02d:%02d", endHour, endMin);		// ���� �ð�
		// ������ �Է� ���� ��������
		String subject = ((EditText)findViewById(R.id.subject)).getText().toString();
		String professor = ((EditText)findViewById(R.id.professor)).getText().toString();
		String memo = ((EditText)findViewById(R.id.memo)).getText().toString();
		String classroom = ((EditText)findViewById(R.id.classroom)).getText().toString();
		
		// ������� �ʼ��� �Է��ϰ� ��
		if(TextUtils.isEmpty(subject)){
			Toast.makeText(this, "������� �Է��ϼ���", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// TODO Auto-generated method stub
		MyDBHelper dbhp =  new MyDBHelper(this);	// ����� Ŭ����
		SQLiteDatabase db = dbhp.getReadableDatabase();	// �б�𵵷� ������
		ContentValues cv = new ContentValues();
		
		
		cv.put("day", day);
		cv.put("order_num", maxiumOrder);
		cv.put("subject", subject);
		cv.put("professor", professor);
		cv.put("classroom", classroom);
		cv.put("memo", memo);
		cv.put("s_time", startTime);
		cv.put("e_time", endTime);
		// db�� ���������� �߰� �Ǿ����� �佺Ʈ�� ���´�.
		if(	db.insert(MyDBHelper.DATABASE_TABLE, null, cv) > 0 ){
			Toast.makeText(this, "�������� �߰��Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK,null);
			finish();
		}
		db.close();
		
	}
    
    

}
