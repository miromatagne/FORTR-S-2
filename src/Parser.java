import java.util.List;
import java.util.ArrayList;

/**
 * Rules done :
 * 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33
 */

public class Parser {
    private static List<Symbol> tokens;
    private static int index;
    private static List<Integer> rules;
    private static String errorMessage;

    public Parser(List<Symbol> tokens) {
        this.tokens = tokens;
        this.index = 0;
        this.rules = new ArrayList<Integer>();
        this.errorMessage = null;
    }

    public static List<Integer> start() {
        program();
        if(errorMessage != null) {
            System.out.println(errorMessage);
            return null;
        }
        else {
            return rules;
        }
    }

    private static void program() {
        match(new Symbol(LexicalUnit.BEGINPROG));
        match(new Symbol(LexicalUnit.PROGNAME));
        endLine();
        code();
        match(new Symbol(LexicalUnit.ENDPROG));
        addRule(1);
    }

    private static void code() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case VARNAME:
            case IF:
            case WHILE:
            case PRINT:
            case READ:
                instruction();
                endLine();
                code();
                addRule(2);
                break;
            case ENDPROG:
            case ENDWHILE:
            case ELSE:
            case ENDIF:
                addRule(3);
                break;
            default:
                //System.out.println("ERROR" + token.toString());
        }
    }

    private static void instruction() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case VARNAME:
                assign();
                addRule(4);
                break;
            case IF:
                If();
                addRule(5);
                break;
            case WHILE:  
                While();
                addRule(6);
                break;
            case PRINT:
                print();
                addRule(7);
                break; 
            case READ:
                read();
                addRule(8);
                break;
            default:
                //System.out.println("ERROR");
        }
    }

    private static void assign() {
        match(new Symbol(LexicalUnit.VARNAME));
        match(new Symbol(LexicalUnit.ASSIGN));
        exprArith();
        addRule(9);
    }

    private static void exprArith() {
        prod();
        exprArithPrime();
        addRule(10);
    }

    private static void exprArithPrime() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case PLUS:
                match(new Symbol(LexicalUnit.PLUS));
                prod();
                exprArithPrime();
                addRule(11);
                break;
            case MINUS:
                match(new Symbol(LexicalUnit.MINUS));
                prod();
                exprArithPrime();
                addRule(12);
                break;
            case ENDLINE:  
            case RPAREN: 
            case GT:
            case EQ:
                addRule(13); 
                break;
        }
    }

    private static void prod() {
        atom();
        prodPrime();
        addRule(14);
    }

    private static void prodPrime() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case TIMES:
                match(new Symbol(LexicalUnit.TIMES));
                atom();
                prodPrime();
                addRule(15);
                break;
            case DIVIDE:
                match(new Symbol(LexicalUnit.DIVIDE));
                atom();
                prodPrime();
                addRule(16);
                break;
            case ENDLINE:
            case GT:
            case EQ:
            case PLUS:
            case MINUS:
            case RPAREN: 
                addRule(17);
                break;
        }        
    }

    private static void atom() {
        Symbol token = nextToken();
        // System.out.println(token.getType());
        switch(token.getType()) {
            case MINUS:
                match(new Symbol(LexicalUnit.MINUS));
                atom();
                addRule(18);
                break;
            case LPAREN:
                match(new Symbol(LexicalUnit.LPAREN));
                exprArith();
                match(new Symbol(LexicalUnit.RPAREN));
                addRule(19);
                break;
            case VARNAME:
                match(new Symbol(LexicalUnit.VARNAME));
                addRule(20);
                break;
            case NUMBER:
                match(new Symbol(LexicalUnit.NUMBER));
                addRule(21);
                break;
            default:
                //System.out.println("ERROR ATOM");
        }
    }

    private static void If() {
        match(new Symbol(LexicalUnit.IF));
        match(new Symbol(LexicalUnit.LPAREN));
        cond();
        match(new Symbol(LexicalUnit.RPAREN));
        match(new Symbol(LexicalUnit.THEN));
        endLine();
        code();
        ifTail();
        addRule(22);
    }

    private static void ifTail() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case ENDIF:
                match(new Symbol(LexicalUnit.ENDIF));
                addRule(23);
                break;
            case ELSE:
                match(new Symbol(LexicalUnit.ELSE));
                endLine();
                code();
                match(new Symbol(LexicalUnit.ENDIF));
                addRule(24);
                break;
            default:
               // System.out.println("ERROR");
        }
    }

    private static void cond() {
        exprArith();
        comp();
        exprArith();
        addRule(25);
    }

    private static void comp() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case GT:
                match(new Symbol(LexicalUnit.GT));
                addRule(26);
                break;
            case EQ:
                match(new Symbol(LexicalUnit.EQ));
                addRule(27);
                break;
            default:
               // System.out.println("ERROR");
        }
    }

    private static void While() {
        match(new Symbol(LexicalUnit.WHILE));
        match(new Symbol(LexicalUnit.LPAREN));
        cond();
        match(new Symbol(LexicalUnit.RPAREN));
        match(new Symbol(LexicalUnit.DO));
        endLine();
        code();
        match(new Symbol(LexicalUnit.ENDWHILE));
        addRule(28);
    }

    private static void print() {
        match(new Symbol(LexicalUnit.PRINT));
        match(new Symbol(LexicalUnit.LPAREN));   
        match(new Symbol(LexicalUnit.VARNAME));   
        match(new Symbol(LexicalUnit.RPAREN));   
        addRule(29);   
    }

    private static void read() {
        match(new Symbol(LexicalUnit.READ));
        match(new Symbol(LexicalUnit.LPAREN));   
        match(new Symbol(LexicalUnit.VARNAME));   
        match(new Symbol(LexicalUnit.RPAREN));
        addRule(30);   
    }

    private static void endLine() {
        match(new Symbol(LexicalUnit.ENDLINE));
        endLinePrime();
        addRule(31);
    }

    private static void endLinePrime() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case ENDLINE:
                match(new Symbol(LexicalUnit.ENDLINE));
                endLinePrime();
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
                addRule(33);
                break;
            default:
               // System.out.println("ERROR");
        }
    }


    public static void match(Symbol symbol) {
        if(tokens.get(index).getType() == symbol.getType()) {
            //System.out.println("MATCH " + symbol.getType());
            index++;
        }
        else {
            if(errorMessage == null) {
                errorMessage = "Error at line " + tokens.get(index).getLine() + ", at position " + tokens.get(index).getLine() + ", expected " + symbol.getType() + " but got " + tokens.get(index).getType();
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