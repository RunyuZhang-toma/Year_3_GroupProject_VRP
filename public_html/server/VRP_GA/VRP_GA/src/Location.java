/*
 * Filename: Location.java
 * This java file is prepared by Team 2018-10 of COMP 2043.GRP.
 * University of Nottingham, Ningbo, China. 
 * The supervisor of Team-10 is prof. Ruibin BAI. 
 * The developer group consists six people, Runyu ZHANG, Qichen ZHANG, Yinglun LI, Huixing ZHANG, Zeyu ZHANG, and Yundan WANG. 
 * The project is No.14 UAV Patrol System.
 */
public class Location {
	
	public double longitude;
	public double latitude;

	public Location() {
		this.longitude = 0;
		this.latitude = 0;
	}
	
	public Location(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
}
