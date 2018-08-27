
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.math3.*;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;


public class Dijkstra {
	public static ComputePaths comPath = new ComputePaths();

	public static GetComputePaths getPath = new GetComputePaths();
	public static NumberFormat formatter = new DecimalFormat("#.##");
	public static int RUNSIZE = 14;
	public static int NODESIZE = 100;
	public static int SHUFFLET=5;

	//D:\EclipseWorkspace\lastIdeaDecember2\test2
	public static keepCutLevels[][] levelCuts = new keepCutLevels[NODESIZE][NODESIZE];
	public static int[][][] hopCountSD= new int[RUNSIZE+1][NODESIZE][NODESIZE];
	public static int[][][] hopCountSDToFile= new int[RUNSIZE+1][NODESIZE][NODESIZE];

	public static ArrayList<Vertex>[] dspPaths;
	public static int FLOWSIZE = 3;

	public static int flowRange = 25;
	public static int bandwidthRange = 1000;

	public static int sourceNode = 0;
	public static int targetNode = 31;

	public static int level = 0;

	public static final double levelCI = 0.95;

	/*
	 * public static final int outmostNodesSource[] = {99,75,15}; public static
	 * final int outmostNodesTarget[] = {10,0,6};
	 */
	public static final int outmostNodesSourceX[] = { 1,3, 5, 10, 17};
	public static final int outmostNodesTargetX[] = { 21 ,23, 25, 30 ,31};

	public static int[] outmostNodesSource = {88,94, 86, 55, 10};//{ 99, 96, 98, 95, 97,95 };//,94,93,92,91,90
	public static int[] outmostNodesTarget = {28,44,82, 96, 97, 22};//{ 3, 1, 5, 0, 2,4 };//,6,7,8,9,10
	public static LinkedList<Integer> outmostNodesSourceL;
	public static LinkedList<Integer> outmostNodesTargetL;
	public static keepCutLevels[][] cutLevelArray = new keepCutLevels[NODESIZE][NODESIZE];
	public static int[][] totalFlowForPairs = new int[NODESIZE][NODESIZE]; 
	public static ArrayList<Flow> flowArray = new ArrayList<Flow>();
	public static ArrayList<Flow> flowArrayTemp = new ArrayList<Flow>();
	public static ArrayList<Flow> flowArrayTemp2 = new ArrayList<Flow>();
	public static ArrayList<Flow> flowArrayTemp3 = new ArrayList<Flow>();
	public static ArrayList<Flow> flowArrayTemp4 = new ArrayList<Flow>();
	public static ArrayList<Flow> flowArrayTemp5 = new ArrayList<Flow>();
	public static ArrayList<Flow> flowArrayTemp6 = new ArrayList<Flow>();

	public static Vertex[] vertices = new Vertex[NODESIZE];
	public static LinkedList<Edge> edges = new LinkedList<Edge>();

	public static Vertex[] verticesTemp = new Vertex[NODESIZE];
	public static LinkedList<Edge> edgesTemp = new LinkedList<Edge>();

	public static LinkedList<SharedEdges> listOfSharedEdges;
	public static LinkedList<Edge>[] keepEdgesByLevel = null;

	public static Random Rand = new Random();
	public static boolean flagMM[] = new boolean[RUNSIZE+1];

	public static double totalFlow = 0;
	public static long allTotalFlows = 0;
	public static long maxForCut = 0;
	public static long maxForPre = 0;
	public static long maxForDis=0;
	public static long maxDJ = 0;
	public static long maxWSP = 0;
	public static long maxDJI = 0;
	public static long maxDJSH = 0;

	public static long lastIdeaTime = 0;
	public static long preTime = 0;
	public static long preProcessTime = 0;

	public static long allPreTime = 0;
	public static long allCutTime = 0;
	public static long disTime = 0;
	public static long djTime = 0;
	public static long wspTime = 0;
	public static long djITime = 0;
	public static long djSHTime = 0;

	public static long lostPre = 0;
	public static long lostDis = 0;
	public static long lostDjAll =0;
	public static long lostWSP = 0;
	public static long lostDjI =0;
	public static long lostDjSH =0;
	public static long lostCut = 0;

	public static int sucPre = 0;
	public static int sucDis = 0;
	public static int sucDjAll =0;
	public static int sucWSP = 0;
	public static int sucDjI =0;
	public static int[] sucDj= new int[SHUFFLET];
	public static int[] lostDj = new int[SHUFFLET];
	
	public static int sucCut = 0;


	public static double ciPre = 0.0;
	public static double ciDis = 0.0;
	public static double ciDj =0.0;
	public static double ciWSP = 0.0;
	public static double ciDjI =0.0;
	public static double ciDjSH =0.0;
	public static double ciCUT = 0.0;


	public static double avgPre = 0.0;
	public static double avgDis = 0.0;
	public static double avgDj = 0.0;
	public static double avgWSP = 0.0;
	public static double avgDjI = 0.0;
	public static double avgDjSH = 0.0;
	public static double avgCUT = 0.0;




	public static int alpha = 3;
	public static int beta = 5;


	public static int utCO = 0;
	public static int utLO = 0;
	public static int utAO = 0;
	public static int ut0O = 0;

	public static int utCP = 0;
	public static int utLP = 0;
	public static int utAP = 0;
	public static int ut0P = 0;

	public static int[] utCD = new int[SHUFFLET];
	public static int[] utLD = new int[SHUFFLET];
	public static int[] utAD = new int[SHUFFLET];
	public static int[] ut0D = new int[SHUFFLET];

	public static int utCDL = 0;
	public static int utLDL = 0;
	public static int utADL = 0;
	public static int ut0DL = 0;

	public static int utCDI = 0;
	public static int utLDI = 0;
	public static int utADI = 0;
	public static int ut0DI = 0;

	public static int utCDSH = 0;
	public static int utLDSH = 0;
	public static int utADSH = 0;
	public static int ut0DSH = 0;

	public static int utCW = 0;
	public static int utLW = 0;
	public static int utAW = 0;
	public static int ut0W = 0;

	public static int utCC = 0;
	public static int utLC = 0;
	public static int utAC = 0;
	public static int ut0C = 0;

	public static double[][] utDD;

	public static int EDGESIZE = 0;

	public static BufferedWriter writer; 
	public static BufferedWriter writer2;
	@SuppressWarnings({ "static-access", "unlikely-arg-type", "resource", "unchecked" })
	public static void main(String[] args) throws FileNotFoundException,

	IOException {

//		writer = new BufferedWriter(new FileWriter("C:\\Users\\erdalEv\\eclipse-workspace\\levelCut-EV\\src\\utEdge.txt"));
//		writer2 = new BufferedWriter(new FileWriter("C:\\Users\\erdalEv\\eclipse-workspace\\levelCut-EV\\src\\utFlow.txt"));


		System.out.println("GIT HADI DA");

		for(int k = 1; k <= RUNSIZE; k++) {
			flagMM[k] = true;

			/*			intializeVertices(vertices, NODESIZE);
			intializeVertices(verticesTemp, NODESIZE);
			readFileYeni2(k);
			for(int i =0; i < NODESIZE; i++) {
				for(int j = 0; j < NODESIZE; j++) {
					hopCountSD[k][i][j] = 0;
					hopCountSDToFile[k][i][j] = 0;

					for(Vertex v: vertices) {
						v.hopCount = 0;
						v.minDistance = Double.MAX_VALUE;
					}
					if (vertices[i].id != vertices[j].id) {
						int x = comPath.hopCount(vertices[i], vertices[j], edges, vertices);

						if(x>3) {

							String ss = Integer.toString(k) + "\t" + Integer.toString(i) + "\t" + Integer.toString(j) + "\n"; 
							writer2.write(ss);

							System.out.println(ss);

						}
						//System.out.println(vertices[outmostNodesSource[i]].name+"->"+vertices[outmostNodesTarget[j]].name+": " + x);
					}

					edges.clear();

					for (int t = 0; t < edges.size(); t++) {
						vertices[t].adjacencies.clear();
					}
					copyGraph2();


				}
			}



			 */
		}
		//		writer2.close();
		System.out.println("FLOW\tPRE_DATA\tDIS_DATA\tDSP_DATA\tLEVELCUTMM_DATA\tDSPSH_DATA\tDWSP_DATA\tPRE_DATA_AVG\tDIS_DATA_AVG\tDSP_DATA_AVG\tDSPI_DATA_AVG\tDSPSH_DATA_AVG\tDWSP_DATA_AVG\tPRE_DATA_CI\tDIS_DATA_CI\tDSP_DATA_CI\tDSPI_DATA_CI\tDSPSH_DATA_CI\tDWSP_DATA_CI\tPRE_TIME\tDIS_TIME\tDSP_TIME\tDSPI_TIME\tDSPSH_TIME\tDWSP_TIME\tPRE_OVERLOAD_LINK\tOUR_OVERLOAD_LINK\tDSP_OVERLOAD_LINK\tDSPI_OVERLOAD_LINK\tDSPSH_OVERLOAD_LINK\tDWSP_OVERLOAD_LINK\tPRE_SHORTLOAD_LINK\tOUR_SHORTLOAD_LINK\tDSP_SHORTLOAD_LINK\tDSPI_SHORTLOAD_LINK\tDSPSH_SHORTLOAD_LINK\tDWSP_SHORTLOAD_LINK\tPRE_AVGLOAD_LINK\tOUR_AVGLOAD_LINK\tDSP_AVGLOAD_LINK\tDSPI_AVGLOAD_LINK\tDSPSH_AVGLOAD_LINK\tDWSP_AVGLOAD_LINK");
		PreEmptionAlgorithms pre = new PreEmptionAlgorithms();
		for (FLOWSIZE = 50; FLOWSIZE <= 500; FLOWSIZE = FLOWSIZE + 50) {

			totalFlow = 0;
			preTime = 0;
			lastIdeaTime = 0;
			maxForCut = 0;
			maxForPre = 0;
			maxForDis = 0;

			lostPre = 0;
			lostDis = 0;
			lostDjAll =0;
			lostWSP = 0;
			lostCut = 0;

			sucPre = 0;
			sucDis = 0;
			sucDjAll =0;
			sucWSP = 0;
			sucCut = 0;

			disTime = 0;
			djTime=0;

			maxDJ = 0;
			wspTime=0;
			maxWSP = 0;
			maxDJI = 0;
			maxDJSH = 0;

			ciPre = 0.0;
			ciDis =0.0;
			ciDj = 0.0;
			ciWSP = 0.0;
			ciDjI = 0.0;
			ciDjSH = 0.0;
			ciCUT =0.0;

			avgPre = 0.0;
			avgDis = 0.0;
			avgDj = 0.0;
			avgWSP = 0.0;
			avgDjI = 0.0;
			avgDjSH = 0.0;
			avgCUT = 0.0;

			utCO = 0;
			utLO = 0;
			utAO = 0;
			ut0O = 0;

			utCP = 0;
			utLP = 0;
			utAP = 0;
			ut0P = 0;

			utCDL = 0;
			utLDL = 0;
			utADL = 0;
			ut0DL = 0;

			for(int ss = 0; ss<utCD.length;ss++) {
				utCD[ss] = 0;
				utLD[ss] = 0;
				utAD[ss] = 0;
				ut0D[ss] = 0;

				sucDj[ss] = 0;
				lostDj[ss] = 0;
			}
			utCDI = 0;
			utLDI = 0;
			utADI = 0;
			ut0DI = 0;

			utCDSH = 0;
			utLDSH = 0;
			utADSH = 0;
			ut0DSH = 0;

			utCW = 0;
			utLW = 0;
			utAW = 0;
			ut0W = 0;

			utCC = 0;
			utLC = 0;
			utAC = 0;
			ut0C = 0;

			SummaryStatistics stats = new SummaryStatistics();
			SummaryStatistics stats2 = new SummaryStatistics();
			SummaryStatistics stats3 = new SummaryStatistics();
			SummaryStatistics stats4 = new SummaryStatistics();

			SummaryStatistics stats5 = new SummaryStatistics();
			SummaryStatistics stats6 = new SummaryStatistics();
			SummaryStatistics stats7 = new SummaryStatistics();

			EDGESIZE = 0;
			for (int run = 1; run <= RUNSIZE; run++) {

				for (int i = 0; i < NODESIZE; i++) {

					for (int j =0; j < NODESIZE; j++) {

						totalFlowForPairs[i][j] = 0;
					}
				}
				String fileName1 = "C:\\Users\\erdalEv\\eclipse-workspace\\levelCut-EV\\src\\utResults\\EDGE\\"+"utEdge"+FLOWSIZE+"-"+run+".txt";
				String fileName2 = "C:\\Users\\erdalEv\\eclipse-workspace\\levelCut-EV\\src\\utResults\\FLOW\\"+"utFlow"+FLOWSIZE+"-"+run+".txt";
				writer = new BufferedWriter(new FileWriter(fileName1));
				writer2 = new BufferedWriter(new FileWriter(fileName2));


				intializeVertices(vertices, NODESIZE);
				intializeVertices(verticesTemp, NODESIZE);

				flowArray.clear();
				flowArrayTemp.clear();
				flowArray = new ArrayList<Flow>();
				flowArrayTemp = new ArrayList<Flow>();
				flowArrayTemp2 = new ArrayList<Flow>();
				flowArrayTemp3 = new ArrayList<Flow>();
				flowArrayTemp4 = new ArrayList<Flow>();
				flowArrayTemp5 = new ArrayList<Flow>();
				flowArrayTemp6 = new ArrayList<Flow>();

				// Topology okuma...
				//readFileYeni();
				//basicGraph2();
				//readFileYeni();
				readFileYeni2(run);
				//readFile();

				EDGESIZE += edges.size();

				utDD = new double[SHUFFLET][edges.size()];

				dspPaths = new ArrayList[SHUFFLET];
				BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\erdalEv\\eclipse-workspace\\levelCut-EV\\pairs.txt"));
				String line;
				//				outmostNodesSource = new LinkedList<Integer>();
				//				outmostNodesTarget = new LinkedList<Integer>();
				/*
				while ((line = br.readLine()) != null) {

					if(!line.isEmpty()) {
						String[] s = line.split("\t");
						//System.out.println(line);
						if(Integer.parseInt(s[0]) == run) {
							for(Vertex v: vertices) {
								v.hopCount = 0;
								v.minDistance = Double.MAX_VALUE;
							}
							int src = Integer.parseInt(s[1]);
							int dst = Integer.parseInt(s[2]);
							//int x = comPath.hopCount(vertices[src], vertices[dst], edges, vertices);
							//if( x > 3) {
							outmostNodesSource.add(Integer.parseInt(s[1]));
							outmostNodesTarget.add(Integer.parseInt(s[2]));

							//}

						}
					}

				}*/
				//				System.out.println("!!"+outmostNodesSource);
				//				System.out.println("??"+outmostNodesTarget);
				//				for(Vertex v: vertices) {
				//					v.hopCount = 0;
				//					v.minDistance = Double.MAX_VALUE;
				//				}
				//				System.out.println(comPath.hopCount(vertices[outmostNodesSource.get(2)], vertices[outmostNodesTarget.get(2)], edges, vertices));

				//readFile();
				/*				for (int i = 0; i < outmostNodesSource.length; i++) {
					for(int j = 0; j < outmostNodesTarget.length; j++) {
						for(Vertex v: vertices) {
							v.hopCount = 0;
							v.minDistance = Double.MAX_VALUE;
						}
						if (vertices[i].id != vertices[j].id) {
							int x = comPath.hopCount(vertices[outmostNodesSource[i]], vertices[outmostNodesTarget[j]], edges, vertices);
							//if(x>3)
							System.out.println(vertices[outmostNodesSource[i]].name+"->"+vertices[outmostNodesTarget[j]].name+": " + x);
						}

						edges.clear();

						for (int t = 0; t < edges.size(); t++) {
							vertices[t].adjacencies.clear();
						}
						copyGraph2();

					}
				}*/
				for (int j = 0; j < FLOWSIZE; j++) {

					sourceNode =Rand.nextInt(outmostNodesSource.length);// Rand.nextInt(NODESIZE);//outmostNodesSource[Rand.nextInt(outmostNodesSource.length)];
					//burasi 32 lik graph icin

					targetNode = sourceNode;////outmostNodesTarget[Rand.nextInt(outmostNodesTarget.length)];
					//			while(targetNode==sourceNode) {

					//				sourceNode = Rand.nextInt(NODESIZE);//Rand.nextInt(outmostNodesSource.size());outmostNodesSource[Rand.nextInt(outmostNodesSource.length)];
					//				targetNode = Rand.nextInt(NODESIZE);//sourceNode;////outmostNodesTarget[Rand.nextInt(outmostNodesTarget.length)];

					//			}
					//	sourceNode = 1; //Rand.nextInt(NODESIZE);
					//	targetNode = 30; //Rand.nextInt(NODESIZE);
					//for(Vertex v: vertices) {
					//	v.hopCount = 0;
					//	v.minDistance = Double.MAX_VALUE;
					//}
					//if (hopCountSD[run][sourceNode][targetNode] < 4) {
					/*	int x = 0;//comPath.hopCount(vertices[sourceNode], vertices[targetNode], edges, vertices);

						while (targetNode == sourceNode || x == 0) {
							for(Vertex v: vertices) {
								v.hopCount = 0;
								v.minDistance = Double.MAX_VALUE;
							}
							sourceNode = Rand.nextInt(NODESIZE);
							targetNode = Rand.nextInt(NODESIZE);
							x = 1;
							if(vertices[sourceNode].adjV.contains(vertices[targetNode])) {
									x = 0;
								}

							else {
								for(Vertex v: vertices[sourceNode].adjV) {
									if(v.adjV.contains(vertices[targetNode]))
										x = 0;
							}
							}
							//comPath.hopCount(vertices[sourceNode], vertices[targetNode], edges, vertices);
							hopCountSD[run][sourceNode][targetNode] = x;
						}
					//}
					if(hopCountSDToFile[run][sourceNode][targetNode] == 0) {
						String ss = Integer.toString(run) + "\t" + Integer.toString(sourceNode) + "\t" + Integer.toString(targetNode) + "\n"; 
						writer.write(ss);
						hopCountSDToFile[run][sourceNode][targetNode] = 1;
					}
					Flow f = new Flow(vertices[sourceNode],
							vertices[targetNode], 1 + Rand.nextInt(flowRange),
							j);
					 */
					int demandF = 1+ Rand.nextInt(25);
					/*					double pr = Rand.nextDouble();
					if( pr < 0.25) {//if(pr<0.5){
						demandF = 1 + Rand.nextInt(10);
					}else if(pr < 0.75) {
						demandF = 10 + Rand.nextInt(15);
					}else {
						demandF = 25 + Rand.nextInt(25);
					}*/
					Flow f = new Flow(vertices[outmostNodesSource[sourceNode]],vertices[outmostNodesTarget[targetNode]],demandF,j);//new Flow(vertices[sourceNode],vertices[targetNode],demandF,j);//
					allTotalFlows += f.cost;
					totalFlowForPairs[f.source.id][f.target.id] = totalFlowForPairs[f.source.id][f.target.id] + f.cost;

					flowArray.add(f);
					flowArrayTemp.add(f);
					flowArrayTemp2.add(f);
					flowArrayTemp3.add(f);
					flowArrayTemp4.add(f);
					flowArrayTemp5.add(f);
					flowArrayTemp6.add(f);
					//					vertices[sourceNode].inDemandFlows.add(j);
					//					verticesTemp[sourceNode].inDemandFlows.add(j);
					//					vertices[targetNode].outDemandFlows.add(j);
					//					verticesTemp[targetNode].outDemandFlows.add(j);
				}

				//System.out.println("TOT: " + allTotalFlows);
				//
				//				//Pre
				//				for (Vertex v : vertices) {
				//					v.minDistance = Double.MAX_VALUE;
				//					v.previous = null;
				//					v.visit = false;
				//				}
				//
				/*								System.out.println("FLOWS\n");
								for (Flow f:flowArray){
									System.out.println(f.source.id+"\t"+f.target.id+"\t " + f.cost);
									System.out.println(f.source.id+"->"+f.target.id+": " + totalFlowForPairs[f.source.id][f.target.id]);
									System.out.println(hopCountSD[run][f.source.id][f.target.id]);
								}*/
				//				System.out.println("----"+allTotalFlows);


				//				long startTime = System.nanoTime();




				//				long endTime = System.nanoTime();
				//				System.out.println((endTime-startTime)/ (1e6 * RUNSIZE));
				//				copyGraph2();


				edges.clear();

				for (int t = 0; t < edges.size(); t++) {
					vertices[t].adjacencies.clear();
				}
				comPath.setMax(0);
				copyGraph2();
				//System.out.println(sumOfFlow(flowArray));
				/*				if (lastIdea(flowArray, run)) {
					maxForCut += comPath.maxOfFlow;//sumOfFlow(flowArray);
				} else
					maxForCut += comPath.maxOfFlow;

				System.out.println(maxForCut);*/
				/*				Scanner sc = new Scanner(System.in);
				sc.next();
				 */
				Collections.sort(flowArray, Collections.reverseOrder());
				long startMM = System.nanoTime();
				long resMM = MMoffline(flowArray, outmostNodesSource,outmostNodesTarget, run);
				long endMM = System.nanoTime();
				disTime += (endMM-startMM);
				maxForDis  = maxForDis + resMM ;
				stats4.addValue(resMM);
				comPath.setMax(0);

				//System.out.println(maxDJ + "--" + allTotalFlows);
				//System.out.println();
				//Collections.sort(edges, Collections.reverseOrder());
				writer.write("DIS\t");
				for(Edge e: edges) {
					double ut = (double)(e.capTemp-e.capacity)/e.capTemp;
					String utS = Double.toString(ut) + "\t";
					writer.append(utS);
					//System.out.print(ut+"\t");
					if(ut > 0.85) {
						//System.out.println(e.source.name+"->"+e.target.name+": "+ut);
						utCO++;
					}
					if(ut < 0.25)
						utLO++;
					if(0.25 <= ut && ut <= 0.85)
						utAO++;
					if(ut == 0)
						ut0O++;
					if(ut > 1.0)
						System.out.println(e.source + " - " + e.target +" " + e.capacity);
					if(e.capacity < 0) {
						System.out.println(e.source + " - " + e.target +" " + e.capacity);
						System.out.println(comPath.findEdge(e.sourceId, e.targetId, edges).capacity);

						//System.out.println(e.flowCount);
					}


				}
				//				System.out.println("OUR"+"\t"+(double)utC/edges.size() +"\t" +(double)utA/edges.size()+"\t" +(double)utL/edges.size() + "\t"+(double)ut0/edges.size());

				//	System.out.println();
				if(sucDis == FLOWSIZE) {
					comPath.maxUTofPath(flowArray, edges, -1);
					writer2.write("Dis\n");
					for(Flow f: flowArray) {

						//System.out.println(f.id+ "\t" + f.lastUT);
						String utS = Integer.toString(f.id)+"\t"+Double.toString(f.lastUT) + "\n";
						writer2.append(utS);
					}
				}

				edges.clear();

				for (int t = 0; t < edges.size(); t++) {
					vertices[t].adjacencies.clear();
				}
				copyGraph2();
				long maxStepByStep = 0;




				//				if(maxDJ < 0.9*allTotalFlows) {
				//System.out.println(FLOWSIZE+ "--");
				//continue;


				//				System.out.println((float)maxDJ/(float)allTotalFlows);
				//System.out.println("PRE");
				long startTime = System.nanoTime();
				//Collections.sort(flowArrayTemp, Collections.reverseOrder());
				int pRes = pre.callGreedy(flowArrayTemp, edges, vertices);
				sucPre = pre.DList.size();
				//System.out.println("----"+pRes);
				maxForPre += pRes;
				stats2.addValue(pRes);

				long endTime = System.nanoTime();

				//				Collections.sort(edges, Collections.reverseOrder());
				writer.append("\nPRE\t");
				for(Edge e: edges) {
					double ut = (double)(e.capTemp-e.capacity)/e.capTemp;
					String utS = Double.toString(ut) + "\t";
					writer.append(utS);
					//					System.out.print(ut+"\t");
					if(ut > 0.85) {
						//System.out.println(ut);
						utCP++;
					}
					if(ut < 0.25)
						utLP++;

					if(0.25 <= ut && ut <= 0.85)
						utAP++;

					if(ut == 0)
						ut0P++;
				}
				
				if(sucPre == FLOWSIZE) {
					comPath.maxUTofPath(flowArrayTemp3, edges,-1);

					writer2.write("\nPRE\n");
					for(Flow f: flowArrayTemp) {

						//System.out.println(f.id+ "\t" + f.lastUT);
						String utS = Integer.toString(f.id)+"\t"+Double.toString(f.lastUT) + "\n";
						writer2.append(utS);
					}
				}
				////comPath.maxUTofPath(flowArrayTemp, edges, -1);

				/*		writer2.write("\nPRE\n");
				for(Flow f: flowArrayTemp) {

					//System.out.println(f.id+ "\t" + f.lastUT);
					String utS = Integer.toString(f.id)+"\t"+Double.toString(f.lastUT) + "\n";
					writer2.append(utS);
				}
				 */		//				System.out.println("PRE"+"\t"+(double)utC/edges.size() +"\t" +(double)utA/edges.size()+"\t" +(double)utL/edges.size() + "\t"+(double)ut0/edges.size());
				//System.out.println(utC +"--" + utA+"--"+ utL +"-0-"+ut0);
				preTime += (endTime - startTime);

				comPath.setMax(0);
				//				System.out.println();
				edges.clear();

				for (int t = 0; t < edges.size(); t++) {
					vertices[t].adjacencies.clear();
				}

				copyGraph2();




				long shfTot = 0;
				startTime = System.nanoTime();

				int shf = 0;
				int highestS = -1;
				while(shf < SHUFFLET) {

					comPath.setMax(0);

					edges.clear();

					for (int t = 0; t < edges.size(); t++) {
						vertices[t].adjacencies.clear();
					}
					copyGraph2();
					shfTot = 0;

					Collections.shuffle(flowArrayTemp3);
					maxStepByStep = 0;
					//				System.out.println("DSP");
					for(Flow f: flowArrayTemp3) {
						for (Vertex v : vertices) {
							v.minDistance = Double.MAX_VALUE;
							v.previous = null;
							v.visit = false;
						}
						int x = comPath.computePathwoRate(vertices, edges, f, shf);
						if (x==1) {
							//maxDJ = maxDJ + f.cost;
							maxStepByStep += f.cost;
							sucDj[shf]++;
						}
						else
							lostDj[shf] = lostDj[shf] + 1;
					}

					if(shfTot < maxStepByStep) {
						shfTot = maxStepByStep;
						highestS = shf;

					}


					for(Edge e: edges) {
						double ut = (double)(e.capTemp-e.capacity)/e.capTemp;
						utDD[shf][e.id] = ut;
						if(ut > 0.85) {
							//System.out.println(ut);
							utCD[shf]++;
						}
						if(ut < 0.25)
							utLD[shf]++;
						if(0.25 <= ut && ut <= 0.85)
							utAD[shf]++;

						if(ut == 0)
							ut0D[shf]++;
					}
					shf = shf + 1;



				}

				endTime = System.nanoTime();
				maxDJ += shfTot;
				utCDL = utCD[highestS];
				utLDL = utLD[highestS];
				utADL = utAD[highestS];
				ut0DL = ut0D[highestS];
				sucDjAll = sucDj[highestS];
				lostDjAll = lostDj[highestS];
				stats3.addValue(shfTot);		
				if(sucDjAll == FLOWSIZE) {
					comPath.maxUTofPath(flowArrayTemp3, edges,highestS);

					writer2.write("\nSHF DSP\n");
					for(Flow f: flowArrayTemp3) {

						//System.out.println(f.id+ "\t" + f.lastUT);
						String utS = Integer.toString(f.id)+"\t"+Double.toString(f.lastUT) + "\n";
						writer2.append(utS);
					}
				}
				writer.append("\nSHF DSP\t");
				for(Edge e: edges) {
					//					System.out.print(utDD[highestS][e.id]+"\t");
					String utS = Double.toString(utDD[highestS][e.id]) + "\t";
					writer.append(utS);
					//System.out.println();
				}
				djTime += (endTime-startTime);

				//				Collections.sort(edges, Collections.reverseOrder());


				//System.out.println(utC+"--"+utA+"--"+utL+"-0-"+ut0);

				//				System.out.println("DSP(GA)"+"\t"+(double)utC/edges.size() +"\t" +(double)utA/edges.size()+"\t" +(double)utL/edges.size() + "\t"+(double)ut0/edges.size());

				//DSPI

				comPath.setMax(0);

				edges.clear();

				for (int t = 0; t < edges.size(); t++) {
					vertices[t].adjacencies.clear();
				}

				copyGraph2();



				startTime = System.nanoTime();

				maxStepByStep = 0;
				//				System.out.println("DSP");
				Collections.sort(flowArrayTemp4, Collections.reverseOrder());
				for(Flow f: flowArrayTemp4) {
					for (Vertex v : vertices) {
						v.minDistance = Double.MAX_VALUE;
						v.previous = null;
						v.visit = false;
					}
					int x = comPath.computePathwoRate(vertices, edges, f, 6);
					if (x==1) {
						maxDJI = maxDJI + f.cost;
						maxStepByStep += f.cost;
						sucDjI++;
					}
					else
						lostDjI = lostDjI + 1;
				}

				endTime = System.nanoTime();
				//				System.out.println();
				stats5.addValue(maxStepByStep);
				djITime += (endTime-startTime);



				//				Collections.sort(edges, Collections.reverseOrder());

				for(Edge e: edges) {
					double ut = (double)(e.capTemp-e.capacity)/e.capTemp;

					if(ut > 0.85) {
						//System.out.println(ut);
						utCDI++;
					}
					if(ut < 0.25)
						utLDI++;
					if(0.25 <= ut && ut <= 0.85)
						utADI++;

					if(ut == 0)
						ut0DI++;
				}
				//System.out.println(utC+"--"+utA+"--"+utL+"-0-"+ut0);

				//				System.out.println("DSP(GA)"+"\t"+(double)utC/edges.size() +"\t" +(double)utA/edges.size()+"\t" +(double)utL/edges.size() + "\t"+(double)ut0/edges.size());



				comPath.setMax(0);
				//				System.out.println("DWSP");
				edges.clear();

				for (int t = 0; t < edges.size(); t++) {
					vertices[t].adjacencies.clear();
				}

				copyGraph2();

				startTime = System.nanoTime();

				maxStepByStep = 0;
				//				System.out.println("DSP");
				Collections.shuffle(flowArrayTemp5);
				for(Flow f: flowArrayTemp5) {
					for (Vertex v : vertices) {
						v.minDistance = Double.MAX_VALUE;
						v.previous = null;
						v.visit = false;
					}
					int x = comPath.computePathwoRate(vertices, edges, f, 7);
					if (x==1) {
						maxDJSH = maxDJSH + f.cost;
						maxStepByStep += f.cost;
						
					}
					else
						lostDjSH = lostDjSH + 1;
				}

				endTime = System.nanoTime();
				//				Collections.sort(edges, Collections.reverseOrder());

				stats6.addValue(maxStepByStep);
				djSHTime += (endTime-startTime);


				for(Edge e: edges) {
					double ut = (double)(e.capacity)/e.capTemp;
					if(ut > 0.85) {
						//System.out.println(ut);
						utCDSH++;
					}
					if(ut < 0.25)
						utLDSH++;
					if(0.25 <= ut && ut <= 0.85)
						utADSH++;

					if(ut == 0)
						ut0DSH++;
				}
				//System.out.println(utC+"--"+utA+"--"+utL+"-0-"+ut0);

				//				System.out.println("DSP(GA)"+"\t"+(double)utC/edges.size() +"\t" +(double)utA/edges.size()+"\t" +(double)utL/edges.size() + "\t"+(double)ut0/edges.size());



				comPath.setMax(0);
				//				System.out.println("DWSP");
				edges.clear();

				for (int t = 0; t < edges.size(); t++) {
					vertices[t].adjacencies.clear();
				}

				copyGraph2();


				long startTime2 = System.nanoTime();
				maxStepByStep  = 0;
				for(Flow f: flowArrayTemp2) {
					for (Vertex v : vertices) {
						v.minDistance = Double.MAX_VALUE;
						v.previous = null;
						v.visit = false;
					}
					int x = comPath.computePaths2(f.source, f.target, edges, vertices, f.cost);
					if (x==1) {
						maxWSP = maxWSP + f.cost;
						maxStepByStep = maxStepByStep + f.cost;
						sucWSP++;


					}
					else
						lostWSP = lostWSP + 1;
				}
				long endTime2 = System.nanoTime();
				stats.addValue(maxStepByStep);
				wspTime += (endTime2-startTime2);
				//writer.append("\nDWSP\n");
				for(Edge e: edges) {
					double ut = (double)(e.capTemp-e.capacity)/e.capTemp;
					//System.out.print(ut+"\t");
					//String utS = Double.toString(ut) + "\t";
					//writer.append(utS);
					if(ut > 0.85) {
						//System.out.println(ut);
						utCW++;
					}
					if(ut < 0.25)
						utLW++;
					if(0.25 <= ut && ut <= 0.85)
						utAW++;

					if(ut == 0)
						ut0W++;
				}
				//				System.out.println("DWSP"+"\t"+(double)utC/edges.size() +"\t" +(double)utA/edges.size()+"\t" +(double)utL/edges.size() + "\t"+(double)ut0/edges.size());

				comPath.setMax(0);


				//				int prevC = flowArray.get(0).cost;
				//				System.out.println("C: "+ prevC);
				for (Vertex v : vertices) {
					v.minDistance = Double.MAX_VALUE;
					v.previous = null;
					v.visit = false;
				}
				//lastIdea

				//				if (lastIdea(flowArray, level)) {
				//					maxForCut += sumOfFlow(flowArray);
				//				} else
				//					maxForCut += comPath.maxOfFlow;
				//System.out.println("M: "+comPath.maxOfFlow + " --- " + sumOfFlow(flowArray));
				//Max of computePath class set 0
				//				comPath.setMax(0);

				/*				copyGraph2();
				flowArrayTemp.get(0).cost = prevC;
				System.out.println("CC: " + flowArrayTemp.get(0).cost);
				long start = System.nanoTime();
				comPath.computeAllWidestPath(flowArrayTemp.get(0), vertices, edges);
				long end = System.nanoTime();
				long tt = end-start;
				//System.out.println(formatter.format(tt / (1e6 * RUNSIZE)));
				//System.out.println(comPath.maxOfFlow);
				preTime = preTime + (tt);
				maxForPre = maxForPre + comPath.maxOfFlow;
				 */	
				comPath.setMax(0);




				//			}else {
				//				maxDJ = maxDJ;
				//			}

				edges.clear();
				//		edgesTemp.clear();

				for (int t = 0; t < edges.size(); t++) {
					vertices[t].adjacencies.clear();
					//	verticesTemp[t].adjacencies.clear();
				}
				copyGraph2();
				startTime = System.nanoTime();
				maxStepByStep = MMofflineLevelCut(flowArrayTemp6 , outmostNodesSource,outmostNodesTarget, run);
				endTime = System.nanoTime();
				maxForCut += maxStepByStep;
				//lastIdeaTime += (endTime-startTime);


				stats7.addValue(maxStepByStep);
				writer.append("\nLEVELMM\t");
				for(Edge e: edges) {
					double ut = (double)(e.capTemp-e.capacity)/e.capTemp;
					String utS = Double.toString(ut) + "\t";
					writer.append(utS);
					if(ut > 0.85) {
						//System.out.println(ut);
						utCC++;
					}
					if(ut < 0.25)
						utLC++;
					if(0.25 <= ut && ut <= 0.85)
						utAC++;

					if(ut == 0)
						ut0C++;
				}

				if(sucCut == FLOWSIZE) {
					comPath.maxUTofPath(flowArrayTemp6, edges,-1);

					writer2.write("\nLEVELCUTMM\n");
					for(Flow f: flowArrayTemp6) {

						//System.out.println(f.id+ "\t" + f.lastUT);
						String utS = Integer.toString(f.id)+"\t"+Double.toString(f.lastUT) + "\n";
						writer2.append(utS);
					}

				}
				edges.clear();
				edgesTemp.clear();

				for (int t = 0; t < edges.size(); t++) {
					vertices[t].adjacencies.clear();
					verticesTemp[t].adjacencies.clear();
				}

			}

			//					formatter.format((double) preTime/(double)disTime) + "\t" + disTime/disTime);
			//			System.out.println(FLOWSIZE+"\t"+lostPre/RUNSIZE+"\t"+lostDis/RUNSIZE + "\t" + lostDj/RUNSIZE + "\t" + lostWSP/RUNSIZE);
			//System.out.println(FLOWSIZE + "\t"+maxForPre+"\t"+formatter.format(preTime / (1e6 * RUNSIZE))+"\t");
			//allPreTime += preTime;
			//allCutTime += lastIdeaTime;

			ciWSP = calcMeanCI(stats, levelCI);
			avgWSP = stats.getMean();

			ciPre = calcMeanCI(stats2, levelCI);
			avgPre = stats2.getMean();

			ciDj = calcMeanCI(stats3, levelCI);
			avgDj = stats3.getMean();

			ciDjI = calcMeanCI(stats5, levelCI);
			avgDjI = stats5.getMean();

			ciDjSH = calcMeanCI(stats6, levelCI);
			avgDjSH = stats6.getMean();

			ciDis = calcMeanCI(stats4, levelCI);
			avgDis = stats4.getMean();

			ciCUT = calcMeanCI(stats7, levelCI);
			avgCUT = stats7.getMean();

			double edgeSIZEAVG = (double) EDGESIZE/100; //Bu 100 ile carpmayi unuttugum icin
			//			System.out.println();
			System.out.println(FLOWSIZE + "\t"+ maxForPre+"\t"+maxForDis+"\t"+maxDJ+"\t"+maxForCut+"\t"+maxWSP+
					"\t"+formatter.format(avgPre) + "\t" + formatter.format(avgDis)+ "\t" + formatter.format(avgDj) + "\t" + formatter.format(avgCUT)+"\t" + formatter.format(avgWSP)+
					"\t"+formatter.format(ciPre) + "\t" + formatter.format(ciDis)+ "\t" + formatter.format(ciDj) + "\t" + formatter.format(ciCUT)+ "\t" + formatter.format(ciWSP)+
					"\t"+formatter.format(preTime / (1e6 * RUNSIZE))+ "\t"+formatter.format(disTime / (1e6 * RUNSIZE)) + "\t" + formatter.format(djTime / (1e6 * RUNSIZE))+ "\t" + formatter.format(lastIdeaTime / (1e6 * RUNSIZE))+"\t" + formatter.format(wspTime / (1e6 * RUNSIZE))+
					"\t"+formatter.format((double)utCP/edgeSIZEAVG) +"\t"+formatter.format((double)utCO/edgeSIZEAVG) +"\t" +formatter.format((double)utCDL/edgeSIZEAVG)+"\t" +formatter.format((double)utCC/edgeSIZEAVG) +  "\t"+formatter.format((double)utCW/edgeSIZEAVG)+
					"\t"+formatter.format((double)utLP/edgeSIZEAVG) +"\t" +formatter.format((double)utLO/edgeSIZEAVG) +"\t" +formatter.format((double)utLDL/edgeSIZEAVG)+"\t" +formatter.format((double)utLC/edgeSIZEAVG) + "\t"+formatter.format((double)utLW/edgeSIZEAVG)+
					"\t"+formatter.format((double)utAP/edgeSIZEAVG) +"\t"+formatter.format((double)utAO/edgeSIZEAVG) +"\t" +formatter.format((double)utADL/edgeSIZEAVG)+"\t" +formatter.format((double)utAC/edgeSIZEAVG) +  "\t"+formatter.format((double)utAW/edgeSIZEAVG)+
					"\t"+ sucPre+"\t"+sucDis+"\t"+sucDj+"\t"+sucCut+"\t"+sucWSP);
/*			System.out.println("PRE\t" + "DIS\t"+"DJ\t"+"CUT\t"+"WSP");
			System.out.println(maxForPre+"\t"+maxForDis+"\t"+maxDJ+"\t"+maxForCut+"\t"+maxWSP+"\t"+maxDJI);
			System.out.println(sucPre+"\t"+sucDis+"\t"+sucDjAll+"\t"+sucCut+"\t"+sucWSP+"\t"+sucDjI);
			System.out.println(lostPre +"\t" + lostDis + "\t" + lostDjAll + "\t" + lostCut + "\t" + lostWSP +"\t" + lostDjI);
*/			// "\t"+formatter.format(100*((float) maxForPre/allTotalFlows))+"\t"+formatter.format(100*((float)maxForDis/allTotalFlows))+"\t"+formatter.format(100*((float)maxDJ/allTotalFlows))+"\t"+formatter.format(100*((float)maxWSP/allTotalFlows))+
			//			System.out.println(FLOWSIZE+"\t"+formatter.format(avgPre) + "\t"+formatter.format(avgDis) +"\t"+formatter.format(avgDj)+"\t"+formatter.format(avgWSP));
			//			System.out.println(FLOWSIZE+"\t"+formatter.format(ciPre) + "\t"+formatter.format(ciDis) +"\t"+formatter.format(ciDj)+"\t"+formatter.format(ciWSP));
		}

		writer.close();
		writer2.close();
	}


	@SuppressWarnings("static-access")
	public static int MMoffline(ArrayList<Flow> flowArray, int[] sources, int[] targets, int run) {
		int maxRateRes = 0;
		//long startTimePre = System.nanoTime();

		long startTime = 0;
		if(flagMM[run]) {
			flagMM[run] = false;
			startTime = System.nanoTime();
			//System.out.println(run);
		}
		//		System.out.println("////"+sources);
		for(int i = 0; i < sources.length; i++) {
			//			for (int j = 0; j < NODESIZE; j++) {
			//				if(totalFlowForPairs[i][j] > 0) {//hopCountSD[run][i][j] > 0 && 
			Vertex source = vertices[outmostNodesSource[i]];//vertices[i];//[sources.get(i)];
			Vertex target = vertices[outmostNodesTarget[i]];//vertices[i];//[targets.get(i)];
			for (Vertex v : vertices) {
				v.minDistance = Double.MAX_VALUE;
				v.previous = null;
				v.visit = false;

			}

			for(Edge e: edges) {
				e.visit = false;
				e.flowCount = 1;
			}
			int times = 0;
			while(comPath.computePathsForAll(source, target, edges, vertices)==1) {
				times++;
				for (Vertex v : vertices) {
					v.minDistance = Double.MAX_VALUE;
					v.previous = null;
				}
			}

			//	}
			//System.out.println(source + "-"+target+": "+times + " times");
		}
		//		}
		//		}
		//long endTimePre = System.nanoTime();

		//System.out.println((endTimePre-startTimePre)/(1e6));
		if(flagMM[run] == false)
			startTime = System.nanoTime();
		for (Edge e: edges) {
			//System.out.println(e.id +". "+e.source+"-"+e.target+ ":\n"+e.pairs);
			e.flowCount = 1;
			for(String pair: e.pairs) {

				String[] sp = pair.split("-");

				int s = Integer.parseInt(sp[0]);
				int t = Integer.parseInt(sp[1]);

				e.totalPair = e.totalPair + totalFlowForPairs[s][t];


			}

			for(String pair: e.pairs) {

				String[] sp = pair.split("-");

				int s = Integer.parseInt(sp[0]);
				int t = Integer.parseInt(sp[1]);
				if (totalFlowForPairs[s][t] != 0)
					e.rate[s][t] = (float) totalFlowForPairs[s][t]/e.totalPair;

				//System.out.println(e.id + ". " +s+"-"+t+": "+ e.rate[s][t]);

			}


		}
		/*		ArrayList<Flow> yeniFlowArray1 = new ArrayList<Flow>();
		ArrayList<Flow> yeniFlowArray2 = new ArrayList<Flow>();

		ArrayList<Flow> yeniFlowArray3 = new ArrayList<Flow>();

		ArrayList<Flow> yeniFlowArray4 = new ArrayList<Flow>();

		ArrayList<Flow> yeniFlowArray5 = new ArrayList<Flow>();

		LinkedList<ArrayList<Flow>> allArrays = new LinkedList<ArrayList<Flow>>();
		Collections.sort(flowArray,Collections.reverseOrder());

		for(Flow f: flowArray) {

			if(f.source.id == outmostNodesSource[0])
				yeniFlowArray1.add(f);
			else if (f.source.id == outmostNodesSource[1])
				yeniFlowArray2.add(f);
			else if (f.source.id == outmostNodesSource[2])
				yeniFlowArray3.add(f);
			else if (f.source.id == outmostNodesSource[3])
				yeniFlowArray4.add(f);
			else if (f.source.id == outmostNodesSource[4])
				yeniFlowArray5.add(f);
		}

		if(!yeniFlowArray1.isEmpty())
			allArrays.add(yeniFlowArray1);
		if(!yeniFlowArray2.isEmpty())
			allArrays.add(yeniFlowArray2);
		if(!yeniFlowArray3.isEmpty())
			allArrays.add(yeniFlowArray3);
		if(!yeniFlowArray4.isEmpty())
			allArrays.add(yeniFlowArray4);
		if(!yeniFlowArray5.isEmpty())
			allArrays.add(yeniFlowArray5);
		long preM = 0;
		for(ArrayList<Flow> Flows: allArrays) {
			for(Flow f: Flows)
				System.out.println(f.source.name + "-> "+ f.target.name+": " + f.cost);
			System.out.println("TT: " + sumOfFlow(Flows));

				for (Vertex v : vertices) {
						v.minDistance = Double.MAX_VALUE;
						v.previous = null;
						v.visit = false;

					}

					for(Edge e: edges) {
						e.visit = false;
						e.flowCount = 1;
					}

			int s = Flows.get(0).source.id;
			int t = Flows.get(0).target.id;
			try {

				if(lastIdea(Flows, run, s, t)) {
					maxRateRes += sumOfFlow(Flows);}
				else
					maxRateRes += comPath.maxOfFlow;

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println(">>"+(maxRateRes - preM) + "<<");
			preM = maxRateRes;
			comPath.setMax(0);
			//copyGraph2();
		}

		 */
		//		for (int i = flowArray.size()/2 + 1;  i < flowArray.size(); i++)
		//			yeniFlowArray.add(flowArray.get(i));


		for (Flow f: flowArray) {

			int a = comPath.computePathwithRate(vertices, edges, f);//comPath.computePathwithRate(vertices, edges, f);

			if(a==1) {
				maxRateRes = maxRateRes + f.cost;
				sucDis++;

			}
			else
				lostDis = lostDis + 1;

		}
		long endTime = System.nanoTime();
		//disTime = disTime + (endTime-startTime);

		//System.out.println("Run: "+(endTime-startTime)/(1e6));

		return maxRateRes;
	}

	@SuppressWarnings("static-access")
	public static int MMofflineLevelCut(ArrayList<Flow> flowArray, int[] sources, int[] targets, int run) {
		int maxRateRes = 0;
		//long startTimePre = System.nanoTime();

		long startTime = 0;
		if(flagMM[run]) {
			flagMM[run] = false;
			startTime = System.nanoTime();
			//System.out.println(run);
		}
		//		System.out.println("////"+sources);
		for(int i = 0; i < sources.length; i++) {
			//			for (int j = 0; j < NODESIZE; j++) {
			//				if(totalFlowForPairs[i][j] > 0) {//hopCountSD[run][i][j] > 0 && 
			Vertex source = vertices[outmostNodesSource[i]];//vertices[i];//[sources.get(i)];
			Vertex target = vertices[outmostNodesTarget[i]];//vertices[i];//[targets.get(i)];
			for (Vertex v : vertices) {
				v.minDistance = Double.MAX_VALUE;
				v.previous = null;
				v.visit = false;

			}

			for(Edge e: edges) {
				e.visit = false;
				e.flowCount = 1;
			}
			int times = 0;
			while(comPath.computePathsForAll(source, target, edges, vertices)==1) {
				times++;
				for (Vertex v : vertices) {
					v.minDistance = Double.MAX_VALUE;
					v.previous = null;
				}
			}

			//	}
			//System.out.println(source + "-"+target+": "+times + " times");
		}
		//		}
		//		}
		//long endTimePre = System.nanoTime();

		//System.out.println((endTimePre-startTimePre)/(1e6));
		if(flagMM[run] == false)
			startTime = System.nanoTime();
		for (Edge e: edges) {
			//System.out.println(e.id +". "+e.source+"-"+e.target+ ":\n"+e.pairs);
			e.flowCount = 1;
			for(String pair: e.pairs) {

				String[] sp = pair.split("-");

				int s = Integer.parseInt(sp[0]);
				int t = Integer.parseInt(sp[1]);

				e.totalPair = e.totalPair + totalFlowForPairs[s][t];


			}

			for(String pair: e.pairs) {

				String[] sp = pair.split("-");

				int s = Integer.parseInt(sp[0]);
				int t = Integer.parseInt(sp[1]);
				if (totalFlowForPairs[s][t] != 0)
					e.rate[s][t] = (float) totalFlowForPairs[s][t]/e.totalPair;

				//System.out.println(e.id + ". " +s+"-"+t+": "+ e.rate[s][t]);

			}


		}
		ArrayList<Flow> yeniFlowArray1 = new ArrayList<Flow>();
		ArrayList<Flow> yeniFlowArray2 = new ArrayList<Flow>();

		ArrayList<Flow> yeniFlowArray3 = new ArrayList<Flow>();

		ArrayList<Flow> yeniFlowArray4 = new ArrayList<Flow>();

		ArrayList<Flow> yeniFlowArray5 = new ArrayList<Flow>();

		LinkedList<ArrayList<Flow>> allArrays = new LinkedList<ArrayList<Flow>>();
		Collections.sort(flowArray,Collections.reverseOrder());

		for(Flow f: flowArray) {

			if(f.source.id == outmostNodesSource[0])
				yeniFlowArray1.add(f);
			else if (f.source.id == outmostNodesSource[1])
				yeniFlowArray2.add(f);
			else if (f.source.id == outmostNodesSource[2])
				yeniFlowArray3.add(f);
			else if (f.source.id == outmostNodesSource[3])
				yeniFlowArray4.add(f);
			else if (f.source.id == outmostNodesSource[4])
				yeniFlowArray5.add(f);
		}

		if(!yeniFlowArray1.isEmpty())
			allArrays.add(yeniFlowArray1);
		if(!yeniFlowArray2.isEmpty())
			allArrays.add(yeniFlowArray2);
		if(!yeniFlowArray3.isEmpty())
			allArrays.add(yeniFlowArray3);
		if(!yeniFlowArray4.isEmpty())
			allArrays.add(yeniFlowArray4);
		if(!yeniFlowArray5.isEmpty())
			allArrays.add(yeniFlowArray5);
		long preM = 0;
		for(ArrayList<Flow> Flows: allArrays) {
			for(Flow f: Flows)
				//System.out.println(f.source.name + "-> "+ f.target.name+": " + f.cost);
				//System.out.println("TT: " + sumOfFlow(Flows));

				for (Vertex v : vertices) {
					v.minDistance = Double.MAX_VALUE;
					v.previous = null;
					v.visit = false;

				}

			/*for(Edge e: edges) {
						e.visit = false;
						e.flowCount = 1;
					}*/

			int s = Flows.get(0).source.id;
			int t = Flows.get(0).target.id;
			try {

				if(lastIdea(Flows, run, s, t)) {
					maxRateRes += sumOfFlow(Flows);
				}
				else
					maxRateRes += comPath.maxOfFlow;

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println(">>"+(maxRateRes - preM) + "<<");
			preM = maxRateRes;
			comPath.setMax(0);
			//copyGraph2();
		}


		//		for (int i = flowArray.size()/2 + 1;  i < flowArray.size(); i++)
		//			yeniFlowArray.add(flowArray.get(i));


		/*		for (Flow f: flowArray) {

			int a = comPath.computePathwithRate(vertices, edges, f);//comPath.computePathwithRate(vertices, edges, f);

			if(a==1) {
				maxRateRes = maxRateRes + f.cost;

			}
			else
				lostDis = lostDis + 1;

		}
		 */		long endTime = System.nanoTime();
		 //disTime = disTime + (endTime-startTime);
		 lastIdeaTime += (endTime-startTime);
		 //System.out.println("Run: "+(endTime-startTime)/(1e6));

		 return maxRateRes;
	}


	@SuppressWarnings({ "static-access", "unchecked" })
	public static boolean lastIdea(ArrayList<Flow> flowArray, int level, int sourceNode, int targetNode)
			throws FileNotFoundException, IOException {

		int sum = sumOfFlow(flowArray);
		long startTime = 0;
		boolean flag = true;
		// copyGraph2();
		long startForCalc = System.nanoTime();
		long endForCalc = 0;
		if (cutLevelArray[sourceNode][targetNode] != null) {
			level = cutLevelArray[sourceNode][targetNode].level;
			keepEdgesByLevel = new LinkedList[level];
			int ll = 0;
			while (ll < level)
				keepEdgesByLevel[ll++] = new LinkedList<Edge>();

			// System.out.println("LLL " +level);
			// keepEdgesByLevel =
			// cutLevelArray[sourceNode][targetNode].keepEdges;
			ll = 0;
			while (ll < level) {
				for (int x = 0; x < cutLevelArray[sourceNode][targetNode].keepEdges[ll]
						.size(); x++)
					keepEdgesByLevel[ll]
							.add(edges
									.get(cutLevelArray[sourceNode][targetNode].keepEdges[ll]
											.get(x).id));
				ll++;
			}

			endForCalc = System.nanoTime();

			preProcessTime += (endForCalc - startForCalc);

			//	startTime = System.nanoTime();
		} else {

			flag = true;

			//Calculate hopcounts
			for (Vertex v : vertices) {
				v.minDistance = Double.MAX_VALUE;
				v.previous = null;
				v.visit = false;

			}
			comPath.hopCount(vertices[sourceNode], vertices[targetNode], edges,
					vertices);

			for (Vertex v : vertices) {
				v.minDistance = Double.MAX_VALUE;
				v.previous = null;
				v.visit = false;
			}

			//Compute levels with hop count
			comPath.computeLevelWithHopCount(vertices[sourceNode],
					vertices[targetNode], edges, vertices);
			//Arrange levels of cross and straight edges
			for(Edge e: edges){
				if(e.target.level > vertices[targetNode].level)
				{	 e.target.level = e.source.level;
				e.level = -1;
				}
			} 
			/*	 System.out.println("hc: " +  vertices[targetNode].hopCount);
			 for(Edge e: edges)
				 System.out.println("["+e.source + " -> " + e.target +"] s: "+e.source.level + " e: " + e.level + " t: " + e.target.level);*/
			//Level is the destination level
			level = vertices[targetNode].level;

			keepCutLevels k = new keepCutLevels();
			k.level = level;
			endForCalc = System.nanoTime();
			preProcessTime += (endForCalc - startForCalc);

			//	startTime = System.nanoTime();

			//Eger graph connected ise
			if (level != Integer.MAX_VALUE && level > 0) {
				//keep each cuts levels
				keepEdgesByLevel = new LinkedList[level];
				int ll = 0;
				//in each level we will keep edges
				while (ll < level)
					keepEdgesByLevel[ll++] = new LinkedList<Edge>();
				//Each edge kept level by level in the linklist array
				for (Edge e : edges) {
					if (e.level >= 0 && e.level < level)
						if (!keepEdgesByLevel[e.level].contains(e))
							keepEdgesByLevel[e.level].add(e);
				}
				//k keeps cut levels for next time
				k.keepEdges = keepEdgesByLevel;
				cutLevelArray[sourceNode][targetNode] = k;
			} else {

				//no connected graph
				flag = false;
			}
		}

		startTime = System.nanoTime();
		//If connected graph
		if (flag) {

			int ll = 0;
			//keeps total of all edges in each level
			LinkedList<Integer> listOfMinLevels = new LinkedList<Integer>();

			/*			while (ll < level) {

				int tot = totOfEdges(keepEdgesByLevel[ll], edges, sourceNode, targetNode);

				//if (sum > tot)
					//sum = tot;
				ll++;
			}
			ll = 0;
			System.out.println("new Sum: "+ sum + " "+ level);
			 */			//flowArray.get(0).cost = sum; //Burasi degisecek
			//int ls = 0; //min of min
			while (ll < level) {

				double tot = totOfEdges(keepEdgesByLevel[ll], edges, sourceNode, targetNode);
				//If total is greater than sum or less than 1.5 * sum
				//System.out.println(ll + "== " + tot);

				if (tot >= sum && tot <= (1.5) * sum)
					listOfMinLevels.add(ll);

				ll++;
			}
			//System.out.println("-- " + listOfMinLevels);
			//If there is a cut can cause bottleneck
			/*			if (listOfMinLevels.size() > 0) {
				ll = 0;
				while (ll < listOfMinLevels.size()) {
					//Try to share flow
					if (edgeFit(flowArray, edges,
							keepEdgesByLevel[listOfMinLevels.get(ll)]) == 1) {
						//If shared arrange flow
						//flowArrange(listOfSharedEdges);
						System.out.println(">>");
						ll++;
					}
					ll++;
				}
			}*/

			ll = 0;
			//	System.out.println("FF: " + flowArray.size());
			while (ll < listOfMinLevels.size()) {
				//Try to share flow
				if (edgeFitNew(flowArray, edges,
						keepEdgesByLevel[listOfMinLevels.get(ll)]) == 1) {
					//If shared arrange flow
					flowArrange(listOfSharedEdges);
					ll++;
				}
				ll++;
			}			
			//		System.out.println("FFLL: " + flowArray.size());			
			//if destination is not added to targetlist then we add overhere.
			for (Flow f : flowArray) {
				if (!f.targetList.contains(vertices[targetNode]))
					f.targetList.add(vertices[targetNode]);	  
			}

			/*			for(Flow f: flowArray) {
				if(f.targetList.size()>2) {
					System.out.println(f.cost+": " + f.targetList);
					Dijkstra dj = new Dijkstra();
					Edge e = dj.findEdge(f.targetList.get(1).id, f.targetList.get(2).id, edges);

					System.out.println(e.capacity);
				}
			}*/
			/* for(Flow f: flowArray){
			 if(f.targetList.size() > 2){
				  System.out.println(flowArray.size() + " SEQUENCE");
				  System.out.println("F" + f.id+ " c: " + f.cost);
				  for(Vertex v: f.targetList) 
					  System.out.print(v+" "); System.out.println();
			 }
		 }*/


			long endTime;
			startTime = System.nanoTime();
			//Then compute level by level
			//if all sent return true o.w. return false.
			if (comPath.computeLevelByLevel(sum, flowArray, edges, vertices, alpha) == 1) {

				//if all sent stop time and add last idea time
				endTime = System.nanoTime();
				//System.out.println(".");
				lastIdeaTime += (endTime - startTime);
				/*				edges.clear();
				for (int i = 0; i < vertices.length; i++) {
					vertices[i].adjacencies.clear();
					vertices[i].inDemandFlows.clear();
					vertices[i].outDemandFlows.clear();

				}*/

				return true;
			}
			//System.out.println("asss "+comPath.getMax());
			//if false stop and add to lastidea time
			endTime = System.nanoTime();

			lastIdeaTime += (endTime - startTime);

			/*			edges.clear();
			for (int i = 0; i < vertices.length; i++) {
				vertices[i].adjacencies.clear();
				vertices[i].inDemandFlows.clear();
				vertices[i].outDemandFlows.clear();

			}*/

			return false;


		}

		long endTime = System.nanoTime();

		lastIdeaTime += (endTime - startTime);

		/*		edges.clear();
		for (int i = 0; i < vertices.length; i++) {
			vertices[i].adjacencies.clear();
			vertices[i].inDemandFlows.clear();
			vertices[i].outDemandFlows.clear();

		}*/
		return false;

	}



	public static void readFileYeni() throws FileNotFoundException, IOException {

		String fileName = "test1.brite";
		Rand = new Random();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			int sourceId = 0;
			int targetId = 0;
			int edgeId = 0;
			int k = 0;
			while ((line = br.readLine()) != null) {

				// line = line.trim();
				line = line.replaceAll("\\s", " ");

				String[] s = line.split(" ");

				edgeId = Integer.parseInt(s[0].toString());
				sourceId = Integer.parseInt(s[1].toString());

				targetId = Integer.parseInt(s[2].toString());
				// s[5] = s[5].split("\\.")[0];
				int bw = 10 + Rand.nextInt(100);// Integer.parseInt(s[5].toString());

				Vertex u = vertices[sourceId];
				Vertex v = vertices[targetId];

				Vertex uTemp = verticesTemp[sourceId];
				Vertex vTemp = verticesTemp[targetId];

				Edge e = new Edge(sourceId, targetId, u, v, bw, edgeId);

				// System.out.println("E: " + e.id + " s: " + e.source + " t: "
				// + e.target + " bw: " + e.capacity);

				edges.add(e);
				edgesTemp.add(e);

				u.adjacencies.add(e);
				uTemp.adjacencies.add(e);

			}
		}
		// wr.close();
	}

	public static void readFileYeni2(int run) throws FileNotFoundException, IOException {
		Path path = Paths.get("C:\\Users\\erdalEv\\eclipse-workspace\\levelCut-EV\\src\\test2");
		String sP = path.toString();
		String fileName = sP+"\\test"+run+ ".brite";
		//System.out.println(fileName);
		Rand = new Random();
		boolean flag = true;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			int sourceId = 0;
			int targetId = 0;
			int edgeId = 0;
			int k = 0;
			while ((line = br.readLine()) != null) {

				// line = line.trim();
				line = line.replaceAll("\\s", " ");
				String[] s = line.split(" ");

				if(s[0].toLowerCase().equals("Edges:".toLowerCase())){

					flag = false;
					continue;
				}
				if(flag){

					continue;
				}



				//edgeId = Integer.parseInt(s[0].toString());
				sourceId = Integer.parseInt(s[1].toString());

				targetId = Integer.parseInt(s[2].toString());
				s[5] = s[5].split("\\.")[0];
				int bw =  Integer.parseInt(s[5].toString());////100 + Rand.nextInt(bandwidthRange);//

				Vertex u = vertices[sourceId];
				Vertex v = vertices[targetId];

				Vertex uTemp = verticesTemp[sourceId];
				Vertex vTemp = verticesTemp[targetId];

				Edge e1 = new Edge(sourceId, targetId, u, v, bw, edgeId);
				edgeId++;
				Edge e2 = new Edge(targetId, sourceId, v, u, bw, edgeId);

				// System.out.println("E: " + e.id + " s: " + e.source + " t: "
				// + e.target + " bw: " + e.capacity);

				edges.add(e1);
				edgesTemp.add(e1);
				edges.add(e2);
				edgesTemp.add(e2);

				u.adjacencies.add(e1);
				u.adjV.add(v);
				uTemp.adjacencies.add(e1);
				uTemp.adjV.add(vTemp);
				v.adjacencies.add(e2);
				v.adjV.add(u);
				vTemp.adjacencies.add(e2);
				vTemp.adjV.add(uTemp);

			}
		}
		// wr.close();
	}


	public static void readFile() throws FileNotFoundException, IOException {

		int edgeId = 0;
		BufferedWriter writer = new BufferedWriter(new FileWriter("Demands.txt"));
		try (BufferedReader br = new BufferedReader(new FileReader("test1.txt"))) {
			String line;
			int sourceId = 0;
			int k = 0;

			while ((line = br.readLine()) != null) {

				line = line.trim();
				String[] s = line.split(" ");

				if (s[0].equals("NodeID:")) {

					sourceId = Integer.parseInt(s[1]);
					k = 0;
					//					if (sourceId == 0) {
					//						br.readLine();
					//						continue;

					//					}

				} else if (s[0].equals("LinkTo:")) {
					// System.out.println(line);
					// String[] sT = line.split("  ");
					// String[] ss = sT[1].split(" ");

					int targetId = Integer.parseInt(s[1]);
					// System.out.println("t: " + targetId);
					//					if (targetId == 0)
					//						continue;
					// System.out.println("t: " + targetId);
					int bw = 0;

					// if(searchNodes(targetId,2) || searchNodes(sourceId,1))
					// bw = Rand.nextInt(10);
					// else
					bw = 100 + Rand.nextInt(bandwidthRange); // burda randomly
					String toF=Integer.toString(sourceId) + "\t" + Integer.toString(targetId)+"\t"+Integer.toString(bw) + "\n";
					writer.write(toF);
					// 10-100 yap.

					// System.out.println("BW: " + bw);
					// double bw = 15;
					Vertex v = vertices[targetId];
					Vertex u = vertices[sourceId];
					Vertex uTemp = verticesTemp[sourceId];
					Vertex vTemp = verticesTemp[targetId];
					Edge e = new Edge(sourceId, targetId, u, v, bw, edgeId++);
					edges.add(e);
					edgesTemp.add(e);

					u.adjacencies.add(e);
					uTemp.adjacencies.add(e);
					if(!u.adjV.contains(v))
						u.adjV.add(v);
					if(!v.adjV.contains(u))
						v.adjV.add(u);
					if(!uTemp.adjV.contains(v))
						uTemp.adjV.add(vTemp);
					if(!vTemp.adjV.contains(uTemp))
						vTemp.adjV.add(uTemp);



				}
			}
		}
	}

	//Share flows in cuts
	public static int edgeFitNew(ArrayList<Flow> flowArray,
			LinkedList<Edge> edges, LinkedList<Edge> list) {
		int i = 0;
		int l = 0;
		int k = 0;
		while (i < beta) {
			l = 0;
			k = 0;

			listOfSharedEdges = new LinkedList<SharedEdges>();
			// copying again.
			for (int as = 0; as < list.size(); as++) {
				Edge e = list.get(as);
				e.capacity = e.capTemp;
				e.possibleFlow.clear();

			}

			if (i == 0)
				Collections.sort(flowArray, Collections.reverseOrder());
			else if (i == 1)
				Collections.sort(flowArray);
			else
				Collections.shuffle(flowArray);

			while (l < flowArray.size() && k < list.size()) {

				int capOfEdge = list.get(k).capacity;
				if (flowArray.get(l).cost <= capOfEdge) {
					//eger remaining capacity costtan buyukse put keep as shared edge for arranging next
					SharedEdges se = new SharedEdges(list.get(k).id,
							flowArray.get(l).id, list.get(k));
					listOfSharedEdges.add(se);
					//System.out.println("k: "+ k + " - " + list.get(k).capacity);
					list.get(k).capacity -= flowArray.get(l).cost;
					l++;
				} else {
					k++;

				}
			}
			if (l == flowArray.size() ) { // means done
				//System.out.println("FLX: " + flowArray.size());
				//System.out.println(list.get(k).id +"-"+ list.get(k).capacity);
				return 1;
			} else {
				listOfSharedEdges.clear();

			}
			i++;
		}

		return 0;
	}
	public static int edgeFit(ArrayList<Flow> flowArray,
			LinkedList<Edge> edges, LinkedList<Edge> list) {

		int k = 0;

		int l = 0;

		int i = 0;

		int j = 0;
		boolean flagS = true;
		while (flagS) {
			// System.out.println("A: " + alpha);
			i = 0;
			while (i < beta) {
				l = 0;
				k = 0;

				listOfSharedEdges = new LinkedList<SharedEdges>();
				// copying again.
				for (int as = 0; as < list.size(); as++) {
					Edge e = list.get(as);
					e.capacity = e.capTemp;
					e.possibleFlow.clear();

				}

				if (i == 0)
					Collections.sort(flowArray, Collections.reverseOrder());
				else if (i == 1)
					Collections.sort(flowArray);
				else
					Collections.shuffle(flowArray);
				boolean flagT = false;
				while (l < flowArray.size() && k < list.size()) {

					int capOfEdge = list.get(k).capacity;
					if (flowArray.get(l).cost <= capOfEdge) {
						//eger remaining capacity costtan buyukse put keep as shared edge for arranging next
						//SharedEdges se = new SharedEdges(list.get(k).id,
						//		flowArray.get(l).id, list.get(k));
						//listOfSharedEdges.add(se);
						//System.out.println("k: "+ k + " - " + list.get(k).capacity);
						list.get(k).capacity -= flowArray.get(l).cost;
						l++;
						flagT = true;
					} else {
						flagT = false;
						k++;

					}
				}
				if (l == flowArray.size() && flagT) { // means done
					//System.out.println("FLX: " + flowArray.size());
					//System.out.println(list.get(k).id +"-"+ list.get(k).capacity);
					return 1;
				}
				i++;
			}

			Collections.sort(flowArray, Collections.reverseOrder());

			Flow ff = flowArray.get(0);

			int x = ff.cost;
			//System.out.println("xx = " + x);
			int y = x/2;
			x = x - y;
			//System.out.println("x = " + x + " y = " + y);
			flowArray.get(0).cost = x;

			Flow ffx = new Flow(ff.source, ff.target, ff.id, flowArray.size());

			ffx.cost = y;

			flowArray.add(ffx);


			/*for (Flow f : flowArray) {
				System.out.println(f.cost);
			}*/
		}
		//System.out.println("FL: " + flowArray.size());

		return 0;

	}

	//Shared edgeleri duzenleyecez
	public static void flowArrange(LinkedList<SharedEdges> listOfSharedEdges) {

		//
		for (SharedEdges se : listOfSharedEdges) {

			//Edge in se
			Edge e = edges.get(se.id);
			//soruce and target of e
			Vertex s = vertices[e.sourceId];
			Vertex t = vertices[e.targetId];
			//If source does not have flow insert
			if (!s.outDemandFlows.contains(se.flow)) {
				s.outDemandFlows.add(se.flow);
				// s.outDemandFlowsTemp.add(se.flow);
			}
			//If target does not have flow insert
			if (!t.inDemandFlows.contains(se.flow)) {
				t.inDemandFlows.add(se.flow);
				// t.inDemandFlowsTemp.add(se.flow);
			}
			//e possible flow add
			if (!e.possibleFlow.contains(se.flow)) {
				e.possibleFlow.add(se.flow);

			}

		}
		//Son duzenleme, eger her flow icin her edge flow a sahipse target listine ekle
		for (Flow f : flowArray) {
			for (Edge e : edges) {
				if (e.possibleFlow.contains(f.id)) {
					if (!f.targetList.contains(e.source))
						f.targetList.add(e.source);
					if (!f.targetList.contains(e.target))
						f.targetList.add(e.target);
				}
			}
		}
	}

	public static void basicGraph() {

		Edge e1 = new Edge(0, 1, vertices[0], vertices[1], 5, 0);
		Edge e2 = new Edge(0, 2, vertices[0], vertices[2], 4, 1); // destination
		// was 3
		Edge e3 = new Edge(1, 3, vertices[1], vertices[3], 6, 2);
		Edge e4 = new Edge(1, 2, vertices[1], vertices[2], 6, 3);
		Edge e5 = new Edge(2, 4, vertices[2], vertices[4], 3, 4);
		Edge e6 = new Edge(2, 1, vertices[2], vertices[1], 6, 5);
		Edge e7 = new Edge(3, 5, vertices[3], vertices[5], 7, 6);
		Edge e8 = new Edge(4, 5, vertices[4], vertices[5], 2, 7);
		Edge e9 = new Edge(3, 4, vertices[3], vertices[4], 6, 8);
		Edge e10 = new Edge(4, 3, vertices[4], vertices[3], 6, 9);

		// Edge e11 = new Edge(1,6,vertices[1],vertices[6],1,10);
		// Edge e12 = new Edge(6,7,vertices[6],vertices[7],1,11);
		// Edge e13 = new Edge(7,3,vertices[7],vertices[3],1,12);
		// Edge e4 = new Edge(2,3,vertices[3],10,3);
		// Edge e5 = new Edge(2,1,vertices[1],10,4);

		vertices[0].adjacencies.add(e1);
		vertices[0].adjacencies.add(e2);

		vertices[1].adjacencies.add(e3);
		vertices[1].adjacencies.add(e4);

		// vertices[1].adjacencies.add(e11);

		vertices[2].adjacencies.add(e5);
		vertices[2].adjacencies.add(e6);

		vertices[3].adjacencies.add(e7);
		vertices[3].adjacencies.add(e9);

		vertices[4].adjacencies.add(e8);
		vertices[4].adjacencies.add(e10);

		// vertices[6].adjacencies.add(e12);
		// vertices[7].adjacencies.add(e13);

		edges.add(e1);
		edges.add(e2);
		edges.add(e3);
		edges.add(e4);
		edges.add(e5);
		edges.add(e6);
		edges.add(e7);
		edges.add(e8);
		edges.add(e9);
		edges.add(e10);
		// edges.add(e11);
		// edges.add(e12);
		// edges.add(e13);

		verticesTemp[0].adjacencies.add(e1);
		verticesTemp[0].adjacencies.add(e2);

		verticesTemp[1].adjacencies.add(e3);
		verticesTemp[1].adjacencies.add(e4);

		// verticesTemp[1].adjacencies.add(e11);

		verticesTemp[2].adjacencies.add(e5);
		verticesTemp[2].adjacencies.add(e6);

		verticesTemp[3].adjacencies.add(e7);
		verticesTemp[3].adjacencies.add(e9);

		verticesTemp[4].adjacencies.add(e8);
		verticesTemp[4].adjacencies.add(e10);

		// verticesTemp[6].adjacencies.add(e12);
		// verticesTemp[7].adjacencies.add(e13);

		edgesTemp.add(e1);
		edgesTemp.add(e2);
		edgesTemp.add(e3);
		edgesTemp.add(e4);
		edgesTemp.add(e5);
		edgesTemp.add(e6);

		edgesTemp.add(e7);
		edgesTemp.add(e8);
		edgesTemp.add(e9);
		edgesTemp.add(e10);

		// edgesTemp.add(e11);
		// edgesTemp.add(e12);
		// edgesTemp.add(e13);

	}

	public static void basicGraph4() {

		Edge e1 = new Edge(0, 1, vertices[0], vertices[1], 5, 0);
		Edge e2 = new Edge(0, 2, vertices[0], vertices[2], 4, 1); // destination
		// was 3

		Edge e3 = new Edge(1, 3, vertices[1], vertices[3], 5, 2);
		Edge e4 = new Edge(2, 3, vertices[2], vertices[3], 4, 3);
		// Edge e5 = new Edge(1,2,vertices[1],vertices[2],6,4);
		// Edge e6 = new Edge(2,1,vertices[2],vertices[1],6,5);
		vertices[0].adjacencies.add(e1);
		vertices[0].adjacencies.add(e2);

		// vertices[1].adjacencies.add(e5);
		vertices[1].adjacencies.add(e3);

		// vertices[2].adjacencies.add(e6);
		vertices[2].adjacencies.add(e4);

		edges.add(e1);
		edges.add(e2);
		// edges.add(e5);
		// edges.add(e6);
		edges.add(e3);
		edges.add(e4);

		verticesTemp[0].adjacencies.add(e1);
		verticesTemp[0].adjacencies.add(e2);

		// verticesTemp[1].adjacencies.add(e5);
		verticesTemp[1].adjacencies.add(e3);

		// verticesTemp[2].adjacencies.add(e6);
		verticesTemp[2].adjacencies.add(e4);

		edgesTemp.add(e1);
		edgesTemp.add(e2);
		// edgesTemp.add(e5);
		// edgesTemp.add(e6);
		edgesTemp.add(e3);
		edgesTemp.add(e4);
	}

	public static void basicGraph3() {

		Edge e1 = new Edge(0, 1, vertices[0], vertices[1], 5, 0);
		Edge e2 = new Edge(0, 2, vertices[0], vertices[2], 4, 1); // destination
		// was 3
		Edge e3 = new Edge(1, 3, vertices[1], vertices[3], 4, 2);
		Edge e4 = new Edge(2, 4, vertices[2], vertices[4], 5, 3);
		Edge e5 = new Edge(1, 2, vertices[1], vertices[2], 6, 4);
		Edge e6 = new Edge(2, 1, vertices[2], vertices[1], 6, 5);
		Edge e7 = new Edge(3, 4, vertices[3], vertices[4], 6, 6);
		Edge e8 = new Edge(4, 3, vertices[4], vertices[3], 6, 7);
		Edge e9 = new Edge(3, 5, vertices[3], vertices[5], 9, 8);
		Edge e10 = new Edge(4, 5, vertices[4], vertices[5], 1, 9);
		Edge e11 = new Edge(1, 6, vertices[1], vertices[6], 1, 10);
		Edge e12 = new Edge(6, 7, vertices[6], vertices[7], 1, 11);
		Edge e13 = new Edge(5, 4, vertices[5], vertices[4], 6, 12);
		Edge e14 = new Edge(7, 8, vertices[7], vertices[8], 1, 13);
		Edge e15 =  new Edge(8, 9, vertices[8], vertices[9], 1, 14);
		Edge e16 = new Edge(9, 3, vertices[9], vertices[3], 1, 15);
		// Edge e4 = new Edge(2,3,vertices[3],10,3);
		// Edge e5 = new Edge(2,1,vertices[1],10,4);

		vertices[0].adjacencies.add(e1);
		vertices[0].adjacencies.add(e2);

		vertices[1].adjacencies.add(e3);
		vertices[1].adjacencies.add(e5);

		vertices[1].adjacencies.add(e11);
		verticesTemp[1].adjacencies.add(e11);

		vertices[2].adjacencies.add(e4);
		vertices[2].adjacencies.add(e6);

		vertices[3].adjacencies.add(e7);
		vertices[3].adjacencies.add(e9);

		vertices[4].adjacencies.add(e8);
		vertices[4].adjacencies.add(e10);

		vertices[6].adjacencies.add(e12);
		verticesTemp[6].adjacencies.add(e12);

		vertices[5].adjacencies.add(e13);
		verticesTemp[5].adjacencies.add(e13);

		vertices[7].adjacencies.add(e14);
		verticesTemp[7].adjacencies.add(e14);

		vertices[8].adjacencies.add(e15);
		verticesTemp[8].adjacencies.add(e15);

		vertices[9].adjacencies.add(e16);
		verticesTemp[9].adjacencies.add(e16);
		edges.add(e1);
		edges.add(e2);
		edges.add(e3);
		edges.add(e4);
		edges.add(e5);
		edges.add(e6);
		edges.add(e7);
		edges.add(e8);
		edges.add(e9);
		edges.add(e10);
		edges.add(e11);
		edges.add(e12);
		edges.add(e13);
		edges.add(e14);
		edges.add(e15);
		edges.add(e16);

		verticesTemp[0].adjacencies.add(e1);
		verticesTemp[0].adjacencies.add(e2);

		verticesTemp[1].adjacencies.add(e5);
		verticesTemp[1].adjacencies.add(e4);

		verticesTemp[2].adjacencies.add(e3);
		verticesTemp[2].adjacencies.add(e6);

		verticesTemp[3].adjacencies.add(e7);
		verticesTemp[3].adjacencies.add(e9);

		verticesTemp[4].adjacencies.add(e8);
		verticesTemp[4].adjacencies.add(e10);

		edgesTemp.add(e1);
		edgesTemp.add(e2);
		edgesTemp.add(e3);
		edgesTemp.add(e4);
		edgesTemp.add(e5);
		edgesTemp.add(e6);
		edgesTemp.add(e7);
		edgesTemp.add(e8);
		edgesTemp.add(e9);
		edgesTemp.add(e10);
		edgesTemp.add(e11);
		edgesTemp.add(e12);
		edgesTemp.add(e13);
		edgesTemp.add(e14);
		edgesTemp.add(e15);
		edgesTemp.add(e16);

	}

	public static void basicGraph2() {

		Edge e1 = new Edge(0, 1, vertices[0], vertices[1], 5, 0);
		Edge e2 = new Edge(0, 2, vertices[0], vertices[2], 4, 1); // destination
		// was 3

		Edge e3 = new Edge(1, 2, vertices[1], vertices[2], 6, 2);
		Edge e4 = new Edge(1, 3, vertices[1], vertices[3], 4, 3);

		Edge e5 = new Edge(2, 1, vertices[2], vertices[1], 6, 4);
		Edge e6 = new Edge(2, 4, vertices[2], vertices[4], 5, 5);

		Edge e7 = new Edge(3, 4, vertices[3], vertices[4], 6, 6);
		Edge e8 = new Edge(3, 5, vertices[3], vertices[5], 6, 7);

		Edge e9 = new Edge(4, 3, vertices[4], vertices[3], 6, 8);
		Edge e10 = new Edge(4, 6, vertices[4], vertices[6], 3, 9);

		Edge e11 = new Edge(5, 6, vertices[5], vertices[6], 6, 10);
		Edge e12 = new Edge(5, 7, vertices[5], vertices[7], 7, 11);

		Edge e13 = new Edge(6, 5, vertices[6], vertices[5], 6, 12);
		Edge e14 = new Edge(6, 8, vertices[6], vertices[8], 2, 13);

		Edge e15 = new Edge(7, 8, vertices[7], vertices[8], 6, 14);
		Edge e16 = new Edge(7, 9, vertices[7], vertices[9], 3, 15);

		Edge e17 = new Edge(8, 7, vertices[8], vertices[7], 6, 16);
		Edge e18 = new Edge(8, 9, vertices[8], vertices[9], 6, 17);

		vertices[0].adjacencies.add(e1);
		vertices[0].adjacencies.add(e2);

		vertices[1].adjacencies.add(e3);
		vertices[1].adjacencies.add(e4);

		vertices[2].adjacencies.add(e5);
		vertices[2].adjacencies.add(e6);

		vertices[3].adjacencies.add(e7);
		vertices[3].adjacencies.add(e8);

		vertices[4].adjacencies.add(e9);
		vertices[4].adjacencies.add(e10);

		vertices[5].adjacencies.add(e11);
		vertices[5].adjacencies.add(e12);

		vertices[6].adjacencies.add(e13);
		vertices[6].adjacencies.add(e14);

		vertices[7].adjacencies.add(e15);
		vertices[7].adjacencies.add(e16);

		vertices[8].adjacencies.add(e17);
		vertices[8].adjacencies.add(e18);

		edges.add(e1);
		edges.add(e2);
		edges.add(e3);
		edges.add(e4);
		edges.add(e5);
		edges.add(e6);

		edges.add(e7);
		edges.add(e8);
		edges.add(e9);
		edges.add(e10);
		edges.add(e11);
		edges.add(e12);

		edges.add(e13);
		edges.add(e14);
		edges.add(e15);
		edges.add(e16);
		edges.add(e17);
		edges.add(e18);

		verticesTemp[0].adjacencies.add(e1);
		verticesTemp[0].adjacencies.add(e2);

		verticesTemp[1].adjacencies.add(e3);
		verticesTemp[1].adjacencies.add(e4);

		verticesTemp[2].adjacencies.add(e5);
		verticesTemp[2].adjacencies.add(e6);

		verticesTemp[3].adjacencies.add(e7);
		verticesTemp[3].adjacencies.add(e8);

		verticesTemp[4].adjacencies.add(e9);
		verticesTemp[4].adjacencies.add(e10);

		verticesTemp[5].adjacencies.add(e11);
		verticesTemp[5].adjacencies.add(e12);

		verticesTemp[6].adjacencies.add(e13);
		verticesTemp[6].adjacencies.add(e14);

		verticesTemp[7].adjacencies.add(e15);
		verticesTemp[7].adjacencies.add(e16);

		verticesTemp[8].adjacencies.add(e17);
		verticesTemp[8].adjacencies.add(e18);

		edgesTemp.add(e1);
		edgesTemp.add(e2);
		edgesTemp.add(e3);
		edgesTemp.add(e4);
		edgesTemp.add(e5);
		edgesTemp.add(e6);

		edgesTemp.add(e7);
		edgesTemp.add(e8);
		edgesTemp.add(e9);
		edgesTemp.add(e10);
		edgesTemp.add(e11);
		edgesTemp.add(e12);

		edgesTemp.add(e13);
		edgesTemp.add(e14);
		edgesTemp.add(e15);
		edgesTemp.add(e16);
		edgesTemp.add(e17);
		edgesTemp.add(e18);

	}

	public static void copyGraph2() {

		for (int i = 0; i < edgesTemp.size(); i++) {
			edges.add(edgesTemp.get(i));
			edges.get(i).capacity = edges.get(i).capTemp;
		}

		for (int i = 0; i < verticesTemp.length; i++) {

			if (verticesTemp[i].adjacencies != null) {
				for (int j = 0; j < verticesTemp[i].adjacencies.size(); j++) {

					vertices[i].adjacencies.add(verticesTemp[i].adjacencies
							.get(j));
					// vertices[i].inDemandFlows.addAll(verticesTemp[i].inDemandFlows);
					// vertices[i].outDemandFlows.addAll(verticesTemp[i].outDemandFlows);
				}

			}

			// vertices[i] = verticesTemp[i];
		}
	}

	public static void intializeVertices(Vertex[] vert, int size) {

		for (int i = 0; i < size; i++) {
			String name = "v" + Integer.toString(i);
			vert[i] = new Vertex(name, i);
		}
	}

	public static Vertex findSource(int i, Vertex[] vertices) {
		Vertex s = null;

		for (int k = 0; k < vertices.length; k++) {
			s = vertices[k];
			if (s.id == i)
				break;
		}
		return s;
	}

	public static Vertex findTarget(int i, Vertex[] vertices) {
		Vertex s = null;

		for (int k = 0; k < vertices.length; k++) {
			s = vertices[k];
			if (s.id == i)
				break;
		}
		return s;
	}

	public Edge findEdge(int i, int j, LinkedList<Edge> edges) {
		Edge e = null;
		ListIterator<Edge> listIterator = edges.listIterator();
		while (listIterator.hasNext()) {
			e = listIterator.next();
			if (e.sourceId == i && j == e.targetId)
				break;
		}

		return e;
	}

	public LinkedList<Edge> findEdgeOfTarget(int targetId,
			LinkedList<Edge> edges) {
		Edge e = null;

		LinkedList<Edge> temp = new LinkedList<Edge>();
		// System.out.println("edge: " + edges.size());
		ListIterator<Edge> listIterator = edges.listIterator();
		while (listIterator.hasNext()) {
			e = listIterator.next();
			if (e.targetId == targetId)
				temp.add(e);
			// System.out.println("E: " + e.id);
		}

		return temp;
	}

	public LinkedList<Edge> findEdgeOfSource(int sourceId,
			LinkedList<Edge> edges) {
		Edge e = null;

		LinkedList<Edge> temp = new LinkedList<Edge>();

		ListIterator<Edge> listIterator = edges.listIterator();
		while (listIterator.hasNext()) {
			e = listIterator.next();
			if (e.sourceId == sourceId)
				temp.add(e);
		}

		return temp;
	}

	public static double totOfEdges(LinkedList<Edge> list, LinkedList<Edge> edges, int s, int t) {
		double sum = 0;
		//for (int i = 0; i < list.size(); i++) {
		for (Edge e : list) {
			// Edge e = edges.get(list.get(i).id);
			//e = list.get(i);
			//System.out.println( "--" + e.sourceId + "->" + e.targetId);
			sum = sum + (double) e.capTemp*e.rate[s][t];
		}
		return sum;
	}

	public static void printLevels() {

		for (int i = 0; i < vertices.length; i++)
			System.out.println(vertices[i].id + ". v level: "
					+ vertices[i].level);
		System.out
		.println("*****************************************************");

		for (int i = 0; i < edges.size(); i++)
			System.out.println(edges.get(i).id + ". e level: "
					+ edges.get(i).level);
	}

	public static int sumOfFlow(ArrayList<Flow> flows) {

		int sum = 0;

		for (int i = 0; i < flows.size(); i++) {
			sum += flows.get(i).cost;
		}

		return sum;
	}

	public static double calcMeanCI(SummaryStatistics stats, double level) {

		try {
			TDistribution tDist = new TDistribution(stats.getN()-1);
			double cr = tDist.inverseCumulativeProbability(1.0-(1-level)/2);
			return cr*stats.getStandardDeviation()/Math.sqrt(stats.getN());
		}catch(MathIllegalArgumentException e){

			return Double.NaN;
		}
	}
}
