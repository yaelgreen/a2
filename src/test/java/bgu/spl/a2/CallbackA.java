package bgu.spl.a2;

public class CallbackA implements callback{
	
	boolean isCalled = false;

	@Override
	public void call() {
		isCalled = true;
	}
	
	public Boolean isCalled() {
		return isCalled;
	}

}
