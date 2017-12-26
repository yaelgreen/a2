package bgu.spl.a2.sim.actions;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.a2.Action;
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
		
		testCourse("courseA", 1, 0);
		testCourse("courseB", 1, 0);
		
		CountDownLatch remainedActionCounter = new CountDownLatch(2);
		Action<Boolean> actionA = new OpeningNewPlacesInACourse(1);
		Action<Boolean> actionB = new OpeningNewPlacesInACourse(1);
    	
		actionA.getResult().subscribe(()->remainedActionCounter.countDown());
		actionB.getResult().subscribe(()->remainedActionCounter.countDown());
		testActorThreadPool.submit(actionA, "courseB", null);
		testActorThreadPool.submit(actionB, "courseA", null);
		try {
			remainedActionCounter.await();
		} catch (InterruptedException e1) {	}
		
		submitAllStudents();
		
		testCourse("courseA", 2, 0);
		testCourse("courseB", 2, 0);
		
		CountDownLatch remainedActionCounter2 = new CountDownLatch(2);
		actionA = new OpeningNewPlacesInACourse(1);
		actionB = new OpeningNewPlacesInACourse(1);
    	
		actionA.getResult().subscribe(()->remainedActionCounter2.countDown());
		actionB.getResult().subscribe(()->remainedActionCounter2.countDown());
		testActorThreadPool.submit(actionA, "courseB", null);
		testActorThreadPool.submit(actionB, "courseA", null);
		try {
			remainedActionCounter2.await();
		} catch (InterruptedException e1) {	}
		
		submitAllStudents();		
		
		testCourse("courseA", 3, 0);
		testCourse("courseB", 3, 0);
		
		//test if stays 3 students
		CountDownLatch remainedActionCounter3 = new CountDownLatch(2);
		actionA = new OpeningNewPlacesInACourse(1);
		actionB = new OpeningNewPlacesInACourse(1);
    	
		actionA.getResult().subscribe(()->remainedActionCounter3.countDown());
		actionB.getResult().subscribe(()->remainedActionCounter3.countDown());
		testActorThreadPool.submit(actionA, "courseB", null);
		testActorThreadPool.submit(actionB, "courseA", null);
		try {
			remainedActionCounter2.await();
		} catch (InterruptedException e1) {	}
		
		submitAllStudents();
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {	}
		
		testCourse("courseA", 3, 1);
		testCourse("courseB", 3, 1);
		
	}

	private void submitAllStudents() {
		CountDownLatch remainedActionCounter = new CountDownLatch(6);
		Action<Boolean> actionA = new ParticipatingInCourse("111", new String[]{"10"});
		Action<Boolean> actionB = new ParticipatingInCourse("111", new String[]{"20"});
		Action<Boolean> actionC = new ParticipatingInCourse("222", new String[]{"30"});
		Action<Boolean> actionD = new ParticipatingInCourse("222", new String[]{"40"});
		Action<Boolean> actionE = new ParticipatingInCourse("333", new String[]{"50"});
		Action<Boolean> actionF = new ParticipatingInCourse("333", new String[]{"60"});
		actionA.getResult().subscribe(()->remainedActionCounter.countDown());
		actionC.getResult().subscribe(()->remainedActionCounter.countDown());
		actionD.getResult().subscribe(()->remainedActionCounter.countDown());
		actionE.getResult().subscribe(()->remainedActionCounter.countDown());
		actionF.getResult().subscribe(()->remainedActionCounter.countDown());
		actionB.getResult().subscribe(()->remainedActionCounter.countDown());
		testActorThreadPool.submit(actionA, "courseA", null);
		testActorThreadPool.submit(actionB, "courseB", null);
		testActorThreadPool.submit(actionC, "courseA", null);
		testActorThreadPool.submit(actionD, "courseB", null);
		testActorThreadPool.submit(actionE, "courseA", null);
		testActorThreadPool.submit(actionF, "courseB", null);
		try {
			remainedActionCounter.await();
		} catch (InterruptedException e1) {	}
	}

	private void testCourse(String string, int expected, int expectedSpots) {
		CoursePrivateState course = (CoursePrivateState) testActorThreadPool.getPrivateState(string);
		Assert.assertTrue(course.getAvailableSpots() == expectedSpots);
		Assert.assertTrue(course.getPrequisites().isEmpty());
		Assert.assertTrue(course.getRegistered() == expected);
		Assert.assertTrue(course.getRegStudents().size() == expected);	
	}
}
