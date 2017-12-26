package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.actions.courseActions.ParticipatingInCourse;
import bgu.spl.a2.sim.actions.departmentActions.AddStudent;
import bgu.spl.a2.sim.actions.departmentActions.CloseACourse;
import bgu.spl.a2.sim.actions.departmentActions.OpenANewCourse;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class TestCloseCourse {
	ActorThreadPool testActorThreadPool = new ActorThreadPool(4);
	@Before
	public void prepareDepartmentAndCourses(){
		testActorThreadPool.start();
		testActorThreadPool.submit(new AddStudent("111"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("222"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("333"), "chem", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(3, new LinkedList<>(), "courseA"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(1, new LinkedList<>(), "courseB"), "chem", new DepartmentPrivateState());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) { Assert.assertTrue(false);	}
		testActorThreadPool.submit(new ParticipatingInCourse("111", new String[]{"10"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("111", new String[]{"20"}), "courseB", null);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"30"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"40"}), "courseB", null);
		testActorThreadPool.submit(new ParticipatingInCourse("333", new String[]{"50"}), "courseA", null);
		testActorThreadPool.submit(new ParticipatingInCourse("333", new String[]{"60"}), "courseB", null);
	}	
	
	@Test
	public void testCloseCourse(){		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) { Assert.assertTrue(false);	}
		
		testCourse("courseA", 3, 0);
		testCourse("courseB", 1, 0);
		isLearning("111", "courseA", true);
		isLearning("222", "courseA", true);
		isLearning("333", "courseA", true);
		//nothing should happend
		testActorThreadPool.submit(new CloseACourse("courseB"), "bio", null);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) { Assert.assertTrue(false);	}
		testCourse("courseA", 3, 0);
		testCourse("courseB", 1, 0);
		isLearning("111", "courseA", true);
		isLearning("222", "courseA", true);
		isLearning("333", "courseA", true);
		
		testActorThreadPool.submit(new CloseACourse("courseB"), "chem", null);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) { Assert.assertTrue(false);	}
		testCourse("courseA", 3, 0);
		testCourse("courseB", 0, -1);
		isLearning("111", "courseA", true);
		isLearning("222", "courseA", true);
		isLearning("333", "courseA", true);
		
		testActorThreadPool.submit(new CloseACourse("courseA"), "bio", null);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) { Assert.assertTrue(false);	}
		testCourse("courseA", 0, -1);
		testCourse("courseB", 0, -1);
		isLearning("111", "courseA", false);
		isLearning("222", "courseA", false);
		isLearning("333", "courseA", false);
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