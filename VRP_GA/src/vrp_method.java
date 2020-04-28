import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * Filename: vrp_method.java
 * This java file is prepared by Team 2018-10 of COMP 2043.GRP.
 * University of Nottingham, Ningbo, China. 
 * The supervisor of Team-10 is prof. Ruibin BAI. 
 * The developer group consists six people, Runyu ZHANG, Qichen ZHANG, Yinglun LI, Huixing ZHANG, Zeyu ZHANG, and Yundan WANG. 
 * The project is No.14 UAV Patrol System.
 */

public class vrp_method {

	//details functions to build distance matrix
	public static double[][] calculateDistance(Location[] locationSet) {
		
		int i,j;
		
		double[][] distanceMatrix = new double[VRP_GA.LocationNumber][VRP_GA.LocationNumber];
		
		for(i = 0; i < VRP_GA.LocationNumber; i++) {
			for(j = i; j < VRP_GA.LocationNumber; j++) {
				
				if(j == i) {
					distanceMatrix[i][i] = 0;
				}
				
				distanceMatrix[i][j] = 
						Math.sqrt((locationSet[i].latitude-locationSet[j].latitude)*111320*111320*(locationSet[i].latitude-locationSet[j].latitude)+(locationSet[i].longitude-locationSet[j].longitude)*100000*100000*(locationSet[i].longitude-locationSet[j].longitude));
				distanceMatrix[j][i] = distanceMatrix[i][j];
				
			}
		}
		
		return distanceMatrix;	
	}

	
	//calculate the chromosome fitness means the all vehicles and locations distance
	public static double calFit(Chromosome c) {
		double fitness = 0;
		int i = 0,j = 0;

		for(; i < c.route.length; i++) { //select the vehicle
			for(j = 1; j < c.route[i].length; j++) { //select the location
				fitness = fitness + VRP_GA.distanceMatrix[c.route[i][j-1]][c.route[i][j]];
			}
			
			//add distance to start location
			fitness = fitness + VRP_GA.distanceMatrix[0][c.route[i][0]] + VRP_GA.distanceMatrix[0][c.route[i][c.route[i].length-1]];
		}
		
		return fitness;
	}

	//test if the Location and Vehicle number are over the MAX recommend
	public static boolean test() {

		return (VRP_GA.LocationNumber < VRP_GA.MAX_LOCATION && VRP_GA.VehicleNumber < VRP_GA.MAX_VEHICLE? true: false);
		
	}
	
	//transfer the info to Json string
	public static String transferJson(Chromosome c) {
		JSONArray jsonArray = new JSONArray();
		JSONObject json1 = new JSONObject();
		json1.accumulate("distance", c.distance);
		jsonArray.add(json1);
		
		JSONObject json;
		for(int i = 0; i < c.route.length; i++) {
			json = new JSONObject();
			for(int j = 0; j < c.route[i].length; j++) {
				String str = String.valueOf(j);
				json.accumulate(str, c.route[i][j]);
			}
			jsonArray.add(json);
		}
		
		return jsonArray.toString();
	}
	
	//the back end used interface
	public static String vrpAlgorithm(String s, int popSize, int popNumber,double mutationRate, int tournament_size) {
		
		Chromosome c;
		
		try {
			
			//get basic info by object
			JsonParser parser = new JsonParser();
			c = new Chromosome();
			JsonObject object = (JsonObject)parser.parse(s);
			VRP_GA.VehicleNumber = object.get("Vehicle").getAsInt();
			VRP_GA.LocationNumber = object.get("Location").getAsInt();
			//System.out.println(VehicleNumber + " " + LocationNumber);
			VRP_GA.LocationSet = new Location[VRP_GA.LocationNumber];
			
			//constructor for algotrithm
			VRP_GA vrp = new VRP_GA(VRP_GA.VehicleNumber, VRP_GA.LocationNumber);
			
			//get location info
			JsonArray array = object.get("Locations").getAsJsonArray();
			for(int i = 0; i < VRP_GA.LocationNumber; i++) {
				VRP_GA.LocationSet[i] = new Location();
				JsonObject subObject = array.get(i).getAsJsonObject();
				
				VRP_GA.LocationSet[i].latitude = subObject.get("lat").getAsDouble();
				VRP_GA.LocationSet[i].longitude = subObject.get("lng").getAsDouble();
			}

			vrp.initial(popSize); //get ordered population
			vrp.selectMode(popSize, popNumber, mutationRate, tournament_size);
			
			c = VRP_GA.population.get(0);
			return vrp_method.transferJson(c);
		}catch(JsonIOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
