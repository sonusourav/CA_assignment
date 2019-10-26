package processor.pipeline;

public class EX_MA_LatchType {

	static boolean MA_enable=false;
	int aluresult;
	int op1,op2,opcode,instruction,immediate;
	boolean MA_busy;

	public int getImmediate() {
		return this.immediate;
	}

	public void setImmediate(int immediate) {
		this.immediate = immediate;
	}

	public int getOp1() {
		return this.op1;
	}

	public void setOp1(int op1) {
		this.op1 = op1;
	}

	public int getOp2() {
		return this.op2;
	}

	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public int getAluresult() {
		return this.aluresult;
	}

	public void setAluresult(int aluresult) {
		this.aluresult = aluresult;
	}

	public  int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public EX_MA_LatchType(boolean MA_enable, int aluresult, int op1, int op2, int opcode, int instruction,int immediate) {
		this.MA_enable = MA_enable;
		this.aluresult = aluresult;
		this.op1 = op1;
		this.op2 = op2;
		this.opcode = opcode;
		this.instruction = instruction;
		this.immediate=immediate;
	}

	public EX_MA_LatchType() {

	}

	public int getOpcode() {
		return this.opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public static boolean isMA_enable() {
		return MA_enable;
	}

	public static void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}
	public void setMA_busy(boolean busy){
		MA_busy=busy;
	}

	public boolean isMA_busy(){
		return MA_busy;
	}

}
