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
		if(OF_EX_Latch.isEX_enable()){

			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);
			
			int instruction=OF_EX_Latch.getInstruction();
			int opcode=OF_EX_Latch.getOpcode();
			int op1=OF_EX_Latch.getOp1();
			int op2=OF_EX_Latch.getOp2();
			int immediate=OF_EX_Latch.getImmediate();
			int aluresult=0;
			int branchTarget=OF_EX_Latch.getBranchTarget();
			int tempOpcode=opcode;
			if(tempOpcode>=0 && tempOpcode<22){
				tempOpcode/=2;
			}
			switch(tempOpcode){
				case 0:
				aluresult=op1+op2;
				break;
				case 1:
				aluresult=op1-op2;
				break;
				case 2:
				aluresult=op1*op2;
				break;
				case 3:
				aluresult=op1/op2;
				containingProcessor.getRegisterFile().setValue(31, op1%op2);
				
				break;
				case 4:
				aluresult=op1 & op2;
				break;
				case 5:
				aluresult=op1 | op2;
				break;
				case 6:
				aluresult=op1 ^ op2;
				break;
				case 7:
				if(op1<op2)
				aluresult=1;
				break;
				case 8:aluresult=op1<<op2;
				break;
				case 9:aluresult=op1>>>op2;
				break;
				case 10:aluresult=op1>>op2;
				break;
				case 25:
				if(op1==op2){
				EX_IF_Latch.setIsBranchTaken(true);
				EX_IF_Latch.setBranchPC(branchTarget);
				containingProcessor.getRegisterFile().setProgramCounter(branchTarget);
			}
				break;
				case 26:
				if(op1!=op2){
					EX_IF_Latch.setIsBranchTaken(true);
					EX_IF_Latch.setBranchPC(branchTarget);
					containingProcessor.getRegisterFile().setProgramCounter(branchTarget);
				}
				break;
				case 27:
				if(op1<op2){
					EX_IF_Latch.setIsBranchTaken(true);
					EX_IF_Latch.setBranchPC(branchTarget);	
					containingProcessor.getRegisterFile().setProgramCounter(branchTarget);
				}
				break;
				case 28:
				if(op1>op2){
					
					EX_IF_Latch.setIsBranchTaken(true);
					EX_IF_Latch.setBranchPC(branchTarget);
					containingProcessor.getRegisterFile().setProgramCounter(branchTarget);	
				}
				break;
				
			}
	
			EX_MA_Latch.setAluresult(aluresult);
			EX_MA_Latch.setOp1(op1);
			EX_MA_Latch.setOp2(op2);
			EX_MA_Latch.setInstruction(instruction);
			EX_MA_Latch.setOpcode(opcode);
			EX_MA_Latch.setImmediate(immediate);

		}
		
		}

}
