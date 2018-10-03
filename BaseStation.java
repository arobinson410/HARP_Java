/**Encompasses all required methods to determine positioning for base station movement 
 * 
 * @author Andrew Robinson
 *
 */
public class BaseStation {

	public double groundLatitude, groundLongitude, groundHeading, groundAltitude;
	
	
	/**
	 * Creates a new object for a base station tracking array.
	 * 
	 * @param latitude Base station latitude in decimal notation (North is positive and South is Negative)
	 * @param longitude Base station longitude in decimal notation (East is positive and West is Negative) 
	 * @param heading Heading of the "zero-ed" base station in degrees
	 * @param altitude Altitude of the base station's dish in meters
	 */
	public BaseStation(double latitude, double longitude, double heading, double altitude) {
		
		groundLatitude = Math.toRadians(latitude);
		groundLongitude = Math.toRadians(longitude);
		groundHeading = Math.toRadians(heading);
		groundAltitude = altitude;
	}
	
	/**
	 * Calculates a heading in degrees using the current payload and ground station latitude and longitude. 
	 * These values will typically be passed into this method through the MySQLAccess object.
	 * This equation is pulled from the "Bearing" section of Movable Type Scripts: http://www.movable-type.co.uk/scripts/latlong.html
	 * 
	 * @param payloadLat Current payload latitude in degrees
	 * @param payloadLong Current payload longitude in degrees
	 * @return The heading of the payload relative to the base staion
	 */
	public double calculateHeading(double payloadLat, double payloadLong) {
		
		 payloadLat = Math.toRadians(payloadLat);
		 payloadLong = Math.toRadians(payloadLong);
		 
		 double y = Math.sin(payloadLong - groundLongitude) * Math.cos(payloadLat);
		 double x = (Math.cos(groundLatitude) * Math.sin(payloadLat)) - (Math.sin(groundLatitude) * Math.cos(payloadLat) * Math.cos(payloadLong - groundLongitude));
		 double heading = Math.toDegrees(Math.atan2(y, x));
		 
		if(heading < 0) {
			 heading += 360;
		 }

		 
		 return heading;
	}
	
	/**
	 * Outputs an angle and direction to turn in order to pan to a new heading.
	 * This heading is usually going to be for the payload's latitude and longitude.
	 * Uses dot product of unit vectors to calculate which direction it must turn.
	 * @param payloadLat Current payload latitude in degrees
	 * @param payloadLong Current payload longitude in degrees
	 * @return Positive (Clockwise) or Negative (Counter-Clockwise) angle to turn to match new heading
	 */
	public double turnToCoordinate(double payloadLat, double payloadLong) {
		
		double newHeading = Math.toRadians(calculateHeading(payloadLat,  payloadLong));
		double dotProductAngle = Math.acos((Math.cos(newHeading)*Math.cos(groundHeading)) + (Math.sin(newHeading)*Math.sin(groundHeading)));
		
		if(dotProductAngle == Math.abs(newHeading - groundHeading)) {
			
			dotProductAngle = Math.toDegrees(dotProductAngle);
			return dotProductAngle;
		}
		
		else {
			
			dotProductAngle = Math.toDegrees(dotProductAngle);
			return -dotProductAngle;
		}	
	}
	
	/**
	 * Outputs an angle of elevation between the ground and the payload, relative to the base station.
	 * Uses the haversine formula to determine what the distance along the ground is.
	 * @param payloadLat Current payload latitude in degrees
	 * @param payloadLong Current payload longitude in degrees
	 * @param altitude Current altitude of the payload in meters
	 * @return Angle of elevation of the payload relative to base station.
	 */
	public double calculateAngleOfElevation(double payloadLat, double payloadLong, double altitude) {
		
		payloadLat = Math.toRadians(payloadLat);
		payloadLong = Math.toRadians(payloadLong);
		
		double radiusEarth = 6371000; //in m
		double deltaLatitude = payloadLat - groundLatitude;
		double deltaLongitude = payloadLong - groundLongitude;
		
		double a = (Math.sin(deltaLatitude/2)*Math.sin(deltaLatitude/2)) + (Math.cos(groundLatitude) * Math.cos(payloadLat) * Math.sin(deltaLongitude/2) * Math.sin(deltaLongitude/2));		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = radiusEarth * c;
		double angleOfElevation = Math.atan((altitude - groundAltitude)/d);
		
		return Math.toDegrees(angleOfElevation);		
	}
}
