package forms;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

public class Form {

	// Image
	protected Mat image;
	
	// Guides & Major Segments
	int guideCount;
	private int[] guideMatch;
	ArrayList<MatOfPoint> guideContours;
	ArrayList<MatOfPoint> groupContours;
	
	// Major Field Types
	private int groupCount;
	private int[] groupTypes;
	
	// Total First Layer External Contours
	int totalContours;
	
	// Element Count for each Major Field
	private int[] elementCount;
	
	private List<List<Object>> groups;

	public Mat getImage() {
		return image;
	}

	public void setImage(Mat image) {
		this.image = image;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int fieldCount) {
		this.groupCount = fieldCount;
	}

	public int[] getGroupTypes() {
		return groupTypes;
	}

	public void setGroupTypes(int[] fieldTypes) {
		this.groupTypes = fieldTypes;
	}

	public List<List<Object>> getGroups() {
		return groups;
	}

	public void setGroups() {
		this.groups = new ArrayList<>();
	}

	public void addGroup(List<Object> field) {
		this.groups.add(field);
	}

	public int[] getElementCount() {
		return elementCount;
	}

	public void setElementCount(int[] elementCount) {
		this.elementCount = elementCount;
	}

	public int[] getGuideMatch() {
		return guideMatch;
	}

	public void setGuideMatch(int[] guideMatch) {
		this.guideMatch = guideMatch;
	}
	
}
