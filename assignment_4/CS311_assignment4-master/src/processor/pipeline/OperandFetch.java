package processor.pipeline;

import generic.Simulator;
import processor.Processor;
import processor.Clock;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	static int counter = 0;
	int n = 0;
	int njump = 0;
	int previousPC = 0;
	int nend = 0;
	int data = 0;
	int control = 0;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public void performOF() {

		int addEX = 100, addMA = 100, addRW = 100, inst = 0, addOP1 = 0, addOP2 = 0;
		int instruction = IF_OF_Latch.getInstruction();
		String insInBin = Integer.toBinaryString(instruction);
		insInBin = String.format("%32s", insInBin).replace(' ', '0');
		int opcode = Integer.parseInt(insInBin.substring(0, 5), 2);

		EX_MA_LatchType EX_MA_Latch = new EX_MA_LatchType();
		MA_RW_LatchType MA_RW_Latch = new MA_RW_LatchType();
		IF_EnableLatchType if_latch = new IF_EnableLatchType();
		IF_OF_LatchType if_of_latch = new IF_OF_LatchType();

		if (OF_EX_Latch.getInstruction() != 0) {
			addEX = getRD(OF_EX_Latch.getInstruction());
			// System.out.println("INSTRUCTION EX"+OF_EX_Latch.getInstruction());
		}
		if (containingProcessor.getEXUnit().EX_MA_Latch.getInstruction() != 0) {
			addMA = getRD(containingProcessor.getEXUnit().EX_MA_Latch.getInstruction());
			// System.out.println("INSTRUCTION
			// MA"+containingProcessor.getEXUnit().EX_MA_Latch.getInstruction());

		}
		if (containingProcessor.getMAUnit().MA_RW_Latch.getInstruction() != 0) {
			addRW = getRD(containingProcessor.getMAUnit().MA_RW_Latch.getInstruction());
			// System.out.println("INSTRUCTION RW"+MA_RW_Latch.getInstruction());
		}

		if (counter == 1) {
			System.out.println("counter" + counter);
			counter++;
			IF_EnableLatchType.setIF_enable(false);
			IF_OF_LatchType.setOF_enable(false);
			System.out.println("control " + control);
			control++;
		} else if (counter == 2) {
			// System.out.println("counter"+counter);
			counter = 0;
			int ProgramCounter = containingProcessor.getRegisterFile().getProgramCounter();
			// System.out.println("ProgramCounter is " + ProgramCounter);
			// System.out.println("Previous Counter is "+ previousPC);

			if (previousPC + 1 == ProgramCounter && opcode != 24) {
				IF_EnableLatchType.setIF_enable(true);
				IF_OF_LatchType.setOF_enable(true);
			} else {
				IF_EnableLatchType.setIF_enable(true);
				IF_OF_LatchType.setOF_enable(true);
				containingProcessor.getOFUnit().IF_OF_Latch.setInstruction(0);
				IF_OF_Latch.setInstruction(0);
				// System.out.println("ITS A JUMP");

			}

		}

		else {

			if (opcode >= 0 && opcode < 22) {

				if (opcode % 2 == 0) {

					//for r3 type
					addOP1 = Integer.parseInt(insInBin.substring(5, 10), 2);
					addOP2 = Integer.parseInt(insInBin.substring(10, 15), 2);

					if (((addOP1 == addEX) || (addOP1 == addMA) || (addOP1 == addRW) || (addOP2 == addEX)
							|| (addOP2 == addMA) || (addOP2 == addRW))
							&& (addEX != 100 || addMA != 100 || addRW != 100)) {

						IF_EnableLatchType.setIF_enable(false);
						IF_OF_LatchType.setOF_enable(false);
						// System.out.println("its false1 and opcode" +opcode);
						data++;
						System.out.println("data= " + data);
					} else {

						if (addOP1 == 31 || addOP2 == 31) {
							IF_EnableLatchType.setIF_enable(false);
							IF_OF_LatchType.setOF_enable(false);
							// System.out.println("its false2 and opcode " + opcode);
							data++;
							System.out.println("data= " + data);
						} else {
							if (instruction != 0) {
								IF_EnableLatchType.setIF_enable(true);
								// System.out.println("instruction is not 0");
								IF_OF_LatchType.setOF_enable(true);
							} else {
								// System.out.println("instruction is 0");
								IF_EnableLatchType.setIF_enable(true);
								IF_OF_LatchType.setOF_enable(false);
							}
						}

					}
				} else {

					//for R2 type
					addOP1 = Integer.parseInt(insInBin.substring(5, 10), 2);

					if ((addOP1 == addEX && addEX != 100) || (addOP1 == addMA && addMA != 100)
							|| (addOP1 == addRW && addRW != 100) || (addOP2 == addEX && addEX != 100)
							|| (addOP2 == addMA && addMA != 100) || (addOP2 == addRW && addRW != 100) || addOP1 == 31
							|| addOP2 == 31) {
						IF_EnableLatchType.setIF_enable(false);
						IF_OF_LatchType.setOF_enable(false);
						// System.out.println("its false3 and opcode" + opcode);
						data++;
						System.out.println("data= " + data);
					} else {

						if ((addOP1 == 31 || addOP2 == 31)) {
							IF_EnableLatchType.setIF_enable(false);
							IF_OF_LatchType.setOF_enable(false);
							System.out.println("its false4 and opcode " + opcode);
							data++;
							System.out.println("data= " + data);
						} else {

							IF_EnableLatchType.setIF_enable(true);
							IF_OF_LatchType.setOF_enable(true);

						}
					}

				}
			} else if ((opcode >= 25 && opcode <= 28) || opcode == 22 || opcode == 23) {
				addOP1 = Integer.parseInt(insInBin.substring(5, 10), 2);
				addOP2 = Integer.parseInt(insInBin.substring(10, 15), 2);

				// System.out.println("Operand 1 " + addOP1 + " Operand 2 " + addOP2);
				if ((addOP1 == addEX || addOP1 == addMA || addOP1 == addRW || addOP1 == 31) && (addOP1 != 31)) {
					IF_EnableLatchType.setIF_enable(false);
					IF_OF_LatchType.setOF_enable(false);
					// System.out.println("its false5 and opcode"+opcode);
					data++;
					System.out.println("data= " + data);
				} else if ((addOP2 == addEX || addOP2 == addMA || addOP2 == addRW || addOP2 == 31) && addOP2 != 31) {
					IF_EnableLatchType.setIF_enable(false);
					IF_OF_LatchType.setOF_enable(false);
					data++;
					System.out.println("data= " + data);
				} else {
					IF_EnableLatchType.setIF_enable(true);
					IF_OF_LatchType.setOF_enable(true);

				}

			}
			// System.out.println("Instruction in OFLATCH " + OF_EX_Latch.getInstruction());
			// System.out.println("Instruction in MALATCH " +
			// containingProcessor.getEXUnit().EX_MA_Latch.getInstruction());
			// System.out.println("Instruction in RWLATCH" +
			// containingProcessor.getMAUnit().MA_RW_Latch.getInstruction());

			if (OF_EX_Latch.getInstruction() == containingProcessor.getEXUnit().EX_MA_Latch.getInstruction()
					&& containingProcessor.getEXUnit().EX_MA_Latch
							.getInstruction() == containingProcessor.getMAUnit().MA_RW_Latch.getInstruction()
					&& containingProcessor.getMAUnit().MA_RW_Latch.getInstruction() != 0) {

				if (n == 1) {
					IF_EnableLatchType.setIF_enable(true);
					IF_OF_LatchType.setOF_enable(true);
					n = 0;
					data--;
					if(data<0){
						data=0;
					}
					System.out.println("data= " + data);
				} else {
					n++;
					IF_EnableLatchType.setIF_enable(false);
					IF_OF_LatchType.setOF_enable(false);
				}
			}

		}
		if (opcode == 29) {

			boolean branchTaken = containingProcessor.getEXUnit().EX_IF_Latch.getIsBranchTaken();
			containingProcessor.getRegisterFile()
					.setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() - 1);
			// System.out.println("IS THE BRANCH TAKEN" + branchTaken);
			if (nend == 0) {
				nend = 0;
				if (branchTaken == true) {

					containingProcessor.getOFUnit().IF_OF_Latch.setInstruction(0);
					IF_OF_Latch.setInstruction(0);

				}
				if (branchTaken == false) {
					IF_EnableLatchType.setIF_enable(true);
					IF_OF_LatchType.setOF_enable(true);

				}
				// System.out.println("Its partial");
			}

		}

		if (IF_OF_LatchType.isOF_enable()) {
			OF_EX_LatchType.setEX_enable(true);
			instruction = IF_OF_Latch.getInstruction();
			insInBin = Integer.toBinaryString(instruction);
			insInBin = String.format("%32s", insInBin).replace(' ', '0');
			opcode = Integer.parseInt(insInBin.substring(0, 5), 2);
			System.out.println("instruction at OF " + Integer.toBinaryString(instruction)
			 + " Clock "+Clock.getCurrentTime());

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
				previousPC = containingProcessor.getRegisterFile().getProgramCounter();
				// System.out.println("imm =" + immediate);
				// System.out.println("pcforjump"+(containingProcessor.getRegisterFile().getProgramCounter()
				// + immediate -1 ));

				containingProcessor.getRegisterFile()
						.setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter() + immediate - 2);

				IF_EnableLatchType.setIF_enable(true);
				IF_OF_LatchType.setOF_enable(false);
				counter++;
				System.out.println("control " + control);
				control++;
			} else if (opcode >= 25 && opcode < 29) {
				op1 = containingProcessor.getRegisterFile().getValue(Integer.parseInt(insInBin.substring(5, 10), 2));
				op2 = containingProcessor.getRegisterFile().getValue(Integer.parseInt(insInBin.substring(10, 15), 2));
				previousPC = containingProcessor.getRegisterFile().getProgramCounter();

				immediate = ((instruction & 131071) << 15) >> 15;
				OF_EX_Latch.setBranchTarget(containingProcessor.getRegisterFile().getProgramCounter() + immediate - 1);
				OF_EX_Latch.setOp1(op1);
				OF_EX_Latch.setOp2(op2);
				IF_EnableLatchType.setIF_enable(true);
				IF_OF_LatchType.setOF_enable(false);
				counter++;
				System.out.println("control " + control);
				control++;
			} else if (opcode == 29) {

				// Simulator.setSimulationComplete(true);

			}

			OF_EX_Latch.setInstruction(instruction);

			OF_EX_Latch.setOpcode(opcode);

		}

		// System.out.println("is IF enabled? " + IF_EnableLatchType.isIF_enable());

	}

	public int getRD(int instruction) {
		String insInBin = Integer.toBinaryString(instruction);
		insInBin = String.format("%32s", insInBin).replace(' ', '0');
		int opcode = Integer.parseInt(insInBin.substring(0, 5), 2);

		if (opcode >= 0 && opcode < 22) {
			if (opcode % 2 == 0) {
				return Integer.parseInt(insInBin.substring(15, 20), 2);
			} else
				return Integer.parseInt(insInBin.substring(10, 15), 2);
		} else if (opcode == 22 || opcode == 23) {
			return Integer.parseInt(insInBin.substring(10, 15), 2);
		} else if (opcode >= 25 && opcode <= 28) {
			return Integer.parseInt(insInBin.substring(10, 15), 2);
		} else
			return 100;
	}

}
