import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static List<Symbol> tokens;
    private static int index;

    public Parser(List<Symbol> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    private static void atom() {
        // token token = nextToken();
        // switch(token) {
        //     case 1 : token = VarName;
        //              match(VarName);
        //              break;
        //     case 2 :    
        // }
    }

    private static void match(Symbol symbol) {}

    private static void nextToken() {

    }

}