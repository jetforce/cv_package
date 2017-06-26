package cv_package.filereader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cv_package.basicelem.Form;
import cv_package.basicelem.Group;
import cv_package.basicelem.Input;

public class FormFileReader {

	private String GROUP_SPLITTER = "-";
	private String NEWLINE = "\n";
	private String COMMA = ",";
	
	// TEXT FILE to FORM
	public void readToForm(String filename, Form form) {
		try {
			
			Scanner text = new Scanner(new FileReader(filename + ".txt"));
			
			// NAME
			if(text.hasNextLine()) {
			    form.name = text.nextLine();
			}
			
			// GROUPS
			text.useDelimiter(GROUP_SPLITTER);
			List<String> gStr = new ArrayList<>();
			
			while(text.hasNext()) {
				gStr.add(text.next().trim());
			}
			text.close();
			
			// Get lines
			List<Group> groupList = new ArrayList<>();
			String tempStr;
			int in1 = 0, in2, in3, cut;
			String st1, st2, st3 = "";
			for(int i = 0; i < gStr.size(); i++) {
				Group group = new Group();
				
				tempStr = gStr.get(i);
				
				// 3 LINES
				cut = tempStr.indexOf(NEWLINE) + 1;
				st1 = tempStr.substring(0, cut).trim();
				tempStr = tempStr.substring(cut);
				
				cut = tempStr.indexOf(NEWLINE) + 1;
				st2 = tempStr.substring(0, cut).trim();
				tempStr = tempStr.substring(cut);
				
				st3 = tempStr.trim();

//				System.out.println("\ncut1 - " + st1);
//				System.out.println("cut2 - " + st2);
//				System.out.println("cut3 - " + st3);

				// 1ST LINE
				
				group.type = st1;
				
				// 2ND & 3RD LINE

				String[] aName = st2.split(COMMA);
				String[] aCount = st3.split(COMMA);
				
				List<Input> inputList = new ArrayList<>();
				int len = aName.length;
				String tName;
				int tCount;
				for(int j = 0; j < len; j++) {
					tName = aName[j].trim();
					tCount = Integer.parseInt(aCount[j].trim());
					inputList.add(new Input(tName, tCount));
				}
				
				group.inputList = inputList;
				
				groupList.add(group);
			}
			
			form.groupList = groupList;
//			
//
//			System.out.println("FORM NAME: " + form.name);
////			
//			for(int i = 0; i < gStr.size(); i++) {
//				System.out.println("GROUP#"+i+":\n"+gStr.get(i));
//			}

			
//			List<Group> groupList = new ArrayList<>();
//			while(txt.hasNextLine()) {
//				if(startOfGroup(txt)) {
//					groupList.add(getGroup(txt));
//				}
//				// else skip empty line
//			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
