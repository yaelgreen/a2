package bgu.spl.a2;

import org.junit.Assert;
import junit.framework.TestCase;

/**
 * @author yaelgree
 */
public class PromiseTest extends TestCase {

	public PromiseTest(String name) {
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
	 * Test method for {@link bgu.spl.a2.Promise#get()}.
	 */
	public void testGet() {
		Promise<Integer> promise = new Promise<>();
		//test get throws an exception if it is not yet resolved
		try{
			promise.get();
			Assert.fail("can't use get on unresolved promise!");
		} 
		catch(IllegalStateException exc){
			// expected exception
		}
		
		//test get returns the correct value once it is resolved
		try{
			promise.resolve(new Integer(1));
		} catch(IllegalStateException resolveExc){
			Assert.fail("failed to resolve");
		}
		
		try{
			assertTrue(promise.get().intValue() == 1);
		}catch(IllegalStateException e){
			Assert.fail("failed to resolve");
		}
	}

	/**
	 * Test method for {@link bgu.spl.a2.Promise#isResolved()}.
	 */
	public void testIsResolved() {
		Promise<Integer> promise = new Promise<>();
		assertFalse(promise.isResolved());
		try {
			promise.resolve(1);
		}catch (IllegalStateException e){
			Assert.fail("fail to resolve!");
		}
		assertTrue(promise.isResolved());
	}

	/**
	 * Test method for {@link bgu.spl.a2.Promise#resolve(java.lang.Object)}.
	 */
	public void testResolve() {
		Promise<Integer> promise = new Promise<>();
		CallbackA callback = new CallbackA();
		promise.subscribe(callback);
		promise.resolve(1);
		assertTrue(callback.isCalled());
		assertTrue(promise.get() == 1);
		try {
			promise.resolve(2);
			Assert.fail("can't resolve twice!");
		}catch (IllegalStateException e) {
			// expected exception
		}
		assertTrue(promise.get() == 1);
	}

	/**
	 * Test method for {@link bgu.spl.a2.Promise#subscribe(bgu.spl.a2.callback)}.
	 */

	public void testSubscribe() {
		/*test if when calling subscribe and the promise is already resolved-
			that the callback is called immediately*/
		Promise<Integer> p1 = new Promise<>();
		p1.resolve(1);
		CallbackA callback1 = new CallbackA();
		p1.subscribe(callback1);
		assertTrue(callback1.isCalled());
		assertEquals(callback1.getCalledCounter(), 1);
		//test if callback is used when promise is resolved
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
