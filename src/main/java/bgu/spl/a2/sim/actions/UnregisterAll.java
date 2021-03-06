package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class UnregisterAll extends Action<Boolean>{
	
	/**
	 * Constructor
	 * this action will unregister all the students from the course in purpose to close it
	 * we use this action in {@link bgu.spl.a2.sim.actions.CloseACourse}
	 */
	public UnregisterAll() {
		setActionName("UnregisterAll");
	}

	@Override
	protected void start() {
		CoursePrivateState myState = (CoursePrivateState) this.currentState;
		List<Action<Boolean>> actions = new ArrayList<>();
		for (String student : myState.getRegStudents()) {
			Action<Boolean> unregisterAction = new Unregister(student);
			sendMessage(unregisterAction, currentActorId, new CoursePrivateState());
			actions.add(unregisterAction);
		}		
		myState.setAvailableSpots(-1-myState.getRegistered());//we want to block forward register
		then(actions, ()->{
			//myState.setAvailableSpots(-1);
			//myState.setRegistered(0);
			myState.getRegStudents().clear();
			complete(true);
		});
	}

}
