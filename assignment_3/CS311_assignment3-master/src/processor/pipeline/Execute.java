package processor.pipeline;

import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;

	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		
		int instruction=OF_EX_Latch.getInstruction();
		int opcode=OF_EX_Latch.getOpcode();
		int op1=OF_EX_Latch.getOp1();
		int op2=OF_EX_Latch.getOp2();
		int immediate=OF_EX_Latch.getImmediate();
		int aluresult=0;
		
		switch(opcode%2){
			case 0:
			aluresult=op1+op2;
			break;
			case 1:
			aluresult=op1-op2;
			break;
			case 3:
			aluresult=op1*op2;
			break;
			case 4:
			aluresult=op1/op2;
			containingProcessor.getRegisterFile().setValue(31, op1%op2);
			break;
			case 5:
			aluresult=op1 & op2;
			break;
			case 6:
			aluresult=op1 | op2;
			break;
			case 7:
			aluresult=op1 ^ op2;
			break;
			case 8:
			if(op1<op2)
			aluresult=1;
			break;
			case 9:
			break;
			case 10:
			break;
			
			


		
		}

	}

}
