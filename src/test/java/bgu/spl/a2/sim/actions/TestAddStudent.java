package bgu.spl.a2.sim.actions;

import org.junit.Assert;
import org.junit.Test;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class TestAddStudent {
	ActorThreadPool testActorThreadPool = new ActorThreadPool(4);
	
	//test that student added to the right PS
	@Test
	public void testAddStudend(){		
		testActorThreadPool.start();		
		
		testActorThreadPool.submit(new AddStudent("111"), "bio", new DepartmentPrivateState());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}
		
		DepartmentPrivateState department = (DepartmentPrivateState) testActorThreadPool.getPrivateState("bio");
		Assert.assertTrue(department.getStudentList().contains("111"));
		
		testActorThreadPool.submit(new AddStudent("222"), "bio", new DepartmentPrivateState());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}
		
		department = (DepartmentPrivateState) testActorThreadPool.getPrivateState("bio");
		Assert.assertTrue(department.getStudentList().contains("111"));
		Assert.assertTrue(department.getStudentList().contains("222"));

		testActorThreadPool.submit(new AddStudent("333"), "chem", new DepartmentPrivateState());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}
		
		department = (DepartmentPrivateState) testActorThreadPool.getPrivateState("bio");
		Assert.assertTrue(department.getStudentList().contains("111"));
		Assert.assertTrue(department.getStudentList().contains("222"));
		Assert.assertTrue(!department.getStudentList().contains("333"));
		
		department = (DepartmentPrivateState) testActorThreadPool.getPrivateState("chem");
		Assert.assertTrue(!department.getStudentList().contains("111"));
		Assert.assertTrue(!department.getStudentList().contains("222"));
		Assert.assertTrue(department.getStudentList().contains("333"));		
	}
}
