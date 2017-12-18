package bgu.spl.a2;

import java.util.concurrent.CountDownLatch;

//import org.junit.*;
import junit.framework.TestCase;

public class ActorThreadPoolTest extends TestCase {

	public ActorThreadPoolTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	//Test basic flow. based on figure 2 from assignment description 
	public void testFlow() {
		ActorThreadPool actorThreadPool = new ActorThreadPool(2);
		Action<String> action1 = new ActionA<String>("Action1", "Actor1");
		Action<String> action2 = new ActionA<String>("Action2", "Actor1");
		actorThreadPool.submit(action1, "Actor1", new ActorState());
		actorThreadPool.submit(action2, "Actor1", new ActorState());
		actorThreadPool.start();
		CountDownLatch latch = new CountDownLatch(2);
		action1.getResult().subscribe(() -> {
			latch.countDown();
		});
		action2.getResult().subscribe(() -> {
			latch.countDown();
		});
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		try {
			actorThreadPool.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}

}

