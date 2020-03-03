import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
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
  String memid;
  String name;
  int uses;
  
  // Use memid (hex) for these lists
  ArrayList<String> calls;
  HashMap<String,Integer> callpairs;
  
  func() {
    calls = new ArrayList<>();
    callpairs = new HashMap<>();
  }
  
}

public class Pi {
  HashMap<String,func> funclist = new HashMap<>();
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
    
    // Create a file object to open the file
    File file = new File(absoulouteFilePath);
    
    // Buffer Reader to read the file
    BufferedReader bf = new BufferedReader(new FileReader(file));
    
    
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
      
      System.out.println(st);
      int index_name = st.indexOf('\'');
      int lastindex_name = st.lastIndexOf('\'');
      int index_uses = st.indexOf('=');
      
      
      // ignore empty lines also ignores external nodes
      if (index_name != -1) {
        
        
        // this needs to  be done only for the Call graph line
        if (index_uses != -1) {
          f.name = st.substring(index_name + 1, lastindex_name);
          System.out.println(f.name);
          f.uses = Integer.valueOf(st.substring(index_uses + 1, st.length()));
          System.out.println(f.uses);
          int index_mem = st.indexOf("<");
          int lastindex_mem = st.indexOf(">");
          f.memid = st.substring(index_mem + 2,lastindex_mem);
          System.out.println(f.memid);
          
        } else {
          
          
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
  }
  
  public static void analyzeData() {
    // iterate through
  }
}
