package com.example.vocabularyschedule;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

//import android.util.Log;
import android.widget.ImageView;

public class Book implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2483112553973876359L;
	static int PAGE=0;
	static int COST=1;
	static int VOCABULARY=2;
	static int PART=3;
	
	
	int totalDays;
	int maxDailyTotalTasks;
	int reviewModeId;
	int id;
	String title;
	int partUnit;
	String unitStr;
	ImageView cover;
	int perPageAvgWords;
	int perPartAvgPages;
	int startPos;
	int endPos;
	int total;
	int totalPages;
	Vector<Gap> gapPages;
	Calendar startDate;
	Vector<Plan> dayPlans;
	Hashtable<Integer,Plan> weekPlans;
	Vector<Section> sections;
	
	public Book() {
		// TODO Auto-generated constructor stub
		
		//3
		this.id=1;
		this.title="新东方词汇书";
		this.unitStr="页";
		this.perPartAvgPages=12;
		this.perPageAvgWords=12;
		this.reviewModeId=0;
		
		//1
		gapPages=new Vector<Gap>();
		sections=new Vector<Section>();
		this.startPos=1;
		this.endPos=50;
		this.partUnit=this.VOCABULARY;
		/*
		gapPages.add(new Gap(2,3));
		gapPages.add(new Gap(20,23));
		gapPages.add(new Gap(40,40));
		*/
		this.UpdateSections();
		
		//2
		dayPlans=new Vector<Plan>();
		dayPlans.add(new Plan(400));
		dayPlans.add(new Plan(200));
		
		weekPlans=new Hashtable<Integer,Plan>();
		weekPlans.put(Integer.valueOf(Calendar.MONDAY), new Plan(10));
		weekPlans.put(Integer.valueOf(Calendar.TUESDAY), new Plan(20));
		weekPlans.put(Integer.valueOf(Calendar.WEDNESDAY), new Plan(30));
		weekPlans.put(Integer.valueOf(Calendar.THURSDAY), new Plan(40));
		weekPlans.put(Integer.valueOf(Calendar.FRIDAY), new Plan(50));
		weekPlans.put(Integer.valueOf(Calendar.SATURDAY), new Plan(60));
		weekPlans.put(Integer.valueOf(Calendar.SUNDAY), new Plan(70));
		
		startDate=Calendar.getInstance();
		startDate.set(Calendar.HOUR, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		
		this.UpdateDayPlan();
		
	}
	
	void UpdateSections(){
		int curStrat;
		int curGapSum=0;//可能为页，也可能为单元
		sections.clear();
		if (this.partUnit==PART)
			sections.add(new Section(this.startPos,this.endPos));
		else {
			if (gapPages.size()==0)
				sections.add(new Section(this.startPos, this.endPos));
			else {
				curStrat=this.startPos-1;//-1是补偿第一次之用
				Iterator<Gap> gapPagesIt=gapPages.iterator();
				Gap curGap;
				while (gapPagesIt.hasNext()) {
					curGap=gapPagesIt.next();
					sections.add(new Section(curStrat+1, curGap.startPos-1));
					curGapSum+=(curGap.endPos-curGap.startPos+1);
					curStrat=curGap.endPos;
				}
				sections.add(new Section(curStrat+1, this.endPos));
			}
		}
		UpdateTotal(curGapSum);
	}
	
	void UpdateDayPlan(){
		Calendar curDate=Calendar.getInstance();
		curDate.set(this.startDate.get(Calendar.YEAR),this.startDate.get(Calendar.MONTH),this.startDate.get(Calendar.DAY_OF_MONTH),0,0,0);
		Vector<Plan> newDayPlans=(Vector<Plan>)this.dayPlans.clone();
		int curSum=0;
		int remainder=0;
		int curIndex=-1;
		Iterator<Plan> dayPlansIt=newDayPlans.iterator();
		Plan newPlan,curPlan;
		while(dayPlansIt.hasNext()){
			curPlan=dayPlansIt.next();
			curIndex++;
			if (curPlan.GetPlan()!=0){
				curSum+=curPlan.GetAutoPlan(this);
				remainder=curSum-this.total;
				if (remainder>0) {
					curPlan.UpdatePlan(curPlan.GetAutoPlan(this)-remainder, this);
					if(curIndex<(newDayPlans.size()-1))
						VectorPlanRemoveRange(newDayPlans,curIndex+1,newDayPlans.size()-1);
					this.dayPlans.clear();
					this.dayPlans=(Vector<Plan>)newDayPlans.clone();
					break;
				}else {
					if (remainder==0) {
						this.dayPlans.clear();
						this.dayPlans=(Vector<Plan>)newDayPlans.clone();
						break;
					}
				}
			}
			curDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		remainder=curSum-this.totalPages;
		if (remainder<0) {
			while(remainder<0){
				newPlan=new Plan(this.weekPlans.get(curDate.get(Calendar.DAY_OF_WEEK)).GetPlan());
				curSum+=newPlan.GetAutoPlan(this);
				remainder=curSum-this.total;
				if (remainder>0) {//即最后一个添加的过大
					newPlan.UpdatePlan(newPlan.GetAutoPlan(this)-remainder, this);
					this.dayPlans.add(newPlan);
					break;
				}else {
					this.dayPlans.add(newPlan);
					if (remainder==0) 
						break;
				}
				curDate.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
	}

	void VectorPlanRemoveRange(Vector<Plan> vector,int startPos,int endPos){
		for(int curPos=endPos;curPos>=startPos;curPos--)
			vector.removeElementAt(curPos);
	}
	
	void UpdateTotal(int gapPageSum){
		switch (this.partUnit) {
		case 0:
			this.total=(this.endPos-this.startPos+1)-gapPageSum;
			this.totalPages=this.total;
			break;
		case 1:
			this.total=(this.endPos-this.startPos+1)-gapPageSum;
			this.totalPages=this.total;
			break;
		case 2:
			this.total=(this.endPos-this.startPos+1)-gapPageSum;
			this.totalPages=this.total;
			break;
		case 3://单元
			this.total=this.endPos-this.startPos+1;//单元
			this.totalPages=(total*this.perPartAvgPages)-gapPageSum;//页
			break;
		default:
			break;
		}
	}
	
	void SetUnitStr(String unitStr){
		
	}
}
