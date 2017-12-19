package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

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
		// TODO Auto-generated method stub
		
	}

}
