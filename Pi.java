import javafx.util.Pair;
import java.util.ArrayList;
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
	public static void main(String args[]) {
		parseCallGraph();
	}

	public static void parseCallGraph() {

	}

	public static void analyzeData() {
		// todo, see above
	}
}
