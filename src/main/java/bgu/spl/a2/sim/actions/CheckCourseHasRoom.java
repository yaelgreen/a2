package bgu.spl.a2.sim.actions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class CheckCourseHasRoom  extends Action<Boolean> {
	
	private String student;
	
	public CheckCourseHasRoom(String student) {
		this.student = student;
	}

	@Override
	protected void start() {
		CoursePrivateState courseState = (CoursePrivateState) state;
		synchronized(courseState) {
			if (courseState.getAvailableSpots() <= 0 ) {
				complete(false);
				return;
			}
			courseState.setRegistered(courseState.getRegistered()+1);
			courseState.setAvailableSpots(courseState.getAvailableSpots()-1);
			List<String> newRegisteredList = courseState.getRegStudents();
			newRegisteredList.add(student);
			courseState.setRegStudents(newRegisteredList);
			complete(true);
		}
	}

}
