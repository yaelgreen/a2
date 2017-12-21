package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Register extends Action {
	
	String course;
	int grade;

	public Register(String course, String[] grades) {
		this.course = course;
		grade = Integer.parseInt(grades[0]);
	}

	@Override
	protected void start() {
		StudentPrivateState myState = (StudentPrivateState) this.state;
		myState.getGrades().put(course, grade);
		complete(course);
	}

}
