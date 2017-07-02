package cv_package.dumps;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Images {

	private static Images images = new Images();
    public static Images getInstance() { return images; }
    private Images() { }

    private int counter = 0;
    private String path;
    
    public void setPath(String path) {
    	this.path = path;
    }
    
    public String save(Mat image) {
    	String imagepath = path+File.separator+counter+".png"; 
    	Imgcodecs.imwrite(imagepath, image);
    	counter++;
    	return imagepath;
    }
    
    public int getCount() {
    	return counter;
    }
    
}
