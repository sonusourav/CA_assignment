package processor.pipeline;

import processor.Processor;
import processor.memorysystem.Cache;
import generic.CacheReadEvent;
import generic.CacheResponseEvent;
import generic.Element;
import generic.Event;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;

public class InstructionFetch implements Element {

	Processor containingProcessor;
	public IF_EnableLatchType IF_EnableLatch;
	public IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	Cache instructionCache;

	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch,
			IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public IF_EnableLatchType getIF_EnableLatch() {
		return this.IF_EnableLatch;
	}

	public void setIF_EnableLatch(IF_EnableLatchType IF_EnableLatch) {
		this.IF_EnableLatch = IF_EnableLatch;
	}

	public IF_OF_LatchType getIF_OF_Latch() {
		return this.IF_OF_Latch;
	}

	public void setIF_OF_Latch(IF_OF_LatchType IF_OF_Latch) {
		this.IF_OF_Latch = IF_OF_Latch;
	}

	public Cache getInstructionCache() {
		return this.instructionCache;
	}

	public void setInstructionCache(Cache instructionCache) {
		this.instructionCache = instructionCache;
	}

	public void performIF() {
		if (IF_EnableLatchType.isIF_enable()) {

			if(IF_EnableLatch.isIF_busy()){
				return;
			}else{

				Simulator.getEventQueue().addEvent(new CacheReadEvent((Clock.getCurrentTime() + Clock.getCacheSize() ),
				 this, containingProcessor.getInstructionCache(),containingProcessor.getRegisterFile().getProgramCounter()));

				int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
				System.out.println("instruction at IF " + Integer.toBinaryString(newInstruction)+ " Clock "+Clock.getCurrentTime());
	
				IF_EnableLatch.setIF_busy(true) ;
			}
		}
	}

	@Override
	public void handleEvent(Event event) {
		int currentPC = containingProcessor.getRegisterFile().getProgramCounter();

		if(event.getEventType()==EventType.MemoryResponse){
			MemoryResponseEvent event2=(MemoryResponseEvent) event;
			IF_OF_Latch.setInstruction(event2.getValue());
			System.out.println("Memory Response IF");

		}else if(event.getEventType()==EventType.CacheResponse){

			CacheResponseEvent event2=(CacheResponseEvent) event;
			IF_OF_Latch.setInstruction(event2.getValue());
			System.out.println("Cache Response IF");

		}
		containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
		IF_OF_LatchType.setOF_enable(true);
		IF_EnableLatch.setIF_busy(false);
		
	}

}
