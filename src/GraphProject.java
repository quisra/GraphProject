import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import java.io.File;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
public class GraphProject {
	static String styleSheet =
			"node{"+
					"fill-color: black;"+
					"size: 12px;"+
					"text-size: 50;" + "text-alignment: at-left;" +"text-color: green;"+ 
					"}"+
			"node.Target{"+
					"fill-color: red;"+
			"}";
		
	
	public static class HashInput{
		int distance;
		String prevNode;
		public HashInput(int d, String n) {
			this.distance=d;
			this.prevNode=n;
		}
	}

	
	public static ArrayList<String> ShortestPath(Graph g, String s, String e) {
		PriorityQueue<String>pq = new PriorityQueue<String>();
		HashMap<String,Integer> visited = new HashMap<>();
		HashMap<String, HashInput> Hmap = new HashMap<>();
		int EDistance =1;
		int calc = 0;
		Node tempNode = g.getNode(s);
		Node tempNode2=null;
		Edge tempEdge= tempNode.getEdge(0);
		pq.add(s); //adds the start vertex
		//Creates a hashmap that stores all the vertices, distance to them from the src, and the prev node
		for(int i=0;i<g.getNodeCount();i++) {
			if(g.getNode(i).getId().equals(s)) {
				Hmap.put(g.getNode(i).getId(),new HashInput(0,null));
			}else {
				Hmap.put(g.getNode(i).getId(),new HashInput(Integer.MAX_VALUE,null));
			}
			
		}
		
		while(!pq.isEmpty()) {
			tempNode=g.getNode(pq.poll());
			if(visited.containsKey(tempNode.getId())) {continue;}
			visited.put(tempNode.getId(), null);
			//goes through all the neighbors
			for(int i=0;i<tempNode.getDegree();i++) {
				tempEdge=tempNode.getEdge(i);
				tempNode2=tempEdge.getOpposite(tempNode); //gets the neighbor
				if(!visited.containsKey(tempNode2.getId())) {
					calc = (Hmap.get(tempEdge.getOpposite(tempNode2).getId()).distance) + EDistance;
					if(calc<Hmap.get(tempNode2.getId()).distance) {
						Hmap.put(tempNode2.getId(), new HashInput(calc,tempEdge.getOpposite(tempNode2).getId()));
					}
					pq.add(tempNode2.getId());
				}
			}
		}

		
		
		HashInput reverse = new HashInput(0,null);
		ArrayList<String> ReverseA = new ArrayList<String>();
		reverse=Hmap.get(e);
		ReverseA.add(e);
		while(reverse.prevNode!=null) {	
			ReverseA.add(reverse.prevNode);
			reverse=Hmap.get(reverse.prevNode);
		}
		Collections.reverse(ReverseA);
		return ReverseA;
	}
	
	public static Graph readingGraphfromFile(File f) {
		try {
			Scanner scnr = new Scanner(f);
			Graph g = new SingleGraph("Random");
			g.setStrict(false);
			/*
			 * Below code creates nodes from the file
			 */
			while(scnr.hasNext()) {
				String fullLine = scnr.nextLine();
				if(fullLine=="") {
					continue;       // skips the blank line in between the numbers
				}
				String[] parsedLine = fullLine.split("\\s");
				for(int i=0;i<parsedLine.length;i++) {
					g.addNode(parsedLine[i]);
				}
			}
			scnr= new Scanner(f);
			/*
			 * Below code creates edges from the file
			 */
			while(scnr.hasNext()) {
				String fullLine = scnr.nextLine();
				if(fullLine=="") {
					continue;   //skips the blank line in between the numbers
				}
				String[] parsedLine = fullLine.split("\\s");
				for(int i=1;i<parsedLine.length;i++) {
				g.addEdge(parsedLine[0]+parsedLine[i],g.getNode(parsedLine[0]),g.getNode(parsedLine[i]));
				}
				
			}
			scnr.close();
			return g;
		}catch(Exception e) {
			System.out.print(e);
		}
		return null;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scn = new Scanner(System.in);
		ArrayList<String> Alist = new ArrayList<String>();
		File graphFile = new File("tree.txt");
		Graph graph = new SingleGraph("Random");
		String start = null;
		String end = null;
		Node test;
		graph.setStrict(false);
		graph.setAutoCreate(true);
		graph = readingGraphfromFile(graphFile);
		for (Node node : graph) {
			node.setAttribute("ui.label", node.getId());
		}
		System.setProperty("org.graphstream.ui", "swing");
		graph.setAttribute("ui.stylesheet", styleSheet);
		graph.display();
		do {
			System.out.println("Enter in source node or q to finish");
			start=scn.nextLine();
			if(start.equals("q")) {continue;}
			System.out.println("Enter in end node");
			end=scn.nextLine();
			Alist=ShortestPath(graph,start,end);
			System.out.println("Shortest Path from "+start+ " to "+end+" is "+Alist);
			for (Node node : graph) {
				node.setAttribute("ui.label", node.getId());
			}
			
			System.setProperty("org.graphstream.ui", "swing");
			graph.setAttribute("ui.stylesheet", styleSheet);
			for(int i=0;i<Alist.size();i++) {
				test=graph.getNode(Alist.get(i));
				test.setAttribute("ui.class", "Target");
			}
			graph.display();
		}while(!start.equals("q"));
		scn.close();
		
		
	}

}
