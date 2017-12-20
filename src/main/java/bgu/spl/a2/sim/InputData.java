package bgu.spl.a2.sim;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class InputData {	
	int threads;
	ArrayList<Computer> Computers;
	@SerializedName("Phase 1") ArrayList<InputDataPhaseObject> phase1;
	@SerializedName("Phase 2") ArrayList<InputDataPhaseObject> phase2;
	@SerializedName("Phase 3") ArrayList<InputDataPhaseObject> phase3;

}