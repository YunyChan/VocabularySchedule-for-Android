package com.example.vocabularyschedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class AddBookActivity extends Activity{
	AddBookActivity activity=this;
	private MainApp mApp;
	
	//����ҳ��	
	private ViewPager vp_addBook;//ҳ������
    private List<View> listViews; // Tabҳ���б�
    
    //����ҳͷ
    private ImageView iv_cursor;// ����ͼƬ
    private TextView tv_step1, tv_step2, tv_step3, tv_step4;// ҳ��ͷ��
    private int offset = 0;// ����ͼƬƫ����
    private int bmpW;// ����ͼƬ���
    private int currIndex = 0;// ��ǰҳ�����
    private int totalTabBar=4;
    
    //����1
    RadioGroup rg_unit1,rg_unit2,rg_unit3;
    RadioButton rbtn_page,rbtn_cost,rbtn_vocabulary,rbtn_list,rbtn_chapter,rbtn_other;
    private Boolean HasLockedGroup = false;
    int curUnitSelect;
    EditText et_other,et_bookTitle;
    
    //����2
    EditText et_perPageAvgWords,et_perPartAvgPages,et_start,et_end;
    TextView tv_perPartAvgPages_unit,tv_rangeStartUnit,tv_rangeEndUnit;
    LinearLayout lly_perPartAvgPage;
    Button btn_startDate;
    Spinner spn_reviewMode;
    private Calendar bookStartDate;
    ArrayAdapter<String> spn_modes_adapter;
    
    //����3
    EditText et_monday,et_tuesday,et_wednesday,et_thursday,et_friday,et_saturday,et_sunday;
    TextView tv_mondayUnit,tv_tuesdayUnit,tv_wednesdayUnit,tv_thursdayUnit,tv_fridayUnit,tv_saturdayUnit,tv_sundayUnit;
    
    //����4
    Button btn_finish,btn_addGap,btn_undo;
    CheckBox cb_setGaps;
    LinearLayout llyt_gapPages;
    ListView lv_gapPages;
    GapsListAdapter lv_gapPages_adapter;
    Vector<Gap> gapPages;
    
  	//private Vector<Operating> operatingSequence;//-2��������,-1����ɾ����>0�����޸�
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addbook);
		mApp=(MainApp)getApplication();
		
		InitImageView();
		InitTextView();
		InitViewPager();
	}
	
	/**
     * ��ʼ������
*/
    private void InitImageView() {
    	iv_cursor = (ImageView) findViewById(R.id.iv_cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
                .getWidth();// ��ȡͼƬ���
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
        offset = (screenW / totalTabBar - bmpW) / 2;// ����ƫ���� 
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        iv_cursor.setImageMatrix(matrix);// ���ö�����ʼλ��
    }
    
    /**
     * ��ʼ��ͷ��
*/
    private void InitTextView() {
    	tv_step1 = (TextView) findViewById(R.id.tv_step1);
    	tv_step2 = (TextView) findViewById(R.id.tv_step2);
    	tv_step3 = (TextView) findViewById(R.id.tv_step3);
    	tv_step4 = (TextView) findViewById(R.id.tv_step4);

    	tv_step1.setOnClickListener(new MyOnClickListener(0));
    	tv_step2.setOnClickListener(new MyOnClickListener(1));
    	tv_step3.setOnClickListener(new MyOnClickListener(2));
    	tv_step4.setOnClickListener(new MyOnClickListener(4));
    }
    
    /**
     * ͷ��������
*/
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
        	vp_addBook.setCurrentItem(index);
        }
    };
    
    /**
     * ��ʼ��ViewPager
*/
    private void InitStepOne() {
    	LayoutInflater mInflater = getLayoutInflater();
    	View curView=mInflater.inflate(R.layout.viewpage_item1, null);
    	rg_unit1=(RadioGroup)curView.findViewById(R.id.rg_unit1);
        rg_unit1.setOnCheckedChangeListener(new
        		UnitRadioGroupOnCheckedChangeListener());
        rg_unit2=(RadioGroup)curView.findViewById(R.id.rg_unit2);
        rg_unit2.setOnCheckedChangeListener(new
        		UnitRadioGroupOnCheckedChangeListener());
        rg_unit3=(RadioGroup)curView.findViewById(R.id.rg_unit3);
        rg_unit3.setOnCheckedChangeListener(new
        		UnitRadioGroupOnCheckedChangeListener());
        
        curUnitSelect=-1;
        
        rbtn_page=(RadioButton)curView.findViewById(R.id.rbtn_page);
        rbtn_page.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked==true){
					lly_perPartAvgPage.setVisibility(View.GONE);
					SetRangeUnit("ҳ");
			    	SetWeekPlanUnit("ҳ");
				}
			}
		});
        rbtn_cost=(RadioButton)curView.findViewById(R.id.rbtn_cost);
        rbtn_cost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked==true){
					lly_perPartAvgPage.setVisibility(View.GONE);
					SetRangeUnit("ҳ");
			    	SetWeekPlanUnit("����");
				}
			}
		});
        rbtn_vocabulary=(RadioButton)curView.findViewById(R.id.rbtn_vocabulary);
        rbtn_vocabulary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked==true){
					lly_perPartAvgPage.setVisibility(View.GONE);
					SetRangeUnit("ҳ");
			    	SetWeekPlanUnit("��");
				}
			}
		});
        rbtn_list=(RadioButton)curView.findViewById(R.id.rbtn_list);
        rbtn_list.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked==true){
					lly_perPartAvgPage.setVisibility(View.VISIBLE);
					tv_perPartAvgPages_unit.setText("List");
					SetRangeUnit("List");
					SetWeekPlanUnit("List");
				}
			}
		});
        rbtn_chapter=(RadioButton)curView.findViewById(R.id.rbtn_chapter);
        rbtn_chapter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked==true){
					lly_perPartAvgPage.setVisibility(View.VISIBLE);
					tv_perPartAvgPages_unit.setText("��");
					SetRangeUnit("��");
					SetWeekPlanUnit("��");
				}
			}
		});
        rbtn_other=(RadioButton)curView.findViewById(R.id.rbtn_other);
        rbtn_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked==true){
					lly_perPartAvgPage.setVisibility(View.VISIBLE);
					tv_perPartAvgPages_unit.setText(et_other.getText().toString());
					SetRangeUnit(et_other.getText().toString());
					SetWeekPlanUnit(et_other.getText().toString());
				}
			}
		});        
        
        et_other=(EditText)curView.findViewById(R.id.et_other);
        et_other.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				Log.d("vocabulary", "onCheckedChanged");
				SetRangeUnit(et_other.getText().toString());
				SetWeekPlanUnit(et_other.getText().toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        et_bookTitle=(EditText)curView.findViewById(R.id.et_bookTitle);

        listViews.add(curView);
	}
    
    private void InitStepTwo() {
    	LayoutInflater mInflater = getLayoutInflater();
    	View curView=mInflater.inflate(R.layout.viewpage_item2, null);
    	
    	et_perPageAvgWords=(EditText)curView.findViewById(R.id.et_perPageAvgWords);
    	et_perPageAvgWords.setKeyListener(new DigitsKeyListener(false,true));
    	
    	lly_perPartAvgPage=(LinearLayout)curView.findViewById(R.id.lly_perPartAvgPage);
    	tv_perPartAvgPages_unit=(TextView)curView.findViewById(R.id.tv_perPartAvgPages_unit);
    	et_perPartAvgPages=(EditText)curView.findViewById(R.id.et_perPartAvgPages);
    	et_perPartAvgPages.setKeyListener(new DigitsKeyListener(false,true));
    	
    	et_start=(EditText)curView.findViewById(R.id.et_start);
    	et_start.setKeyListener(new DigitsKeyListener(false,true));
    	et_end=(EditText)curView.findViewById(R.id.et_end);
    	et_end.setKeyListener(new DigitsKeyListener(false,true));
    	tv_rangeStartUnit=(TextView)curView.findViewById(R.id.tv_rangeStartUnit);
    	tv_rangeEndUnit=(TextView)curView.findViewById(R.id.tv_rangeEndUnit);
    	
    	btn_startDate=(Button)curView.findViewById(R.id.btn_startDate);
    	btn_startDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bookStartDate=Calendar.getInstance();
				new DatePickerDialog(activity,DateSetListener,
						bookStartDate.get(Calendar.YEAR), bookStartDate.get(Calendar.MONTH), bookStartDate.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
    	
    	spn_reviewMode=(Spinner)curView.findViewById(R.id.spn_reviewMode);
    	spn_modes_adapter=new
				ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,mApp.spn_modes_al);
		spn_modes_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spn_reviewMode.setAdapter(spn_modes_adapter);
    	
        listViews.add(curView);
	}
    
    private void InitStepThree() {
    	LayoutInflater mInflater = getLayoutInflater();
    	View curView=mInflater.inflate(R.layout.viewpage_item3, null);
    	et_monday=(EditText)curView.findViewById(R.id.et_monday);
    	et_tuesday=(EditText)curView.findViewById(R.id.et_tuesday);
    	et_wednesday=(EditText)curView.findViewById(R.id.et_wednesday);
    	et_thursday=(EditText)curView.findViewById(R.id.et_thursday);
    	et_friday=(EditText)curView.findViewById(R.id.et_friday);
    	et_saturday=(EditText)curView.findViewById(R.id.et_saturday);
    	et_sunday=(EditText)curView.findViewById(R.id.et_sunday);
    	
    	et_monday.setKeyListener(new DigitsKeyListener(false,true));
    	et_tuesday.setKeyListener(new DigitsKeyListener(false,true));
    	et_wednesday.setKeyListener(new DigitsKeyListener(false,true));
    	et_thursday.setKeyListener(new DigitsKeyListener(false,true));
    	et_friday.setKeyListener(new DigitsKeyListener(false,true));
    	et_saturday.setKeyListener(new DigitsKeyListener(false,true));
    	et_sunday.setKeyListener(new DigitsKeyListener(false,true));
    	
    	tv_mondayUnit=(TextView)curView.findViewById(R.id.tv_mondayUnit);
    	tv_tuesdayUnit=(TextView)curView.findViewById(R.id.tv_tuesdayUnit);
    	tv_wednesdayUnit=(TextView)curView.findViewById(R.id.tv_wednesdayUnit);
    	tv_thursdayUnit=(TextView)curView.findViewById(R.id.tv_thursdayUnit);
    	tv_fridayUnit=(TextView)curView.findViewById(R.id.tv_fridayUnit);
    	tv_saturdayUnit=(TextView)curView.findViewById(R.id.tv_saturdayUnit);
    	tv_sundayUnit=(TextView)curView.findViewById(R.id.tv_sundayUnit);
    	
        listViews.add(curView);
	}
    
    private void InitStepFour() {
    	LayoutInflater mInflater = getLayoutInflater();
    	View curView=mInflater.inflate(R.layout.viewpage_item4, null);
    	btn_finish=(Button)curView.findViewById(R.id.btn_finish);
    	
    	llyt_gapPages=(LinearLayout)curView.findViewById(R.id.llyt_gapPages);
    	lv_gapPages=(ListView)curView.findViewById(R.id.lv_gapPages);
    	lv_gapPages_adapter=new GapsListAdapter();		
    	lv_gapPages.setAdapter(lv_gapPages_adapter);
    	gapPages=new Vector<Gap>();
    	
    	btn_addGap=(Button)curView.findViewById(R.id.btn_addGap);
    	btn_addGap.setOnClickListener(new OnClickListener() {
    		EditText et_gapStart,et_gapEnd;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!("".equals(et_start.getText().toString()))&&
						!("".equals(et_end.getText().toString()))){
		    		LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.dialog_set_gap,(ViewGroup)findViewById(R.id.dlg_gap));
					AlertDialog.Builder builder = new Builder(activity);
					builder.setTitle("�������¼����Χ");
					builder.setIcon(android.R.drawable.ic_dialog_info);
					et_gapStart=(EditText)layout.findViewById(R.id.et_gapStart);
					et_gapEnd=(EditText)layout.findViewById(R.id.et_gapEnd);
					et_gapStart.setKeyListener(new DigitsKeyListener(false,true));
					et_gapEnd.setKeyListener(new DigitsKeyListener(false,true));
					builder.setView(layout);
					builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){
		
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							
							String et_gapStart_contant=et_gapStart.getText().toString();
							String et_gapEnd_contant=et_gapEnd.getText().toString();
							if (!("".equals(et_gapStart_contant))&&!("".equals(et_gapEnd_contant))) {
								int gapStart=Integer.parseInt(et_gapStart_contant);
								int gapEnd=Integer.parseInt(et_gapEnd_contant);
								if (gapStart<=gapEnd) {
									if (gapStart>=Integer.parseInt(et_start.getText().toString())&&
											gapEnd<=Integer.parseInt(et_end.getText().toString())) {
										Gap newGap=new Gap(Integer.parseInt(et_gapStart_contant),Integer.parseInt(et_gapEnd_contant));								
										if(lv_gapPages_adapter.AddNewGap(lv_gapPages_adapter.getCount(),newGap));
											arg0.dismiss();
									}else
										Toast.makeText(getApplicationContext(),"�������鱾�ı��з�Χ������������Χ���߱��з�Χ��",Toast.LENGTH_LONG).show();
								}else
									Toast.makeText(getApplicationContext(),"��ʼ��Χ����С�ڽ�����Χ��",Toast.LENGTH_LONG).show();
							}else 
								Toast.makeText(getApplicationContext(),"���벻��Ϊ�գ�",Toast.LENGTH_SHORT).show();
						}
						
					});
					builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener(){
		
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();
						}
						
					});
					builder.create().show();
				}else
					Toast.makeText(getApplicationContext(),"��û����ɱ��з�Χ������",Toast.LENGTH_SHORT).show();
			}
		});
    	
    	cb_setGaps=(CheckBox)curView.findViewById(R.id.cb_setGaps);
    	cb_setGaps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked)
					llyt_gapPages.setVisibility(View.VISIBLE);
				else
					llyt_gapPages.setVisibility(View.GONE);		
			}
		});
    	
        listViews.add(curView);
	}
    
    private void SetRangeUnit(String unitStr) {
    	tv_rangeStartUnit.setText(unitStr);
    	tv_rangeEndUnit.setText(unitStr);
	}
    
    private void SetWeekPlanUnit(String unitStr) {
    	tv_mondayUnit.setText(unitStr);
    	tv_tuesdayUnit.setText(unitStr);
    	tv_wednesdayUnit.setText(unitStr);
    	tv_thursdayUnit.setText(unitStr);
    	tv_fridayUnit.setText(unitStr);
    	tv_saturdayUnit.setText(unitStr);
    	tv_sundayUnit.setText(unitStr);
	}
    
    private void InitViewPager() {
        listViews = new ArrayList<View>();
        
        InitStepOne();
        InitStepTwo();
        InitStepThree();
        InitStepFour();
        
        vp_addBook = (ViewPager) findViewById(R.id.vp_addBook);
        vp_addBook.setAdapter(new MyPagerAdapter(listViews));
        vp_addBook.setCurrentItem(0);
        vp_addBook.setOnPageChangeListener(new MyOnPageChangeListener());
        
        rbtn_page.setChecked(true);
        llyt_gapPages.setVisibility(View.GONE);
    }
    
    DatePickerDialog.OnDateSetListener DateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			bookStartDate.set(Calendar.YEAR, arg1);
			bookStartDate.set(Calendar.MONTH, arg2);
			bookStartDate.set(Calendar.DAY_OF_MONTH, arg3);
			String dateStr="";
			dateStr+=String.valueOf(arg1)+"-";
			dateStr+=String.valueOf(arg2+1)+"-";
			dateStr+=String.valueOf(arg3);
			btn_startDate.setText(dateStr);
		}
    };
    
    /**
     * ViewPager������
*/
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
        	//����ʼ����ViewPage���뵽������
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }
    
    /**
     * ҳ���л�����
*/
    public class MyOnPageChangeListener implements OnPageChangeListener {

        int one   = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
        int two   = one * 2;// ҳ��1 -> ҳ��3 ƫ����
        int three = one * 3;

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }else if (currIndex == 3) {
                	animation = new TranslateAnimation(three, 0, 0, 0);
				}
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, one, 0, 0);
                }else if (currIndex == 3) {
                	animation = new TranslateAnimation(three, one, 0, 0);
				}
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                } else if (currIndex == 3) {
                	animation = new TranslateAnimation(three, two, 0, 0);
				}
                break;
            case 3:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, three, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, three, 0, 0);
                }else if (currIndex == 2) {
                	animation = new TranslateAnimation(two, three, 0, 0);
				}
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
            animation.setDuration(300);
            iv_cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
    
    class UnitRadioGroupOnCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			if (!HasLockedGroup) {
				//�������RadioGroup����ִ��onCheckedChanged
				//����HasLockedGroup����Ϊtrue������������
				//�����������RadioGroup��˳��ִ������δ�������RadioGroup��clearCheck()����ʱ
				//���������ǵ��ñ������RadioGroup��clearCheck()������������ѭ����
				HasLockedGroup=true;
                if (arg0 == rg_unit1) {
                	rg_unit2.clearCheck();
                	rg_unit3.clearCheck();
                	et_other.setFocusable(false);
                	if(arg1==R.id.rbtn_page)
                		curUnitSelect=0;
                	else
                		curUnitSelect=1;
                } else if (arg0 == rg_unit2) {
                	Log.d("RadioGroup", "unit2");
                	rg_unit1.clearCheck();
                	rg_unit3.clearCheck();
                	et_other.setFocusable(false);
                	if(arg1==R.id.rbtn_vocabulary)
                		curUnitSelect=2;
                	else
                		curUnitSelect=3;
                } else if (arg0 == rg_unit3) {
                	rg_unit1.clearCheck();
                	rg_unit2.clearCheck();
                	if(arg1==R.id.rbtn_chapter){
                		curUnitSelect=4;
                		et_other.setFocusable(false);
                	}else {
                		curUnitSelect=5;
                		et_other.setFocusableInTouchMode(true);
                	}
                }
                HasLockedGroup=false;
			}
		}
    	
    }
    
	class GapsListAdapter extends BaseAdapter{
		private List<Gap> curShowList=new ArrayList<Gap>();
		
		public GapsListAdapter() {  
        }
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return curShowList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return curShowList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView==null) {
				String inflater=Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater layoutInflater=(LayoutInflater)activity.getSystemService(inflater);
				convertView=layoutInflater.inflate(R.layout.listview_gaps_item,null);
				holder=new ViewHolder();
				holder.tv_gapIndex=(TextView)convertView.findViewById(R.id.tv_gapIndex);
				holder.tv_gapStart=(TextView)convertView.findViewById(R.id.tv_gapStart);
				holder.tv_gapEnd=(TextView)convertView.findViewById(R.id.tv_gapEnd);
				holder.ibtn_editGap=(ImageButton)convertView.findViewById(R.id.ibtn_editGap);
				holder.ibtn_deleteGap=(ImageButton)convertView.findViewById(R.id.ibtn_deleteGap);
				convertView.setTag(holder);
			}else
				holder = (ViewHolder) convertView.getTag();
						
			holder.tv_gapIndex.setText(String.valueOf(position+1)); 
			holder.tv_gapStart.setText(String.valueOf(curShowList.get(position).startPos));
			holder.tv_gapEnd.setText(String.valueOf(curShowList.get(position).endPos));
			
			holder.ibtn_editGap.setOnClickListener(new OnClickListener() {
				EditText et_gapStart,et_gapEnd;
				Gap oldGap;
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final int position = lv_gapPages.getPositionForView(v);
					if (position != ListView.INVALID_POSITION) {
			            //DO THE STUFF YOU WANT TO DO WITH THE position
						LayoutInflater inflater = getLayoutInflater();
						View layout = inflater.inflate(R.layout.dialog_set_gap,(ViewGroup)findViewById(R.id.dlg_gap));
						AlertDialog.Builder builder = new Builder(activity);
						builder.setTitle("��༭�����Χ");
						builder.setIcon(android.R.drawable.ic_dialog_info);
						et_gapStart=(EditText)layout.findViewById(R.id.et_gapStart);
						et_gapEnd=(EditText)layout.findViewById(R.id.et_gapEnd);
						oldGap=curShowList.get(position);
						et_gapStart.setText(String.valueOf(oldGap.startPos));
						et_gapEnd.setText(String.valueOf(oldGap.endPos));
						et_gapStart.setKeyListener(new DigitsKeyListener(false,true));
						et_gapEnd.setKeyListener(new DigitsKeyListener(false,true));
						builder.setView(layout);
						builder.setPositiveButton("ȷ��",new android.content.DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								String et_gapStart_contant=et_gapStart.getText().toString();
								String et_gapEnd_contant=et_gapEnd.getText().toString();
								if (!("".equals(et_gapStart_contant))&&!("".equals(et_gapEnd_contant))) {
									int gapStart=Integer.parseInt(et_gapStart_contant);
									int gapEnd=Integer.parseInt(et_gapEnd_contant);
									if (gapStart<=gapEnd) {
										if (gapStart>=Integer.parseInt(et_start.getText().toString())&&
												gapEnd<=Integer.parseInt(et_end.getText().toString())) {
											Gap newGap=new Gap(Integer.parseInt(et_gapStart_contant),
													Integer.parseInt(et_gapEnd_contant));
											if (SetGap(position,newGap))
												dialog.dismiss();
										}else
											Toast.makeText(getApplicationContext(),"�������鱾�ı��з�Χ������������Χ���߱��з�Χ��",Toast.LENGTH_LONG).show();
									}else
										Toast.makeText(getApplicationContext(),"��ʼ��Χ����С�ڽ�����Χ��",Toast.LENGTH_LONG).show();
								}else
									Toast.makeText(getApplicationContext(),"���벻��Ϊ�գ�",Toast.LENGTH_SHORT).show();
							}

						});
						builder.setNegativeButton("ȡ��",new android.content.DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}

						});						
						builder.create().show();
			        }
				}
			});
			
			holder.ibtn_deleteGap.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final int position = lv_gapPages.getPositionForView(v);
					if (position != ListView.INVALID_POSITION) {
			            //DO THE STUFF YOU WANT TO DO WITH THE position
						RemoveGap(position);
						gapPages.remove(position);
			        }
				}
			});
						
			return convertView;
		}
		
		public boolean AddNewGap(int position,Gap newGap) {
			int insertIndex=GetInsertIndex(newGap);
			if(insertIndex!=-1){
				curShowList.add(insertIndex, newGap);
				gapPages.add(insertIndex, newGap);
				notifyDataSetChanged();
				return true;
			}
			return false;
		}
		
		public void RemoveGap(int position) {
			curShowList.remove(position);
			notifyDataSetChanged();
		}
		
		public boolean SetGap(int position,Gap gap) {
			Vector<Gap> tmpGapPages=(Vector<Gap>)gapPages.clone();
			gapPages.remove(position);
			int insertIndex=GetInsertIndex(gap);
			if (insertIndex!=-1) {
				curShowList.remove(position);
				curShowList.add(insertIndex, gap);
				gapPages.add(insertIndex, gap);
				notifyDataSetChanged();
				return true;
			}else{
				gapPages=tmpGapPages;
				return false;
			}
		}
		
		public int GetInsertIndex(Gap gap) {
			Iterator<Gap> gapPagesIt=gapPages.iterator();
			int curIndex=0;
			while (gapPagesIt.hasNext()) {
				Gap curGap = (Gap) gapPagesIt.next();
				if (gap.endPos<curGap.startPos) {
					return curIndex;
				}
				if (gap.startPos>curGap.endPos){
					curIndex++;
					continue;
				}
				if (gap.startPos>=curGap.startPos&&gap.startPos<=curGap.endPos){
					Toast.makeText(getApplicationContext(),"�����Χ���ô��󣬸����˵�"+String.valueOf(curIndex+1)+"�����",Toast.LENGTH_LONG).show();
					return -1;
				}
				if (gap.endPos>=curGap.startPos&&gap.endPos<=curGap.endPos){
					Toast.makeText(getApplicationContext(),"�����Χ���ô��󣬸����˵�"+String.valueOf(curIndex+1)+"�����",Toast.LENGTH_LONG).show();
					return -1;
				}
				if (gap.startPos<curGap.startPos&&gap.endPos>curGap.endPos){
					Toast.makeText(getApplicationContext(),"�����Χ���ô��󣬸����˵�"+String.valueOf(curIndex+1)+"�����",Toast.LENGTH_LONG).show();
					return -1;
				}
			}
			return curIndex;
		}
	}
	
	static class ViewHolder{
		TextView tv_gapIndex;
		TextView tv_gapStart,tv_gapEnd;
		ImageButton ibtn_editGap;
		ImageButton ibtn_deleteGap;
	}
}
