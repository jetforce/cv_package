package fields;
import org.opencv.core.Mat;

public class Text {
	private int letterCount;
	Mat letterImage;
	String ocrText;
	String name;
	
	public Text(String name, int letterCount) {
		this.setLetterCount(letterCount);
		this.name = name;
	}

	public int getLetterCount() {
		return letterCount;
	}

	public void setLetterCount(int letterCount) {
		this.letterCount = letterCount;
	}
}
