package processor.pipeline;

public class IF_OF_LatchType {
<<<<<<< HEAD

	static boolean OF_enable=false;
	int instruction;

	public IF_OF_LatchType()
	{	}

	public static boolean isOF_enable() {
		return OF_enable;
	}

	public static void setOF_enable(boolean oF_enable) {
=======
	
	boolean OF_enable;
	int instruction;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
>>>>>>> final version
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

}
