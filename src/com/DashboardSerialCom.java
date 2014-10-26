package com;
import processing.core.PApplet;
import processing.serial.Serial;


enum SerialMessage{
	KNOB (0,3,2),				// data comes from rotary knob 
	BTN  (1,3,2),					// data comes from button 
	RFID (2,5,3); 				// data comes from rfid tag 
	
	final int code;				// the code which encodes the type of serial message (which hw component it comes from)
	final int msgLength; 		// the allowed length of the serial message (different for the different actions) 
	final int msgParts; 		// how many ':'-separated msg parts contains the serial message 
	
	SerialMessage(int code, int msgLength, int msgParts){
		this.code 			= code; 
		this.msgLength  	= msgLength;
		this.msgParts		= msgParts; 
	}
	
	int getCode() {
		return code; 
	}
	
	int getMsgLength() {
		return msgLength; 
	}
	
	int getMsgParts() {
		return msgParts;
	}
}

enum SerialState {
	COMPLETE, INCOMPLETE, UNKNOWN
}

/** When new serial data arrives, this class adds it to an internally defined String.
 When a newline is received, the loop prints the string and 
 clears it.
 */
public class DashboardSerialCom {

	/**
	 * Simple Read
	 * 
	 */
	Serial myPort;  // Create object from Serial class
	
	String inputString = "";           // a string to hold incoming data
	boolean stringComplete = false;    // whether the string is complete
	
	DashboardListener listener; 
	
	public DashboardSerialCom(PApplet p) {
	  
	  String[] ports = Serial.list();
	  System.out.println("ports:"+ports.length);
	  for(int i=0;i<ports.length;i++){
		  System.out.println(ports[i]);  
	  }
	  //String portName = "/dev/tty.usbmodemfa1412";
	  //myPort = new Serial(p, portName, 9600);
	}

	public DashboardListener getListener() {
		return listener;
	}

	public void setDashboardListener(DashboardListener listener) {
		this.listener = listener;
	}
	
	/*
	 SerialEvent occurs whenever a new data comes in the
	 hardware serial RX.  This routine is run between each
	 time the main loop() runs, so using delay inside loop can delay
	 response.  Multiple bytes of data may be available.
	 */
	public void serialEvent(Serial p) {
	  while (p.available()>0) {
	    // get the new byte:
	    char inChar = (char)p.read(); 
	    // add it to the inputString:
	    inputString += inChar;
//		System.out.println("inputString " + inputString);

	    // if the incoming character is a newline, set a flag
	    // so the main loop can do something about it:
	    if (inChar == '\n') {
		  stringComplete = true;
//		  System.out.println("nl: <" + inputString + ">");

	    } 
	  }
	}

	public void readSerialMsg()
	{
		SerialState state = SerialState.UNKNOWN;
		//--------------------analyze the string when a newline arrives:
		if (stringComplete) {
//			System.out.println("before: <" + inputString + ">");
			
			//inputString = inputString.substring(0, inputString.length()-1);
			//inputString = inputString.replace(System.getProperty("line.separator"), "");
			inputString = inputString.replaceAll("\\r|\\n", "");

			System.out.println();
			System.out.println("DashboardSerialCom::readSerialMsg(): <" + inputString + ">");
						
			//----------------dispatch the DB write event corresponding to the incomping string 
			state = dispatchSerialEvent(inputString);
			
			//----------------clear the incomping string:
			inputString 	= "";
			stringComplete 	= false;
			
//			System.out.println(state); 
		}
		
	}
	
	private SerialState dispatchSerialEvent(String inputString) {
	    System.out.println("DashboardSerialCom::dispatchSerialEvent() " + inputString);

		SerialState state = SerialState.UNKNOWN; 

		//--------------------split serial message according to delimiter 
		String[] stringtemp = PApplet.split(inputString, ':');
		
		int msgLength 			= inputString.length();
		int msgParts			= stringtemp.length; 
		
		//--------------------check if not empty or incomplete 
	    if (msgLength < 3) {
	    	System.out.println("dispatchSerialEvent: Serial string too short!"); 
	    	state = SerialState.INCOMPLETE; 
	    	return state;
	    } else if (stringtemp[0].length() != 1) { //first part is always a one-digit code 
	    	System.out.println("dispatchSerialEvent: Serial msg code invalid"); 
	    	state = SerialState.INCOMPLETE; 
	    	return state;	
	    }
	    
	  //--------------------decide according to the action that happened @ the physical computing side (encoded) 
	    int actionCode = Integer.parseInt(stringtemp[0]);

	    ///*
	  //--------------------action=rotary knob rotated=new category selected
	    if (actionCode == SerialMessage.KNOB.getCode() && 
	    	msgLength == SerialMessage.KNOB.getMsgLength() && 
	    	msgParts == SerialMessage.KNOB.getMsgParts()) {
	    	
	    	listener.categorySelected(Integer.parseInt(stringtemp[1])); 
	    	state = SerialState.COMPLETE;
	    	
	  //--------------------action=rfid swipe=new sentiment submitted  	
	  //--------------------ATTENTION! for RFID msg we can allow different cardID lengths, but they have to have minimum one char
	    } else if (actionCode == SerialMessage.RFID.getCode() && 
	    		   msgLength >= SerialMessage.RFID.getMsgLength() &&
	    		   msgParts == SerialMessage.RFID.getMsgParts()) {

    		listener.sentimentSubmitted(Integer.parseInt(stringtemp[1]), stringtemp[2]);
        	state = SerialState.COMPLETE;
       
      //--------------------action=heart btn pressed=special category selected <3  	   	
	    } else if (actionCode == SerialMessage.BTN.getCode() && 
	    		   msgLength == SerialMessage.BTN.getMsgLength() &&
	    		   msgParts  == SerialMessage.BTN.getMsgParts()) {
	    	
	    	listener.specialCategorySelected(Integer.parseInt(stringtemp[1]));
	    	state = SerialState.COMPLETE;	
	    } else {
	    	state = SerialState.UNKNOWN;
	    }
		//*/
		/////////////////////////////////////////////////////////////////////////
		return state; 
	}
	
}


