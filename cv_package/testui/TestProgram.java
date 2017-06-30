package cv_package.testui;

import java.io.IOException;

import cv_package.testgen.HandwrittenDigitClassifier;

public class TestProgram {

	public static void main(String args[]) throws IOException {
		HandwrittenDigitClassifier m = new HandwrittenDigitClassifier();
		m.init();
		m.test();
	}
	
}
