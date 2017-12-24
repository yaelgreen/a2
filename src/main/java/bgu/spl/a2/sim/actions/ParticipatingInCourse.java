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
	
	private String course; //TODO: need to change to String[] course
	private String student;
	private String[] grades;
	private String actionName = "Participate In Course";

	public ParticipatingInCourse(String student, String course, String[] grades) {
		this.course = course;
		this.student = student;
		this.grades = grades;
	}
	
	@Override
    protected String getName(){
        return actionName; 
    }

	@Override
	protected void start() {
		CoursePrivateState courseState = (CoursePrivateState) state;
		//TODO: need to add here a loop on course names. if first dosen't work go to next one. 
		if(courseState.getAvailableSpots() <= 0)
		{
			complete(false);
			return;
		}
		Action<Boolean> checkStudentPrequisites = new checkPrequisites(courseState.getPrequisites());
		Action<Boolean> registerStudent = new Register(course, grades);
		Action<Boolean> checkCourseHasRoom = new CheckCourseHasRoom(student);
		checkStudentPrequisites.getResult().subscribe(()->
		{
			if(checkStudentPrequisites.getResult().get()) {
				checkCourseHasRoom.getResult().subscribe(()->
				{
					if (checkCourseHasRoom.getResult().get()) {
						sendMessage(checkCourseHasRoom, course, new CoursePrivateState());
						sendMessage(registerStudent, student, new StudentPrivateState());
					}
					else
						checkCourseHasRoom.getResult().resolve(false);
				});
			}
			else//didn't have all the Prequisites
				registerStudent.getResult().resolve(false);
		});
		sendMessage(checkStudentPrequisites, student, new StudentPrivateState());
		List<Action<Boolean>> actionList = new ArrayList<Action<Boolean>>();
		actionList.add(checkStudentPrequisites);
		actionList.add(registerStudent);
		actionList.add(checkCourseHasRoom);
		then(actionList, () -> complete(registerStudent.getResult().get()));
	}

}
