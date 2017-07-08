/*
package cv_package.testgen;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.opencv.core.Mat;

import cv_package.dumps.Time;


public class HandwrittenDigitClassifier {

	private static HandwrittenDigitClassifier hdc = new HandwrittenDigitClassifier();
    public static HandwrittenDigitClassifier getInstance() { return hdc; }
    private HandwrittenDigitClassifier() { }

	private MultiLayerNetwork model;
	private NativeImageLoader loader;
//	private String modelLoc = "trained_mnist_model.zip";
	private String modelLoc = "src/cv_package/_model/trained_mnist_model.zip";
	private List<Integer> labelList = Arrays.asList(0,1,2,3,4,5,6,7,8,9);
	
	
	public static void main(String args[]) throws Exception {
		HandwrittenDigitClassifier m = new HandwrittenDigitClassifier();
		m.init();
		m.test();
//		System.out.println("RESULT -> "+m.classify("0.jpg"));
	}

	public void setModelLocation(String loc) {
		modelLoc = loc;
	}
	
	public void test() throws IOException {
//		System.out.println(""+labelList);
		for(int i = 0; i < 10; i++) {
			System.out.println("RESULT -> "+classify("zero"+File.separator+i+".png"));
		}
	}
	
	public void init() throws IOException {
		int height = 28;
		int width = 28;
		int channels = 1;
		
		File locationToSave = new File(modelLoc);
		model = ModelSerializer.restoreMultiLayerNetwork(locationToSave);
		loader = new NativeImageLoader(height, width, channels);
	}

	public int classify(String filename) throws IOException {
		File file = new File(filename);
		INDArray image = loader.asMatrix(file);
		DataNormalization scaler = new ImagePreProcessingScaler(0,1);
		scaler.transform(image);
		INDArray output = model.output(image);
		int digitResult = getMax(output);
		return digitResult;
	}
	
	public int classify(Mat mat) throws IOException {
		org.bytedeco.javacpp.opencv_core.Mat javacvmat = getJavaCVMat(mat);
		INDArray image = loader.asMatrix(javacvmat);
		DataNormalization scaler = new ImagePreProcessingScaler(0,1);
		scaler.transform(image);
		INDArray output = model.output(image);
		int digitResult = getMax(output);
		return digitResult;
	}
	
	public org.bytedeco.javacpp.opencv_core.Mat getJavaCVMat(org.opencv.core.Mat opencvmat) {     
        int type = 0;
        if (opencvmat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (opencvmat.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(opencvmat.width() ,opencvmat.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        opencvmat.get(0, 0, data);
        
        OpenCVFrameConverter.ToMat cv = new OpenCVFrameConverter.ToMat();
        return cv.convertToMat(new Java2DFrameConverter().convert(image));
	}
	
	public int getMax(INDArray arr) {
		int maxIndex = 0;
		for(int i = 1; i < 10; i++) {
			if(arr.getDouble(i) > arr.getDouble(maxIndex)) {
				maxIndex = i;
			}
		}
//		System.out.println("\nmax actual ~ "+arr.maxNumber());
//		System.out.println("max func   ~ "+arr.getDouble(maxIndex));
//		System.out.println("max index  ~ "+maxIndex);
		return maxIndex;
	}
}
*/

