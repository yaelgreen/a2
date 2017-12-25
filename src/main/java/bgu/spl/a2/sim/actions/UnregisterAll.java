package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class UnregisterAll extends Action<Boolean>{
	
	private String course;
	
	/**
	 * Constructor
	 * this action will unregister all the students from the course
	 * @param course - the course to unregister students from
	 */
	public UnregisterAll(String course) {
		this.course = course;
		setActionName("UnregisterAll");
	}

	@Override
	protected void start() {
		CoursePrivateState myState = (CoursePrivateState) this.state;
		List<Action<Boolean>> actions = new ArrayList<>();
		for (String student : myState.getRegStudents()) {
			Action<Boolean> unregisterAction = new Unregister(student, course);
			sendMessage(unregisterAction, course, new CoursePrivateState());
			actions.add(unregisterAction);
		}		
		then(actions, ()->{
			myState.setAvailableSpots(-1);
			myState.getRegStudents().clear();
			complete(true);
		});
	}

}
