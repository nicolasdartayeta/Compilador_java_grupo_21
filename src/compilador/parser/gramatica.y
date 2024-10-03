%{
    package compilador.parser;
    import compilador.lexer.Lexer;
    import compilador.lexer.token.*;
    import compilador.lexer.TablaSimbolos;
    import compilador.lexer.CampoTablaSimbolos;
    import compilador.lexer.TablaToken;

%}

%token IDENTIFICADOR_GENERICO IDENTIFICADOR_FUN IDENTIFICADOR_TIPO IDENTIFICADOR_ULONGINT IDENTIFICADOR_SINGLE CONSTANTE_DECIMAL CONSTANTE_OCTAL CONSTANTE_SINGLE SUMA RESTA MULTIPLICACION DIVISION ASIGNACION MAYOR_O_IGUAL MENOR_O_IGUAL MAYOR MENOR IGUAL DESIGUAL CORCHETE_L CORCHETE_R PARENTESIS_L PARENTESIS_R COMA PUNTO PUNTO_Y_COMA INLINE_STRING ERROR STRUCT FOR UP DOWN SINGLE ULONGINT IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET TOS

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

sentencia_declarativa       :   tipo lista_de_identificadores PUNTO_Y_COMA  { System.out.println("Declaracion de VARIABLES detectada"); }
		                    |   funcion { System.out.println("Linea " + $1.ival + ": " + "Declaracion de FUNCION detectada"); }
		                    |   struct   { System.out.println("Linea " + $1.ival + ": " + "Declaracion de STRUCT detectada"); }
		                    ;

struct                      :   TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA {
                                                                                                                                                                $$.ival = ((Token) $1.obj).getNumeroDeLinea();
                                                                                                                                                                String lexema = ((Token) $9.obj).getLexema().toString();
                                                                                                                                                                TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                                }
                            ;

tipo                        :   ULONGINT { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
		                    |   SINGLE { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
		                    |   IDENTIFICADOR_TIPO { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
		                    ;

lista_de_tipos              :   lista_de_tipos COMA tipo
		                    |   tipo
		                    ;

lista_de_identificadores    :   lista_de_identificadores COMA identificador
		                    |   identificador { $$.ival = $1.ival; }
		                    ;

identificador               :   identificador_simple { $$.ival = $1.ival; }
                            |   identificador_compuesto { $$.ival = $1.ival; }
                            ;

identificador_simple        :   IDENTIFICADOR_GENERICO { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
                            |   IDENTIFICADOR_ULONGINT { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
                            |   IDENTIFICADOR_SINGLE { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
                            ;

identificador_compuesto     :   identificador_simple PUNTO identificador_compuesto { $$.ival = $1.ival; }
                            |   identificador_simple PUNTO identificador_simple { $$.ival = $1.ival; }
                            ;

funcion                     :    encabezado_funcion BEGIN cuerpo_funcion END { $$.ival = $1.ival; }
                            ;

encabezado_funcion          :   tipo FUN IDENTIFICADOR_GENERICO PARENTESIS_L parametro PARENTESIS_R {
                                                                                                        $$.ival = $1.ival;
                                                                                                        String lexema = ((Token) $3.obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                        }

parametro                   :   tipo identificador
                            ;

cuerpo_funcion              :   cuerpo_funcion sentencia_ejecutable_en_funcion
                            |   cuerpo_funcion sentencia_declarativa
                            |   sentencia_ejecutable_en_funcion
                            |   sentencia_declarativa
                            ;

sentencia_ejecutable_en_funcion         :   sentencia_asignacion PUNTO_Y_COMA { System.out.println("Linea " + $1.ival + ": " + "ASIGNACION detectada"); }
                                        |   sentencia_seleccion_en_funcion PUNTO_Y_COMA { System.out.println("Linea " + $1.ival + ": " + "Sentencia IF detectada"); }
                                        |   sentencia_salida PUNTO_Y_COMA { System.out.println("Linea " + $1.ival + ": " + "Sentencia de SALIDA detectada"); }
                                        |   sentencia_control_en_funcion { System.out.println("Linea " + $1.ival + ": " + "Sentencia FOR detectada"); }
                                        |   sentencia_retorno
                                        ;

sentencia_control_en_funcion           :   encabezado_for bloque_de_sent_ejecutables_en_funcion { $$.ival = $1.ival; }
                                       ;

sentencia_retorno           :   RET PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA
                            ;

sentencia_seleccion_en_funcion  :   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
                                ;

cuerpo_if_en_funcion        :   bloque_de_sent_ejecutables_en_funcion bloque_else_en_funcion
		                    |   bloque_de_sent_ejecutables_en_funcion
		                    ;

bloque_else_en_funcion      :   ELSE bloque_de_sent_ejecutables_en_funcion
                            ;

bloque_de_sent_ejecutables_en_funcion   :   BEGIN sentencias_ejecutable_en_funcion END PUNTO_Y_COMA
                                        |   sentencia_ejecutable_en_funcion
                                        ;

sentencias_ejecutable_en_funcion        :   sentencias_ejecutable_en_funcion sentencia_ejecutable_en_funcion
                                        |   sentencia_ejecutable_en_funcion
                                        ;

sentencia_ejecutable        :   sentencia_asignacion PUNTO_Y_COMA { System.out.println("Linea " + $1.ival + ": " + "ASIGNACION detectada"); }
		                    |   sentencia_seleccion PUNTO_Y_COMA { System.out.println("Linea " + $1.ival + ": " + "Sentencia IF detectada"); }
		                    |   sentencia_salida PUNTO_Y_COMA { System.out.println("Linea " + $1.ival + ": " + "Sentencia de SALIDA detectada"); }
		                    |   sentencia_control { System.out.println("Linea " + $1.ival + ": " + "Sentencia FOR detectada"); }
		                    ;

sentencia_asignacion        :   lista_de_identificadores ASIGNACION lista_de_expresiones { $$.ival = $1.ival; }
                            ;

sentencia_seleccion         :   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
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

sentencia_salida            :   OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
		                    |   OUTF PARENTESIS_L expresion PARENTESIS_R { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
		                    ;

sentencia_control           :   encabezado_for bloque_de_sent_ejecutables { $$.ival = $1.ival; }
                            ;

encabezado_for              :   FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion PARENTESIS_R { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
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
                            |   constante
                            |   RESTA constante {
                                                    String lexema = ((Token) $2.obj).getLexema();
                                                    String tipoConstante = ((Token) $2.obj).getTokenName();

                                                    int cantidadDeUsos = TablaSimbolos.getCantidadDeUsos(lexema);
                                                    String tipoLexemaOriginal = TablaSimbolos.getTipo(lexema);

                                                    if (cantidadDeUsos > 1) {
                                                        // Bajar la cantidad de usos en 1
                                                        TablaSimbolos.decrementarUso(lexema);
                                                    } else {
                                                        // Eliminar la entrada
                                                        TablaSimbolos.eliminarLexema(lexema);
                                                    }

                                                    String lexemaNegativo = "-" + lexema;

                                                    // Chequear rango
                                                    if (TablaToken.CONSTANTE_DECIMAL.equals(tipoConstante)) {
                                                        // long numero = Long.parseUnsignedLong(lexemaNegativo);
                                                    } else if (TablaToken.CONSTANTE_OCTAL.equals(tipoConstante)) {
                                                        // long numero = Long.parseUnsignedLong(lexemaNegativo,8);
                                                    } else if (TablaToken.CONSTANTE_SINGLE.equals(tipoConstante)) {
                                                        float numero = Float.parseFloat(lexemaNegativo.replace('s','e'));
                                                        if (numero == Float.POSITIVE_INFINITY || numero == Float.NEGATIVE_INFINITY || numero == -0.0f) {
                                                            System.out.println(numero);
                                                            throw new NumberFormatException("Linea " + ((Token) $2.obj).getNumeroDeLinea() + ": la constante se va de rango");
                                                        }
                                                    }

                                                    // Hay que fijarse si ya esta la negativa en la tabla, sino agregarla como negativa.
                                                    if (TablaSimbolos.existeLexema(lexemaNegativo)) {
                                                        TablaSimbolos.aumentarUso(lexemaNegativo);
                                                    } else {
                                                        TablaSimbolos.agregarLexema(lexemaNegativo, new CampoTablaSimbolos(false, tipoLexemaOriginal));
                                                        TablaSimbolos.aumentarUso(lexemaNegativo);
                                                    }
                                                }
		                    |   invocacion_a_funcion
		                    ;

constante                   :   CONSTANTE_DECIMAL { $$.obj = ((Token) $1.obj); }
                            |   CONSTANTE_OCTAL { $$.obj = ((Token) $1.obj); }
                            |   CONSTANTE_SINGLE { $$.obj = ((Token) $1.obj); }
                            ;

invocacion_a_funcion        :   IDENTIFICADOR_FUN PARENTESIS_L parametro_real PARENTESIS_R
                            ;

parametro_real              :   expresion
                            ;


%%
private static Lexer lex;

public static void main(String[] args) {
    Lexer lexer = new Lexer("src/programa_sin_funciones.txt");
    Parser.lex = lexer;
    Parser parser = new Parser(true);
    parser.run();
    TablaSimbolos.imprimirTabla();
}

private int yylex() {
    Token tok = lex.getNextToken();
    yylval = new ParserVal(tok);
    return tok.getTokenID();
}

private void yyerror(String string) {
  throw new UnsupportedOperationException("ERROR");
}