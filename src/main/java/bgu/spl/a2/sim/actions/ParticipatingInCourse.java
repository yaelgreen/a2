package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class ParticipatingInCourse extends Action<Boolean> {
	
	//Behavior: This action should try to register the student in the course, if it succeeds, should add the
	//course to the grades sheet of the student, and give him a grade if supplied. See the input example.
	//Actor: Must be initially submitted to the course's actor.
	
	private String course; 
	private String student;
	private String[] grades;

	public ParticipatingInCourse(String student, String course, String[] grades) {
		this.course = course;
		this.student = student;
		this.grades = grades;
		setActionName("Participate In Course");
	}

	@Override
	protected void start() {
		CoursePrivateState courseState = (CoursePrivateState) state;
		if(courseState.getAvailableSpots() <= 0)
		{
			complete(false);
			return;
		}
		Action<Boolean> checkStudentPrequisites = new checkPrequisites(courseState.getPrequisites());
		Action<Boolean> registerStudent = new Register(course, grades);
		
		sendMessage(checkStudentPrequisites, student, new StudentPrivateState());
		
		List<Action<Boolean>> actionList = new ArrayList<Action<Boolean>>();
		actionList.add(checkStudentPrequisites);
		
		//if student have the prequisites we check if there is available seat for him,
		//and if there is we will save a seat for him and ask him actor to register him
		then(actionList, () -> {
			if (!checkStudentPrequisites.getResult().get() | courseState.getAvailableSpots() <= 0 ) {
				complete(false);
				return;
			}
			courseState.setRegistered(courseState.getRegistered()+1);
			courseState.setAvailableSpots(courseState.getAvailableSpots()-1);
			List<String> newRegisteredList = courseState.getRegStudents();
			newRegisteredList.add(student);
			courseState.setRegStudents(newRegisteredList);
			sendMessage(registerStudent, student, new StudentPrivateState());
			registerStudent.getResult().subscribe(()->complete(true));;
		});
	}

}
