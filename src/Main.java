import compilador.lexer.Lexer;
import compilador.lexer.TablaSimbolos;
import compilador.parser.Parser;

public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer(args[0]);
       int token = -1;

       while (token != 0) {
           token = lexer.getNextToken().getTokenID();
       }

       TablaSimbolos.imprimirTabla();
    }
}