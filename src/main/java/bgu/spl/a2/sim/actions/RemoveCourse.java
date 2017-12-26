package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RemoveCourse extends Action<Boolean>{
	
	private String course;

	/**
	 * called from the student actor
	 * remove a student from a course- deleting the course from its grade list
	 * @param course - the course to remove
	 */
	public RemoveCourse(String course) {
		this.course = course;
		setActionName("Remove Course");
	}

	@Override
	protected void start() {
		StudentPrivateState myState = (StudentPrivateState) currentState;
		myState.getGrades().remove(course);
		complete(true);
	}

}
