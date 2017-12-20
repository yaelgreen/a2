package bgu.spl.a2.sim;

import java.util.ArrayList;
import java.util.List;

/**
 * represents a warehouse that holds a finite amount of computers
 * and their suspended mutexes.
 * releasing and acquiring should be blocking free.
 */
public class Warehouse {
	
	//The warehouse class holds a fnite amount of computers. Each computer has a Suspending Mutex
	//When the department wants to acquire a computer, it should lock its mutex if it is free. And
	//release it once it finished the work with the computer. If the computer is not free, the department should not
	//be blocked. It should get a promise which will be resolved later when the computer becomes available.
	
	private List<Computer> computers;
	
	public Warehouse(ArrayList<Computer> computers) {
		this.computers = computers;//"and their suspended mutexes"?releasing and acquiring should be blocking free.
	}
	
}
