package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

public class EmptyAction extends Action<Boolean> {
	
	/**
	 * This action do nothing.
	 * we will use it to create student actors even if they didn't ask any request during the phases 
	 */
	public EmptyAction() {
		setActionName("Empty Action");
	}
	
	@Override
	protected void start() {
		complete(true);
	}

}
