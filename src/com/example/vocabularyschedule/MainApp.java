package com.example.vocabularyschedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.ArrayAdapter;

public class MainApp extends Application{
	Hashtable<Integer, Book> books;
	Hashtable<Integer, ReviewMode> modes;
	
	
	Hashtable<Integer, Hashtable<String, Vector<Task>>> schedules;
	Hashtable<Integer, Vector<Task>> booksTodayTasks;
	
	ArrayList<String> spn_modes_al=new ArrayList<String>();
	Vector<Integer> modesIdVec;//Value-ModeId
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		InitDate();
	}
	
	private void InitDate() {
		books=new Hashtable<Integer, Book>();
		Book newBook=new Book();
		books.put(Integer.valueOf(newBook.id), newBook);
		
		modes=new Hashtable<Integer, ReviewMode>();
		Vector<Integer> periods=new Vector<Integer>();
		periods.add(new Integer(0));
		periods.add(new Integer(1));
		periods.add(new Integer(2));
		periods.add(new Integer(3));
		ReviewMode mode=new ReviewMode(periods,"默认模式名0",0);
		modes.put(Integer.valueOf(0), mode);
		
		periods=new Vector<Integer>();
		periods.add(new Integer(10));
		periods.add(new Integer(11));
		periods.add(new Integer(12));
		periods.add(new Integer(13));
		mode=new ReviewMode(periods,"默认模式名1",1);
		mode.selectedBookIds.add(Integer.valueOf(newBook.id));
		modes.put(Integer.valueOf(1), mode);
		
		GetAllSchedules();
		Log.i("Schedule-GetAllSchedules", String.valueOf(schedules.get(Integer.valueOf(1)).size()));
		GetAllBookTodayTask();
		//当当天没有
		//Log.e("Schedule", String.valueOf(booksTodayTasks.get(Integer.valueOf(1)).size()));
		
		modesIdVec=new Vector<Integer>();
		ResetModesSpinnerData();
	}
	
	void GetBookSchedule(int bookId){
		Book curBook=books.get(new Integer(bookId));
		Calendar curDate=Calendar.getInstance();
		curDate.set(curBook.startDate.get(Calendar.YEAR),curBook.startDate.get(Calendar.MONTH),curBook.startDate.get(Calendar.DAY_OF_MONTH),0,0,0);
		Iterator<Plan> dayPlansIt=curBook.dayPlans.iterator();
		Iterator<Section> sectionsIt=curBook.sections.iterator();
		Plan curPlan;
		Plan curRemainderPlan;
		Section curSection=sectionsIt.next();//每本书至少有一个section
		Section curRemainderSection=new Section(curSection.startPos, curSection.endPos);
		int curMaxDailyTotalTasks=0;
		while(dayPlansIt.hasNext()){
			curPlan=dayPlansIt.next();
			curRemainderPlan=new Plan(curPlan.GetPlan());
			while(curRemainderPlan.GetPlan()>0){
				if (curRemainderSection.GetLength()>=curRemainderPlan.GetAutoPlan(curBook)) {
					curMaxDailyTotalTasks=InsertNewTask(
							curRemainderSection.startPos,
							curRemainderSection.startPos+(curRemainderPlan.GetAutoPlan(curBook)-1),
							curDate,
							modes.get(new Integer(curBook.reviewModeId)),
							curBook
							);
					if (curBook.maxDailyTotalTasks<curMaxDailyTotalTasks)
						curBook.maxDailyTotalTasks=curMaxDailyTotalTasks;
					curRemainderSection.Update(curRemainderSection.startPos+curRemainderPlan.GetAutoPlan(curBook));
					curRemainderPlan.UpdatePlan(0, curBook);
				}else {
					curMaxDailyTotalTasks=InsertNewTask(
							curRemainderSection.startPos, 
							curRemainderSection.endPos, 
							curDate, 
							modes.get(new Integer(curBook.reviewModeId)), 
							curBook);
					if (curBook.maxDailyTotalTasks<curMaxDailyTotalTasks)
						curBook.maxDailyTotalTasks=curMaxDailyTotalTasks;
					curRemainderPlan.UpdatePlan(curRemainderPlan.GetAutoPlan(curBook)-curRemainderSection.GetLength(), curBook);
					curRemainderSection.Update(curRemainderSection.endPos);
				}
				if (curRemainderSection.startPos==curRemainderSection.endPos) {
					curSection=sectionsIt.next();
					curRemainderSection=new Section(curSection.startPos, curSection.endPos);
				}
			}
			if(!dayPlansIt.hasNext())//若当前为最后一个dayPlan
				//SetBookTotalDays(curDate,modes.get(new Integer(curBook.reviewModeId)),curBook);
			curDate.add(Calendar.DAY_OF_MONTH, 1);
		}
	}
	
	int InsertNewTask(int start,int end,Calendar startDate,ReviewMode mode,Book book){
		int maxDailyTotalTasks=0;
		Hashtable<String, Vector<Task>> curBookSchedule=schedules.get(new Integer(book.id));
		String temp="";
		Iterator<Integer> periodsIt=mode.periods.iterator();
		Integer curPeriod;
		int cost=CalculateTaskCost(end-start+1,book);
		int vocabulary=CalculateTaskVocabulary(end-start+1,book);
		int curTime=1;//当前复习周期数
		Calendar curNextDate=Calendar.getInstance();
		curNextDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH),0,0,0);
		
		while(periodsIt.hasNext()){
			curPeriod=new Integer(periodsIt.next());
			curNextDate.add(Calendar.DAY_OF_MONTH, curPeriod.intValue());
			temp+=new Integer(curNextDate.get(Calendar.YEAR)).toString();
			temp+=new Integer(curNextDate.get(Calendar.MONTH)).toString();
			temp+=new Integer(curNextDate.get(Calendar.DAY_OF_MONTH)).toString();
			Task newTask=new Task(
					start,end,false,curTime,cost,vocabulary,book);
			if (curBookSchedule.containsKey(temp)){
				curBookSchedule.get(temp).add(newTask);
				if (maxDailyTotalTasks<curBookSchedule.get(temp).size())
					maxDailyTotalTasks=curBookSchedule.get(temp).size();
			}
			else {
				Vector<Task> newTaskVec=new Vector<Task>();
				newTaskVec.add(newTask);
				curBookSchedule.put(temp, newTaskVec);
				if (maxDailyTotalTasks<1)
					maxDailyTotalTasks=1;
			}
			curTime++;
			temp="";
		}
		return maxDailyTotalTasks;
	}
	
	void GetAllBookTodayTask(){
		booksTodayTasks=new Hashtable<Integer, Vector<Task>>();
		String todayStr="";
		Calendar today=Calendar.getInstance();
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		todayStr+=new Integer(today.get(Calendar.YEAR)).toString();
		todayStr+=new Integer(today.get(Calendar.MONTH)).toString();
		todayStr+=new Integer(today.get(Calendar.DAY_OF_MONTH)).toString();
		Enumeration<Book> booksEnu=books.elements();
		while (booksEnu.hasMoreElements()) {
			Book curBook = (Book) booksEnu.nextElement();
			if (schedules.get(new Integer(curBook.id)).containsKey(todayStr))
				booksTodayTasks.put(new Integer(curBook.id), schedules.get(new Integer(curBook.id)).get(todayStr));
		}
	}
	
	void GetAllSchedules(){
		//新建总表
		schedules=new Hashtable<Integer, Hashtable<String, Vector<Task>>>();
		Enumeration<Book> booksEnu=books.elements();
		while (booksEnu.hasMoreElements()) {
			Book curBook = (Book) booksEnu.nextElement();
			Hashtable<String, Vector<Task>> curBookSchedule=new Hashtable<String, Vector<Task>>();
			schedules.put(curBook.id, curBookSchedule);
			GetBookSchedule(curBook.id);
		}
	}
	
	void SetBookTotalDays(Calendar lastDate,ReviewMode mode,Book book){
		Calendar curDate=Calendar.getInstance();
		curDate.set(book.startDate.get(Calendar.YEAR), book.startDate.get(Calendar.MONTH), book.startDate.get(Calendar.DAY_OF_MONTH),0,0,0);
		String curDateStr="",lastDateStr="";
		curDateStr+=new Integer(curDate.get(Calendar.YEAR)).toString();
		curDateStr+=new Integer(curDate.get(Calendar.MONTH)).toString();
		curDateStr+=new Integer(curDate.get(Calendar.DAY_OF_MONTH)).toString();
		lastDateStr+=new Integer(lastDate.get(Calendar.YEAR)).toString();
		lastDateStr+=new Integer(lastDate.get(Calendar.MONTH)).toString();
		lastDateStr+=new Integer(lastDate.get(Calendar.DAY_OF_MONTH)).toString();
		int curTotalDays=0;
		while(!curDateStr.equals(lastDateStr)){
			curDate.add(Calendar.DAY_OF_MONTH, 1);
			curTotalDays++;
			curDateStr="";
			curDateStr+=new Integer(curDate.get(Calendar.YEAR)).toString();
			curDateStr+=new Integer(curDate.get(Calendar.MONTH)).toString();
			curDateStr+=new Integer(curDate.get(Calendar.DAY_OF_MONTH)).toString();
		}
		Iterator<Integer> periodsIt=mode.periods.iterator();
		Integer curPeriod;
		while(periodsIt.hasNext()){
			curPeriod=new Integer(periodsIt.next());
			curTotalDays+=curPeriod.intValue();
		}
		
		book.totalDays=curTotalDays+1;//+1是加上起始天
	}
	
	int CalculateTaskCost(int length,Book book){
		double doubleLength=(double)length;
		double doublePerPageAvgWords=(double)book.perPageAvgWords;
		double doublePerPartAvgPages=(double)book.perPartAvgPages;
		double perWordAvgMinute=0.3;
		double result;
		switch (book.partUnit) {
		case 3:
			result=doubleLength*doublePerPageAvgWords*perWordAvgMinute;
			return (int)result;
		default:
			result=doubleLength*doublePerPartAvgPages*doublePerPageAvgWords*perWordAvgMinute;
			return (int)result;
		}
	}
	
	int CalculateTaskVocabulary(int length,Book book){
		double doubleLength=(double)length;
		double doublePerPageAvgWords=(double)book.perPageAvgWords;
		double doublePerPartAvgPages=(double)book.perPartAvgPages;
		double result;
		switch (book.partUnit) {
		case 3:
			result=doubleLength*doublePerPageAvgWords;
			return (int)result;
		default:
			result=doubleLength*doublePerPartAvgPages*doublePerPageAvgWords;
			return (int)result;
		}
	}
	
	void ResetModesSpinnerData(){
		modesIdVec.clear();
		spn_modes_al.clear();
		Enumeration<ReviewMode> modesEnu=modes.elements();
		while (modesEnu.hasMoreElements()) {
			ReviewMode curMode = (ReviewMode) modesEnu.nextElement();
			spn_modes_al.add(curMode.name);
			modesIdVec.add(Integer.valueOf(curMode.id));
		}
	}
}
