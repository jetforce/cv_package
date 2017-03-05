package cv_package.forms;

import java.util.ArrayList;
import java.util.List;
import cv_package.fields.Text;

public class FormMarks extends Form{

	
	
	 // Basic Details
		List<Object> basicDetails;

	    // Patient Details
		List<Object> patientDetails;
	    String patientName;
	    int philhealthNo;
	    int birthDate;
	    char gender;
	    char maritalStatus;
	    String bloodType;
	    String address;
	    int cellphoneNo;
	    String guardianName;

	    // Surgeries & Hospitalizations
	    String surgeries;
	    String hospitalizations;

	    // Childhood Illnesses
	    boolean[] childhoodIllnesses;
	    String otherchildhoodIllnesses;

	    // Present Illnesses
	    boolean[] presentIllnesses;
	    String otherPresentIllnesses;
	    String maintenanceMed;
	    
	    public FormMarks() {
	    	super();
			answers = new Answer[4];
	    	this.guideCount = 0;
	    	int[] guideMatch = {};
	    	this.setGuideMatch(guideMatch);
	    	this.setGroupCount(4);
	    	int[] fieldTypes = {2, 2, 2, 2};
	    	this.setGroupTypes(fieldTypes);
	    	this.totalContours = guideCount + getGroupCount();
	    	int[] elementCount = {6, 6, 6, 6};
	    	this.setElementCount(elementCount);
	    	this.setGroups();
	    	this.addGroup(basicDetails);
	    	this.addGroup(patientDetails);	    	
	    }


	
	
	
}
