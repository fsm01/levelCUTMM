

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;

public class ComputePaths {

	public static int maxOfFlow = 0;
	public static int minEdgeInPath = Integer.MAX_VALUE;
	public static ArrayList<Vertex> pathArray = new ArrayList<Vertex>();
	public static int totPath = 0;
	public static int maxOfMax = 0;
	public static LinkedList<Edge> pathEdges = new LinkedList<Edge>();
	@SuppressWarnings({ "static-access", "unused" })
	public static int hopCount(Vertex source, Vertex target, LinkedList<Edge> edges, Vertex[] vertices){
		int hopCount = 0;

		PriorityQueue<Vertex> Q = new PriorityQueue<Vertex>();

		source.minDistance = 0.0;

		//System.out.println(source + " " + target);
		Q.add(source);
		while(!Q.isEmpty()){
			Vertex u = Q.remove();
			//System.out.println(Q);
			//u.visit = true;
			//	System.out.println(" " + u);
			if(u.adjacencies != null){

				for(Edge e: u.adjacencies){

					Vertex v = e.target;

					//if(v.visit == false){

					double min = u.minDistance + 1;

					if(v.minDistance > min){
						v.minDistance = min;
						v.hopCount = (int)min;
						v.previous = u;
						Q.add(v);
					}	

				}
				//}
			}
		}
		hopCount =target.hopCount;
		return hopCount;
	}

	public static void computeLevelWithHopCount(Vertex source, Vertex target, LinkedList<Edge> edges, Vertex[] vertices){

		PriorityQueue<Vertex> Q = new PriorityQueue<Vertex>();

		Q.add(source);
		source.level = 0;
		while(!Q.isEmpty()){

			Vertex u = Q.poll();
			u.visit = true;
			if(u.adjacencies != null){

				for(Edge e: u.adjacencies){

					Vertex v = e.target;
					//			System.out.println(u.hopCount + " " + v.hopCount);
					if(v.hopCount > u.hopCount /*&& v.hopCount <= target.hopCount*/){
						v.level = u.level+1;
						e.level = u.level;
						Q.add(v);
					}else if(v.hopCount == u.hopCount){
						v.level = u.level;
						e.level = -1;
					}/*else if(u.hopCount > v.hopCount){
							u.level = v.level;
							e.level = -1;
						}*//*else if(v.hopCount > target.hopCount){
							e.level = -1;
							v.level = u.level;
						}*/

					//	System.out.println(u.level + " " + v.level + " " + e.level);
				}

			}
		}
	}


	public static int computeLevel(Vertex source, Vertex target, LinkedList<Edge> edges, LinkedList<edgesOfLevels> edgesLevel,LinkedList<edgesOfLevels> edgesLevelTemp) //source coklu gonderimde lazim olabilir
	{
		Dijkstra dj = new Dijkstra();
		int retLev = 0;
		int max = 0;
		//PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		Queue<Vertex> vertexQueue = new LinkedList<Vertex>();
		//target.level = 0;
		target.level = 0;
		int k = 0;
		vertexQueue.add(target);
		Vertex temp = null;

		while (!vertexQueue.isEmpty()) {

			Vertex u = vertexQueue.poll();
			LinkedList<Edge> listOfAdj = dj.findEdgeOfTarget(u.id,edges);
			max = u.level;
			//System.out.println("EEL " + u.id + " as " + u.level);
			if(!listOfAdj.isEmpty()){
				// Visit each edge exiting u
				for (int i = 0; i < listOfAdj.size(); i++)
				{

					Edge e = listOfAdj.get(i);

					Vertex v = e.source;

					//System.out.println(u + " " + u.level + " " + v + " " + v.level);
					if(v.level > u.level){

						v.level = u.level+1;
						vertexQueue.add(v);
						e.level = u.level;

					}
					else if(v.level == u.level){	

						v.crossEdges.add(e);
						v.crossFlag = true;
						//e.tempLevel = u.level; // cross edge
						//e.level = u.level;
						e.level = -1;
						e.tempLevel = -1;
					}

					if(!v.crossFlag){
						v.demand += min( u.demand, e.capacity);
						edgesLevel.add(new edgesOfLevels(u.level,e));
						edgesLevelTemp.add(new edgesOfLevels(u.level,e));
						e.level = u.level;
						v.crossFlag = false;
					}
					if(v.id == source.id){

						retLev = v.level-1;
						break;
					}
				}
			}
		}
		return source.level - 1;
	}

	@SuppressWarnings("static-access")
	public static int computeLevelByLevel(int sum, ArrayList<Flow> flowArray, LinkedList<Edge> edges, Vertex[] vertices, int alpha){

		int i = 0;
		int tot = 0;
		alpha = 1;
		while(i < alpha ){
			tot = 0;

			Collections.sort(flowArray, Collections.reverseOrder());


			int k = 0;
			for(Flow f: flowArray){
				//System.out.println(f.source + " -> " + f.target);
				//System.out.println(f.targetList);
				for(Vertex v: vertices){
					v.minDistance = Double.MAX_VALUE;
					v.previous = null;
					v.visit = false;
				}
				for(Edge e: edges)
					e.visit = false;

				k = 0;
				int flag = 0;
				int flagx = 0;
				/*				while(k < f.targetList.size()-1){
					Vertex source = f.targetList.get(k++);
					Vertex target = f.targetList.get(k);
					if(source.adjV.contains(target)) {
						Edge e = findEdge(source.id, target.id, edges);
						if (e.capacity >= f.cost && e.visit == false) {
							//System.out.println("!");
							target.previous = source;
							//e.capacity = e.capacity - f.cost;
							e.visit = true;
							flag = 1;
							}else {
								flag = 0;
								flagx = 0;
							}
					}
				}*/
				ArrayList<Vertex> keepVertexOfPath = new ArrayList<Vertex>();
				//k = 0;
				//flag = 0;
				//her flowun target listesine gore shortest path bul
				//eger sequential varsa devam eder eger yoksa zaten source and dest arasinda normal shortest path bulur
				//System.out.println(f.targetList);
				LinkedList<ArrayList<Vertex>> partialPaths=new LinkedList<ArrayList<Vertex>>();
				while(k < f.targetList.size()-1){
					for(Vertex v: vertices) {
						v.minDistance = Double.MAX_VALUE;
						v.visit = false;
						v.previous = null;
					}

					Vertex source = f.targetList.get(k++);
					Vertex target = f.targetList.get(k);
					if(source.adjV.contains(target)) {
						Edge e = findEdge(source.id, target.id, edges);
						//System.out.println(e.source.name + " " + e.target.name);
						if (e.capacity >= f.cost) {
							flag = 1;
							target.previous = source;
							ArrayList<Vertex> pp = new ArrayList<Vertex>();
							pp.add(source);
							pp.add(target);
							
							partialPaths.add(pp);
							continue;
							
						}

						//continue;

					}
					//	System.out.println("Burda: "+f.id + " c: " + f.cost + " " + source + " " + target);
					//if(source.id == target.id)
					//flag = 1;
					//else
					//if(flag == 0)
					flag = computePartially(f, source, target, edges, vertices,partialPaths);

				}
				//System.out.println(flag);
				ArrayList<Vertex> path = new ArrayList<Vertex>();
				if(flag == 1) {
					

					//System.out.println(partialPaths);
					path = partialPaths.getFirst();
					if(partialPaths.size() > 1) {
					for(int part = 1; part < partialPaths.size(); part++) {
						ArrayList<Vertex> pp = partialPaths.get(part);
						for(Vertex v: pp) {
							
							if(!path.contains(v)) {
								path.add(v);
							}else{
								int ind = path.indexOf(v);
								//System.out.println(v + " IN " + ind);
								//System.out.println(path);
//								for(int index = ind; index < path.size(); index++)
//									path.remove(index);
								
								path.subList(ind, path.size()).clear();
								
								path.add(v);
							}

							
						}
					}
					}


				}
//arrayList.subList(2, arrayList.size()).clear();

				//System.out.println(path);
				flagx = 0;
				if(!path.isEmpty())
					flagx = pathCheck(path, edges, f);
				if (flagx == 1) {
					f.path = path;
					tot += f.cost;
					Dijkstra.sucCut++;
				}else
					Dijkstra.lostCut++;
				//else
					//System.out.println("####");

			}
			//Max flow ayarlanir eger hepsi girmeyecekse bu bizim max olacak
			//System.out.println(tot+"---"+maxOfFlow);
			if(tot > maxOfFlow){
				maxOfFlow = tot;
			}
			if(tot == sum){
				//System.out.println("V");
				return 1;

			}
			i++;
		}
		return 0;
	}
	
	public static int pathCheck(ArrayList<Vertex> path, LinkedList<Edge> edges, Flow f) {
		Vertex next = null;
		Vertex temp = null;
		Edge e;
		Dijkstra d = new Dijkstra();
		LinkedList<Edge> edgesOfPath = new LinkedList<>();
		temp = path.get(0);

		for(int a =1; a <path.size();a++){

			next = path.get(a);

			e = d.findEdge(temp.id, next.id, edges);
			if(e != null && e.capacity >= f.cost){
				edgesOfPath.add(e);
				temp = next;
			}else
				return 0;

		}
		
		for(Edge ex: edgesOfPath)
			ex.capacity  = ex.capacity - f.cost;
		return 1;
	}
	public static void computeNormal(ArrayList<Flow> flowArray, LinkedList<Edge> edges, Vertex[] vertices){
		int tot = 0;
		for(Flow f: flowArray){
			for(Vertex v: vertices){
				v.minDistance = Double.MAX_VALUE;
				v.previous = null;
			}

			int flag = computePartially(f, f.source, f.target, edges, vertices,null);

			if(flag == 1)
				tot += f.cost;
		}
		maxOfFlow = tot;
	}
	//cutlar arasi shortest path bulur
	public static int computePartially(Flow f, Vertex source, Vertex target, LinkedList<Edge> edges, Vertex[] vertices, LinkedList<ArrayList<Vertex>> partialPaths){
		//System.out.println("s: " + source + " -> " + "t:"+target);

		PriorityQueue<Vertex> Q = new PriorityQueue<Vertex>();
		if(source.minDistance == Double.MAX_VALUE)
			source.minDistance = 0.0;

		Q.add(source);

		int res = 0;
		while(!Q.isEmpty()){
			Vertex u = Q.remove();

			if(!u.adjacencies.isEmpty() ){
				for(Edge e: u.adjacencies){
/*					if(e.possibleFlow.contains(f.id)){

						//System.out.println("F " + f.id + " " + e.possibleFlow + " " + target + " " + e.target);
						//Eger target bir sonraki vertex ise hesap yapmaya gerek yok degilse shortest path bulur
						if(e.target.id == target.id && source.visit == false && target.visit == false && e.capacity >= f.cost){
							//e.capacity -= f.cost;
							target.previous = source;

							findPathPart(source, target, edges, vertices, keepVertexOfPath);
							return 1;
						}else
							return 0;
						//System.out.println("s: " + source + " t: " + target);
						//Q.add(e.target);
						//continue;
					}
				}

				for(Edge e: u.adjacencies){*/
					int x = e.capacity;//* e.rate[source.id][target.id];
					Vertex v = e.target;
					if(x>= f.cost){

						double cost = (double) 1.0/e.capacity;//*e.rate[source.id][target.id];//*e.capacity;
						Double min = u.minDistance + cost;

						if(min < v.minDistance){

							v.minDistance = min;
							v.previous = u;
							Q.add(v);
						}

						if(v.id == target.id){
							res = 1;
						}
					}
				}
			}
		}
		if(res !=0) {
			ArrayList<Vertex > pp = findPathPart(source, target, edges, vertices);
			//System.out.println("asass "+pp);
			if(pp != null)
				partialPaths.add(pp);
			
			else
				return 0;
		}else
			return 0;
		return 1;
	}

	public static int computeWidestPath(Flow fl, Vertex[] vertices, LinkedList<Edge> edges, int level, int type){

		Vertex s = fl.source;
		Vertex t = fl.target;
		int f = fl.cost;
		//int l = level +1;

		int x = 0;
		s.minDistance = Double.MAX_VALUE;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(s);

		while (!vertexQueue.isEmpty()) {

			Vertex u = vertexQueue.remove();

			if(u.adjacencies != null){

				//System.out.println("U: " + u);
				for (int i = 0; i < u.adjacencies.size(); i++)
				{
					Edge e = u.adjacencies.get(i);
					if(e.capacity >= f){
						if(type == 2 && (e.source.level >= e.target.level)){
							Vertex v = e.target;

							//if((e.totalCost + flow) / e.capTemp <= alfa ){

							Double distanceThroughU = minDouble(u.minDistance,e.capacity);
							//System.out.println("MI: " + distanceThroughU);
							//System.out.println(u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);

							if ((distanceThroughU > v.minDistance)) {
								//System.out.println("in--> "+u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);
								//vertexQueue.remove(v);

								v.minDistance = distanceThroughU ;
								v.previous = u;
								vertexQueue.add(v);


							}

							if(v.id == t.id){
								x = 1;
							}
						}else if(type == 1 && e.source.level == e.target.level ){


							Vertex v = e.target;

							//if((e.totalCost + flow) / e.capTemp <= alfa ){

							double distanceThroughU = minDouble(u.minDistance,e.capacity);
							//System.out.println("MI: " + distanceThroughU);
							//System.out.println(u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);

							if ((distanceThroughU > v.minDistance)) {
								//System.out.println("in--> "+u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);
								//vertexQueue.remove(v);

								v.minDistance = distanceThroughU ;
								v.previous = u;
								vertexQueue.add(v);


							}

							if(v.id == t.id){
								x = 1;
							}
						}
					}
				}

			}
		}



		//}




		//System.out.println("X: " + x);
		if(x != 0)
			findPath(s, t, edges, vertices, f);
		//x = findPath(source, target, edges, vertices,flow,flag);
		//System.out.println("y: " + x);

		return x;


	}


	public static int computeAllWidestPath(Flow fl, Vertex[] vertices, LinkedList<Edge> edges){
		int f = fl.cost;
		int x = 1;
		int tF = fl.cost;
		int pathS = 0;
		while (f > 0 && x == 1) {
			for (Vertex v : vertices) {
				v.minDistance = 0.0;
				v.previous = null;
				v.visit = false;
			}
			Vertex s = fl.source;
			Vertex t = fl.target;
			x = 0;

			s.minDistance = Double.MAX_VALUE;
			PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
			vertexQueue.add(s);

			while (!vertexQueue.isEmpty()) {

				Vertex u = vertexQueue.remove();

				if(u.adjacencies != null){

					//System.out.println("U: " + u);
					for (int i = 0; i < u.adjacencies.size(); i++)
					{
						Edge e = u.adjacencies.get(i);

						Vertex v = e.target;

						//if((e.totalCost + flow) / e.capTemp <= alfa ){

						Double distanceThroughU = minDouble(u.minDistance,e.capacity);
						//System.out.println("MI: " + distanceThroughU);
						//System.out.println(u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);

						if ((distanceThroughU > v.minDistance)) {
							//System.out.println("in--> "+u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);
							//vertexQueue.remove(v);

							v.minDistance = distanceThroughU ;
							v.previous = u;
							vertexQueue.add(v);


						}

						if(v.id == t.id){
							x = 1;
						}
					}
				}
			}


			//System.out.println("X: " + x);
			if(x != 0) {
				//System.out.println("mm: " + t.minDistance.intValue());
				pathS = pathS + 1;
				findPathNew(s, t, edges, vertices, t.minDistance.intValue());
				if (t.minDistance.intValue() <= f){
					f = f - t.minDistance.intValue();
					//maxOfFlow = maxOfFlow + (int) t.minDistance.intValue();
				}else if (t.minDistance.intValue() > f && f != 0) {

					//maxOfFlow = maxOfFlow + f;
					f = 0;
				}else
					break;
			}
			maxOfFlow = tF - f;
			//x = findPath(source, target, edges, vertices,flow,flag);
			//System.out.println("y: " + x);
		}
		//System.out.println("P: " + pathS);
		return x;


	}

	public static int computePathwithRate( Vertex[] vertices, LinkedList<Edge> edges, Flow fl){
		int f = fl.cost;
		int x = 0;
		for (Vertex v : vertices) {
			v.minDistance = Double.MAX_VALUE;
			v.previous = null;
		}
		Vertex s = fl.source;
		Vertex t = fl.target;


		s.minDistance = 0.0;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(s);

		while (!vertexQueue.isEmpty()) {

			Vertex u = vertexQueue.remove();

			if(u.adjacencies != null){

				//System.out.println("U: " + u);
				for (int i = 0; i < u.adjacencies.size(); i++)
				{
					Edge e = u.adjacencies.get(i);

					Vertex v = e.target;

					//if((e.totalCost + flow) / e.capTemp <= alfa ){


					if (e.capacity >= f) {
						//e.flowCount = 1;
						//System.out.println(e.rate[s.id][t.id]);
						//e.rate[s.id][t.id] = 1.0;
						Double distanceThroughU = u.minDistance + (1.0*e.flowCount)/(e.rate[s.id][t.id]*e.capacity);						
						//System.out.println("MI: " + distanceThroughU);
						//System.out.println(u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);

						if ((distanceThroughU < v.minDistance)) {
							//System.out.println("in--> "+u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);
							//vertexQueue.remove(v);

							v.minDistance = distanceThroughU ;
							v.previous = u;
							vertexQueue.add(v);


						}

						if(v.id == t.id){
							x = 1;
						}
					}
				}

			}


			//System.out.println("X: " + x);

			//x = findPath(source, target, edges, vertices,flow,flag);
			//System.out.println("y: " + x);
		}
		if(x != 0) {
			//System.out.println("mm: " + t.minDistance.intValue());

			fl.path = findPathR(s, t, edges, vertices, f);
		}
		return x;


	}

	public static int computePathwithRate2( Vertex[] vertices, LinkedList<Edge> edges, Flow fl){
		int f = fl.cost;
		int x = 0;
		for (Vertex v : vertices) {
			v.minDistance = 0.0; // Double.MAX_VALUE;
			v.previous = null;
		}
		Vertex s = fl.source;
		Vertex t = fl.target;


		s.minDistance =(-1)* Double.MAX_VALUE;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(s);

		while (!vertexQueue.isEmpty()) {

			Vertex u = vertexQueue.remove();

			if(u.adjacencies != null){

				//System.out.println("U: " + u);
				for (int i = 0; i < u.adjacencies.size(); i++)
				{
					Edge e = u.adjacencies.get(i);

					Vertex v = e.target;

					//if((e.totalCost + flow) / e.capTemp <= alfa ){


					if (e.rate[s.id][t.id]*e.capacity >= f) {
						//e.flowCount = 1;
						//System.out.println(e.rate[s.id][t.id]);
						Double distanceThroughU = minDouble((-1)*u.minDistance, e.rate[s.id][t.id]*e.capacity);//u.minDistance + (1.0*e.flowCount)/(e.rate[s.id][t.id]*e.capacity);						
						//System.out.println("MI: " + distanceThroughU);
						//System.out.println(u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);

						if ((distanceThroughU > (-1)*v.minDistance)) {
							//System.out.println("in--> "+u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);
							//vertexQueue.remove(v);

							v.minDistance = (-1)*distanceThroughU ;
							v.previous = u;
							vertexQueue.add(v);


						}

						if(v.id == t.id){
							x = 1;
						}
					}
				}

			}


			//System.out.println("X: " + x);

			//x = findPath(source, target, edges, vertices,flow,flag);
			//System.out.println("y: " + x);
		}
		if(x != 0) {
			//System.out.println("mm: " + t.minDistance.intValue());

			findPath(s, t, edges, vertices, f);
		}
		return x;


	}
	public static int computePathwoRate( Vertex[] vertices, LinkedList<Edge> edges, Flow fl, int shf){
		int f = fl.cost;
		int x = 0;
		for (Vertex v : vertices) {
			v.minDistance = Double.MAX_VALUE;
			v.previous = null;
		}
		Vertex s = fl.source;
		Vertex t = fl.target;


		s.minDistance = 0.0;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(s);

		while (!vertexQueue.isEmpty()) {

			Vertex u = vertexQueue.remove();

			if(u.adjacencies != null){

				//System.out.println("U: " + u);
				for (int i = 0; i < u.adjacencies.size(); i++)
				{
					Edge e = u.adjacencies.get(i);

					Vertex v = e.target;

					//if((e.totalCost + flow) / e.capTemp <= alfa ){
					if (e.capacity >= f) {
						Double distanceThroughU = u.minDistance + (1.0)/e.capacity;
						//System.out.println("MI: " + distanceThroughU);
						//System.out.println(u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);

						if ((distanceThroughU < v.minDistance)) {
							//System.out.println("in--> "+u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);
							//vertexQueue.remove(v);

							v.minDistance = distanceThroughU ;
							v.previous = u;
							vertexQueue.add(v);


						}

						if(v.id == t.id){
							x = 1;
						}
					}
				}

			}


			//System.out.println("X: " + x);

			//x = findPath(source, target, edges, vertices,flow,flag);
			//System.out.println("y: " + x);
		}
		if(x != 0) {
			//System.out.println("mm: " + t.minDistance.intValue());

			fl.dspPaths[shf] = findPathR(s, t, edges, vertices, f);
		}
		return x;


	}

	public static int computePathsForAll(Vertex source, Vertex target,LinkedList<Edge> edges, Vertex[] vertices)
	{
		int x = 0;
		source.minDistance = 0.0;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		Vertex temp = null;
		while (!vertexQueue.isEmpty()) {

			Vertex u = vertexQueue.remove();

			if(u.adjacencies != null){
				// Visit each edge exiting u


				for (int i = 0; i < u.adjacencies.size(); i++)
				{

					Edge e = u.adjacencies.get(i);
					Vertex v = e.target;

					if(e.visit == false){


						//if((e.totalCost + flow) / e.capTemp <= alfa ){

						double distanceThroughU = u.minDistance + 1;
						//System.out.println("MI: " + distanceThroughU);
						//System.out.println(u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);

						if ((distanceThroughU < v.minDistance)) {
							//System.out.println("in--> "+u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);
							//vertexQueue.remove(v);

							v.minDistance = distanceThroughU ;
							v.previous = u;
							vertexQueue.add(v);


						}

						if(v.id == target.id)
							x = 1;

					}
				}
			}



			//}



		}

		ArrayList<Vertex> p = null;

		if(x != 0)
			p = findPathForAll(source, target, edges, vertices);
		//	else
		//		System.err.println(flow + " s: " + source + " t: " + target + " NO PATH");
		//x = findPath(source, target, edges, vertices,flow,flag);
		//System.out.println("y: " + x);
		//System.out.println(p);
		if (p == null) return 0;
		else return 1;

	}

	public static int computePaths2(Vertex source, Vertex target,LinkedList<Edge> edges, Vertex[] vertices, int flow)
	{
		int x = 0;
		source.minDistance = (-1)*Double.MAX_VALUE;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		Vertex temp = null;
		while (!vertexQueue.isEmpty()) {

			Vertex u = vertexQueue.remove();

			if(u.adjacencies != null){
				// Visit each edge exiting u


				for (int i = 0; i < u.adjacencies.size(); i++)
				{
					Edge e = u.adjacencies.get(i);

					if(e.capacity >= flow){
						Vertex v = e.target;

						//if((e.totalCost + flow) / e.capTemp <= alfa ){

						double distanceThroughU = minDouble((-1*u.minDistance),e.capacity);
						//System.out.println("MI: " + distanceThroughU);
						//System.out.println(u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);

						if ((distanceThroughU > (-1*v.minDistance))) {
							//System.out.println("in--> "+u.name + " to " + v.name + " id" +e.id+ " c: " + e.capacity + " e.n: " + e.id);
							//vertexQueue.remove(v);

							v.minDistance = (-1)*distanceThroughU ;
							v.previous = u;
							vertexQueue.add(v);


						}

						if(v.id == target.id)
							x = 1;

					}
				}
			}



			//}



		}

		if(x != 0)
			findPath(source, target, edges, vertices, flow);
		//	else
		//		System.err.println(flow + " s: " + source + " t: " + target + " NO PATH");
		//x = findPath(source, target, edges, vertices,flow,flag);
		//System.out.println("y: " + x);
		return x;

	}


	public static ArrayList<Vertex> findPathForAll(Vertex source, Vertex target, LinkedList<Edge> edges,Vertex[] vertices){

		ArrayList<Vertex> path = new ArrayList<Vertex>();


		String pair = Integer.toString(source.id) + "-"+Integer.toString(target.id);
		Vertex temp = target;
		Vertex temp2 = null;

		int b = 1;

		while(temp != source){

			temp2 = temp;
			if(temp.previous != null)
				temp = temp.previous;
			else
				break;

			path.add(temp2);


		}

		path.add(source);
		Collections.reverse(path);
		//System.out.println("NL "+ flow + " s: " + source.id + " t: " + target.id);
		//System.out.println("PATH " + path);
		Vertex next = null;
		Edge e;
		Dijkstra d = new Dijkstra();
		temp = path.get(0);

		for(int a =1; a <path.size();a++){

			next = path.get(a);

			e = d.findEdge(temp.id, next.id, edges);
			if(e != null){

				temp = next;
				e.pairs.add(pair);
				e.visit = true;
			}

		}

		return path;


	}


	public static ArrayList<Vertex> findPathPart(Vertex source, Vertex target, LinkedList<Edge> edges,Vertex[] vertices){

		ArrayList<Vertex> path = new ArrayList<Vertex>();



		Vertex temp = target;
		Vertex temp2 = null;

		int b = 1;

		while(temp != source){

			temp2 = temp;
			if(temp.previous != null)
				temp = temp.previous;
			else {
				return null;
			}

			path.add(temp2);


		}

		path.add(source);
		Collections.reverse(path);
		return path;
		


/*		//System.out.println("NL "+ flow + " s: " + source.id + " t: " + target.id);
		System.out.println("PATH " + path);
		for(Vertex v: vertices) {
			if (keepVertexOfPath.contains(v))
				v.visit = true;
			else
				v.previous = null;

		}
		//			System.out.println(v.name+"-"+v.previous);
		Vertex next = null;
		Edge e;
		Dijkstra d = new Dijkstra();
		temp = path.get(0);

		for(int a =1; a <path.size();a++){
			temp.visit = true;
			next = path.get(a);

			e = d.findEdge(temp.id, next.id, edges);
			if(e != null){

				temp = next;
				e.visit = true;
			}

		}*/


	}
	public static int findPath(Vertex source, Vertex target, LinkedList<Edge> edges,Vertex[] vertices, int flow){

		ArrayList<Vertex> path = new ArrayList<Vertex>();



		Vertex temp = target;
		Vertex temp2 = null;

		int b = 1;

		while(temp != source){

			temp2 = temp;
			if(temp.previous != null)
				temp = temp.previous;
			else
				break;

			path.add(temp2);



		}

		path.add(source);
		Collections.reverse(path);
		//System.out.println("NL "+ flow + " s: " + source.id + " t: " + target.id);
		//System.out.println("PATH " + path);
		Vertex next = null;
		Edge e;
		Dijkstra d = new Dijkstra();
		temp = path.get(0);

		for(int a =1; a <path.size();a++){

			next = path.get(a);

			e = d.findEdge(temp.id, next.id, edges);
			if(e != null){

				temp = next;
				e.capacity = e.capacity - flow;

				e.totalCost = e.totalCost + flow;
				e.flowCount++;
			}else {
				Vertex tmp = path.get(0);
				for(int c = 1; c < a; c++) {
					Vertex nx = path.get(c);
					Edge ex = d.findEdge(tmp.id, nx.id, edges);

					ex.capacity = e.capacity + flow;
					ex.flowCount--;

				}
				return 0;
			}

		}
		return 1;


	}

	public static ArrayList<Vertex> findPathR(Vertex source, Vertex target, LinkedList<Edge> edges,Vertex[] vertices, int flow){

		ArrayList<Vertex> path = new ArrayList<Vertex>();



		Vertex temp = target;
		Vertex temp2 = null;

		int b = 1;

		while(temp != source){

			temp2 = temp;
			if(temp.previous != null)
				temp = temp.previous;
			else
				break;

			path.add(temp2);



		}

		path.add(source);
		Collections.reverse(path);
		//System.out.println("NL "+ flow + " s: " + source.id + " t: " + target.id);
		//System.out.println("PATH " + path);
		Vertex next = null;
		Edge e;
		Dijkstra d = new Dijkstra();
		temp = path.get(0);

		for(int a =1; a <path.size();a++){

			next = path.get(a);

			e = d.findEdge(temp.id, next.id, edges);
			if(e != null){

				temp = next;
				e.capacity = e.capacity - flow;

				e.totalCost = e.totalCost + flow;
				e.flowCount++;
			}else {
				Vertex tmp = path.get(0);
				for(int c = 1; c < a; c++) {
					Vertex nx = path.get(c);
					Edge ex = d.findEdge(tmp.id, nx.id, edges);

					ex.capacity = e.capacity + flow;
					ex.flowCount--;

				}
				return null;
			}

		}
		return path;


	}

	public static int findPathNew(Vertex source, Vertex target, LinkedList<Edge> edges,Vertex[] vertices, int flow){

		ArrayList<Vertex> path = new ArrayList<Vertex>();



		Vertex temp = target;
		Vertex temp2 = null;

		int b = 1;

		while(temp != source){
			temp2 = temp;
			//System.out.println(temp);
			if(temp.previous != null)
				temp = temp.previous;
			else
				break;

			path.add(temp2);



		}

		path.add(source);
		Collections.reverse(path);/*
		System.out.println("Oth: " + flow +" s: " + source.id + " t: " + target.id);*/
		//System.out.println("PATHALL " + path);
		Vertex next = null;
		Edge e;
		Dijkstra d = new Dijkstra();
		temp = path.get(0);

		for(int a =1; a <path.size();a++){

			next = path.get(a);

			e = findEdge(temp.id, next.id, edges);

			if(e != null){

				temp = next;


				e.capacity = e.capacity - flow;

				e.totalCost = e.totalCost + flow;
				e.flowCount++;
				//e.visit = true;
			}else {
				Vertex tmp = path.get(0);
				for(int c = 1; c < a; c++) {
					Vertex nx = path.get(c);
					Edge ex = d.findEdge(tmp.id, nx.id, edges);

					ex.capacity = e.capacity + flow;
					ex.flowCount--;

				}
				return 0;
			}
		}

		return 1;
	}

	public static int findPath2(int f,Vertex source, Vertex target, LinkedList<Edge> edges,Vertex[] vertices){

		ArrayList<Vertex> path = new ArrayList<Vertex>();

		Vertex temp = target;
		Vertex temp2 = null;
		int returnMin = 0;
		//	System.out.println("T: " + temp + " Tas  " + temp.previous);
		while(temp != source){

			temp2 = temp;
			if(temp.previous != null)
				temp = temp.previous;
			else
				break;

			path.add(temp2);
		}

		path.add(source);
		Collections.reverse(path);
		//System.out.println("NL "+ f + " s: " + source.id + " t: " + target.id);

		Vertex next = null;

		LinkedList<Edge> edgesInPath = new LinkedList<Edge>();


		temp = path.get(0);

		for(int a =1; a <path.size();a++){

			next = path.get(a);
			//System.out.println("MIN: " + minEdgeInPath);
			Edge e = findEdge(temp.id, next.id, edges);
			if(e == null)
				return 0;
			edgesInPath.add(findEdge(temp.id, next.id, edges));
			e.capacity = e.capacity - f;
			temp = next;

		}
		setPathEdges(edgesInPath);
		//System.out.println("EDD " + edgesInPath.size());
		/*for(Edge e: edgesInPath){
			//System.out.println(e);
			//System.out.println(e.id + "  " + e.capacity);
			//edges.get(e.id).capacity = edges.get(e.id).capacity - f;
		}
		 */
		setPath(path);
		//System.out.println("PATH " + path + " Bandwith: " +f );


		return 1;
	}

	public static void maxUTofPath(ArrayList<Flow> Flows, LinkedList<Edge> edges, int ll) {
		ArrayList<Vertex > path = null;
		for(Flow f: Flows) {
			if(ll == -1) {
				path = f.path;
			}else
				path = f.dspPaths[ll];

			Vertex temp = path.get(0);
			Vertex next;
			double utM = Double.MIN_VALUE;
			//System.out.println("P " + path);
			for(int a =1; a <path.size();a++){

				next = path.get(a);
				//System.out.println("MIN: " + minEdgeInPath);
				//System.out.println(temp.id + " - " + next.id);
				Edge e = findEdge(temp.id, next.id, edges);
				double ut = (double)(e.capTemp-e.capacity)/e.capTemp;

				if(ut > utM)
					utM = ut;
				temp = next;

			}

			f.lastUT = utM;

		}


	}

	public static void maxUTofPathPre(LinkedList<PathKeepForPre> DList, LinkedList<Edge> edges) {
		for( PathKeepForPre p: DList) {
			ArrayList<Vertex> path = p.path;
			Vertex temp = path.get(0);
			Vertex next;
			double utM = Double.MIN_VALUE;
			for(int a =1; a <path.size();a++){

				next = path.get(a);
				//System.out.println("MIN: " + minEdgeInPath);
				Edge e = findEdge(temp.id, next.id, edges);
				double ut = (double)(e.capTemp-e.capacity)/e.capTemp;

				if(ut > utM)
					utM = ut;
				temp = next;

			}

			p.lastUT = utM;

		}


	}


	public static Edge findEdge(int i, int j, LinkedList<Edge> edges){
		Edge ex = null;
		for(Edge e: edges){

			if(e.sourceId == i && j == e.targetId){
				ex = e;
				if(e.capacity < minEdgeInPath)
					minEdgeInPath = e.capacity;
				break;
			}
		}

		return ex;
	}


	public static int min(int x, int y){
		if(x < y)
			return x;
		else if(y < x)
			return y;
		else 
			return y;

	}
	public static double minDouble(double x, double y){
		if(x < y)
			return x;
		else if(y < x)
			return y;
		else 
			return y;

	}
	public static int max(int x, int y){
		if(x < y)
			return y;
		else if(y < x)
			return x;
		else 
			return y;

	}

	public static int sumOfList(LinkedList<Integer> l){
		int sum = 0;
		ListIterator<Integer> it = l.listIterator();

		while(it.hasNext()){
			sum += it.next();
		}

		return sum;
	}

	public static void setMax(int m){
		maxOfFlow = m;
	}

	public ArrayList<Vertex> getPath(){
		return pathArray;
	}

	public static void setPath(ArrayList<Vertex> path){
		pathArray = path;
	}

	public static int getTot(){
		return totPath;
	}
	public int getMax() {
		return maxOfMax;
	}
	public static void setPathEdges(LinkedList<Edge> p){
		pathEdges = new LinkedList<Edge>();
		pathEdges = p;
	}
	public static LinkedList<Edge> getPathEdges(){
		return pathEdges;
	}


}
