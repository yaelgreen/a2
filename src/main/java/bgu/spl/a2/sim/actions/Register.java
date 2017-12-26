package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Register extends Action<Boolean> {
	
	String course;
	Integer grade;
	/**
	 * Called from student actor.
	 * register student to course with the grade that provided. if "-" provided the grade is -1
	 * @param course the actorID of the course
	 * @param grades the grade array of the student. we will use just the first value if exist
	 */
	public Register(String course, String[] grades) {
		this.course = course;
		if(grades.length < 1 || grades[0].equals("-"))
			grade = -1;
		else
			grade = Integer.parseInt(grades[0]);
		setActionName("Register");
	}

	@Override
	protected void start() {
		StudentPrivateState myState = (StudentPrivateState) currentState;
		myState.getGrades().put(course, grade);
		complete(true);
	}

}
