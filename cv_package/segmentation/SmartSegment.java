package cv_package.segmentation;


import android.content.Context;

import com.virtusio.sibayan.thesis_project.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import cv_package.basicelem2.Form;
import cv_package.filereader.FormFileReader4;
import cv_package.helpers.ComputerVision;
import cv_package.helpers.Filtering;

public class SmartSegment {

	//This segmentation uses eucledian distance as a basis for relevant components
	//it assumes that a perfect input exists and will use it as a basis of what is correct.

    public ArrayList<MatOfPoint> getNearestContours(Mat form, int number, Context context){
        ComputerVision cv = ComputerVision.getInstance();

        FormFileReader4 fr = new FormFileReader4();

        InputStream is = null;
        switch(number){
            case 1: is = context.getResources().openRawResource(R.raw.form1loc); break;
            case 2: is = context.getResources().openRawResource(R.raw.form2loc); break;
            case 3: is = context.getResources().openRawResource(R.raw.form3loc); break;
            case 4: is = context.getResources().openRawResource(R.raw.form4loc); break;
        }


        Rect temp;
        ArrayList<MatOfPoint> conts2 = new ArrayList<>();
        ArrayList<MatOfPoint> theNearest = new ArrayList<>();
        int nearestIndex = 0;
        double nearestDistance;
        try {

            double[][] locations = fr.getLocations(is);
            ArrayList<MatOfPoint> conts = cv.findContours(form, Imgproc.RETR_EXTERNAL);



            for(MatOfPoint mp:conts){
                if(Imgproc.contourArea(mp)>500){
                    conts2.add(mp);
                }
            }



            System.out.println("Number of Conts "+conts.size() );
            System.out.println("Number of Conts Filtered"+conts2.size() );



            Point mid,rmid,tl,br;
            double distance;
            Rect rBox;



            for(int i=0; i< locations.length; i++){

                tl = new Point(locations[i][0]* form.cols(),  locations[i][1]* form.rows());
                br = new Point(locations[i][2]* form.cols(),  locations[i][3]* form.rows());
                mid = new Point(locations[i][4]* form.cols(),  locations[i][5]* form.rows());
                rBox = Imgproc.boundingRect(conts2.get(0));
                rmid = new Point(rBox.x + rBox.width/2, rBox.y + rBox.height/2 );
                nearestDistance = computeDistance(tl,rBox.tl()) + computeDistance(br,rBox.br());



                for(int y=1;y< conts2.size(); y++){
                    rBox = Imgproc.boundingRect(conts2.get(y));

                   //rmid = new Point(rBox.x + rBox.width/2, rBox.y + rBox.height/2 );
                    distance = computeDistance(tl,rBox.tl()) + computeDistance(br,rBox.br());

                    if(nearestDistance>distance){
                        nearestDistance = distance;
                        nearestIndex = y;
                    }

                }
                theNearest.add(conts2.get(nearestIndex));
            }



        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return theNearest;

    }


	
	public ArrayList<MatOfPoint> getNearestContours(Mat form, int number){
		ComputerVision cv = ComputerVision.getInstance();
		Filtering filer = Filtering.getInstance();
		FormFileReader4 fr = new FormFileReader4();
		Rect temp;
		ArrayList<MatOfPoint> conts2 = new ArrayList<>();
		ArrayList<MatOfPoint> theNearest = new ArrayList<>();
		int nearestIndex = 0;
		double nearestDistance;


		try {
		
			double[][] locations = fr.getLocations();
			ArrayList<MatOfPoint> conts = cv.findContours(form, Imgproc.RETR_EXTERNAL);
			
				
			
			for(MatOfPoint mp:conts){
				if(Imgproc.contourArea(mp)>500){
					conts2.add(mp);
				}
			}
			
			
		
			System.out.println("Number of Conts "+conts.size() );
			System.out.println("Number of Conts Filtered"+conts2.size() );
			

	
			Point mid,rmid;
			
			double distance;
			Rect rBox;
			

					
			for(int i=0; i< locations.length; i++){ 
				
				//tl = new Point(locations[i][0]* form.cols(),  locations[i][1]* form.rows());
				//br = new Point(locations[i][2]* form.cols(),  locations[i][3]* form.rows());
				mid = new Point(locations[i][4]* form.cols(),  locations[i][5]* form.rows());
				rBox = Imgproc.boundingRect(conts2.get(0));
				rmid = new Point(rBox.x + rBox.width/2, rBox.y + rBox.height/2 );
				nearestDistance = computeDistance(mid,rmid);
				for(int y=1;y< conts2.size(); y++){
					rBox = Imgproc.boundingRect(conts2.get(y));
					
					rmid = new Point(rBox.x + rBox.width/2, rBox.y + rBox.height/2 );
					distance = computeDistance(rmid,mid);
					
					if(nearestDistance>distance){
						nearestDistance = distance;
						nearestIndex = y;
					}
					
				}
				theNearest.add(conts2.get(nearestIndex));
			}
			


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return theNearest;

	}
	
	
	
	public void printFormFeatures(List<MatOfPoint> contours){
		Rect temp; 
		for(int i=0; i< contours.size(); i++){
			temp = Imgproc.boundingRect(contours.get(i));
			System.out.println("Rect features x:"+temp.x +" y:"+temp.y+" width"+ temp.width+ " height "+temp.height);
		}
	}
	
	
	
	
    public double computeDistance(Point p1, Point p2){
        return Math.sqrt( Math.pow(p1.x - p2.x,2)  +  Math.pow(p1.y - p2.y,2) );
    }
    
	
	
	
	
	
}
