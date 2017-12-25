package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class TestRegWithPreferences {
	ActorThreadPool testActorThreadPool = new ActorThreadPool(4);
	@Before
	public void prepareDepartmentAndCourses(){
		testActorThreadPool.start();
		testActorThreadPool.submit(new AddStudent("111"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("222"), "chem", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(1, new LinkedList<>(), "courseA"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(1, new LinkedList<>(), "courseB"), "chem", new DepartmentPrivateState());
		LinkedList<String> preCourses = new LinkedList<>();
		testActorThreadPool.submit(new OpenANewCourse(2, preCourses, "courseC"), "chem", new DepartmentPrivateState());		
	}	
	
	@Test
	public void testCloseCourse(){		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) { Assert.assertTrue(false);	}
		
		String[] toReg = new String[]{"courseA","courseC","courseB"};
		String[] grades = new String[]{"10","20","30"};
		testCourse("courseA", 0, 1);
		testCourse("courseB", 0, 1);
		testCourse("courseC", 0, 2);
		isLearning("111", "courseA", false);
		isLearning("222", "courseA", false);
		isLearning("111", "courseB", false);
		isLearning("222", "courseB", false);
		isLearning("111", "courseC", false);
		isLearning("222", "courseC", false);		
		//111 - > courseA
		testActorThreadPool.submit(new RegisterWithPreferences(toReg, grades), "111", null);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) { Assert.assertTrue(false);	}
		testCourse("courseA", 1, 0);
		testCourse("courseB", 0, 1);
		testCourse("courseC", 0, 2);
		isLearning("111", "courseA", true);
		isLearning("222", "courseA", false);
		isLearning("111", "courseB", false);
		isLearning("222", "courseB", false);
		isLearning("111", "courseC", false);
		isLearning("222", "courseC", false);
		//111 - > courseB
		testActorThreadPool.submit(new RegisterWithPreferences(toReg, grades), "111", null);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) { Assert.assertTrue(false);	}
		testCourse("courseA", 1, 0);
		testCourse("courseB", 1, 0);
		testCourse("courseC", 0, 2);
		isLearning("111", "courseA", true);
		isLearning("222", "courseA", false);
		isLearning("111", "courseB", true);
		isLearning("222", "courseB", false);
		isLearning("111", "courseC", false);
		isLearning("222", "courseC", false);
		//111 - > courseC
		testActorThreadPool.submit(new RegisterWithPreferences(toReg, grades), "222", null);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) { Assert.assertTrue(false);	}
		testCourse("courseA", 1, 0);
		testCourse("courseB", 1, 0);
		testCourse("courseC", 1, 1);
		isLearning("111", "courseA", true);
		isLearning("222", "courseA", false);
		isLearning("111", "courseB", true);
		isLearning("222", "courseB", false);
		isLearning("111", "courseC", false);
		isLearning("222", "courseC", true);
		//111 - > courseC
		testActorThreadPool.submit(new RegisterWithPreferences(toReg, grades), "111", null);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) { Assert.assertTrue(false);	}
		testCourse("courseA", 1, 0);
		testCourse("courseB", 1, 0);
		testCourse("courseC", 2, 0);
		isLearning("111", "courseA", true);
		isLearning("222", "courseA", false);
		isLearning("111", "courseB", true);
		isLearning("222", "courseB", false);
		isLearning("111", "courseC", true);
		isLearning("222", "courseC", true);
	}
	
	private void testCourse(String string, int expected, int expectedSpots) {
		CoursePrivateState course = (CoursePrivateState) testActorThreadPool.getPrivateState(string);
		//System.out.println(course.getRegistered() + " " + course.getAvailableSpots() + " " + course.getRegStudents());
		//System.out.println(expected + " " + expectedSpots + "\n");
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
