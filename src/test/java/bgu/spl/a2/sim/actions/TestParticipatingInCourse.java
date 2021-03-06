package bgu.spl.a2.sim.actions;

import static org.junit.Assert.assertFalse;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.actions.AddStudent;
import bgu.spl.a2.sim.actions.OpenANewCourse;
import bgu.spl.a2.sim.actions.ParticipatingInCourse;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class TestParticipatingInCourse {
	private int threads = 8;
	ActorThreadPool testActorThreadPool = new ActorThreadPool(threads);
	@Before
	public void prepareDepartmentAndCourses(){
		testActorThreadPool.start();		
		testActorThreadPool.submit(new AddStudent("111"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("222"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new AddStudent("333"), "chem", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(2, new LinkedList<>(), "research methods 1970-80"), "bio", new DepartmentPrivateState());
		List<String> preCourses = new LinkedList<>();
		preCourses.add("research methods 1970-80");
		testActorThreadPool.submit(new OpenANewCourse(2, preCourses, "research methods 1980-90"), "bio", new DepartmentPrivateState());
		testActorThreadPool.submit(new OpenANewCourse(2, new LinkedList<>(), "lab1"), "chem", new DepartmentPrivateState());
		List<String> preCoursesChem = new LinkedList<>();
		preCoursesChem.add("research methods 1980-90");
		preCoursesChem.add("lab1");
		testActorThreadPool.submit(new OpenANewCourse(2, preCoursesChem, "lab2"), "chem", new DepartmentPrivateState());
	}	
	
	@After
	public void prepareActorPool(){
		testActorThreadPool.shutdown();
		testActorThreadPool = new ActorThreadPool(threads);
	}
	
	@Test
	public void testParticipate(){		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	assertFalse(true);	}
		CountDownLatch latch = new CountDownLatch(5);
		Action<Boolean> action = new ParticipatingInCourse("111", new String[]{"10"});
		action.getResult().subscribe(() -> {
			latch.countDown();
		});
		testActorThreadPool.submit(action, "lab2", null);
		action = new ParticipatingInCourse("333", new String[]{"10"});
		action.getResult().subscribe(() -> {
			latch.countDown();
		});
		testActorThreadPool.submit(action, "research methods 1980-90", null);
		action = new ParticipatingInCourse("222", new String[]{"10"});
		action.getResult().subscribe(() -> {
			latch.countDown();
		});
		testActorThreadPool.submit(action, "research methods 1970-80", null);
		action = new ParticipatingInCourse("222", new String[]{"10"});
		action.getResult().subscribe(() -> {
			latch.countDown();
		});
		testActorThreadPool.submit(action, "lab1", null);
		action = new ParticipatingInCourse("222", new String[]{"10"});
		action.getResult().subscribe(() -> {
			latch.countDown();
		});
		testActorThreadPool.submit(action, "lab2", null);	
		
		//main waiting for latch
		try {
			latch.await();
		} catch (InterruptedException e) {	assertFalse(true);	}
		
		testCourse("research methods 1970-80", 1, 1);
		testCourse("research methods 1980-90", 0, 2);
		testCourse("lab1", 1, 1);
		testCourse("lab2", 0, 2);		
		
		isLearning("111","research methods 1970-80",false);
		isLearning("111","research methods 1980-90",false);
		isLearning("111","lab1",false);
		isLearning("111","lab2",false);
		isLearning("222","research methods 1970-80",true);
		isLearning("222","research methods 1980-90",false);
		isLearning("222","lab1",true);
		isLearning("222","lab2",false);
		isLearning("333","research methods 1970-80",false);
		isLearning("333","research methods 1980-90",false);
		isLearning("333","lab1",false);
		isLearning("333","lab2",false);
		
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"10"}), "research methods 1980-90", null);
		try {
			Thread.sleep(40);
		} catch (InterruptedException e) {	}		
		testCourse("research methods 1970-80", 1, 1);
		testCourse("research methods 1980-90", 1, 1);
		testCourse("lab1", 1, 1);
		testCourse("lab2", 0, 2);	
		isLearning("222","research methods 1970-80",true);
		isLearning("222","research methods 1980-90",true);
		isLearning("222","lab1",true);
		isLearning("222","lab2",false);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"10"}), "lab2", null);
		
		try {
			Thread.sleep(40);
		} catch (InterruptedException e) { assertFalse(true);	}
		
		isLearning("222","research methods 1970-80",true);
		isLearning("222","research methods 1980-90",true);
		isLearning("222","lab1",true);
		isLearning("222","lab2",true);
		testCourse("research methods 1970-80", 1, 1);
		testCourse("research methods 1980-90", 1, 1);
		testCourse("lab1", 1, 1);
		testCourse("lab2", 1, 1);		
	}
	
	@Test
	public void testParticipateForSize(){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) { assertFalse(true);	}
		testActorThreadPool.submit(new ParticipatingInCourse("111", new String[]{"10"}), "lab1", null);
		testActorThreadPool.submit(new ParticipatingInCourse("222", new String[]{"10"}), "lab1", null);
		try {
			Thread.sleep(40);
		} catch (InterruptedException e) { assertFalse(true);	}
		testCourse("lab1", 2, 0);	
		isLearning("111","lab1",true);
		isLearning("222","lab1",true);
		isLearning("333","lab1",false);
		
		testActorThreadPool.submit(new ParticipatingInCourse("333", new String[]{"10"}), "lab1", null);
		try {
			Thread.sleep(40);
		} catch (InterruptedException e) { assertFalse(true);	}
		testCourse("lab1", 2, 0);	
		isLearning("111","lab1",true);
		isLearning("222","lab1",true);
		isLearning("333","lab1",false);
	}
	
	@Test
	public void testMultipleRegistration() {		
		try {
			Thread.sleep(110);
		} catch (InterruptedException e) { assertFalse(true);	}	
		testCourse("lab1", 0, 2);	
		isLearning("111","lab1",false);
		isLearning("222","lab1",false);
		isLearning("333","lab1",false);
		CountDownLatch latch = new CountDownLatch(30);	
		for(int i = 0; i<30; i++)
		{
			Action<Boolean> register = new ParticipatingInCourse("111", new String[]{"10"});
			testActorThreadPool.submit(register, "lab1", null);
			register.getResult().subscribe(()->latch.countDown());
		}
		try {
			latch.await();
		} catch (InterruptedException e) { assertFalse(true);	}
		testCourse("lab1", 1, 1);	
		isLearning("111","lab1",true);
		isLearning("222","lab1",false);
		isLearning("333","lab1",false);
	}
	
	@Test
	public void testDeadLock() throws Exception{
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) { assertFalse(true);	}
		for(int j =  0; j <20000; j++){
			//System.out.println(j);			
			CountDownLatch latch = new CountDownLatch(3);
			for(int i = 0; i<3; i++)
			{				
				Action<Boolean> register = new ParticipatingInCourse("111", new String[]{"10"});
				testActorThreadPool.submit(register, "lab1", null);
				register.getResult().subscribe(()->latch.countDown());
			}
			//System.out.println(Thread.currentThread() + " waiting, " + latch.getCount());
			try {
				if(latch.getCount() != 0)
					latch.await();
			} catch (InterruptedException e) { assertFalse(true);	}
		}
	}
	
	private void testCourse(String string, int expected, int expectedSpots) {
		CoursePrivateState course = (CoursePrivateState) testActorThreadPool.getPrivateState(string);
		Assert.assertTrue(course.getAvailableSpots() == expectedSpots);
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
