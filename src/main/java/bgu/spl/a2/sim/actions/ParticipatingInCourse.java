package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class ParticipatingInCourse extends Action {
	
	//Behavior: This action should try to register the student in the course, if it succeeds, should add the
	//course to the grades sheet of the student, and give him a grade if supplied. See the input example.
	//Actor: Must be initially submitted to the course's actor.
	
	private String course;
	private String student;
	private String[] grades;

	public ParticipatingInCourse(String student, String course, String[] grade) {
		this.course = course;
		this.student = student;
		this.grades = grade;
	}

	@Override
	protected void start() {
		CoursePrivateState courseState = (CoursePrivateState) this.state;
		if (courseState.getAvailableSpots() != 0) {
			sendMessage(new Register(course, grades),  student, new StudentPrivateState());
		}
		complete(student);
	}

}
