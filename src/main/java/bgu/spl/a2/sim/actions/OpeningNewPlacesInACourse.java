package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class OpeningNewPlacesInACourse extends Action<Boolean>{
	
	//Behavior: This action should increase the number of available spaces for the course.
	//Actor: Must be initially submitted to the course's actor.	
	private int increase;
	
	public OpeningNewPlacesInACourse(int inc) {
		increase = inc;
		setActionName("Add Spaces");
	}

	@Override
	protected void start() {
		CoursePrivateState courseState = (CoursePrivateState) state;
		courseState.setAvailableSpots(courseState.getAvailableSpots() + increase);
		complete(true);
	}

}
