package kr.co.schedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 *	디비 초기화 및 도우미 클래스	
 */
public class MyDBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_TABLE = "timetable";	// 테이블 이름
	private static final String DATABASE_NAME = "schedule";	// DB이름
	private static final int DATABASE_VERSION = 7;			// 데이터 베이스 버젼
    
	public MyDBHelper(Context context ) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/** DB가 생성됐을때 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
		
		// sqlite가 생성 됐을경우 data table을 생성한다.
		// 인덱스, 요일, 교시, 과목, 교수, 메모, 시작시간, 종료시간, 알람여부 
        db.execSQL("create table "+ DATABASE_TABLE
        		+ " ( idx integer primary key autoincrement, day TEXT, "
        		+ " order_num integer, subject TEXT, professor TEXT, memo TEXT,"
        		+ " s_time TEXT, e_time TEXT );");
      
	}
	
	/** DB가 업그레이드 됐을때 table 재 생성 */
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// sqlite가 업그레이드 됐을경우 이전에 있던 테이블은 없앤다.
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);
        onCreate(db);
	}
	
}