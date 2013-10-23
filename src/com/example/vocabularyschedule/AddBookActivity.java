package com.example.vocabularyschedule;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class AddBookActivity extends Activity{
	private ViewPager vp_addBook;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView iv_cursor;// 动画图片
    private TextView tv_step1, tv_step2, tv_step3, tv_step4;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int bmpW;// 动画图片宽度
    private int currIndex = 0;// 当前页卡编号
    private int totalTabBar=4;
    RadioGroup rg_unit1,rg_unit2,rg_unit3;
    private Boolean HasLockedGroup = false;
    public int curUnitSelect;
    EditText et_other;
    
    
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
    private void InitViewPager() {

        listViews = new ArrayList<View>();
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
        et_other=(EditText)curView.findViewById(R.id.et_other);
        
        listViews.add(curView);
        listViews.add(mInflater.inflate(R.layout.viewpage_item2, null));
        listViews.add(mInflater.inflate(R.layout.viewpage_item3, null));
        listViews.add(mInflater.inflate(R.layout.viewpage_item4, null));
        
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
