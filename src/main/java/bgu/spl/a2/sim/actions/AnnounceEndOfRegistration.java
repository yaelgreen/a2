package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

public class AnnounceEndOfRegistration extends Action{
	
	//Behavior: From this moment, reject any further changes in registration. And, close courses with
	//number of students less than 5.
	//Actor: Must be initially submitted to the department's actor.

	@Override
	protected void start() {
		// Canceled	
	}

}
