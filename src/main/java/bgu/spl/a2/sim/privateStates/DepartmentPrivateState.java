package bgu.spl.a2.sim.privateStates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe department's private state
 */
public class DepartmentPrivateState extends PrivateState implements Serializable{
	
	private List<String> courseList;
	private List<String> studentList;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public DepartmentPrivateState() {
		//are not thread safe DataStructures - MAYBE THIS?->
		//import java.util.concurrent.CopyOnWriteArrayList;
		courseList = new ArrayList<String>();
		studentList = new ArrayList<String>();
	}

	public List<String> getCourseList() {
		return courseList;
	}

	public List<String> getStudentList() {
		return studentList;
	}

}
