package bgu.spl.a2.sim;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import bgu.spl.a2.Promise;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 * 
 * Holds a flag which indicates if the computer is free or not, and has a queue of promises to be resolved once
 * the Mutex is available. In the Suspending Mutex there are two methods:
 * {@link #up()}: Release the mutex.
 * {@link #down()}: Acquire the mutex, If the mutex is not free, the thread should no be blocked. It should get a
 * promise which will be resolved later when the computer becomes available.
 * Note: The Suspending Mutex can be implemented without any synchronization. However, using synchronization 
 * will be accepted as long as the implementation is blocking free.
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
	private Semaphore semaphore;
	private Queue<Promise<Computer>> promiseQueue = new ConcurrentLinkedQueue<Promise<Computer>>();
	/**
	 * Constructor
	 * @param computer the {@link bgu.spl.a2.sim.Computer} that this class will save
	 */
	public SuspendingMutex(Computer computer){
		this.semaphore = new Semaphore(1);
		this.computer = computer;
	}
	
	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediately
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down(){
		Promise<Computer> computerPromise = new Promise<Computer>();
		if (!semaphore.tryAcquire())
			promiseQueue.add(computerPromise);
		else
			computerPromise.resolve(computer);
		return computerPromise;
	}
	
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 */
	public void up(){
		if(promiseQueue.peek() != null)
			promiseQueue.poll().resolve(computer);
		else
			semaphore.release();
	}
}
