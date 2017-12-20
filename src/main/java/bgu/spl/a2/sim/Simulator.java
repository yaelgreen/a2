/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.actions.AddStudent;
import bgu.spl.a2.sim.actions.AnnounceEndOfRegistration;
import bgu.spl.a2.sim.actions.CheckAdministrativeObligations;
import bgu.spl.a2.sim.actions.CloseACourse;
import bgu.spl.a2.sim.actions.OpenANewCourse;
import bgu.spl.a2.sim.actions.OpeningNewPlacesInACourse;
import bgu.spl.a2.sim.actions.ParticipatingInCourse;
import bgu.spl.a2.sim.actions.Unregister;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	public static InputData input; 
	public static Warehouse warehouse;
	
	private static InputData ParseJson(String inputFilePath) {
		 BufferedReader bufferedReader;
		 InputData json = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(inputFilePath));
			Gson gson = new Gson();
			json = gson.fromJson(bufferedReader, InputData.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	private static void submitPhaseActions(ArrayList<InputDataPhaseObject> phase) {
		for (InputDataPhaseObject data : phase) {
			if (data.action.isEmpty())
				break;
			Action action = null;
			String actorId = UUID.randomUUID().toString();
			PrivateState actorState = null;
			switch (data.action) {
	         case "Open Course":
	        	 int space = Integer.parseInt(data.space);
	        	 ArrayList<String> pre = new ArrayList<String>(Arrays.asList(data.prerequisites));
	        	 action = new OpenANewCourse(space, pre);
	        	 actorState = new DepartmentPrivateState(); 
	             break;
	         case "Add Student":
	        	 action = new AddStudent(data.student, data.department);
	        	 actorState = new DepartmentPrivateState(); 
	             break;  
	         case "Participate In Course":
	        	 action = new ParticipatingInCourse(data.student, data.course);
	        	 actorState = new CoursePrivateState();
	        	 break;
	         case "Unregister":
	        	 action = new Unregister(data.student, data.course);
	        	 actorState = new CoursePrivateState();
	        	 break;
	         case "Administrative Check":
	        	 action = new CheckAdministrativeObligations();
	        	 actorState = new DepartmentPrivateState(); 
	        	 break;
	        // TODO: the last three where not in the Json example
	        // verify if there should be an option to call them from the Json file
	        // and if the answer is yes, verify the correct Names
	         case "Close A Course":
	        	 action = new CloseACourse(data.course);
	        	 actorState = new DepartmentPrivateState(); 
	        	 break;
	         case "Opening New places In a Course":
	        	 action = new OpeningNewPlacesInACourse(data.course, Integer.parseInt(data.space));
	        	 actorState = new CoursePrivateState();
	        	 break;
	         case "Announce about the end of registration period":
	        	 action = new AnnounceEndOfRegistration();
	        	 actorState = new DepartmentPrivateState(); 
	        	 break;
			}
			actorThreadPool.submit(action, actorId, actorState);
		}
	}
	
	private static boolean SubmitActions(InputData input) {
		submitPhaseActions(input.phase1);
		//TODO: make sure all action in previous stage are completed before continuing 
		submitPhaseActions(input.phase2);
		//TODO: make sure all action in previous stage are completed before continuing 
		submitPhaseActions(input.phase3);
		//TODO: make sure all action in previous stage are completed before continuing 
		return true;
	}
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
    	warehouse = new Warehouse(input.Computers);
    	// Submit actions to the thread pool passed to the method attachActorThreadPool.
    	boolean res2 = SubmitActions(input);
    	//DO NOT create an ActorThreadPool in start. You need to attach the ActorThreadPool in the main
    	//method, and then call start.
    }
	
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool = myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
		//shut down the simulation.
		//returns a HashMap containing all the private states of the actors as serialized object 
		//to the le "result.ser".
		//TODO: Sould we close actor thraed pool here?
		FileOutputStream fout;
		HashMap<String,PrivateState> res = (HashMap<String, PrivateState>) actorThreadPool.getActors();
		try {
			fout = new FileOutputStream("result.ser");
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(fout);
			oos.writeObject(res);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	
	public static void main(String[] args) {
		if (args.length == 0 || args[0].isEmpty())
			System.out.println("No arguments supllied, or bad arguments");
		else {
			InputData input = ParseJson(args[0]); 
			Simulator sim = new Simulator();
			ActorThreadPool pool = new ActorThreadPool(input.threads);
			sim.attachActorThreadPool(pool);
			sim.start();
			sim.end();
		}
			
	}
}