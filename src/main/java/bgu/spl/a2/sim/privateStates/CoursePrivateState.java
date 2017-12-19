package bgu.spl.a2.sim.privateStates;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.PrivateState;
import bgu.spl.a2.university.Student;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{
	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		this.availableSpots = 0;
		this.registered = 0;
		this.regStudents = new ArrayList<String>();
		this.prequisites= new ArrayList<String>();
	}

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrequisites() {
		return prequisites;
	}
}
