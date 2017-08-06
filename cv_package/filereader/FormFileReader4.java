package cv_package.filereader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class FormFileReader4 {

	
	public double[][] getLocations() throws FileNotFoundException{
		
		Scanner text = new Scanner(new FileReader("input3/form1Loc.txt"));
		ArrayList<String> lines = linesplit(text, "\n");
		
		double[][] locations = new double[lines.size()][6];
		String fields[];
		double temp;
		
		for(int i=0; i < lines.size(); i++){
			fields = lines.get(i).split(",");
			for(int y=0; y < fields.length; y++){
				locations[i][y] = Double.parseDouble(fields[y]);
				System.out.println(Double.parseDouble(fields[y]));
			}
		}
		
		
		return locations;
		
	}
	
	
	public ArrayList<String> linesplit(Scanner text, String delimiter) {
		text.useDelimiter(delimiter);
		ArrayList<String> components = new ArrayList<>();
		
		while(text.hasNext()) {
			components.add(text.next().trim());
		}
		
//		print(components);
		
		return components;
	}
	
	
	
	
	
	
}
