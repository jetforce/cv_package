package cv_package.helpers;

import cv_package.localadapters.LocalPrinter;

public class Printer implements LocalPrinter {

	@Override
	public void print(String tag, String info) {
		System.out.println(tag+ " >"+info);
	}

	
	
}
