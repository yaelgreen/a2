package bgu.spl.a2;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.a2.sim.Warehouse;

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
	//we dont need to sync on the hash maps because every actor got init just once during all the running 
	private Map<String, Actor> _actorsMap = new ConcurrentHashMap<String, Actor>();
	private Worker[] workers;
	private Boolean runPermission;
	private Warehouse warehouse;
	
	public ActorThreadPool(int nthreads) {
		workers = new Worker[nthreads];
	}

	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		Map<String, PrivateState> res = new HashMap<String, PrivateState>();
		for (Map.Entry<String, Actor> pair : _actorsMap.entrySet()) 
			res.put(pair.getKey(), pair.getValue().getPrivateState());
		return res;
	}
	
	public void setWarehouse(Warehouse w) {
		this.warehouse = w;
	}
	
	public Warehouse getWarehouse() {
		return this.warehouse;
	}
	
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId){
		for (Map.Entry<String, Actor> pair : _actorsMap.entrySet()) {
			if (pair.getKey().equals(actorId))
				return pair.getValue().getPrivateState();
		}
		return null;
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
		if(!_actorsMap.containsKey(actorId))
			_actorsMap.put(actorId, new Actor(actorId, actorState));		
		_actorsMap.get(actorId).submit(action);
		VersionMonitor.inc();
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
	public void shutdown() {
		runPermission = false;
		for(Worker currWorker : workers)
			currWorker.interrupt();//we use sync because it is important that no one will interrupt this
		
		boolean joined;
		for(Worker currWorker : workers)
		{
			joined = false;
			while(!joined)
			{
				try {
					currWorker.join();
					joined = true;
				} catch (InterruptedException e) {				}
			}
		}
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		runPermission = true;
		for(int i = 0; i < workers.length; i++)
		{
			workers[i] = new Worker(this);
			workers[i].start();
		}
	}
	
	/**
	 * Worker class
	 * @author Roy
	 *
	 */
	private class Worker extends Thread {
		
		//VersionMonitor will interrupt for us
		private VersionMonitor _verMonitor = new VersionMonitor(this);
		private ActorThreadPool _mypool;
		
		private Worker(ActorThreadPool mypool){
			_mypool = mypool;
		}
		
		/**
		 * search for action to do on the actors and execute it
		 */
		@Override
		public void run() {
			System.out.println(this + " has been born");
			while(runPermission)
			{
				_verMonitor.await(_verMonitor.getVersion());
				for(Actor actor : _actorsMap.values())
				{
					if(actor.tryToOccupy())
					{
						System.out.println(Thread.currentThread() + ", occupy actor " + actor.getId());
						Action<?> toExecute = actor.getAction();
						//we should check if the worker can run this one?
						while(toExecute != null & runPermission)
						{							
							toExecute.handle(_mypool, actor.getId(), _mypool.getPrivateState(actor.getId()));
							toExecute = actor.getAction();
						}
						
						actor.releaseActor();
						System.out.println(Thread.currentThread() + ", released actor " + actor.getId());
					}
				}
				
				//if version await didnt interrupt us we can wait
				if(!Thread.interrupted() & runPermission)
				{
					try
					{
						System.out.println(this + " is wating");
						synchronized (this) {
							wait();//anyway the wait will turn the interrupt flag off
			    		}						
					}
					catch(InterruptedException Ex){	}//remove syso later if needed
					//System.out.println(this + " is awake");
				}
			}
			System.out.println(this + " passed out");
		}	
	}
	
	/**
	 * Actor class
	 * @author Roy
	 *
	 */
	private class Actor {
		private AtomicInteger occupied = new AtomicInteger(0);
		private String myId;
		private PrivateState state;
		private Queue<Action<?>> actorActions;
		
		private Actor(String id, PrivateState s){
			actorActions =new ConcurrentLinkedQueue<Action<?>>();
			myId = id;
			state = s;
		}
		
		public String getId() {			
			return myId;
		}
		
		public PrivateState getPrivateState() {			
			return state;
		}

		/**
		 * in case two threads (or more) want to occupy the Actor, if he is'nt occupied yet,
		 * we will use atomic integer to increment it during one clock so just one thread will occupy it
		 * @return true if the thread occupy the Actor
		 */
		public boolean tryToOccupy()
		{
			boolean output;
			if(occupied.get()>0)
				output = false;
			else if(occupied.getAndIncrement() == 0)//because of the atomicInteger method only one thread will get true in this line
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
		
		/**
		 * Add the newAction to the beginning of the queue
		 * @param newAction - a new action to execute
		 */
		public void submit(Action<?> newAction)
		{
			actorActions.add(newAction);
		}
		
		/**
		 * @return return the first action in the queue, and deleting it from the queue
		 */
		public Action<?> getAction()
		{
			return actorActions.poll();
		}
		
		/**
		 * @return the first action in the queue without polling (for cheking)
		 */
		public Action<?> getTopAction()
		{
			return actorActions.peek();
		}
	}
}
