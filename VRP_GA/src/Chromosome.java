/*
 * Filename: Chromosome.java
 * This java file is prepared by Team 2018-10 of COMP 2043.GRP.
 * University of Nottingham, Ningbo, China. 
 * The supervisor of Team-10 is prof. Ruibin BAI. 
 * The developer group consists six people, Runyu ZHANG, Qichen ZHANG, Yinglun LI, Huixing ZHANG, Zeyu ZHANG, and Yundan WANG. 
 * The project is No.14 UAV Patrol System.
 */
import java.util.Random;

public class Chromosome {
	
	
	double distance; //amount distance
	int[][] route; //record the order of each car
	
	
	public Chromosome() {
		
	}
	
	public Chromosome(int VehicleNumber, int LocationNumber) {
		
		
	}
	
	//clone a absolute same chromosome
	public Chromosome clone() {
		
		Chromosome copy = new Chromosome();

		copy.distance = this.distance;
		copy.route = new int[this.route.length][];
		for(int i = 0; i < this.route.length; i++) {
			copy.route[i] = new int[this.route[i].length];
			for(int j = 0; j < copy.route[i].length; j++) {
				copy.route[i][j] = this.route[i][j];
			}
		}
		
		return copy;
	}
	
	//detail functions in four mutation ways
	public Chromosome singleVehicleMutation() {
		int vehicle, location1, location2;
		int replace;
		Chromosome newOne = new Chromosome();
		newOne = this.clone();
		Random rand = new Random();
		
		vehicle = rand.nextInt(newOne.route.length);
		location1 = rand.nextInt(newOne.route[vehicle].length);
		location2 = rand.nextInt(newOne.route[vehicle].length);
		while(location1==location2) {
			
			location2 = rand.nextInt(newOne.route[vehicle].length);
		}
		replace = newOne.route[vehicle][location1];
		newOne.route[vehicle][location1] = newOne.route[vehicle][location2];
		newOne.route[vehicle][location2] = replace;
		return newOne;
	}
	
	public Chromosome doubleVehicleMutation() {
		int vehicle1, vehicle2, location1, location2;
		int replace;
		Chromosome newOne = new Chromosome();
		newOne = this.clone();
		Random rand = new Random();
		
		vehicle1 = rand.nextInt(newOne.route.length);
		vehicle2 = rand.nextInt(newOne.route.length);
		while(vehicle1 == vehicle2) {
			vehicle2 = rand.nextInt(newOne.route.length);
		}
		
		location1 = rand.nextInt(newOne.route[vehicle1].length);
		location2 = rand.nextInt(newOne.route[vehicle2].length);
		
		replace = newOne.route[vehicle1][location1];
		newOne.route[vehicle1][location1] = newOne.route[vehicle2][location2];
		newOne.route[vehicle2][location2] = replace;
		return newOne;
	}

	
	public Chromosome singleVehicleCut() {
		int size, vehicle, location1, location2;
		int replace;
		Chromosome newOne = new Chromosome();
		newOne = this.clone();
		Random rand = new Random();
		
		//size = 2 + rand.nextInt(3);
		vehicle = rand.nextInt(newOne.route.length);
		size = rand.nextInt(Math.max(2, newOne.route[vehicle].length/4));
		//vehicle = rand.nextInt(newOne.order.length);

		location1 = rand.nextInt(newOne.route[vehicle].length - size);
		location2 = rand.nextInt(newOne.route[vehicle].length - size);
		while(location1-size <= location2 && location2 <= location1+size) {
			location2 = rand.nextInt(newOne.route[vehicle].length - size);
		}
		
		for(int i = 0; i < size; i++) {
			replace = newOne.route[vehicle][location1+i];
			newOne.route[vehicle][location1+i] = newOne.route[vehicle][location2+i];
			newOne.route[vehicle][location2+i] = replace;
		}

		return newOne;
		
	}
	
	public Chromosome doubleVehicleCut() {
		int size, vehicle1, vehicle2, location1, location2;
		int replace;
		Chromosome newOne = new Chromosome();
		newOne = this.clone();
		Random rand = new Random();
		
		vehicle1 = rand.nextInt(newOne.route.length);
		vehicle2 = rand.nextInt(newOne.route.length);
		while(vehicle1 == vehicle2) {
			vehicle2 = rand.nextInt(newOne.route.length);
		}
		
		size = rand.nextInt(Math.max(2, newOne.route[vehicle1].length/4));
		
		location1 = rand.nextInt(newOne.route[vehicle1].length - size);
		location2 = rand.nextInt(newOne.route[vehicle2].length - size);
		
		for(int i = 0; i < size; i++) {
			replace = newOne.route[vehicle1][location1+i];
			newOne.route[vehicle1][location1+i] = newOne.route[vehicle2][location2+i];
			newOne.route[vehicle2][location2+i] = replace;
		}

		return newOne;
	}
	
	//single vehicle corssover
	public Chromosome singleCrossOver(Chromosome c) {
		int size, location;
		//int[] cross;
		Chromosome newOne = new Chromosome();
		newOne.route = new int[1][this.route[0].length];
		//random the father element and add to mother chromosome
		Random rand = new Random();
		size = rand.nextInt(this.route[0].length);
		location = rand.nextInt(this.route[0].length - size);
		
		for(int i = 0; i < size; i++) {
			newOne.route[0][location+i] = this.route[0][location+i];
			
		}
		
		for(int i = 0; i < newOne.route[0].length; i++) {
			for(int j = 0; j < c.route[0].length; j++) {
				if(i >= location && i < location+size) {
					continue;
				}
				if(ifexist(newOne.route[0], c.route[0][j])) {
					;
				}else {
					addgene(newOne.route[0], c.route[0][j]);
				}
			}
		}
		
		return newOne;
		
	}
	
	//test the test element if is in the route
	public boolean ifexist(int[] route, int test) {
		for(int i = 0; i < route.length; i++) {
			if(test == route[i]) {
				return true;
			}
		}
		return false;
	}
	
	//add the number into the route
	public void addgene(int[] route, int number) {
		for(int i = 0; i < route.length; i++) {
			if(route[i] == 0) {
				route[i] = number;
				return;
			}
		}
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getDistance() {
		return distance;
	}
	
}
