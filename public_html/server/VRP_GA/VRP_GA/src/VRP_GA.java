/*
 * Filename: VRP_GA.java
 * This java file is prepared by Team 2018-10 of COMP 2043.GRP.
 * University of Nottingham, Ningbo, China. 
 * The supervisor of Team-10 is prof. Ruibin BAI. 
 * The developer group consists six people, Runyu ZHANG, Qichen ZHANG, Yinglun LI, Huixing ZHANG, Zeyu ZHANG, and Yundan WANG. 
 * The project is No.14 UAV Patrol System.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONObject; 
import net.sf.json.JSONArray;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VRP_GA {

	//used to constrain the program
	public final static int MAX_LOCATION = 1000;
	public final static int MAX_VEHICLE = 10;
	public final static int LOOP = 10;
	public final static int MAX = 1000000;
	public static int popSize = 20;
	public static int popNumber = 5;
	public static int touranment_size = 3;
	public static double mutation = 0.1;
	public static int[] SEED = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
	
	public static int LocationNumber;
	public static int VehicleNumber;
	public double shortestDistance;
	public static double[][] distanceMatrix;
	public static Location[] LocationSet;
	public static List<Chromosome> population;
	
	//constructor only used for VehicleNumber and LocationNumber
	public VRP_GA(int VehicleNumber, int LocationNumber) {
	
		VRP_GA.VehicleNumber = VehicleNumber;
		VRP_GA.LocationNumber = LocationNumber;
		
		VRP_GA.distanceMatrix = new double[LocationNumber][LocationNumber];
		population = new ArrayList<Chromosome>();
	}

	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String s = "{\"Vehicle\":1, \"Location\": 21, \"Locations\":[{\"lng\":121.562965,\"lat\":29.800722},{\"lng\":121.562416,\"lat\":29.800007},{\"lng\":121.561479,\"lat\":29.799629},{\"lng\":121.561301,\"lat\":29.798984},{\"lng\":121.562222,\"lat\":29.798213},{\"lng\":121.563966,\"lat\":29.798634},{\"lng\":121.562706,\"lat\":29.797134},{\"lng\":121.564354,\"lat\":29.798255},{\"lng\":121.565306,\"lat\":29.79904},{\"lng\":121.564757,\"lat\":29.799573},{\"lng\":121.564386,\"lat\":29.800315},{\"lng\":121.563837,\"lat\":29.801212},{\"lng\":121.562884,\"lat\":29.801745},{\"lng\":121.561479,\"lat\":29.802249},{\"lng\":121.560349,\"lat\":29.800876},{\"lng\":121.55946,\"lat\":29.798746},{\"lng\":121.559848,\"lat\":29.799671},{\"lng\":121.560171,\"lat\":29.798465},{\"lng\":121.560962,\"lat\":29.797653},{\"lng\":121.561624,\"lat\":29.797022},{\"lng\":121.562254,\"lat\":29.796573}]}";
		String s1 = "{\"Vehicle\":1, \"Location\": 30, \"Locations\":[{\"lng\":121.562609,\"lat\":29.797106},{\"lng\":121.56156,\"lat\":29.797737},{\"lng\":121.560736,\"lat\":29.798129},{\"lng\":121.559993,\"lat\":29.79904},{\"lng\":121.560284,\"lat\":29.799657},{\"lng\":121.560494,\"lat\":29.800988},{\"lng\":121.561108,\"lat\":29.801647},{\"lng\":121.562416,\"lat\":29.801464},{\"lng\":121.562868,\"lat\":29.802081},{\"lng\":121.563578,\"lat\":29.801689},{\"lng\":121.565161,\"lat\":29.801128},{\"lng\":121.564499,\"lat\":29.800848},{\"lng\":121.56374,\"lat\":29.800848},{\"lng\":121.562432,\"lat\":29.800539},{\"lng\":121.562383,\"lat\":29.799937},{\"lng\":121.56227,\"lat\":29.79932},{\"lng\":121.562432,\"lat\":29.79876},{\"lng\":121.56269,\"lat\":29.798171},{\"lng\":121.563756,\"lat\":29.798045},{\"lng\":121.564176,\"lat\":29.797583},{\"lng\":121.563692,\"lat\":29.797078},{\"lng\":121.562949,\"lat\":29.796153},{\"lng\":121.563659,\"lat\":29.796377},{\"lng\":121.56466,\"lat\":29.796868},{\"lng\":121.565129,\"lat\":29.797611},{\"lng\":121.565839,\"lat\":29.798227},{\"lng\":121.566356,\"lat\":29.79918},{\"lng\":121.565339,\"lat\":29.799713},{\"lng\":121.564547,\"lat\":29.799559},{\"lng\":121.563934,\"lat\":29.79911}]}";
		//String s = vrpAlgorithm(args[1], 10, 10, 0.01, 3);
		String res = vrp_method.vrpAlgorithm(s1, popSize, popNumber, mutation, touranment_size);
		System.out.println(res);
		System.out.println(System.currentTimeMillis() - start); 
		
		for(int i = 0; i < 15; i++) {
			res = vrp_method.vrpAlgorithm(s1, popSize, popNumber, mutation, touranment_size);
			System.out.println(res);
			System.out.println(System.currentTimeMillis() - start); 
			
			
		}
	}

	
	//mode select function
	public void selectMode(int popSize, int popNumber, double mutationRate, int tournament_size) {
		if(VehicleNumber == 1 && LocationNumber <= 50) {
			
			mode1(popSize, popNumber, mutationRate, tournament_size);
			
		}
		if(VehicleNumber == 2 && LocationNumber < 50) {
			//mode2();
		}
		if(VehicleNumber == 1 && LocationNumber > 50) {
			//mode3();
		}
		else {
			//modeL();
		}
		return;
	}
	
	//details implement of different mode
	public void mode1(int popSize, int popNumber, double mutationRate, int tournament_size) {
		
		Chromosome newOne;

		for(int i = 0; i < popNumber; i++) { //determine the generation
			for(int j = 0; j < popSize; j++) { //determine the size of each generation
				
				newOne = selection(tournament_size);
				newOne.distance = vrp_method.calFit(newOne);
//				for(int m = 0; m < newOne.route[0].length; m++) {
//					System.out.print(newOne.route[0][m] + " ");
//				}
//				System.out.println();
//				System.out.println(newOne.distance);
				population.add(newOne);

			}
			
			//order the population
			orderPop(popSize);
			
			
			
			//deletePop(geneSize);
			removePop(popSize);

			
		}
		
	}
	
	
	//used to sort by fitness
	public void orderPop(int geneSize) {
		
		int next = 0;
		if(population.size() < geneSize) {
			geneSize = population.size();
		}
		double min = population.get(next).getDistance();
		for(int i = 0; i < geneSize; i++) {
			for(int j = i; j < population.size(); j++) {
				if(min > population.get(j).getDistance()) {
					next = j;
					min = population.get(j).getDistance();
				}
			}
			
			population.add(i, population.get(next));
			population.remove(next+1);
		}
			
	}
	
	//used to delete the same chromosome
	public void deletePop(int geneSize) {
		if(population.size() < geneSize) {
			geneSize = population.size();
		}
		int i, j;
		i = 0;
		j = i+1;
		while(i< population.size()) {
			while(j < population.size()) {
				if(population.get(i).distance == population.get(j).distance) {
					population.remove(j);
				}else {
					j++;
				}
			}
			i++;
			j = i+1;
		}
	}
	
	//remove the chromosome more than the popSize
	public void removePop(int geneSize) {
		while(population.size()>geneSize) {
			population.remove(population.size()-1);
		}
	}

	
	//init dataset after explaining the json string
//	public void init() {
//		distanceMatrix = new double[LocationNumber][LocationNumber];
//		distanceMatrix = vrp_method.calculateDistance(LocationSet);
//		Chromosome c = new Chromosome(VehicleNumber, LocationNumber);
//		c.setDistance(vrp_method.calFit(c));
//		population.add(c);
//		shortestDistance = population.get(0).getDistance();
//
//	}
	
	//initial the population(FOR 1 VEHICLE)
	public void initial(int popSize) {
		distanceMatrix = vrp_method.calculateDistance(LocationSet);
		Chromosome c;
		Random rand = new Random(SEED[1]);
		double[] randNumber = new double[LocationNumber-1];
		for(int i = 0; i < LocationNumber-1; i++) {
			randNumber[i] = rand.nextDouble();
		}
		//System.out.println();
		int record = 0;
		for(int i = 0; i <popSize; i++) {
			for(int j = 0; j < LocationNumber-1; j++) {
				randNumber[j] = rand.nextDouble();
			}
			c = new Chromosome(1, LocationNumber-1);
			c.route = new int[1][LocationNumber-1];
			for(int m = 0; m < LocationNumber-1; m++) {
				record = 1;
				for(int n = 0; n < LocationNumber-1; n++) {
					if(randNumber[m] > randNumber[n]) {
						record++;
					}
				}
				
				c.route[0][m] = record;
				
			}
//			for(int j = 0; j < c.route[0].length; j++) {
//				System.out.print(c.route[0][j] + " ");
//			}
			c.distance = vrp_method.calFit(c);
//			System.out.println();
//			System.out.print(c.distance);
			
			population.add(c);
			
		}
		
		
		orderPop(popSize);
	}

	//this method is used to select the parent chromosome and return the crossovered chromosome
	public Chromosome selection(int tournament_size) {
		Chromosome fa, mo;

		ArrayList<Chromosome> pop = new ArrayList<Chromosome>();
		Random rand = new Random(SEED[1]);
		for(int i = 0; i < tournament_size; i++) {
			pop.add(population.get(rand.nextInt(population.size())));
			
		}

		fa = pop.get(0);
		for(int i = 1; i < tournament_size; i++) {
			fa = (pop.get(i).distance < fa.distance)?pop.get(i):fa;
		}

		for(int i = 0; i < tournament_size; i++) {
			pop.remove(0);
		}
		
		for(int i = 0; i < tournament_size; i++) {
			pop.add(population.get(rand.nextInt(population.size())));
			pop.get(i).distance = vrp_method.calFit(pop.get(i));
			
		}
		mo = pop.get(0);
		for(int i = 1; i < tournament_size; i++) {
			mo = (pop.get(i).distance < mo.distance)?pop.get(i):mo;
		}
		for(int i = 0; i < tournament_size; i++) {
			pop.remove(0);
		}

		return fa.singleCrossOver(mo);
	}

}
