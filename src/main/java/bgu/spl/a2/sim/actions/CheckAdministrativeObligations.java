package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Warehouse;

public class CheckAdministrativeObligations extends Action{
	
	//Behavior: The department's secretary have to allocate one of the computers available in the ware-
	//house, and check for each student if he meets some administrative obligations. 
	//The computer generates a signature and save it in the private state of the students.
	//Actor: Must be initially submitted to the department's actor.

	@Override
	protected void start() {
		Warehouse w = this._currpool.getWarehouse();
		
	}

}
