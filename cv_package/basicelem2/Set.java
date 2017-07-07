package cv_package.basicelem2;

import java.util.ArrayList;

public class Set {

	public ArrayList<Form> forms;
	public int patientNumber;
	
	public Set() {
		forms = new ArrayList<>();
	}
	
	public Set(int patientNumber) {
		forms = new ArrayList<>();
		this.patientNumber = patientNumber;
	}
	
//	public void createForm() {
//		Form form = 
//		forms.add(form);
//	}
	
	public Form get(int index) {
		return forms.get(index);
	}
	
}
