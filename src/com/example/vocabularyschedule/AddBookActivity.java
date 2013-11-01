package com.example.vocabularyschedule;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class AddBookActivity extends Activity{
	//滑动页面	
	private ViewPager vp_addBook;//页卡内容
    private List<View> listViews; // Tab页面列表
    
    //滑动页头
    private ImageView iv_cursor;// 动画图片
    private TextView tv_step1, tv_step2, tv_step3, tv_step4;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int bmpW;// 动画图片宽度
    private int currIndex = 0;// 当前页卡编号
    private int totalTabBar=4;
    
    //步骤1
    RadioGroup rg_unit1,rg_unit2,rg_unit3;
    RadioButton rbtn_page,rbtn_cost,rbtn_vocabulary,rbtn_list,rbtn_chapter,rbtn_other;
    private Boolean HasLockedGroup = false;
    public int curUnitSelect;
    EditText et_other,et_bookTitle;
    
    //步骤2
    EditText et_perPageAvgWords,et_perPartAvgPages,et_start,et_end;
    TextView tv_perPartAvgPages_unit,tv_rangeStartUnit,tv_rangeEndUnit;
    LinearLayout lly_perPartAvgPage;
    Button btn_startDate;
    Spinner spn_reviewMode;
    
    //步骤3
    EditText et_monday,et_tuesday,et_wednesday,et_thursday,et_friday,et_saturday,et_sunday;
    TextView tv_mondayUnit,tv_tuesdayUnit,tv_wednesdayUnit,tv_thursdayUnit,tv_fridayUnit,tv_saturdayUnit,tv_sundayUnit;
    
    //步骤4
    Button btn_finish,btn_addGap,btn_undo;
    RadioButton rbtn_setGaps;
    LinearLayout llyt_gaps;
    ListView lv_gaps;
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addbook);
		InitImageView();
		InitTextView();
		InitViewPager();
	}
	
	/**
     * 初始化动画
*/
    private void InitImageView() {
    	iv_cursor = (ImageView) findViewById(R.id.iv_cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
                .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / totalTabBar - bmpW) / 2;// 计算偏移量 
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        iv_cursor.setImageMatrix(matrix);// 设置动画初始位置
    }
    
    /**
     * 初始化头标
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
     * 头标点击监听
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
     * 初始化ViewPager
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
        
        rbtn_page=(RadioButton)curView.findViewById(R.id.rbtn_page);
        rbtn_page.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked==true){
					lly_perPartAvgPage.setVisibility(View.GONE);
					SetRangeUnit("页");
			    	SetWeekPlanUnit("页");
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
					SetRangeUnit("页");
			    	SetWeekPlanUnit("分钟");
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
					SetRangeUnit("页");
			    	SetWeekPlanUnit("个");
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
					tv_perPartAvgPages_unit.setText("章");
					SetRangeUnit("章");
					SetWeekPlanUnit("章");
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
    	lly_perPartAvgPage=(LinearLayout)curView.findViewById(R.id.lly_perPartAvgPage);
    	tv_perPartAvgPages_unit=(TextView)curView.findViewById(R.id.tv_perPartAvgPages_unit);
    	et_perPartAvgPages=(EditText)curView.findViewById(R.id.et_perPartAvgPages);
    	et_start=(EditText)curView.findViewById(R.id.et_start);
    	et_end=(EditText)curView.findViewById(R.id.et_end);
    	tv_rangeStartUnit=(TextView)curView.findViewById(R.id.tv_rangeStartUnit);
    	tv_rangeEndUnit=(TextView)curView.findViewById(R.id.tv_rangeEndUnit);
    	btn_startDate=(Button)curView.findViewById(R.id.btn_startDate);
    	spn_reviewMode=(Spinner)curView.findViewById(R.id.spn_reviewMode);
    	
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
    	btn_addGap=(Button)curView.findViewById(R.id.btn_addGap);
    	btn_undo=(Button)curView.findViewById(R.id.btn_undo);
    	
    	rbtn_setGaps=(RadioButton)curView.findViewById(R.id.rbtn_setGaps);
    	
    	llyt_gaps=(LinearLayout)curView.findViewById(R.id.llyt_gaps);
    	
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
    }
    
    /**
     * ViewPager适配器
*/
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
        	//将初始化的ViewPage存入到本类中
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
     * 页卡切换监听
*/
    public class MyOnPageChangeListener implements OnPageChangeListener {

        int one   = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two   = one * 2;// 页卡1 -> 页卡3 偏移量
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
            animation.setFillAfter(true);// True:图片停在动画结束位置
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
				//被点击的RadioGroup将会执行onCheckedChanged
				//并将HasLockedGroup设置为true（即被锁定）
				//这样被点击的RadioGroup在顺序执行其它未被点击的RadioGroup的clearCheck()方法时
				//不会因它们调用被点击的RadioGroup的clearCheck()方法而导致死循环。
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
}
