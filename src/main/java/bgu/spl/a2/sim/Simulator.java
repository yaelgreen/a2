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
import bgu.spl.a2.sim.actions.Unregister;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

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
	 * @param phase - parse the actions and 
	 */
	private AtomicInteger submitPhaseActions(ArrayList<InputDataPhaseObject> phase) {
		List<Action<?>> phaseList = new ArrayList<Action<?>>();
		for (InputDataPhaseObject data : phase) {
			if (data.action.isEmpty())
				break;
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
	        	 action = new AddStudent(data.student, data.department);
	        	 actorState = new DepartmentPrivateState(); 
	             break;  
	         case "Participate In Course":
	        	 actorId = data.course;
	        	 action = new ParticipatingInCourse(data.student, actorId, data.grade);
	        	 actorState = new CoursePrivateState();
	        	 break;
	         case "Unregister":
	        	 actorId = data.course;
	        	 action = new Unregister(data.student, actorId);
	        	 actorState = new CoursePrivateState();
	        	 break;
	         case "Administrative Check":
	        	 actorId = data.department;
	        	 action = new CheckAdministrativeObligations();
	        	 actorState = new DepartmentPrivateState(); 
	        	 break;
	        // TODO: the last three where not in the Json example
	        // verify if there should be an option to call them from the Json file
	        // and if the answer is yes, verify the correct Names
	         case "Close A Course":
	        	 actorId = data.course;
	        	 action = new CloseACourse(actorId);
	        	 actorState = new DepartmentPrivateState(); 
	        	 break;
	         case "Opening New places In a Course":
	        	 actorId = data.course;
	        	 action = new OpeningNewPlacesInACourse(actorId, Integer.parseInt(data.space));
	        	 actorState = new CoursePrivateState();
	        	 break;
	         case "Announce about the end of registration period":
	        	 actorId = data.department;
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
	
	private boolean SubmitActions(InputData input) {
		AtomicInteger remainedActionCounter = submitPhaseActions(input.phase1);
		try {
			 while (remainedActionCounter.get() != 0){
				 synchronized (this) {
					 this.wait();
				}
				 }
		} catch (InterruptedException e) {		
			System.out.println("Error thrown");
		}
		System.out.println("Finish first phase");
		remainedActionCounter = submitPhaseActions(input.phase2);
		try {
			 while (remainedActionCounter.get() != 0){
				 synchronized (this) {
					 this.wait();
				}
			    }
		} catch (InterruptedException e) {		
			System.out.println("Error thrown");
		}
		System.out.println("Finish seconde phase");
		remainedActionCounter = submitPhaseActions(input.phase3);		
		try {
			 while (remainedActionCounter.get() != 0){
				 synchronized (this) {
					 this.wait();
				}
			    }
		} catch (InterruptedException e) {		
			System.out.println("Error thrown");
		}
		System.out.println("Finish third phase");
		return true;
	}
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
    	actorThreadPool.setWarehouse(new Warehouse(input.Computers));
    	actorThreadPool.start();
    	// Submit actions to the thread pool passed to the method attachActorThreadPool.
    	boolean res2 = new Simulator().SubmitActions(input);
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
		//TODO: Should we close actor thread pool here?
		actorThreadPool.shutdown();
		FileOutputStream fout;
		HashMap<String,PrivateState> res = (HashMap<String, PrivateState>) actorThreadPool.getActors();
		try {
			fout = new FileOutputStream("result.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(res);
			oos.close();
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
			input = ParseJson(args[0]);
			ActorThreadPool pool = new ActorThreadPool(input.threads);
			Simulator.attachActorThreadPool(pool);
			Simulator.start();
			Simulator.end();
		}
			
	}
}