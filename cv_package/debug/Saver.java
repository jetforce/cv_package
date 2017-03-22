package cv_package.debug;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Saver implements LocalSaver {

	@Override
	public void saveImage(String filename, Mat m) {
		Imgcodecs.imwrite(filename+".jpg", m);
		
	}

	@Override
	public void saveImage(String folder, String filename, Mat m) {
		
		File directory = new File(folder);
		if(!directory.exists()){
			directory.mkdirs();
		}
		
		String savedFile = folder+File.separator+filename+".jpg";
		System.out.println("Saving filename :"+ savedFile );
		Imgcodecs.imwrite(savedFile, m);
	}

	
	
	
}
