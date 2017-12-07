package bgu.spl.a2;

import junit.framework.Assert;
import junit.framework.TestCase;

public class VersionMonitorTest extends TestCase {

	public VersionMonitorTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

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
		
        Thread t1 = new Thread(() -> {
        	for(int i = 0; i < 50; i++){
        		int beforeVersion = version.getVersion();
        		version.inc();
        		assertTrue(version.getVersion() == 1 + beforeVersion);
        	}        	
		});
        Thread t2 = new Thread(() -> {
        	for(int i = 0; i < 50; i++){
        		int beforeVersion = version.getVersion();
        		version.inc();
        		assertTrue(version.getVersion() == 1 + beforeVersion);
        	}        	
		});        
        Thread t3 = new Thread(() -> {
        	for(int i = 0; i < 50; i++){
        		int beforeVersion = version.getVersion();
        		version.inc();
        		assertTrue(version.getVersion() == 1 + beforeVersion);
        	}        	
		});

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
		VersionMonitor version = new VersionMonitor();
		int firstVersion = version.getVersion();
		Thread t1 = new Thread(() -> {
        	try {
        		if(firstVersion != version.getVersion())
					Assert.fail("changed the version during await function");	
				version.await(firstVersion);		//waiting for the version to change
				if(firstVersion+1 != version.getVersion())
					Assert.fail("waited more then needed to call thread");
				// keep the thread alive
				int i = 1;
				while(i > 0)
					i = i + 1;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}        	   		
        });
		t1.start();
		Assert.assertFalse(t1.isAlive());//t1.getState();
		version.inc();
		Assert.assertTrue(t1.isAlive());
		version.inc();
		Assert.assertTrue(t1.isAlive());
		// we end chaking t1
		try {
			t1.wait();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
		//should get back immediately - because the versions are different
		Thread t2 = new Thread(() -> {
        	try {
        		if(firstVersion+2 != version.getVersion())
					Assert.fail("changed the version during await function");	
				version.await(version.getVersion()+1);		//waiting for the version to change
				if(firstVersion+2 != version.getVersion())
					Assert.fail("waited more then needed to call thread");
				// keep the thread alive
				int i = 1;
				while(i > 0)
					i = i + 1;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}        	   		
        });
		t2.start();
		Assert.assertTrue(t1.isAlive());//t2.getState();
		version.inc();
		Assert.assertTrue(t1.isAlive());
		version.inc();
		Assert.assertTrue(t1.isAlive());
	}

}
