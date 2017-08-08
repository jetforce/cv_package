package cv_package.testgen;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.virtusio.sibayan.formcreatormodule.PrototypeActivity;
import com.virtusio.sibayan.image_process.helpers.ComputerVisionUtility;
import com.virtusio.sibayan.image_process.helpers.ImageSaver;
import com.virtusio.sibayan.image_process.helpers.Logger;
import com.virtusio.sibayan.thesis_project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cv_package.basicelem2.Form;
import cv_package.basicelem2.Mark;
import cv_package.basicelem2.Set;
import cv_package.basicelem2.Table;
import cv_package.basicelem2.Text;
import cv_package.basicelem2.Type;
import cv_package.debug.LocalPrinter;

import cv_package.debug.Printer;
import cv_package.debug.Saver;
import cv_package.dumps.Error;
import cv_package.dumps.Folder;
import cv_package.dumps.Images;
import cv_package.dumps.Time;
import cv_package.basicelem2.Blob;
import cv_package.filereader.FormFileReader2;
import cv_package.helpers.BorderHandler;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;
import cv_package.helpers.Sorting;
import cv_package.paperextractorv2.FourCornerBoxv4;
import cv_package.segmentation.OpticalMarkSegmentationv2;
import cv_package.segmentation.SmartSegment;
import cv_package.segmentation.TableSegmentation;


public class Segmenter3 {

	private static Filtering filter = Filtering.getInstance();
	private static ComputerVision cv = ComputerVision.getInstance();
	private static OCR3 ocr = OCR3.getInstance();
	OpticalMarkSegmentationv2 omr = new OpticalMarkSegmentationv2(new Saver());
	TableSegmentation ts = TableSegmentation.getInstance();
	//private static Folder folder = Folder.getInstance();
	private static Time time = Time.getInstance();
	private CharacterClassifier classifier;
	private Context context;

	private ImageSaver imageSaver;

	Form form;
	
	private String filepath, structpath;
	
	private Mat img;

	/*
	public Segmenter3() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}*/
	
//	public void init(String filepath, String structpath) throws IOException {
//		this.filepath = filepath.replace("\\", "/");
//		this.structpath = structpath.replace("\\", "/");
//		
//		folder.init();
//		time.start();
//		hdc.init();
//		time.stamp("digit model done loading");
//	}
	
	public void init(String filepath, boolean isSaving, LocalPrinter printer, CharacterClassifier classifier) throws IOException {
		this.filepath = filepath.replace("\\", "/");

		//folder.setSaving(isSaving);
		//folder.init();
//		hdc.init();
		time.setPrinter(printer);
		time.start();
//		time.stamp("digit model done loading");
		
		this.classifier = classifier;
		
		img = Imgcodecs.imread(filepath);
	}
	
	public void init(Mat image, boolean isSaving, LocalPrinter printer, CharacterClassifier classifier) throws IOException {
//		this.filepath = filepath.replace("\\", "/");

		//folder.setSaving(isSaving);
		//folder.init();
//		hdc.init();
		time.setPrinter(printer);
		time.start();
//		time.stamp("digit model done loading");

		this.classifier = classifier;
		
		img = image;
	}
	
//	public void init(String filepath, boolean isSaving, LocalPrinter printer) throws IOException {
//		this.filepath = filepath.replace("\\", "/");
//
//		folder.setSaving(isSaving);
//		folder.init();
////		hdc.init();
//		time.setPrinter(printer);
//		time.start();
//		time.stamp("digit model done loading");
//		
//		img = Imgcodecs.imread(filepath);
//	}
//	
//	public void init(Mat image, boolean isSaving, LocalPrinter printer) throws IOException {
////		this.filepath = filepath.replace("\\", "/");
//
//		folder.setSaving(isSaving);
//		folder.init();
////		hdc.init();
//		time.setPrinter(printer);
//		time.start();
//		time.stamp("digit model done loading");
//		
//		img = image;
//	}
//

	public void initForm(InputStream is) {
		FormFileReader2 reader = new FormFileReader2();
		form = new Form();
		reader.readToForm(is, form);
	}



	public void initForm() {
		FormFileReader2 reader = new FormFileReader2();
		form = new Form();
		reader.readToForm(structpath, form);
	}




	public Form segment(Context context) throws IOException {
		//folder.save(img, "ORIG");
		//Change this to change text file
		this.context = context;

		imageSaver = new ImageSaver("Output",context);
//		tempsave("_before 4 corn", img);
		FourCornerBoxv4 extract = new FourCornerBoxv4(new ImageSaver("Output",context),new Logger());
		Size sz = new Size(1494,2656);
		Imgproc.resize( img, img, sz );
		img = extract.extractPaper(img);

		imageSaver.saveImage("normalized",img);

//		tempsave("_after 4 corn", img);

		cv.preprocess(img);
		//folder.save(img, "PREPROC");

		//tempsave("_preroc", img);

		time.stamp("form number extracting..");
		Mat formNumMat = getFormNumberMat(img);

		imageSaver.saveImage("formnumMat",formNumMat);

		int formNum = classifier.classifyDigit(formNumMat);
		InputStream is = null;
		switch(formNum){
			case 1: is = context.getResources().openRawResource(R.raw.w1); break;
			case 2: is = context.getResources().openRawResource(R.raw.w2); break;
			case 3: is = context.getResources().openRawResource(R.raw.w3); break;
			case 4: is = context.getResources().openRawResource(R.raw.w4); break;
		}


		//folder.save(formNumMat, "formnummat");
		//folder.save(img, "image after get formnum");


		initForm(is);

		form.formNumber = formNum;
		time.stamp("form init done..");
		time.stamp("form number:" + formNum);

//		tempsave("_formnum", formNumMat);
//		tempsave("_img", img);

		time.stamp("tempsave done");

		form.image = img;
		imageSaver.saveImage("formImage",img);
		go(form.image, form.components);

		time.end();
		return form;
	}

//	public void tempsave(String filename, Mat mat) {
//
//		String root = Environment.getExternalStorageDirectory().toString() + "/CONVERTER";
//		File myDir = new File(root);
//		myDir.mkdirs();
//
//		String fname = filename+ ".jpg";
//		File file = new File(myDir, fname);
//		if (file.exists()) file.delete();
//
//		ComputerVisionUtility cv = new ComputerVisionUtility();
//		Bitmap bmp = cv.convertToBitmap(mat);
//		FileOutputStream out = null;
//		try {
//			out = new FileOutputStream(file);
//			bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//			// PNG is a lossless format, the compression factor (100) is ignored
//			out.flush();
//			out.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (out != null) {
//					out.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}



	public Form segment() throws IOException {
		//folder.save(img, "ORIG");
		
    	FourCornerBoxv4 extract = new FourCornerBoxv4(new Saver(),new Printer());
		img = extract.extractPaper(img);
		
    	cv.preprocess(img);
    	//folder.save(img, "PREPROC");

    	
    	Mat formNumMat = getFormNumberMat(img);
    	int formNum = classifier.classifyDigit(formNumMat);
    	
    	//folder.save(formNumMat, "formnummat");
    	//folder.save(img, "image after get formnum");
    	
		time.stamp("form number extracting..");
//		int formNumber = getFormNumber(getbetter);
//		structpath = folder.getStructPath(formNumber);
		//structpath = folder.getStructPath(1);
		
		structpath = "input2/1.txt";
		initForm();
//		form.formNumber = formNumber;
		time.stamp("form init done..");
		
		form.image = img;
		go(form.image, form.components);
		
		time.end();
		return form;
	}
	
	private Mat getFormNumberMat(Mat image) {
		//List<MatOfPoint> contours = cv.findContours(image.clone(), Imgproc.RETR_EXTERNAL);
		List<MatOfPoint> contours =  cv.getSquareContours(image, 200, Imgproc.RETR_EXTERNAL);
		
		System.out.println("Contours size "+contours.size());

		Sorting sort = Sorting.getInstance();
		contours = sort.contourPositions(contours);
		
		MatOfPoint formNumCont = contours.get(0);
		
		Rect rect = Imgproc.boundingRect(formNumCont);
		
		Mat formNumMat = null;
		formNumMat = image.submat(rect);
		cv.invert(formNumMat);
		formNumMat = filter.removeOutlineChar(formNumMat);
		
		int start = rect.y + rect.height + 10;
//		time.stamp("start: "+start);
//		time.stamp("rows: "+image.rows());
//		time.stamp("cols: "+image.cols());
		img = image.submat(start, image.rows(), 0, image.cols());

		cv.invert(formNumMat);

		return formNumMat;
	}
	
	private void go(Mat image, ArrayList<Type> components) throws IOException {
		
		
		BorderHandler border = new BorderHandler();
		Mat location = border.getBorders(image, 100, 100);


		SmartSegment ss = new SmartSegment();


		imageSaver.saveImage("location",location);
		//List<Mat> mats = filter.largeAreaElements(image.clone(), cv.findContours(location, Imgproc.RETR_EXTERNAL), components.size());
		List<Mat> mats = filter.getImages(image, ss.getNearestContours(location, form.formNumber,this.context));
		int index = 0;

		for(Type c:components) {
			
			c.image = mats.get(index);

			imageSaver.saveImage("component"+index,c.image);

//			String path2 = save("3-PROCESS-"+index, c.image);
//			pb.addImage("PREPROC "+index+"-"+c.label, path2);
			//folder.save(c.image, "3-PROCESS-"+index);
			
			switch(c.typename) {
			case "TEXT": 
//				long num = ocr.goo((Text)c); 
//				String num = ocr.gooString((Text)c);
//				time.stamp("num: "+num);

				ArrayList<String> chars = ocr.go((Text)c, classifier);
				String str = getArrayToString(chars);
				((Text)c).setText(str);
				((Text)c).setText(str);
				time.stamp("string: "+Arrays.toString(chars.toArray()));
				break;
			case "MARK": omr.go((Mark)c); break;
			case "BLOB": goBlob((Blob)c, mats.get(index)); break;
				case "TABLE": ts.go((Table) c,imageSaver,index); break;
			default: System.out.println("Error typename invalid");
			}
			
			index++;
			
		}
	}

	public String getArrayToString(ArrayList<String> chars) {
		String s = "";
		for(String c: chars) {
			s += c;
		}
		return s;
	}
	
	public void goBlob(Blob comp, Mat img) {
//    	img = filter.removeOutline2(img);
//		Imgproc.threshold(img, img, 100, 255, Imgproc.THRESH_BINARY_INV);
		//img = filter.removeBackground(img);
        img =  filter.removeOutlineChar(img);
		cv.invert(img);
		comp.image = img;
    }
	
}
