
import java.util.*;


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
	ArrayList<String> allRequests;
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
		sim.processInput();
		
		try {
		
		//for each request
		for (int k = 0; k < sim.allRequests.size(); k++) {
			String[] pickDrop = sim.allRequests.get(k).split(" ");
			int l = 0;
			while (true) {
				
				//check for traffic updates
				if (pickDrop[l].equals("-1")) {
					//process traffic updates
					String node = pickDrop[l+1];
					String dest = pickDrop[l+2];
					int cost = Integer.parseInt(pickDrop[l+3]);
					sim.g.setCost(node, dest, cost);
					if (l < (pickDrop.length-4))
					{
						l+=4;
					}
					else
					{
						break;
					}
				
				}
				
				// request
				String pick = pickDrop[l];
				String drop = pickDrop[l+1];
				System.out.println("client "+pick+" " +drop);
			
				// choose driver
				String home = sim.chooseDriver(pick, drop);

				// get output
				if (home != null) {
					sim.output(home, pick, drop);
				}
				if (l >= (pickDrop.length - 2))
				{
					break;
				}
				l+=2;
				}
			}
		
	}
			
		catch (NoSuchElementException e)
		{
			System.out.println("cannot be helped");
		}

			
		
		
		
	}
	

	 /**
     *Reads in input and constructs graph using specified nodes and edges. 
     *
     */ 
	public void processInput() {
		
		Scanner sc = new Scanner(System.in);
		//Scanner sc = new Scanner(test);
		int nodeNum = Integer.parseInt(sc.nextLine());
		String[] sourceDest = null;
		for (int i = 0; i<nodeNum; i++)
		{
			sourceDest = sc.nextLine().split(" ");
			String source = sourceDest[0];
			for(int j = 1; j<(sourceDest.length -1); j+=2)
			{
				//add edges for that node
				String destName = sourceDest[j];
				int cost = Integer.parseInt(sourceDest[j+1]);
				g.addEdge(source, destName, cost);
			}
		}
		int driverNum = Integer.parseInt(sc.nextLine());
		homeNodeNums = sc.nextLine().split(" ");
		int deliveryRequests = Integer.parseInt(sc.nextLine());
		allRequests = new ArrayList<String>();
		while (true)
		{
			String temp = sc.nextLine();
			if(temp.equals("#"))
			{
				break;
			}
			allRequests.add(temp);
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
						System.out.println("cannot be helped");
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
		
		// print output for each leg of trip
		System.out.println("truck " + home);
		g.dijkstra(home);
		int cost = (int)g.getCost(home, pick)*petrolFactor;
		g.printPath(pick, cost); //from home to pick up
		System.out.println("pickup " + pick);
		g.dijkstra(pick);
		cost = (int)g.getCost(pick, drop)*petrolFactor;
		g.printPath(drop, cost); //from pick up to drop off
		System.out.println("dropoff " + drop);
		g.dijkstra(drop);
		cost = (int)g.getCost(drop, home)*petrolFactor;
		g.printPath(home, cost); //from drop off to home
		
	}
	
	
	

}
