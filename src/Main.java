import compilador.lexer.Lexer;
import compilador.parser.Parser;

public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("src/programa.txt");
       int token = -1;

       while (token != 0) {
           token = lexer.getNextToken().getTokenID();
       }
    }
}