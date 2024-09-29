%{
    package compilador.parser;
    import compilador.lexer.Lexer;
%}

%token IDENTIFICADOR_GENERICO IDENTIFICADOR_ULONGINT IDENTIFICADOR_SINGLE CONSTANTE_DECIMAL CONSTANTE_OCTAL CONSTANTE_SINGLE SUMA RESTA MULTIPLICACION DIVISION ASIGNACION MAYOR_O_IGUAL MENOR_O_IGUAL MAYOR MENOR IGUAL DESIGUAL PARENTESIS_L PARENTESIS_R COMA PUNTO PUNTO_Y_COMA INLINE_STRING ERROR STRUCT FOR UP DOWN SINGLE ULONGINT IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET TOS

%left SUMA RESTA
%left MULTIPLICACION DIVISION
%%

programa                    :   IDENTIFICADOR_GENERICO BEGIN sentencias END
                            ;

sentencias                  : 	sentencias sentencia
                            |   sentencia
                            ;

sentencia                   :   sentencia_declarativa
                            |   sentencia_ejecutable
                            ;
            
sentencia_declarativa       :   tipo lista_de_identificadores PUNTO_Y_COMA
		                    |   funcion
		                    |   struct PUNTO_Y_COMA
		                    ;
		                
struct                      :   TYPEDEF STRUCT MENOR lista_de_tipos MAYOR "{" lista_de_identificadores "}" IDENTIFICADOR_GENERICO
                            ;
                        
tipo                        :   ULONGINT
		                    |   SINGLE
		                    ;
		                
lista_de_tipos              :   lista_de_tipos COMA tipo
		                    |   tipo
		                    ;
		                
lista_de_identificadores    :   lista_de_identificadores COMA identificador
		                    |   identificador
		                    ;

identificador               :   IDENTIFICADOR_GENERICO
                            |   IDENTIFICADOR_ULONGINT
                            |   IDENTIFICADOR_SINGLE
                            ;

		                
funcion                     :    encabezado_funcion BEGIN cuerpo_funcion END
                            ;

encabezado_funcion          :   tipo FUN IDENTIFICADOR_GENERICO PARENTESIS_L parametro PARENTESIS_R

parametro                   :   tipo identificador
                            ;

cuerpo_funcion              :   cuerpo_funcion sentencia
		                    |   cuerpo_funcion return
		                    |   return
		                    ;
		                
return                      :   RET PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA
                            ;

sentencia_ejecutable        :   sentencia_asignacion PUNTO_Y_COMA
		                    |   sentencia_seleccion PUNTO_Y_COMA
		                    |   sentencia_salida PUNTO_Y_COMA
		                    |   sentencia_control
		                    ;

sentencia_asignacion        :   lista_de_identificadores ASIGNACION lista_de_expresiones
                            ;

sentencia_seleccion         :   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF
                            ;

cuerpo_if                   :   bloque_de_sent_ejecutables bloque_else
		                    |   bloque_de_sent_ejecutables
		                    ;

bloque_else                 :   ELSE bloque_de_sent_ejecutables
                            ;

condicion                   :   expresion comparador expresion
                            ;

comparador                  :   MAYOR
                            |   MENOR
                            |   MAYOR_O_IGUAL
                            |   MENOR_O_IGUAL
                            |   IGUAL
                            |   DESIGUAL
                            ;

bloque_de_sent_ejecutables  :   BEGIN sentencias_ejecutables END PUNTO_Y_COMA
			                |   sentencia_ejecutable
			                ;

sentencias_ejecutables      :   sentencias_ejecutables sentencia_ejecutable
		                    |   sentencia_ejecutable
		                    ;

sentencia_salida            :   OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R
		                    |   OUTF PARENTESIS_L expresion PARENTESIS_R
		                    ;

sentencia_control           :   encabezado_for bloque_de_sent_ejecutables
                            ;

encabezado_for              :   FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion PARENTESIS_R
                            ;

asignacion_enteros          :   identificador ASIGNACION CONSTANTE_DECIMAL
                            |   identificador ASIGNACION CONSTANTE_OCTAL
                            ;

accion                      :   UP CONSTANTE_DECIMAL
                            |   UP CONSTANTE_OCTAL
                            |   DOWN CONSTANTE_OCTAL
                            |   DOWN CONSTANTE_DECIMAL
                            ;

lista_de_expresiones        :   lista_de_expresiones COMA expresion
		                    |   expresion
		                    ;

expresion                   :   TOS PARENTESIS_L expresion_aritmetica PARENTESIS_R
		                    |   expresion_aritmetica
		                    ;

expresion_aritmetica        :   expresion SUMA termino
		                    |   expresion RESTA termino
		                    |   termino
		                    ;

termino                     :   termino MULTIPLICACION factor
		                    |   termino DIVISION factor
		                    |   factor
		                    ;

factor                      :   identificador
		                    |   CONSTANTE_DECIMAL
		                    |   CONSTANTE_OCTAL
		                    |   CONSTANTE_SINGLE
		                    |   invocacion_a_funcion
		                    ;

invocacion_a_funcion      :   IDENTIFICADOR_GENERICO PARENTESIS_L parametro_real PARENTESIS_R
                            ;

parametro_real              :   expresion
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