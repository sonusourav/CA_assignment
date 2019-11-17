package processor.memorysystem;

import configuration.Configuration;
import generic.CacheReadEvent;
import generic.CacheResponseEvent;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;
import processor.Processor;
import processor.pipeline.EX_MA_LatchType;
import processor.pipeline.IF_EnableLatchType;
import processor.pipeline.IF_OF_LatchType;
import processor.pipeline.InstructionFetch;
import processor.pipeline.MA_RW_LatchType;
import processor.pipeline.MemoryAccess;
import processor.pipeline.OF_EX_LatchType;

public class Cache implements Element {

    int lines;
    static public boolean cacheHit;
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
        this.cacheLine=new CacheLine[line];
        for(int i=0;i<line;i++){
            cacheLine[i]=new CacheLine();
        }
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = containingProcessor.getIFUnit().getIF_EnableLatch();
		this.IF_OF_Latch = containingProcessor.getIFUnit().getIF_OF_Latch();
        this.EX_MA_Latch = containingProcessor.getMAUnit().getEX_MA_Latch();
        this.MA_RW_Latch = containingProcessor.getMAUnit().getMA_RW_Latch();
	}

    public int getLines() {
        return this.lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public void cacheRead(CacheReadEvent event, int address) {
        cacheHit = false;
        int bits = (int) (Math.log(lines / 2) / Math.log(2));
        String addressInBits = Integer.toBinaryString(address);
        addressInBits=String.format("%32s",addressInBits).replace(' ', '0');
        System.out.println("addressInBits"+addressInBits);
        int set=0;
        if(bits!=0){
            String lastBits = addressInBits.substring(32 - bits, 32);
            set = Integer.parseInt(lastBits, 2);
        }
        
        Cache insCache=null;
        if(event.getRequestingElement() instanceof MemoryAccess){
            System.out.println("Instance of Memory Acess");
            insCache=containingProcessor.getMemoryCache();

        }else if(event.getRequestingElement() instanceof InstructionFetch){
            insCache=containingProcessor.getInstructionCache();
            System.out.println("Instance of Memory Acess");
        }
        CacheLine insCacheLine[]=new CacheLine[insCache.getLines()];
        insCacheLine=insCache.getCacheLine();

        System.out.println("add"+address);
        System.out.println("tag0"+insCacheLine[2*set].tag);
        System.out.println("tag1"+insCacheLine[2*set+1].tag);

        if (insCacheLine[(2 * set)].tag == address ) {
            insCacheLine[2 * set].setState(true);
            insCacheLine[(2 * set) + 1].setState(false);
            handleCacheHit(event, address);
        } else if (cacheLine[(2 * set) + 1].tag == address) {
            insCacheLine[2 * set].setState(false);
            insCacheLine[2 * set + 1].setState(true);
            handleCacheHit(event,address);
        } else {

            handleCacheMiss(event,address);
        }

    }

    public void cacheWrite(Event event,int address, int value) {

        System.out.println("reaching Write "+ address);
        int bits = (int) (Math.log(lines / 2) / Math.log(2));
        String addressInBits = Integer.toBinaryString(address);
        addressInBits=String.format("%32s",addressInBits).replace(' ', '0');
        int set=0;
        if(bits!=0){
            String lastBits = addressInBits.substring(32 - bits, 32);
            set = Integer.parseInt(lastBits, 2);
        }
        Cache insCache=null;
        if(event.getRequestingElement() instanceof InstructionFetch){
             insCache=containingProcessor.getInstructionCache();
        }else if(event.getRequestingElement() instanceof MemoryAccess){
            insCache=containingProcessor.getMemoryCache();
        }
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

    public void handleCacheHit(CacheReadEvent event, int address){
        cacheHit = true;
        System.out.println("reaching CacheHit "+ address);
        System.out.println("ma enabled" + EX_MA_LatchType.isMA_enable());
        System.out.println("ex enabled" + OF_EX_LatchType.isEX_enable());
        System.out.println("OF enabled" + IF_OF_LatchType.isOF_enable());
        System.out.println("RW enabled" + MA_RW_LatchType.isRW_enable());
        System.out.println("IF enabled" + IF_EnableLatchType.isIF_enable());
        int data = containingProcessor.getMainMemory().getWord(address);
        String insInBin_new = Integer.toBinaryString(data);
		insInBin_new = String.format("%32s", insInBin_new).replace(' ', '0');
		int opcode_new = Integer.parseInt(insInBin_new.substring(0, 5), 2);
		

        if(event.getRequestingElement() instanceof InstructionFetch){
            IF_EnableLatch.setIF_busy(false);
            if(opcode_new==29){	
                IF_EnableLatch.setIF_busy(true);
            }
            Simulator.getEventQueue().addEvent(new CacheResponseEvent(processor.Clock.getCurrentTime(), this, event.getRequestingElement(),data));

        }else if(event.getRequestingElement() instanceof MemoryAccess){
            EX_MA_Latch.setMA_busy(false);
            Simulator.getEventQueue().addEvent(new CacheResponseEvent(processor.Clock.getCurrentTime(), this, event.getRequestingElement(),data));

        }
    }

    public void handleCacheMiss(Event event,int address) {
        cacheHit = false;
        System.out.println("reaching CacheMiss "+ address);
        cacheWrite(event,address,containingProcessor.getMainMemory().getWord(address));
        Simulator.getEventQueue().addEvent(new MemoryReadEvent((Clock.getCurrentTime() + Configuration.mainMemoryLatency ),
                 event.getRequestingElement(), containingProcessor.getMainMemory(),address));
                 
                 if(event.getRequestingElement() instanceof InstructionFetch){
                    IF_EnableLatch.setIF_busy(true) ;
                 }else{
                    EX_MA_Latch.setMA_busy(true) ;
                 }
    }

    @Override
    public void handleEvent(Event event) {
    
        System.out.println("type="+event.getEventType());
        if(event.getEventType()==EventType.CacheRead){
            CacheReadEvent event1=(CacheReadEvent) event;
            if(event.getRequestingElement() instanceof MemoryAccess){
                containingProcessor.getInstructionCache().cacheRead(event1,event1.getAddressToReadFrom());;

            }else if(event.getRequestingElement() instanceof InstructionFetch){
                containingProcessor.getMemoryCache().cacheRead(event1,event1.getAddressToReadFrom());;
            }

        }else if(event.getEventType()==EventType.MemoryResponse){
            MemoryResponseEvent event2=(MemoryResponseEvent)event;
            
            if(event.getRequestingElement() instanceof InstructionFetch){

                Simulator.getEventQueue().addEvent(new MemoryResponseEvent((Clock.getCurrentTime() ),
                containingProcessor.getMainMemory(), containingProcessor.getIFUnit(),event2.getValue()));

            }else if(event.getRequestingElement() instanceof MemoryAccess){

                Simulator.getEventQueue().addEvent(new MemoryResponseEvent((Clock.getCurrentTime() ),
                containingProcessor.getMainMemory(), containingProcessor.getMAUnit(),event2.getValue()));
            }

           
        }
    }


}