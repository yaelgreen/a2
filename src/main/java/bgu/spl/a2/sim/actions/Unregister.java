package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Unregister extends Action<Boolean>{
	
	//Behavior: If the student is enrolled in the course, this action should unregister him (update the
	//list of students of course, remove the course from the grades sheet of the student and increases the
	//number of available spaces).
	//Actor: Must be initially submitted to the course's actor.
	
	private String student;


	public Unregister(String student) {
		this.student = student;
		setActionName("Unregister");
	}

	@Override
	protected void start() {
		CoursePrivateState courseState = (CoursePrivateState) currentState;		
		if(!courseState.getRegStudents().contains(student)) {
			complete(true);
			return;
		}
		courseState.getRegStudents().remove(student);
		courseState.setAvailableSpots(courseState.getAvailableSpots()+1);
		courseState.setRegistered(courseState.getRegistered()-1);
		Action<Boolean> rm = new RemoveCourse(currentActorId);
		sendMessage(rm, student, new StudentPrivateState());
		rm.getResult().subscribe(()->complete(rm.getResult().get()));
	}
}
