package bgu.spl.a2.sim.actions;
import org.junit.Assert;
import org.junit.Test;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class TestUnregister {
	ActorThreadPool testActorThreadPool = new ActorThreadPool(4);
	@Test
	public void testAddStudend(){		
		testActorThreadPool.start();		
		
		testActorThreadPool.submit(new AddStudent("111"), "bio", new DepartmentPrivateState());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}
		
		DepartmentPrivateState department = (DepartmentPrivateState) testActorThreadPool.getPrivateState("bio");
		Assert.assertTrue(department.getStudentList().contains("111"));
			
	}
}