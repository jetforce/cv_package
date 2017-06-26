package cv_package.basicelem2;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class Text extends Type {
	
	public int characterCount;
	public String type;
	public ArrayList<Mat> characterMats;
	
	public Text() {
		typename = "TEXT";
		characterMats = new ArrayList<>();
	}
	
}
