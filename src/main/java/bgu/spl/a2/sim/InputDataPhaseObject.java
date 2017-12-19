package bgu.spl.a2.sim;

import com.google.gson.annotations.SerializedName;

public class InputDataPhaseObject {
	
	@SerializedName("Action") private String action;
	@SerializedName("Department") private String department;
	@SerializedName("Course") private String course;
	@SerializedName("Student") private String student;
	@SerializedName("Grade") private String[] grade;
	@SerializedName("Space") private String space;
	@SerializedName("Prerequisites") private String[] prerequisites;
	@SerializedName("Conditions") private String[] conditions;
	@SerializedName("Computer") private String computer;
	@SerializedName("Students") private String[] students;

}
