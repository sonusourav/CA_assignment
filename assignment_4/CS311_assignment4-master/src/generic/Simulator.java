package generic;


import java.io.*;
import processor.Clock;
import processor.Processor;

public class Simulator {
<<<<<<< HEAD

	static Processor processor;
	static boolean simulationComplete;

=======
		
	static Processor processor;
	static boolean simulationComplete;
	
>>>>>>> final version
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
<<<<<<< HEAD

		simulationComplete = false;
	}

	static void loadProgram(String assemblyProgramFile)
	{
		String fileName = assemblyProgramFile;

=======
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		String fileName = assemblyProgramFile;
		
>>>>>>> final version
		DataInputStream in=null;
            try
            {
				 in = new DataInputStream(new FileInputStream(fileName));

                int address = 0;
<<<<<<< HEAD
                while (in.available()>0 )
=======
                while (in.available()>0 ) 
>>>>>>> final version
                {
                    int data = in.readInt();
                    if(address == 0)
                    {
						processor.getRegisterFile().setProgramCounter(data);
<<<<<<< HEAD

=======
						
>>>>>>> final version
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
<<<<<<< HEAD

                }
            }catch (IOException ignored)
=======
					
                }
            }catch (IOException ignored) 
>>>>>>> final version
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
<<<<<<< HEAD

=======
	
>>>>>>> final version
	public static void simulate()
	{          int k = 0;
		while(simulationComplete == false )
		{
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			processor.getOFUnit().performOF();
<<<<<<< HEAD
			processor.getIFUnit().performIF();
			//System.out.println("IF done ");

			//System.out.println("OF done ");

			//System.out.println("EX done ");

			//System.out.println("MA done ");

			//System.out.println("RW done ");
			Clock.incrementClock();
			System.out.println("Number of cycles is "+ Clock.getCurrentTime());
=======

			processor.getIFUnit().performIF();
			//System.out.println("IF done ");
			
			//System.out.println("OF done ");
			
			//System.out.println("EX done ");
			
			//System.out.println("MA done ");
			
			//System.out.println("RW done ");
			Clock.incrementClock();
>>>>>>> final version
			k=k+1;
			//System.out.println("complete? " + simulationComplete);

		}
<<<<<<< HEAD
		//processor.getRegisterFile().setProgramCounter(processor.getRegisterFile().getProgramCounter()-2);



	}

	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;

	}
}
=======
		System.out.println("Number of cycles is "+ Clock.getCurrentTime());

		
		
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
>>>>>>> final version
