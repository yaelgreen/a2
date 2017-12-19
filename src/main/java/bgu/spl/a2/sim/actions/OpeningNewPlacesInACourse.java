package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

public class OpeningNewPlacesInACourse extends Action{
	
	//Behavior: This action should increase the number of available spaces for the course.
	//Actor: Must be initially submitted to the course's actor.
	
	private String course;
	private int increase;
	
	public OpeningNewPlacesInACourse(String course, int inc) {
		this.course = course;
		this.increase = inc;
	}

	@Override
	protected void start() {
		// TODO Auto-generated method stub
		
	}

}
