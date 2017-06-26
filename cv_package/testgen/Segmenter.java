package cv_package.testgen;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cv_package.basicelem2.Form;
import cv_package.basicelem2.Mark;
import cv_package.basicelem2.Text;
import cv_package.basicelem2.Type;
import cv_package.basicelem2.Blob;
import cv_package.filereader.FormFileReader2;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.testui.PanelBuilder;

public class Segmenter {

	String testpath = "C:/Users/Hannah/Desktop/Test-Images";
//	String testpath_form = "C:/Users/Hannah/Desktop/form-B.txt";
	String testpath_form = "Eto na talaga/form-B.txt";

	private static Filtering filter = Filtering.getInstance();
	private static ComputerVision cv = ComputerVision.getInstance();

	OCR ocr = OCR.getInstance();
	OMR omr = OMR.getInstance();
	
	PanelBuilder pb;
	Form form;
	
	public Segmenter(PanelBuilder pb) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.pb = pb;
		createFolder();
		form = createForm();
	}
	
	public void segment(String filepath) {
		filepath = filepath.replace("\\", "/");
		
		Mat img = Imgcodecs.imread(filepath);
		String path0 = save("0-original", img);
//		pb.addImage("ORIG", path0);
		
//		double x = 0.5;
//		Imgproc.resize(img, img, new Size(0, 0), x, x, Imgproc.INTER_LINEAR);
		String path1 = save("1-original", img);
//		pb.addImage("RESIZE", path1);
		
		cv.preprocess(img);
		String path2 = save("2-preprocess", img);
//		pb.addImage("PREPROC", path2);
		
		form.image = img;
		go(img, form.components);
		
		// use examination form
		
		// different panel
		// integrate with android
		// integrate mnist recog

		// do related forms
//		for(Type c:form.components) {
//			switch(c.typename) {
//			case "TEXT": 
//				// show characters
//				// show resulting text
//				break;
//			case "MARK": 
//				// show mark image
//				// show mark values
//				// show decisions
//				break;
//			case "BLOB":
//				// show blob image
//			}
//		}
//		List<Mat> imgs = filter.largeAreaElements
//				(img.clone(), cv.findContours(img.clone(), Imgproc.RETR_EXTERNAL), form.components.size());
//		int i = 0;
//		for(Mat im:imgs) {
//			String path = save("3-large-"+i, im);
//			pb.addImage("LARGE "+i, path);
//			ext(i, im, form.components.get(i));
//			i++;
//		}
		
		// get and show each form stuff
	}
	
	private void go(Mat image, ArrayList<Type> components) {
		
		List<Mat> mats = filter.largeAreaElements
				(image.clone(), cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL), components.size());
		
		int index = 0;
		
		for(Type c:components) {
			
			c.image = mats.get(index);
			String path2 = save("3-PROCESS-"+index, c.image);
			pb.addImage("PREPROC "+index+"-"+c.label, path2);
			
//			switch(c.typename) {
//			case "TEXT": ocr.go((Text)c); break;
//			case "MARK": omr.go((Mark)c); break;
//			case "BLOB": goBlob((Blob)c, mats.get(index)); break;
//			default: System.out.println("Error typename invalid");
//			}
			
			index++;
			
		}
	}
	
	public void goBlob(Blob comp, Mat img) {
    	img = filter.removeOutline2(img); 
		Imgproc.threshold(img, img, 100, 255, Imgproc.THRESH_BINARY_INV); 	
		comp.image = img;
    }
	
	
//	public void ext(int x, Mat img, Type comp) {
//		switch(comp.typename) {
//		case "TEXT":
//			OCR ocr = new OCR();
//			ocr.go(img, (Text)comp);
////			((Text)comp).characterMats 
////				= new ArrayList<>(ocr.getMatListCharsClean(img, ((Text)comp).characterCount));
//			
////			int i = 1;
////			for(Mat im:lets) {
////				String path = save("4-text-"+x+"-"+i, im);
////				pb.addImage(x+" TEXT "+i, path);
////				comp.imagesProcessed.add(im);
////				comp.imagesProcessedLabels.add("TEXT "+x+"-"+i);
////				i++;
////			}
//			break;
//		case "MARK":
//			OMR omr = new OMR();
//			omr.go(img, (Mark)comp);
////			List<Mat> mats = omr.getMatCirclesClean(img, ((Mark)comp).labels.size()+1);
////			List<Integer> values = omr.countWhiteOfMats(mats);
////			List<Boolean> decisions = omr.decideIfMarked(values);
////			((Mark)comp).markMats = new ArrayList<>(mats);
////			((Mark)comp).markValues = new ArrayList<>(values);
////			((Mark)comp).markDecision = new ArrayList<>(decisions);
//			
////			int j = 1;
////			for(Mat im:omrs) {
////				String path = save("4-MARK-"+x+"-"+j, im);
////				pb.addImage(x+" MARK "+j, path);
////				j++;
////			}
//			break;
//		case "BLOB":
//			filter.blob(img, (Blob)comp);
////			img = filter.removeOutline2(img); 
////			Imgproc.threshold(img, img, 100, 255, Imgproc.THRESH_BINARY_INV); 	
////			((Blob)comp).image = img;
////			String path = save("4-BLOB-"+x, img);
////			pb.addImage(x+" BLOB ", path);
//			break;
//		default:
//			System.out.println("Error typename invalid");
//		}
//	}
	
	public Form createForm() {
		FormFileReader2 reader = new FormFileReader2();
		Form form = new Form();
		reader.readToForm(testpath_form, form);
		return form;
	}
	
	public void createFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.HH.mm.ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		testpath += File.separator + sdf.format(timestamp);
		new File(testpath).mkdir();
	}
	
	public String save(String name, Mat img) {
		String path = testpath+File.separator+name+".png";
		Imgcodecs.imwrite(path, img);
		return path;
	}
	
}
