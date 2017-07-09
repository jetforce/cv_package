package cv_package.basicelem2;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class Table extends Type {
	
	//This table only supports 2 Columns so far
	// OMR being the first column
	// Blob being the 2nd Column
	
	//This is the first OMR column
	public  Mark markColumn;
	
	//this the 2nd Blob Column
	public ArrayList<Blob> blobColumn = new ArrayList<>();
	
	//Number of Rows
	public int numRows;
	
	
	public Table(int numRows){
		this.numRows = numRows;
		super.typename= "TABLE";
		markColumn = new Mark();
		blobColumn = new ArrayList<>();
	}
	
	public void setMarkLabels(ArrayList<String> labels){
		markColumn.labels = labels;
	}
	
	
	public Mat getMarkBlob(int row){
		return markColumn.markMats.get(row);	
	}
	
	public Mat getBlob(int row){
		return blobColumn.get(row).image;
	}
	
	public void addMark(Mat blobImage , int value){
		markColumn.addMark(blobImage, value);
	}
	
	public void addBlob(Mat blobImage){	
		Blob temp = new Blob();
		temp.image = blobImage;
		blobColumn.add(temp);	
	}
	 
	public ArrayList<Integer> getMarkValues(){
		return markColumn.markValues;
	}
	
	public void setDecisions(ArrayList<Boolean> decs){
		markColumn.markDecision = decs;
	}
	
	public void printDecs(){
		for(int i=0; i< markColumn.markDecision.size(); i++){
			System.out.println(">> Mark "+ markColumn.markDecision.get(i)+" value "+ markColumn.markValues.get(i) );
		}
	}
	
}
