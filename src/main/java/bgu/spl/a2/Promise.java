package bgu.spl.a2;

import java.util.List;
import java.util.ArrayList;

/**
 * this class represents a deferred result i.e., an object that eventually will
 * be resolved to hold a result of some operation, the class allows for getting
 * the result once it is available and registering a callback that will be
 * called once the result is available.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 * @param <T> the result type, when we will use {@link #resolve(java.lang.Object)} the promise,
 * we will can use {@link #get()}
 */
public class Promise<T>{
	
	private Object lock = new Object();
	private T _value = null;
	private List<callback> callbacks = new ArrayList<callback>();

	/**
	 *
	 * @return the resolved value if such exists (i.e., if this object has been
	 *         {@link #resolve(java.lang.Object)}ed 
	 * @throws IllegalStateException
	 *             in the case where this method is called and this object is
	 *             not yet resolved
	 */
	public T get() {
		if (_value != null)
			return _value;
		throw new IllegalStateException();		
	}

	/**
	 *
	 * @return true if this object has been resolved - i.e., if the method
	 *         {@link #resolve(java.lang.Object)} has been called on this object
	 *         before.
	 */
	public boolean isResolved() {
		if (_value != null)
			return true;
		return false;		
	}


	/**
	 * resolve this promise object - from now on, any call to the method
	 * {@link #get()} should return the given value
	 *
	 * Any callbacks that were registered to be notified when this object is
	 * resolved via the {@link #subscribe(callback)} method should
	 * be executed before this method returns
	 *
     * @throws IllegalStateException
     * 			in the case where this object is already resolved
	 * @param value
	 *            - the value to resolve this promise object with
	 */
	public void resolve(T value){
		if (_value != null)
			throw new IllegalStateException();
		synchronized (lock) {
			_value = value;
		}
		for(callback c : callbacks)
			c.call();
		callbacks.clear();
	}
 
	/**
	 * add a callback to be called when this object is resolved. If while
	 * calling this method the object is already resolved - the callback should
	 * be called immediately
	 *
	 * Note that in any case, the given callback should never get called more
	 * than once, in addition, in order to avoid memory leaks - once the
	 * callback got called, this object should not hold its reference any
	 * longer.
	 * we will use synchronized to prevent uncalled call-backs, 
	 * when we call {@link #isResolved()} another thread can call {@link #resolve(Object)} 
	 * and then run on the callbacks collection and when we add our callback to collection it will
	 * be called because the {@link #resolve(Object)} already run on the collection and no one will call our callback now. 
	 * synchronize here ({@link #subscribe(callback)} ) and in {@link #resolve(Object)} will prevent this situation
	 * @param callback
	 *            the callback to be called when the promise object is resolved
	 */
	public void subscribe(callback callback) {
		//we will use synchronized to prevent uncalled call-backs, when we call isResolved another thread can call resolve
		//and we will not call it see above
		synchronized (lock) {			
			if (isResolved())
				callback.call();
			else
				callbacks.add(callback);
		}		
	}
}
