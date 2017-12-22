package bgu.spl.a2.sim.actions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class CreateNewCourse extends Action<Boolean> {
	
	private int availableSpaces;
	private List<String> prerequisites;

	public CreateNewCourse(int availableSpaces, List<String> prerequisites) {
		this.availableSpaces = availableSpaces;
		this.prerequisites = prerequisites;
	}

	@Override
	protected void start() {
		CoursePrivateState myState = (CoursePrivateState) this.state;
		myState.setAvailableSpots(availableSpaces);
		myState.setPrequisites(prerequisites);
		complete(true);
	}

}
