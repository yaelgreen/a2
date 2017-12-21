package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class AddStudent extends Action{
	
	//Behavior: This action adds a new student to a specified department.
	//Actor: Must be initially submitted to the Department's actor.
	
	private String department;
	private String student;
	
	public AddStudent(String student, String department) {
		System.out.println(this + "Int add a student "+student+" to department "+department);
		this.student = student;
		this.department = department;
	}

	@Override
	protected void start() {	
		System.out.println(this + "begin add a student "+student+" to department "+department);
		DepartmentPrivateState myState = (DepartmentPrivateState) this.state;
		myState.getStudentList().add(student);
		complete(student);
		System.out.println(this + "Added a student "+student+" to department "+department);
	}
	
}
