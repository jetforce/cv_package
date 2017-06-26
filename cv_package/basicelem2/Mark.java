package cv_package.basicelem2;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class Mark extends Type {

	public ArrayList<String> labels;
	public ArrayList<Mat> markMats;
	public ArrayList<Integer> markValues;
	public ArrayList<Boolean> markDecision;
	
	public Mark() {
		typename = "MARK";
		labels = new ArrayList<>();
		markMats = new ArrayList<>();
		markValues = new ArrayList<>();
	}
	
}
