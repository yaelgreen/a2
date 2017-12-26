package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class AnnounceEndOfRegistration extends Action<Boolean>{
	
	/**
	 * Behavior: From this moment, reject any further changes in registration.
	 * And, close courses with number of students less than 5.
	 * Actor: Must be initially submitted to the department's actor.	
	 */
	public AnnounceEndOfRegistration(){
		setActionName("End Registeration");
	}

	@Override
	protected void start() {
		// Canceled
		List<Action<Boolean>> actions = new ArrayList<>();
		for (Map.Entry<String, PrivateState> actor : this.currentpool.getActors().entrySet())
		{
			if (actor.getValue() instanceof DepartmentPrivateState) {
				DepartmentPrivateState state = (DepartmentPrivateState) actor.getValue();
				for(String course : state.getCourseList())
				{
					Action<Boolean> end = new EndOfRegistration(actor.getKey());//department's name
					sendMessage(end, course, new CoursePrivateState());
					actions.add(end);
				}
			}
		}
		
		then(actions, () -> complete(true));
	}
}
