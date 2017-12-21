package bgu.spl.a2.sim.actions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class CreateNewCourse extends Action {
	
	private int availableSpaces;
	private List<String> prerequisites;
	private String courseName;

	public CreateNewCourse(int availableSpaces, List<String> prerequisites, String courseName) {
		System.out.println(this + "init create a new course ");
		this.availableSpaces = availableSpaces;
		this.prerequisites = prerequisites;
		this.courseName = courseName;
	}

	@Override
	protected void start() {
		System.out.println(this + "begin create a new course ");
		CoursePrivateState myState = (CoursePrivateState) this.state;
		myState.setAvailableSpots(availableSpaces);
		myState.setPrequisites(prerequisites);
		complete(courseName);
		System.out.println(this + "finish create a new course ");
	}

}
