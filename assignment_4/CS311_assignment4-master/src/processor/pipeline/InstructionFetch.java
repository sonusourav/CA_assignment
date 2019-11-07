package processor.pipeline;

import processor.Processor;
import processor.Clock;

public class InstructionFetch {

	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;

	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch,
			IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void performIF() {
<<<<<<< HEAD
		if (IF_EnableLatchType.isIF_enable()) {
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			IF_OF_Latch.setInstruction(newInstruction);
			System.out.println("instruction at IF " + Integer.toBinaryString(newInstruction)+ " Clock "+Clock.getCurrentTime());

			//System.out.println("The current PC is " + currentPC);
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);


			IF_OF_LatchType.setOF_enable(true);
=======
		if (IF_EnableLatch.isIF_enable()) {
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			IF_OF_Latch.setInstruction(newInstruction);
			System.out.println("instruction in IF " + newInstruction+ " Clock "+Clock.getCurrentTime());

			
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);


			IF_OF_Latch.setOF_enable(true);
>>>>>>> final version
		}
	}

}
