package bgu.spl.a2;

public class ActionA extends Action<Integer> {
	public ActionA()
	{
		setActionName("ActionA");
	}
	
	@Override
	protected void start() {
		Integer myInt = new Integer(506949*3569);
		complete(myInt);	
	}
}