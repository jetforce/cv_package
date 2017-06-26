package cv_package.paperextractorv2;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import cv_package.debug.LocalPrinter;
import cv_package.debug.LocalSaver;
import cv_package.segmentation.FourSquareCornerv2;

public class FourCornerBoxv4 {
	//Does not crop the image...
	//Uses the centrer as the basis of the cloest rectangles
	//Best version so far.
	
	
	
    public static String TAG = "FOURCORNERBOX";
    LocalSaver saver;
    LocalPrinter printer;

    public FourCornerBoxv4(LocalSaver saver, LocalPrinter printer){
        this.saver = saver;
        this.printer = printer;
    }

    

    public Mat extractPaper(Mat originalBitmap) {

        //Timer t = Timer.getInstance();


        Mat rgba = originalBitmap;

        printer.print(TAG,"Oringal image type is"+ rgba.type());
        int width = rgba.width();
        int height = rgba.height();
        double midWidth = width /2;
        double midHeight = height/2;
        double nWidth = width * 0.90;
        double nHeight = height *0.70;

        //Upper Left
        Point vertex1 = new Point(midWidth - (nWidth/2) , midHeight - (nHeight/2) );
        Point vertex2 =  new Point(midWidth + (nWidth/2) , midHeight + (nHeight/2) );
        Rect rect = new Rect(vertex1,vertex2);

        
   
        
        FourSquareCornerv2 normalizer = new FourSquareCornerv2();
        Mat paper = normalizer.Normalize(rgba,rect,false);
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

	
	
	
	

