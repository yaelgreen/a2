package bgu.spl.a2;

import org.junit.*;

import junit.framework.AssertionFailedError;
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
		assertTrue(newVersion > firstVersion);
		
        Thread t1 = new Thread(() -> {
        	for(int i = 0; i < 50; i++){
        		int beforeVersion = version.getVersion();
        		version.inc();
        		assertTrue(version.getVersion() > beforeVersion);
        	}
        });

        Thread t2 = new Thread(() -> {
        	for(int i = 0; i < 50; i++){
        		int beforeVersion = version.getVersion();
        		version.inc();
        		assertTrue(version.getVersion() > beforeVersion);
        	}   		
        });
        
        Thread t3 = new Thread(() -> {
        	for(int i = 0; i < 50; i++){
        		int beforeVersion = version.getVersion();
        		version.inc();
        		assertTrue(version.getVersion() > beforeVersion);
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
		assertTrue(version.getVersion() >= 151+firstVersion);
	}

	/**
	 * Test method for {@link bgu.spl.a2.VersionMonitor#await(java.lang.Object)}.
	 */
	public void testAwait() {
		VersionMonitor version = new VersionMonitor();
		int firstVersion = version.getVersion();
		Thread t1 = new Thread(() -> {
        	for(int i = 0; i < 50; i++){
        		try {
					version.await(firstVersion+2);
					//wait for it
					if(firstVersion+2 >= version.getVersion())
						Assert.fail("continued before the version updated");						
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}   		
        });
		Thread t2 = new Thread(() -> {
    		//firstVersion -> what should Not happened
			version.inc();
			//firstVersion + 1 -> what should Not happened
			version.inc();
			//firstVersion + 2 -> what should Not happened
			version.inc();
			//firstVersion + 3 -> what should happened in await function
			version.inc();
			//firstVersion + 4 -> what should Not happened        	
        });
		t1.start();
		try {
			t1.join();
		}catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        t2.start();
	}

}
