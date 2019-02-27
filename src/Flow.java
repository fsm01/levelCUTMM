import java.util.ArrayList;
import java.util.LinkedList;



class Flow implements Comparable<Flow>
{
	public Vertex source;
	public Vertex target;
	public int cost;
	public int id;
	public LinkedList<Vertex> targetList = new LinkedList<Vertex>();
	public ArrayList<Vertex> path = new ArrayList<Vertex>();
	
	public double lastUT = 0.0;
	public double lastDL = 0.0;
	
	@SuppressWarnings("unchecked")
	public ArrayList<Vertex>[] dspPaths = new ArrayList[10];
	public Flow(Vertex argSource, Vertex argTarget, int argCost, int id)
	{ 
		source = argSource;
		target = argTarget; 
		cost = argCost;
		this.id = id;
		this.targetList.add(argSource);
	}
	
	public int compareTo(Flow o) {
		// TODO Auto-generated method stub
		
		if(this.cost > o.cost )
			return 1;
		else if(this.cost == o.cost)
			return 0;
		else
			return -1;
	}
}