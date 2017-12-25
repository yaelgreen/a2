package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class TestOpenNewPlacesInCourse {
	ActorThreadPool testActorThreadPool = new ActorThreadPool(4);
	@Before
	public void prepareDepartmentAndCourses(){
		testActorThreadPool.start();		
		testActorThreadPool.submit(new AddStudent("111"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("222"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("333"), "chem", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(1, new LinkedList<>(), "courseA"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(1, new LinkedList<>(), "courseB"), "chem", new DepartmentPrivateState());
	}	
	
	@Test
	public void testOpenNewPlacesInCourse(){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}		
		submitAllStudents();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {	}
		
		testCourse("courseA", 1, 0);
		testCourse("courseB", 1, 0);
		
		testActorThreadPool.submit(new OpeningNewPlacesInACourse(1), "courseB", null);
		testActorThreadPool.submit(new OpeningNewPlacesInACourse(1), "courseA", null);
		submitAllStudents();
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {	}
		
		testCourse("courseA", 2, 0);
		testCourse("courseB", 2, 0);
		
		testActorThreadPool.submit(new OpeningNewPlacesInACourse(1), "courseB", null);
		testActorThreadPool.submit(new OpeningNewPlacesInACourse(1), "courseA", null);
		submitAllStudents();
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {	}
		testCourse("courseA", 3, 0);
		testCourse("courseB", 3, 0);
		
		//test if stayed 3 students
		testActorThreadPool.submit(new OpeningNewPlacesInACourse(1), "courseB", null);
		testActorThreadPool.submit(new OpeningNewPlacesInACourse(1), "courseA", null);
		submitAllStudents();
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {	}
		
		testCourse("courseA", 3, 1);
		testCourse("courseB", 3, 1);
		
	}

	private void submitAllStudents() {
		testActorThreadPool.submit(new ParticipatingInCourse("111", new String[]{"10"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("111", new String[]{"20"}), "courseB", null);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"30"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"40"}), "courseB", null);
		testActorThreadPool.submit(new ParticipatingInCourse("333", new String[]{"50"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("333", new String[]{"60"}), "courseB", null);		
	}

	private void testCourse(String string, int expected, int expectedSpots) {
		CoursePrivateState course = (CoursePrivateState) testActorThreadPool.getPrivateState(string);
		Assert.assertTrue(course.getAvailableSpots() == expectedSpots);
		Assert.assertTrue(course.getPrequisites().isEmpty());
		Assert.assertTrue(course.getRegistered() == expected);
		Assert.assertTrue(course.getRegStudents().size() == expected);	
	}
}
