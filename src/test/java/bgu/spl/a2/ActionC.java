package bgu.spl.a2;

import java.util.Collection;
import java.util.LinkedList;

public class ActionC extends Action<String>{
	public ActionC()
	{
		setActionName("ActionC");
	}
	
	@Override
	protected void start() {
		Action<Integer> a = new ActionA();
		Action<Integer> b = new ActionA();
		Collection<Action<?>> actionCollection =  new LinkedList<>();
		actionCollection.add(a);
		actionCollection.add(b);
		sendMessage(a, cuurActorId, new ActorState());
		sendMessage(b, "Actor2", new ActorState());
		then(actionCollection, ()->complete("finished"));
	}
}
