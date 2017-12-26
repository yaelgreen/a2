package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class ParticipatingInCourse extends Action<Boolean> {
	
	/**
	 * Behavior: This action should try to register the student in the course, if it succeeds, should add the
	 * course to the grades sheet of the student, and give him a grade if supplied. See the input example.
	 * Actor: Must be initially submitted to the course's actor.
	 */
	
	private String student;
	private String[] grades;

	public ParticipatingInCourse(String student, String[] grades) {
		this.student = student;
		this.grades = grades;
		setActionName("Participate In Course");
	}

	@Override
	protected void start() {
		CoursePrivateState courseState = (CoursePrivateState) currentState;
		if(courseState.getRegStudents().contains(student))
		{
			complete(true);
			return;
		}
		if(courseState.getAvailableSpots() <= 0)
		{
			complete(false);
			return;
		}
		courseState.getRegStudents().add(student);//we will do it to see later if unregister had been called,
		//also to prevent double registration
		Action<Boolean> checkStudentPrequisites = new CheckPrequisites(courseState.getPrequisites());		
		sendMessage(checkStudentPrequisites, student, new StudentPrivateState());

		Action<Boolean> registerStudent = new Register(currentActorId, grades);
		//if student have the prerequisites we check if there is available seat for him,
		//and if there is we will save a seat for him and ask him actor to register him
		checkStudentPrequisites.getResult().subscribe(() -> {
			//unregister has been called
			if(!courseState.getRegStudents().contains(student))
			{
				courseState.setRegistered(courseState.getRegistered()+1);
				courseState.setAvailableSpots(courseState.getAvailableSpots()-1);
				complete(false);
				return;
			}
			//we can not want to register this student
			if (!checkStudentPrequisites.getResult().get() | courseState.getAvailableSpots() <= 0 ) {
				courseState.getRegStudents().remove(student);
				complete(false);
				return;
			}
			courseState.setRegistered(courseState.getRegistered()+1);
			courseState.setAvailableSpots(courseState.getAvailableSpots()-1);
			sendMessage(registerStudent, student, new StudentPrivateState());
			registerStudent.getResult().subscribe(()->complete(true));;
		});
	}

}
