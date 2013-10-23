package com.example.vocabularyschedule;

import java.io.Serializable;

public class Task implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6175917599672128148L;
	int startPos;
	int endPos;
	boolean isFinish;
	int time;
	int cost;
	int vocabulary;
	int belongBookId;
	String bookTitleStr;
	String unit;
	public Task(
			int startPos,
			int endPos,
			boolean isFinish,
			int time,//´ÎÊý
			int cost,
			int vocabulary,
			Book book			
			) {
		// TODO Auto-generated constructor stub
		this.startPos=startPos;
		this.endPos=endPos;
		this.isFinish=isFinish;
		this.time=time;
		this.cost=cost;
		this.vocabulary=vocabulary;
		this.belongBookId=book.id;
		this.bookTitleStr=book.title;
		this.unit=book.unitStr;		
	}
}
