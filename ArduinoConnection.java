import java.util.concurrent.TimeUnit;

import arduino.*;

public class ArduinoConnection{
	
	private String comPort;
	private int baudRate;
	Arduino arduino;
	
	public ArduinoConnection(String port, int baud){
		
		comPort = port;
		baudRate = baud;
		
		arduino = new Arduino(comPort, baudRate);
		arduino.openConnection();
		
		try {	
			TimeUnit.SECONDS.sleep(5);	
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Arduino communication opened on " + comPort + " at a baudrate of " + baudRate);
		
	}
	
	public void toggleLED(String state) {
		
		if(state == "HIGH") {
			arduino.serialWrite('1');
		}
		else if(state == "LOW") {
			arduino.serialWrite('0');
		}
	}
	
	public void closeConnection() {
		
		arduino.closeConnection();
	}
	

}
