package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

public class CloseACourse extends Action{
	
	//Behavior: This action should close a course. Should unregister all the registered students in the
	//course and remove the course from the department courses' list and from the grade sheets of the
	//students. The number of available spaces of the closed course should be updated to -1. DO NOT
	//remove its actor. After closing the course, all the request for registration should be denied.
	//Actor: Must be initially submitted to the department's actor.
	
	private String course;
	
	public CloseACourse(String course) {
		this.course = course;	
	}

	@Override
	protected void start() {
		// TODO Auto-generated method stub
		
	}

}
