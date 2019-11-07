package generic;


import java.io.*;
import processor.Clock;
import processor.Processor;

public class Simulator {

	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue;.

	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);

		simulationComplete = false;
	}

	static void loadProgram(String assemblyProgramFile)
	{
		String fileName = assemblyProgramFile;

		DataInputStream in=null;
            try
            {
				 in = new DataInputStream(new FileInputStream(fileName));

                int address = 0;
                while (in.available()>0 )
                {
                    int data = in.readInt();
                    if(address == 0)
                    {
						processor.getRegisterFile().setProgramCounter(data);

                        //System.out.println("setting first PC :"+data);
                        processor.getRegisterFile().setValue(1, 0);
                        processor.getRegisterFile().setValue(1, 65535);
                        processor.getRegisterFile().setValue(2, 65535);
                    }
                    else
                    {
                        processor.getMainMemory().setWord(address - 1, data);
                        //System.out.println("setting data in main memory :"+(address-1)+" data is "+data);
                    }
					address++;

                }
            }catch (IOException ignored)
            {
                System.out.println("EOF exception");
            }
            try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static void simulate()
	{          int k = 0;
		while(simulationComplete == false )
		{
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			processor.getOFUnit().performOF();

			processor.getIFUnit().performIF();
			//System.out.println("IF done ");

			//System.out.println("OF done ");

			//System.out.println("EX done ");

			//System.out.println("MA done ");

			//System.out.println("RW done ");
			Clock.incrementClock();
			k=k+1;
			//System.out.println("complete? " + simulationComplete);

		}
		System.out.println("Number of cycles is "+ Clock.getCurrentTime());



	}

	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
