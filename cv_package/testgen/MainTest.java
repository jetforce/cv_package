
//package cv_package.testgen;
//
//import java.io.File;
//import java.util.List;
//
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.Size;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
//
//import cv_package.filereader.FormFileReader2;
//import cv_package.helpers.ComputerVision;
//import cv_package.helpers.Filtering;
//
//import cv_package.basicelem2.Form;

//
//public class MainTest {
//
//	private static Filtering filter = Filtering.getInstance();
//	private static ComputerVision cv = ComputerVision.getInstance();
//	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		System.out.println("Starting..");
//		
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		
//		MainTest test = new MainTest();
//		test.formread();
//	
//		System.out.println("Done..");
//	}
//	
//	public void formread() {
//		FormFileReader2 reader = new FormFileReader2();
//		Form form = new Form();
//		reader.readToForm("C:/Users/Hannah/Desktop/form-A.txt", form);
//		
//		Mat img = Imgcodecs.imread("C:/Users/Hannah/Desktop/yo2.png");
//		double x = 0.5;
//		Imgproc.resize(img, img, new Size(0, 0), x, x, Imgproc.INTER_LINEAR);
//		cv.preprocess(img);
//		
//		List<Mat> imgs = filter.largeAreaElements
//				(img.clone(), cv.findContours(img.clone(), Imgproc.RETR_EXTERNAL), form.components.size());
//		
//	}
//	
//	public void ocrTest2() {
//		OCR ocr = new OCR();
//		Mat ocrimage = Imgcodecs.imread("Tests/OCR2/ocr2-test.png");
//		Imgproc.cvtColor(ocrimage, ocrimage, Imgproc.COLOR_BGR2GRAY);
//		Imgproc.threshold(ocrimage, ocrimage, 100, 255, Imgproc.THRESH_BINARY);
//		Imgcodecs.imwrite("Tests/OCR2/0 ORIGINAL.jpg", ocrimage);
//		
//		List<Mat> matStrings = ocr.getMatListStrings(ocrimage, 2);
//		
//		int i = 0;
//		for(Mat s:matStrings) {
//			Imgcodecs.imwrite("Tests/OCR2/1_"+i+".jpg", s);
//			i++;
//			
//			List<Mat> matChars = ocr.getMatListCharsClean(s, 2);
//			int j = 0;
//			for(Mat c:matChars) {
//				Imgcodecs.imwrite("Tests/OCR2/2_"+i+"_"+j+".jpg", c);
//				j++;
//			}
//		}
//		
//	}

//	
//	public void ocrTest() {
//		OCR ocr = new OCR();
//		Mat ocrimage = Imgcodecs.imread("Tests/OCR/ocr-test-4.png");
//		Imgproc.cvtColor(ocrimage, ocrimage, Imgproc.COLOR_BGR2GRAY);
//		Imgproc.threshold(ocrimage, ocrimage, 100, 255, Imgproc.THRESH_BINARY);
//		Imgcodecs.imwrite("Tests/OCR/0 ORIGINAL.jpg", ocrimage);
//		
//		List<Mat> largeElem = ocr.getMatListChar(ocrimage, 3);
//		
//		int i = 0;
//		for(Mat l:largeElem) {
//			Imgcodecs.imwrite("Tests/OCR/4 LETTER - "+i+".jpg", l);
//			Mat c = ocr.getMatChar(largeElem.get(i).clone());
//			Imgcodecs.imwrite("Tests/OCR/6 CHAR!!!"+i+".jpg", c);
//			i++;
//		}
//
//	}
//	
//	public void filterTest() {
//		Mat img = Imgcodecs.imread("Tests/border3.png");
//		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
//		Imgproc.threshold(img, img, 100, 255, Imgproc.THRESH_BINARY);
//		
//		filter.removeOutline(img);
//	}
//	
//	public void omrTest1() {
//		OMR omr = new OMR();
//		Mat omrimg = Imgcodecs.imread("Tests/omr-test-2.png");
////		Mat omrimg = Imgcodecs.imread("Tests/test.jpg");
//		Imgproc.cvtColor(omrimg, omrimg, Imgproc.COLOR_BGR2GRAY);
//		Imgproc.threshold(omrimg, omrimg, 100, 255, Imgproc.THRESH_BINARY);
//		
//		List<Mat> largeElem = filter.largeAreaElements(omrimg, cv.findContours(omrimg.clone(), Imgproc.RETR_EXTERNAL), 1);
//		Mat circleContainter = filter.removeOutline(largeElem.get(0));
//		List<Mat> circs = omr.getMatCircles(circleContainter, 4);
//		
//		for(int i = 0; i < circs.size(); i++) {
//			Imgcodecs.imwrite("Tests/CIRC-"+i+".jpg", circs.get(i)); 
//		}
//	}
//	
////	public void readForm() {
////		Form form = new Form();
////		FormFileReader file = new FormFileReader();
////
////		form.formImage = Imgcodecs.imread("input"+File.separator+"figure1.jpg");
////		file.readToForm("structs"+File.separator+"test", form);
////	}
////	
////	public void testPrintForm(Form form) {
////		System.out.println("Name: " + form.name);
////		System.out.println("Groups:\n");
////		
////		for(int i = 0; i < form.groupList.size(); i++) {
////			
////			Group group = (Group) form.groupList.get(i);
////			System.out.println("\n"+group.type + " ~ #" + i + " :\n");
////			for(int j = 0; j < group.inputList.size(); j++) {
////				System.out.println("> " + group.inputList.get(j).name + " ~ " + group.inputList.get(j).count);
////			}
////		}
////	}
//		
//}

