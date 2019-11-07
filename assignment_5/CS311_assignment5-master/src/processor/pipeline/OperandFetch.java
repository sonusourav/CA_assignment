package processor.pipeline;

import generic.Simulator;
import processor.Processor;
import processor.Clock;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	static int counter = 0;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public void performOF() {

		int addEX = 0, addMA = 0, addRW = 0, inst = 0, rd = 0, addOP1 = 0, addOP2 = 0;
		int instruction = IF_OF_Latch.getInstruction();
		String insInBin = Integer.toBinaryString(instruction);
		insInBin = String.format("%32s", insInBin).replace(' ', '0');
		int opcode = Integer.parseInt(insInBin.substring(0, 5), 2);

		EX_MA_LatchType EX_MA_Latch = new EX_MA_LatchType();
		MA_RW_LatchType MA_RW_Latch = new MA_RW_LatchType();
		IF_EnableLatchType if_latch = new IF_EnableLatchType();
		IF_OF_LatchType if_of_latch = new IF_OF_LatchType();

		inst = OF_EX_Latch.getInstruction();
		rd = Integer.parseInt(String.format("%32s", insInBin).replace(' ', '0').substring(15, 20));
		addEX = rd;
		inst = EX_MA_Latch.getInstruction();
		rd = Integer.parseInt(String.format("%32s", insInBin).replace(' ', '0').substring(15, 20));
		addMA = rd;
		inst = MA_RW_Latch.getInstruction();
		rd = Integer.parseInt(String.format("%32s", insInBin).replace(' ', '0').substring(15, 20));
		addRW = rd;

		if (counter == 1) {
			counter++;
		} else if (counter == 2) {

			counter = 0;
		} else {

			if (opcode >= 0 && opcode < 22) {

				if (opcode % 2 == 0) {

					addOP1 = Integer.parseInt(insInBin.substring(5, 10), 2);
					addOP2 = Integer.parseInt(insInBin.substring(10, 15), 2);

					if (addOP1 == addEX || addOP1 == addMA || addOP1 == addRW || addOP2 == addEX || addOP2 == addMA
							|| addOP2 == addRW) {
						if_latch.setIF_enable(false);
						if_of_latch.setOF_enable(false);

					} else {

						if (addOP1 == 31 || addOP2 == 31) {
							if_latch.setIF_enable(false);
							if_of_latch.setOF_enable(false);
						} else {
							if_latch.setIF_enable(true);
							if_of_latch.setOF_enable(true);
						}

					}
				} else {
					addOP1 = Integer.parseInt(insInBin.substring(5, 10), 2);
					if (addOP1 == addEX || addOP1 == addMA || addOP1 == addRW) {
						if_latch.setIF_enable(false);
						if_of_latch.setOF_enable(false);
					} else {

						if ( (addOP1 == 31 || addOP2 == 31)) {
							if_latch.setIF_enable(false);
							if_of_latch.setOF_enable(false);
						} else {
							if_latch.setIF_enable(true);
							if_of_latch.setOF_enable(true);
						}
					}

				}
			} else if (opcode >= 25 && opcode <= 28) {
				addOP1 = Integer.parseInt(insInBin.substring(5, 10), 2);
				if (addOP1 == addEX || addOP1 == addMA || addOP1 == addRW || addOP1==31) {
					if_latch.setIF_enable(false);
					if_of_latch.setOF_enable(false);
				} else {
					if_latch.setIF_enable(true);
					if_of_latch.setOF_enable(true);

				}

			}
		}

		if (IF_OF_Latch.isOF_enable()) {
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);

			System.out.println("instruction at OF " + instruction + " Clock "+Clock.getCurrentTime());

			int immediate = 0, op1, op2;

			if (opcode >= 0 && opcode < 22) {

				if (opcode % 2 == 0) {

					immediate = 0;
					op1 = containingProcessor.getRegisterFile()
							.getValue(Integer.parseInt(insInBin.substring(5, 10), 2));
					op2 = containingProcessor.getRegisterFile()
							.getValue(Integer.parseInt(insInBin.substring(10, 15), 2));
					OF_EX_Latch.setOp1(op1);
					OF_EX_Latch.setOp2(op2);

				} else {

					op1 = containingProcessor.getRegisterFile()
							.getValue(Integer.parseInt(insInBin.substring(5, 10), 2));
					op2 = Integer.parseInt(insInBin.substring(10, 15), 2);
					immediate = ((instruction & 131071) << 15) >> 15;
					OF_EX_Latch.setIsImmediate(true);
					OF_EX_Latch.setOp1(op1);
					OF_EX_Latch.setOp2(immediate);

				}
			} else if (opcode == 22 || opcode == 23) {
				op1 = containingProcessor.getRegisterFile().getValue(Integer.parseInt(insInBin.substring(5, 10), 2));
				op2 = Integer.parseInt(insInBin.substring(10, 15), 2);
				immediate = ((instruction & 131071) << 15) >> 15;
				OF_EX_Latch.setIsImmediate(true);
				OF_EX_Latch.setImmediate(immediate);
				OF_EX_Latch.setOp1(op1);
				OF_EX_Latch.setOp2(op2);

			} else if (opcode == 24) {
				immediate = ((instruction & 4194303) << 10) >> 10;
				// System.out.println("imm =" + immediate);
				// System.out.println("pcforjump"+(containingProcessor.getRegisterFile().getProgramCounter()
				// + immediate -1 ));

				containingProcessor.getRegisterFile()
						.setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() + immediate - 1);

				if_latch.setIF_enable(false);
				if_of_latch.setOF_enable(false);
				counter++;
			} else if (opcode >= 25 && opcode < 29) {
				op1 = containingProcessor.getRegisterFile().getValue(Integer.parseInt(insInBin.substring(5, 10), 2));
				op2 = containingProcessor.getRegisterFile().getValue(Integer.parseInt(insInBin.substring(10, 15), 2));
				immediate = ((instruction & 131071) << 15) >> 15;
				OF_EX_Latch.setBranchTarget(containingProcessor.getRegisterFile().getProgramCounter() + immediate - 1);
				OF_EX_Latch.setOp1(op1);
				OF_EX_Latch.setOp2(op2);

				if_latch.setIF_enable(false);
				if_of_latch.setOF_enable(false);
				counter++;
			} else if (opcode == 29) {

				//Simulator.setSimulationComplete(true);
			}

			OF_EX_Latch.setInstruction(instruction);
			OF_EX_Latch.setOpcode(opcode);
		}
	}

}
