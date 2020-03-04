import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.text.DecimalFormat;
import java.util.*;

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
  String memid;
  String name;
  int uses;
  
  // Use memid (hex) for these lists
  ArrayList<String> calls;

  
  func() {
    calls = new ArrayList<>();

  }
  
}

public class Pi {
  //callpairs holds pairs nodes and the number of times they are called together
  static HashMap<String,Double>callpairs = new HashMap<>();
  //callpairs2 holds individual nodes and the total number of times they get called
  static HashMap<String,Double>callpairs2 = new HashMap<>();
  //scopes holds the scopes and the nodes they contain
  static HashMap<String,String> scopes = new HashMap<>();

  public static void main(String args[]) {
    try {
      parseCallGraph();
    } catch (Exception e){
      e.printStackTrace();
    }
    
  }
  
  public static void parseCallGraph () throws Exception {
    
    // to get the working directory
    String filename = "/src/callgraph.txt";
    String workingdirectory = System.getProperty("user.dir");
    String absoulouteFilePath = workingdirectory + filename;
    String tempScope="";

    // Create a file object to open the file
    File file = new File(absoulouteFilePath);
    
    // Buffer Reader to read the file
    BufferedReader bf = new BufferedReader(new FileReader(file));
    ArrayList<String> nodes = new ArrayList<String>();

    String st;
    
    while ((st = bf.readLine()) != null) {

      // TODO Read through callgraph and parse functions into func class
      
      // Create an object of func
      func f = new func();
      
      // check if the call is for null function
      boolean isFound = st.contains("null function");
      
      // if this is true the move to the next Call graph a loop so for consective calls
      while (isFound) {
        
        while ((st = bf.readLine()) != null) {
          if (st.contains("Call graph")) {
            break;
          }
          
          //System.out.println("in " + st);
        }
        isFound = st.contains("null function");
        
      }
      
      // Save the name of this function in func object

      //System.out.println(st);
      int index_name = st.indexOf('\'');
      int lastindex_name = st.lastIndexOf('\'');
      int index_uses = st.indexOf('=');


      // ignore empty lines also ignores external nodes
      if (index_name != -1) {
        
        
        // this needs to  be done only for the Call graph line
        if (index_uses != -1) {

          f.name = st.substring(index_name + 1, lastindex_name);
          //stores the name of the sope of this callgraph in tempScope
          tempScope = f.name;
          //System.out.println("Name: "+f.name);
          f.uses = Integer.valueOf(st.substring(index_uses + 1, st.length()));
          //System.out.println("#Uses: "+f.uses);
          int index_mem = st.indexOf("<");
          int lastindex_mem = st.indexOf(">");
          f.memid = st.substring(index_mem + 2,lastindex_mem);
          //System.out.println("memID: "+f.memid);
          //initializes the nodes string in scopes for this call graph to null
          scopes.put(f.name,"");
          /*
          for(int i=0;i<nodes.size();i++){
            System.out.println("nodes["+i+"]: "+nodes.get(i));
          }

           */
          Set<String> nodeSet = new LinkedHashSet<>();
          nodeSet.addAll(nodes);
          nodes.clear();
          nodes.addAll(nodeSet);
          //for(int i=0;i<nodes.size();i++){
           // System.out.println("nodes["+i+"]: "+nodes.get(i));
          //}
          //add individual functions to callpairs2
          if(nodes.size()>0){
            for(int i=0;i<nodes.size();i++) {
              if (callpairs2.containsKey(nodes.get(i))) {
                callpairs2.put(nodes.get(i), callpairs2.get(nodes.get(i)) + 1);
              } else {
                callpairs2.put(nodes.get(i), 1.0);
              }
            }
          }
          //if nodes contains more than one function
          //count function pairs called together and add to callpairs
          if(nodes.size() > 1){
            for(int i=0;i<nodes.size();i++) {
              for(int j=i+1; j<nodes.size();j++) {
                if(!nodes.get(i).equals(nodes.get(j))) {
                  if (callpairs.containsKey(nodes.get(i) +", "+ nodes.get(j))) {
                    callpairs.put(nodes.get(i) +", "+ nodes.get(j), callpairs.get(nodes.get(i) + ", "+ nodes.get(j)) + 1);
                  } else if(callpairs.containsKey(nodes.get(j) +", "+ nodes.get(i))) {
                    callpairs.put(nodes.get(j) +", "+ nodes.get(i), callpairs.get(nodes.get(j) +", "+ nodes.get(i)) + 1);
                  }else {
                    callpairs.put(nodes.get(i) + ", "+ nodes.get(j), 1.0);
                  }
                }
              }
            }
          }
          nodes.clear();
          
        } else {
          f.name = st.substring(index_name + 1, lastindex_name);
          if(!scopes.get(tempScope).contains(f.name)) {
            scopes.put(tempScope, scopes.get(tempScope).concat("," + f.name));
          }
          //System.out.println("Name: "+f.name);

          nodes.add(f.name);
          //                        //Save the file names
          //                        f.name = st.substring(index_name + 1, lastindex_name);
          //                        System.out.println(f.name);
          //                        int index_mem = st.indexOf("<");
          //                        int lastindex_mem = st.indexOf(">");
          //                        System.out.println(f.memid);
        }
      }
      // external nodes
    }




    Set<String> nodeSet = new LinkedHashSet<>();
    nodeSet.addAll(nodes);
    nodes.clear();
    nodes.addAll(nodeSet);
    //for(int i=0;i<nodes.size();i++){
     // System.out.println("nodes["+i+"]: "+nodes.get(i));
    //}
    if(nodes.size()>0){
      for(int i=0;i<nodes.size();i++) {
        if (callpairs2.containsKey(nodes.get(i))) {
          callpairs2.put(nodes.get(i), callpairs2.get(nodes.get(i)) + 1);
        } else {
          callpairs2.put(nodes.get(i), 1.0);
        }
      }
    }
    //if nodes contains more than one function
    if(nodes.size() > 1){
      for(int i=0;i<nodes.size();i++) {
        for(int j=i+1; j<nodes.size();j++) {
          if(!nodes.get(i).equals(nodes.get(j))) {
            if (callpairs.containsKey(nodes.get(i) +", "+ nodes.get(j))) {
              callpairs.put(nodes.get(i) +", "+ nodes.get(j), callpairs.get(nodes.get(i) +", "+ nodes.get(j)) + 1);
            } else if(callpairs.containsKey(nodes.get(j) +", "+ nodes.get(i))) {
              callpairs.put(nodes.get(j) +", "+ nodes.get(i), callpairs.get(nodes.get(j) +", "+ nodes.get(i)) + 1);
            }else {
              callpairs.put(nodes.get(i) +", "+ nodes.get(j), 1.0);
            }
          }
        }
      }
    }
    nodes.clear();

    //Print callpairs hashmap

    //callpairs.entrySet().forEach(entry->{
    //  System.out.println(entry.getKey()+ " " +entry.getValue());
    //});
    //callpairs2.entrySet().forEach(entry->{
     // System.out.println(entry.getKey()+ " " +entry.getValue());
    //});

    //scopes.entrySet().forEach(entry->{
     // System.out.println("Scope: "+entry.getKey()+ "   Nodes: " +entry.getValue());
    //});





    //calculate confidence
    Iterator cp2 = callpairs2.entrySet().iterator();
    while(cp2.hasNext()){
      Iterator cp =callpairs.entrySet().iterator();
      Map.Entry mapElement2 = (Map.Entry)cp2.next();
      while(cp.hasNext()){
          Map.Entry mapElement = (Map.Entry)cp.next();
          String pair = (String) mapElement.getKey();
          String firstNode, secondNode;
          firstNode = pair.substring(0,pair.indexOf(','));
          secondNode = pair.substring(pair.indexOf(',')+2);
          String name2 = (String) mapElement2.getKey();
          if(firstNode.equals(name2)||secondNode.equals(name2)){
            Double support = (Double)mapElement.getValue();
            Double support2 = (Double)mapElement2.getValue();
            Double confidence = support/support2;
            if(confidence >= .65  && support>=3){
              String thisScope = "";
              String func1, func2;
              func1 = pair.substring(0,pair.indexOf(','));
              func2 = pair.substring(pair.indexOf(',')+2);
              if(!name2.equals(func1)){
                func2=func1;
                func1=name2;
              }
              //System.out.println("name: "+name2);
              //System.out.println("func1: "+func1);
              //System.out.println("func2: "+func2);
              for(Map.Entry<String,String> entry: scopes.entrySet()){
                String k = entry.getKey();
                String v = entry.getValue();
                if(v.contains(func1)&& !v.contains(func2)){
                  thisScope =k;
                  break;
                }
              }
              if(!thisScope.equals("")&&confidence<1) {
                System.out.println("bug: " + name2 + " in " + thisScope + ", pair: (" + pair + "), support: " + support.intValue() + ", confidence: " + String.format("%.2f", (confidence * 100)) + "%");
              }
              }
          }

      }
    }

  }
  
  public static void analyzeData() {
    // iterate through
  }
}
