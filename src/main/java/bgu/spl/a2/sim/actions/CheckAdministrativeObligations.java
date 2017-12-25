package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class CheckAdministrativeObligations extends Action<Boolean>{
	
	//Behavior: The department's secretary have to allocate one of the computers available in the ware-
	//house, and check for each student if he meets some administrative obligations. 
	//The computer generates a signature and save it in the private state of the students.
	//Actor: Must be initially submitted to the department's actor.
	
	String neededComputer;
	String[] studentsToSign;
	String[] conditionsOnStudents;
	Promise<Computer> myComputerPromise;
	
	public CheckAdministrativeObligations(String computer, String[] students, String[] conditions) {
		neededComputer = computer;
		studentsToSign = students;
		conditionsOnStudents = conditions;
		setActionName("Administrative Check");
	}

	@Override
	protected void start() {
		Warehouse myWarehouse = currentpool.getWarehouse();	
		List<Action<Boolean>> actions = new ArrayList<>();
		myComputerPromise = myWarehouse.tryAllocate(neededComputer);
		//when we get the computer each student will do as followed
		myComputerPromise.subscribe(()->{
			for (String student : studentsToSign) {
				Action<Boolean> getCoursesAction = new gatherGradesAndCheck(myComputerPromise.get(), conditionsOnStudents);
				sendMessage(getCoursesAction, student, new StudentPrivateState());
				actions.add(getCoursesAction);
			}
			then(actions, ()->complete(true));
		});
	}

}
