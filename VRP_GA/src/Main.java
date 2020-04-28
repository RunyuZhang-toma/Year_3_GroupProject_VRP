/*
 * Filename: Main.java
 * This java file is prepared by Team 2018-10 of COMP 2043.GRP.
 * University of Nottingham, Ningbo, China. 
 * The supervisor of Team-10 is prof. Ruibin BAI. 
 * The developer group consists six people, Runyu ZHANG, Qichen ZHANG, Yinglun LI, Huixing ZHANG, Zeyu ZHANG, and Yundan WANG. 
 * The project is No.14 UAV Patrol System.
 */

public class Main extends VRP_GA{

	public Main(int VehicleNumber, int LocationNumber) {
		super(VehicleNumber, LocationNumber);
		
	}

	public static void main(String[] args) {
		
		vrp_method.vrpAlgorithm(args[1], popSize, popNumber, mutation, touranment_size);
	}

	public String vrp(String s) {
		
		return vrp_method.vrpAlgorithm(s, popSize, popNumber, mutation, touranment_size);
	}

}
