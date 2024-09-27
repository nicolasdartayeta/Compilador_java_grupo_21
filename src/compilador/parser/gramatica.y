%{
    package compilador.parser;
    import compilador.lexer.Lexer;
%}

%token IDENTIFICADOR CONSTANTE_DECIMAL CONSTANTE_OCTAL CONSTANTE_SINGLE SUMA RESTA MULTIPLICACION DIVISION ASIGNACION MAYOR_O_IGUAL MENOR_O_IGUAL MAYOR MENOR IGUAL DESIGUAL PARENTESIS_L PATENTESIS_R COMA PUNTO PUNTO_Y_COMA INLINE_STRING ERROR STRUCT FOR UP DOWN SINGLE ULONGINT IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET

%%

programa                :   IDENTIFICADOR BEGIN sentencias END
                        ;

sentencias              : 	sentencias sentencia
                        |   sentencia
                        ;
sentencia               :   sentencia_declarativa PUNTO_Y_COMA
                        |   sentencia_ejecutable PUNTO_Y_COMA
                        ;
            
sentencia_declarativa   :   tipo lista_de_identificadores
		                |   funcion
		                |   struct
		                ;
		                
struct                  :   TYPEDEF STRUCT MENOR lista_de_tipos MAYOR { lista_de_identificadores }
                        ;
                        
tipo                    :   ULONGINT
		                |   SINGLE
		                ;
		                
lista_de_tipos          :   lista_de_tipos COMA tipo
		                |   tipo
		                ;
		                
lista_de_identificadores:   lista_de_identificadores COMA IDENTIFICADOR
		                |   ID
		                ;
		                
funcion                 :   tipo FUN IDENTIFICADOR PARENTESIS_L parametro PARENTESIS_R BEGIN cuerpo_funcion END
                        ;
                        
parametro               :   tipo IDENTIFICADOR
cuerpo_funcion          :   cuerpo_funcion sentencia
		                |   cuerpo_funcion return
		                ;
		                
return                  :   RET PARENTESIS_L expresion PARENTESIS_R
sentencia_ejecutable    :   asignacion
		                |   sentencia_seleccion
		                |   sentencia_salida
		                |   sentencia_control
		                ;

asignacion              :   lista_de_identificadores ASIGNACION lista_de_expresiones
                        ;

sentencia_seleccion     :   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF
                        ;

cuerpo_if               :   bloque_de_sent_ejecutables bloque_else
		                |   bloque_de_sent_ejecutables
		                ;

bloque_else             :   ELSE bloque_de_sent_ejecutables
                        ;

condicion               :   expresion comparador expresion
                        ;

bloque_de_sent_ejecutables  :   BEGIN sentencias_ejecutables END
			                |   sentencia_ejecutable
			                ;

sentencias_ejecutables      :   sentencias_ejecutables sentencia_ejecutable
		                    |   sentencia_ejecutable
		                    ;

sentencia_salida            :   OUTF PARENTESIS_L { INLINE_STRING } PARENTESIS_R
		                    |   OUTF PARENTESIS_L expresion PARENTESIS_R
		                    ;

// REHACER
sentencia_control >	->	FOR PARENTESIS_L ID ASIGNACION CONSTANTE PUNTO_Y_COMA < condicion > PUNTO_Y_COMA UP CONSTANTE (PUNTO_Y_COMA “(” < condicion >”)”)? PARENTESIS_R < bloque_de_sentencias_ejecutables >
		FOR PARENTESIS_L ID ASIGNACION CONSTANTE PUNTO_Y_COMA < condicion > PUNTO_Y_COMA DOWN CONSTANTE (PUNTO_Y_COMA “(” < condicion >”)”)? PARENTESIS_R < bloque_de_sentencias_ejecutables >

lista_de_expresiones >	->	< lista_de_expresiones > COMA < expresion >
		< expresion >
expresion >	->	TOS PARENTESIS_L < expresion_aritmetica > PARENTESIS_R
		< expresion_aritmetica >
expresion_aritmetica >	->	< expresion > + < termino >
		< expresion > - < termino >
		< termino >
termino >	->	< termino > * < factor >
		< termino> / < factor >
		< factor >
< factor >	->	ID
		CONSTANTE
		< invocacion_a_funcion >
		< expresion >
< invocacion_a_funcion >	->	ID PARENTESIS_L < parametro_real > ”)”
< parametro_real >	->	< expresion >


%%
private static Lexer lex;

public static void main(String[] args) {
    Lexer lexer = new Lexer("hola begin end");
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