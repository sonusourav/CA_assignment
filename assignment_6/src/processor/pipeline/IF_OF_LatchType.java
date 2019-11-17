package processor.pipeline;

public class IF_OF_LatchType {

	static boolean OF_enable=false;
	int instruction;
	static boolean OF_bust = false;

	public IF_OF_LatchType()
	{	}

	public static boolean isOF_enable() {
		return OF_enable;
	}

	public static void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
	public static boolean isOF_busy(){
		return OF_bust;
	}
	public static void setOF_busy(boolean value){
		OF_bust = value;
	}

}
