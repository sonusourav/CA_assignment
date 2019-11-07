package processor.pipeline;

public class IF_EnableLatchType {
<<<<<<< HEAD

	static boolean IF_enable;

=======
	
	boolean IF_enable;
	
>>>>>>> final version
	public IF_EnableLatchType()
	{
		IF_enable = true;
	}

<<<<<<< HEAD
	public static boolean isIF_enable() {
		return IF_enable;
	}

	public static void setIF_enable(boolean iF_enable) {
=======
	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
>>>>>>> final version
		IF_enable = iF_enable;
	}

}
