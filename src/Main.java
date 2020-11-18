import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;  

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
      else if(argv[i].equals("-wt")) {
        if(i != argv.length -1 && argv[i+1].charAt(0) != '-') {
          texFileName = argv[i+1];
          i++;
        }
        else {
          System.out.println("-wt should be followed by a file name.");
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
        if(rules != null) {
          if(verbose) {
            printVerboseRules(rules);
          }
          else {
            printRules(rules); 
          }
          if(texFileName != null) {
            writeToTex(parser,texFileName);
          }
        }
        //System.out.print(parseTree.toLaTeX());

        // for(int i = 0; i < parseTree.getChildren().size(); i++) {
        //   System.out.print(parseTree.getChildren().get(i) + " ");
        // }
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

    private static void printRules(List<Integer> rules) {
      for(int i = 0; i < rules.size(); i++) {
        System.out.print(rules.get(i) + " ");
      }
    }

    private static void printVerboseRules(List<Integer> rules) {
      for(int i = 0; i < rules.size(); i++) {
          System.out.println(getVerbose(rules.get(i)));
        }
      }

    private static void writeToTex(Parser parser, String texFileName) {
      try {
        FileWriter myWriter = new FileWriter(texFileName);
        myWriter.write(parser.getTree().toLaTeX());
        myWriter.close();
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

    private static String getVerbose(int i) {
      switch (i) {
        case 1:
          return "<Program> -> BEGINPROG [ProgName] <EndLine> <Code> ENDPROG";          
        case 2:
          return "<Code> -> <Instruction> [EndLine] <Code>";          
        case 3:
          return "<Code> -> epsilon";          
        case 4:
          return "<Instruction> -> <Assign>";          
        case 5:
          return "<Instruction> -> <If>";          
        case 6:
          return "<Instruction> -> <While>";          
        case 7:
          return "<Instruction> -> <Print>";          
        case 8:
          return "<Instruction> -> <Read>";          
        case 9:
          return "<Assign> -> [VarName] := <ExprArith>";          
        case 10:
          return "<ExprArith> -> <Prod><ExprArith'>";          
        case 11:
          return "<ExprArith'> -> +<Prod><ExprArith'>";          
        case 12:
          return "<ExprArith'> -> -<Prod><ExprArith'>";          
        case 13:
          return "<ExprArith'> -> epsilon";          
        case 14:
          return "<Prod> -> <Atom><Prod'>";          
        case 15:
          return "<Prod'> -> *<Atom><Prod'>";          
        case 16:
          return "<Prod'> -> /<Atom><Prod'>";          
        case 17:
          return "<Prod'> -> epsilon";          
        case 18:
          return "<Atom> -> -<Atom>";          
        case 19:
          return "<Atom> -> (<ExprArith>)";          
        case 20:
          return "<Atom> -> [VarName]";          
        case 21:
          return "<Atom> -> [Number]";          
        case 22:
          return "<If> -> IF (<Cond>) THEN <EndLine> <Code> <IfTail>";          
        case 23:
          return "<IfTail> -> ENDIF";          
        case 24:
          return "<IfTail> -> ELSE <EndLine> <Code> ENDIF";          
        case 25:
          return "<Cond> -> <ExprArith> <Comp> <ExprArith>";          
        case 26:
          return "<Comp> -> =";          
        case 27:
          return "<Comp> -> >";          
        case 28:
          return "<While> -> WHILE (<Cond>) DO <EndLine> <Code> ENDWHILE";          
        case 29:
          return "<Print> -> PRINT([VarName])";          
        case 30:
          return "<Read> -> READ([VarName])";          
        case 31:
          return "<EndLine> -> [EndLine] <EndLine'>";          
        case 32:
          return "<EndLine'> -> [EndLine] <EndLine'>";          
        case 33:
          return "<EndLine'> -> epsilon";          
        default:
          return null;
      }
    }
}