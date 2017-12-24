package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

public class RegisterWithPreferences extends Action<Boolean> {
	
	private String student;
	private String[] preferences;
	private String[] grade;

	public RegisterWithPreferences(String student, String[] preferences, String[] grade) {
		this.student = student;
		this.preferences = preferences;
		this.grade = grade;
	}

	@Override
	protected void start() {
		for (int i = 0 ; i < preferences.length; i++) {
			Action<Boolean> participatingInCourse = new ParticipatingInCourse(student, preferences[i], new String[]{grade[i]});
			participatingInCourse.getResult().subscribe(()->
			{
				if(participatingInCourse.getResult().get()) {
					complete(true);
					return;
				}
			});
		}
		complete(false);
	}

}
