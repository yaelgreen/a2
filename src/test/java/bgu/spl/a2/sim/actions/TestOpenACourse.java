package bgu.spl.a2.sim.actions;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.actions.OpenANewCourse;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class TestOpenACourse {
	ActorThreadPool testActorThreadPool = new ActorThreadPool(4);
	@Test
	public void testOpenCourse(){		
		testActorThreadPool.start();		
		
		testActorThreadPool.submit(new OpenANewCourse(10, new LinkedList<>(), "research methods 1970-80"), "bio", new DepartmentPrivateState());
		List<String> preCourses = new LinkedList<>();
		preCourses.add("research methods 1970-80");
		testActorThreadPool.submit(new OpenANewCourse(11, preCourses, "research methods 1980-90"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(12, new LinkedList<>(), "lab1"), "chem", new DepartmentPrivateState());
		List<String> preCoursesChem = new LinkedList<>();
		preCoursesChem.add("research methods 1970-80");
		preCoursesChem.add("lab1");
		testActorThreadPool.submit(new OpenANewCourse(13, preCoursesChem, "lab2"), "chem", new DepartmentPrivateState());
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}
		
		DepartmentPrivateState department = (DepartmentPrivateState) testActorThreadPool.getPrivateState("bio");
		Assert.assertTrue(department.getCourseList().contains("research methods 1970-80"));
		Assert.assertTrue(department.getCourseList().contains("research methods 1980-90"));
		Assert.assertTrue(!department.getCourseList().contains("lab1"));
		Assert.assertTrue(!department.getCourseList().contains("lab2"));		

		department = (DepartmentPrivateState) testActorThreadPool.getPrivateState("chem");
		Assert.assertTrue(!department.getCourseList().contains("research methods 1970-80"));
		Assert.assertTrue(!department.getCourseList().contains("research methods 1980-90"));
		Assert.assertTrue(department.getCourseList().contains("lab1"));
		Assert.assertTrue(department.getCourseList().contains("lab2"));
		
		CoursePrivateState course = (CoursePrivateState) testActorThreadPool.getPrivateState("research methods 1970-80");
		Assert.assertTrue(course.getAvailableSpots() == 10);
		Assert.assertTrue(course.getPrequisites().isEmpty());
		Assert.assertTrue(course.getRegistered() == 0);
		Assert.assertTrue(course.getRegStudents().isEmpty());		
		
		course = (CoursePrivateState) testActorThreadPool.getPrivateState("research methods 1980-90");
		Assert.assertTrue(course.getAvailableSpots() == 11);
		Assert.assertTrue(course.getPrequisites().remove("research methods 1970-80"));
		Assert.assertTrue(course.getPrequisites().isEmpty());
		Assert.assertTrue(course.getRegistered() == 0);
		Assert.assertTrue(course.getRegStudents().isEmpty());	
		
		course = (CoursePrivateState) testActorThreadPool.getPrivateState("lab1");
		Assert.assertTrue(course.getAvailableSpots() == 12);
		Assert.assertTrue(course.getPrequisites().isEmpty());
		Assert.assertTrue(course.getRegistered() == 0);
		Assert.assertTrue(course.getRegStudents().isEmpty());
		
		course = (CoursePrivateState) testActorThreadPool.getPrivateState("lab2");
		Assert.assertTrue(course.getAvailableSpots() == 13);
		Assert.assertTrue(course.getPrequisites().remove("research methods 1970-80"));
		Assert.assertTrue(course.getPrequisites().remove("lab1"));
		Assert.assertTrue(course.getPrequisites().isEmpty());
		Assert.assertTrue(course.getRegistered() == 0);
		Assert.assertTrue(course.getRegStudents().isEmpty());	
	}
}
