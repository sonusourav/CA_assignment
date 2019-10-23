package processor.memorysystem;

import java.time.Clock;

import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import generic.Event.EventType;

public class MainMemory implements Element {
	int[] memory;

	public MainMemory() {
		memory = new int[65536];
	}

	public int getWord(int address) {
		return memory[address];
	}

	public void setWord(int address, int value) {
		memory[address] = value;
	}

	public String getContentsAsString(int startingAddress, int endingAddress) {
		if (startingAddress == endingAddress)
			return "";

		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for (int i = startingAddress; i <= endingAddress; i++) {
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void handleEvent(Event event) {
		if(event.getEventType()==EventType.MemoryRead){
			MemoryReadEvent event2 =(MemoryReadEvent) event;
			Simulator.getEventQueue().addEvent(new MemoryResponseEvent(processor.Clock.getCurrentTime(), this, event2.getRequestingElement(),getWord(event2.getAddressToReadFrom())));
		}
	}
}
