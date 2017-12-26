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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
import bgu.spl.a2.sim.actions.RegisterWithPreferences;
import bgu.spl.a2.sim.actions.Unregister;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	public static InputData input; 
	
	/**
	 * 
	 * @param inputFilePath - a string
	 * @return InputData to parsed with Gson
	 */
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
	
	/**
	 * submit the action to actor thread pool
	 * @param phase - parse the actions and 
	 */
	private AtomicInteger submitPhaseActions(ArrayList<InputDataPhaseObject> phase) {
		List<Action<?>> phaseList = new ArrayList<Action<?>>();
		for (InputDataPhaseObject data : phase) {
			if (data.action.isEmpty())
				continue;
			Action<?> action = null;
			String actorId = null;
			PrivateState actorState = null;
			switch (data.action) {
	         case "Open Course":
	        	 int space = Integer.parseInt(data.space);
	        	 ArrayList<String> pre = new ArrayList<String>(Arrays.asList(data.prerequisites));
	        	 actorId = data.department;
	        	 action = new OpenANewCourse(space, pre, data.course);
	        	 actorState = new DepartmentPrivateState(); 
	             break;
	         case "Add Student":
	        	 actorId = data.department;
	        	 action = new AddStudent(data.student);
	        	 actorState = new DepartmentPrivateState(); 
	             break;  
	         case "Register With Preferences":
	        	 actorId = data.student;
	        	 action = new RegisterWithPreferences(data.preferences, data.grade);
	        	 actorState = new StudentPrivateState();
	        	 break;
	         case "Participate In Course":
	        	 actorId = data.course;
	        	 action = new ParticipatingInCourse(data.student, data.grade);
	        	 actorState = new CoursePrivateState();
	        	 break;
	         case "Unregister":
	        	 actorId = data.course;
	        	 action = new Unregister(data.student);
	        	 actorState = new CoursePrivateState();
	        	 break;
	         case "Administrative Check":
	        	 actorId = data.department;
	        	 action = new CheckAdministrativeObligations(data.computer, data.students, data.conditions);
	        	 actorState = new DepartmentPrivateState(); 
	        	 break;	        
	         case "Close Course":
	        	 actorId = data.department;
	        	 action = new CloseACourse(data.course);
	        	 actorState = new DepartmentPrivateState(); 
	        	 break;
	         case "Add Spaces":
	        	 actorId = data.course;
	        	 action = new OpeningNewPlacesInACourse(Integer.parseInt(data.number));
	        	 actorState = new CoursePrivateState();
	        	 break;
	         case "End Registeration":
	        	 actorId = "End Registeration";//will not work without actor
	        	 action = new AnnounceEndOfRegistration();
	        	 actorState = new DepartmentPrivateState(); 
	        	 break;
			}
			phaseList.add(action);
			actorThreadPool.submit(action, actorId, actorState);
		}
		AtomicInteger remainedActionCounter = new AtomicInteger(phaseList.size());
    	for(Action<?> action : phaseList)
       		action.getResult().subscribe(()->
       		{
       			//count down latch, an atomic counter that will count every action that been completed
       			if(remainedActionCounter.decrementAndGet() == 0)
       				synchronized(this) {
       					this.notifyAll();//should notify main (and all threads)
       				}
       		});
    	return remainedActionCounter;
	}
	
	/**
	 * submit the phases to {@link #submitPhaseActions(ArrayList)}
	 * @param input the gson data
	 */
	private void SubmitActions(InputData input) {
		AtomicInteger remainedActionCounter = submitPhaseActions(input.phase1);
		try {
			 while (remainedActionCounter.get() != 0){
				 synchronized (this) {
					 this.wait();
				}
			 }
		} catch (InterruptedException e) {	}
		remainedActionCounter = submitPhaseActions(input.phase2);
		try {
			 while (remainedActionCounter.get() != 0){
				 synchronized (this) {
					 this.wait();
				 }
			 }
		} catch (InterruptedException e) {		}
		remainedActionCounter = submitPhaseActions(input.phase3);		
		try {
			 while (remainedActionCounter.get() != 0){
				 synchronized (this) {
					 this.wait();
				}
			 }
		} catch (InterruptedException e) {		}
	}
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
    	actorThreadPool.setWarehouse(new Warehouse(input.Computers));
    	actorThreadPool.start();
    	new Simulator().SubmitActions(input);
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
	 * returns a HashMap containing all the private states of the actors -
     * as serialized object to the "result.ser".
	 * @return a map of all the private states
	 */
	public static HashMap<String,PrivateState> end(){		
		actorThreadPool.shutdown();
		FileOutputStream fout;
		HashMap<String,PrivateState> result = (HashMap<String, PrivateState>) actorThreadPool.getActors();
		try {
			fout = new FileOutputStream("result.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(result);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		if (args.length == 0 || args[0].isEmpty())
			System.out.println("No arguments supllied, or bad arguments");
		else {
			input = ParseJson(args[0]);
			ActorThreadPool pool = new ActorThreadPool(input.threads);
			Simulator.attachActorThreadPool(pool);
			Simulator.start();
			Simulator.end();
		}
			
	}
}