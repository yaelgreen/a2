package bgu.spl.a2;

import java.util.concurrent.TimeUnit;

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
				version.await(firstVersion); //waiting for the version to change
				assertTrue(firstVersion < version.getVersion());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}        	   		
        });
		t1.start();
		// t1 should be alive since it cannot finish running before the inc is called
		Assert.assertTrue(t1.isAlive());
		version.inc();
		try {
			t1.join();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
		//should get back immediately - because the versions are different
		Thread t2 = new Thread(() -> {
        	try {
				version.await(version.getVersion()+1);		//waiting for the version to change
				if(firstVersion+2 != version.getVersion())
					Assert.fail("waited more then needed to call thread");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}        	   		
        });
		t2.start();
	}

}
