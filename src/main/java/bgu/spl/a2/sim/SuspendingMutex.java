package bgu.spl.a2.sim;
import java.util.concurrent.Semaphore;

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
	
	//Holds a flag which indicates if the computer is free or not, and has a queue of promises to be resolved once
	//the Mutex is available. In the Suspending Mutex there are two methods:
	//Up: Release the mutex.
	//Down: Acquire the mutex, If the mutex is not free, the thread should no be blocked. It should get a
	//promise which will be resolved later when the computer becomes available.
	//Note: The Suspending Mutex can be implemented without any synchronization. However, using synchro-
	//nization will be accepted as long as the implementation is blocking free.
	
	private Computer computer;
	Semaphore semaphore;
	
	/**
	 * Constructor
	 * @param computer
	 */
	public SuspendingMutex(Computer computer){
		this.semaphore = new Semaphore(1);
		this.computer = computer;
	}
	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 * 
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down(){
		Promise<Computer> computerPromise = new Promise<Computer>();
		boolean aquired = semaphore.tryAcquire();
		if (!aquired) {
			// TODO: add callback to promise
			Thread waitForSemaphore = new Thread((()->
			{				
				try {
					semaphore.acquire(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}		
			}));	//leagality?
			return computerPromise;
		}
		return computerPromise;
	}
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 */
	public void up(){
		this.semaphore.release();
	}
}
