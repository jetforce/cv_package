package cv_package.dumps;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Error {

	private static Error error = new Error();
    public static Error getInstance() { return error; }
    private Error() { }
	
    private String errorStr = "";
    
    public void error(String errorStr) {
    	this.errorStr += errorStr;
    }
    
    public void printError() {
    	System.out.println(errorStr);
    }
    
    public void saveError(String path) {
    	try{
    	    PrintWriter writer = new PrintWriter(path+File.separator+"Error.txt", "UTF-8");
    	    writer.println(errorStr);
    	    writer.close();
    	} catch (IOException e) {
    	   // do something
    		e.printStackTrace();
    	}
    }
    
}
