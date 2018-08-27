

import java.util.LinkedList;



class Vertex implements Comparable<Vertex>
{
	public final String name;
	//public Edge[] adjacencies;
	
	LinkedList<Edge> adjacencies = new LinkedList<Edge>();
	LinkedList<Edge> crossEdges = new LinkedList<Edge>();
	LinkedList<Vertex> adjV = new LinkedList<Vertex>();
	public boolean crossFlag = false;
	public int level = Integer.MAX_VALUE;
	public int demand = 0;
	public int vis=0;
	public LinkedList<Integer> inDemandFlows = new LinkedList<Integer>();
	public LinkedList<Integer> outDemandFlows = new LinkedList<Integer>();
	public boolean visit = false;
 	public Flow[] flows;
	public Double minDistance = 0.0;
	public int hopCount = 0;
	public int id;
	public Vertex previous;
	public Vertex(String argName, int argId) { name = argName; id = argId; }
	public String toString() { return name; }
	public int compareTo(Vertex other)
	{
		return Double.compare(minDistance, other.minDistance);
	}
}