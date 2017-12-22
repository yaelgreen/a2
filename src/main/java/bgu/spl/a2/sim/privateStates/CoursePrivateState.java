package bgu.spl.a2.sim.privateStates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState implements Serializable{
	
	private int availableSpots = -1;
	private int registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		availableSpots = 0;
		registered = 0;
		regStudents = new ArrayList<String>();
		prequisites= new ArrayList<String>();
	}

	public void setAvailableSpots(int availableSpots) {
		this.availableSpots = availableSpots;
	}

	public void setRegistered(int registered) {
		this.registered = registered;
	}

	public void setRegStudents(List<String> regStudents) {
		this.regStudents = regStudents;
	}

	public void setPrequisites(List<String> prequisites) {
		this.prequisites = prequisites;
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
