import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/*
 *  Project 2 - CS408
 *  Spring 2020
 *  Part i.D
 */

class func {
    String memid;
    String name;
    int uses;

    // Use memid (hex) for these lists
    ArrayList<String> calls;

    func() {
        calls = new ArrayList<>();
    }
}

public class Pi_partD {
    // callpairs holds pairs nodes and the number of times they are called together
    static HashMap<String, Double> callpairs = new HashMap<>();
    // callpairs2 holds individual nodes and the total number of times they get
    // called
    static HashMap<String, Double> callpairs2 = new HashMap<>();
    // scopes holds the scopes and the nodes they contain
    static HashMap<String, String> scopes = new HashMap<>();
    // Used to check if a function calls an external node via the null function
    static ArrayList<String> ext_node_check = new ArrayList<>();

    public static void main(String[] args) {
        int inputSupport = 3;
        Double inputConfidence = .65;
        if (args.length == 1 || args.length > 3) {
            System.out.println("invalid arguments");
        } else if (args.length == 2) {
            inputSupport = Integer.parseInt(args[0]);
            inputConfidence = Double.parseDouble(args[1]) / 100;
        } else if (args.length == 0) {
            inputSupport = 3;
            inputConfidence = .65;
        }
        try {
            parseCallGraph(inputSupport, inputConfidence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseCallGraph(int inSup, Double inConf) throws Exception {
        // to get the working directory
        String filename = "/call_graph.txt";
        String workingdirectory = System.getProperty("user.dir");
        String absoulouteFilePath = workingdirectory + filename;
        String tempScope = "";

        // Create a file object to open the file
        File file = new File(absoulouteFilePath);

        // Buffer Reader to read the file
        BufferedReader bf = new BufferedReader(new FileReader(file));
        ArrayList<String> nodes = new ArrayList<String>();

        String st;
        String st_p = null; // Previous line, set to null at start
        while ((st = bf.readLine()) != null) {
            // Create an object of func
            func f = new func();

            // check if the call is for null function
            boolean isFound = st.contains("null function");

            // if this is true the move to the next Call graph a loop so for consective
            // calls
            while (isFound) {

                while ((st = bf.readLine()) != null) {
                    if (st.contains("Call graph")) {
                        break;
                    }
                }
                isFound = st.contains("null function");

            }

            // Save the name of this function in func object
            int index_name = st.indexOf('\'');
            int lastindex_name = st.lastIndexOf('\'');
            int index_uses = st.indexOf('=');
            int index_ext_node = st.indexOf("CS<0x0> calls external node");

            // ignore empty lines
            if (index_name != -1) {

                // this needs to be done only for the Call graph line
                if (index_uses != -1) {

                    f.name = st.substring(index_name + 1, lastindex_name);
                    // stores the name of the sope of this callgraph in tempScope
                    tempScope = f.name;

                    f.uses = Integer.valueOf(st.substring(index_uses + 1, st.length()));

                    int index_mem = st.indexOf("<");
                    int lastindex_mem = st.indexOf(">");
                    f.memid = st.substring(index_mem + 2, lastindex_mem);

                    // initializes the nodes string in scopes for this call graph to null
                    scopes.put(f.name, "");

                    Set<String> nodeSet = new LinkedHashSet<>();
                    nodeSet.addAll(nodes);
                    nodes.clear();
                    nodes.addAll(nodeSet);

                    // add individual functions to callpairs2
                    if (nodes.size() > 0) {
                        for (int i = 0; i < nodes.size(); i++) {
                            if (callpairs2.containsKey(nodes.get(i))) {
                                callpairs2.put(nodes.get(i), callpairs2.get(nodes.get(i)) + 1);
                            } else {
                                callpairs2.put(nodes.get(i), 1.0);
                            }
                        }
                    }
                    // if nodes contains more than one function
                    // count function pairs called together and add to callpairs
                    if (nodes.size() > 1) {
                        for (int i = 0; i < nodes.size(); i++) {
                            for (int j = i + 1; j < nodes.size(); j++) {
                                if (!nodes.get(i).equals(nodes.get(j))) {
                                    if (callpairs.containsKey(nodes.get(i) + ", " + nodes.get(j))) {
                                        callpairs.put(nodes.get(i) + ", " + nodes.get(j),
                                                callpairs.get(nodes.get(i) + ", " + nodes.get(j)) + 1);
                                    } else if (callpairs.containsKey(nodes.get(j) + ", " + nodes.get(i))) {
                                        callpairs.put(nodes.get(j) + ", " + nodes.get(i),
                                                callpairs.get(nodes.get(j) + ", " + nodes.get(i)) + 1);
                                    } else {
                                        callpairs.put(nodes.get(i) + ", " + nodes.get(j), 1.0);
                                    }
                                }
                            }
                        }
                    }
                    nodes.clear();
                } else {
                    f.name = st.substring(index_name + 1, lastindex_name);
                    if (!scopes.get(tempScope).contains(f.name + ",")) {
                        scopes.put(tempScope, scopes.get(tempScope).concat("," + f.name + ","));
                    }

                    nodes.add(f.name);

                }
            } else { // part D
                // store functions that call external nodes in a list
                if ((index_ext_node != -1) && (st_p != null)) {
                    String nodename_temp = st_p.substring(st_p.indexOf("\'") + 1, st_p.lastIndexOf("\'"));
                    ext_node_check.add(nodename_temp);
                }
            }
            st_p = st;
        }

        Set<String> nodeSet = new LinkedHashSet<>();
        nodeSet.addAll(nodes);
        nodes.clear();
        nodes.addAll(nodeSet);

        if (nodes.size() > 0) {
            for (int i = 0; i < nodes.size(); i++) {
                if (callpairs2.containsKey(nodes.get(i))) {
                    callpairs2.put(nodes.get(i), callpairs2.get(nodes.get(i)) + 1);
                } else {
                    callpairs2.put(nodes.get(i), 1.0);
                }
            }
        }
        // if nodes contains more than one function
        if (nodes.size() > 1) {
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = i + 1; j < nodes.size(); j++) {
                    if (!nodes.get(i).equals(nodes.get(j))) {
                        if (callpairs.containsKey(nodes.get(i) + ", " + nodes.get(j))) {
                            callpairs.put(nodes.get(i) + ", " + nodes.get(j),
                                    callpairs.get(nodes.get(i) + ", " + nodes.get(j)) + 1);
                        } else if (callpairs.containsKey(nodes.get(j) + ", " + nodes.get(i))) {
                            callpairs.put(nodes.get(j) + ", " + nodes.get(i),
                                    callpairs.get(nodes.get(j) + ", " + nodes.get(i)) + 1);
                        } else {
                            callpairs.put(nodes.get(i) + ", " + nodes.get(j), 1.0);
                        }
                    }
                }
            }
        }
        nodes.clear();

        // calculate confidence
        Iterator cp2 = callpairs2.entrySet().iterator();
        while (cp2.hasNext()) {
            Iterator cp = callpairs.entrySet().iterator();
            Map.Entry mapElement2 = (Map.Entry) cp2.next();
            while (cp.hasNext()) {
                Map.Entry mapElement = (Map.Entry) cp.next();
                String pair = (String) mapElement.getKey();
                String firstNode, secondNode;
                firstNode = pair.substring(0, pair.indexOf(','));
                secondNode = pair.substring(pair.indexOf(',') + 2);
                String name2 = (String) mapElement2.getKey();
                if (firstNode.equals(name2) || secondNode.equals(name2)) {
                    Double support = (Double) mapElement.getValue();
                    Double support2 = (Double) mapElement2.getValue();
                    Double confidence = support / support2;
                    if (confidence >= inConf && support >= inSup) {
                        String thisScope = "";
                        String func1, func2;
                        func1 = pair.substring(0, pair.indexOf(','));
                        func2 = pair.substring(pair.indexOf(',') + 2);
                        if (!name2.equals(func1)) {
                            func2 = func1;
                            func1 = name2;
                        }
                        // debugging option for part D
                        boolean debugging = false;

                        for (Map.Entry<String, String> entry : scopes.entrySet()) {
                            String k = entry.getKey();
                            String v = entry.getValue();
                            if (v.contains("," + func1 + ",") && !v.contains("," + func2 + ",")) {
                                String bug = "";
                                thisScope = k;
                                if (!thisScope.equals("") && confidence < 1) {
                                    if (func1.compareTo(func2) > 0) {
                                        bug = ("bug: " + name2 + " in " + thisScope + ", pair: (" + func2 + ", " + func1
                                                + "), support: " + support.intValue() + ", confidence: "
                                                + String.format("%.2f", (confidence * 100)) + "%");
                                    } else {
                                        bug = ("bug: " + name2 + " in " + thisScope + ", pair: (" + func1 + ", " + func2
                                                + "), support: " + support.intValue() + ", confidence: "
                                                + String.format("%.2f", (confidence * 100)) + "%");
                                    }
                                }
                                // case for false positive - part D
                                if ((ext_node_check.indexOf(firstNode) != -1)
                                        || (ext_node_check.indexOf(secondNode) != -1)) {
                                    if (debugging) {
                                        System.out.println("FALSE POS: " + bug);
                                    }
                                } else {
                                    System.out.println(bug);
                                }
                                bug = "";
                                thisScope = "";
                            }
                        }
                    }
                }
            }
        }
    }
}
