package bgu.spl.a2.sim.actions;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class TestCloseCourse {
	ActorThreadPool testActorThreadPool = new ActorThreadPool(4);
	@Before
	public void prepareDepartmentAndCourses(){
		testActorThreadPool.start();		
		testActorThreadPool.submit(new AddStudent("111"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("222"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("333"), "chem", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(10, new LinkedList<>(), "research methods 1970-80"), "bio", new DepartmentPrivateState());
		List<String> preCourses = new LinkedList<>();
		preCourses.add("research methods 1970-80");
		testActorThreadPool.submit(new OpenANewCourse(10, preCourses, "research methods 1980-90"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(10, new LinkedList<>(), "lab1"), "chem", new DepartmentPrivateState());
		List<String> preCoursesChem = new LinkedList<>();
		preCoursesChem.add("research methods 1970-80");
		preCoursesChem.add("lab1");
		testActorThreadPool.submit(new OpenANewCourse(10, preCoursesChem, "lab2"), "chem", new DepartmentPrivateState());
		
		//Participate
	}	
	
	@Test
	public void testCloseCourse(){		
		
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}
		
		DepartmentPrivateState department = (DepartmentPrivateState) testActorThreadPool.getPrivateState("bio");
		Assert.assertTrue(department.getStudentList().contains("111"));
			
	}
}