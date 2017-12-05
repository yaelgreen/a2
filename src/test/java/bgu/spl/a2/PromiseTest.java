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
		//test get throws an exception if it is not yet resolved
		try{
			p.get();
			Assert.fail();
		} catch(IllegalStateException exc){
			// expected exception
		}
		//test get returns the correct value once it is resolved
		try{
			p.resolve(new Integer(1));
		} catch(IllegalStateException resolveExc)
		{
			Assert.fail();
		}
		try{
			assertTrue(p.get().intValue() == 1);
		}catch(IllegalStateException e){
			Assert.fail();
		}
	}

	/**
	 * Test method for {@link bgu.spl.a2.Promise#isResolved()}.
	 */
	public void testIsResolved() {
		Promise<Integer> p = new Promise<>();
		assertFalse(p.isResolved());
		try {
			p.resolve(1);
		}catch (IllegalStateException e){
			Assert.fail();
		}
		assertTrue(p.isResolved());
	}

	/**
	 * Test method for {@link bgu.spl.a2.Promise#resolve(java.lang.Object)}.
	 */
	public void testResolve() {
		Promise<Integer> p = new Promise<>();
		CallbackA callback = new CallbackA();
		p.subscribe(callback);
		p.resolve(1);
		assertTrue(callback.isCalled());
		assertTrue(p.get() == new Integer(1));
		try {
			p.resolve(2);
			Assert.fail();
		}
		catch (IllegalStateException e) {
			// expected exception
		}
		assertTrue(p.get() == 1);
	}

	/**
	 * Test method for {@link bgu.spl.a2.Promise#subscribe(bgu.spl.a2.callback)}.
	 */

	public void testSubscribe() {
		//test if while calling subscribe the promise is already resolved-
		//the callback is called immediately
		Promise<Integer> p1 = new Promise<>();
		p1.resolve(1);
		CallbackA callback1 = new CallbackA();
		p1.subscribe(callback1);
		assertTrue(callback1.isCalled());
		assertEquals(callback1.getCalledCounter(), 1);
		
		Promise<Integer> p2 = new Promise<>();
		CallbackA callback2 = new CallbackA();
		CallbackA callback3 = new CallbackA();
		p2.subscribe(callback2);
		p2.subscribe(callback3);
		p2.resolve(2);
		assertTrue(callback2.isCalled());
		assertEquals(callback2.getCalledCounter(), 1);
		assertTrue(callback3.isCalled());
		assertEquals(callback3.getCalledCounter(), 1);
	}

}
