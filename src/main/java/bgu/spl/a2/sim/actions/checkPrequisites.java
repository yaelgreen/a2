package bgu.spl.a2.sim.actions;

import java.util.HashMap;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

//this action called from student actor to check if he stand in the Prequisites
public class checkPrequisites extends Action<Boolean> {

	List<String> prequisites;
	public checkPrequisites(List<String> prequisites) {
		this.prequisites = prequisites;
	}

	@Override
	protected void start() {
		StudentPrivateState studentState = (StudentPrivateState) state;
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
