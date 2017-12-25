package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class RegisterWithPreferences extends Action<Boolean> {
	
	private String student;
	private String[] preferences;
	private String[] grade;

	public RegisterWithPreferences(String student, String[] preferences, String[] grade) {
		this.student = student;
		this.preferences = preferences;
		this.grade = grade;
		setActionName("Register With Preferences");
	}

	//register a student to one from his preferred courses by with priority to the first courses
	@Override
	protected void start() {
		Action<Boolean> first = new ParticipatingInCourse(student, preferences[0], new String[]{grade[0]});
		Action<Boolean> lastCourse = first;
		//create a list of calls one by one
		for (int i = 1 ; i < preferences.length - 1; i++) {
			Action<Boolean> currCourse = new ParticipatingInCourse(student, preferences[i], new String[]{grade[i]});
			String actorId = preferences[i];
			lastCourse.getResult().subscribe(()->
			{
				if(getResult().get()) 
					complete(true);				
				else
					sendMessage(currCourse, actorId, new CoursePrivateState());
			});
			lastCourse = currCourse;
		}
		//if no one succeeded complete(false)
		lastCourse.getResult().subscribe(()->
		{
			if(getResult().get()) 
				complete(true);				
			else
				complete(false);
		});
		sendMessage(first, preferences[0], new CoursePrivateState());
		
	}

}
