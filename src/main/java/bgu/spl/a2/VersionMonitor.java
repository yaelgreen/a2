package bgu.spl.a2;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {	
	private static AtomicInteger _version = new AtomicInteger(0);
	private static Queue<Thread> _toNotifyList = new ConcurrentLinkedQueue<Thread>();
	/**
	 * @return the current version
	 */
    public int getVersion() {
        return _version.get();
    }
    
    /**
     * Interrupt waiting threads. best to deal with thread safty
     */
    public void inc() {    	    	
    	_version.getAndIncrement();
    	Thread currThread;
    	while((currThread = _toNotifyList.poll()) != null)
    		currThread.interrupt(); 
    }

    /**     
     * This action will make the thread wait until
     * @param version - the version we wait for to changed
     * we will use synchronized to use wait function  
     */
    public void await(int version) {
    	//the first thing we will do is add it, later we will decide what to do with it
    	//System.out.println(Thread.currentThread() + ", " + version+"_"+_version.get());
    	_toNotifyList.add(Thread.currentThread());
    	if (version == _version.get())  {    		
    		synchronized (this) {
				try {
					//wait if it is fine or if we have to delete the interrupt mark
					if(version == _version.get() || !_toNotifyList.remove(Thread.currentThread()))
						this.wait();				
				} catch (InterruptedException e) {		}
			}
    	}
    	else//restore the situation as before
    		if(!_toNotifyList.remove(Thread.currentThread()))
    			Thread.interrupted();
    }
}