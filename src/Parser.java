import java.util.List;

import jdk.javadoc.internal.doclets.formats.html.resources.standard;

public class Parser {
    private static List<Symbol> tokens;
    private static int index;

    public Parser(List<Symbol> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    public static void start() {
        //atom();
        read();
    }

    private static void atom() {
        Symbol token = nextToken();
        // System.out.println(token.getType());
        switch(token.getType()) {
            case VARNAME:
                match(token);
                break;
            case NUMBER:
                match(token);
                break;
            // case LPAREN:
            //     match(new Symbol(LexicalUnit.LPAREN));
            //     exprArith();
            //     match(new Symbol(LexicalUnit.RPAREN));
            //     break;
            case MINUS:
                match(new Symbol(LexicalUnit.MINUS));
                atom();
                break;
            default:
                System.out.println("ERROR");
        }
    }

    private static void comp() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case GT:
                match(new Symbol(LexicalUnit.GT));
                break;
            case EQ:
                match(new Symbol(LexicalUnit.EQ));
                break;
            default:
               // System.out.println("ERROR");
        }
    }

    private static void print() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case PRINT:
                match(new Symbol(LexicalUnit.PRINT));
                break;
            case LPAREN:
                match(new Symbol(LexicalUnit.LPAREN));   
                break;    
            case VARNAME:
                match(new Symbol(LexicalUnit.VARNAME));   
                break;
            case RPAREN:
                match(new Symbol(LexicalUnit.RPAREN));   
                break;  
            default:
                System.out.println("ERROR");  
        }    
    }

    private static void read() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case READ:
                match(new Symbol(LexicalUnit.PRINT));
                break;
            case LPAREN:
                match(new Symbol(LexicalUnit.LPAREN));   
                break;    
            case VARNAME:
                match(new Symbol(LexicalUnit.VARNAME));   
                break;
            case RPAREN:
                match(new Symbol(LexicalUnit.RPAREN));   
                break; 
            default:
                System.out.println("ERROR");   
        }    
    }

    private static void exprArith() {
        prod();
        exprArithPrime();
    }

    private static void exprArithPrime() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case PLUS:
                match(new Symbol(LexicalUnit.PLUS));
                prod();
                exprArithPrime();
                break;
            case MINUS:
                match(new Symbol(LexicalUnit.MINUS));
                prod();
                exprArithPrime();
                break;
            case ENDLINE:  
            case RPAREN: 
            case GT:
            case EQ: 
                return;
        }
    }

    private static void prod() {
        atom();
        prodPrime();  
    }

    private static void prodPrime() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case TIMES:
                match(new Symbol(LexicalUnit.TIMES));
                atom();
                prodPrime();
                break;
            case DIVIDE:
                 match(new Symbol(LexicalUnit.DIVIDE));
                atom();
                prodPrime();
                break;
            case ENDLINE:
            case GT:
            case EQ:
            case PLUS:
            case MINUS:
            case RPAREN: 
                return;
        }        
    }

    private static void cond() {
        exprArith();
        comp();
        exprArith();
    }



    public static void match(Symbol symbol) {
        if(tokens.get(index).getType() == symbol.getType()) {
            System.out.println("MATCH " + symbol.getType());
            index++;
        }
        else {
            System.out.println("NO MATCH");
        }
    }

    private static Symbol nextToken() {
        return tokens.get(index);
    }

}