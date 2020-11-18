import java.util.List;
import java.util.ArrayList;

public class Parser {
    private static List<Symbol> tokens;
    private static int index;
    private static List<Integer> rules;
    private static String errorMessage;
    private static ParseTree tree;

    public Parser(List<Symbol> tokens) {
        this.tokens = tokens;
        this.index = 0;
        this.rules = new ArrayList<Integer>();
        this.errorMessage = null;
    }

    public static List<Integer> start() {
        tree = program();
        //tree = atom(); 
        //tree = assign();

        if(errorMessage != null) {
            System.out.println(errorMessage);
            return null;
        }
        else {
            return rules;
        }
    }

    public static ParseTree getTree() {
        return tree;
    }

    private static ParseTree program() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        match(new Symbol(LexicalUnit.BEGINPROG), children);
        match(new Symbol(LexicalUnit.PROGNAME), children);
        children.add(endLine());
        children.add(code());                        
        match(new Symbol(LexicalUnit.ENDPROG), children);
        addRule(1);
        Symbol program = new Symbol("Program");
        ParseTree programTree = new ParseTree(program, children);
        return programTree;
    }

    private static ParseTree code() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        Symbol token = nextToken();
        switch(token.getType()) {
            case VARNAME:
            case IF:
            case WHILE:
            case PRINT:
            case READ:
                Symbol epsilon = new Symbol("epsilon");
                ParseTree epsilonTree = new ParseTree(epsilon);
                children.add(epsilonTree);
                children.add(instruction());
                children.add(endLine());
                children.add(code());
                addRule(2);
                break;
            case ENDPROG:
            case ENDWHILE:
            case ELSE:
            case ENDIF:
                Symbol epsilonBis = new Symbol("epsilon");
                ParseTree epsilonBisTree = new ParseTree(epsilonBis);
                children.add(epsilonBisTree);
                addRule(3);
                break;
            default:
                //System.out.println("ERROR" + token.toString());
        }
        Symbol code = new Symbol("Code");
        ParseTree codeTree = new ParseTree(code, children);
        return codeTree;
    }

    private static ParseTree instruction() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        Symbol token = nextToken();
        switch(token.getType()) {
            case VARNAME:
               children.add(assign());
                addRule(4);
                break;
            case IF:
                children.add(If());
                addRule(5);
                break;
            case WHILE:  
                children.add(While());
                addRule(6);
                break;
            case PRINT:
                children.add(print());
                addRule(7);
                break; 
            case READ:
                children.add(read());
                addRule(8);
                break;
            default:
                //System.out.println("ERROR");
        }
        Symbol instruction = new Symbol("Instruction");
        ParseTree instructionTree = new ParseTree(instruction, children);
        return instructionTree;
    }

    private static ParseTree assign() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        match(new Symbol(LexicalUnit.VARNAME), children);
        match(new Symbol(LexicalUnit.ASSIGN), children);
        children.add(exprArith());
        addRule(9);
        Symbol assign = new Symbol("Assign");
        ParseTree assignTree = new ParseTree(assign, children);
        return assignTree;
    }

    private static ParseTree exprArith() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        children.add(prod());
        children.add(exprArithPrime());
        addRule(10);
        Symbol exprArith = new Symbol("ExprArith");
        ParseTree exprArithTree = new ParseTree(exprArith, children);
        return exprArithTree;
    }

    private static ParseTree exprArithPrime() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        Symbol token = nextToken();
        switch(token.getType()) {
            case PLUS:
                match(new Symbol(LexicalUnit.PLUS), children);
                children.add(prod());
                children.add(exprArithPrime());
                addRule(11);
                break;
            case MINUS:
                match(new Symbol(LexicalUnit.MINUS), children);
                children.add(prod());
                children.add(exprArithPrime());
                addRule(12);
                break;
            case ENDLINE:  
            case RPAREN: 
            case GT:
            case EQ:
                Symbol epsilon = new Symbol("epsilon");
                ParseTree epsilonTree = new ParseTree(epsilon);
                children.add(epsilonTree);
                addRule(13); 
                break;
        }
        Symbol exprArithPrime = new Symbol("ExprArith'");
        ParseTree exprArithPrimeTree = new ParseTree(exprArithPrime, children);
        return exprArithPrimeTree;
    }

    private static ParseTree prod() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        children.add(atom());
        children.add(prodPrime());
        addRule(14);
        Symbol prod = new Symbol("Prod");
        ParseTree prodTree = new ParseTree(prod, children);
        return prodTree;
    }

    private static ParseTree prodPrime() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        Symbol token = nextToken();
        switch(token.getType()) {
            case TIMES:
                match(new Symbol(LexicalUnit.TIMES), children);
                children.add(atom());
                children.add(prodPrime());
                addRule(15);
                break;
            case DIVIDE:
                match(new Symbol(LexicalUnit.DIVIDE), children);
                children.add(atom());
                children.add(prodPrime());
                addRule(16);
                break;
            case ENDLINE:
            case GT:
            case EQ:
            case PLUS:
            case MINUS:
            case RPAREN: 
                Symbol epsilon = new Symbol("epsilon");
                ParseTree epsilonTree = new ParseTree(epsilon);
                children.add(epsilonTree);
                addRule(17);
                break;
        }  
        Symbol prodPrime = new Symbol("Prod'");
        ParseTree prodPrimeTree = new ParseTree(prodPrime, children);
        return prodPrimeTree;      
    }

    private static ParseTree atom() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        Symbol token = nextToken();
        switch(token.getType()) {
            case MINUS:
                match(new Symbol(LexicalUnit.MINUS), children);
                children.add(atom());
                addRule(18);
                break;
            case LPAREN:
                match(new Symbol(LexicalUnit.LPAREN), children);
                children.add(exprArith());
                match(new Symbol(LexicalUnit.RPAREN), children);
                addRule(19);
                break;
            case VARNAME:
                match(new Symbol(LexicalUnit.VARNAME), children);
                addRule(20);
                break;
            case NUMBER:
                match(new Symbol(LexicalUnit.NUMBER), children);
                addRule(21);
                break;
            default:
                //System.out.println("ERROR ATOM");
        }
        Symbol atom = new Symbol("Atom");
        ParseTree tree = new ParseTree(atom, children);
        return tree;
    }

    private static ParseTree If() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        match(new Symbol(LexicalUnit.IF), children);
        match(new Symbol(LexicalUnit.LPAREN), children);
        children.add(cond());
        match(new Symbol(LexicalUnit.RPAREN), children);
        match(new Symbol(LexicalUnit.THEN), children);
        children.add(endLine());
        children.add(code());
        children.add(ifTail());
        addRule(22);
        Symbol ifSymbol = new Symbol("If");
        ParseTree ifTree = new ParseTree(ifSymbol, children);
        return ifTree;
    }

    private static ParseTree ifTail() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        Symbol token = nextToken();
        switch(token.getType()) {
            case ENDIF:
                match(new Symbol(LexicalUnit.ENDIF), children);
                addRule(23);
                break;
            case ELSE:
                match(new Symbol(LexicalUnit.ELSE), children);
                children.add(endLine());
                children.add(code());
                match(new Symbol(LexicalUnit.ENDIF), children);
                addRule(24);
                break;
            default:
               // System.out.println("ERROR");
        }
        Symbol ifTail = new Symbol("IfTail");
        ParseTree ifTailTree = new ParseTree(ifTail, children);
        return ifTailTree;
    }

    private static ParseTree cond() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        children.add(exprArith());
        children.add(comp());
        children.add(exprArith());
        addRule(25);
        Symbol cond = new Symbol("Cond");
        ParseTree condTree = new ParseTree(cond, children);
        return condTree;
    }

    private static ParseTree comp() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        Symbol token = nextToken();
        switch(token.getType()) {
            case GT:
                match(new Symbol(LexicalUnit.GT), children);
                addRule(26);
                break;
            case EQ:
                match(new Symbol(LexicalUnit.EQ), children);
                addRule(27);
                break;
            default:
               // System.out.println("ERROR");
        }
        Symbol comp = new Symbol("Comp");
        ParseTree compTree = new ParseTree(comp, children);
        return compTree;
    }

    private static ParseTree While() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        match(new Symbol(LexicalUnit.WHILE), children);
        match(new Symbol(LexicalUnit.LPAREN), children);
        children.add(cond());
        match(new Symbol(LexicalUnit.RPAREN), children);
        match(new Symbol(LexicalUnit.DO), children);
        children.add(endLine());
        children.add(code());
        match(new Symbol(LexicalUnit.ENDWHILE), children);
        addRule(28);
        Symbol whileSymbol = new Symbol("While");
        ParseTree whileTree = new ParseTree(whileSymbol, children);
        return whileTree;
    }

    private static ParseTree print() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        match(new Symbol(LexicalUnit.PRINT), children);
        match(new Symbol(LexicalUnit.LPAREN), children);   
        match(new Symbol(LexicalUnit.VARNAME), children);   
        match(new Symbol(LexicalUnit.RPAREN), children);   
        addRule(29);   
        Symbol print = new Symbol("Print");
        ParseTree printTree = new ParseTree(print, children);
        return printTree;
    }

    private static ParseTree read() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        match(new Symbol(LexicalUnit.READ), children);
        match(new Symbol(LexicalUnit.LPAREN), children);   
        match(new Symbol(LexicalUnit.VARNAME), children);   
        match(new Symbol(LexicalUnit.RPAREN), children);
        addRule(30);  
        Symbol read = new Symbol("Read");
        ParseTree readTree = new ParseTree(read, children);
        return readTree; 
    }

    private static ParseTree endLine() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        match(new Symbol(LexicalUnit.ENDLINE), children);
        children.add(endLinePrime());
        addRule(31);
        Symbol endLine = new Symbol("EndLine");
        ParseTree endLineTree = new ParseTree(endLine, children);
        return endLineTree;
    }

    private static ParseTree endLinePrime() {
        List<ParseTree> children = new ArrayList<ParseTree>();
        Symbol token = nextToken();
        switch(token.getType()) {
            case ENDLINE:
                match(new Symbol(LexicalUnit.ENDLINE), children);
                children.add(endLinePrime());
                addRule(32);
            break;
            case VARNAME:
            case IF:
            case WHILE:
            case PRINT:
            case READ:
            case ENDPROG:
            case ENDIF:
            case ELSE:
            case ENDWHILE:
                Symbol epsilon = new Symbol("epsilon");
                ParseTree epsilonTree = new ParseTree(epsilon);
                children.add(epsilonTree);
                addRule(33);
                break;
            default:
               // System.out.println("ERROR");
        }
        Symbol endLinePrime = new Symbol("EndLine'");
        ParseTree endLinePrimeTree = new ParseTree(endLinePrime, children);
        return endLinePrimeTree;
    }


    public static void match(Symbol symbol, List<ParseTree> children) {
        if(tokens.get(index).getType() == symbol.getType()) {
            //System.out.println("MATCH " + symbol.getType());
            children.add(new ParseTree(tokens.get(index)));
            index++;
        }
        else {
            if(errorMessage == null) {
                errorMessage = "Error at line " + tokens.get(index).getLine() + ", at position " + tokens.get(index).getColumn() + ", expected " + symbol.getType() + " but got " + tokens.get(index).getType();
            }    
        }
    }

    private static Symbol nextToken() {
        return tokens.get(index);
    }

    private static void addRule(int i) {
        rules.add(0,i);
    }
}