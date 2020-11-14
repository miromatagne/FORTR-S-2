import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/* 
    INFO-F-430 project, Part 2

    Students :
        -MATAGNE Miro-Manuel (459668)
        -TRAN NGOC Linh (459764)
    
    Design of a parser for a FORTR-S compiler
*/

public class Main{
  /**
   * Main function, creates an instance of the LexicalAnalyzer class and prints
   * out the tokens it returns.
   * @param argv
   */
    public static void main(String[] argv) { 
    //Syntax check
    boolean valid = true;
    if (argv.length == 0) {
      System.out.println("Invalid number of arguments");
      valid = false;
    }
    boolean verbose = false;
    String texFileName = null;
    String fileName = null;
    for(int i = 0; i < argv.length; i++) {
      if(argv[i].equals("-v")) {
        verbose = true;
      }
      else if(argv[i].equals("-wb")) {
        if(i != argv.length -1 && argv[i+1].charAt(0) != '-') {
          texFileName = argv[i+1];
          i++;
        }
        else {
          System.out.println("-wb should be followed by a file name.");
          valid = false;
        }
      }
      else {
        fileName = argv[i];
      }
    }
    if(valid && fileName != null) {
        List<Symbol> tokens = new ArrayList<Symbol>();
        tokens = getTokens(fileName);
        Parser parser = new Parser(tokens);
        List<Integer> rules = parser.start();
        System.out.println(rules);
    }
    }
  
    
  private static List<Symbol> getTokens(String fileName) {
      LexicalAnalyzer scanner = null;
      List<Symbol> tokens = new ArrayList<Symbol>();
      try {
        java.io.FileInputStream stream = new java.io.FileInputStream(fileName);
        java.io.Reader reader = new java.io.InputStreamReader(stream, "UTF-8");
        scanner = new LexicalAnalyzer(reader);
        
        //Initialization of the symbol table, stored in a Map
        Map<String,Integer> symbolTable = new TreeMap<String,Integer>();
        
        //Read all the symbols returned by the lexical analyzer unitl the EOS Symbol,
        //indicating the end of the file
        Symbol receivedSymbol = scanner.nextToken();
        while (receivedSymbol.getType() != LexicalUnit.EOS ) {
          //Check if the returned symbol is a variable name
          if(receivedSymbol.getType() == LexicalUnit.VARNAME) {
            //If the variable is not in the symbol table yet, it is added
            if(!symbolTable.containsKey(receivedSymbol.getValue().toString())) {
              symbolTable.put(receivedSymbol.getValue().toString(),receivedSymbol.getLine());
            }
          }
          //System.out.println(receivedSymbol.toString());
          tokens.add(receivedSymbol);
          receivedSymbol = scanner.nextToken();
        }

        //Displaying of the symbol table
        //printSymbolTable(symbolTable);

      } catch (java.io.FileNotFoundException e) {
        System.out.println("File not found : \"" + fileName + "\"");
      } catch (java.io.IOException e) {
        System.out.println("IO error scanning file \"" + fileName + "\"");
        System.out.println(e);
      } catch (Exception e) {
        System.out.println("Unexpected exception:");
        e.printStackTrace();
      }
      return tokens;
  }

  /**
   * Iterates through the symbol table and prints every variable in alphabetical 
   * order with the line of first occurence.
   * @param symbolTable
   */
  private static void printSymbolTable(Map<String, Integer> symbolTable) {
      Iterator<Entry<String, Integer>> it = symbolTable.entrySet().iterator();
        if(it.hasNext()){
          System.out.println("\nVariables");
        }
        while (it.hasNext()) {
          Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>)it.next();
          System.out.println(pair.getKey() + "\t" + pair.getValue());
          it.remove();
        }
    }
}