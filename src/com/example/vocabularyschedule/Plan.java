package com.example.vocabularyschedule;

import java.io.Serializable;

public class Plan implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8404172335334679176L;
	private int plan;
	double perWordMinute=0.33;
	
	Plan(int plan){
		this.plan=plan;
	}
	
	int GetPlan(){
		return this.plan;
	}
	
	int GetAutoPlan(Book book){//����ҳ���ߵ�Ԫ
		double doublePlan;
		double doublePerPageAvgWords;
		double doubleResult;
		switch (book.partUnit) {
		case 0://ҳ
			return this.plan;
		case 1://��ʱ
			doublePlan=(double)this.plan;
			doublePerPageAvgWords=(double)book.perPageAvgWords;
			doubleResult=Math.ceil((doublePlan/perWordMinute)/doublePerPageAvgWords);
			return (int)doubleResult;
		case 2://�ʻ���
			doublePlan=(double)this.plan;
			doublePerPageAvgWords=(double)book.perPageAvgWords;
			doubleResult=Math.ceil(doublePlan/doublePerPageAvgWords);
			return (int)doubleResult;
		case 3://��Ԫ
			return this.plan;
		default:
			return this.plan;
		}
	}
	
	void UpdatePlan(int newPlan,Book book){
		double doubleNewPlan;
		double doublePerPageAvgWords;
		double doubleResult;
		switch (book.partUnit) {
		case 0:
			this.plan=newPlan;
			break;
		case 1:
			doubleNewPlan=(double)newPlan;
			doublePerPageAvgWords=(double)book.perPageAvgWords;
			doubleResult=Math.ceil(doubleNewPlan*doublePerPageAvgWords*this.perWordMinute);
			this.plan=(int)doubleResult;
			break;
		case 2:
			this.plan=newPlan*book.perPageAvgWords;
			break;
		case 3:
			this.plan=newPlan;
			break;
		default:
			this.plan=newPlan;
			break;
		}
	}
}
