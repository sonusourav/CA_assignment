package processor.pipeline;

public class IF_EnableLatchType {

	static boolean IF_enable;
	boolean IF_busy;

	public IF_EnableLatchType()
	{
		IF_enable = true;
	}

	public static boolean isIF_enable() {
		return IF_enable;
	}

	public static void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public void setIF_busy(boolean busy){
		IF_busy=busy;
	}

	public boolean isIF_busy(){
		return IF_busy;
	}
}
