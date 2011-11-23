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
	private LayoutInflater inflater;	// 레이아웃 전개를 위한 인플레이터
	private Schedule schedule = null;		// 스케쥴 정보를 담을 객체

	public ScheduleDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;	// db, toast에 context가 필요함

		// 다이얼로그를 전개시킬 인플레이터 서비스를 얻어오자
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	private Schedule setScheduleData(int idx) {
		// TODO Auto-generated method stub
		MyDBHelper mydb = new MyDBHelper(context);
		SQLiteDatabase db = mydb.getReadableDatabase(); // 읽기모도로 해주자		
		try{
			Cursor cursor = null;
			// 날짜와, 교시 로 해당 스켸쥴을 가져온다,.
			cursor = db.query(MyDBHelper.DATABASE_TABLE, null, "idx = ?",
					new String[] {String.valueOf(idx), }, null, null, null);
			if (cursor.moveToFirst()) { // cursor에 row가 1개 이상 있으면
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
		if( setScheduleData(idx) == null){	// 검색된 데이터가 없으면 null
			return null;
		}
		
		View layout = inflater.inflate(R.layout.info_dialog, null);

		this.setContentView(layout);	// 다이얼로그에 layout을 입힌다.
		
		// 에니메이션 효과를 넣어줄 텍스트스위쳐
		TextSwitcher ts = (TextSwitcher) layout.findViewById(R.id.title);
		final String tmpTitleStr = schedule.getSubject();
		ts.setFactory(new ViewFactory() {
			public View makeView() {
				TextView tv = new TextView(ScheduleDialog.this.context);
				// 글자수가 많아짐에 따라 2줄로 나오는것을 방지하기 위해 크기를 조절
				if (tmpTitleStr.length() < 10)	
					tv.setTextSize(26);
				else
					tv.setTextSize(21);
				
				// textview의 style 설정
				tv.setTextColor(Color.parseColor("#5a9af7"));
				tv.setShadowLayer(1, 1, 1, Color.parseColor("#adc7f7"));
				return tv;
			}

		});
		ts.setText(schedule.getSubject());

		// layout에 child View들을 얻어오자
		TextView professorTV = (TextView) layout.findViewById(R.id.professor);
		TextView classroomTV = (TextView) layout.findViewById(R.id.classroom);
		TextView memoTV = (TextView) layout.findViewById(R.id.memo);
		TextView timeTV = (TextView) layout.findViewById(R.id.time);
		// 수정 버튼 이벤트
		((Button)layout.findViewById(R.id.modify_btn)).setOnClickListener(this);
		// 각 view 값을 셋팅 해준다.
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
			// 수정엑티비티로
			Intent intent = new Intent(context, ModifyActivity.class);
			// 요일 및 교시정보를 같이 넘겨준다.
			intent.putExtra("day", schedule.getDay());
			intent.putExtra("orderNum", schedule.getOrder());
			((Activity) context).startActivityForResult(intent, 0);
			this.dismiss();	// 현재 다이얼로그는 닫는다
		}
	}

}
