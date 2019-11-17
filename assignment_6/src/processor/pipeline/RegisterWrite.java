package processor.pipeline;

import generic.Simulator;
import processor.Processor;
import processor.Clock;
import processor.memorysystem.Cache;

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
	{	System.out.println("Counter" + OperandFetch.IF_counter);
		if (containingProcessor.getMAUnit().EX_MA_Latch.isMA_busy()){
			
		}
		else {
			//System.out.println("IF counter "+ OperandFetch.IF_counter);
			//System.out.println("is if busy"+ containingProcessor.getIFUnit().IF_EnableLatch.isIF_busy());

			if (containingProcessor.getIFUnit().IF_EnableLatch.isIF_busy()){
				
				if (OperandFetch.IF_counter==6){
					OperandFetch.IF_counter++;
					MA_RW_LatchType.setRW_enable(true);
				}
				
				else {
					if (Cache.instructionCacheHit==true){
						if(MA_RW_LatchType.isRW_enable()==true){
							MA_RW_LatchType.setRW_enable(true);
							//OperandFetch.IF_counter=0;
						}
						else{
							MA_RW_LatchType.setRW_enable(false);
						}
					}
					else {
						MA_RW_LatchType.setRW_enable(false);
					}
					
				}
		}
	
			if(MA_RW_LatchType.isRW_enable())
			{
				int aluresult=MA_RW_Latch.getAluresult();
				int loadresult=MA_RW_Latch.getLoadresult();
				int instruction=MA_RW_Latch.getInstruction();
				if(Cache.instructionCacheHit==true){
					MA_RW_LatchType.setRW_enable(false);
				}
				if (MemoryAccess.MA_counter==1){
					MemoryAccess.MA_counter =0;
				}

				System.out.println("instruction at RW " + Integer.toBinaryString(instruction) + " Clock "+Clock.getCurrentTime());
				/*
				if (Cache.cacheHit==true)
				{
					Cache.cacheHit=false;
				}
				*/
				String insInBin = Integer.toBinaryString(instruction);
				insInBin=String.format("%32s", insInBin).replace(' ', '0');
	
				int opcode=MA_RW_Latch.getOpcode();
				//System.out.println("Current PC"+containingProcessor.getRegisterFile().getProgramCounter());
				if (opcode == 29){
						if(Cache.instructionCacheHit==true){
							containingProcessor.getRegisterFile().setProgramCounter(containingProcessor.getRegisterFile().getProgramCounter()+1);
						}
						Simulator.setSimulationComplete(true);
						containingProcessor.getIFUnit().IF_OF_Latch.setInstruction(0);
						
						
	
				}
				int rd;
	
				if(opcode>=0 && opcode<22){

					if(opcode%2==0){
	
						rd=Integer.parseInt(insInBin.substring(15,20),2);
						containingProcessor.getRegisterFile().setValue(rd, aluresult);
						System.out.println("The address" + rd + " the value "+aluresult);
	
					}else{
	
						rd=Integer.parseInt(insInBin.substring(10,15),2);
						containingProcessor.getRegisterFile().setValue(rd, aluresult);
						System.out.println("The address" + rd + " the value "+aluresult);

	
	
	
					}
				}else if(opcode==22){
					rd=Integer.parseInt(insInBin.substring(10,15),2);
					System.out.println("ITS LOADING" + loadresult);
					containingProcessor.getRegisterFile().setValue(rd, loadresult);
				}
			}
		}
		
		
		

	}

}
