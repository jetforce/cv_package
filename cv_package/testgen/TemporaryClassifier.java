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

	@Override
	public int classifyNum(Mat image) {
		return 0;
	}

	@Override
	public char classifyAlpha(Mat image) {
		return 0;
	}

	@Override
	public char classifyAlphaNum(Mat image) {
		return 0;
	}

}
