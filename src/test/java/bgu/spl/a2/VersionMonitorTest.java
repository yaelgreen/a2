package bgu.spl.a2;

import org.junit.Assert;
import junit.framework.TestCase;

public class VersionMonitorTest extends TestCase {

	public VersionMonitorTest(String name) {
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

	/**
	 * Test method for {@link bgu.spl.a2.VersionMonitor#getVersion(java.lang.Object)}.
	 */
	public void testGetVersion() {
		VersionMonitor version = new VersionMonitor(new Thread());
		int firstVersion = version.getVersion();
		int secondtVersion = version.getVersion();
		assertEquals(firstVersion, secondtVersion);
	}

	/**
	 * Test method for {@link bgu.spl.a2.VersionMonitor#inc(java.lang.Object)}.
	 * tests if the version is protected from multiple increment requests
	 */
	public void testInc() {
		VersionMonitor version = new VersionMonitor(new Thread());
		int firstVersion = version.getVersion();
		VersionMonitor.inc();
		int newVersion = version.getVersion();
		assertTrue(newVersion == firstVersion + 1);
		Runnable testIncRun = () -> {
	       	for(int i = 0; i < 50; i++){
	      		int beforeVersion = version.getVersion();
	       		VersionMonitor.inc();
	       		assertTrue(version.getVersion() >= 1 + beforeVersion);
	       	}
		};
        Thread t1 = new Thread(testIncRun);
        Thread t2 = new Thread(testIncRun);        
        Thread t3 = new Thread(testIncRun);

        t1.start();
        t2.start();
        t3.start();
        
        try {
			t1.join();
			t2.join();
	        t3.join();
		} catch (InterruptedException e) {
			Assert.fail("should not happen here");
			e.printStackTrace();
		}
        //test the overall version
		assertTrue(version.getVersion() == 151 + firstVersion);
	}

	/**
	 * Test method for {@link bgu.spl.a2.VersionMonitor#await(java.lang.Object)}.
	 */
	public void testAwait() {
		Thread awaitTester = new Thread((()->
		{				
				try {
					synchronized (this) {
						wait();
		    		}					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}			
		}));	
		
		VersionMonitor version = new VersionMonitor(awaitTester);		
		awaitTester.start();
		int firstVersion = version.getVersion();		
		Thread t1 = new Thread(() -> {
        	version.await(firstVersion); //waiting for the version to change
			assertTrue(firstVersion == version.getVersion());
			//System.out.println("WAITING\n"+awaitTester.getState() + "/>");
			//System.out.println(!awaitTester.isAlive() + " " + awaitTester.getState().equals("WAITING"));
			//System.out.println(awaitTester.getState());
			//assertTrue(!awaitTester.isAlive() | awaitTester.getState().equals("WAITING"));
			VersionMonitor.inc();
			//System.out.println(awaitTester.getState());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			assertTrue(!awaitTester.isAlive());// | awaitTester.getState().equals("TIMED_WAITING"));				        	   		
        });
		t1.start();
		try {
			t1.join();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
		/*Test that call it for different version number*/
		Thread awaitTester2 = new Thread((()->
		{				
				try {
					synchronized (this) {
						wait();
		    		}					
				} catch (InterruptedException e) {				}			
		}));
		
		VersionMonitor version2 = new VersionMonitor(awaitTester2);
		awaitTester2.start();
		version2.await(version2.getVersion()+1);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(!awaitTester2.isAlive());
	}
	
	//test to understand threads better
//	public void testThreadablity() {
//		Thread testThreadablity = new Thread((()->
//		{	
//			if(Thread.interrupted())
//				System.out.println("yooo");
//			try
//			{
//				synchronized (this) {
//					wait();
//	    		}
//				System.out.println("unreachable code");			
//			}
//			catch(InterruptedException Ex){	System.out.println("notified!");	}//remove syso later if needed
//						
//			int x = 0;
//			for(int i=0; i < 100000; i++)
//				x++;
//			if(Thread.interrupted())
//				System.out.println("yooo2");
//			if(Thread.interrupted())//turn off the flag
//				System.out.println("yooo3");
//			System.out.println("Passed out");
//		}));
//		testThreadablity.start();
//		System.out.println("before sleep: " + testThreadablity.getState());
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {		}
//		System.out.println("1before interrupt: " + testThreadablity.getState());
//		System.out.println("1before interrupt: " + testThreadablity.isInterrupted());
//		//true just if the thread wasn't sleep/waiting while called
//		
//		testThreadablity.interrupt();
//		
//		System.out.println("2after interrupt: " + testThreadablity.getState());
//		System.out.println("2after interrupt: " + testThreadablity.isInterrupted());
//		testThreadablity.interrupt();
//		System.out.println("3after interrupt: " + testThreadablity.getState());
//		System.out.println("3after interrupt: " + testThreadablity.isInterrupted());
//		System.out.println("3.2after interrupt: " + testThreadablity.getState());
//		System.out.println("3.2after interrupt: " + testThreadablity.isInterrupted());
//		try {
//			Thread.sleep(200);
//		} catch (InterruptedException e) {		}
//		System.out.println("4after sleep: " + testThreadablity.getState());
//		System.out.println("4after sleep: " + testThreadablity.isInterrupted());
//		System.out.println("5after sleep: " + testThreadablity.getState());
//		System.out.println("5after sleep: " + testThreadablity.isInterrupted());
//	}	
}
