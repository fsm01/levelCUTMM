

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GetComputePaths {
	
	

	public List<Vertex> getShortestPathTo(Vertex source,Vertex target, double x, Edge[] edges)
	{
		List<Vertex> path = new ArrayList<Vertex>();
		Vertex temp;
		
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous){
		
			path.add(vertex);
		}
		

		Collections.reverse(path);
		return path;
	}


}
