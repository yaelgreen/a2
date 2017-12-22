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
		VersionMonitor version = new VersionMonitor();
		int firstVersion = version.getVersion();
		int secondtVersion = version.getVersion();
		assertEquals(firstVersion, secondtVersion);
	}

	/**
	 * Test method for {@link bgu.spl.a2.VersionMonitor#inc(java.lang.Object)}.
	 * tests if the version is protected from multiple increment requests
	 */
	public void testInc() {
		VersionMonitor version = new VersionMonitor();
		int firstVersion = version.getVersion();
		version.inc();
		int newVersion = version.getVersion();
		assertTrue(newVersion == firstVersion + 1);
		Runnable testIncRun = () -> {
	       	for(int i = 0; i < 50; i++){
	      		int beforeVersion = version.getVersion();
	      		version.inc();
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

	//we need a global bool to check from the thread if the await cause the main to wait
	boolean isWaited = false;
	/**
	 * Test method for {@link bgu.spl.a2.VersionMonitor#await(java.lang.Object)}.
	 */
	public void testAwait() {
		VersionMonitor version = new VersionMonitor();		
		Thread awaitTester = new Thread((()->
		{				
				try {
					Thread.sleep(100);		    							
				} catch (InterruptedException e) {	}
				isWaited = true;
				version.inc();
		}));
		
		awaitTester.start();
		//should not wait
		version.await(version.getVersion() + 1);
		assertFalse(isWaited);
		//should wait
		version.await(version.getVersion());
		assertTrue(isWaited);		
		
		isWaited = false;
		/*Test that call it for different version number*/
		Thread awaitTester2 = new Thread((()->
		{				
			version.await(version.getVersion()-1);
			isWaited = true;
			version.await(version.getVersion());
			isWaited = true;
		}));
		
		awaitTester2.start();		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}
		//should not wait
		assertTrue(isWaited);
		//should wait
		isWaited = false;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}
		version.inc();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {	}
		assertTrue(isWaited);
	}
}
