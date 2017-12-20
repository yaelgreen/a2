package bgu.spl.a2;

public class ActionB extends Action<String> {
	public ActionB()
	{
		setActionName("ActionB");
	}
	
	@Override
	protected void start() {
		complete("done 9");		
	}
}
