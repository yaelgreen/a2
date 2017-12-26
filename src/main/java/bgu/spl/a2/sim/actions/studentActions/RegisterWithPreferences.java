package bgu.spl.a2.sim.actions.studentActions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.actions.courseActions.ParticipatingInCourse;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class RegisterWithPreferences extends Action<Boolean> {
	
	private String[] preferences;
	private String[] grade;

	public RegisterWithPreferences(String[] preferences, String[] grade) {
		this.preferences = preferences;
		this.grade = grade;
		setActionName("Register With Preferences");
	}

	/**
	 * Register a student to one from his preferred courses by with priority to the first courses
	 * start at student actor
	 */
	@Override
	protected void start() {
		Action<Boolean> first = new ParticipatingInCourse(currentActorId, new String[]{grade[0]});
		Action<Boolean> lastCourse = first;
		//create a list of calls one by one
		for (int i = 1 ; i < preferences.length; i++) {
			Action<Boolean> currCourse = new ParticipatingInCourse(currentActorId, new String[]{grade[i]});
			String actorId = preferences[i];
			Action<Boolean> copyLast = lastCourse;
			lastCourse.getResult().subscribe(()->
			{
				if(copyLast.getResult().get()) 
					complete(true);				
				else
					sendMessage(currCourse, actorId, new CoursePrivateState());
			});
			lastCourse = currCourse;
		}
		//if no one succeeded complete(false)
		Action<Boolean> copyLast = lastCourse;
		lastCourse.getResult().subscribe(()->
		{
			if(copyLast.getResult().get()) 
				complete(true);				
			else
				complete(false);
		});
		sendMessage(first, preferences[0], new CoursePrivateState());
		
	}

}
