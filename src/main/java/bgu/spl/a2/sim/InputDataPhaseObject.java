package bgu.spl.a2.sim;

import com.google.gson.annotations.SerializedName;

public class InputDataPhaseObject {
	
	@SerializedName("Action") String action;
	@SerializedName("Department") String department;
	@SerializedName("Course") String course;
	@SerializedName("Student") String student;
	@SerializedName("Grade") String[] grade;
	@SerializedName("Space") String space;
	@SerializedName("Prerequisites") String[] prerequisites;
	@SerializedName("Conditions") String[] conditions;
	@SerializedName("Computer") String computer;
	@SerializedName("Students") String[] students;

}
