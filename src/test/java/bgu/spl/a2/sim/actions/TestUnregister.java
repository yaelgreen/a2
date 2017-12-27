package bgu.spl.a2.sim.actions;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.actions.AddStudent;
import bgu.spl.a2.sim.actions.OpenANewCourse;
import bgu.spl.a2.sim.actions.ParticipatingInCourse;
import bgu.spl.a2.sim.actions.Unregister;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class TestUnregister {
	ActorThreadPool testActorThreadPool = new ActorThreadPool(4);
	@Before
	public void prepareDepartmentAndCourses(){
		testActorThreadPool.start();		
		testActorThreadPool.submit(new AddStudent("111"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("222"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("333"), "chem", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(4, new LinkedList<>(), "courseA"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(3, new LinkedList<>(), "courseB"), "chem", new DepartmentPrivateState());
	}	
	
	@Test
	public void testUnregister(){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}		
		testActorThreadPool.submit(new Unregister("111"), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("111", new String[]{"10"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("111", new String[]{"20"}), "courseB", null);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"30"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"40"}), "courseB", null);
		testActorThreadPool.submit(new ParticipatingInCourse("333", new String[]{"50"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("333", new String[]{"60"}), "courseB", null);
		testActorThreadPool.submit(new ParticipatingInCourse("111", new String[]{"70"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("111", new String[]{"80"}), "courseB", null);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"90"}), "courseA", null);
		testActorThreadPool.submit(new Unregister("222"), "courseA", null);
		testActorThreadPool.submit(new Unregister("222"), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"90"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"91"}), "courseB", null);
		testActorThreadPool.submit(new ParticipatingInCourse("333", new String[]{"92"}), "courseA", null);
		testActorThreadPool.submit(new Unregister("333"), "courseB", null);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}
		
		testCourse("courseA", 3, 1);
		testCourse("courseB", 2, 1);
		
		testActorThreadPool.submit(new Unregister("111"), "courseB", null);
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {	}
		
		testCourse("courseA", 3, 1);
		testCourse("courseB", 1, 2);
		isLearning("111","courseA",true);
		isLearning("111","courseB",false);		
		isLearning("222","courseA",true);
		isLearning("222","courseB",true);
		isLearning("333","courseA",true);
		isLearning("333","courseB",false);
		

		testActorThreadPool.submit(new ParticipatingInCourse("333", new String[]{"93"}), "courseB", null);
		testActorThreadPool.submit(new Unregister("222"), "courseA", null);
		testActorThreadPool.submit(new Unregister("222"), "courseA", null);
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {	}
		
		testCourse("courseA", 2, 2);
		testCourse("courseB", 2, 1);
		isLearning("111","courseA",true);
		isLearning("111","courseB",false);		
		isLearning("222","courseA",false);
		isLearning("222","courseB",true);
		isLearning("333","courseA",true);
		isLearning("333","courseB",true);
		
		testActorThreadPool.submit(new Unregister("222"), "courseB", null);
		testActorThreadPool.submit(new Unregister("333"), "courseA", null);
		testActorThreadPool.submit(new Unregister("333"), "courseB", null);
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {	}
		
		testCourse("courseA", 1, 3);
		testCourse("courseB", 0, 3);
		isLearning("111","courseA",true);
		isLearning("111","courseB",false);		
		isLearning("222","courseA",false);
		isLearning("222","courseB",false);
		isLearning("333","courseA",false);
		isLearning("333","courseB",false);
	}

	private void testCourse(String string, int expected, int expectedSpots) {
		CoursePrivateState course = (CoursePrivateState) testActorThreadPool.getPrivateState(string);
		Assert.assertTrue(course.getAvailableSpots() == expectedSpots);
		Assert.assertTrue(course.getPrequisites().isEmpty());
		Assert.assertTrue(course.getRegistered() == expected);
		Assert.assertTrue(course.getRegStudents().size() == expected);	
	}
	
	private void isLearning(String studentId, String Course, boolean expected) {
		StudentPrivateState student = (StudentPrivateState) testActorThreadPool.getPrivateState(studentId);
		Assert.assertTrue(student.getGrades().containsKey(Course) == expected);
		CoursePrivateState coursePS = (CoursePrivateState) testActorThreadPool.getPrivateState(Course);
		Assert.assertTrue(coursePS.getRegStudents().contains(studentId) == expected);
	}
}