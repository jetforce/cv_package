package cv_package.dumps;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.opencv.core.Mat;

import cv_package.testui.PanelBuilder;

public class Folder {

	private static Images images = Images.getInstance();
	private static Error error = Error.getInstance();
	private static Time time = Time.getInstance();
	private static PanelBuilder pb = PanelBuilder.getInstance();
	
	private static Folder folder = new Folder();
    public static Folder getInstance() { return folder; }
    private Folder() { }
    
    public static String mainpath = "C:/Users/Hannah/Desktop/TEST";
    public static String folderpath;
    
    public String save(Mat img, String label) {
		String imagepath = images.save(img);
		String timediff = time.stamp((images.getCount()-1) + " - " + label);
//		pb.addImage(label + " - " + timediff, imagepath);
		return imagepath;
	}
    
    public String save(Mat img) {
		String imagepath = images.save(img);
		String timediff = time.stamp((images.getCount()-1) + "");
//		pb.addImage(timediff, imagepath);
		return imagepath;
	}
    
//    public String getPath() {
//    	return folderpath;
//    }
    
    public void createFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.HH.mm.ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		folderpath = mainpath + File.separator + sdf.format(timestamp);
		new File(folderpath).mkdir();
	}
    
    public void init() {
    	createFolder();
		images.setPath(folderpath);
		time.setPath(folderpath);
    }
    
}
