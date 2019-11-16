package processor;

public class Clock {
	static long currentTime = 0;
	static long latency=2;
	
	public static void incrementClock()
	{
		currentTime++;
	}
	
	public static long getCurrentTime()
	{
		return currentTime;
	}

	public static long getlatency(){
		return latency;
	}

	public static void setlatency(long latency1){
		latency=latency1;
	}
}
