package cv_package.helpers;

import org.opencv.core.Mat;

public class MarkRecogTools {

	
	public static MarkRecogTools mrt = new MarkRecogTools();
	public static MarkRecogTools getInstance(){ return mrt;}
	 
	public int countWhites(Mat box){
		
	int count=0;
		
		for(int i=0; i< box.width(); i++){
			for(int j=0;j<box.height(); j++){
				if((int) box.get(j, i)[0] == 255){
					count++;
				}
			}
		}
		
		return count;

	}
	
		
}
