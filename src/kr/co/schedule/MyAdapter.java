package kr.co.schedule;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.BaseAdapter;

public class MyAdapter extends BaseAdapter {
	private ArrayList<Schedule> list;
	public MyAdapter(ArrayList<Schedule> list ){
		this.list = list;
	}
	/** 전체 리스트 */
	public ArrayList<?> getList(){
		return list;
	}

	
	/** 전체갯수 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/** list 의 각  view 설정 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewGroup item = getViewGroup(convertView, parent);
		TextView subjectTV = (TextView)item.findViewById(R.id.item);
		TextView timeTV = (TextView)item.findViewById(R.id.add_time);
		TextView txno = (TextView)item.findViewById(R.id.no);
		// ex) 교시-> 1교시
		txno.setText(String.valueOf(position+1) +"교시");	
		// ex) 과목명-> 디지털 논리회로
		subjectTV.setText(((Schedule)getItem(position)).getSubject());			
		// 과목 시간
		String sTime = ((Schedule)getItem(position)).getStartTime();
		String eTime = ((Schedule)getItem(position)).getEndTime();
		timeTV.setText(sTime + " ~ " + eTime);
		return item;	
	}
	
	/**
	 * 뷰의 재사용 체크후 custom list로 뷰 반환
	 * @param reuse 변환될 뷰
	 * @param parent 부모뷰
	 * @return 전개후 얻어진 뷰
	 */
	private ViewGroup getViewGroup(View reuse, ViewGroup parent){
		if(reuse instanceof ViewGroup){	// 재사용이 가능하면 뷰를 재사용한다.
			return (ViewGroup)reuse;
		}

		Context context = parent.getContext();	// 부모뷰로부터 컨택스트를 얻어온다.
		LayoutInflater inflater = LayoutInflater.from(context);
		// custom list를 위해 인플레이터로 뷰를 가져온다
		ViewGroup item = (ViewGroup)inflater.inflate(R.layout.custom_list, null);
		return item;
	}	

}