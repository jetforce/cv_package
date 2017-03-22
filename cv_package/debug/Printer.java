package cv_package.debug;

public class Printer implements LocalPrinter {

	@Override
	public void print(String tag, String info) {
		System.out.println(tag+ " >"+info);
	}

	
	
}
