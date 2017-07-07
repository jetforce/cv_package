package cv_package.filereader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cv_package.basicelem2.Blob;
import cv_package.basicelem2.Form;
import cv_package.basicelem2.Mark;
import cv_package.basicelem2.Text;
import cv_package.basicelem2.Type;

public class FormFileReader3 {

	private String NEWLINE = "\n";
	private String UNDERSCORE = "_";
	private String SPACE = " ";
	
	public void readToForm(String filename, Form form) {
		try {
			
			Scanner text = new Scanner(new FileReader(filename));
			
			ArrayList<String> lines = linesplit(text, NEWLINE);
			
			ArrayList<Type> components = getComponents(lines);
			
			form.components = components;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public int getFormNumber(ArrayList<String> lines) {
//		String numberStr = lines.get(0);
//		int numberInt = Integer.parseInt(numberStr);
//		lines.remove(0);
//		return numberInt;
//	}
	
	public ArrayList<Type> getComponents(ArrayList<String> lines) {
		ArrayList<Type> components = new ArrayList<>();
		
		for(String l:lines) {
			System.out.println("-");
			ArrayList<String> words = wordsplit(l, SPACE);
			
			switch(words.get(0)) {
			
			case "TEXT": 
				Text t = readTypeText(words);
				components.add(t);
				
//				System.out.println("TEXT");
//				System.out.println("  count: " + t.characterCount);
//				System.out.println("  type: " + t.type);
//				System.out.println("  label: " + t.label);
				break;
				
			case "MARK": 
				Mark m = readTypeMark(words);
				components.add(m);
				
//				System.out.println("MARK");
//				System.out.println("  label: " + m.label);
//				for(String s:m.labels) System.out.println("  labels: " + s);
				break;
				
			case "BLOB":
				Blob b = readTypeBlob(words); 
				components.add(b);
				
//				System.out.println("BLOB");
//				System.out.println("  label: " + b.label);
				break;
				
			default: 
				System.out.println("Error first word");
				
			}
		}
		
		return components;
	}

	public Blob readTypeBlob(ArrayList<String> words) {
		Blob blob = new Blob();
		String label;
		label = words.get(1);
		label = cleanlabel(label);
		blob.label = label;
		return blob;
	}
	
	public Mark readTypeMark(ArrayList<String> words) {
		Mark mark = new Mark();
		String label;
		label = words.get(1);
		label = cleanlabel(label);
		mark.label = label;
		
		int size = words.size();
		if(size > 2) {
			mark.labels = new ArrayList<String>(words.subList(2, size));
		}
		
		return mark;
	}
	
	public Text readTypeText(ArrayList<String> words) {
		
		Text text = new Text();
		String sCount, type, label;
		
		sCount = words.get(1);
		type = words.get(2);
		label = words.get(3);
		
		// character count
		int count = Integer.parseInt(sCount);	
		// type
		if(!(type.equals("ALPHA") || type.equals("NUM") || type.equals("ALPHANUM")))
			System.out.println("Error wrong type");
		// label
		label = cleanlabel(label);
		
		text.characterCount = count;
		text.type = type;
		text.label = label;
		
		return text;
		
	}
	
	public String cleanlabel(String label) {
		return label.replaceAll(UNDERSCORE, SPACE);
	}
	
	public ArrayList<String> wordsplit(String text, String delimiter) {
		text = text.trim().replaceAll(" +", " ");
		String[] split1 = text.split(delimiter);
		ArrayList<String> split2 = new ArrayList<String>(Arrays.asList(split1));
		for(int i = 0; i < split2.size(); i++) {
			split2.set(i, split2.get(i).toUpperCase());
		}
		return split2;
	}
	
	public ArrayList<String> linesplit(Scanner text, String delimiter) {
		text.useDelimiter(NEWLINE);
		ArrayList<String> components = new ArrayList<>();
		
		while(text.hasNext()) {
			components.add(text.next().trim());
		}
		
//		print(components);
		
		return components;
	}
	
	public void print(List<String> x) {
		for(String a:x) {
			System.out.println("> " + a);
		}
	}
	
}
