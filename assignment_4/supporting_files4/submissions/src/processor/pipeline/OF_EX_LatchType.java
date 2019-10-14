package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	boolean isImmediate;
	int immediate,branchTarget,op1,op2,instruction,opcode;

	public int getImmediate() {
		return this.immediate;
	}

	public void setImmediate(int immediate) {
		this.immediate = immediate;
	}

	public int getBranchTarget() {
		return this.branchTarget;
	}

	public void setBranchTarget(int branchTarget) {
		this.branchTarget = branchTarget;
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

	public boolean getIsImmediate() {
		return this.isImmediate;
	}

	public void setIsImmediate(boolean isImmediate) {
		this.isImmediate = isImmediate;
	}
	
	public OF_EX_LatchType()
	{
		EX_enable = false;
		isImmediate = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

}
