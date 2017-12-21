package bgu.spl.a2.sim.actions;

import java.util.Arrays;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Unregister extends Action{
	
	//Behavior: If the student is enrolled in the course, this action should unregister him (update the
	//list of students of course, remove the course from the grades sheet of the student and increases the
	//number of available spaces).
	//Actor: Must be initially submitted to the course's actor.
	
	private String course;
	private String student;

	public Unregister(String student, String course) {
		this.course = course;
		this.student = student;
	}

	@Override
	protected void start() {
		CoursePrivateState courseState = (CoursePrivateState) this.state;
		if(!Arrays.asList(courseState.getRegStudents()).contains(student)) {
			complete(course);
			return;
		}
		courseState.getRegStudents().remove(student);
		courseState.setAvailableSpots(courseState.getAvailableSpots()+1);
		courseState.setRegistered(courseState.getRegistered()-1);
		sendMessage(new RemoveCourse(course), student, new StudentPrivateState());
		complete(course);
	}

}
