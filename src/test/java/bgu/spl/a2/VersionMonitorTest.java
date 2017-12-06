package bgu.spl.a2;

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

	public void testGetVersion() {
		VersionMonitor version = new VersionMonitor();
		int firstVersion = version.getVersion();
		int secondtVersion = version.getVersion();
		assertEquals(firstVersion, secondtVersion);
	}

	public void testInc() {
		VersionMonitor version = new VersionMonitor();
		int firstVersion = version.getVersion();
		version.inc();
		int newVersion = version.getVersion();
		assertTrue(newVersion > firstVersion);
	}

	public void testAwait() {
		VersionMonitor version = new VersionMonitor();
		int firstVersion = version.getVersion();
		//TODO
	}

}
