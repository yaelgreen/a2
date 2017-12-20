package bgu.spl.a2;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> {

	private String _actionName;
	private callback _myCall;
	private Promise<R> _myPromise = new Promise<R>();
	/**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start();
    

    private ActorThreadPool _currpool;
    protected String _cuurActorId;
    /**
    *
    * start/continue handling the action
    *
    * this method should be called in order to start this action
    * or continue its execution in the case where it has been already started.
    *
    * IMPORTANT: this method is package protected, i.e., only classes inside
    * the same package can access it - you should *not* change it to
    * public/private/protected
    *
    */
   /*package*/ final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) {	   
	   _currpool = pool;
	   _cuurActorId = actorId;
	   if(_myCall == null)
	   {
		   actorState.addRecord(_actionName);//add to record every action that executed
		   start();
	   }
	   else
	   {
		   _myCall.call();
	   }
   }
    
    
    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * 
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback task) {
       	AtomicInteger remainedActionCounter = new AtomicInteger(actions.size());
    	for(Action<?> action : actions)
       		action.getResult().subscribe(()->
       		{
       			//count down latch, an atomic counter that will count every action that been completed
       			if(remainedActionCounter.decrementAndGet() == 0)
       				_currpool.submit(this, _cuurActorId, null);//he should have a private state
       		});
    	_myCall = task;
    }

    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result) {
    	_myPromise.resolve(result);   
    }
    
    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult() {
    	return _myPromise;
    }
    
    /**
     * send an action to an other actor
     * 
     * @param action
     * 				the action
     * @param actorId
     * 				actor's id
     * @param actorState
	 * 				actor's private state (actor's information)
	 *    
     * @return promise that will hold the result of the sent action
     */
	public Promise<?> sendMessage(Action<?> action, String actorId, PrivateState actorState){
		_currpool.submit(action, actorId, actorState);
		return action.getResult();
	}
	
	/**
	 * set action's name
	 * @param actionName
	 */
	public void setActionName(String actionName){
		_actionName = actionName;
	}
	
	/**
	 * @return action's name
	 */
	public String getActionName(){
        return _actionName;
	}
}
