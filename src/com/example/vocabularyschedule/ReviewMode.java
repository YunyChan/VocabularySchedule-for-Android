package com.example.vocabularyschedule;

import java.io.Serializable;
import java.util.Vector;

public class ReviewMode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8519426387785752245L;
	Vector<Integer> periods;
	String name;
	int id;
	Vector<Integer> selectedBookIds;
	public ReviewMode(Vector<Integer> periods,String name,int id) {
		// TODO Auto-generated constructor stub
		this.periods=periods;
		this.name=name;
		this.id=id;
		this.selectedBookIds=new Vector<Integer>();
	}
	
}
