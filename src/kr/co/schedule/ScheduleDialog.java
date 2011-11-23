package kr.co.schedule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public final class ScheduleDialog extends Dialog implements View.OnClickListener {
	private Context context;
	private LayoutInflater inflater;	// ���̾ƿ� ������ ���� ���÷�����
	private Schedule schedule = null;		// ������ ������ ���� ��ü

	public ScheduleDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;	// db, toast�� context�� �ʿ���

		// ���̾�α׸� ������ų ���÷����� ���񽺸� ������
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	private Schedule setScheduleData(int idx) {
		// TODO Auto-generated method stub
		MyDBHelper mydb = new MyDBHelper(context);
		SQLiteDatabase db = mydb.getReadableDatabase(); // �б�𵵷� ������		
		try{
			Cursor cursor = null;
			// ��¥��, ���� �� �ش� �������� �����´�,.
			cursor = db.query(MyDBHelper.DATABASE_TABLE, null, "idx = ?",
					new String[] {String.valueOf(idx), }, null, null, null);
			if (cursor.moveToFirst()) { // cursor�� row�� 1�� �̻� ������
				schedule = new Schedule();
				schedule.setDay(cursor.getString(cursor.getColumnIndex("day")));
				schedule.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
				schedule.setOrder(cursor.getInt(cursor.getColumnIndex("order_num")));
				schedule.setProfessor(cursor.getString(cursor.getColumnIndex("professor")));
				schedule.setClassroom(cursor.getString(cursor.getColumnIndex("classroom")));
				schedule.setMemo(cursor.getString(cursor.getColumnIndex("memo")));
				schedule.setStartTime(cursor.getString(cursor.getColumnIndex("s_time")));
				schedule.setEndTime(cursor.getString(cursor.getColumnIndex("e_time")));
			}
		}finally{
			db.close();
		}
		
		return schedule;
	}

	public final ScheduleDialog makeDialog(int idx) {
		if( setScheduleData(idx) == null){	// �˻��� �����Ͱ� ������ null
			return null;
		}
		
		View layout = inflater.inflate(R.layout.info_dialog, null);

		this.setContentView(layout);	// ���̾�α׿� layout�� ������.
		
		// ���ϸ��̼� ȿ���� �־��� �ؽ�Ʈ������
		TextSwitcher ts = (TextSwitcher) layout.findViewById(R.id.title);
		final String tmpTitleStr = schedule.getSubject();
		ts.setFactory(new ViewFactory() {
			public View makeView() {
				TextView tv = new TextView(ScheduleDialog.this.context);
				// ���ڼ��� �������� ���� 2�ٷ� �����°��� �����ϱ� ���� ũ�⸦ ����
				if (tmpTitleStr.length() < 10)	
					tv.setTextSize(26);
				else
					tv.setTextSize(21);
				
				// textview�� style ����
				tv.setTextColor(Color.parseColor("#5a9af7"));
				tv.setShadowLayer(1, 1, 1, Color.parseColor("#adc7f7"));
				return tv;
			}

		});
		ts.setText(schedule.getSubject());

		// layout�� child View���� ������
		TextView professorTV = (TextView) layout.findViewById(R.id.professor);
		TextView classroomTV = (TextView) layout.findViewById(R.id.classroom);
		TextView memoTV = (TextView) layout.findViewById(R.id.memo);
		TextView timeTV = (TextView) layout.findViewById(R.id.time);
		// ���� ��ư �̺�Ʈ
		((Button)layout.findViewById(R.id.modify_btn)).setOnClickListener(this);
		// �� view ���� ���� ���ش�.
		professorTV.setText(schedule.getProfessor());
		classroomTV.setText(schedule.getClassroom());
		memoTV.setText(schedule.getMemo());
		timeTV.setText(schedule.getStartTime() + " ~ " + schedule.getEndTime());
		return this;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if( v.getId() == R.id.modify_btn){
			// ������Ƽ��Ƽ��
			Intent intent = new Intent(context, ModifyActivity.class);
			// ���� �� ���������� ���� �Ѱ��ش�.
			intent.putExtra("day", schedule.getDay());
			intent.putExtra("orderNum", schedule.getOrder());
			((Activity) context).startActivityForResult(intent, 0);
			this.dismiss();	// ���� ���̾�α״� �ݴ´�
		}
	}

}
