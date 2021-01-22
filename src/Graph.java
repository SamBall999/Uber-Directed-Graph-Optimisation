
import java.util.*;

/**
 * A Graph object represents all nodes and edges and allows methods to find the shortest path
 *
 *@author Samantha Ball
 *@version 1.0
 *@since 1.0
 */
public class Graph {
	
	public static final double INFINITY = Double.MAX_VALUE;
	private Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( ); 
	public int dist;

	
  	public Graph(String[] nodes) {

		//for each node
		for (int i = 0 ; i < nodes.length; i++) {

			String[] edges = nodes[i].split(" ");
			String source = edges[0];
			//for each edge attached to the node
			for(int j = 1; j< (edges.length-1); j+=2)
			{
				//check for blank line
				if (edges.length == 1) {
					break; //if no attached edges attached to the node
				}
				//add edges for that node
				String destName = edges[j];
				int cost = Integer.parseInt(edges[j+1]);
				addEdge(Integer.toString(i), destName, cost);
			}
		}
  	}
	

	private Vertex getVertex (String vertexName) {
		
		Vertex v = vertexMap.get(vertexName);
		if (v==null)
		{
			v = new Vertex(vertexName);
			vertexMap.put(vertexName, v);
			
		}
		return v;
	}
	
	/**
	 * Add new edge for the given source and destination vertices and weight
	 * 
	 * @param sourceName Name of start vertex 
	 * @param destName Name of end vertex
	 * @param cost Weight of edge 
	 */
	public void addEdge(String sourceName, String destName, double cost) {
	
		Vertex v = getVertex(sourceName);
		Vertex w = getVertex(destName);
		v.adj.add(new Edge(w, cost));
	}
	
	
	/**
	 * 
	 * @param vertName Name of start vertex
	 * @param dest Name of end vertex
	 * @param cost Cost to be added to original cost
	 */
	public void setCost(String vertName, String dest, double cost) {
		Vertex v = getVertex(vertName);
		v.setCost(dest, cost); 
	}
	
	/**
	 * Resets all vertices
	 */
	private void clearAll() {
		for (Vertex v: vertexMap.values()) {
			v.reset();
		}
	}
	
	
	private int printPath(Vertex dest, int cost) {
	if(dest.prev.size() > 1)
	{
	
		System.out.print("multiple solutions cost " + cost);
		return -1; 
	}
	else
	{
	if (dest.prev.size() == 1) {   
		int test = printPath(dest.prev.get(0), cost);  
		System.out.print(" ");
		if (test == -1) {
			return -1;
		}
	}
		System.out.print(dest.name);
		return 1;
	}
}
	
	/**
	 * Prints path to specified destination and determines whether there are multiple solutions
	 * 
	 * @param destName Name of destination vertex
	 * @param cost Value of full cost of path as previously computed
	 */
	public void printPath(String destName, int cost) {
	Vertex w = vertexMap.get( destName );
	if(w ==null)
	{
		throw new NoSuchElementException( );
	}
	else if( w.dist == INFINITY ) {
	}
	else
	{
		printPath(w, cost);
		System.out.println( );
		}
	}
	

	/**
	 * Computes the cost of a path from specified start to finish
	 * 
	 * @param start Name of start vertex
	 * @param dest Name of end vertex
	 * @return Double specifying total cost of path
	 */
	public double getCost(String start, String dest)
	{
		dijkstra(start);
		Vertex w = vertexMap.get(dest);
		if(w ==null)
		{
			throw new NoSuchElementException( );
		}
		else if( w.dist == INFINITY ) {
			
			return w.dist;
		}
		else
		{
			return w.dist;
	
		}
		
	}
		
	
	
	
	/**
	 * Computes shortest distances to all vertices starting from specified source node
	 * 
	 * @param startnode Source Vertex to compute distances from
	 */
	public void dijkstra (String startnode)
	{
		PriorityQueue<Path> pq = new PriorityQueue<Path>();
		Vertex start = vertexMap.get(startnode);
		if (start == null) {
			throw new NoSuchElementException("Start vertex not found");
		}
		clearAll();
		pq.add(new Path(start, 0));
		start.dist = 0;
		
		int nodesSeen = 0;
		while(!pq.isEmpty()&& nodesSeen < vertexMap.size()) {
			Path vrec = pq.remove();
			Vertex v = vrec.dest;
			if (v.scratch != 0)
				continue;
			
			v.scratch = 1;
			nodesSeen++;
			
			for(Edge e: v.adj)
			{
				Vertex w = e.dest;
				double cvw = e.cost;
				
				if (cvw < 0)
					throw new GraphException("Graph has negative edges");
				
				if (w.dist >= v.dist + cvw) //or equal to? //w.dist > v.dist + cvw
				{
					if (w.dist > v.dist + cvw) //if purely greater than, don't worry about multi path
					{
						w.dist = v.dist + cvw;
						//w.prev = v;  //w.prev = v; //make set
						w.clearPrevs();
						w.prev.add(v);
						pq.add(new Path(w, w.dist)); //where does this go now?
						//clear other vertices
					}
					else
					{
						w.prev.add(v); //if equal, add node to prev
						pq.add(new Path(w, w.dist)); //remove if bad
						
						
					}
				}
			}
		}
	}

}


	/**
	 * Used to signal violations of preconditions for various shortest path algorithms.
	 * 
	 * @author Samantha Ball
	 */
	class GraphException extends RuntimeException
	{
		public GraphException( String name ) { super(name ); }
	}



/**
 * An Edge object represents the directed link between vertices and its corresponding weight
 *
 *@author Samantha Ball
 *@version 1.0
 *@since 1.0
 */
class Edge {
	
	public Vertex dest; //second vertex in Edge 
	public double routeLength;
	public double cost; //Edge cost
	
	/**
	 * Creates a new Edge instance with specified destination Vertex and weight
	 * 
	 * @param d Vertex that edge points to
	 * @param c Value specifying weight of edge
	 */
	public Edge(Vertex d, double c)
	{
		dest = d;
		routeLength = c;
		cost = c;
	}
	
	
	/**
	 * Returns the name of the destination vertex that the Edge points towards
	 * 
	 * @return String value specifying destination that edge points towards
	 */
	public String getDest() 
	{
		return dest.name;
	}
	
	/**
	 * Alter cost of edge by adding traffic factor
	 * 
	 * @param c Value specifying traffic congestion on route
	 */
	public void addToCost(double c)
	{
		double traffic = c;
		cost = routeLength + traffic;
	}

}

/**
 * A Path object allows for comparison of different path lengths
 *
 *@author Samantha Ball
 *@version 1.0
 *@since 1.0
 */
class Path implements Comparable<Path> {
	
	public Vertex dest;
	public double cost;
	
	/**
	 * Creates a new Path instance with specified destination Vertex and weight
	 * 
	 * @param d Vertex that path points to
	 * @param c Value specifying weight of path
	 */
	public Path (Vertex d, double c) {
		dest = d;
		cost = c; 
	}
	
	/**
	 * Compares cost of paths 
	 */
	public int compareTo( Path rhs ) 
	{
		double otherCost = rhs.cost;
		return cost < otherCost ? -1 : cost > otherCost ? 1 : 0; 
	}

}
/**
 * A Vertex object represents a node in the graph and thus a location in the city
 *
 *@author Samantha Ball
 *@version 1.0
 *@since 1.0
 */
class Vertex {
	
	
	public String name;
	public List<Edge> adj;
	public double dist;
	public ArrayList<Vertex> prev; //make set
	public int scratch;
	
	/**
	 * Creates a new Vertex instance with specified name
	 * 
	 * @param nm String specifying name of vertex
	 */
	public Vertex(String nm)
	{
		name = nm;
		adj = new LinkedList<Edge>();
		reset();
	}
	
	/**
	 * Resets all distances to infinity and clears record of previous nodes
	 *
	 */
	public void reset() {
		dist = Graph.INFINITY;
		prev = new ArrayList<Vertex>();
		scratch = 0;	
	}
	
	/**
	 * Clears record of previous nodes
	 *
	 */
	public void clearPrevs() {
		prev = new ArrayList<Vertex>();
	}
	
	/**
	 * Identifies desired edge and modifies the weight of the edge by adding to the original value
	 * 
	 * @param dest String value specifying destination node of desired edge
	 * @param cost Value specifying traffic factor to add to weight of edge
	 */
	public void setCost(String dest, double cost)
	{
		Edge want = getEdge(dest);
		if (want!=null)
		{
			want.addToCost(cost);
		}
	}
	
	/**
	 * Returns Edge pointing to specified vertex
	 * 
	 * @param dest String specifying name of destination vertex corresponding to desired edge
	 * @return Edge that points to specified destination
	 */
	public Edge getEdge(String dest) 
	{
		Edge want = null; 
		for (Edge e: adj) {
			if (e.getDest().equals(dest))
			{
				want = e;
			}
		}
		return want;
		
	}

}
	

