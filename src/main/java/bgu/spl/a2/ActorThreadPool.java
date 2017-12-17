package bgu.spl.a2;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	Map<String, PrivateState> privateStateMap = new HashMap<String, PrivateState>();
	Map<String, Actor> actorsMap = new HashMap<String, Actor>();
	public ActorThreadPool(int nthreads) {
		// TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}

	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		return privateStateMap;
	}
	
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId){
		return privateStateMap.get(actorId);
	}

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		if(!privateStateMap.containsKey(actorId))
		{
			privateStateMap.put(actorId, actorState);
			actorsMap.put(actorId, new Actor());
		}
		actorsMap.get(actorId).submit(action);
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		// TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		// TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	
	class Actor {
		private AtomicInteger occupied = new AtomicInteger(0);
		private Queue<Action<?>> _ActorActions;
		private Actor(){
			_ActorActions = new LinkedList<Action<?>>(); 
		}
		
		/**
		 * in case two threads (or more) want to occupy the Actor, if he is'nt occupied yet,
		 * we will use atomic integer to increment it during one clock so just one thread will occupy it
		 * @return true if the thread occupy the
		 */
		public boolean tryToOccupy()
		{
			boolean output;
			if(occupied.get()>0)
				output = false;
			else if(occupied.getAndIncrement() == 0)
				output = true;
			else
				output = false;
			return output;
		}
		
		/**
		 * release the actor by retrieving the value to 0.
		 * will not influence on try to occupy because it be a matter just when we reach the first 'if', but there
		 */
		public void releaseActor()
		{
			occupied.set(0);
		}
		
		public void submit(Action<?> newAction)
		{
			_ActorActions.add(newAction);
		}
		
		/**
		 * we assume the the first
		 * @return the first action in the queue
		 */
		public Action<?> getTopAction()
		{
			return _ActorActions.peek();
		}
	}
}
