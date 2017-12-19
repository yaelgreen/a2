package bgu.spl.a2.sim;
import bgu.spl.a2.Promise;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 * 
 * Note: this class can be implemented without any synchronization. 
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {
	
	//Holds a ag which indicates if the computer is free or not, and has a queue of promises to be resolved once
	//the Mutex is available. In the Suspending Mutex there are two methods:
	//Up: Release the mutex.
	//Down: Acquire the mutex, If the mutex is not free, the thread should no be blocked. It should get a
	//promise which will be resolved later when the printer becomes available.
	//Note: The Suspending Mutex can be implemented without any synchronization. However, using synchro-
	//nization will be accepted as long as the implementation is blocking free.
	
	/**
	 * Constructor
	 * @param computer
	 */
	public SuspendingMutex(Computer computer){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 * 
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down(){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 */
	public void up(){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
}
