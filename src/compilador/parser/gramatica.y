%{
    import compilador.lexer.Lexer;
    import compilador.lexer.token.*;
    import compilador.lexer.TablaSimbolos;
    import compilador.lexer.CampoTablaSimbolos;
    import compilador.lexer.TablaToken;
    import java.util.ArrayList;
    import java.util.List;

%}

%token IDENTIFICADOR_GENERICO IDENTIFICADOR_FUN IDENTIFICADOR_TIPO IDENTIFICADOR_ULONGINT IDENTIFICADOR_SINGLE CONSTANTE_DECIMAL CONSTANTE_OCTAL CONSTANTE_SINGLE SUMA RESTA MULTIPLICACION DIVISION ASIGNACION MAYOR_O_IGUAL MENOR_O_IGUAL MAYOR MENOR IGUAL DESIGUAL CORCHETE_L CORCHETE_R PARENTESIS_L PARENTESIS_R COMA PUNTO PUNTO_Y_COMA INLINE_STRING TOKERROR STRUCT FOR UP DOWN SINGLE ULONGINT IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET TOS

%left SUMA RESTA
%left MULTIPLICACION DIVISION

%%

programa                    :   identificador_simple BEGIN sentencias END { Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "PROGRAMA"); }
                            |   identificador_simple BEGIN sentencias END error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) $4.obj).getNumeroDeLinea() + ": Todo lo que esta despues del END no forma parte del programa."); }
			                |   identificador_simple error BEGIN sentencias END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) $4.obj).getNumeroDeLinea() + ": Todo lo que esta despues del identificador del programa y antes del primer begin no forma parte del programa."); }
                            |   BEGIN sentencias END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea 1: Falta identificador de programa"); }
                            |   identificador_simple sentencias END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + (((Token) $1.obj).getNumeroDeLinea() +1) + ": Falta un BEGIN despues del identificador del programa"); }
                            |   identificador_simple BEGIN sentencias { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Ultima linea: Falta un END al final del programa"); }
                            |   identificador_simple sentencias { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Falta un BEGIN despues del identificador del programa y falta un END al final del programa"); }
                            |   identificador_simple BEGIN sentencias error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR."); }
                            |   identificador_simple BEGIN error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR."); }
                            |   identificador_simple BEGIN sentencias error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. FALTA EL END AL FINAL DEL PROGRAMA"); }
                            |   identificador_simple BEGIN error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "HUBO ERRORES IRRECUPERABLES PORQUE NO SE ENCONTRO UN CARACTER DE SINCRONIZACION LUEGO DEL ERROR. FALTA EL END AL FINAL DEL PROGRAMA"); }
                            |   identificador_simple sentencias BEGIN sentencias END  { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) $2.obj).getNumeroDeLinea() + ": No se permite sentencias entre el identificador del programa y el BEGIN."); }
                            ;

sentencias                  : 	sentencias sentencia
                            |   sentencia
                            ;

sentencia                   :   sentencia_declarativa
                            |   sentencia_ejecutable
                            // Cualquier error que no se haya agarrado antes se captura aca de manera generica hasta que aparezca un caracter de sincornizacion (';')
                            |   error PUNTO_Y_COMA {agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token)$2.obj).getNumeroDeLinea() + ": Syntax Error"); }
                            ;

sentencia_declarativa       :   tipo lista_de_identificadores PUNTO_Y_COMA  { Parser.agregarEstructuraDetectadas($1.ival, "VARIABLE/S"); eliminarUltimosElementos(representacionPolaca, cantidadIdEnListaId); cantidadIdEnListaId = 0; agregarTipoAIdentificadores($1.sval); listaIdentificadores.clear(); }
                            |   tipo lista_de_identificadores error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia"); }
		                    |   tipo lista_de_identificadores error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia"); }
		                    |   funcion { Parser.agregarEstructuraDetectadas($1.ival, "FUNCION"); }
		                    |   struct
		                    ;

struct                      :   TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA {
                                                                                                                                                    $$.ival = ((Token) $1.obj).getNumeroDeLinea();
                                                                                                                                                    Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "STRUCT");
                                                                                                                                                    String lexema = ((Token) $9.obj).getLexema().toString();
                                                                                                                                                    TablaSimbolos.convertirATipo(lexema, TablaSimbolos.STRUCT);
                                                                                                                                                    eliminarUltimosElementos(representacionPolaca, cantidadIdEnListaId);
                                                                                                                                                    cantidadIdEnListaId = 0;
                                                                                                                                                     listaIdentificadores.clear();
                                                                                                                                                }
                            |   TYPEDEF MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta palabra STRUCT en la declaración del STRUCT"); }
                            |   TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' en la declaración del STRUCT"); }
                            |   TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia"); }
                            |   TYPEDEF STRUCT lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta simbolo '<' en la declaración del STRUCT");}
                            |   TYPEDEF STRUCT lista_de_tipos CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta simbolos '<' y '>' en la declaración del STRUCT");}
                            |   TYPEDEF STRUCT MENOR lista_de_tipos CORCHETE_L lista_de_identificadores CORCHETE_R IDENTIFICADOR_GENERICO PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta simbolo '>' en la declaración del STRUCT");}
                            |   TYPEDEF STRUCT MENOR lista_de_tipos MAYOR CORCHETE_L lista_de_identificadores CORCHETE_R PUNTO_Y_COMA{ agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta identificador al final de la declaración del STRUCT");}

                            ;

tipo                        :   ULONGINT { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); $$.sval = ((Token) $1.obj).getLexema().toUpperCase(); }
		                    |   SINGLE { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); $$.sval = ((Token) $1.obj).getLexema(); }
		                    |   IDENTIFICADOR_TIPO { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); $$.sval = ((Token) $1.obj).getLexema(); }
		                    ;

lista_de_tipos              :   lista_de_tipos COMA tipo
		                    |   tipo
		                    ;

lista_de_identificadores    :   lista_de_identificadores COMA identificador {cantidadIdEnListaId++; listaIdentificadores.add($3.sval);}
                            |   lista_de_identificadores identificador { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ',' entre las identificadores"); }
		                    |   identificador { $$.ival = $1.ival; cantidadIdEnListaId++; listaIdentificadores.add($1.sval);}
		                    ;

identificador               :   identificador_simple { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); $$.sval = ((Token) $1.obj).getLexema(); representacionPolaca.add(((Token) $1.obj).getLexema());}
                            |   identificador_compuesto { $$.ival = $1.ival; $$.sval = $1.sval; System.out.println("ID COMPUESTO: " + $1.sval); representacionPolaca.add($1.sval);}
                            ;

identificador_simple        :   IDENTIFICADOR_GENERICO { $$.obj = ((Token) $1.obj); }
                            |   IDENTIFICADOR_ULONGINT { $$.obj = ((Token) $1.obj); }
                            |   IDENTIFICADOR_SINGLE { $$.obj = ((Token) $1.obj); }
                            ;

identificador_compuesto     :   identificador_simple PUNTO identificador_compuesto { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); $$.sval = ((Token) $1.obj).getLexema() + ((Token) $2.obj).getLexema() + $3.sval;}
                            |   identificador_simple PUNTO identificador_simple { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); $$.sval = ((Token) $1.obj).getLexema() + ((Token) $2.obj).getLexema() +((Token) $3.obj).getLexema();}
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
                                                                                                        TablaSimbolos.setCantidadDeParametros(lexema, 1);
                                                                                                        TablaSimbolos.setTipoParametro(lexema, $5.sval);
                                                                                                        TablaSimbolos.setTipoRetorno(lexema, $1.sval);
                                                                                                    }
                            |   tipo FUN PARENTESIS_L parametro PARENTESIS_R {
                                                                                $$.ival = $1.ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el identificador de la funcion");
                                                                             }
                            ;

parametro                   :   tipo identificador { $$.sval = $1.sval; TablaSimbolos.cambiarTipo($2.sval, $1.sval); }
                            |   identificador { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el tipo del parametro de la funcion"); }
                            |   tipo { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el identificador del parametro de la funcion"); }
                            |   tipo identificador error {agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": La funcion no puede tener mas de un parametro");}
                            |   error {agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $$.ival + ": Falta el parametro de la funcion");}
                            ;

cuerpo_funcion              :   cuerpo_funcion sentencia_ejecutable_en_funcion
                            |   cuerpo_funcion sentencia_declarativa
                            |   sentencia_ejecutable_en_funcion
                            |   sentencia_declarativa
                            ;

sentencia_ejecutable_en_funcion         :   sentencia_asignacion PUNTO_Y_COMA { $$.ival = $1.ival; Parser.agregarEstructuraDetectadas($1.ival, "ASIGNACION"); }
                                        |   sentencia_asignacion error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia"); }
                                        |   sentencia_asignacion error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia"); }
                                        |   sentencia_seleccion_en_funcion
                                        |   sentencia_salida
                                        |   sentencia_control_en_funcion { Parser.agregarEstructuraDetectadas($1.ival, "FOR"); }
                                        |   sentencia_retorno { Parser.agregarEstructuraDetectadas($1.ival, "RET"); returnEncontrado = true; }
                                        ;

sentencia_control_en_funcion           :   encabezado_for bloque_de_sent_ejecutables_en_funcion { $$.ival = $1.ival; }
                                       |   encabezado_for error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el cuerpo del FOR"); }
                                       ;

sentencia_retorno           :   RET PARENTESIS_L expresion_aritmetica PARENTESIS_R PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); representacionPolaca.add(((Token) $1.obj).getLexema()); }
                            |   RET PARENTESIS_L expresion_aritmetica PARENTESIS_R error PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            |   RET PARENTESIS_L expresion_aritmetica PARENTESIS_R  error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            ;

sentencia_seleccion_en_funcion  :   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "IF"); }
                                |   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
                                |   IF PARENTESIS_L condicion PARENTESIS_R cuerpo_if_en_funcion END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
                                |   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia");}
                                |   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if_en_funcion PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
                                |   IF PARENTESIS_L condicion THEN cuerpo_if_en_funcion END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
                                |   IF condicion PARENTESIS_R THEN cuerpo_if_en_funcion END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
                                |   IF condicion THEN cuerpo_if_en_funcion END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
                                |   IF PARENTESIS_L condicion PARENTESIS_R THEN END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
                                ;

cuerpo_if_en_funcion        :   bloque_de_sent_ejecutables_en_funcion bloque_else_en_funcion
		                    |   bloque_de_sent_ejecutables_en_funcion
		                    ;

bloque_else_en_funcion      :   ELSE bloque_de_sent_ejecutables_en_funcion
                            |   ELSE { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
                            ;

bloque_de_sent_ejecutables_en_funcion   :   BEGIN sentencias_ejecutable_en_funcion END PUNTO_Y_COMA
                                        |   BEGIN sentencias_ejecutable_en_funcion END error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $3.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                                        |   BEGIN sentencias_ejecutable_en_funcion END error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $3.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                                        |   sentencia_ejecutable_en_funcion
                                        ;

sentencias_ejecutable_en_funcion        :   sentencias_ejecutable_en_funcion sentencia_ejecutable_en_funcion
                                        |   sentencia_ejecutable_en_funcion
                                        ;

sentencia_ejecutable        :   sentencia_asignacion PUNTO_Y_COMA { $$.ival = $1.ival; Parser.agregarEstructuraDetectadas($1.ival, "ASIGNACION"); }
                            |   sentencia_asignacion error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia"); }
                            |   sentencia_asignacion error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia"); }
		                    |   sentencia_seleccion
		                    |   sentencia_salida
		                    |   sentencia_control
		                    ;

sentencia_asignacion        :   lista_de_identificadores ASIGNACION lista_de_expresiones { $$.ival = $1.ival; representacionPolaca.add(((Token) $2.obj).getLexema());cantidadIdEnListaId = 0; listaIdentificadores.clear();}
                            ;

sentencia_seleccion         :   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "IF"); }
                            |   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia de selección");}
                            |   IF PARENTESIS_L condicion PARENTESIS_R cuerpo_if END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta la palabra reservada THEN en la sentencia de selección");}
                            |   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if END_IF error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            |   IF PARENTESIS_L condicion PARENTESIS_R THEN cuerpo_if PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el END_IF en la sentencia de selección"); }
                            |   IF PARENTESIS_L condicion THEN cuerpo_if END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
                            |   IF condicion PARENTESIS_R THEN cuerpo_if END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
                            |   IF condicion THEN cuerpo_if END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
                            |   IF PARENTESIS_L condicion PARENTESIS_R THEN END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el cuerpo de la sentencia de seleccion"); }
                            ;

cuerpo_if                   :   bloque_de_sent_ejecutables bloque_else
		                    |   bloque_de_sent_ejecutables
		                    ;

bloque_else                 :   ELSE bloque_de_sent_ejecutables
                            |   ELSE { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
                            ;

condicion                   :   expresion_aritmetica comparador expresion_aritmetica { representacionPolaca.add(((Token) $2.obj).getLexema()); }
                            |   expresion_aritmetica error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
                            ;

comparador                  :   MAYOR { $$.obj = ((Token) $1.obj); }
                            |   MENOR { $$.obj = ((Token) $1.obj); }
                            |   MAYOR_O_IGUAL { $$.obj = ((Token) $1.obj); }
                            |   MENOR_O_IGUAL { $$.obj = ((Token) $1.obj); }
                            |   IGUAL { $$.obj = ((Token) $1.obj); }
                            |   DESIGUAL { $$.obj = ((Token) $1.obj); }
                            ;

bloque_de_sent_ejecutables  :   BEGIN sentencias_ejecutables END PUNTO_Y_COMA
                            |   BEGIN sentencias_ejecutables END error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $3.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            |   BEGIN END PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
			                |   BEGIN sentencias_ejecutables END error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $3.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
			                |   sentencia_ejecutable
			                ;

sentencias_ejecutables      :   sentencias_ejecutables sentencia_ejecutable
		                    |   sentencia_ejecutable
		                    ;

sentencia_salida            :   OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R PUNTO_Y_COMA{ $$.ival = ((Token) $1.obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) $3.obj).getLexema()); representacionPolaca.add(((Token) $1.obj).getLexema());}
		                    |   OUTF PARENTESIS_L expresion_aritmetica PARENTESIS_R PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) $1.obj).getLexema());}
		                    |   OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R error PUNTO_Y_COMA{ $$.ival = ((Token) $1.obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            |   OUTF PARENTESIS_L expresion_aritmetica PARENTESIS_R error PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
		                    |   OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R error END{ $$.ival = ((Token) $1.obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            |   OUTF PARENTESIS_L expresion_aritmetica PARENTESIS_R error END { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
		                    |   OUTF PARENTESIS_L PARENTESIS_R PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
		                    |   OUTF PARENTESIS_L error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
		                    ;

sentencia_control           :   encabezado_for bloque_de_sent_ejecutables { Parser.agregarEstructuraDetectadas($1.ival, "FOR"); }
                            |   encabezado_for error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el cuerpo del FOR"); }
                            ;

encabezado_for              :   encabezado_for_obligatorio PUNTO_Y_COMA PARENTESIS_L condicion PARENTESIS_R PARENTESIS_R { $$.ival = $1.ival; }
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

asignacion_enteros          :   identificador ASIGNACION constante_entera { representacionPolaca.add(((Token) $2.obj).getLexema()); }
                            ;

accion                      :   UP constante_entera { representacionPolaca.add(((Token) $1.obj).getLexema()); }
                            |   DOWN constante_entera { representacionPolaca.add(((Token) $1.obj).getLexema()); }
                            |   constante_entera { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
                            ;

lista_de_expresiones        :   lista_de_expresiones COMA expresion_aritmetica
		                    |   expresion_aritmetica
		                    ;

expresion_aritmetica        :   expresion_aritmetica SUMA termino { representacionPolaca.add(((Token) $2.obj).getLexema()); $$.sval = $1.sval;}
		                    |   expresion_aritmetica RESTA termino { representacionPolaca.add(((Token) $2.obj).getLexema()); $$.sval = $1.sval;}
		                    |   termino { $$.sval = $1.sval; }
		                    |   error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": Falta operador, operandos, o coma entre expresiones"); }
		                    ;

termino                     :   termino MULTIPLICACION factor { representacionPolaca.add(((Token) $2.obj).getLexema()); $$.sval = $1.sval;}
		                    |   termino DIVISION factor { representacionPolaca.add(((Token) $2.obj).getLexema()); $$.sval = $1.sval;}
		                    |   factor { $$.sval = $1.sval; }
		                    ;

factor                      :   identificador { $$.sval = TablaSimbolos.getTipo($1.sval); System.out.println("AAAAAAAAAAAAAAAAA TIPO:" + TablaSimbolos.getTipo($1.sval)); }
                            |   constante { $$.sval = TablaSimbolos.getTipo(((Token) $1.obj).getLexema()); }
                            |   TOS PARENTESIS_L expresion_aritmetica PARENTESIS_R {$$.ival = ((Token) $1.obj).getNumeroDeLinea();  $$.sval = "single"; }
                            |   TOS PARENTESIS_L PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta la expresión"); }
		                    |   invocacion_a_funcion { $$.sval = $1.sval; }
		                    ;

constante_entera            :   CONSTANTE_DECIMAL { $$.obj = ((Token) $1.obj); representacionPolaca.add(((Token) $1.obj).getLexema());}
                            |   CONSTANTE_OCTAL { $$.obj = ((Token) $1.obj); representacionPolaca.add(((Token) $1.obj).getLexema());}
                            ;

constante                   :   constante_entera { $$.obj = ((Token) $1.obj);}
                            |   RESTA constante_entera { $$.obj = ((Token) $1.obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea()  + ": la constante se va de rango. No se permiten constantes enteras negativas."); }
                            |   CONSTANTE_SINGLE { $$.obj = ((Token) $1.obj); representacionPolaca.add(((Token) $1.obj).getLexema());}
                            |   TOKERROR { $$.obj = ((Token) $1.obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
                            |   RESTA CONSTANTE_SINGLE {
                                                            $$.obj = ((Token) $1.obj);
                                                            String lexema = ((Token) $2.obj).getLexema();
                                                            int cantidadDeUsos = TablaSimbolos.getCantidadDeUsos(lexema);

                                                            if (cantidadDeUsos > 1) {
                                                                // Bajar la cantidad de usos en 1
                                                                TablaSimbolos.decrementarUso(lexema);
                                                            } else {
                                                                // Eliminar la entrada
                                                                TablaSimbolos.eliminarLexema(lexema);
                                                            }

                                                            String lexemaNegativo = "-" + lexema;

                                                            float numero = Float.parseFloat(lexemaNegativo.replace('s','e'));
                                                            if (numero == Float.POSITIVE_INFINITY || numero == Float.NEGATIVE_INFINITY || numero == -0.0f) {
                                                                System.out.println(numero);
                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea " + ((Token) $2.obj).getNumeroDeLinea() + ": la constante se va de rango");
                                                            } else {
                                                                // Hay que fijarse si ya esta la negativa en la tabla, sino agregarla como negativa.
                                                                if (TablaSimbolos.existeLexema(lexemaNegativo)) {
                                                                    TablaSimbolos.aumentarUso(lexemaNegativo);
                                                                } else {
                                                                    TablaSimbolos.agregarLexema(lexemaNegativo, new CampoTablaSimbolos(false, TablaSimbolos.SINGLE));
                                                                    TablaSimbolos.aumentarUso(lexemaNegativo);
                                                                }
                                                                representacionPolaca.add("-"+ ((Token) $2.obj).getLexema());
                                                            }
                                                        }
                            |   RESTA TOKERROR { $$.obj = ((Token) $1.obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
                            ;

invocacion_a_funcion        :   IDENTIFICADOR_FUN PARENTESIS_L expresion_aritmetica PARENTESIS_R {
                                                                                                    $$.sval = TablaSimbolos.getTipoRetorno(((Token) $1.obj).getLexema());
                                                                                                    if (!TablaSimbolos.getTipoRetorno(((Token) $1.obj).getLexema()).equals($3.sval)) {
                                                                                                        agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea()  + ": Parametro real: " + $3.sval +". Parametro formal: " + TablaSimbolos.getTipoRetorno(((Token) $1.obj).getLexema()) + ".");
                                                                                                    }
                                                                                                    representacionPolaca.add(((Token) $1.obj).getLexema()); representacionPolaca.add("jmp");

                                                                                                }
                            |   IDENTIFICADOR_FUN PARENTESIS_L  PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
                            |   IDENTIFICADOR_FUN PARENTESIS_L expresion_aritmetica error PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
                            ;

%%
private static int cantidadIdEnListaId = 0;
private static Lexer lex;
private static ArrayList<String> representacionPolaca;

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

// Lista de tokens recibidos
public static final List<Token> tokensRecibidos = new ArrayList<>();

// Lista de identificadores
public static final List<String> listaIdentificadores = new ArrayList<>();

// Lista de estructuras detectadas
public static final List<String> estructurasDetectadas = new ArrayList<>();

// Listas de errores
public static final List<String> erroresLexicos = new ArrayList<>();
public static final List<String> erroresSintacticos = new ArrayList<>();

// Agregar tipo a la tabla de simbolos para los identificadores que estan en la lista de identificadores
public static void agregarTipoAIdentificadores(String tipo) {
    for (String lexema: listaIdentificadores) {
        System.out.println(lexema + " es tipo " + tipo);
        TablaSimbolos.cambiarTipo(lexema, tipo);
    }
}

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

public static <T> void imprimirLista(List<T> lista) {
    for (T elemento : lista) {
        System.out.println(elemento);
    }
}

public static void imprimirPolaca(List<String> lista) {
    if (!lista.isEmpty()) {
    int i = 0;
    for (String elemento : lista) {
        System.out.println(i + " " + elemento);
        i++;
    }
    }
}

public static <T> void eliminarUltimosElementos(List<T> lista, int cantidadElementos){
    for (int i = 0; i < cantidadElementos; i++) {
        lista.remove(lista.size() - 1);
    }
}

public static <T> void appendListToList(List<T> lista1, List<T> lista2) {
}

public static void main(String[] args) {
    representacionPolaca = new ArrayList<String>();
    Lexer lexer = new Lexer(args[0]);
    Parser.lex = lexer;
    Parser parser = new Parser(true);
    parser.run();

    System.out.println(Parser.VERDE + "TOKENS RECIBIDOS DEL ANALIZADOR LEXICO" + Parser.RESET);
    Parser.imprimirLista(tokensRecibidos);

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

    imprimirPolaca(representacionPolaca);
}

private int yylex() {
    Token tok = lex.getNextToken();
    tokensRecibidos.add(tok);

    if (tok.isError()) {
        agregarError(erroresLexicos, ERROR_LEXICO, ((TokenError) tok).getDescripcionError());
    }

    yylval = new ParserVal(tok);
    return tok.getTokenID();
}

private void yyerror(String string) {
  parsingConErrores = true;
  System.out.println("Error: " + string );
}
