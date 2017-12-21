package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class OpenANewCourse extends Action{

	//Behavior: This action opens a new course in a specied department. 
	//The course has an initially available spaces and a list of prerequisites.
	//Actor: Must be initially submitted to the Department's actor.
	
	private int availableSpaces;
	private List<String> prerequisites;
	private String courseName;
	
	public OpenANewCourse(int spaces, List<String> arrayList, String courseName) {
		this.availableSpaces = spaces;
		this.prerequisites = arrayList;
		this.courseName = courseName;
	}
	
	@Override
	protected void start() {
		DepartmentPrivateState myState = (DepartmentPrivateState) this.state;
		myState.getCourseList().add(courseName);
		sendMessage(new CreateNewCourse(availableSpaces, prerequisites), courseName, new CoursePrivateState());
	}

}
