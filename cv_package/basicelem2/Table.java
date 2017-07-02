package cv_package.basicelem2;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class Table extends Type {
	
	//This table only supports 2 Columns so far
	// OMR being the first column
	// Blob being the 2nd Column
	
	//This is the first OMR column
	private Mark markColumn;
	
	//this the 2nd Blob Column
	private ArrayList<Blob> blobColumn = new ArrayList<>();
	
	//Number of Rows
	public int numRows;
	
	
	public Table(int numRows){
		this.numRows = numRows;
		super.typename= "TABLE";
		markColumn = new Mark();
		blobColumn = new ArrayList<>();
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
	
}
