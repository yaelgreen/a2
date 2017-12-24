package bgu.spl.a2.sim.actions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class OpenANewCourse extends Action<Boolean>{

	//Behavior: This action opens a new course in a specied department. 
	//The course has an initially available spaces and a list of prerequisites.
	//Actor: Must be initially submitted to the Department's actor.
	
	private final int availableSpaces;
	private final List<String> prerequisites;
	private final String courseName;
	private String actionName = "Open Course";  
	
	public OpenANewCourse(int spaces, List<String> prerequisites, String courseName) {
		this.availableSpaces = spaces;
		this.prerequisites = prerequisites;
		this.courseName = courseName;
	}
	
	@Override
    protected String getName(){
        return actionName; 
    }
	
	@Override
	protected void start() {
		DepartmentPrivateState myState = (DepartmentPrivateState) state;
		myState.getCourseList().add(courseName);
		Action<Boolean> newCourse = new CreateNewCourse(availableSpaces, prerequisites);
		sendMessage(newCourse , courseName, new CoursePrivateState());
		newCourse.getResult().subscribe(()->complete(newCourse.getResult().get()));
	}

}
