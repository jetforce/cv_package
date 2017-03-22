package cv_package.debug;

import org.opencv.core.Mat;

/**
 * Created by Jet on 06/03/2017.
 */

public interface LocalSaver {

    public void saveImage(String filename, Mat m);
    public void saveImage(String folder, String filename, Mat m);


}
