package cv_package.forms;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import cv_package.fields.Text;

public class FormMarks extends Form{


	    public FormMarks() {
	    	super();
			answers = new Answer[4];
	    	this.guideCount = 4;
	    	int[] guideMatch = {};
	    	this.setGuideMatch(guideMatch);
	    	this.setGroupCount(4);
	    	int[] fieldTypes = {2, 2, 2, 2};
	    	this.setGroupTypes(fieldTypes);
	    	this.totalContours = guideCount + getGroupCount();
	    	int[] elementCount = {6, 6, 6, 6};
	    	this.setElementCount(elementCount);
	    }


}
