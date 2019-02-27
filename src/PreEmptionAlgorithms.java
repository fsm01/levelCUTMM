import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;


public class PreEmptionAlgorithms {

	public LinkedList<PathKeepForPre> HList = new LinkedList<PathKeepForPre>();
	public LinkedList<PathKeepForPre> DList = new LinkedList<PathKeepForPre>();

	public static LinkedList<PathKeepForPre> B = new LinkedList<PathKeepForPre>();
	public static LinkedList<PathKeepForPre> F = new LinkedList<PathKeepForPre>();
	public static ComputePaths pComp = new ComputePaths();
	public static final int CHOSEN = 5;
	public Flow[] flows;
	public static ArrayList<Flow> flowsArray = new ArrayList<Flow>();
	public static double ciPRE = 0.0;
	public static double avgPRE = 0.0;
	@SuppressWarnings("static-access")
	public int callGreedy(ArrayList<Flow> flowArray2, LinkedList<Edge> edges, Vertex[] vertices){

		//	Collections.sort(flowArray2,Collections.reverseOrder());

		long start = System.nanoTime();
		int sum = 0;
		DList.clear();
		HList.clear();
		B.clear();
		for(int i = 0; i < flowArray2.size(); i++){
			PathKeepForPre pK = new PathKeepForPre(flowArray2.get(i).id, 0, null);
			pK.f = flowArray2.get(i);
			HList.add(pK);
			B.add(pK);

		}

		//	Collections.sort(B, Collections.reverseOrder());

		int x = 0;
		//int cost = 0;
		while(!B.isEmpty()){

			Collections.sort(B,Collections.reverseOrder());
			PathKeepForPre p = B.getFirst();//removeFirst();
			//	System.out.println(p.index + " f: " + p.f.cost);
			Flow f = p.f; // gets first
			for(Vertex v: vertices){
				v.previous = null;
				v.minDistance = Double.MAX_VALUE;
			}
			x = greedyAlgo(f,  edges, vertices);
			//int index = 0;
			if(x != 0){
				x = pComp.findPath2(f.cost,vertices[f.source.id], vertices[f.target.id], edges, vertices);

				//		f.path = pComp.getPath();
				//		f.success = 1;

				p.es = true;
				p.path = pComp.getPath();
				p.flowId = f.id;
				p.pathBWCopare = vertices[f.target.id].minDistance;//1.0/(double) f.cost;
				p.pathBW = f.cost;
				DList.add(p);
				B.removeFirst();


			}
			else{
				if(DList.isEmpty()){
					B.removeFirst();
					continue;
				}else{
					boolean flag = true;

					while(!DList.isEmpty() && flag ){

						Collections.sort(DList,Collections.reverseOrder());
						PathKeepForPre pr = DList.removeFirst();//DList.get(0);

						if(pr.chosen < CHOSEN/*&& pr.path != null*/){

							pr.chosen++;
							int pTrace = 0;
							/*		System.out.println("REMOVED PATH");
							System.out.println(pr.index + " " + pr.pathBW + " " + pr.path);*/
							while(pTrace < pr.path.size()-1 ){

								Vertex s = pr.path.get(pTrace++);
								Vertex t = pr.path.get(pTrace);

								Edge e = findEdge(s.id, t.id, edges);
								//System.out.println("e: " + e.capacity);
								e.capacity = e.capacity + pr.pathBW;
								//System.out.println("eas: " + e.capacity);
							}
							//System.out.println("FFFL " + p.path.size());
							//					System.out.println("SONRA");
							//					for(Edge e: edges)
							//						System.out.print(e.id + " -> " + e.capacity + " ");

							//DList.remove(0);
							//				System.out.println("PASAS " + pr.pathBW);
							pr.es = false;
							pr.path = null;
							pr.pathBW = 0;
							pr.pathBWCopare = 0.0;

							//				System.out.println(flowArray2.get(pr.index).id + " before " + flowArray2.get(pr.index).path + " " + flowArray2.get(pr.index).success);
							//				flowArray2.get(pr.index).path = null;

							//				flowArray2.get(pr.index).success = 0;

							//				System.out.println(flowArray2.get(pr.index).id + " after " + flowArray2.get(pr.index).path + " " + flowArray2.get(pr.index).success);

							B.add(pr); //cikan flow u tekrar run yapmak icin


							for(Vertex v: vertices){
								v.previous = null;
								v.minDistance = Double.MAX_VALUE;
							}
							x = greedyAlgo(f, edges, vertices);
							//System.out.println("BURDA " + f.id + " " + f.cost + " " + f.path);
							if(x == 1){

								x = pComp.findPath2(f.cost,vertices[f.source.id], vertices[f.target.id], edges, vertices);

								//				f.path = pComp.getPath();
								//				f.success = 1;
								p.flowId = f.id;
								p.es = true;
								p.path = pComp.getPath();
								p.pathBWCopare = vertices[f.target.id].minDistance;  //1.0/(double) f.cost;
								p.pathBW = f.cost;
								/*
								System.out.println("instead PATH");
								System.out.println(p.index + " " + p.pathBW + " " + p.path);*/
								//							System.out.println("<<----> "+p.index + " " + p.path + " " + p.pathBW );
								DList.add(p);

								B.removeFirst();

								flag = false;

								//break;
								//flowsArray.remove(0);
								//flowsArray.add(f);

							}else{
								//B.add(p);
								//HList.removeFirst();
								continue;
								//flowsArray.remove(0);
							}
							//continue;

						}else{
							DList.add(pr);
							B.removeFirst();
							flag = false;
							//break;
							//flowsArray.remove(0);
						}
					}
				}
			}
		}

		for(Edge e: edges){
			if(e.capacity < 0)
				System.out.println(e.id + ": " + e.capacity);

		}
		//	System.out.println("LAST LIST");
		Dijkstra dj = new Dijkstra();
		SummaryStatistics stats = new SummaryStatistics();
		if(DList != null){

			dj.lostPre += (flowArray2.size()-DList.size());

			for(PathKeepForPre p : DList){
				sum += p.pathBW;
				stats.addValue(p.pathBW);
				for(Flow f: flowArray2)
					if (f.id == p.flowId)
						f.path = p.path;//flowArray2.get(p.flowId).path = p.path;

				//System.out.println(p.flowId +"\t" + p.lastUT);

				//		System.out.println(p.pathBW + " " + p.path);

			}

		}
		long end = System.nanoTime();
		
		//dj.preTime += (end-start);
		double ci =  dj.calcMeanCI(stats, dj.levelCI);

		setCI(ci);
		setAVG(stats.getMean());
		return sum;
	}

	public Edge findEdge(int i, int j, LinkedList<Edge> edges){
		Edge e = null;
		ListIterator<Edge> listIterator = edges.listIterator();
		while(listIterator.hasNext()){
			e = listIterator.next();
			if(e.sourceId == i && j == e.targetId)
				break;
		}

		return e;
	}
	@SuppressWarnings("static-access")
	public int greedyAlgo(Flow f, LinkedList<Edge> edges, Vertex[] vertices){

		int x = 0;

		Vertex source = vertices[f.source.id];
		Vertex target = vertices[f.target.id];
		//System.out.println("S " + source + " t: " + target);
		source.minDistance = 0.0;

		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		Vertex temp = null;
		while (!vertexQueue.isEmpty()) {

			Vertex u = vertexQueue.remove();
			//System.out.println(u);

			if(u.adjacencies != null){
				// Visit each edge exiting u

				for (int i = 0; i < u.adjacencies.size(); i++)
				{

					Edge e = u.adjacencies.get(i);

					Vertex v = e.target;
					if(e.capacity >= f.cost){
						double distanceThroughU = u.minDistance + 1.0/e.capacity;//e.cost;

						if ((distanceThroughU < v.minDistance)) {
							v.minDistance = distanceThroughU ;
							v.previous = u;
							vertexQueue.add(v);
						}

						if(v.id == target.id)
							x = 1;

					}

				}
			}


		}
		//x = findPath(source, target, edges, vertices,f);
		//System.out.println("y: " + x);
		return x;
	}

	public static void setCI(double ci) {
		ciPRE = ci;
	}

	public static double getCI() {
		return ciPRE;
	}

	public static void setAVG(double avg) {
		avgPRE = avg;
	}

	public static double getAVG() {
		return avgPRE;
	}

}
