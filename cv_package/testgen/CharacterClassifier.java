package cv_package.testgen;

import org.opencv.core.Mat;

public interface CharacterClassifier {

	public char classify(Mat image);
	public int classifyDigit(Mat image);
	public int classifyNum(Mat image);
	public char classifyAlpha(Mat image);
	public char classifyAlphaNum(Mat image);
	
}
