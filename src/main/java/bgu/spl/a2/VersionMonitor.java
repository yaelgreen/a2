package bgu.spl.a2;

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
    public int getVersion() {
        return _version.get();
    }
    
    /**
     * we need to sync on object in order to use notify all
     */
    public void inc() {    	    	
    	_version.getAndIncrement();
    	synchronized (this) {
	    	this.notifyAll();
    	}
    }

    /**     
     * This action will make the thread wait until
     * @param version - the version we wait for to changed
     */
    public void await(int version) {
    	if (version == _version.get()){
    		synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {		}
			} 
    	}
    }
}
