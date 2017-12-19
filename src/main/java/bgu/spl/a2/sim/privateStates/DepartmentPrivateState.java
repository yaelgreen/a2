package bgu.spl.a2.sim.privateStates;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.PrivateState;
import bgu.spl.a2.university.Course;
import bgu.spl.a2.university.Student;

/**
 * this class describe department's private state
 */
public class DepartmentPrivateState extends PrivateState{
	private List<String> courseList;
	private List<String> studentList;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public DepartmentPrivateState() {
		this.courseList = new ArrayList<String>();
		this.studentList = new ArrayList<String>();
	}

	public List<String> getCourseList() {
		return courseList;
	}

	public List<String> getStudentList() {
		return studentList;
	}

}
