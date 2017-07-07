package cv_package.paperextractorv2;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import cv_package.debug.LocalPrinter;
import cv_package.debug.LocalSaver;
import cv_package.segmentation.FourSquareCornerv2;

public class FourCornerBoxv3 {
	//This one assumes the input's center is already cropped.
	
	 public static String TAG = "FOURCORNERBOX";
	    LocalSaver saver;
	    LocalPrinter printer;

	    public FourCornerBoxv3(LocalSaver saver, LocalPrinter printer){
	        this.saver = saver;
	        this.printer = printer;
	    }

	    
	    public Mat extractPaper(Mat originalBitmap) {

	        //Timer t = Timer.getInstance();


	        Mat rgba = originalBitmap;

	        printer.print(TAG,"Oringal image type is"+ rgba.type());
	        
	        
	        //Rect rect = new Rect(vertex1,vertex2);

	        Mat crop = originalBitmap.clone();

	        
	        //t.start();
	        FourSquareCornerv2 normalizer = new FourSquareCornerv2();
	        Mat paper = normalizer.Normalize(crop,false);
	        //t.stop();
	        
	        //t.start();
	        //this.saver.saveImage("original",normalizer.beforeTouching);
	        this.saver.saveImage("marked",normalizer.marked);
	        this.saver.saveImage("Thresh",normalizer.thresholded);
	        this.saver.saveImage("normal",paper);
	        //t.stop();
	        
	        //output = ComputerVisionUtility.convertToBitmap(paper);
	        return paper;

	    }

}
