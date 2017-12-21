package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class UnregisterAll extends Action{
	
	private String course;

	public UnregisterAll(String course) {
		this.course = course;
	}

	@Override
	protected void start() {
		CoursePrivateState myState = (CoursePrivateState) this.state;
		for (String student : myState.getRegStudents()) {
			sendMessage(new Unregister(student, course), course, new StudentPrivateState());
		}
		myState.setAvailableSpots(-1);
		myState.setRegistered(0);
		myState.getRegStudents().clear();
		complete(course);
	}

}
