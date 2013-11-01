package com.example.vocabularyschedule;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;

import com.example.vocabularyschedule.ListViewAdapter.TableRow;
import com.example.vocabularyschedule.ListViewAdapter.TableRowView;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class TodayScheduleActivity extends Activity{
	private Spinner SBooks;
	private ListView LVTodaySchedule;
	private MyAdapter myAdapter=new MyAdapter(this);	
	private int count;
	
	private MainApp mApp;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_schedule);
		//SBooks=(Spinner)findViewById(R.id.books_manager_spinner);
		LVTodaySchedule=(ListView)findViewById(R.id.list_today_tasks);
		LVTodaySchedule.setAdapter(myAdapter);
		Log.i("setAdapter", String.valueOf(1));
		mApp=(MainApp)getApplication();//测试
		
		myAdapter.ShowSchedule(0,mApp.booksTodayTasks);
	}
	
	private void InitList() {
		
	}
	
	class MyAdapter extends BaseAdapter{
		private Context context; 
		private List<Task> curShowList=new ArrayList<Task>();
		private LayoutInflater layoutInflater;
		private View[] viewArray;
		
		public MyAdapter(Context context) {  
            this.context=context;  
        }  
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return curShowList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return this.getView(arg0, null, null);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	        return viewArray[position];
		}
		
		public void UpdateView() {
			for(int cnt=0;cnt<curShowList.size();cnt++){
				Task curTask=curShowList.get(cnt);
				
				String inflater=Context.LAYOUT_INFLATER_SERVICE;
				layoutInflater=(LayoutInflater)context.getSystemService(inflater);
				LinearLayout linearLayout=null;
				linearLayout=(LinearLayout)layoutInflater.inflate(R.layout.today_schedule_item,null);
				
				
				TextView TVPeriod=(TextView)linearLayout.findViewById(R.id.tv_peroid); 
				TVPeriod.setText(String.valueOf(curTask.time));
				TextView TVStart=(TextView)linearLayout.findViewById(R.id.tv_start); 
				TVStart.setText(String.valueOf(curTask.startPos)+curTask.unit);
				TextView TVEnd=(TextView)linearLayout.findViewById(R.id.tv_end); 
				TVEnd.setText(String.valueOf(curTask.endPos)+curTask.unit);
				
				TextView TVCost=(TextView)linearLayout.findViewById(R.id.tv_cost); 
				TVCost.setText(String.valueOf(curTask.cost));
				TextView TVVocabulary=(TextView)linearLayout.findViewById(R.id.tv_vocabulary); 
				TVVocabulary.setText(String.valueOf(curTask.vocabulary));
				TextView TVBookTitle=(TextView)linearLayout.findViewById(R.id.tv_bookTitle); 
				TVBookTitle.setText(String.valueOf(curTask.bookTitleStr));
				
				Button btn_finish=(Button)linearLayout.findViewById(R.id.btn_finish);
				viewArray[cnt]=linearLayout;
				btn_finish.setOnClickListener(finishButtonClickListener);
			}
		}	
		
		public void ShowSchedule(int bookId,Hashtable<Integer, Vector<Task>> data) {
			int totalItem=0;
			switch (bookId) {
				case 0://显示所有书
					Enumeration<Vector<Task>> dataEnu=data.elements();
					while(dataEnu.hasMoreElements()){
						Vector<Task> curBookTodayTasks = dataEnu.nextElement();
						curShowList.addAll(curBookTodayTasks);
						totalItem+=curBookTodayTasks.size();
					}
					viewArray=new View[totalItem];
					break;
				default:
					Vector<Task> curBookTodayTasks = data.get(Integer.valueOf(bookId));
					curShowList.addAll(curBookTodayTasks);
					viewArray=new View[curBookTodayTasks.size()];
					break;
			}
			UpdateView();
			notifyDataSetChanged();
		}
		
		private OnClickListener finishButtonClickListener = new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        final int position = LVTodaySchedule.getPositionForView(v);
		        if (position != ListView.INVALID_POSITION) {
		            //DO THE STUFF YOU WANT TO DO WITH THE position
		        	View curView=(View)getItem(position);
		        	curView.setAlpha((float) 0.3);
		        	notifyDataSetChanged();
		        }
		    }
		};
	}
	
	
}
