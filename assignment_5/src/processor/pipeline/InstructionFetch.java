package processor.pipeline;

import processor.Processor;
import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;

public class InstructionFetch implements Element {

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
		if (IF_EnableLatchType.isIF_enable()) {

			if(IF_EnableLatch.isIF_busy()){
				return;
			}else{

				Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,
				 this, containingProcessor.getMainMemory(),containingProcessor.getRegisterFile().getProgramCounter()));

				int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
				//IF_OF_Latch.setInstruction(newInstruction);
				System.out.println("instruction at IF " + Integer.toBinaryString(newInstruction)+ " Clock "+Clock.getCurrentTime());
	
				//System.out.println("The current PC is " + currentPC);
				//containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
				IF_EnableLatch.setIF_busy(true) ;
				//IF_OF_LatchType.setOF_enable(true);
			}
		}
	}

	@Override
	public void handleEvent(Event event) {
		int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
		MemoryResponseEvent event2=(MemoryResponseEvent) event;
		IF_OF_Latch.setInstruction(event2.getValue());
		containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
		IF_OF_LatchType.setOF_enable(true);
		IF_EnableLatch.setIF_busy(false);
	}

}
