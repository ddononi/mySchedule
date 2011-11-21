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
 * �� ���� Ŭ����
 */
public class MyScheduleActivity extends BaseActivity {
	private ListView listview;	// ���� ����Ʈ
	private ArrayAdapter<Schedule> adapter;	// ����Ʈ�� ���� �ƴ�Ÿ
	private ArrayList<Schedule> list;

	private String selectedDay;		// �� ���� ��¥
	private String[] days = {"��", "ȭ", "��", "��", "��"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initialize();
    }

	/**
	 *	�� ���� �� ����Ʈ ���� 
	 */
	private void initialize() {
		// TODO Auto-generated method stub
        TabHost host = (TabHost)findViewById(R.id.tabhost);
        host.setup();	// �� �ʱ�ȭ
        
        // ������
        TabSpec mon = host.newTabSpec("��");	// ���ο� ���� ����
        mon.setIndicator("��");
		mon.setContent(R.id.monday);
        host.addTab(mon);	// host��  �߰��� ����
        
        // ȭ����
        TabSpec tues = host.newTabSpec("ȭ");
        tues.setIndicator("ȭ");
        tues.setContent(R.id.tuesday);
        host.addTab(tues);	
        
        // ������
        TabSpec wednse = host.newTabSpec("��");
        wednse.setIndicator("��");
        wednse.setContent(R.id.wednesday);
        host.addTab(wednse);	 
        
        // �����
        TabSpec thus = host.newTabSpec("��");
        thus.setIndicator("��");
        thus.setContent(R.id.thursday);
        host.addTab(thus);	
        
        // �ݿ���
        TabSpec fri = host.newTabSpec("��");
        fri.setIndicator("��");
        fri.setContent(R.id.friday);
        host.addTab(fri);	  
        // �� �̵��� ��¥ �˾Ƴ���
        host.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String day) {
				// TODO Auto-generated method stub
				selectedDay = day;
			}
		});
        
        
		// TODO Auto-generated method stub
		MyDBHelper dbhp =  new MyDBHelper(this);
		SQLiteDatabase db = dbhp.getReadableDatabase();	// �б�𵵷� ������
		Cursor cursor = null;
        // ���Ϻ��� ��� ����� ������ �ҷ��� ����Ʈ�� �ִ´�.
		int i = 0;
      //  for(String day :days){	
			list = new ArrayList<Schedule>();
        	ListView listview = (ListView)findViewById(R.id.mon_list);
    		cursor = db.query(MyDBHelper.DATABASE_TABLE,null, "day = '��' ", null, null, null, "order_num asc");
    		if( cursor.moveToFirst() ){	// cursor�� row�� 1�� �̻� ������ 
    			do{
    				Schedule schedule = new Schedule();
    				schedule.setOrder( cursor.getInt( cursor.getColumnIndex("order_num") ));
    				schedule.setSubject( cursor.getString( cursor.getColumnIndex("subject") ));
    				list.add(schedule);

    			}while( cursor.moveToNext() );
    		}	
    		// �ƴ��� ����
    		adapter = new ArrayAdapter<Schedule>(MyScheduleActivity.this, R.layout.custom_list, list);
            listview.setAdapter(adapter);
    		i++;
      //  }
    	// ���� �� �ݾ��ش�.
		db.close();
		dbhp.close();        

	}
}