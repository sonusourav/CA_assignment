package processor.pipeline;
import processor.Clock;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}

	public void performMA()
	{	
		if (containingProcessor.getIFUnit().IF_EnableLatch.isIF_busy()){
			if (OperandFetch.IF_counter==4){
				OperandFetch.IF_counter++;
				EX_MA_LatchType.setMA_enable(true);
			}
			else if(OperandFetch.IF_counter==5){
				OperandFetch.IF_counter++;
				EX_MA_LatchType.setMA_enable(false);
				MA_RW_LatchType.setRW_enable(true);
			}

			else {
				EX_MA_LatchType.setMA_enable(false);
			}
	}

		

		if(EX_MA_LatchType.isMA_enable()){

			MA_RW_LatchType.setRW_enable(true);

		int instruction=EX_MA_Latch.getInstruction();
		System.out.println("instruction at MA " + Integer.toBinaryString(instruction)+ " Clock "+Clock.getCurrentTime());

		int opcode=EX_MA_Latch.getOpcode();
		int op1=EX_MA_Latch.getOp1();
		int op2=EX_MA_Latch.getOp2();
		int immediate=EX_MA_Latch.getImmediate();
		int loadResult=0;

		if(opcode==22){
			loadResult=containingProcessor.getMainMemory().getWord(op1+immediate);
		}else if(opcode ==23){
			containingProcessor.getMainMemory().setWord(containingProcessor.getRegisterFile().getValue(op2)+immediate, op1);
			//System.out.println("DESTINATION "+containingProcessor.getRegisterFile().getValue(op2)+"immm"+immediate);
		}

		MA_RW_Latch.setAluresult(EX_MA_Latch.getAluresult());
		MA_RW_Latch.setLoadresult(loadResult);
		MA_RW_Latch.setOpcode(opcode);
		MA_RW_Latch.setInstruction(instruction);

	}
}
}


