import compilador.Lexer;

public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("hola end 5 04 3.2s3");
        int token = -1;

        while (token != 39) {
            token = lexer.yylex();
        }
    }
}