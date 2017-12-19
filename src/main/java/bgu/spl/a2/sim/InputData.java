package bgu.spl.a2.sim;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class InputData {
	
	private int threads;
	private Computer[] Computers;
	@SerializedName("Phase 1") private ArrayList<InputDataPhaseObject> phase1;
	@SerializedName("Phase 2") private ArrayList<InputDataPhaseObject> phase2;
	@SerializedName("Phase 3") private ArrayList<InputDataPhaseObject> phase3;

}