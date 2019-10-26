package processor.pipeline;
import processor.Clock;
import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import processor.Processor;
import generic.Simulator;

public class MemoryAccess implements Element {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	static int MA_counter=0;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}

	public void performMA()
	{	//System.out.println("if counter"+OperandFetch.IF_counter);
		if ((OperandFetch.IF_counter==4 || EX_MA_Latch.isMA_busy())&&MA_counter==0){
			OperandFetch.IF_counter++;
			EX_MA_LatchType.setMA_enable(true);
		}
		else {
			EX_MA_LatchType.setMA_enable(false);
		}
		if (EX_MA_LatchType.isMA_enable()){
			//System.out.println("IS MA busy" +EX_MA_Latch.isMA_busy());
			if (EX_MA_Latch.isMA_busy()){
				//System.out.println("MA BUSY HAI BHAI");
			}
			else {
				
				int instruction=EX_MA_Latch.getInstruction();
				System.out.println("instruction at MA " + Integer.toBinaryString(instruction)+ " Clock "+Clock.getCurrentTime());
				int opcode=EX_MA_Latch.getOpcode();
				int op1=EX_MA_Latch.getOp1();
				
				int op2=EX_MA_Latch.getOp2();
				int immediate=EX_MA_Latch.getImmediate();
				int loadResult=0;
				if(MA_counter!=1){
					if (opcode ==22||opcode==23){
						EX_MA_Latch.setMA_busy(true);
						
						if (opcode==22){
							Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,
					 this, containingProcessor.getMainMemory(),op1+immediate));
						}
						else if(opcode==23){
							Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + Configuration.mainMemoryLatency,
					 this, containingProcessor.getMainMemory(),op1+immediate));
						}
						MA_RW_Latch.setAluresult(EX_MA_Latch.getAluresult());
						MA_RW_Latch.setLoadresult(op1+immediate);
						MA_RW_Latch.setOpcode(opcode);
						MA_RW_Latch.setInstruction(instruction);
				}
				else {
					if (containingProcessor.getIFUnit().IF_EnableLatch.isIF_busy()){
						if (OperandFetch.IF_counter==4){
							OperandFetch.IF_counter++;
							EX_MA_LatchType.setMA_enable(true);
						}
						else if(OperandFetch.IF_counter==5){
							OperandFetch.IF_counter++;
							EX_MA_LatchType.setMA_enable(false);
							MA_RW_LatchType.setRW_enable(true);
							MA_RW_Latch.setAluresult(EX_MA_Latch.getAluresult());
							MA_RW_Latch.setLoadresult(loadResult);
							MA_RW_Latch.setOpcode(opcode);
							MA_RW_Latch.setInstruction(instruction);
						}
			
						else {
							EX_MA_LatchType.setMA_enable(false);
						}
				}

				}
				}
				else {
					if (containingProcessor.getIFUnit().IF_EnableLatch.isIF_busy()){
						if (OperandFetch.IF_counter==4){
							OperandFetch.IF_counter++;
							EX_MA_LatchType.setMA_enable(true);
						}
						else if(OperandFetch.IF_counter==5){
							OperandFetch.IF_counter++;
							EX_MA_LatchType.setMA_enable(false);
							MA_RW_LatchType.setRW_enable(true);
							MA_RW_Latch.setAluresult(EX_MA_Latch.getAluresult());
							MA_RW_Latch.setLoadresult(loadResult);
							MA_RW_Latch.setOpcode(opcode);
							MA_RW_Latch.setInstruction(instruction);
						}
			
						else {
							EX_MA_LatchType.setMA_enable(false);
						}
				}

				}
			}
		}

}

	@Override
	public void handleEvent(Event event2) {
		
		MA_RW_LatchType.setRW_enable(true);
		MA_counter=1;
		EX_MA_Latch.setMA_busy(false);
		EX_MA_LatchType.setMA_enable(false);
		System.out.println("ITS HANDLING MA");
		int instruction=EX_MA_Latch.getInstruction();
		int opcode=EX_MA_Latch.getOpcode();
		int op1=EX_MA_Latch.getOp1();
		int op2=EX_MA_Latch.getOp2();
		int immediate=EX_MA_Latch.getImmediate();
		int loadResult=0;
		MemoryResponseEvent event3=(MemoryResponseEvent) event2;
		if (opcode==22){
			System.out.println("Contents"+containingProcessor.getMainMemory().getWord(op1+immediate));
			loadResult=containingProcessor.getMainMemory().getWord(op1+immediate);
		}
		else if(opcode==23){
			containingProcessor.getMainMemory().setWord(containingProcessor.getRegisterFile().getValue(op2)+immediate, op1);
		}
		
		MA_RW_Latch.setAluresult(EX_MA_Latch.getAluresult());
		MA_RW_Latch.setLoadresult(loadResult);
		MA_RW_Latch.setOpcode(opcode);
		MA_RW_Latch.setInstruction(instruction);

	}
}


