package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class CloseACourse extends Action<Boolean>{
	
	//Behavior: This action should close a course. Should unregister all the registered students in the
	//course and remove the course from the department courses' list and from the grade sheets of the
	//students. The number of available spaces of the closed course should be updated to -1. DO NOT
	//remove its actor. After closing the course, all the request for registration should be denied.
	//Actor: Must be initially submitted to the department's actor.
	
	private String course;

	public CloseACourse(String course) {
		this.course = course;	
		setActionName("Close Course");
	}

	@Override
	protected void start() {
		DepartmentPrivateState myState = (DepartmentPrivateState) this.state;
		if(!myState.getCourseList().contains(course))
		{
			complete(true);
			return;
		}
		myState.getCourseList().remove(course);
		//call unregister for all students in course
		Action<Boolean> unReg = new UnregisterAll(course);
		sendMessage(new UnregisterAll(course), course, new CoursePrivateState());
		unReg.getResult().subscribe(() -> complete(unReg.getResult().get()));		
	}

}
