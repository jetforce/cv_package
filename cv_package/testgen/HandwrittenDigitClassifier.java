package cv_package.testgen;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

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
