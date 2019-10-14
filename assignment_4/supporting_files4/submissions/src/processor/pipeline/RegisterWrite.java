package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);

			int aluresult=MA_RW_Latch.getAluresult();
			int loadresult=MA_RW_Latch.getLoadresult();
			int instruction=MA_RW_Latch.getInstruction();
			String insInBin = Integer.toBinaryString(instruction);
			insInBin=String.format("%32s", insInBin).replace(' ', '0');

			int opcode=MA_RW_Latch.getOpcode();
			int rd;

			if(opcode>=0 && opcode<22){
				if(opcode%2==0){

					rd=Integer.parseInt(insInBin.substring(15,20),2);
					containingProcessor.getRegisterFile().setValue(rd, aluresult);

				}else{

					rd=Integer.parseInt(insInBin.substring(10,15),2);
					containingProcessor.getRegisterFile().setValue(rd, aluresult);
					
					


				}
			}else if(opcode==22){
				rd=Integer.parseInt(insInBin.substring(10,15),2);
				containingProcessor.getRegisterFile().setValue(rd, loadresult);
			}
		}
	}

}
