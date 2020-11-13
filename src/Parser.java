import java.util.List;

/**
 * Rules done :
 * 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30
 */

public class Parser {
    private static List<Symbol> tokens;
    private static int index;

    public Parser(List<Symbol> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    public static void start() {
        program();
    }

    private static void program() {
        match(new Symbol(LexicalUnit.BEGINPROG));
        match(new Symbol(LexicalUnit.PROGNAME));
        match(new Symbol(LexicalUnit.ENDLINE));
        code();
        match(new Symbol(LexicalUnit.ENDPROG));
    }

    private static void code() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case VARNAME:
                instruction();
                match(new Symbol(LexicalUnit.ENDLINE));
                code();
                break;
            case IF:
                instruction();
                match(new Symbol(LexicalUnit.ENDLINE));
                code();
                break;
            case WHILE:
                instruction();
                match(new Symbol(LexicalUnit.ENDLINE));
                code();
                break;
            case PRINT:
                instruction();
                match(new Symbol(LexicalUnit.ENDLINE));
                code();
                break;
            case READ:
                instruction();
                match(new Symbol(LexicalUnit.ENDLINE));
                code();
                break;
            case ENDPROG:
            case ENDWHILE:
            case ELSE:
            case ENDIF:
                break;
            default:
                System.out.println("ERROR" + token.toString());
        }
    }

    private static void instruction() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case VARNAME:
                assign();
                break;
            case IF:
                If();
                break;
            case WHILE:  
                While();
                break;
            case PRINT:
                print();
                break; 
            case READ:
                read();
                break;
            default:
                System.out.println("ERROR");
        }
    }

    private static void assign() {
        match(new Symbol(LexicalUnit.VARNAME));
        match(new Symbol(LexicalUnit.ASSIGN));
        exprArith();
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
                break;
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
                break;
            case LPAREN:
                match(new Symbol(LexicalUnit.LPAREN));
                exprArith();
                match(new Symbol(LexicalUnit.RPAREN));
                break;
            case VARNAME:
                match(new Symbol(LexicalUnit.VARNAME));
                break;
            case NUMBER:
                match(new Symbol(LexicalUnit.NUMBER));
                break;
            default:
                System.out.println("ERROR ATOM");
        }
    }

    private static void If() {
        match(new Symbol(LexicalUnit.IF));
        match(new Symbol(LexicalUnit.LPAREN));
        cond();
        match(new Symbol(LexicalUnit.RPAREN));
        match(new Symbol(LexicalUnit.THEN));
        match(new Symbol(LexicalUnit.ENDLINE));
        code();
        ifTail();
    }

    private static void ifTail() {
        Symbol token = nextToken();
        switch(token.getType()) {
            case ENDIF:
                match(new Symbol(LexicalUnit.ENDIF));
                break;
            case ELSE:
                match(new Symbol(LexicalUnit.ELSE));
                match(new Symbol(LexicalUnit.ENDLINE));
                code();
                match(new Symbol(LexicalUnit.ENDIF));
                break;
            default:
               // System.out.println("ERROR");
        }
    }

    private static void cond() {
        exprArith();
        comp();
        exprArith();
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

    private static void While() {
        match(new Symbol(LexicalUnit.WHILE));
        match(new Symbol(LexicalUnit.LPAREN));
        cond();
        match(new Symbol(LexicalUnit.RPAREN));
        match(new Symbol(LexicalUnit.DO));
        match(new Symbol(LexicalUnit.ENDLINE));
        code();
        match(new Symbol(LexicalUnit.ENDWHILE));
    }

    private static void print() {
        match(new Symbol(LexicalUnit.PRINT));
        match(new Symbol(LexicalUnit.LPAREN));   
        match(new Symbol(LexicalUnit.VARNAME));   
        match(new Symbol(LexicalUnit.RPAREN));      
    }

    private static void read() {
        match(new Symbol(LexicalUnit.READ));
        match(new Symbol(LexicalUnit.LPAREN));   
        match(new Symbol(LexicalUnit.VARNAME));   
        match(new Symbol(LexicalUnit.RPAREN));   
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