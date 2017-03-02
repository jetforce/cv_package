package filesaving;

import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class FileSave {

    private static FileSave fs = new FileSave();
    public static FileSave getInstance() { return fs; }
    private FileSave() { }
    
    public void writeContours(String filename, List<MatOfPoint> contours, Mat subImage, Scalar color) {
    	Mat newMat = Mat.zeros(subImage.rows(), subImage.cols(), CvType.CV_8UC3);
    	
    	for(int i = 0; i < contours.size(); i++)
    		Imgproc.drawContours(newMat, contours, i, color);
    	
    	Imgcodecs.imwrite(filename, newMat);
    }
    
}
