package com.example.vocabularyschedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import android.R.integer;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class BookManagerActivity extends Activity{
	BookManagerActivity mClass=this;
	private MainApp mApp;
	private ImageButton ibtn_deleteBook,ibtn_addBook;
	private ImageButton ibtn_editBookTitle,ibtn_editPerPageAvgWords,ibtn_editPerPartAvgPages,ibtn_editRange,ibtn_editStartDate,ibtn_editReviewMode,ibtn_editWeekPlans;
	
	Spinner spn_books;
	ArrayList<String> spn_books_al;
	ArrayAdapter<String> spn_books_adapter;
	Vector<Integer> booksIdVec;//Value-ModeId
	Integer curSelectedBookId;
	
	private Calendar bookStartDate;

	private TextView tv_bookTitle,tv_partUnit;
	private TextView tv_perPageAvgWords;
	private TextView tv_perPartAvgPages_unit,tv_perPartAvgPages;
	private TextView tv_rangeStart,tv_rangeStart_unit,tv_rangeEnd,tv_rangeEnd_unit;
	private TextView tv_year,tv_month,tv_day;
	private TextView tv_bookReviewMode;
	private TextView tv_weekPlans;
	
	private Book book;
		
	//private ExpandableListView explv_weekPlans;
	//private List<String> weekPlansGroupData;
	//private List<List<String>> weekPlansChildrenData;
	//private int explv_weekPlans_groupHeight;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books_manager);
		
		mApp=(MainApp)getApplication();
		InitBooksSpinner();
		InitIbtnAddBook();
		InitIbtnStartDate();
		//InitWeekPlans();
	}
	
	public void InitBooksSpinner(){
		spn_books_al=new ArrayList<String>();
		spn_books=(Spinner)findViewById(R.id.spn_books);
		ResetBooksSpinnerAdapter();
	}
    
    private void InitIbtnAddBook(){
    	ibtn_addBook=(ImageButton)findViewById(R.id.ibtn_addBook);
		ibtn_addBook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(mClass, AddBookActivity.class));
			}
		});
    }
    
    private void InitIbtnDeleteBook(){
    	curSelectedBookId=booksIdVec.elementAt(spn_books.getSelectedItemPosition());
    	
    }
    
    private void InitIbtnStartDate(){  	
    	ibtn_editStartDate=(ImageButton)findViewById(R.id.ibtn_editStartDate);
		ibtn_editStartDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bookStartDate=Calendar.getInstance();
				new DatePickerDialog(mClass,mDateSetListener,
						bookStartDate.get(Calendar.YEAR), bookStartDate.get(Calendar.MONTH), bookStartDate.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
    }
    
    private void InitBookInformationTextView() {
    	tv_bookTitle=(TextView)findViewById(R.id.tv_bookTitle);
    	
    	tv_partUnit=(TextView)findViewById(R.id.tv_partUnit);
    	
    	tv_perPageAvgWords=(TextView)findViewById(R.id.tv_perPageAvgWords);
    	
    	tv_perPartAvgPages_unit=(TextView)findViewById(R.id.tv_perPartAvgPages_unit);
    	tv_perPartAvgPages=(TextView)findViewById(R.id.tv_perPartAvgPages);   	
    	
    	tv_bookReviewMode=(TextView)findViewById(R.id.tv_bookReviewMode);  
    	
    	tv_weekPlans=(TextView)findViewById(R.id.tv_weekPlans); 
    	
    	tv_year=(TextView)findViewById(R.id.tv_year);
		tv_month=(TextView)findViewById(R.id.tv_month);
		tv_day=(TextView)findViewById(R.id.tv_day);	
		
		
	}
    
	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			bookStartDate.set(Calendar.YEAR, arg1);
			bookStartDate.set(Calendar.MONTH, arg2);
			bookStartDate.set(Calendar.DAY_OF_MONTH, arg3);
			tv_year.setText(String.valueOf(arg1));
			tv_month.setText(String.valueOf(arg2+1));
			tv_day.setText(String.valueOf(arg3));
		}
    };
    
    private void ResetBooksSpinnerAdapter(){
    	booksIdVec=new Vector<Integer>();
    	Enumeration<Book> boosEnu=mApp.books.elements();
		while (boosEnu.hasMoreElements()) {
			Book curBook = (Book) boosEnu.nextElement();
			spn_books_al.add(curBook.title);
			booksIdVec.add(Integer.valueOf(curBook.id));
		}
		spn_books_adapter=new
				ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spn_books_al);
		spn_books_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_books.setAdapter(spn_books_adapter);
    }
    
    private void ShowBookInformation(){
    	curSelectedBookId=booksIdVec.elementAt(spn_books.getSelectedItemPosition());
    	Book curSelectedBook=mApp.books.get(Integer.valueOf(curSelectedBookId));
    	tv_bookTitle.setText(curSelectedBook.title);
    	tv_partUnit.setText(curSelectedBook.unitStr);
    	tv_perPageAvgWords.setText(String.valueOf(curSelectedBook.perPageAvgWords));
    }
    
    /*
    private void InitWeekPlans() {
    	explv_weekPlans=(ExpandableListView)findViewById(R.id.explv_weekPlans);
    	book=mApp.books.get(Integer.valueOf(1));
    	WeekPlansAdapter explv_weekPlans_adapter=new WeekPlansAdapter(this);
    	explv_weekPlans.setAdapter(explv_weekPlans_adapter);
    	
    	//View weekPlansGroupView=explv_weekPlans_adapter.getGroupView(0, false, null, null);
    	//weekPlansGroupView.measure(0, 0);
    	//explv_weekPlans_groupHeight=weekPlansGroupView.getMeasuredHeight();
    	explv_weekPlans_groupHeight=100;
    	ViewGroup.LayoutParams params = explv_weekPlans.getLayoutParams(); 
		params.height=explv_weekPlans_groupHeight;
		explv_weekPlans.setLayoutParams(params);
    	//Log.i("explv_weekPlans_groupHeight", String.valueOf(explv_weekPlans_groupHeight));
    	
    	explv_weekPlans.setOnGroupExpandListener(new OnGroupExpandListener() {
		
			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				ListAdapter listAdapter = explv_weekPlans.getAdapter(); 
			    int totalHeight = 0; 
			    for (int i = 0; i < listAdapter.getCount(); i++) { 
			      View listItem = listAdapter.getView(i, null, explv_weekPlans); 
			      //計算list的寬度與高度 
			      listItem.measure(0, 0); 
			      Log.i("listItem.getMeasuredHeight()", String.valueOf(listItem.getMeasuredHeight()));    
			      //加總所有子list項目總高度 
			      totalHeight += listItem.getMeasuredHeight();
			      Log.i("totalHeight += listItem.getMeasuredHeight();", String.valueOf(totalHeight));  
			      ViewGroup.LayoutParams params = explv_weekPlans.getLayoutParams(); 
			      Log.i("params.height-原", String.valueOf(params.height)); 
			      params.height = totalHeight+(explv_weekPlans.getDividerHeight() * (listAdapter.getCount() - 1)); 
			      Log.i("params.height-后", String.valueOf(params.height));   
			      explv_weekPlans.setLayoutParams(params);
			}
		}});
    	explv_weekPlans.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int arg0) {
				// TODO Auto-generated method stub
				ViewGroup.LayoutParams params = explv_weekPlans.getLayoutParams(); 
				params.height=explv_weekPlans_groupHeight;
				explv_weekPlans.setLayoutParams(params);
			}
		});
	}
    
    private class WeekPlansAdapter extends BaseExpandableListAdapter{
    	private Context context;
    	private LayoutInflater layoutInflater;
    	
    	WeekPlansAdapter(Context context){
    		this.context = context;
    	}
    	
		@Override
		public Object getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
				ViewGroup arg4) {
			// TODO Auto-generated method stub
			String inflater=Context.LAYOUT_INFLATER_SERVICE;
			layoutInflater=(LayoutInflater)context.getSystemService(inflater);
			LinearLayout listview_gaps_item=null;
			listview_gaps_item=(LinearLayout)layoutInflater.inflate(R.layout.listview_week_plans_item,null);
			
			TextView tv_week=(TextView)listview_gaps_item.findViewById(R.id.tv_week);
			int curWeek=0;
			switch (arg1) {
			case 0://星期一
				tv_week.setText("星期一");
				curWeek=Calendar.MONDAY;
				break;
			case 1://星期二
				tv_week.setText("星期二");
				curWeek=Calendar.TUESDAY;
				break;
			case 2://星期三
				tv_week.setText("星期三");
				curWeek=Calendar.WEDNESDAY;
				break;
			case 3://星期四
				tv_week.setText("星期四");
				curWeek=Calendar.THURSDAY;
				break;
			case 4://星期五
				tv_week.setText("星期五");
				curWeek=Calendar.FRIDAY;
				break;
			case 5://星期六
				tv_week.setText("星期六");
				curWeek=Calendar.SATURDAY;
				break;								
			case 6://星期日
				tv_week.setText("星期日");
				curWeek=Calendar.SUNDAY;
				break;					
			default:
				break;
			}
			TextView tv_plan=(TextView)listview_gaps_item.findViewById(R.id.tv_plan);
			tv_plan.setText(
					String.valueOf(book.weekPlans.get(Integer.valueOf(curWeek)).GetAutoPlan(book)));
			TextView tv_planUnit=(TextView)listview_gaps_item.findViewById(R.id.tv_planUnit);
			tv_planUnit.setText(book.unitStr);
			return listview_gaps_item;
		}

		@Override
		public int getChildrenCount(int arg0) {
			// TODO Auto-generated method stub
			return 7;
		}

		@Override
		public Object getGroup(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View arg2,
				ViewGroup arg3) {
			// TODO Auto-generated method stub
			TextView myText = null; 
			if (arg2 != null) {    
                myText = (TextView)arg2;    
                myText.setText("周进度计划表"); 
                myText.setPadding(50, 0, 0, 0);
                myText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            } else {    
                myText = new TextView(this.context);
            }    
            return myText;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}
    	
    }
    */
}
