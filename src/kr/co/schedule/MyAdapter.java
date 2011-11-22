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
	/** ��ü ����Ʈ */
	public ArrayList<?> getList(){
		return list;
	}

	
	/** ��ü���� */
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

	/** list �� ��  view ���� */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewGroup item = getViewGroup(convertView, parent);
		TextView subjectTV = (TextView)item.findViewById(R.id.item);
		TextView timeTV = (TextView)item.findViewById(R.id.add_time);
		TextView txno = (TextView)item.findViewById(R.id.no);
		// ex) ����-> 1����
		txno.setText(String.valueOf(position+1) +"����");	
		// ex) �����-> ������ ��ȸ��
		subjectTV.setText(((Schedule)getItem(position)).getSubject());			
		// ���� �ð�
		String sTime = ((Schedule)getItem(position)).getStartTime();
		String eTime = ((Schedule)getItem(position)).getEndTime();
		timeTV.setText(sTime + " ~ " + eTime);
		return item;	
	}
	
	/**
	 * ���� ���� üũ�� custom list�� �� ��ȯ
	 * @param reuse ��ȯ�� ��
	 * @param parent �θ��
	 * @return ������ ����� ��
	 */
	private ViewGroup getViewGroup(View reuse, ViewGroup parent){
		if(reuse instanceof ViewGroup){	// ������ �����ϸ� �並 �����Ѵ�.
			return (ViewGroup)reuse;
		}

		Context context = parent.getContext();	// �θ��κ��� ���ý�Ʈ�� ���´�.
		LayoutInflater inflater = LayoutInflater.from(context);
		// custom list�� ���� ���÷����ͷ� �並 �����´�
		ViewGroup item = (ViewGroup)inflater.inflate(R.layout.custom_list, null);
		return item;
	}	

}