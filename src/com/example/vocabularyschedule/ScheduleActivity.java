package com.example.vocabularyschedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.example.vocabularyschedule.ListViewAdapter.TableCell;  
import com.example.vocabularyschedule.ListViewAdapter.TableRow;  

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.widget.DrawerLayout;

public class ScheduleActivity extends Activity {
	
	private MainApp mApp;

	//DrawerLayout变量
	private DrawerLayout drawerLayout = null;
	private ListView leftDrawer;
	private String[] leftDrawerStrings;
	ScheduleActivity mClass=this;
	
	//列表
	SyncHorizontalScrollView HSVTitle,HSVContant;
	ListView LVTitle,LVContant;
	ListViewAdapter listTitleAdapter,listContantAdapter;
	ListView LVBlank,LVTasksId;
	ListViewAdapter listBlankAdapter,listTasksIdAdapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		mApp=(MainApp)getApplication();
		
		InitDrawerLayout();
		InitBlankSchedule();
		InitScrollView();
	}
	
	private void InitScrollView() {
		// TODO Auto-generated method stub
		LVContant.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				LVTasksId.dispatchTouchEvent(event);
				return false;
			}
		});
		
		LVTasksId.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				View v=view.getChildAt(0);
			    if(v != null)
			    	LVContant.setSelectionFromTop(firstVisibleItem, v.getTop());
			}
		});		
		
		HSVTitle=(SyncHorizontalScrollView)findViewById(R.id.hsv_title);
		HSVContant=(SyncHorizontalScrollView)findViewById(R.id.hsv_content);
		HSVTitle.setScrollViewListener(new HSV_listener());
		HSVContant.setScrollViewListener(new HSV_listener());
	}
	
	private class HSV_listener implements SyncHorizontalScrollViewListener{

	      @Override
	      public void onScrollChanged(SyncHorizontalScrollView ssv, int x,
	              int y, int oldX, int oldY) {
	          if(ssv==HSVTitle){
	        	  HSVContant.smoothScrollTo(x, y);
	          }else if (ssv==HSVContant){
	        	  HSVTitle.smoothScrollTo(x, y);  
	          }
	      }
	}		

	private void InitDrawerLayout() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		leftDrawer = (ListView) this.findViewById(R.id.left_drawer);
		leftDrawerStrings=getResources().getStringArray(R.array.left_drawer_array);
		leftDrawer.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, leftDrawerStrings));
        leftDrawer.setOnItemClickListener(new DrawerItemClickListener());
        leftDrawer.setItemChecked(0, true);
	}
	
	private void InitBlankSchedule() {
		LVTitle=(ListView)this.findViewById(R.id.ListTitle);
		LVContant=(ListView)this.findViewById(R.id.ListContant);
		LVBlank=(ListView)this.findViewById(R.id.List_blank);
		LVTasksId=(ListView)this.findViewById(R.id.List_taks_id);
		
		//
		ArrayList<TableRow> listBlank=new ArrayList<TableRow>();
		TableCell[] blankCell = new TableCell[1];
		blankCell[0]=new TableCell("",25,40,TableCell.STRING);
		listBlank.add(new TableRow(blankCell));
		listBlankAdapter=new ListViewAdapter(this,listBlank);
		LVBlank.setAdapter(listBlankAdapter);
		
		//
		ArrayList<TableRow> listTasksId = new ArrayList<TableRow>();
		for(int cnt=0;cnt<20;cnt++){
			TableCell[] taskSIdCell = new TableCell[1];
			taskSIdCell[0]=new TableCell(""+cnt,25,70,TableCell.STRING);
			listTasksId.add(new TableRow(taskSIdCell));
		}
		listTasksIdAdapter=new ListViewAdapter(this,listTasksId);
		LVTasksId.setAdapter(listTasksIdAdapter);
		
		//计算满品列数
		int screenWidth=this.getWindowManager().getDefaultDisplay().getWidth();
		int minColTotal=screenWidth/60;
		minColTotal+=1;
		
		//
		ArrayList<TableRow> listTitle=new ArrayList<TableRow>();
		TableCell[] titleCell = new TableCell[minColTotal];
		for(int curCol=0;curCol<minColTotal;curCol++)
			titleCell[curCol]=new TableCell("标题",60,40,TableCell.STRING);	
		listTitle.add(new TableRow(titleCell));
		listTitleAdapter=new ListViewAdapter(this,listTitle);
		LVTitle.setAdapter(listTitleAdapter);
		
		//
		ArrayList<TableRow> listContant=new ArrayList<TableRow>();
		for(int curRow=0;curRow<20;curRow++){//插入20行
			TableCell[] contantCell = new TableCell[minColTotal];
			for(int curCol=0;curCol<minColTotal;curCol++)
				contantCell[curCol]=new TableCell("",60,70,TableCell.STRING);	
			listContant.add(new TableRow(contantCell));
		}
		listContantAdapter=new ListViewAdapter(this,listContant);
		LVContant.setAdapter(listContantAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {  
	    // TODO Auto-generated method stub  
	    switch (item.getItemId()) {  
	    case R.id.meun:  
	        drawerLayout.openDrawer(Gravity.LEFT);
	        break;  
	    default:  
	        break;  
	    }  
	    return super.onOptionsItemSelected(item);  
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {  
		@Override  
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  
		    Log.e("侧边栏选择","position = "+position);  
		    switch (position) {
			case 0://计划表页面
				drawerLayout.closeDrawers();
				break;
			case 1:
				startActivity(new Intent(mClass, TodayScheduleActivity.class));
				break;
			case 2:
				startActivity(new Intent(mClass, InformationActivity.class));
				break;
			case 3:
				startActivity(new Intent(mClass, BookManagerActivity.class));
				break;
			case 4://复习模式管理
				startActivity(new Intent(mClass, ReviewModeActivity.class));
				break;
			case 5://同步设置
				startActivity(new Intent(mClass, SynSettingActivity.class));
				break;
			case 6://系统设置
				startActivity(new Intent(mClass, PrefsActivity.class));
				break;
			default:
				break;
			}
		}  
	}
	
	
}
