package bgu.spl.a2;

public class CallbackA implements callback{
	//stub class
	
	boolean isCalled = false;
	int calledCounter = 0;

	@Override
	public void call() {
		isCalled = true;
		calledCounter++;
	}
	
	public Boolean isCalled() {
		return isCalled;
	}
	
	public int getCalledCounter() {
		return calledCounter;
	}

}
