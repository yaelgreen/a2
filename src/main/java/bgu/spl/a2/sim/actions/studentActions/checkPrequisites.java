package bgu.spl.a2.sim.actions.studentActions;

import java.util.HashMap;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class checkPrequisites extends Action<Boolean> {

	/**
	 * Check if student has all the requested courses in his grade (he does not have to get a grade above 56)
	 * complete true if the student have it.
	 */
	List<String> prequisites;
	public checkPrequisites(List<String> prequisites) {
		this.prequisites = prequisites;
	}

	@Override
	protected void start() {
		StudentPrivateState studentState = (StudentPrivateState) currentState;
		HashMap<String, Integer> studentGrade = studentState.getGrades();
		for (String course : prequisites) {
			if (!studentGrade.containsKey(course))//hasn't learnt the course
			{
				complete(false);
				return;
			}
			//said in the forum we should not check if the passed the prequisites
		}
		complete(true);
	}
}
