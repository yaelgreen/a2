package bgu.spl.a2.sim.actions;

import java.util.Arrays;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class GatherGradesAndCheck extends Action<Boolean> {
	
	Computer computer;
	String[] coursesOfStudents;
	public GatherGradesAndCheck(Computer computer, String[] conditionsOnStudents) {
		this.computer = computer;
		this.coursesOfStudents = conditionsOnStudents;
		setActionName("gather grades and check");
	}

	/**
	 * this action help the action CheckAdministrativeObligations. it determine per student if he passed the conditions or not
	 * this action called from student actor
	 */
	@Override
	protected void start() {
		StudentPrivateState myState = (StudentPrivateState) currentState;
		myState.setSignature(computer.checkAndSign(Arrays.asList(coursesOfStudents), myState.getGrades()));
		complete(true);
	}
}
