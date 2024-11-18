%{
    import compilador.lexer.Lexer;
    import compilador.lexer.token.*;
    import compilador.lexer.TablaSimbolos;
    import compilador.lexer.CampoTablaSimbolos;
    import compilador.lexer.TablaToken;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;
    import java.util.Stack;
    import compilador.generadorCodigo.GeneradorAssembler;
%}

%token IDENTIFICADOR_GENERICO IDENTIFICADOR_FUN IDENTIFICADOR_TIPO IDENTIFICADOR_ULONGINT IDENTIFICADOR_SINGLE CONSTANTE_DECIMAL CONSTANTE_OCTAL CONSTANTE_SINGLE SUMA RESTA MULTIPLICACION DIVISION ASIGNACION MAYOR_O_IGUAL MENOR_O_IGUAL MAYOR MENOR IGUAL DESIGUAL CORCHETE_L CORCHETE_R PARENTESIS_L PARENTESIS_R COMA PUNTO PUNTO_Y_COMA INLINE_STRING TOKERROR STRUCT FOR UP DOWN SINGLE ULONGINT IF THEN ELSE BEGIN END END_IF OUTF TYPEDEF FUN RET TOS

%left SUMA RESTA
%left MULTIPLICACION DIVISION

%%

programa                    :   identificador_simple BEGIN sentencias END { Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "PROGRAMA"); agregarUsoAIdentificador(((Token) $1.obj).getLexema(), "nombre de programa");}
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

sentencia_declarativa       :   tipo lista_de_identificadores PUNTO_Y_COMA  {
                                                                                System.out.println(getAmbitoActual());
                                                                                Parser.agregarEstructuraDetectadas($1.ival, "VARIABLE/S");
                                                                                eliminarUltimosElementos(representacionPolaca, listaIdentificadores.size());
                                                                                agregarTipoAIdentificadores($1.sval);
                                                                                agregarAmbitoAIdentificadores(listaIdentificadores);
                                                                                agregarUsoAIdentificadores(listaIdentificadores, "nombre de variable");

                                                                                for (int i = 0; i < listaIdentificadores.size(); i++) {
                                                                                   System.out.println((TablaSimbolos.existeLexema(listaIdentificadores.get(i) + getAmbitoActual())));
                                                                                   if ((TablaSimbolos.existeLexema(listaIdentificadores.get(i) + getAmbitoActual())) || listaIdentificadores.get(i).charAt(0) == 'x' || listaIdentificadores.get(i).charAt(0) == 'y' || listaIdentificadores.get(i).charAt(0) == 'z' || listaIdentificadores.get(i).charAt(0) == 's'){
                                                                                        agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ $1.ival + ": Variable " + listaIdentificadores.get(i) +" ya declarada en el mismo ambito");
                                                                                   } else {
                                                                                        TablaSimbolos.cambiarLexema(listaIdentificadores.get(i), listaIdentificadores.get(i) + getAmbitoActual());
                                                                                        if (TablaSimbolos.esUnTipo($1.sval) && TablaSimbolos.getTipo($1.sval).equals("STRUCT")) {
                                                                                            for(String identificador: listaIdentificadores) {
                                                                                                crearCampo($1.sval, identificador);
                                                                                            }
                                                                                        };
                                                                                     };
                                                                                };
                                                                                listaIdentificadores.clear();
                                                                            }
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
                                                                                                                                                    eliminarUltimosElementos(representacionPolaca, listaIdentificadores.size());
                                                                                                                                                    agregarUsoAIdentificador(lexema, "nombre de struct");
                                                                                                                                                    listaIdentificadores.forEach((subcampo)->TablaSimbolos.deleteEntrada(subcampo));
                                                                                                                                                    agregarAmbitoAIdentificador(lexema);
                                                                                                                                                    TablaSimbolos.agregarCampos(lexema, listaTipos, listaIdentificadores);
                                                                                                                                                    listaIdentificadores.clear();
                                                                                                                                                    listaTipos.clear();
                                                                                                                                                    //TablaSimbolos.cambiarLexema(lexema, lexema + getAmbitoActual());
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
		                    |   SINGLE { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); $$.sval = ((Token) $1.obj).getLexema().toUpperCase(); }
		                    |   IDENTIFICADOR_TIPO { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); $$.sval = ((Token) $1.obj).getLexema(); }
		                    ;

lista_de_tipos              :   lista_de_tipos COMA tipo { listaTipos.add($3.sval); }
		                    |   tipo { listaTipos.add($1.sval); }
		                    ;

lista_de_identificadores    :   lista_de_identificadores COMA identificador { listaIdentificadores.add($3.sval);}
                            |   lista_de_identificadores identificador { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ',' entre las identificadores"); }
		                    |   identificador { $$.ival = $1.ival; listaIdentificadores.add($1.sval);}
		                    ;

identificador               :   identificador_simple {
                                                        $$.ival = ((Token) $1.obj).getNumeroDeLinea();
                                                        $$.sval = ((Token) $1.obj).getLexema();
                                                        representacionPolaca.add(((Token) $1.obj).getLexema());
                                                        String idActual = ((Token) $1.obj).getLexema();
                                                        String idActualConAmbito = idActual + getAmbitoActual();
                                                        if (TablaSimbolos.existeLexema(idActualConAmbito)){
                                                           TablaSimbolos.aumentarUso(idActualConAmbito);
                                                           TablaSimbolos.deleteEntrada(idActual);
                                                        };
                                                     }
                            |   identificador_compuesto { $$.ival = $1.ival; $$.sval = $1.sval; System.out.println("ID COMPUESTO: " + $1.sval); representacionPolaca.add($1.sval);
                                                         String idActual = $1.sval;
                                                         ArrayList<String> idSimples = new ArrayList<>(Arrays.asList(idActual.split("\\.")));
                                                         for (String idSimple : idSimples) {
                                                            if (TablaSimbolos.existeLexema(idSimple)){
                                                                TablaSimbolos.deleteEntrada(idSimple);
                                                            };}
                                                         String idActualConAmbito = idActual + getAmbitoActual();
                                                         if (TablaSimbolos.existeLexema(idActualConAmbito)){
                                                             TablaSimbolos.aumentarUso(idActualConAmbito);
                                                         };}
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
                                                                                ambito.pop();
                                                                                representacionPolaca.add("Fin " + aux.pop());
                                                                                representacionPolaca.set(bfs.pop(),String.valueOf(representacionPolaca.size()));
                                                                             }
                            ;

nombre_funcion              :   tipo FUN IDENTIFICADOR_GENERICO {
                                                                    $$.ival = $1.ival;
                                                                    String nombreFuncion = ((Token) $3.obj).getLexema();
                                                                    $$.sval = nombreFuncion;
                                                                    TablaSimbolos.cambiarTipo(nombreFuncion, TablaSimbolos.FUN);
                                                                    agregarAmbitoAIdentificador(nombreFuncion);
                                                                    agregarUsoAIdentificador(nombreFuncion, "nombre de funcion");
                                                                    TablaSimbolos.setTipoRetorno(nombreFuncion, $1.sval);
                                                                    String ambitoDeLaFuncion = getAmbitoActual();
                                                                    TablaSimbolos.cambiarLexema(nombreFuncion, nombreFuncion + ambitoDeLaFuncion);
                                                                    ambito.push(":" + nombreFuncion);
                                                                }
                            ;

encabezado_funcion          :   nombre_funcion PARENTESIS_L parametro PARENTESIS_R {
                                                                                        $$.ival = $1.ival;
                                                                                        String nombreFuncion = $1.sval;
                                                                                        String lexemaParametro = representacionPolaca.get(representacionPolaca.size()-1);
                                                                                        eliminarUltimosElementos(representacionPolaca, 1);
                                                                                        representacionPolaca.add("");
                                                                                        bfs.push(representacionPolaca.size()-1);
                                                                                        representacionPolaca.add("BI");
                                                                                        representacionPolaca.add(nombreFuncion);
                                                                                        aux.push(nombreFuncion);
                                                                                        String ambitoActual = getAmbitoActual();
                                                                                        TablaSimbolos.setTipoParametro(nombreFuncion + ambitoActual.substring(0, ambitoActual.lastIndexOf(':')), $3.sval);
                                                                                        TablaSimbolos.setCantidadDeParametros(nombreFuncion + ambitoActual.substring(0, ambitoActual.lastIndexOf(':')), 1);
                                                                                        agregarAmbitoAIdentificador(lexemaParametro);
                                                                                        TablaSimbolos.cambiarLexema(lexemaParametro, lexemaParametro + getAmbitoActual());
                                                                                    }
                            |   tipo FUN PARENTESIS_L parametro PARENTESIS_R {
                                                                                $$.ival = $1.ival;
                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el identificador de la funcion");
                                                                             }
                            ;

parametro                   :   tipo identificador { $$.sval = $1.sval; TablaSimbolos.imprimirTabla();TablaSimbolos.cambiarTipo($2.sval, $1.sval); agregarUsoAIdentificador($2.sval, "nombre de parametro"); }
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

sentencia_control_en_funcion           :   encabezado_for bloque_de_sent_ejecutables_en_funcion { $$.ival = $1.ival;
                                                                                                  Parser.agregarEstructuraDetectadas($1.ival, "FOR");
                                                                                                  representacionPolaca.add(aux.pop());
                                                                                                  representacionPolaca.add(aux.pop());
                                                                                                  representacionPolaca.set(bfs.pop(),String.valueOf(representacionPolaca.size()+2)); /* Se suma dos debido a los siguientes dos campos que se agregan en la polaca*/
                                                                                                  representacionPolaca.add(String.valueOf(bfs.pop()));
                                                                                                  representacionPolaca.add("BI");}
                                       |   encabezado_for error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el cuerpo del FOR"); }
                                       ;

sentencia_retorno           :   RET PARENTESIS_L expresion_aritmetica PARENTESIS_R PUNTO_Y_COMA { listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); $$.ival = ((Token) $1.obj).getNumeroDeLinea(); representacionPolaca.add(((Token) $1.obj).getLexema()); }
                            |   RET PARENTESIS_L expresion_aritmetica PARENTESIS_R error PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            |   RET PARENTESIS_L expresion_aritmetica PARENTESIS_R  error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            ;

sentencia_seleccion_en_funcion  :   inicio_seleccion cuerpo_if_en_funcion END_IF PUNTO_Y_COMA { $$.ival = $1.ival; Parser.agregarEstructuraDetectadas($1.ival, "IF"); representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size())); }
                                |   inicio_seleccion cuerpo_if_en_funcion END_IF error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia de selección");}
                                |   inicio_seleccion cuerpo_if_en_funcion END_IF error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia"); }
                                |   inicio_seleccion cuerpo_if_en_funcion PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el END_IF en la sentencia de selección"); }
                                |   IF PARENTESIS_L condicion cuerpo_if_en_funcion END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
                                |   IF condicion PARENTESIS_R cuerpo_if_en_funcion END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
                                |   IF condicion cuerpo_if_en_funcion END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
                                |   inicio_seleccion END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el cuerpo de la sentencia de seleccion"); }
                                ;

cuerpo_if_en_funcion        :   THEN cuerpo_then_en_funcion cuerpo_else_en_funcion
                            |   THEN cuerpo_then_en_funcion
                            |   cuerpo_then_en_funcion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta THEN en el cuerpo de la selección"); }
                            |   cuerpo_then_en_funcion cuerpo_else_en_funcion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta THEN en el cuerpo de la selección"); }
                            ;

cuerpo_then_en_funcion      : bloque_de_sent_ejecutables_en_funcion {$$.ival = $1.ival;
                                                        representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size()+2));
                                                        representacionPolaca.add("");bfs.push(representacionPolaca.size()-1);
                                                        representacionPolaca.add("BI"); }
                            ;

cuerpo_else_en_funcion      :   ELSE bloque_de_sent_ejecutables_en_funcion
                            |   bloque_de_sent_ejecutables_en_funcion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ELSE en el cuerpo de la selección"); }
                            |   ELSE { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
                            ;

bloque_de_sent_ejecutables_en_funcion   :   BEGIN sentencias_ejecutable_en_funcion END PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea();}
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

sentencia_asignacion        :   lista_de_identificadores ASIGNACION lista_de_expresiones {
                                                                                            $$.ival = $1.ival;
                                                                                            eliminarUltimosElementos(representacionPolaca, listaIdentificadores.size());
                                                                                            List<List<String>> expresiones = formatearLista(listaExpresiones);
                                                                                            System.out.println("listaIdentificadores    " + listaIdentificadores);
                                                                                            System.out.println("expresiones    " + expresiones);
                                                                                            if (listaIdentificadores.size() == expresiones.size()) {
                                                                                                for (int i = 0; i < listaIdentificadores.size(); i++){
                                                                                                    String identificador = listaIdentificadores.get(i);
                                                                                                    representacionPolaca.add(identificador);
                                                                                                    expresiones.get(i).forEach((elemento)->representacionPolaca.add(elemento));
                                                                                                    representacionPolaca.add(((Token) $2.obj).getLexema());

                                                                                                    // Si la variable empieza con x, y, z, s puede ser que este siendo declarada al usarse, hay que chequear y agregarla a la tabla de simbolos de ser necesario.
                                                                                                    char primerCaracter = identificador.charAt(0);
                                                                                                    if (primerCaracter == 'x' || primerCaracter == 'y' || primerCaracter == 'z' || primerCaracter == 's') {
                                                                                                        if (!TablaSimbolos.existeLexema(identificador + getAmbitoActual())) {
                                                                                                            TablaSimbolos.cambiarLexema(identificador, identificador + getAmbitoActual());
                                                                                                            agregarUsoAIdentificador(identificador + getAmbitoActual(), "nombre de variable");
                                                                                                        } else {
                                                                                                            TablaSimbolos.aumentarUso(identificador + getAmbitoActual());
                                                                                                        }

                                                                                                        TablaSimbolos.eliminarLexema(identificador);
                                                                                                    }
                                                                                                }
                                                                                            } else {
                                                                                                agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ ((Token) $2.obj).getNumeroDeLinea() + ": No coincide la cantidad de variables con la cantidad de valores a asignar.");
                                                                                            }

                                                                                            for (int i = 0; i < listaIdentificadores.size(); i++) {
                                                                                                String ambito = estaAlAlcance(listaIdentificadores.get(i));
                                                                                                String identificador = listaIdentificadores.get(i);
                                                                                                if (ambito == null) {
                                                                                                    agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ $1.ival + ": Variable " + identificador +" no declarada");
                                                                                                } else {
                                                                                                    TablaSimbolos.aumentarUso(identificador + ambito);
                                                                                                    TablaSimbolos.eliminarLexema(identificador);
                                                                                                }
                                                                                            }

                                                                                            listaExpresiones.clear();
                                                                                            listaIdentificadores.clear();
                                                                                            listaTipoExpresiones.clear();
                                                                                         }
                            ;

sentencia_seleccion         :   inicio_seleccion cuerpo_if END_IF PUNTO_Y_COMA { $$.ival = $1.ival; Parser.agregarEstructuraDetectadas($1.ival, "IF"); representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size())); }
                            |   inicio_seleccion cuerpo_if END_IF error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia de selección");}
                            |   inicio_seleccion cuerpo_if END_IF error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ';' al final de la sentencia"); }
                            |   inicio_seleccion cuerpo_if PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el END_IF en la sentencia de selección"); }
                            |   IF PARENTESIS_L condicion cuerpo_if END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis derecho en la condición"); }
                            |   IF condicion PARENTESIS_R cuerpo_if END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parentesis izquierdo en la condición"); }
                            |   IF condicion cuerpo_if END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Faltan ambos parentesis en la condición"); }
                            |   inicio_seleccion END_IF PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el cuerpo de la sentencia de seleccion"); }
                            ;

inicio_seleccion            :   IF PARENTESIS_L condicion PARENTESIS_R {$$.ival = ((Token) $1.obj).getNumeroDeLinea();representacionPolaca.add("");bfs.push(representacionPolaca.size()-1); representacionPolaca.add("BF");}
                            ;

cuerpo_if                   :   THEN cuerpo_then cuerpo_else
		                    |   THEN cuerpo_then
		                    |   cuerpo_then { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta THEN en el cuerpo de la selección"); }
		                    |   cuerpo_then cuerpo_else { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta THEN en el cuerpo de la selección"); }
		                    ;

cuerpo_then                 : bloque_de_sent_ejecutables {$$.ival = $1.ival;
                                                            representacionPolaca.set(bfs.pop(), String.valueOf(representacionPolaca.size()+2));
                                                            representacionPolaca.add("");bfs.push(representacionPolaca.size()-1);
                                                            representacionPolaca.add("BI"); }
                            ;

cuerpo_else                 :   ELSE bloque_de_sent_ejecutables
                            |   bloque_de_sent_ejecutables { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ELSE en el cuerpo de la selección"); }
                            |   ELSE { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el cuerpo deL ELSE de la sentencia de seleccion"); }
                            ;

condicion                   :   expresion_aritmetica comparador expresion_aritmetica { listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); representacionPolaca.add(((Token) $2.obj).getLexema()); }
                            |   expresion_aritmetica error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ lex.getNumeroDeLinea() + ": Falta comparador"); }
                            ;

comparador                  :   MAYOR { $$.obj = ((Token) $1.obj); }
                            |   MENOR { $$.obj = ((Token) $1.obj); }
                            |   MAYOR_O_IGUAL { $$.obj = ((Token) $1.obj); }
                            |   MENOR_O_IGUAL { $$.obj = ((Token) $1.obj); }
                            |   IGUAL { $$.obj = ((Token) $1.obj); }
                            |   DESIGUAL { $$.obj = ((Token) $1.obj); }
                            ;

bloque_de_sent_ejecutables  :   BEGIN sentencias_ejecutables END PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea();}
                            |   BEGIN sentencias_ejecutables END error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $3.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            |   BEGIN END PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Faltan sentencias ejecutables en el bloque"); }
			                |   BEGIN sentencias_ejecutables END error END { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $3.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
			                |   sentencia_ejecutable
			                ;

sentencias_ejecutables      :   sentencias_ejecutables sentencia_ejecutable { $$.ival = ((Token) $1.obj).getNumeroDeLinea();}
		                    |   sentencia_ejecutable
		                    ;

sentencia_salida            :   OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R PUNTO_Y_COMA{ $$.ival = ((Token) $1.obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) $3.obj).getLexema()); representacionPolaca.add(((Token) $1.obj).getLexema()); agregarUsoAIdentificador(((Token) $3.obj).getLexema(), "string");}
		                    |   OUTF PARENTESIS_L expresion_aritmetica PARENTESIS_R PUNTO_Y_COMA { listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); $$.ival = ((Token) $1.obj).getNumeroDeLinea(); Parser.agregarEstructuraDetectadas(((Token) $1.obj).getNumeroDeLinea(), "OUTF"); representacionPolaca.add(((Token) $1.obj).getLexema());}
		                    |   OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R error PUNTO_Y_COMA{ $$.ival = ((Token) $1.obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            |   OUTF PARENTESIS_L expresion_aritmetica PARENTESIS_R error PUNTO_Y_COMA { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
		                    |   OUTF PARENTESIS_L INLINE_STRING PARENTESIS_R error END{ $$.ival = ((Token) $1.obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
                            |   OUTF PARENTESIS_L expresion_aritmetica PARENTESIS_R error END { $$.ival = ((Token) $1.obj).getNumeroDeLinea(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta ';' al final de la sentencia"); }
		                    |   OUTF PARENTESIS_L PARENTESIS_R PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta el parametro de la sentencia de salida"); }
		                    |   OUTF PARENTESIS_L error PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Parametros incorrectos en la sentencia de salida"); }
		                    ;

sentencia_control           :   encabezado_for bloque_de_sent_ejecutables { Parser.agregarEstructuraDetectadas($1.ival, "FOR");
                                                                            representacionPolaca.add(aux.pop());
                                                                            representacionPolaca.add(aux.pop());
                                                                            representacionPolaca.set(bfs.pop(),String.valueOf(representacionPolaca.size()+2)); /* Se suma dos debido a los siguientes dos campos que se agregan en la polaca*/
                                                                            representacionPolaca.add(String.valueOf(bfs.pop()));
                                                                            representacionPolaca.add("BI");}
                            |   encabezado_for error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el cuerpo del FOR"); }
                            ;

encabezado_for              :   encabezado_for_obligatorio PUNTO_Y_COMA PARENTESIS_L condicion PARENTESIS_R PARENTESIS_R { $$.ival = $1.ival;
                                                                                                                           representacionPolaca.add("");
                                                                                                                           bfs.push(representacionPolaca.size()-1);
                                                                                                                           representacionPolaca.add("BF");}
                            |   encabezado_for_obligatorio PARENTESIS_R {$$.ival = $1.ival;
                                                                        representacionPolaca.add("");
                                                                        bfs.push(representacionPolaca.size()-1);
                                                                        representacionPolaca.add("BF");}
                            |   encabezado_for_obligatorio { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
                            |   encabezado_for_obligatorio PUNTO_Y_COMA PARENTESIS_L condicion PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
                            |   encabezado_for_obligatorio PARENTESIS_L condicion PARENTESIS_R PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta un ';' en el encabezado del FOR"); }
                            |   encabezado_for_obligatorio PUNTO_Y_COMA condicion PARENTESIS_R PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el parentesis derecho en la segunda condicion del encabezado del FOR"); }
                            |   encabezado_for_obligatorio PUNTO_Y_COMA PARENTESIS_L condicion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta ambos parentesis izquierdos del encabezado del FOR"); }
                            ;

encabezado_for_obligatorio  :   inicio_for PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion { $$.ival = $1.ival ;
                                                                                                                        representacionPolaca.remove(representacionPolaca.size()-1);} /* Se borra el ultimo elemento ya que se registra en la polaca la constante de la accion aunque se vaya a insertar luego  */
                            |   inicio_for asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA accion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta el parentesis derecho en el encabezado del FOR"); }
                            |   inicio_for PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion PUNTO_Y_COMA { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta la acción en el encabezado del FOR"); }
                            |   inicio_for PARENTESIS_L asignacion_enteros condicion PUNTO_Y_COMA accion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta un ';' en el encabezado del FOR"); }
                            |   inicio_for PARENTESIS_L asignacion_enteros PUNTO_Y_COMA condicion accion { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ $1.ival + ": Falta un ';' en el encabezado del FOR"); }
                            ;

inicio_for                  :   FOR {$$.ival = ((Token) $1.obj).getNumeroDeLinea(); bfs.push(representacionPolaca.size());}
                            ;

asignacion_enteros          :   identificador ASIGNACION constante_entera { representacionPolaca.add(((Token) $2.obj).getLexema());
                                                                            System.out.println(listaIdentificadores);
                                                                            // Si la variable empieza con x, y, z, s puede ser que este siendo declarada al usarse, hay que chequear y agregarla a la tabla de simbolos de ser necesario.
                                                                            String identificador = $1.sval;
                                                                            char primerCaracter = identificador.charAt(0);
                                                                            if (primerCaracter == 'x' || primerCaracter == 'y' || primerCaracter == 'z' || primerCaracter == 's') {
                                                                                if (!TablaSimbolos.existeLexema(identificador + getAmbitoActual())) {
                                                                                    TablaSimbolos.cambiarLexema(identificador, identificador + getAmbitoActual());
                                                                                    agregarUsoAIdentificador(identificador + getAmbitoActual(), "nombre de variable");
                                                                                } else {
                                                                                    TablaSimbolos.aumentarUso(identificador + getAmbitoActual());
                                                                                }

                                                                                TablaSimbolos.eliminarLexema(identificador);
                                                                            }
                                                                            }
                            ;

accion                      :   UP constante_entera { listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); aux.push("UP"); aux.push(((Token) $2.obj).getLexema()); }
                            |   DOWN constante_entera { listaExpresiones.forEach((n) -> representacionPolaca.add(n)); listaExpresiones.clear(); aux.push("DOWN"); aux.push(((Token) $2.obj).getLexema()); }
                            |   constante_entera { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta palabra reservada en la acción del encabezado FOR"); }
                            ;

lista_de_expresiones        :   lista_de_expresiones COMA expresion_aritmetica { listaExpresiones.add(((Token) $2.obj).getLexema()); listaTipoExpresiones.add($3.sval); }
		                    |   expresion_aritmetica { listaExpresiones.add(","); listaTipoExpresiones.add($1.sval); }
		                    ;

expresion_aritmetica        :   expresion_aritmetica SUMA termino { listaExpresiones.add(((Token) $2.obj).getLexema()); $$.sval = $1.sval;  if (!($1.sval).equals($3.sval)){agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": incompatibilidad de tipos. " + $1.sval + ((Token) $2.obj).getLexema() +$3.sval);};}
		                    |   expresion_aritmetica RESTA termino { listaExpresiones.add(((Token) $2.obj).getLexema()); $$.sval = $1.sval;  if (!($1.sval).equals($3.sval)){agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": incompatibilidad de tipos. " + $1.sval + ((Token) $2.obj).getLexema() +$3.sval);};}
		                    |   termino { $$.sval = $1.sval; }
		                    |   error { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": Falta operador, operandos, o coma entre expresiones"); }
		                    ;

termino                     :   termino MULTIPLICACION factor { listaExpresiones.add(((Token) $2.obj).getLexema()); $$.sval = $1.sval;  if (!($1.sval).equals($3.sval)){agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": incompatibilidad de tipos. " + $1.sval + ((Token) $2.obj).getLexema() +$3.sval);};}
		                    |   termino DIVISION factor { listaExpresiones.add(((Token) $2.obj).getLexema()); $$.sval = $1.sval;  if (!($1.sval).equals($3.sval)){agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ Parser.lex.getNumeroDeLinea() + ": incompatibilidad de tipos. " + $1.sval + ((Token) $2.obj).getLexema() +$3.sval);};}
		                    |   factor { $$.sval = $1.sval; }
		                    ;

factor                      :   identificador { String ambitoEncontrado = estaAlAlcance($1.sval);
                                                if (ambitoEncontrado != null) {
                                                    $$.sval = TablaSimbolos.getTipo($1.sval + ambitoEncontrado);
                                                } else {
                                                    agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea " + $1.ival + ": Variable " + $1.sval + " no declarada");
                                                    $$.sval = null;  // O cualquier valor predeterminado que necesites
                                                };
                                                listaExpresiones.add($1.sval);
                                                }
                            |   constante { $$.sval = TablaSimbolos.getTipo($1.sval); agregarUsoAIdentificador($1.sval, "constante");}
                            |   TOS PARENTESIS_L expresion_aritmetica PARENTESIS_R {$$.ival = ((Token) $1.obj).getNumeroDeLinea();  $$.sval = "single"; }
                            |   TOS PARENTESIS_L PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Falta la expresión"); }
		                    |   invocacion_a_funcion { $$.sval = $1.sval; }
		                    ;

constante_entera            :   CONSTANTE_DECIMAL { $$.sval = ((Token) $1.obj).getLexema(); listaExpresiones.add(((Token) $1.obj).getLexema());}
                            |   CONSTANTE_OCTAL { $$.sval = ((Token) $1.obj).getLexema(); listaExpresiones.add(((Token) $1.obj).getLexema());}
                            ;

constante                   :   constante_entera { $$.sval = $1.sval;}
                            |   RESTA constante_entera { $$.sval = ((Token) $1.obj).getLexema() + $2.sval; agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea()  + ": la constante se va de rango. No se permiten constantes enteras negativas."); }
                            |   CONSTANTE_SINGLE { $$.sval = ((Token) $1.obj).getLexema(); listaExpresiones.add(((Token) $1.obj).getLexema());}
                            |   TOKERROR { $$.sval = ((Token) $1.obj).getLexema(); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
                            |   RESTA CONSTANTE_SINGLE {
                                                            System.out.println(((Token) $1.obj).getLexema() + ((Token) $2.obj).getLexema());
                                                            $$.sval = ((Token) $1.obj).getLexema() + ((Token) $2.obj).getLexema();
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
                                                                listaExpresiones.add("-"+ ((Token) $2.obj).getLexema());
                                                            }
                                                        }
                            |   RESTA TOKERROR { $$.obj = ((Token) $1.obj); agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea()  + ": Posible constante fuera de rango (ERROR LEXICO)"); }
                            ;

invocacion_a_funcion        :   IDENTIFICADOR_GENERICO PARENTESIS_L expresion_aritmetica PARENTESIS_R {
                                                                                                        String lexemaFuncion = ((Token) $1.obj).getLexema();
                                                                                                        TablaSimbolos.eliminarLexema(lexemaFuncion);
                                                                                                        String ambitoFuncion = estaAlAlcance(lexemaFuncion);
                                                                                                        if (ambitoFuncion != null) {
                                                                                                            String lexemaFuncionConAmbito = lexemaFuncion + ambitoFuncion;
                                                                                                            String tipoParametroFormal = TablaSimbolos.getTipoRetorno(lexemaFuncionConAmbito);
                                                                                                            $$.sval = tipoParametroFormal;
                                                                                                            String tipoParametroReal = $3.sval;
                                                                                                            if (!tipoParametroFormal.equals(tipoParametroReal)) {
                                                                                                                agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea()  + ": Parametro real: " + tipoParametroReal +". Parametro formal: " + tipoParametroFormal + ".");}
                                                                                                            listaExpresiones.add(lexemaFuncion);
                                                                                                            listaExpresiones.add("BI");
                                                                                                        } else {
                                                                                                            agregarError(erroresSemanticos, ERROR_SEMANTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Funcion " + lexemaFuncion +" no esta el alcance");
                                                                                                        }
                                                                                                       }
                            |   IDENTIFICADOR_GENERICO PARENTESIS_L  PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea()  + ": Falta el parametro en la invocación a la función"); }
                            |   IDENTIFICADOR_GENERICO PARENTESIS_L expresion_aritmetica error PARENTESIS_R { agregarError(erroresSintacticos, ERROR_SINTACTICO, "Linea "+ ((Token) $1.obj).getNumeroDeLinea() + ": Se excede la cantidad de parametros posibles"); }
                            ;

%%
private static int cantidadIdEnListaId = 0;
private static Lexer lex;
private static ArrayList<String> representacionPolaca;

// Pila de ambitos
public static final Stack<String> ambito = new Stack<>();

// Pila para bifurcaciones
public static final Stack<Integer> bfs = new Stack<>();

// Pila para almacenar acción del for
public static final Stack<String> aux = new Stack<>();

// Tipo de mensajes
public static final String ERROR_LEXICO = "ERROR_LEXICO";
public static final String ERROR_SINTACTICO = "ERROR_SINTACTICO";
public static final String ERROR_SEMANTICO = "ERROR_SEMANTICO";

// Código ANSI para texto rojo
public static final String ROJO = "\033[31m";
public static final String VERDE = "\033[32m";
public static final String RESET = "\033[0m";

// Flags
public static boolean parsingConErrores = false;
public static boolean lexingConErrores = false;
public static boolean codIntermedioConErrores = false;
private static boolean returnEncontrado = false;

// Lista de tokens recibidos
public static final List<Token> tokensRecibidos = new ArrayList<>();

// Lista de identificadores
public static final List<String> listaIdentificadores = new ArrayList<>();

// Lista de expresiones
public static final List<String> listaExpresiones = new ArrayList<>();

// Lista del tipo al que se evaluan expresiones
public static final List<String> listaTipoExpresiones = new ArrayList<>();
// Lista de tipos
public static final List<String> listaTipos = new ArrayList<>();

// Lista de estructuras detectadas
public static final List<String> estructurasDetectadas = new ArrayList<>();

// Listas de errores
public static final List<String> erroresLexicos = new ArrayList<>();
public static final List<String> erroresSintacticos = new ArrayList<>();
public static final List<String> erroresSemanticos = new ArrayList<>();

public static String getAmbitoActual() {
    StringBuilder ambitoActual = new StringBuilder();
    for (String elemento: ambito) {
        ambitoActual.append(elemento);
    }
    return ambitoActual.toString();
}

public static void agregarAmbitoAIdentificadores(List<String> identificadores) {
    String ambitoActual = getAmbitoActual();

    for (String lexema: identificadores) {
        TablaSimbolos.setAmbito(lexema, ambitoActual);
    }
}
public static void agregarAmbitoAIdentificador(String lexema) {
    String ambitoActual = getAmbitoActual();
    TablaSimbolos.setAmbito(lexema, ambitoActual);
}

public static void agregarUsoAIdentificador(String identificador, String uso) {
    TablaSimbolos.setUso(identificador, uso);
}

public static void agregarUsoAIdentificadores(List<String> identificadores, String uso) {
    for (String lexema: identificadores) {
        TablaSimbolos.setUso(lexema, uso);
    }
}

//Para hacer chequeos de ambitos
public static String estaAlAlcance(String lexema){
    String ambitoActual = getAmbitoActual();
    while (ambitoActual.length()> 1){
        String idAmb = lexema + ambitoActual;
        if (TablaSimbolos.existeLexema(idAmb)){
            return ambitoActual;
        }
        ambitoActual = ambitoActual.substring(0,ambitoActual.lastIndexOf(':'));
    }
    return null;
};

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
    } else if (tipo.equals(Parser.ERROR_SEMANTICO)){
        codIntermedioConErrores = true;
    }

    listaErrores.add(String.format("%-20s", tipo) + "| "+ error);
}

public static List<List<String>> formatearLista(List<String> lista) {
    String separador = ",";

    List<List<String>> sublists = new ArrayList<>();
    List<String> currentSublist = new ArrayList<>();

    for (String item : lista) {
        if (item.equals(separador)) {
            // Add the current sublist to the list of sublists if it's not empty
            if (!currentSublist.isEmpty()) {
                sublists.add(new ArrayList<>(currentSublist));
                currentSublist.clear();
            }
        } else {
            // Add item to the current sublist
            currentSublist.add(item);
        }
    }
    // Add the last sublist if it has elements
    if (!currentSublist.isEmpty()) {
        sublists.add(currentSublist);
    }
    return sublists;
}

// Agregar los campos de una variable struct a la tabla de simbolos
public static void crearCampo(String tipo, String lexema){
    List<CampoTablaSimbolos.Campo> campos = TablaSimbolos.getCamposTablaSimbolos(tipo);
    for (CampoTablaSimbolos.Campo campo: campos){
        if (TablaSimbolos.esUnTipo(campo.tipo())){
            crearCampo(campo.tipo(), lexema + "." + campo.nombre());
        } else {
            String nombreCampo = lexema + "." + campo.nombre() + getAmbitoActual();
            CampoTablaSimbolos nuevoCampo = new CampoTablaSimbolos(false, campo.tipo());
            TablaSimbolos.agregarLexema(nombreCampo, nuevoCampo);
            agregarAmbitoAIdentificador(nombreCampo);
            TablaSimbolos.setUso(nombreCampo, "nombre de variable");
        }
    }
}

public static <T> void imprimirLista(List<T> lista) {
    for (T elemento : lista) {
        System.out.println(elemento);
    }
}

public static void imprimirPila(Stack<Integer> pila) {
    if (pila.isEmpty()) {
                System.out.println("La pila está vacía.");
    } else {
        System.out.println("Contenido de la pila:");
        // Recorrer la pila e imprimir cada elemento
        for (Integer elemento : pila) {
            System.out.println(elemento);
        }
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
    ambito.push(":main");
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

    if (Parser.codIntermedioConErrores){
            System.out.println(Parser.ROJO + "SE ENCONTRARON ERRORES SEMANTICOS" + Parser.RESET);
            Parser.imprimirLista(erroresSemanticos);
        } else {
            System.out.println(Parser.VERDE + "NO SE ENCONTRARON ERRORES SEMANTICOS" + Parser.RESET);
        }

    Parser.imprimirLista(estructurasDetectadas);

    TablaSimbolos.imprimirTabla();

    imprimirPolaca(representacionPolaca);

    if (!lexingConErrores && !parsingConErrores && !codIntermedioConErrores) {
        GeneradorAssembler gen = new GeneradorAssembler(representacionPolaca, "out.asm");
        gen.generarCodigoAssembler();
    }
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
