package cv_package.testgen;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cv_package.basicelem2.Form;
import cv_package.basicelem2.Mark;
import cv_package.basicelem2.Set;
import cv_package.basicelem2.Text;
import cv_package.basicelem2.Type;
import cv_package.dumps.Error;
import cv_package.dumps.Folder;
import cv_package.dumps.Images;
import cv_package.dumps.Time;
import cv_package.basicelem2.Blob;
import cv_package.filereader.FormFileReader2;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.testui.PanelBuilder;

public class Segmenter2 {

//	String testpath = "C:/Users/Hannah/Desktop/Test-Images";
//	String testpath_form = "C:/Users/Hannah/Desktop/form-B.txt";
//	String testpath_form = "Eto na talaga/form-B.txt";
//	String testpath_form = "C:/Users/Hannah/Desktop/here we go/form structures/1.txt";

	private static Filtering filter = Filtering.getInstance();
	private static ComputerVision cv = ComputerVision.getInstance();
	private static OCR ocr = OCR.getInstance();
	private static OMR omr = OMR.getInstance();
	private static Folder folder = Folder.getInstance();
	private static HandwrittenDigitClassifier hdc = HandwrittenDigitClassifier.getInstance();
	private static Time time = Time.getInstance();

	private int NEG_VALUE = -1;
	private int FORM_NUM_COUNT = 1;
	private int PATIENT_NUM_COUNT = 6;
	
	Form form;
	
	private String filepath, structpath;
	
	public Segmenter2() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		createFolder();
		
//		try {
//			hdc.init();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		time.stamp("digit classifier init done");
	}
	
	public void init(String filepath, String structpath) throws IOException {
		this.filepath = filepath.replace("\\", "/");
		this.structpath = structpath.replace("\\", "/");
		
		folder.init();
		time.start();
		hdc.init();
		time.stamp("digit model done loading");
		
		FormFileReader2 reader = new FormFileReader2();
		form = new Form();
		reader.readToForm(structpath, form);
	}
	
	public void segment() throws IOException {
		Mat img = Imgcodecs.imread(filepath);
		folder.save(img, "ORIG");

    	cv.preprocess2(img);
    	folder.save(img, "PREPROC");
		
		ArrayList<Mat> divisions = filter.divideImage(img, 2);
		Mat getbetter = divisions.get(0);		
		Mat main = divisions.get(1);
		
		folder.save(getbetter);
		folder.save(main);
		
		main = filter.removeOutlineFinal(main);
		form.image = main;

		folder.save(main, "removed");

		go(form.image, form.components);

		time.end();
	}
	
	private void getPrimaryInfo(Mat image) throws IOException {

		ArrayList<Mat> infoMat = (ArrayList<Mat>) 
				filter.largeAreaElements(image.clone(), cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL), 2);

		Mat patientNumMat = infoMat.get(0);
		Mat formNumMat = infoMat.get(1);

		folder.save(patientNumMat, "patient");
		folder.save(formNumMat, "form");

		hdc.init();
//		hdc.test();
		
		int patientNumber = ocr.go(patientNumMat, PATIENT_NUM_COUNT);
		time.stamp("Patient Number: " + patientNumber);
		
		int formNumber = ocr.go(formNumMat, FORM_NUM_COUNT);
		time.stamp("Form Number: " + formNumber);
		
	}
	
	private void go(Mat image, ArrayList<Type> components) throws IOException {
		
		List<Mat> mats = filter.largeAreaElements
				(image.clone(), cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL), components.size());
		
		int index = 0;
		
		for(Type c:components) {
			
			c.image = mats.get(index);
//			String path2 = save("3-PROCESS-"+index, c.image);
//			pb.addImage("PREPROC "+index+"-"+c.label, path2);
			folder.save(c.image, "3-PROCESS-"+index);
			
			switch(c.typename) {
			case "TEXT": 
				long num = ocr.goo((Text)c); 
				time.stamp("num: "+num);
				break;
//			case "MARK": omr.go((Mark)c); break;
//			case "BLOB": goBlob((Blob)c, mats.get(index)); break;
			default: System.out.println("Error typename invalid");
			}
			
			index++;
			
		}
	}
	
	public void goBlob(Blob comp, Mat img) {
    	img = filter.removeOutline2(img); 
		Imgproc.threshold(img, img, 100, 255, Imgproc.THRESH_BINARY_INV); 	
		comp.image = img;
    }
	
//	public void save(Mat img, String label) {
//		String imagepath = images.save(img);
//		String timediff = time.stamp(images.getCountStr() + " - " + label);
//		pb.addImage(label + " - " + timediff, imagepath);
//	}
	
//	public Form createForm() {
//		FormFileReader2 reader = new FormFileReader2();
//		Form form = new Form();
//		reader.readToForm(path, form);
//		return form;
//	}
	
//	public void createFolder() {
//		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.HH.mm.ss");
//		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		testpath += File.separator + sdf.format(timestamp);
//		new File(testpath).mkdir();
//	}
//	
//	public String save(String name, Mat img) {
//		String path = testpath+File.separator+name+".png";
//		Imgcodecs.imwrite(path, img);
//		return path;
//	}
	
}
