package kr.co.schedule;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
	private ListView listview;	// 요일 리스트
	private ArrayAdapter<Schedule> adapter;	// 리스트에 넣을 아답타
	private ArrayList<Schedule> list;

	private String selectedDay;		// 탭 선택 날짜
	private String[] days = {"월", "화", "수", "목", "금"};
	
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
      //  for(String day :days){	
			list = new ArrayList<Schedule>();
        	ListView listview = (ListView)findViewById(R.id.mon_list);
    		cursor = db.query(MyDBHelper.DATABASE_TABLE,null, "day = '월' ", null, null, null, "order_num asc");
    		if( cursor.moveToFirst() ){	// cursor에 row가 1개 이상 있으면 
    			do{
    				Schedule schedule = new Schedule();
    				schedule.setOrder( cursor.getInt( cursor.getColumnIndex("order_num") ));
    				schedule.setSubject( cursor.getString( cursor.getColumnIndex("subject") ));
    				list.add(schedule);

    			}while( cursor.moveToNext() );
    		}	
    		// 아답터 셋팅
    		adapter = new ArrayAdapter<Schedule>(MyScheduleActivity.this, R.layout.custom_list, list);
            listview.setAdapter(adapter);
    		i++;
      //  }
    	// 디비는 꼭 닫아준다.
		db.close();
		dbhp.close();        

	}
}