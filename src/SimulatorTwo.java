
import java.util.*;


//Fix petrol factor
//Visualisation
//Save current graph and driver information and load as default next time
//Fix hash symbol


/** 
 *Prints information regarding an UberHaul service to transport materials. 
 *
 *<p>
 *Finds the cheapest path taking into account length of route, petrol consumption of truck and traffic along the route.   
 *The length of the route is represented by the original weights the graph is constructed with. 
 *The petrol consumption is based on the size of the truck and is taken into account using a multiplication factor.
 *Traffic reports are interspersed with client requests and thus update the weights along the edges before the next client request. 
 *</p>
 *@author Samantha Ball
 *@version 1.0
 *@since 1
 */
public class SimulatorTwo {
	
	Graph g;
	String[] homeNodeNums;
	String[] petrolFactors;
	//ArrayList<String> allRequests;
	int petrolFactor;
	//String test = "8\n0 1 2 2 5\n1 0 3 3 4\n2 5 9 6 4 7 17\n3 1 7 4 5\n4 2 11\n5 3 9\n6 5 5\n7\n3\n0 3 4 2 6 3\n3\n2 5 5 1 3 7\n#";
	
	 /**
     *Prints information regarding cheapest route, truck chosen and route for each segment of the journey, including from the driver's home to pick up, pick up to drop off and drop off to home.
     *
     *<p>
     *For each homeNodeNums array, checks if the next value is -1, signalling a traffic update. 
     *If the next value is -1, the next three values specify the traffic request in the format <startNode><endNode><newWeight>
     */
	public static void main (String[] args) {
		
	
		
		SimulatorTwo sim = new SimulatorTwo();
		sim.g = new Graph( ); 
		sim.createInterface();	//interface needs to be continuous	
	
		
	}


	 /**
     *Reads in input and constructs graph using specified nodes and edges. 
     *
     */ 
	public void getRoadNetworkInput() {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Road Network Construction");
		System.out.println("Please enter the number of locations (nodes) in the road network");
		int nodeNum = Integer.parseInt(sc.nextLine());
		String[] route_weights = null;
		for (int i = 0; i<nodeNum; i++)
		{
			System.out.println("Please enter the route weights from source node " + i + " to other adjoining nodes in the format: <destination node number> <weight> <destination node number> <weight> ...");
			route_weights = sc.nextLine().split(" ");
			for(int j = 0; j<(route_weights.length); j+=2)
			{
				//check for blank line
				if (route_weights.length == 1) {
					break; //if no attached nodes
				}
				//add edges for that node
				String destName = route_weights[j];
				int cost = Integer.parseInt(route_weights[j+1]);
				g.addEdge(Integer.toString(i), destName, cost);
			}
		}
	}


	 /**
     *Reads in input regarding number of available drivers and their respective home nodes and truck sizes. 
     *
     */ 
	public void getDriverInformation() {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Available Truck Driver Information");
		System.out.println("Please enter the number of available UberHaul drivers");
		int driverNum = Integer.parseInt(sc.nextLine());
		System.out.println("Please enter the node numbers representing the home node of each driver in the format: <driver_0 home node number> <driver_1 home node number> <driver_2 home node number> ...");
		homeNodeNums = sc.nextLine().split(" ");
		System.out.println("The petrol consumption of each truck is dependent on truck size which is represented on a scale from 1-4:");
		System.out.println("1 - S");
		System.out.println("2 - M");
		System.out.println("3 - L");
		System.out.println("4 - XL");
		System.out.println("Please enter the truck sizes (1-4) affecting the petrol consumption of each driver in the format: <driver_0 truck_size> <driver_1 truck_size> <driver_2 truck_size> ...");
		petrolFactors = sc.nextLine().split(" ");
	}


	 /**
     *Reads in input regarding delivery requests. 
     *
     */ 
	public void makeDeliveryRequests() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Make Delivery Request");
		System.out.println("Please enter the number of delivery requests");
		int deliveryRequests = Integer.parseInt(sc.nextLine());
		System.out.println("Please enter the delivery request source and destination nodes in the format: <pick-up node number> <drop-off node number> <pick-up node number> <drop-off node number> <pick-up node number> <drop-off node number>... #");
		ArrayList<String> allRequests = new ArrayList<String>();
		while (true)
		{
			String temp = sc.nextLine();
			if(temp.equals("#"))
			{
				break;
			}
			allRequests.add(temp);
		}
		for (int k = 0; k < allRequests.size(); k++) {
			String[] pickDrop = allRequests.get(k).split(" ");
			int l = 0;
			while (true) {
				// request
				String pick = pickDrop[l];
				String drop = pickDrop[l+1];

				// choose driver
				String home = chooseDriver(pick, drop);

				// get output
				if (home != null) {
					output(home, pick, drop);
				}
				if (l >= (pickDrop.length - 2)) {
					break;
				}
				l+=2;
			}
		}
	
	}

	 /**
     *Reads in input and processes changes to graph edge weights according to traffic updates. 
     *
     */ 
	public void processTrafficUpdates() {

		Scanner sc = new Scanner(System.in);
		System.out.println("Report Traffic Update"); 
		System.out.println("The effect of traffic is represented on a scale from 1-5:");
		System.out.println("1 - Light");
		System.out.println("2 - Moderate");
		System.out.println("3 - Congested");
		System.out.println("4 - Heavy");
		System.out.println("5 - Traffic jam");
		System.out.println("Please enter the traffic updates in the format: <source_node> <destination_node> <traffic_factor> <source_node> <destination_node> <traffic_factor> <source_node> <destination_node> <traffic_factor> ...");
		//process traffic updates
		String[] trafficUpdates = sc.nextLine().split(" ");
		for(int i = 0; i < trafficUpdates.length/3; i+=3) {
			String node = trafficUpdates[i];
			String dest = trafficUpdates[i+1];
			int cost = Integer.parseInt(trafficUpdates[i+2]);
			g.setCost(node, dest, cost);
		}
	}


	 /**
     *Generates user interface for UberHaul program. 
     *
     */ 
	public void createInterface() {
		
		System.out.println("\n----- Welcome to the UberHaul Service -----");
		int optionNum = 6;
		while(optionNum!=5)
		{
			System.out.println("\nPlease choose your required action by entering the corresponding number:");
			System.out.println("1. Change road network graph");
			System.out.println("2. Modify number of available drivers and their current locations");
			System.out.println("3. Make a delivery request");
			System.out.println("4. Update routes based on traffic report");
			System.out.println("5. Quit");

			//get input
			Scanner sc = new Scanner(System.in);
			optionNum = Integer.parseInt(sc.nextLine());

			switch(optionNum) {

					case 1:
						getRoadNetworkInput();
						break;
				
					case 2:
						getDriverInformation();
						break;

					case 3: 
						makeDeliveryRequests();
						break;
				
					case 4:
						processTrafficUpdates();
						break;
				
					case 5:
						System.exit(0); //quit
						break;

					default: 
						System.out.println("Invalid option chosen");
			}

		}	
		
	}
	
	
	/**
     *Finds the cheapest driver for the specified route, taking into account the journey from the driver's home and back, as well as the size of the truck. 
     *
     *@param pick String specifying name of node which client is fetched from
     *@param drop String specifying name of node to drop client off at
     *@return Returns a String specifying the home node of the chosen driver
     */ 
	public String chooseDriver(String pick, String drop) {

					System.out.println("\nClient requesting service from node "+pick+" to node to " +drop);
					// choose driver
					ArrayList<Double> drivers = new ArrayList<Double>();
					// split into two arrays
					String[] driverIDs = new String[(homeNodeNums.length)/2];
					String[] petrolFactors = new String[(homeNodeNums.length)/2];
					for (int s = 0; s < driverIDs.length; s++){
						driverIDs[s] = homeNodeNums[2*s];
						petrolFactors[s] = homeNodeNums[2*s+1];
						
					}
					
					for (int l = 0; l < driverIDs.length; l++) 
					{
						// if infinity, go to next driver
						if( g.getCost(driverIDs[l], pick) == g.INFINITY | g.getCost(pick, drop) == g.INFINITY | g.getCost(drop, driverIDs[l])== g.INFINITY) 
						{
							continue;
						}
						petrolFactor = Integer.parseInt(petrolFactors[l]);
						double totalCost = (g.getCost(driverIDs[l], pick) + g.getCost(pick, drop) + g.getCost(drop, driverIDs[l]))*petrolFactor;	
						drivers.add(totalCost);
						
					
					}
					// if no possible route or driver
					if (drivers.isEmpty())
					{
						System.out.println("Unfortunately the client cannot be helped as there is no possible route or \ndriver.");
						return null;
					}
					// find best driver
					double min = Collections.min(drivers);
					ArrayList<String> possibles = new ArrayList<String>();
					for(int m = 0; m < drivers.size(); m++) {
						if(drivers.get(m)==min)
						{
							possibles.add(driverIDs[drivers.indexOf(drivers.get(m))]); 
						}	
					}
					
					//choose driver with lowest node number
					String home = Collections.min(possibles);
					//get petrol factor for that driver
					for (int t=0; t<driverIDs.length; t++) {
						if(driverIDs[t]== home) {
							petrolFactor = Integer.parseInt(petrolFactors[t]);
						}
					}
					return home;
					
	}
	
	/**
     *Prints the truck chosen, the path taken for each leg of the trip and whether there are multiple possible routes
     *
     *@param home String specifying name of node where driver is stationed and returns to
     *@param pick String specifying name of node which client is fetched from
     *@param drop String specifying name of node to drop client off at
     */
	public void output(String home, String pick, String drop) {
		
		int total_cost = 0;
		// print output for each leg of trip
		System.out.println("- Most suitable truck driver is stationed at node " + home);
		g.dijkstra(home);
		int cost = (int)g.getCost(home, pick)*petrolFactor;
		total_cost += cost;
		System.out.print("- Optimal path from driver home to pick up node: ");
		g.printPath(pick, cost); //from home to pick up
		g.dijkstra(pick);
		cost = (int)g.getCost(pick, drop)*petrolFactor;
		total_cost += cost;
		System.out.print("- Optimal path from pick up node to drop off node: ");
		g.printPath(drop, cost); //from pick up to drop off
		g.dijkstra(drop);
		cost = (int)g.getCost(drop, home)*petrolFactor;
		total_cost += cost;
		System.out.print("- Optimal path from drop off node back to driver home node: ");
		g.printPath(home, cost); //from drop off to home
		System.out.println("Total trip cost " + total_cost);
		
	}
	
	
	

}
