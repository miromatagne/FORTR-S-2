import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static List<Symbol> tokens;
    private static int index;

    public Parser(List<Symbol> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    public static void start() {
        atom();
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
        // token token = nextToken();
        // switch(token) {
        //     case 1 : token = VarName;
        //              match(VarName);
        //              break;
        //     case 2 :    
        // }
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