/**
 * 
 */
package bgu.spl.a2;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author yaelgree
 *
 */
public class PromiseTest extends TestCase {

	/**
	 * @param name
	 */
	public PromiseTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link bgu.spl.a2.Promise#get()}.
	 */
	public void testGet() {
		Promise<Integer> p = new Promise<>();
		try{
			p.get();
			Assert.fail();
		} catch(IllegalStateException exc){
			// expected exception
		}
		try{
			p.resolve(new Integer(1));
		} catch(IllegalStateException resolveExc)
		{
			Assert.fail();
		}
		try{
			assertTrue(p.get().intValue() == 1);
		}catch(IllegalStateException getExc){
			Assert.fail();
		}
	}

	/**
	 * Test method for {@link bgu.spl.a2.Promise#isResolved()}.
	 */
	public void testIsResolved() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link bgu.spl.a2.Promise#resolve(java.lang.Object)}.
	 */
	public void testResolve() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link bgu.spl.a2.Promise#subscribe(bgu.spl.a2.callback)}.
	 */
	public void testSubscribe() {
		fail("Not yet implemented");
	}

}
