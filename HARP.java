
public class HARP {

	public static void main(String[] args) {

		MySQLAccess iridium = new MySQLAccess();
		BaseStation ground = new BaseStation(32.83128, -83.64983, 216, 130);

		
		iridium.getPayloadTelemetry();
		System.out.println(ground.turnToCoordinate(iridium.gps_lat, iridium.gps_long));
		System.out.println(ground.calculateAngleOfElevation(iridium.gps_lat, iridium.gps_long, iridium.gps_alt));
		
		ArduinoConnection arduino = new ArduinoConnection("COM7", 9600);
		arduino.closeConnection();
		
	}
}
