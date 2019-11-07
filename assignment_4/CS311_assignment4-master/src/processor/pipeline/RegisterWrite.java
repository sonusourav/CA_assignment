package processor.pipeline;

import generic.Simulator;
import processor.Processor;
import processor.Clock;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
<<<<<<< HEAD

=======
	
>>>>>>> final version
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
<<<<<<< HEAD

	public void performRW()
	{
		if(MA_RW_LatchType.isRW_enable())
		{
=======
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
>>>>>>> final version

			int aluresult=MA_RW_Latch.getAluresult();
			int loadresult=MA_RW_Latch.getLoadresult();
			int instruction=MA_RW_Latch.getInstruction();
<<<<<<< HEAD
			System.out.println("instruction at RW " + Integer.toBinaryString(instruction) + " Clock "+Clock.getCurrentTime());
=======
			System.out.println("instruction at RW " + instruction + " Clock "+Clock.getCurrentTime());
>>>>>>> final version

			String insInBin = Integer.toBinaryString(instruction);
			insInBin=String.format("%32s", insInBin).replace(' ', '0');

			int opcode=MA_RW_Latch.getOpcode();
<<<<<<< HEAD
			//System.out.println("Current PC"+containingProcessor.getRegisterFile().getProgramCounter());
			if (opcode == 29){
					Simulator.setSimulationComplete(true);
					

			}
			int rd;

=======
			if (opcode == 29){
				Simulator.setSimulationComplete(true);

			}
			int rd;
			
>>>>>>> final version
			if(opcode>=0 && opcode<22){
				if(opcode%2==0){

					rd=Integer.parseInt(insInBin.substring(15,20),2);
					containingProcessor.getRegisterFile().setValue(rd, aluresult);

				}else{

					rd=Integer.parseInt(insInBin.substring(10,15),2);
					containingProcessor.getRegisterFile().setValue(rd, aluresult);
<<<<<<< HEAD


=======
					
					
>>>>>>> final version


				}
			}else if(opcode==22){
				rd=Integer.parseInt(insInBin.substring(10,15),2);
				containingProcessor.getRegisterFile().setValue(rd, loadresult);
			}
		}
	}

}
