package cv_package.testgen;

import org.opencv.core.Mat;

public interface CharacterClassifier {

	public char classify(Mat image);
	public int classifyDigit(Mat image);
	
}
