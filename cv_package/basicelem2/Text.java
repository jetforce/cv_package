package cv_package.basicelem2;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class Text extends Type {
	
	public int characterCount;
	public String type;
	public ArrayList<Mat> characterMats;

//	public String text= "asddffghjklaaaazxcvnm";
	public String text;

	public Text() {
		typename = "TEXT";
		characterMats = new ArrayList<>();
	}

	public void setText(String txt){
		this.text = txt;
	}

}
