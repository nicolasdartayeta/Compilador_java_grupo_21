%{
    package compilador.parser;
    import compilador.lexer.Lexer;
%}

%token IDENTIFICADOR CONSTANTE_DECIMAL CONSTANTE_OCTAL CONSTANTE_SINGLE SUMA RESTA MULTIPLICACION DIVISION ASIGNACION MAYOR_O_IGUAL MENOR_O_IGUAL MAYOR MENOR IGUAL DESIGUAL PARENTESIS_L PATENTESIS_R COMA PUNTO PUNTO_Y_COMA INLINE_STRING ERROR STRUCT FOR UP DOWN SINGLE ULONGINT IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET

%%

programa    :   IDENTIFICADOR BEGIN  END
            ;


%%
private static Lexer lex;

public static void main(String[] args) {
    Lexer lexer = new Lexer("C:/Users/santi/Downloads/programa1.txt");
    Parser.lex = lexer;
    Parser parser = new Parser(true);
    parser.run();
    }

private int yylex() {
    return lex.yylex();
}

private void yyerror(String string) {
  throw new UnsupportedOperationException("ERROR");
}