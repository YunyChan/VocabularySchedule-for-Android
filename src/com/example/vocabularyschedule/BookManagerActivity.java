package com.example.vocabularyschedule;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class BookManagerActivity extends Activity{
	BookManagerActivity activity=this;
	private MainApp mApp;
	private ImageButton ibtn_deleteBook,ibtn_addBook;
	private ImageButton ibtn_editBookTitle,ibtn_editPerPageAvgWords,ibtn_editPerPartAvgPages,ibtn_editRange,ibtn_editStartDate,ibtn_editReviewMode,ibtn_editWeekPlans,ibtn_editGapPages;
	
	Spinner spn_books;
	ArrayAdapter<String> spn_books_adapter;
	Integer curSelectedBookId;
	

	private TextView tv_bookTitle,tv_partUnit;
	private TextView tv_perPageAvgWords;
	private LinearLayout llyt_perPartAvgPages;
	private TextView tv_perPartAvgPages_unit,tv_perPartAvgPages;
	private TextView tv_rangeStart,tv_rangeStart_unit,tv_rangeEnd,tv_rangeEnd_unit;
	private TextView tv_year,tv_month,tv_day;
	private TextView tv_bookReviewMode;
	private int reviewModeSelectedIndex;
	private TextView tv_weekPlans;
	private TextView tv_gapPages;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books_manager);
		
		mApp=(MainApp)getApplication();
		InitBookInformationTextView();
		InitBooksSpinner();
		InitIbtnAddBook();
		InitIbtnDeleteBook();
		InitIbtnEditBookTitle();
		InitIbtnEditPerPageAvgWords();
		InitIbtnEditPerPartAvgPages();//
		InitIbtnEditRange();
		InitIbtnStartDate();
		InitIbtnEditReviewMode();
		InitIbtnEditWeekPlan();
		InitIbtnEditGapPage();
	}
	
	public void InitBooksSpinner(){
		spn_books=(Spinner)findViewById(R.id.spn_books);
		SetSpinnerAdapter();
		spn_books.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				ChangeBookInformation(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
    
    private void InitIbtnAddBook(){
    	ibtn_addBook=(ImageButton)findViewById(R.id.ibtn_addBook);
		ibtn_addBook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(activity, AddBookActivity.class));
			}
		});
    }
    
    private void InitIbtnDeleteBook(){
    	ibtn_deleteBook=(ImageButton)findViewById(R.id.ibtn_deleteBook);
    	ibtn_deleteBook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Book curSelectedBook=mApp.books.get(curSelectedBookId);
				mApp.modes.get(Integer.valueOf(curSelectedBook.reviewModeId)).selectedBookIds.remove(curSelectedBookId);
				mApp.books.remove(curSelectedBookId);
				spn_books_adapter.clear();
				mApp.ResetBooksSpinnerData();
				SetSpinnerAdapter();				
			}
		});
    	
    }
    
    private void InitIbtnEditBookTitle(){
    	ibtn_editBookTitle=(ImageButton)findViewById(R.id.ibtn_editBookTitle);
    	ibtn_editBookTitle.setOnClickListener(new OnClickListener() {
    		EditText et_bookTitle;
    		Book curSelectBook;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				curSelectBook=mApp.books.get(curSelectedBookId);
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("词汇书的名词");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				et_bookTitle=new EditText(activity);
				et_bookTitle.setText(curSelectBook.title);
				builder.setView(et_bookTitle);
				builder.setPositiveButton("确定",  new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String et_bookTitle_contant=et_bookTitle.getText().toString();
						if (!"".equals(et_bookTitle_contant)) {
							curSelectBook.title=et_bookTitle_contant;
							int curSpinnerIndex=spn_books.getSelectedItemPosition();
							spn_books_adapter.clear();
							mApp.ResetBooksSpinnerData();
							SetSpinnerAdapter();
							spn_books.setSelection(curSpinnerIndex);
							arg0.dismiss();
						}else
							Toast.makeText(getApplicationContext(),"词汇书名不能为空！",Toast.LENGTH_SHORT).show();
					}
					
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
				});
				builder.create().show();
			}
		});
    }
    
    private void InitIbtnEditPerPartAvgPages(){
    	ibtn_editPerPartAvgPages=(ImageButton)findViewById(R.id.ibtn_editPerPartAvgPages);
    	ibtn_editPerPartAvgPages.setOnClickListener(new OnClickListener() {
    		EditText et_perPartAvgPages;
    		Book curSelectBook;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				curSelectBook=mApp.books.get(curSelectedBookId);
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("平均每"+curSelectBook.unitStr+"页数");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				et_perPartAvgPages=new EditText(activity);
				et_perPartAvgPages.setText(String.valueOf(curSelectBook.perPageAvgWords));
				builder.setView(et_perPartAvgPages);
				builder.setPositiveButton("确定",  new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String et_perPartAvgPages_contant=et_perPartAvgPages.getText().toString();
						if (!"".equals(et_perPartAvgPages_contant)) {
							curSelectBook.perPartAvgPages=Integer.parseInt(et_perPartAvgPages_contant);
							int curSpinnerIndex=spn_books.getSelectedItemPosition();
							spn_books_adapter.clear();
							mApp.ResetBooksSpinnerData();
							SetSpinnerAdapter();
							spn_books.setSelection(curSpinnerIndex);
							arg0.dismiss();
						}else
							Toast.makeText(getApplicationContext(),"平均每"+curSelectBook.unitStr+"页数",Toast.LENGTH_SHORT).show();
					}
					
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
				});
				builder.create().show();
			}
		});
    	
    }
    
    private void InitIbtnEditPerPageAvgWords(){
    	ibtn_editPerPageAvgWords=(ImageButton)findViewById(R.id.ibtn_editPerPageAvgWords);
    	ibtn_editPerPageAvgWords.setOnClickListener(new OnClickListener() {
    		EditText et_perPageAvgWords;
    		Book curSelectBook;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				curSelectBook=mApp.books.get(curSelectedBookId);
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("平均每页单词数");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				et_perPageAvgWords=new EditText(activity);
				et_perPageAvgWords.setText(String.valueOf(curSelectBook.perPageAvgWords));
				builder.setView(et_perPageAvgWords);
				builder.setPositiveButton("确定",  new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String et_perPageAvgWords_contant=et_perPageAvgWords.getText().toString();
						if (!"".equals(et_perPageAvgWords_contant)) {
							curSelectBook.perPageAvgWords=Integer.parseInt(et_perPageAvgWords_contant);
							int curSpinnerIndex=spn_books.getSelectedItemPosition();
							spn_books_adapter.clear();
							mApp.ResetBooksSpinnerData();
							SetSpinnerAdapter();
							spn_books.setSelection(curSpinnerIndex);
							arg0.dismiss();
						}else
							Toast.makeText(getApplicationContext(),"平均每页单词数不能为空！",Toast.LENGTH_SHORT).show();
					}
					
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
				});
				builder.create().show();
			}
		});
    }
    
    private void InitIbtnEditRange(){
    	ibtn_editRange=(ImageButton)findViewById(R.id.ibtn_editRange);
    	ibtn_editRange.setOnClickListener(new OnClickListener() {
    		EditText et_edit_rangeStart,et_edit_rangeEnd;
    		Book curSelectBook;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				curSelectBook=mApp.books.get(curSelectedBookId);
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.dialog_set_range,(ViewGroup)findViewById(R.id.dlg_range));
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("设置背诵范围");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				et_edit_rangeStart=(EditText)layout.findViewById(R.id.et_edit_rangeStart);
				et_edit_rangeEnd=(EditText)layout.findViewById(R.id.et_edit_rangeEnd);
				et_edit_rangeStart.setKeyListener(new DigitsKeyListener(false,true));
				et_edit_rangeEnd.setKeyListener(new DigitsKeyListener(false,true));
				et_edit_rangeStart.setText(String.valueOf(curSelectBook.startPos));
				et_edit_rangeEnd.setText(String.valueOf(curSelectBook.endPos));
				builder.setView(layout);
				builder.setPositiveButton("确定",  new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String et_rangeStad_contant=et_edit_rangeStart.getText().toString();
						String et_rangeEnd_contant=et_edit_rangeEnd.getText().toString();
						if ((!"".equals(et_rangeStad_contant))&&(!"".equals(et_rangeEnd_contant))) {
							curSelectBook.startPos=Integer.parseInt(et_rangeStad_contant);
							curSelectBook.endPos=Integer.parseInt(et_rangeEnd_contant);
							int curSpinnerIndex=spn_books.getSelectedItemPosition();
							spn_books_adapter.clear();
							mApp.ResetBooksSpinnerData();
							SetSpinnerAdapter();
							spn_books.setSelection(curSpinnerIndex);
							arg0.dismiss();
						}else
							Toast.makeText(getApplicationContext(),"背诵范围设置不能为空！",Toast.LENGTH_SHORT).show();
					}
					
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
				});
				builder.create().show();
			}
		});
    }
    
    private void InitIbtnStartDate(){  	
    	ibtn_editStartDate=(ImageButton)findViewById(R.id.ibtn_editStartDate);
		ibtn_editStartDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Book curSelectedBook=mApp.books.get(Integer.valueOf(curSelectedBookId));
				new DatePickerDialog(activity,mDateSetListener,
						curSelectedBook.startDate.get(Calendar.YEAR), curSelectedBook.startDate.get(Calendar.MONTH), curSelectedBook.startDate.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
    }
    
	DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			Book curSelectedBook=mApp.books.get(Integer.valueOf(curSelectedBookId));
			curSelectedBook.startDate.set(Calendar.YEAR, arg1);
			curSelectedBook.startDate.set(Calendar.MONTH, arg2);
			curSelectedBook.startDate.set(Calendar.DAY_OF_MONTH, arg3);
			tv_year.setText(String.valueOf(arg1));
			tv_month.setText(String.valueOf(arg2+1));
			tv_day.setText(String.valueOf(arg3));
		}
    };
    
    private void InitIbtnEditReviewMode(){
    	ibtn_editReviewMode=(ImageButton)findViewById(R.id.ibtn_editReviewMode);
    	ibtn_editReviewMode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("复习模式设置");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				Integer curSelectedBookModeId=Integer.valueOf(mApp.books.get(curSelectedBookId).reviewModeId);
				String[] items = new String[mApp.spn_modes_al.size()];
				mApp.spn_modes_al.toArray(items);
				builder.setSingleChoiceItems(
						items,
						mApp.modesIdVec.indexOf(curSelectedBookModeId),
						new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								reviewModeSelectedIndex =arg1;
							}
							
				});
				builder.setPositiveButton("确定",  new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						mApp.books.get(curSelectedBookId).reviewModeId=mApp.modesIdVec.elementAt(reviewModeSelectedIndex).intValue();
						int curSpinnerIndex=spn_books.getSelectedItemPosition();
						spn_books_adapter.clear();
						mApp.ResetBooksSpinnerData();
						SetSpinnerAdapter();
						spn_books.setSelection(curSpinnerIndex);
						arg0.dismiss();
					}
					
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
				});
				builder.create().show();
				
			}
		});
    }
    
    private void InitIbtnEditWeekPlan(){
    	ibtn_editWeekPlans=(ImageButton)findViewById(R.id.ibtn_editWeekPlans);
    	ibtn_editWeekPlans.setOnClickListener(new OnClickListener() {
    		EditText et_monday,et_tuesday,et_wednesday,et_thursday,et_friday,et_saturday,et_sunday;
    		TextView tv_mondayUnit,tv_tuesdayUnit,tv_wednesdayUnit,tv_thursdayUnit,tv_fridayUnit,tv_saturdayUnit,tv_sundayUnit;
    		Book curSelectBook;
    		@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
    			curSelectBook=mApp.books.get(curSelectedBookId);
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.dialog_set_weekplan,(ViewGroup)findViewById(R.id.sv_weekplan));
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("设置背诵范围");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				et_monday=(EditText)layout.findViewById(R.id.et_monday);
				et_tuesday=(EditText)layout.findViewById(R.id.et_tuesday);
				et_wednesday=(EditText)layout.findViewById(R.id.et_wednesday);
				et_thursday=(EditText)layout.findViewById(R.id.et_thursday);
				et_friday=(EditText)layout.findViewById(R.id.et_friday);
				et_saturday=(EditText)layout.findViewById(R.id.et_saturday);
				et_sunday=(EditText)layout.findViewById(R.id.et_sunday);
				
				et_monday.setKeyListener(new DigitsKeyListener(false,true));
				et_tuesday.setKeyListener(new DigitsKeyListener(false,true));
				et_wednesday.setKeyListener(new DigitsKeyListener(false,true));
				et_thursday.setKeyListener(new DigitsKeyListener(false,true));
				et_friday.setKeyListener(new DigitsKeyListener(false,true));
				et_saturday.setKeyListener(new DigitsKeyListener(false,true));
				et_sunday.setKeyListener(new DigitsKeyListener(false,true));
				
				et_monday.setText(String.valueOf(curSelectBook.weekPlans.get(Integer.valueOf(Calendar.MONDAY)).GetPlan()));
				et_tuesday.setText(String.valueOf(curSelectBook.weekPlans.get(Integer.valueOf(Calendar.TUESDAY)).GetPlan()));
				et_wednesday.setText(String.valueOf(curSelectBook.weekPlans.get(Integer.valueOf(Calendar.WEDNESDAY)).GetPlan()));
				et_thursday.setText(String.valueOf(curSelectBook.weekPlans.get(Integer.valueOf(Calendar.THURSDAY)).GetPlan()));
				et_friday.setText(String.valueOf(curSelectBook.weekPlans.get(Integer.valueOf(Calendar.FRIDAY)).GetPlan()));
				et_saturday.setText(String.valueOf(curSelectBook.weekPlans.get(Integer.valueOf(Calendar.SATURDAY)).GetPlan()));
				et_sunday.setText(String.valueOf(curSelectBook.weekPlans.get(Integer.valueOf(Calendar.SUNDAY)).GetPlan()));
				
				tv_mondayUnit=(TextView)layout.findViewById(R.id.tv_mondayUnit);
				tv_tuesdayUnit=(TextView)layout.findViewById(R.id.tv_tuesdayUnit);
				tv_wednesdayUnit=(TextView)layout.findViewById(R.id.tv_wednesdayUnit);
				tv_thursdayUnit=(TextView)layout.findViewById(R.id.tv_thursdayUnit);
				tv_fridayUnit=(TextView)layout.findViewById(R.id.tv_fridayUnit);
				tv_saturdayUnit=(TextView)layout.findViewById(R.id.tv_saturdayUnit);
				tv_sundayUnit=(TextView)layout.findViewById(R.id.tv_sundayUnit);	
				
				tv_mondayUnit.setText(curSelectBook.unitStr);
				tv_tuesdayUnit.setText(curSelectBook.unitStr);
				tv_wednesdayUnit.setText(curSelectBook.unitStr);
				tv_thursdayUnit.setText(curSelectBook.unitStr);
				tv_fridayUnit.setText(curSelectBook.unitStr);
				tv_saturdayUnit.setText(curSelectBook.unitStr);
				tv_sundayUnit.setText(curSelectBook.unitStr);
				
				builder.setView(layout);
				
				builder.setPositiveButton("确定",  new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						boolean isEmpty=false;
						String et_monday_content=et_monday.getText().toString();
						String et_tuesday_content=et_tuesday.getText().toString();
						String et_wednesday_content=et_wednesday.getText().toString();
						String et_thursday_content=et_thursday.getText().toString();
						String et_friday_content=et_friday.getText().toString();
						String et_saturday_content=et_saturday.getText().toString();
						String et_sunday_content=et_sunday.getText().toString();
						int monday=0,tuesday=0,wednesday=0,thursday=0,friday=0,saturday=0,sunday=0;
						if (!isEmpty){
							if ("".equals(et_monday_content)) {
								isEmpty=true;
								Toast.makeText(getApplicationContext(),"星期一进度不能为空！",Toast.LENGTH_SHORT).show();
							}else 
								monday=Integer.parseInt(et_monday_content);
						}
						
						if (!isEmpty){
							if ("".equals(et_tuesday_content)) {
								isEmpty=true;
								Toast.makeText(getApplicationContext(),"星期二进度不能为空！",Toast.LENGTH_SHORT).show();
							}else
								tuesday=Integer.parseInt(et_tuesday_content);
						}
						
						if (!isEmpty){
							if ("".equals(et_wednesday_content)) {
								isEmpty=true;
								Toast.makeText(getApplicationContext(),"星期三进度不能为空！",Toast.LENGTH_SHORT).show();
							}else
								wednesday=Integer.parseInt(et_wednesday_content);
						}
						
						if (!isEmpty){
							if ("".equals(et_thursday_content)) {
								isEmpty=true;
								Toast.makeText(getApplicationContext(),"星期四进度不能为空！",Toast.LENGTH_SHORT).show();
							}else
								thursday=Integer.parseInt(et_thursday_content);
						}
						
						if (!isEmpty){
							if ("".equals(et_friday_content)) {
								isEmpty=true;
								Toast.makeText(getApplicationContext(),"星期五进度不能为空！",Toast.LENGTH_SHORT).show();
							}else
								friday=Integer.parseInt(et_friday_content);
						}
						
						if (!isEmpty){
							if ("".equals(et_saturday_content)) {
								isEmpty=true;
								Toast.makeText(getApplicationContext(),"星期六进度不能为空！",Toast.LENGTH_SHORT).show();
							}else
								saturday=Integer.parseInt(et_saturday_content);
						}
						
						if (!isEmpty){
							if ("".equals(et_sunday_content)) {
								isEmpty=true;
								Toast.makeText(getApplicationContext(),"星期日进度不能为空！",Toast.LENGTH_SHORT).show();
							}else
								sunday=Integer.parseInt(et_sunday_content);
						}
						
						if (!isEmpty) {
							curSelectBook.weekPlans.get(Integer.valueOf(Calendar.MONDAY)).SetPlan(monday);
							curSelectBook.weekPlans.get(Integer.valueOf(Calendar.TUESDAY)).SetPlan(tuesday);
							curSelectBook.weekPlans.get(Integer.valueOf(Calendar.WEDNESDAY)).SetPlan(wednesday);
							curSelectBook.weekPlans.get(Integer.valueOf(Calendar.THURSDAY)).SetPlan(thursday);
							curSelectBook.weekPlans.get(Integer.valueOf(Calendar.FRIDAY)).SetPlan(friday);
							curSelectBook.weekPlans.get(Integer.valueOf(Calendar.SATURDAY)).SetPlan(saturday);
							curSelectBook.weekPlans.get(Integer.valueOf(Calendar.SUNDAY)).SetPlan(sunday);
							int curSpinnerIndex=spn_books.getSelectedItemPosition();
							spn_books_adapter.clear();
							mApp.ResetBooksSpinnerData();
							SetSpinnerAdapter();
							spn_books.setSelection(curSpinnerIndex);
							arg0.dismiss();
						}
					}
					
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
				});
				builder.create().show();
			}
		});
    }
    
    private void InitIbtnEditGapPage(){
    	ibtn_editGapPages=(ImageButton)findViewById(R.id.ibtn_editGapPages);
    	ibtn_editGapPages.setOnClickListener(new OnClickListener() {
    		Book curSelectBook;
    		ListView lv_gapPages;
    		Button btn_addGap;
    		@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				curSelectBook=mApp.books.get(curSelectedBookId);
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.dialog_set_gaps,(ViewGroup)findViewById(R.id.llyt_gaps));
				AlertDialog.Builder builder = new Builder(activity);
				builder.setTitle("词汇书间隔页设置");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				lv_gapPages=(ListView)layout.findViewById(R.id.lv_gapPages);
				btn_addGap=(Button)layout.findViewById(R.id.btn_addGap);
				builder.setPositiveButton("确定",  new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
					
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
				});
				builder.create().show();
				
			}
		});
    }
    
    private void InitBookInformationTextView() {
    	tv_bookTitle=(TextView)findViewById(R.id.tv_bookTitle);
    	
    	tv_partUnit=(TextView)findViewById(R.id.tv_partUnit);
    	
    	tv_perPageAvgWords=(TextView)findViewById(R.id.tv_perPageAvgWords);
    	
    	llyt_perPartAvgPages=(LinearLayout)findViewById(R.id.llyt_perPartAvgPages);
    	tv_perPartAvgPages_unit=(TextView)findViewById(R.id.tv_perPartAvgPages_unit);
    	tv_perPartAvgPages=(TextView)findViewById(R.id.tv_perPartAvgPages);  
    	
    	tv_rangeStart=(TextView)findViewById(R.id.tv_rangeStart);
    	tv_rangeStart_unit=(TextView)findViewById(R.id.tv_rangeStart_unit);
    	tv_rangeEnd=(TextView)findViewById(R.id.tv_rangeEnd);
    	tv_rangeEnd_unit=(TextView)findViewById(R.id.tv_rangeEnd_unit);
    	
    	tv_bookReviewMode=(TextView)findViewById(R.id.tv_bookReviewMode);  
    	
    	tv_year=(TextView)findViewById(R.id.tv_year);
		tv_month=(TextView)findViewById(R.id.tv_month);
		tv_day=(TextView)findViewById(R.id.tv_day);	
		
		tv_weekPlans=(TextView)findViewById(R.id.tv_weekPlans); 
	}
    
    private void SetSpinnerAdapter() {
    	spn_books_adapter=new
				ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mApp.spn_books_al);
		spn_books_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_books.setAdapter(spn_books_adapter);
	}
    
    private void ChangeBookInformation(int spinnerSelection){
    	curSelectedBookId=mApp.booksIdVec.elementAt(spinnerSelection);
    	Book curSelectedBook=mApp.books.get(Integer.valueOf(curSelectedBookId));
    	tv_bookTitle.setText(curSelectedBook.title);
    	tv_partUnit.setText(curSelectedBook.unitStr);
    	tv_perPageAvgWords.setText(String.valueOf(curSelectedBook.perPageAvgWords));
    	
    	if (curSelectedBook.partUnit==Book.PART){
    		llyt_perPartAvgPages.setVisibility(View.VISIBLE);
    		tv_perPartAvgPages_unit.setText(curSelectedBook.unitStr);
    		tv_perPartAvgPages.setText(String.valueOf(curSelectedBook.perPartAvgPages));
    	}else
    		llyt_perPartAvgPages.setVisibility(View.GONE);
    	
    	if (curSelectedBook.partUnit==Book.PART) {
			tv_rangeStart_unit.setText(curSelectedBook.unitStr);
			tv_rangeEnd_unit.setText(curSelectedBook.unitStr);
		}else{
			tv_rangeStart_unit.setText("页");
			tv_rangeEnd_unit.setText("页");
		}
    	tv_rangeStart.setText(String.valueOf(curSelectedBook.startPos));
    	tv_rangeEnd.setText(String.valueOf(curSelectedBook.endPos));
    	
    	tv_year.setText(String.valueOf(curSelectedBook.startDate.get(Calendar.YEAR)));
    	tv_month.setText(String.valueOf(curSelectedBook.startDate.get(Calendar.MONTH)+1));
    	tv_day.setText(String.valueOf(curSelectedBook.startDate.get(Calendar.DAY_OF_MONTH)));
    	
    	tv_bookReviewMode.setText(mApp.modes.get(Integer.valueOf(curSelectedBook.reviewModeId)).name);
    	
    	String weekPlanStr="";
    	Enumeration<Plan> weeKPlanEnu=curSelectedBook.weekPlans.elements();
    	while (weeKPlanEnu.hasMoreElements()) {
			Plan plan = (Plan) weeKPlanEnu.nextElement();
			weekPlanStr+=String.valueOf(plan.GetPlan());
			weekPlanStr+=",";
		}
    	tv_weekPlans.setText(weekPlanStr);
    }
}
