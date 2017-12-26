package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddStudent extends Action<Boolean>{
	
	/**
	 * Behavior: This action adds a new student to a specified department.
	 * Actor: Must be initially submitted to the Department's actor.
	 */	
	private String student;
	
	public AddStudent(String student) {
		setActionName("Add Student");
		this.student = student;
	}

	@Override
	protected void start() {
		DepartmentPrivateState myState = (DepartmentPrivateState) this.currentState;
		if (myState.getStudentList().contains(student)) {
			complete(true);
			return;
		}
		myState.getStudentList().add(student);
		Action<Boolean> createStudentActor = new EmptyAction();		
		createStudentActor.getResult().subscribe(()-> complete(true));		
		sendMessage(createStudentActor, student, new StudentPrivateState());
	}	
}
