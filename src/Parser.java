//Parser Class
import java.util.ArrayList;

public class Parser {


    private static void atom() {
        token token = next_token();
        switch(token) {
            case 1 : token = VarName;
                     match(VarName);
                     break;
            case 2 :    
        }

    }

    private static match(Symbol symbol) {}

}