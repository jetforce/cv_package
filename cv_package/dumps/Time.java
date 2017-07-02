package cv_package.dumps;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Time {

	private static Time time = new Time();
    public static Time getInstance() { return time; }
    private Time() { }

    private String path, filename = "Time.txt";
	private SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss.SSS");
	private Timestamp start, before, mark;
	private ArrayList<String> times = new ArrayList<>();
    
	public void setPath(String path) {
		this.path = path;
	}
	
    public void start() {
    	before = new Timestamp(System.currentTimeMillis());
    	mark = before;
    	start = before;
    	String currTimeStr = sdf.format(before);
    	times.add(currTimeStr + "  Start");
//     	saveTime(str);
    }
    
    public void end() {
    	stamp("End");
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	times.add("Total Time in mil: " + getDiffMilli(start, now));
    	times.add("Total Time in sec: " + getDiffSec(start, now));
    	saveTime();
    }
    
    public String stamp(String label) {
    	Timestamp now = new Timestamp(System.currentTimeMillis());
     	String currTimeStr = sdf.format(now);
     	String diff = getDiffMilli(before, now)+"";
     	String str = currTimeStr + "  " + label + " (" + diff + "mil)";
     	times.add(str);
     	System.out.println(str);
//     	saveTime(str);
     	before = now;
     	return diff;
    }
    
    public void stampMark(String label) {
    	Timestamp now = new Timestamp(System.currentTimeMillis());
     	String currTimeStr = sdf.format(now);
     	String str = currTimeStr + "  " + label + " (" + getDiffMilli(before, now) + "mil)";
     	times.add(str);
     	System.out.println(str);
//     	saveTime(str);
     	before = now;
     	
 		String milestone = "********* " + label + " (" + getDiffMilli(mark, now) + "mil) " + "(" + getDiffSec(mark, now) + "sec) ";
 		times.add(milestone);
     	System.out.println(milestone);
// 		saveTime(milestone);
 		mark = now;
    }
    
    private long getDiffMilli(Timestamp before, Timestamp now) {
    	long milliseconds1 = before.getTime();
    	long milliseconds2 = now.getTime();
    	long diffMilli = milliseconds2 - milliseconds1;
    	return diffMilli;
    }
    
    private long getDiffSec(Timestamp before, Timestamp now) {
    	long milliseconds1 = before.getTime();
    	long milliseconds2 = now.getTime();
    	long diffMilli = milliseconds2 - milliseconds1;
    	long diffSeconds = diffMilli / 1000;
    	return diffSeconds;
    }
    
    public void createFile() {
    	try{
    	    PrintWriter writer = new PrintWriter(path+File.separator+filename, "UTF-8");
    	    writer.close();
    	} catch (IOException e) {
    	   // do something
    		e.printStackTrace();
    	}
    }
    
    public void saveTime() {
    	try{
    	    PrintWriter writer = new PrintWriter(path+File.separator+filename, "UTF-8");
    	    for(String t:times) {
        	    writer.println(t);
    	    }
    	    writer.close();
    	} catch (IOException e) {
    	   // do something
    		e.printStackTrace();
    	}
    }
    
}
