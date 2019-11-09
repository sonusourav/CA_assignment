package processor.pipeline;

public class MA_RW_LatchType {

	static boolean RW_enable=false;
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

	public MA_RW_LatchType()
	{
		aluresult=0;
		loadresult=0;
		opcode=0;
		instruction=0;
	}

	public static boolean isRW_enable() {
		return RW_enable;
	}

	public static void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}


}
