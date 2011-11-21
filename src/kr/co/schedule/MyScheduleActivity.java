package kr.co.schedule;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

/**
 * 주 메인 클래스
 */
public class MyScheduleActivity extends BaseActivity {
	private boolean isTwoClickBack;	// 두번 뒤로버튼 클릭 여부
	private String[] days= {"월", "화", "수", "목", "금"};
	private String selectedDay = days[0];		// 탭 선택 날짜 초기는 월요일
	
	// element
	private ListView[] listview = new ListView[days.length];	// 요일 리스트
	private MyAdapter[] adapter = new MyAdapter[days.length];	// 리스트에 넣을 아답타
	private ArrayList<Schedule> list;

	private int[] listElem = {R.id.monday_list,		// 월요일  ListView
						  R.id.tuesday_list,		// 화요일  ListView
						  R.id.wednseday_list,		// 수요일  ListView
						  R.id.thursday_list,		// 목요일  ListView
						  R.id.friday_list};		// 금요일  ListView
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initialize();
    }

	/**
	 *	탭 설정 및 리스트 설정 
	 */
	private void initialize() {
		// TODO Auto-generated method stub
        TabHost host = (TabHost)findViewById(R.id.tabhost);
        host.setup();	// 탭 초기화
        
        // 월요일
        TabSpec mon = host.newTabSpec("월");	// 새로운 탭을 생성
        mon.setIndicator("월");
		mon.setContent(R.id.monday);
        host.addTab(mon);	// host에  추가해 주자
        
        // 화요일
        TabSpec tues = host.newTabSpec("화");
        tues.setIndicator("화");
        tues.setContent(R.id.tuesday);
        host.addTab(tues);	
        
        // 수요일
        TabSpec wednse = host.newTabSpec("수");
        wednse.setIndicator("수");
        wednse.setContent(R.id.wednesday);
        host.addTab(wednse);	 
        
        // 목요일
        TabSpec thus = host.newTabSpec("목");
        thus.setIndicator("목");
        thus.setContent(R.id.thursday);
        host.addTab(thus);	
        
        // 금요일
        TabSpec fri = host.newTabSpec("금");
        fri.setIndicator("금");
        fri.setContent(R.id.friday);
        host.addTab(fri);	  
        // 탭 이동시 날짜 알아내기
        host.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String day) {
				// TODO Auto-generated method stub
				selectedDay = day;
			}
		});
        
        
		// TODO Auto-generated method stub
		MyDBHelper dbhp =  new MyDBHelper(this);
		SQLiteDatabase db = dbhp.getReadableDatabase();	// 읽기모도로 해주자
		Cursor cursor = null;
        // 요일별로 디비에 저장된 내용을 불러와 리스트에 넣는다.
		int i = 0;
        for(String day :days){	// 요일배열을 돌면서
			list = new ArrayList<Schedule>();
        	listview[i] = (ListView)findViewById(listElem[i]);	// 엘리먼트를 후킹하고
        	// 교시순으로 정렬
    		cursor = db.query(MyDBHelper.DATABASE_TABLE, null, "day = ? ", new String[]{day,}, null, null, "order_num asc");
    		if( cursor.moveToFirst() ){	// cursor에 row가 1개 이상 있으면 
    			do{
    				Schedule schedule = new Schedule();
    				schedule.setSubject( cursor.getString( cursor.getColumnIndex("subject") ));
    				schedule.setOrder( cursor.getInt( cursor.getColumnIndex("order_num") ));
    				schedule.setStartTime(cursor.getString(cursor.getColumnIndex("s_time") ));
    				schedule.setEndTime(cursor.getString(cursor.getColumnIndex("e_time") ));
    				list.add(schedule);

    			}while( cursor.moveToNext() );	// 다음 커서가 있으면 내용을 가져온다.
    		}	
    		// 아답터 셋팅
    		adapter[i] = new MyAdapter(MyScheduleActivity.this, list);
            listview[i].setAdapter(adapter[i]);
    		i++;
        }
    	// 디비는 꼭 닫아준다.
		db.close();
		dbhp.close();        

	}
	
	private void reload(int where){
		// TODO Auto-generated method stub
		MyDBHelper dbhp =  new MyDBHelper(this);
		SQLiteDatabase db = dbhp.getReadableDatabase();	// 읽기모도로 해주자
		Cursor cursor = null;
    	// 교시순으로 정렬
		cursor = db.query(MyDBHelper.DATABASE_TABLE, null, "day = ? ", new String[]{this.selectedDay,}, null, null, "order_num asc");
		list = new ArrayList<Schedule>();
		if( cursor.moveToFirst() ){	// cursor에 row가 1개 이상 있으면 
			do{
				Schedule schedule = new Schedule();
				schedule.setSubject( cursor.getString( cursor.getColumnIndex("subject") ));
				schedule.setOrder( cursor.getInt( cursor.getColumnIndex("order_num") ));
				schedule.setStartTime(cursor.getString(cursor.getColumnIndex("s_time") ));
				schedule.setEndTime(cursor.getString(cursor.getColumnIndex("e_time") ));
				list.add(schedule);

			}while( cursor.moveToNext() );	// 다음 커서가 있으면 내용을 가져온다.
		}	
		// 아답터 셋팅
		adapter[where] = new MyAdapter(MyScheduleActivity.this, list);
		listview[where].setAdapter(adapter[where]);
    	// 디비는 꼭 닫아준다.
		db.close();
		dbhp.close();   		
	}
	

    /** 옵션 메뉴 만들기 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,1,0, "추가");
    	menu.add(0,2,0, "옵션"); 
    	//item.setIcon();
    	return true;
    }
    
    /** 옵션 메뉴 선택에 따라 해당 처리를 해줌 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	Intent intent = null;
    	switch(item.getItemId()){

			case 1:		// 시간표 추가 엑티비티
				intent = new Intent(getBaseContext(), AddActivity.class);
				intent.putExtra("day", selectedDay);	// 선택된 요일도 같이 실어 보내자
				startActivityForResult(intent, 1);	// 특별한 요청코드는 필요없음
				return true;

			case 2:		// 설정 엑티비티
				intent = new Intent(getBaseContext(), SettingActivity.class);
				startActivity(intent);
				return true;			
    	}
    	return false;
    }

    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){	//리턴값이 정상이면
			for(int i=0; i < days.length; i++){	
				if(days[i] == selectedDay){	// 현재요일의 순번째를 찾은후
					Log.i("aaa", "zzz" + days[i] );
					reload(i);
					adapter[i].notifyDataSetChanged();	// 찾은 번째의 아탑터 변경통지
				}
			}
		}
	}

	/** back 버튼 두번 누르면 종료 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*
		 * back 버튼이면 타이머(2초)를 이용하여 다시한번 뒤로 가기를 
		 * 누르면 어플리케이션이 종료 되도록한다.
		 */
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (!isTwoClickBack) {
					Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.",
							Toast.LENGTH_SHORT).show();
					CntTimer timer = new CntTimer(2000, 1);
					timer.start();
				} else {
					moveTaskToBack(true);
	                finish();
					return true;
				}

			}
		}
		return false;
	}

	// 뒤로가기 종료를 위한 타이머
	class CntTimer extends CountDownTimer {
		public CntTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			isTwoClickBack = true;
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			isTwoClickBack = false;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			Log.i("Test", " isTwoClickBack " + isTwoClickBack);
		}

	} 
	
}