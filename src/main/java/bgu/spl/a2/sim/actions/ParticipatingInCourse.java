package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

public class ParticipatingInCourse extends Action {
	
	//Behavior: This action should try to register the student in the course, if it succeeds, should add the
	//course to the grades sheet of the student, and give him a grade if supplied. See the input example.
	//Actor: Must be initially submitted to the course's actor.
	
	private String course;
	private String student;

	public ParticipatingInCourse(String student, String course) {
		this.course = course;
		this.student = student;
	}

	@Override
	protected void start() {
		// TODO Auto-generated method stub
		
	}

}
