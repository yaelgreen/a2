package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddStudent extends Action<Boolean>{
	
	//Behavior: This action adds a new student to a specified department.
	//Actor: Must be initially submitted to the Department's actor.
	
	private String student;
	
	public AddStudent(String student) {
		setActionName("Add Student");
		this.student = student;
	}

	@Override
	protected void start() {
		DepartmentPrivateState myState = (DepartmentPrivateState) this.currentState;			
		List<Action<Boolean>> anAction = new ArrayList<>();
		Action<Boolean> createStudentActor = new EmptyAction();
		
		anAction.add(createStudentActor);
		//we add the student to the department just after his actor been created, 
		//we use then because we change the private-state DS and we want concurrency 
		then(anAction, ()-> 
		{
			myState.getStudentList().add(student);
			complete(true);});
		
		sendMessage(createStudentActor, student, new StudentPrivateState());
	}	
}
