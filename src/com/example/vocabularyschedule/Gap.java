package com.example.vocabularyschedule;

import java.io.Serializable;

public class Gap implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4691027133431407905L;
	int startPos;
	int endPos;
	public Gap(int startPos,int endPos) {
		// TODO Auto-generated constructor stub
		this.startPos=startPos;
		this.endPos=endPos;
	}
}
