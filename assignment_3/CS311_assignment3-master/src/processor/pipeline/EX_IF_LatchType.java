package processor.pipeline;

public class EX_IF_LatchType {

	boolean isBranchTaken;
	int branchPC;

	public EX_IF_LatchType()
	{
		isBranchTaken=false;
	}

	public int getBranchPC() {
		return this.branchPC;
	}

	public void setBranchPC(int branchPC) {
		this.branchPC = branchPC;
	}

	public boolean getIsBranchTaken() {
		return this.isBranchTaken;
	}

	public void setIsBranchTaken(boolean isBranchTaken) {
		this.isBranchTaken = isBranchTaken;
	}
}
