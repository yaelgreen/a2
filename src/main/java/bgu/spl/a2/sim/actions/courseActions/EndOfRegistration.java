package bgu.spl.a2.sim.actions.courseActions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.actions.departmentActions.CloseACourse;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

//initiate from department
public class EndOfRegistration extends Action<Boolean> {

	String departmentID;
	
	/**
	 * Called from course actor
	 * will close the course for forward registration
	 * we will use it in {@link bgu.spl.a2.sim.actions.departmentActions.AnnounceEndOfRegistration}
	 * @param departmentActorId the id of the department actor
	 */
	public EndOfRegistration(String departmentActorId) {
		departmentID = departmentActorId;
		setActionName("End Of Registration");
	}

	@Override
	protected void start() {
		CoursePrivateState myState = (CoursePrivateState) this.currentState;
		if(myState.getRegistered() < 5)
		{
			Action<Boolean> closeAction = new CloseACourse(currentActorId);//course's name
			sendMessage(closeAction, departmentID, null);//exist for sure because department initiate this action
			closeAction.getResult().subscribe(() -> complete(closeAction.getResult().get()));
		}
		else
		{
			myState.setAvailableSpots(-1);
			complete(true);
		}
	}

}
