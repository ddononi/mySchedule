package kr.co.schedule;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddActivity extends Activity implements TimePicker.OnTimeChangedListener {
	private TimePicker startPicker, endPicker;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_schedule);
        startPicker = (TimePicker) findViewById(R.id.s_time);
        endPicker = (TimePicker) findViewById(R.id.e_time);
        
        startPicker.setOnTimeChangedListener(this);
        endPicker.setOnTimeChangedListener(this);
    }
    
    
    /** 타임피커  */
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		//startPicker.setText(String.format("%d : %d", hourOfDay, minute));
		Toast.makeText(this, String.format("%d : %d", hourOfDay, minute), Toast.LENGTH_SHORT).show();
	}
	
	/** 추가 버튼 클릭 */
	public void mOnclick(View v){
		if(v.getId() == R.id.add_btn){
			
		}
	}
    
    

}
