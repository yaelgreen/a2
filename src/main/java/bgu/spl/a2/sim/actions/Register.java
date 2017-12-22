package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Register extends Action<Boolean> {
	
	String course;
	Integer grade;

	public Register(String course, String[] grades) {
		this.course = course;
		this.grade = Integer.parseInt(grades[0]);
	}

	@Override
	protected void start() {
		StudentPrivateState myState = (StudentPrivateState) state;
		myState.getGrades().put(course, grade);
		complete(true);
	}

}
