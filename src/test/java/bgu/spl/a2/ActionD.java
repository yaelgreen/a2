package bgu.spl.a2;

import java.util.Collection;
import java.util.LinkedList;

public class ActionD extends Action<Long>{
	public ActionD()
	{
		setActionName("ActionD");
	}
	
	@Override
	protected void start() {
		Collection<Action<Integer>> actionCollection =  new LinkedList<>();
		for(int i = 0; i < 8; i++){			
			Action<Integer> action = new ActionA();
			Action<Integer> action2 = new ActionA();
			Action<Integer> action3 = new ActionA();
			actionCollection.add(action);
			actionCollection.add(action2);
			actionCollection.add(action3);
			sendMessage(action, "Actor2", new ActorState());
			sendMessage(action2, "Actor3", new ActorState());
			sendMessage(action3, "Actor4", new ActorState());
		}
		
		then(actionCollection, ()->{
			Long sum = new Long(0);
			for(Action<Integer> Aaction : actionCollection)
				sum += Aaction.getResult().get().intValue();
			complete(sum);
		});
	}

}
