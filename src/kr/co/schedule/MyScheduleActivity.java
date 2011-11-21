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
 * �� ���� Ŭ����
 */
public class MyScheduleActivity extends BaseActivity {
	private boolean isTwoClickBack;	// �ι� �ڷι�ư Ŭ�� ����
	private String[] days= {"��", "ȭ", "��", "��", "��"};
	private String selectedDay = days[0];		// �� ���� ��¥ �ʱ�� ������
	
	// element
	private ListView[] listview = new ListView[days.length];	// ���� ����Ʈ
	private MyAdapter[] adapter = new MyAdapter[days.length];	// ����Ʈ�� ���� �ƴ�Ÿ
	private ArrayList<Schedule> list;

	private int[] listElem = {R.id.monday_list,		// ������  ListView
						  R.id.tuesday_list,		// ȭ����  ListView
						  R.id.wednseday_list,		// ������  ListView
						  R.id.thursday_list,		// �����  ListView
						  R.id.friday_list};		// �ݿ���  ListView
	
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
        for(String day :days){	// ���Ϲ迭�� ���鼭
			list = new ArrayList<Schedule>();
        	listview[i] = (ListView)findViewById(listElem[i]);	// ������Ʈ�� ��ŷ�ϰ�
        	// ���ü����� ����
    		cursor = db.query(MyDBHelper.DATABASE_TABLE, null, "day = ? ", new String[]{day,}, null, null, "order_num asc");
    		if( cursor.moveToFirst() ){	// cursor�� row�� 1�� �̻� ������ 
    			do{
    				Schedule schedule = new Schedule();
    				schedule.setSubject( cursor.getString( cursor.getColumnIndex("subject") ));
    				schedule.setOrder( cursor.getInt( cursor.getColumnIndex("order_num") ));
    				schedule.setStartTime(cursor.getString(cursor.getColumnIndex("s_time") ));
    				schedule.setEndTime(cursor.getString(cursor.getColumnIndex("e_time") ));
    				list.add(schedule);

    			}while( cursor.moveToNext() );	// ���� Ŀ���� ������ ������ �����´�.
    		}	
    		// �ƴ��� ����
    		adapter[i] = new MyAdapter(MyScheduleActivity.this, list);
            listview[i].setAdapter(adapter[i]);
    		i++;
        }
    	// ���� �� �ݾ��ش�.
		db.close();
		dbhp.close();        

	}
	
	private void reload(int where){
		// TODO Auto-generated method stub
		MyDBHelper dbhp =  new MyDBHelper(this);
		SQLiteDatabase db = dbhp.getReadableDatabase();	// �б�𵵷� ������
		Cursor cursor = null;
    	// ���ü����� ����
		cursor = db.query(MyDBHelper.DATABASE_TABLE, null, "day = ? ", new String[]{this.selectedDay,}, null, null, "order_num asc");
		list = new ArrayList<Schedule>();
		if( cursor.moveToFirst() ){	// cursor�� row�� 1�� �̻� ������ 
			do{
				Schedule schedule = new Schedule();
				schedule.setSubject( cursor.getString( cursor.getColumnIndex("subject") ));
				schedule.setOrder( cursor.getInt( cursor.getColumnIndex("order_num") ));
				schedule.setStartTime(cursor.getString(cursor.getColumnIndex("s_time") ));
				schedule.setEndTime(cursor.getString(cursor.getColumnIndex("e_time") ));
				list.add(schedule);

			}while( cursor.moveToNext() );	// ���� Ŀ���� ������ ������ �����´�.
		}	
		// �ƴ��� ����
		adapter[where] = new MyAdapter(MyScheduleActivity.this, list);
		listview[where].setAdapter(adapter[where]);
    	// ���� �� �ݾ��ش�.
		db.close();
		dbhp.close();   		
	}
	

    /** �ɼ� �޴� ����� */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,1,0, "�߰�");
    	menu.add(0,2,0, "�ɼ�"); 
    	//item.setIcon();
    	return true;
    }
    
    /** �ɼ� �޴� ���ÿ� ���� �ش� ó���� ���� */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	Intent intent = null;
    	switch(item.getItemId()){

			case 1:		// �ð�ǥ �߰� ��Ƽ��Ƽ
				intent = new Intent(getBaseContext(), AddActivity.class);
				intent.putExtra("day", selectedDay);	// ���õ� ���ϵ� ���� �Ǿ� ������
				startActivityForResult(intent, 1);	// Ư���� ��û�ڵ�� �ʿ����
				return true;

			case 2:		// ���� ��Ƽ��Ƽ
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
		if(resultCode == RESULT_OK){	//���ϰ��� �����̸�
			for(int i=0; i < days.length; i++){	
				if(days[i] == selectedDay){	// ��������� ����°�� ã����
					Log.i("aaa", "zzz" + days[i] );
					reload(i);
					adapter[i].notifyDataSetChanged();	// ã�� ��°�� ��ž�� ��������
				}
			}
		}
	}

	/** back ��ư �ι� ������ ���� */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*
		 * back ��ư�̸� Ÿ�̸�(2��)�� �̿��Ͽ� �ٽ��ѹ� �ڷ� ���⸦ 
		 * ������ ���ø����̼��� ���� �ǵ����Ѵ�.
		 */
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (!isTwoClickBack) {
					Toast.makeText(this, "'�ڷ�' ��ư�� �ѹ� �� ������ ����˴ϴ�.",
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

	// �ڷΰ��� ���Ḧ ���� Ÿ�̸�
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