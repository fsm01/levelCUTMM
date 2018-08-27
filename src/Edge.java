import java.util.ArrayList;
import java.util.LinkedList;





class Edge implements Comparable<Edge>
{
	public Dijkstra dj = new Dijkstra();
	public LinkedList<Integer> possibleFlow = new LinkedList<Integer>();
	public LinkedList<String> pairs = new LinkedList<String>();
	@SuppressWarnings("static-access")
	public double[][] rate = new double[dj.NODESIZE][dj.NODESIZE];
	public int totalPair = 0;
	public int possibleDemand;
	public int level = -1;
	public int tempLevel = -1;
	public final int sourceId;
	public final Vertex target;
	public final Vertex source;
	public final int targetId;
	public int capacity;
	public int capTemp;
	public double cost;
	public int totalCost = 0;
	public int flowCount = 0;
	public int hopCount = 0;
	public int s = 0;
	public int id;
	public boolean visit = false;
	@SuppressWarnings("static-access")
	public Edge(int argSourceId, int argTargetId, Vertex argSource,Vertex argTarget,int argWeight, int argId)
	{ 

		targetId = argTargetId;
		capacity = argWeight;
		cost = 1.0/argWeight;
		id = argId; 
		sourceId = argSourceId; 
		source = argSource;
		target = argTarget;
		capTemp = argWeight;
		
		for (int i = 0; i < dj.NODESIZE; i++) 
			for(int j = 0; j < dj.NODESIZE; j++)
				rate[i][j] = 1.0;
	}

	public int compareTo(Edge e){
		int c = e.capacity;

		if(this.capacity > c)
			return 1;
		else if(this.capacity == c)
			return 0;
		else 
			return -1;
	}
}