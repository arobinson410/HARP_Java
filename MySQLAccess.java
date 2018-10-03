import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLAccess {
	
	private String IMEI = "300234064904600";

	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	public String gps_fltDate = "0000-00-00", gps_time = "00:00:00";
	public double gps_lat = 0, gps_long = 0, gps_alt = 0;
	
	public MySQLAccess() {
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection("jdbc:mysql://eclipse.rci.montana.edu:3306/freemanproject", "antenna", "tracker");
			st = con.createStatement();
			
		}
		catch(Exception ex) {
			
			System.out.println("Error: " + ex);	
		}
	}
	
	public void getPayloadTelemetry() {
		try {
			
			String query = "SELECT gps_fltDate, gps_time, gps_lat, gps_long, gps_alt FROM gps WHERE gps_IMEI = '"+ IMEI +"' ORDER BY pri_key DESC LIMIT 1";
			rs = st.executeQuery(query);
			
			while(rs.next()) {
				
				gps_fltDate = rs.getString("gps_fltDate");
				gps_time = rs.getString("gps_time");
				gps_lat = Double.parseDouble(rs.getString("gps_lat"));
				gps_long = Double.parseDouble(rs.getString("gps_long"));
				gps_alt = Double.parseDouble(rs.getString("gps_alt"));
				
				System.out.println(gps_fltDate + "\t" + gps_time + " UTC\t" + gps_lat + "\u00b0\t" + gps_long + "\u00b0\t" + gps_alt + " m");
			}
			
		}
		catch(Exception ex) {
			
			System.out.println("Error: " + ex);
		}	
	}
}
