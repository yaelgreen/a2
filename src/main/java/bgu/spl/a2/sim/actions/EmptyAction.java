package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

public class EmptyAction extends Action<Boolean> {

	public EmptyAction() {
		setActionName("Empty Action");
	}
	
	@Override
	protected void start() {
		complete(true);
	}

}
