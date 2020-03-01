import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Scanner;

/*
 *  Project 2 - CS408 
 *  Spring 2020
 *
 */

//TODO 
// Create method to convert callgraph input into set of data
/* Data format : Func. <int memid, str name, int uses, map<list of calls>, map<list of pairs>> */
// Create method to analyze the parsed graph, iterate through function list and generate pairs
/* (IE for func A(), B() if A() and B() are together 5 times, and B() is called 7 times, then generate
   a value 5/7 for confidence of there being a logic bug. More details in doc page 2,3*/
class func {
	int memid;
	String name;
	int uses;

	// Use memid (hex) for these lists
	ArrayList<Integer> calls;
	ArrayList<Pair<Integer, Integer>> callPairs;
}

public class Pi {
	ArrayList<func> funcList = new ArrayList<func>();

	public static void main(String args[]) {
		parseCallGraph();

	}

	public static void parseCallGraph() {
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {
			String s = in.nextLine();
			// TODO Read through callgraph and parse functions into func class
			// Ignore null calls, and external nodes
		}
	}

	public static void analyzeData() {
		// iterate through
	}
}
