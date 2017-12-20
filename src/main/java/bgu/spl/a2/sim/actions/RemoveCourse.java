package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RemoveCourse extends Action{
	
	private String course;

	public RemoveCourse(String course) {
		this.course = course;
	}

	@Override
	protected void start() {
		StudentPrivateState myState = (StudentPrivateState) state;
		myState.getGrades().remove(course);
	}

}
