import java.util.ArrayList;


public class PathKeepForPre implements Comparable<PathKeepForPre>{
	
	int index;
	int pathBW;
	int pathBWTemp;
	ArrayList<Vertex> path;
	int flowId;
	double lastUT;
	boolean es = false;
	int chosen = 0;
	ComputePaths pat = new ComputePaths();
	Dijkstra dr = new Dijkstra();
	double pathBWCopare;
	public PathKeepForPre(int index, int pathBW, ArrayList<Vertex> path){
		this.index = index;
		this.path = path;
		this.pathBW = pathBW;
		this.pathBWTemp = pathBW;
	}
	Flow f;

	@Override
	public int compareTo(PathKeepForPre o) {
	double max = o.pathBWCopare;
		
		if(this.pathBWCopare > max)
			return 1;
		else if(this.pathBWCopare == max)
			return 0;
		else
			return -1;
	
/*		long max = 0;
		long max2 = 0;
		for(int i = 0; i<this.path.size()-1; i++){
			@SuppressWarnings("static-access")
			Edge e = pat.findEdge(this.path.get(i).id, this.path.get(i+1).id, dr.edges);
			int x = 0;
			if(e.capacity == 0)
				x = Integer.MAX_VALUE;
			else
				x = 1/e.capacity;
			max += x;
			//System.out.println("ELLAS " + e.capacity);
		}
		for(int i = 0; i<o.path.size()-1; i++){
			@SuppressWarnings("static-access")
			Edge e = pat.findEdge(o.path.get(i).id, o.path.get(i+1).id, dr.edges);
			int x = 0;
			if(e.capacity == 0)
				x = Integer.MAX_VALUE;
			else
				x = 1/e.capacity;
			max2 += x;
			//System.out.println("ELLAS " + e.capacity);
		}
		
		if(max > max2)
			return 1;
		else if(max == max2)
			return 0;
		else
			return -1;*/
	}

	
}
