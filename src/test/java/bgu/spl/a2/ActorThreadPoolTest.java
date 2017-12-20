package bgu.spl.a2;

import java.util.concurrent.CountDownLatch;

//import org.junit.*;
import junit.framework.TestCase;

public class ActorThreadPoolTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	//Test basic flow. based on figure 2 from assignment description
	/**
	 * create actor thread pool and check completion of simple Action
	 */
	public void testFlow() {
		ActorThreadPool actorThreadPool = new ActorThreadPool(4);
		Action<?> action1 = new ActionA();
		Action<?> action2 = new ActionB();
		Action<?> action3 = new ActionA();
		actorThreadPool.submit(action1, "Actor1", new ActorState());
		actorThreadPool.submit(action2, "Actor1", new ActorState());
		actorThreadPool.submit(action3, "Actor2", new ActorState());
		CountDownLatch latch = new CountDownLatch(3);
		action1.getResult().subscribe(() -> {
			latch.countDown();
		});
		action2.getResult().subscribe(() -> {
			latch.countDown();
		});
		action3.getResult().subscribe(() -> {
			latch.countDown();
		});		
		
		actorThreadPool.start();
		//main waiting for latch
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		
		int answerVal = 1809300981;
		assertTrue(action1.getResult().isResolved());
		assertEquals(answerVal, action1.getResult().get());
		assertTrue(action2.getResult().isResolved());
		assertEquals("done 9", action2.getResult().get());
		assertTrue(action3.getResult().isResolved());
		assertEquals(answerVal, action3.getResult().get());
		
		String actual = "";
		for(String record : actorThreadPool.getPrivateState("Actor1").getLogger())
			actual += record + ",";
		assertEquals("ActionA,ActionB,", actual);
		
		actual = "";
		for(String record : actorThreadPool.getPrivateState("Actor2").getLogger())
			actual += record + ",";
		assertEquals("ActionA,", actual);
		actorThreadPool.shutdown();		
	}
	
	/**
	 * Test sending a message from one actor to another
	 */
	public void testSendMsg(){
		System.out.println("testSendMsg");
		ActorThreadPool actorThreadPool = new ActorThreadPool(3);
		Action<?> actionC = new ActionC();
		actorThreadPool.submit(actionC, "Actor1", new ActorState());
		CountDownLatch latch = new CountDownLatch(1);
		actionC.getResult().subscribe(() -> {
			latch.countDown();
		});
		
		actorThreadPool.start();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		
		assertTrue(actionC.getResult().isResolved());
		assertEquals("finished", actionC.getResult().get());
		
		try{
			Thread.sleep(30);
		}
		catch(InterruptedException e){		}
		
		String actual = "";
		for(String record : actorThreadPool.getPrivateState("Actor1").getLogger())
			actual += record + ",";
		assertEquals("ActionC,ActionA,", actual);
		
		actual = "";
		for(String record : actorThreadPool.getPrivateState("Actor2").getLogger())
			actual += record + ",";
		assertEquals("ActionA,", actual);
		actorThreadPool.shutdown();
	}
	
	/**
	 * Test big then expression that call actionA 24 time and return the sum of the results
	 */
	private void testComplexThenExp(int threads){
		System.out.println("testComplexThenExp " + threads);
		ActorThreadPool actorThreadPool = new ActorThreadPool(threads);
		Action<?> actionD = new ActionD();
		actorThreadPool.submit(actionD, "Actor1", new ActorState());
		CountDownLatch latch = new CountDownLatch(1);
		actionD.getResult().subscribe(() -> {
			latch.countDown();
		});
		
		actorThreadPool.start();
		//						 43423223544	
		Long answerVal = new Long(1809300981);
		answerVal = 24*answerVal;
								  	
		try{
			latch.await();
		}
		catch(InterruptedException e){		}
		
		assertTrue(actionD.getResult().isResolved());		
		assertEquals(answerVal.longValue(), actionD.getResult().get());
		
		String actual = "";
		for(String record : actorThreadPool.getPrivateState("Actor1").getLogger())
			actual += record;
		assertEquals("ActionD", actual);
		
		String expected = "";
		for(int i = 0; i < 8; i++)
			expected += "ActionA"; 
		
		actual = "";
		for(String record : actorThreadPool.getPrivateState("Actor2").getLogger())
			actual += record;
		assertEquals(expected, actual);
		
		actual = "";
		for(String record : actorThreadPool.getPrivateState("Actor3").getLogger())
			actual += record;
		assertEquals(expected, actual);
		
		actual = "";
		for(String record : actorThreadPool.getPrivateState("Actor4").getLogger())
			actual += record;
		assertEquals(expected, actual);
		
		actorThreadPool.shutdown();
	}
	
	/**
	 * Test 'testComplexThenExp' with variety of threads
	 */
	public void testComplexThenExp(){		
		testComplexThenExp(8);//takes 0.005 sec
		testComplexThenExp(1);//takes 0.065 sec
	}
}

