package processor.pipeline;

public class MA_RW_LatchType {
<<<<<<< HEAD

	static boolean RW_enable=false;
=======
	
	boolean RW_enable;
>>>>>>> final version
	int aluresult,loadresult,instruction,opcode;

	public int getAluresult() {
		return this.aluresult;
	}

	public void setAluresult(int aluresult) {
		this.aluresult = aluresult;
	}

	public int getLoadresult() {
		return this.loadresult;
	}

	public void setLoadresult(int loadresult) {
		this.loadresult = loadresult;
	}

	public int getInstruction() {
		return this.instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public int getOpcode() {
		return this.opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}
<<<<<<< HEAD

	public MA_RW_LatchType()
	{
=======
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
>>>>>>> final version
		aluresult=0;
		loadresult=0;
		opcode=0;
		instruction=0;
	}

<<<<<<< HEAD
	public static boolean isRW_enable() {
		return RW_enable;
	}

	public static void setRW_enable(boolean rW_enable) {
=======
	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
>>>>>>> final version
		RW_enable = rW_enable;
	}


}
