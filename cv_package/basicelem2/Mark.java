package cv_package.basicelem2;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class Mark extends Type {

	public ArrayList<String> labels;
	public ArrayList<Mat> markMats;
	public ArrayList<Integer> markValues;
	public ArrayList<Boolean> markDecision;
	public int choices;
	
	
	public Mark() {
		typename = "MARK";
		labels = new ArrayList<>();
		markMats = new ArrayList<>();
		markValues = new ArrayList<>();
	}
	
	public void addMark(Mat mark, int shade){
		markMats.add(mark);
		markValues.add(shade);
	}
	
	public void printDecs(){
		for(int i=0;i < markValues.size();i++){
			System.out.println(">> "+ markDecision.get(i) +" "+ markValues.get(i));
		}
	}
	
}
