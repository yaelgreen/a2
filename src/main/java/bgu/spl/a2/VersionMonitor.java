package bgu.spl.a2;

import java.util.LinkedList;
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
	private static AtomicInteger _awaitVersion = new AtomicInteger(0);
	private static LinkedList<Thread> _toNotifyList = new LinkedList<Thread>();
	private Thread _toNotify;
	VersionMonitor(Thread toNotify){
		_toNotify = toNotify; 
	}
	
    public int getVersion() {
        return _version.get();
    }
    
    /**
     * we will synchronized the list because we dont want threads to change the list while we iterate on it
     * anyway it will not be important for multiple inc because of the if
     */
    public static void inc() {    	    	
    	if(_awaitVersion.get() == _version.getAndIncrement())
	    	synchronized (_toNotifyList) {//mandatory in interrupt func
	    		for(Thread myThread : _toNotifyList)
	    			myThread.interrupt();  
	    		_toNotifyList.clear();
			} 	
    }

    /**
     * we will synchronized the list because we dont want threads to change the list while we adding to it
     * In addition we want to avoid sync problem between inc to the value _awaitVersion that we setting
     * for example when doing await and in the middle of it calling from another thread inc. it can make problem to the rule
     * whenever objects is in the list _awaitVersion.get() == _version.getAndIncrement(), more over one can be stuck in it until the next inc
     * @param version
     */
    public void await(int version) {
    	synchronized (_toNotifyList) {
	    	if (version != _version.get())
	            	_toNotify.interrupt(); 	    		
	    	else
	    	{
	    		_awaitVersion.set(version);
	    		_toNotifyList.add(_toNotify);	    		
	    	}
    	}
    }
}
