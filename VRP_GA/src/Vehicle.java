//This program is written by Runyu Zhang in 2018-2019 UNNC Year3 GRP project Team 10

public class Vehicle {
	
	public String name;
	public int VehicleNumber;
	public double battery;
	public int locations;
	
	public int[] order;
	
	public Vehicle() {
		;
	}
	
	public Vehicle(int VehicleNumber) {
		this.VehicleNumber = VehicleNumber;
	}
	
	public Vehicle(int VehicleNumber, double battery, int location) {
		this.VehicleNumber = VehicleNumber;
		this.battery = battery;
		this.locations = location;
	}
	
	public Vehicle(int VehicleNumber, int location) {
		this.VehicleNumber = VehicleNumber;
		this.locations = location;
	}
	
}
