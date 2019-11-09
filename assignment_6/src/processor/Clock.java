package processor;

public class Clock {
	static long currentTime = 0;
	static long cacheSize=32;
	
	public static void incrementClock()
	{
		currentTime++;
	}
	
	public static long getCurrentTime()
	{
		return currentTime;
	}

	public static long getCacheSize(){
		return cacheSize;
	}

	public static void setCacheSize(long cacheSize1){
		cacheSize=cacheSize1;
	}
}
