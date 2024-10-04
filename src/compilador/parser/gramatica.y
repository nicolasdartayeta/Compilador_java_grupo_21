%{
    import compilador.lexer.Lexer;
    import compilador.lexer.token.*;
    import compilador.lexer.TablaSimbolos;
    import compilador.lexer.CampoTablaSimbolos;
    import compilador.lexer.TablaToken;
    import java.util.ArrayList;
    import java.util.List;

%}

%token IDENTIFICADOR_GENERICO IDENTIFICADOR_FUN IDENTIFICADOR_TIPO IDENTIFICADOR_ULONGINT IDENTIFICADOR_SINGLE CONSTANTE_DECIMAL CONSTANTE_OCTAL CONSTANTE_SINGLE SUMA RESTA MULTIPLICACION DIVISION ASIGNACION MAYOR_O_IGUAL MENOR_O_IGUAL MAYOR MENOR IGUAL DESIGUAL CORCHETE_L CORCHETE_R PARENTESIS_L PARENTESIS_R COMA PUNTO PUNTO_Y_COMA INLINE_STRING ERROR STRUCT FOR UP DOWN SINGLE ULONGINT IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET TOS

%left SUMA RESTA
%left MULTIPLICACION DIVISION

%%

programa                    :   IDENTIFICADOR_GENERICO BEGIN sentencias END { Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "PROGRAMA"); }
                            |   BEGIN sentencias END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea 1: Falta identificador de programa"); }
                            |   IDENTIFICADOR_GENERICO sentencias END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + (((Token) $1.obj).getNumeroDeLinea()+1) + ": Falta un BEGIN despues del identificador del programa"); }
                            |   IDENTIFICADOR_GENERICO BEGIN sentencias { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Ultima linea: Falta un END al final del programa"); }
                            |   IDENTIFICADOR_GENERICO sentencias { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Falta un BEGIN despues del identificador del programa y falta un END al final del programa"); }
                            |   IDENTIFICADOR_GENERICO BEGIN sentencias error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO. <3"); }
                            |   IDENTIFICADOR_GENERICO BEGIN error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO. <3"); }
                            |   IDENTIFICADOR_GENERICO BEGIN sentencias error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO. FALTA EL END AL FINAL DEL PROGRAMA <3"); }
                            |   IDENTIFICADOR_GENERICO BEGIN error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. APRENDE A ESCRIBIR CODIGO. FALTA EL END AL FINAL DEL PROGRAMA <3"); }
                            ;

sentencias                  : 	sentencias sentencia
                            |   sentencia
                            ;

sentencia                   :   sentencia_declarativa
                            |   sentencia_ejecutable
                            // Cualquier error que no se haya agarrado antes se captura aca de manera generica hasta que aparezca un caracter de sincornizacion (';')
                            |   error PUNTO_Y_COMA {agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token)$2.obj).getNumeroDeLinea() + ": Syntax Error");}
                            ;

sentencia_declarativa       :   tipo lista_de_identificadores PUNTO_Y_COMA  { Parser.agregarEstructuraDetectadas($1.ival, "VARIABLE/S"); }
                            |   tipo lista_de_identificadores { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia"); }
		                    |   funcion { Parser.agregarEstructuraDetectadas($1.ival, "FUNCION"); }
		                    |   struct
		                    ;

struct                      :   TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA {
                                                                                                                                                    $$.ival = ((Token) $1.obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) $9.obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                }
                            |   TYPEDEF MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
                            |   TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
                            |   TYPEDEF STRUCT lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
                            |   TYPEDEF MENOR STRUCT lista_de_tipos CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
                            |   TYPEDEF MENOR STRUCT lista_de_tipos CORCHETE_L lista_de_identificadores CORCHETE_R PUNTO_Y_COMA{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}

                            ;

tipo                        :   ULONGINT { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
		                    |   SINGLE { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
		                    |   IDENTIFICADOR_TIPO { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
		                    ;

lista_de_tipos              :   lista_de_tipos COMA tipo
		                    |   tipo
		                    ;

lista_de_identificadores    :   lista_de_identificadores COMA identificador
                            //|   lista_de_identificadores identificador { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ',' entre las variables"); }
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

funcion                     :    encabezado_funcion BEGIN cuerpo_funcion END {
                                                                                $$.ival = $1.ival;
                                                                                if (!returnEncontrado) {
                                                                                    agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta un return en la funcion");
                                                                                }
                                                                                returnEncontrado = false;
                                                                             }
                            ;

encabezado_funcion          :   tipo FUN IDENTIFICADOR_GENERICO PARENTESIS_L parametro PARENTESIS_R {
                                                                                                        $$.ival = $1.ival;
                                                                                                        String lexema = ((Token) $3.obj).getLexema().toString();
                                                                                                        TablaSimbolos.cambiarTipo(lexema, TablaSimbolos.FUN);
                                                                                                    }
                            |   tipo FUN PARENTESIS_L parametro PARENTESIS_R {
                                                                                $$.ival = $1.ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el identificador de la funcion");
                                                                            }
                            ;

parametro                   :   tipo identificador
                            |   identificador { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el tipo del parametro de la funcion"); }
                            |   tipo { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el identificador del parametro de la funcion"); }
                            ;

cuerpo_funcion              :   cuerpo_funcion sentencia_ejecutable_en_funcion
                            |   cuerpo_funcion sentencia_declarativa
                            |   sentencia_ejecutable_en_funcion
                            |   sentencia_declarativa
                            ;

sentencia_ejecutable_en_funcion         :   sentencia_asignacion PUNTO_Y_COMA { Parser.agregarEstructuraDetectadas($1.ival, "ASIGNACION"); }
                                        |   sentencia_seleccion_en_funcion PUNTO_Y_COMA { Parser.agregarEstructuraDetectadas($1.ival, "IF"); }
                                        |   sentencia_salida PUNTO_Y_COMA { Parser.agregarEstructuraDetectadas($1.ival, "IF"); }
                                        |   sentencia_control_en_funcion { Parser.agregarEstructuraDetectadas($1.ival, "FOR"); }
                                        |   sentencia_retorno { Parser.agregarEstructuraDetectadas($1.ival, "RET"); returnEncontrado = true; }
                                        ;

sentencia_control_en_funcion           :   encabezado_for bloque_de_sent_ejecutables_en_funcion { $$.ival = $1.ival; }
                                       |   encabezado_for error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el cuerpo del FOR"); }
                                       ;

sentencia_retorno           :   RET PARENTESIS_L expresion PARENTESIS_R PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
                            ;

sentencia_seleccion_en_funcion  :   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
                                |   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
                                |   IF PARENTESIS_L condicion THEN cuerpo_if_en_funcion END_IF { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
                                |   IF condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
                                |   IF condicion THEN cuerpo_if_en_funcion END_IF { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
                                |   IF PARENTESIS_L condicion PARENTESIS_R THEN END_IF  { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
                                ;

cuerpo_if_en_funcion        :   bloque_de_sent_ejecutables_en_funcion bloque_else_en_funcion
		                    |   bloque_de_sent_ejecutables_en_funcion
		                    ;

bloque_else_en_funcion      :   ELSE bloque_de_sent_ejecutables_en_funcion
                            |   ELSE { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
                            ;

bloque_de_sent_ejecutables_en_funcion   :   BEGIN sentencias_ejecutable_en_funcion END PUNTO_Y_COMA
                                        |   sentencia_ejecutable_en_funcion
                                        ;

sentencias_ejecutable_en_funcion        :   sentencias_ejecutable_en_funcion sentencia_ejecutable_en_funcion
                                        |   sentencia_ejecutable_en_funcion
                                        ;

sentencia_ejecutable        :   sentencia_asignacion PUNTO_Y_COMA { Parser.agregarEstructuraDetectadas($1.ival, "ASIGNACION"); }
                            |   sentencia_asignacion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia");}
		                    |   sentencia_seleccion PUNTO_Y_COMA { Parser.agregarEstructuraDetectadas($1.ival, "IF"); }
		                    |   sentencia_seleccion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia");}
		                    |   sentencia_salida PUNTO_Y_COMA { Parser.agregarEstructuraDetectadas($1.ival, "SALIDA"); }
		                    |   sentencia_control { Parser.agregarEstructuraDetectadas($1.ival, "FOR"); }
		                    ;

sentencia_asignacion        :   lista_de_identificadores ASIGNACION lista_de_expresiones { $$.ival = $1.ival; }
                            ;

sentencia_seleccion         :   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
                            |   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
                            |   IF PARENTESIS_L condicion THEN cuerpo_if END_IF { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
                            |   IF condicion PARENTESIS_R THEN cuerpo_if END_IF { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
                            |   IF condicion THEN cuerpo_if END_IF { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
                            |   IF PARENTESIS_L condicion PARENTESIS_R THEN END_IF  { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
                            ;

cuerpo_if                   :   bloque_de_sent_ejecutables bloque_else
		                    |   bloque_de_sent_ejecutables
		                    ;

bloque_else                 :   ELSE bloque_de_sent_ejecutables
                            |   ELSE { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
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
		                    |   OUTF PARENTESIS_L PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
		                    ;

sentencia_control           :   encabezado_for bloque_de_sent_ejecutables { $$.ival = $1.ival; }
                            |   encabezado_for error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el cuerpo del FOR"); }
                            ;

encabezado_for              :   encabezado_for_obligatorio PUNTO_Y_COMA PARENTESIS_L condicion PARENTESIS_R PARENTESIS_R {$$.ival = $1.ival;}
                            |   encabezado_for_obligatorio PARENTESIS_R {$$.ival = $1.ival;}
                            |   encabezado_for_obligatorio { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
                            |   encabezado_for_obligatorio PUNTO_Y_COMA PARENTESIS_L condicion PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
                            |   encabezado_for_obligatorio PARENTESIS_L condicion PARENTESIS_R PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta un ';' en el encabezado del FOR"); }
                            |   encabezado_for_obligatorio PUNTO_Y_COMA condicion PARENTESIS_R PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
                            |   encabezado_for_obligatorio PUNTO_Y_COMA PARENTESIS_L condicion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
                            ;

encabezado_for_obligatorio  :   FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); }
                           |   FOR asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis derecho en el encabezado del FOR"); }
                           |   FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta la acción en el encabezado del FOR"); }
                           |   FOR PARENTESIS_L asignacion_enteros condicion PUNTO_Y_COMA accion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
                           |   FOR PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion accion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta un ';' en el encabezado del FOR"); }
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
                            |   TOS PARENTESIS_L PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta la expresión"); }
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

// Tipo de mensajes
public static final String ERROR_LEXICO = "ERROR_LEXICO";
public static final String ERROR_SINTACTICO = "ERROR_SINTACTICO";

// Código ANSI para texto rojo
public static final String ROJO = "\033[31m";
public static final String VERDE = "\033[32m";
public static final String RESET = "\033[0m";

// Flags
public static boolean parsingConErrores = false;
public static boolean lexingConErrores = false;
private static boolean returnEncontrado = false;

// Lista de estructuras detectadas
public static final List<String> estructurasDetectadas = new ArrayList<>();

// Listas de errores
public static final List<String> erroresLexicos = new ArrayList<>();
public static final List<String> erroresSintacticos = new ArrayList<>();

// Agregar error a la lista de errores
public static void agregarEstructuraDetectadas(int numeroLinea, String estructura) {
    estructurasDetectadas.add("Linea " + numeroLinea + ": " + "Declaracion de " + estructura + " detectada");
}

// Agregar error a la lista de errores
public static void agregarError(List<String> listaErrores, String tipo, String error) {
    if (tipo.equals(Parser.ERROR_SINTACTICO)) {
        parsingConErrores = true;
    } else if (tipo.equals(Parser.ERROR_LEXICO)) {
        lexingConErrores = true;
    }

    listaErrores.add(String.format("%-20s", tipo) + "| "+ error);
}

public static void imprimirLista(List<String> lista) {
    for (String elemento : lista) {
        System.out.println(elemento);
    }
}

public static void main(String[] args) {
    Lexer lexer = new Lexer("src/programa_sin_funciones.txt");
    Parser.lex = lexer;
    Parser parser = new Parser(true);
    parser.run();

    if (Parser.lexingConErrores){
        System.out.println(Parser.ROJO + "SE ENCONTRARON ERRORES LEXICOS" + Parser.RESET);
        Parser.imprimirLista(erroresLexicos);
    } else {
        System.out.println(Parser.VERDE + "NO SE ENCONTRARON ERRORES LEXICOS" + Parser.RESET);
    }

    if (Parser.parsingConErrores){
        System.out.println(Parser.ROJO + "SE ENCONTRARON ERRORES SINTACTICOS" + Parser.RESET);
        Parser.imprimirLista(erroresSintacticos);
    } else {
        System.out.println(Parser.VERDE + "NO SE ENCONTRARON ERRORES SINTACTICOS" + Parser.RESET);
    }

    Parser.imprimirLista(estructurasDetectadas);

    TablaSimbolos.imprimirTabla();
}

private int yylex() {
    Token tok = lex.getNextToken();

    if (tok.isError()) {
        agregarError(erroresLexicos, ERROR_LEXICO, ((TokenError) tok).getDescripcionError());
    }

    yylval = new ParserVal(tok);
    return tok.getTokenID();
}

private void yyerror(String string) {
  System.out.println("Error: " + string );
}