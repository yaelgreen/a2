package bgu.spl.a2.sim.actions.courseActions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class CreateNewCourse extends Action<Boolean> {
	
	private int availableSpaces;
	private List<String> prerequisites;
	
	/**
	 * The action sets the course data for the action in the department actor in {@link bgu.spl.a2.sim.actions.departmentActions.OpenANewCourse}
	 * @param availableSpaces the available places in the course
	 * @param prerequisites the needed courses to learn this course
	 */
	public CreateNewCourse(int availableSpaces, List<String> prerequisites) {
		this.availableSpaces = availableSpaces;
		this.prerequisites = prerequisites;
		setActionName("Create New Course");
	}

	@Override
	protected void start() {
		CoursePrivateState myState = (CoursePrivateState) this.currentState;
		myState.setAvailableSpots(availableSpaces);
		myState.setPrequisites(prerequisites);
		complete(true);
	}

}
