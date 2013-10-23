package com.example.vocabularyschedule;

import java.io.Serializable;

public class Section implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -919254490434687546L;
	int startPos;
	int endPos;
	
	int GetLength(){
		return this.endPos-this.startPos+1;
	}
	
	public Section(int startPos,int endPos) {
		// TODO Auto-generated constructor stub
		this.startPos=startPos;
		this.endPos=endPos;
	}
	
	void Update(int newStartPos) {
		this.startPos=newStartPos;
	}
	
}
