package bgu.spl.a2.sim;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class Computer {
	
	//A computer has a method of checkAndSign. This method gets a list of the grades of the students and a list
	//of courses he needs to pass (grade above 56). The method checks if the student passed all these courses.
	//Each computer has two different signatures. One signatures for sign that the student meets the requirement
	//and the other to sign the he does not.

	@SerializedName("Type") String computerType;
	@SerializedName("Sig  Fail") long failSig;
	@SerializedName("Sig Success") long successSig;
	
	public Computer(String computerType) {
		this.computerType = computerType;
	}
	
	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){
		for (String course : courses) {
			if (coursesGrades.get(course) <= 56)
				return failSig;
		}
		return successSig;
	}
}
