package cv_package.debug;

import java.util.ArrayList;

public class Timer {

	public static  Timer time = new Timer();
	long start;
	ArrayList<Long> times;
	
	public static Timer getInstance(){
		return time;
	}
	
	
	public Timer(){
		times = new ArrayList<>();

	}
	
	
	public void start(){
		start =  System.currentTimeMillis();
	}
	
	
	public void stop(){
		times.add(System.currentTimeMillis() - start);
	}
	
	
	public ArrayList<Long> getLaps(){
		return times;
	}
	
	
}
