package cv_package.testgen;

import org.opencv.core.Mat;

public class TemporaryClassifier implements CharacterClassifier {

	@Override
	public char classify(Mat image) {
		// TODO Auto-generated method stub
		return '-';
	}

	@Override
	public int classifyDigit(Mat image) {
		// TODO Auto-generated method stub
		return 1;
	}

}
