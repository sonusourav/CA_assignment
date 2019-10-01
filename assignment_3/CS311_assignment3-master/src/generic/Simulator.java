package generic;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

import processor.Clock;
import processor.Processor;

public class Simulator {

	static Processor processor;
	static boolean simulationComplete;

	public static void setupSimulation(String assemblyProgramFile, Processor p) throws IOException {
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);

		simulationComplete = false;
	}

	static void loadProgram(String assemblyProgramFile) throws IOException
	{
		DataInputStream in = new DataInputStream(new FileInputStream(assemblyProgramFile));
		
		try
		{
			int address = 0;
			
			while (in.available()>0 ) 
			{
				int data = in.readInt();
				if(address == 0)
				{
					processor.getRegisterFile().setProgramCounter(data);
					processor.getRegisterFile().setValue(1, 0);
					processor.getRegisterFile().setValue(1, 65535);
					processor.getRegisterFile().setValue(2, 65535);
				}
				else
				{
					processor.getMainMemory().setWord(address - 1, data);
				}
				address++;
			}
		} 
		catch (EOFException ignored) 
		{
			
			System.out.println("EOF exception");
		}
		in.close();

	}
	
	public static void simulate()
	{
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();
		}
		
		// TODO
		// set statistics
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
