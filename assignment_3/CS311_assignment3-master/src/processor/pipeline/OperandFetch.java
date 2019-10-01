package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public void performOF() {
		if (IF_OF_Latch.isOF_enable()) {
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);

			int instruction = IF_OF_Latch.getInstruction();
			String insInBin = Integer.toString(instruction);

			int opcode = Integer.parseInt(insInBin.substring(0, 5));
			int immediate = 0, branchTarget = 0, op1, op2;

			System.out.println("opcode value =" + opcode);

			if (opcode >= 0 && opcode < 22) {

				if (opcode % 2 == 0) {

					branchTarget = 0;
					immediate = 0;
					op1 = containingProcessor.getRegisterFile().getValue(Integer.parseInt(insInBin.substring(5, 10)));
					op2 = containingProcessor.getRegisterFile().getValue(Integer.parseInt(insInBin.substring(10, 15)));
					OF_EX_Latch.setOp1(op1);
					OF_EX_Latch.setOp2(op2);
				} else {

					op1 = containingProcessor.getRegisterFile().getValue(Integer.parseInt(insInBin.substring(5, 10)));
					op2 = Integer.parseInt(insInBin.substring(10, 15));
					immediate = ((instruction & 131071) << 15) >> 15;
					OF_EX_Latch.setIsImmediate(true);
					OF_EX_Latch.setOp1(op1);
					OF_EX_Latch.setOp2(immediate);
				}
			} else if (opcode == 22 && opcode == 23) {
				op1 = containingProcessor.getRegisterFile().getValue(Integer.parseInt(insInBin.substring(5, 10)));
				op2 = Integer.parseInt(insInBin.substring(10, 15));
				immediate = ((instruction & 131071) << 15) >> 15;
				OF_EX_Latch.setIsImmediate(true);
				OF_EX_Latch.setOp1(op1);
				OF_EX_Latch.setOp2(op2);

			} else if (opcode == 24) {
				immediate = ((instruction & 131071) << 10) >> 10;
				containingProcessor.getRegisterFile()
						.setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() + immediate - 1);
				OF_EX_Latch.setBranchTarget(branchTarget);

			} else if (opcode >= 25 && opcode < 29) {
				op1 = containingProcessor.getRegisterFile().getValue(Integer.parseInt(insInBin.substring(5, 10)));
				op2 = Integer.parseInt(insInBin.substring(10, 15));
				immediate = ((instruction & 131071) << 15) >> 15;
				OF_EX_Latch.setBranchTarget(containingProcessor.getRegisterFile().getProgramCounter() + immediate - 1);
				OF_EX_Latch.setOp1(op1);
				OF_EX_Latch.setOp2(op2);
			} else if (opcode == 29) {

				Simulator.setSimulationComplete(true);
			}

			OF_EX_Latch.setInstruction(instruction);
			OF_EX_Latch.setOpcode(opcode);
		}
	}

}
