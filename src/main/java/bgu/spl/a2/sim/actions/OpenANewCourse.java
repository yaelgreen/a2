package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;

public class OpenANewCourse extends Action{

	//Behavior: This action opens a new course in a specied department. 
	//The course has an initially available spaces and a list of prerequisites.
	//Actor: Must be initially submitted to the Department's actor.
	
	private int availableSpaces;
	private List<String> prerequisites;
	
	public OpenANewCourse(int spaces, ArrayList<String> pre) {
		this.availableSpaces = spaces;
		this.prerequisites = pre;
	}
	
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		
	}

}
