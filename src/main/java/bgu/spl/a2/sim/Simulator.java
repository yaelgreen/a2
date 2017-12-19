/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	public static String inputFilePath; 
	
	private static boolean ParseJson() {
		 BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(inputFilePath));
			Gson gson = new Gson();
		    Object json = gson.fromJson(bufferedReader, InputData.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean SubmitActions() {
		//TODO
		return false;
	}
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
		// Parse the Json Files.
    	boolean res1 = ParseJson();
    	// Submit actions to the thread pool passed to the method attachActorThreadPool.
    	boolean res2 = SubmitActions();
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
		//returns a HashMap containing all the private states of the actors as serialized object to the le "result.ser".
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	
	
	public static void main(String[] args) {
		if (args.length == 0 || args[0].isEmpty())
			System.out.println("No arguments supllied, or bad arguments");
		else {
			inputFilePath = args[0];
			// TODO: should we pass the path to simulator in it's constractor?
			Simulator sim = new Simulator();
			ActorThreadPool pool = new ActorThreadPool(3);
			sim.attachActorThreadPool(pool);
			sim.start();
		}
			
	}
}
