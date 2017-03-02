package forms;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.MatOfPoint;

import fields.Text;

/**
 * Created by Hannah on 2/13/2017.
 */

public class FormPatientInfo extends Form {
	
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
    
    public FormPatientInfo() {
    	
    	this.guideCount = 6;
    	int[] guideMatch = {1, 2, 3, 4, 5, 5, 6};
    	this.setGuideMatch(guideMatch);
    	this.setGroupCount(7);
    	int[] fieldTypes = {1, 1, 3, 3, 2, 2, 3};
    	this.setGroupTypes(fieldTypes);
    	this.totalContours = guideCount + getGroupCount();
    	int[] elementCount = {3, 9, 1, 1, 8, 9, 1};
    	this.setElementCount(elementCount);

    	basicDetails = new ArrayList<>();
    	basicDetails.add(new Text("Health Center", 6));
    	basicDetails.add(new Text("Healthcare Personnel ID", 12));
    	basicDetails.add(new Text("Date", 6));

    	patientDetails = new ArrayList<>();
    	patientDetails.add(new Text("Patient Name", 25));
    	patientDetails.add(new Text("PhilHealth Number", 14));
    	patientDetails.add(new Text("Date of Birth", 6));
    	patientDetails.add(new Text("Gender", 1));
    	patientDetails.add(new Text("Marital Status", 1));
    	patientDetails.add(new Text("Blood Type", 3));
    	patientDetails.add(new Text("Address", 27));
    	patientDetails.add(new Text("Cellphone Number", 11));
    	patientDetails.add(new Text("Guardian Name", 25));
    	
    	this.setGroups();
    	this.addGroup(basicDetails);
    	this.addGroup(patientDetails);
    	
    }

}
