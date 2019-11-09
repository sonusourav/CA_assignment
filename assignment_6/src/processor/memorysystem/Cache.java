package processor.memorysystem;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;
import processor.pipeline.EX_MA_LatchType;
import processor.pipeline.IF_EnableLatchType;
import processor.pipeline.IF_OF_LatchType;
import processor.pipeline.MA_RW_LatchType;

public class Cache implements Element {

    int lines;
    Processor containingProcessor;
    CacheLine cacheLine[];
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;


    public Cache(int line){
        this.lines=line;
        this.cacheLine=new CacheLine[line];
    }

    public CacheLine[] getCacheLine() {
        return cacheLine;
    }

    public void setCacheLine(CacheLine cacheLine[]) {
        this.cacheLine= cacheLine;
    }

    public Cache(int line,Processor containingProcessor) {
        this.lines=line;        
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = containingProcessor.getIFUnit().getIF_EnableLatch();
		this.IF_OF_Latch = containingProcessor.getIFUnit().getIF_OF_Latch();
        this.EX_MA_Latch = containingProcessor.getMAUnit().getEX_MA_Latch();
        this.MA_RW_Latch = containingProcessor.getMAUnit().getMA_RW_Latch();
	}

    public void cacheRead(int address) {

        int bits = (int) (Math.log(lines / 2) / Math.log(2));
        String addressInBits = Integer.toBinaryString(address);
        addressInBits=String.format("%32s",addressInBits).replace(' ', '0');
        System.out.println("addressInBits"+addressInBits);
        String lastBits = addressInBits.substring(32 - bits, 32);
        int set = Integer.parseInt(lastBits, 2);
        Cache insCache=containingProcessor.getIFUnit().getInstructionCache();
        CacheLine insCacheLine[]=insCache.getCacheLine();

        if (insCacheLine[(2 * set)].tag == address ) {
            insCacheLine[2 * set].setState(true);
            insCacheLine[(2 * set) + 1].setState(false);
            handleCacheHit(address);
        } else if (cacheLine[(2 * set) + 1].tag == address) {
            insCacheLine[2 * set].setState(false);
            insCacheLine[2 * set + 1].setState(true);
            handleCacheHit(address);
        } else {

            handleCacheMiss(address);
        }

    }

    public void cacheWrite(int address, int value) {

        int bits = (int) (Math.log(lines / 2) / Math.log(2));
        String addressInBits = Integer.toBinaryString(address);
        addressInBits=String.format("%32s",addressInBits).replace(' ', '0');
        String lastBits = addressInBits.substring(32 - bits, 32);
        int set = Integer.parseInt(lastBits, 2);
        Cache insCache=containingProcessor.getIFUnit().getInstructionCache();
        CacheLine insCacheLine[]=insCache.getCacheLine();

        if(insCacheLine[2*set].state==true && insCacheLine[2*set+1].state==false){
            insCacheLine[2*set+1].tag=address;
            insCacheLine[2*set+1].data=value;
            insCacheLine[2*set].state=false;
            insCacheLine[2*set+1].state=true;
        }else{
            insCacheLine[2*set].tag=address;
            insCacheLine[2*set].data=value;
            insCacheLine[2*set].state=true;
            insCacheLine[2*set+1].state=false;
        }

    }

    public void handleCacheHit(int address){
		IF_OF_Latch.setInstruction(address);
		containingProcessor.getRegisterFile().setProgramCounter(address + 1);
		IF_OF_LatchType.setOF_enable(true);
        IF_EnableLatch.setIF_busy(false);
    }

    public void handleCacheMiss(int address) {

        Simulator.getEventQueue().addEvent(new MemoryReadEvent((Clock.getCurrentTime() + Configuration.mainMemoryLatency ),
                 this, containingProcessor.getMainMemory(),containingProcessor.getRegisterFile().getProgramCounter()));
                 
		IF_EnableLatch.setIF_busy(true) ;
    }

    @Override
    public void handleEvent(Event event) {

        MemoryReadEvent memoryReadEvent=(MemoryReadEvent)event;
        IF_OF_Latch.setInstruction(memoryReadEvent.getAddressToReadFrom());
        int instruction=containingProcessor.getMainMemory().getWord(memoryReadEvent.getAddressToReadFrom());
        containingProcessor.getRegisterFile().setProgramCounter(memoryReadEvent.getAddressToReadFrom() + 1);
        cacheWrite(memoryReadEvent.getAddressToReadFrom(),instruction);
		IF_OF_LatchType.setOF_enable(true);
        IF_EnableLatch.setIF_busy(false);
    
    }


}