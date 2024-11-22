package compilador.generadorCodigo;

import compilador.lexer.CampoTablaSimbolos;
import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GeneradorAssembler {
    private final ArrayList<String> polaca;
    private final BufferedWriter writer;
    private static Stack<String> pila = new Stack<>();
    private int contadorAux = 0;
    // Pila de ambitos
    private final Stack<String> ambito = new Stack<>();
    private final Stack<String> pilaBifurcaciones = new Stack<>();
    private StringBuilder code;
    private StringBuilder data;
    private final String f64printVariable = "@f64printVariable";

    private String getAmbitoActual() {
        StringBuilder ambitoActual = new StringBuilder();
        for (String elemento: ambito) {
            ambitoActual.append(elemento);
        }
        return ambitoActual.toString();
    }

    //Para hacer chequeos de ambitos
    private String estaAlAlcance(String lexema){
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

    public GeneradorAssembler(ArrayList<String> representacionPolaca, String rutaArchivo){
        this.polaca = representacionPolaca;
        this.ambito.push(":main");
        this.code = new StringBuilder();
        this.data = new StringBuilder();
        try {
            this.writer = new BufferedWriter(new FileWriter(rutaArchivo));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generarCodigoAssembler(){
        try {
            generarHeader();
            generarCodigo();
            generarData();
            writer.write(data.toString());
            writer.write(code.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR GENERANDO ASSEMBLER");
        }
    }

    private void generarData() throws IOException {
        data.append(".data\n");
        List<String> usosAAgregar = List.of("nombre de variable", "string", "constante", "nombre de parametro");

        for (String usoAAgregar : usosAAgregar) {
            List<String> lexemas = TablaSimbolos.getEntradasPorUso(usoAAgregar);
            for (String lexema : lexemas) {
                String tipo = TablaSimbolos.getTipo(lexema);
                String uso = TablaSimbolos.getUso(lexema);
                String lexemaViejo = lexema;


                if (uso.equals("constante")) {
                    switch (tipo){
                        case TablaSimbolos.SINGLE:
                            data.append("\t").append(formatearLexemaConstante(lexema)).append(" REAL4 ").append(lexema.replace('s', 'e')).append("\n");
                            break;
                        case TablaSimbolos.ULONGINT:
                            data.append("\t").append(formatearLexemaConstante(lexema)).append(" dd ").append(lexema).append("\n");
                            break;
                        default:
                            break;
                    }
                } else {
                    lexema = formatearOperando(lexema);
                    switch (tipo) {
                        case TablaSimbolos.SINGLE:
                            data.append("\t").append(lexema).append(" REAL4 0.0\n");
                            break;
                        case "DOUBLE":
                            data.append("\t").append(lexema).append(" REAL8 0.0\n");
                            break;
                        case TablaSimbolos.ULONGINT:
                            data.append("\t").append(lexema).append(" dd 0\n");
                            break;
                        case TablaToken.INLINE_STRING:
                            data.append("\t@").append(lexema.replace(' ', '_')).append(" db \"").append(lexemaViejo).append("\", 0\n");
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        data.append("\tfuncionActual dd 0\n");
        data.append("\terrorNegativoTxt db \"Error: La resta da un resultado menor que 0\", 0\n");
        data.append("\terrorOverflowTxt db \"Error: La multiplicacion se va de rango\", 0\n");
        data.append("\terrorRecursion db \"Error: No se permite la recursion\", 0\n");
    }

    private String formatearLexemaConstante(String lexema) {
        return "_"+lexema.replace(".","f").replace("-", "m").replace("+", "");
    }


    private void generarCodigo() throws IOException {
        CampoTablaSimbolos campoUlongInt = new CampoTablaSimbolos(false, TablaSimbolos.ULONGINT);
        campoUlongInt.setUso("nombre de variable");
        campoUlongInt.setAmbito(":main");
        TablaSimbolos.agregarLexema("@retValUlongint", campoUlongInt);

        CampoTablaSimbolos campoSingle = new CampoTablaSimbolos(false, TablaSimbolos.SINGLE);
        campoSingle.setUso("nombre de variable");
        campoSingle.setAmbito(":main");
        TablaSimbolos.agregarLexema("@retValSingle", campoSingle);

        code.append(".code\n");
        code.append("start:\n");
        for (int i = 0; i < polaca.size(); i++) {
            procesarToken(polaca.get(i), i);
        }
        agregarErrores();
        code.append("_quit:\n");
        code.append("\tinvoke ExitProcess, 0\n");
        code.append("end start");
    }

    private void agregarErrores() {
        code.append("\tJMP _quit\n");
        code.append("_errorNegativo:\n");
        code.append("\tinvoke printf, ADDR errorNegativoTxt \n");
        code.append("\tJMP _quit\n");
        code.append("_errorOverflow:\n");
        code.append("\tinvoke printf, ADDR errorOverflowTxt \n");
        code.append("\tJMP _quit\n");
        code.append("_errorRecursion:\n");
        code.append("\tinvoke printf, ADDR errorRecursion \n");
        code.append("\tJMP _quit\n");
    }

    private void procesarToken(String token, int indice){
        String ambitoDeLaVariable = estaAlAlcance(token);
        if (ambitoDeLaVariable != null) {
            pila.push(token + ambitoDeLaVariable);
        } else {
            switch (token) {
                case "+":
                    suma(pila.pop(), pila.pop());
                    break;
                case ":=":
                    String v1 = pila.pop();
                    String v2 = pila.pop();
                    asignacion(v1, v2);
                    break;
                case "-":
                    resta(pila.pop(), pila.pop());
                    break;
                case "/":
                    division(pila.pop(), pila.pop());
                    break;
                case "*":
                    multiplicacion(pila.pop(), pila.pop());
                    break;
                case "UP":
                    accionControl(token, pila.pop(), pila.pop());
                    break;
                case "DOWN":
                    accionControl(token, pila.pop(), pila.pop());
                    break;
                case "BI":
                    code.append("\tJMP Label").append(pila.pop()).append("\n");
                    break;
                case "BF":
                    bifurcacionFalso();
                    break;
                case "tos":
                    realizarConversion(pila.pop());
                    break;
                case "outf":
                    generarSalida(pila.pop());
                    break;
                case "CALL":
                    invocacionFuncion(pila.pop(), pila.pop());
                    break;
                case "ret":
                    retorno(pila.pop());
                    break;
                default:
                    if (token.startsWith("_")){
                        if (token.startsWith("_L")) {
                            // Etiqueta de saltos
                            String label = "Label" + token.substring(2) + ":";
                            code.append(label).append("\n");
                        } else if (token.startsWith("_fin")) {
                            String funcion = pila.pop();
                            ambito.pop();
                        } else if (token.startsWith("_inicio")) {
                            String funcion = pila.pop();
                            ambito.push(":" + funcion.substring(0, funcion.indexOf(":")));
                            // Agregar etiqueta del inicio de una funcion
                            code.append(funcion.replace(":", "_")).append(":\n");
                        }
                    } else {
                        pila.push(token);
                    }
                    break;
            }
        }
    }

    private void invocacionFuncion(String funcion, String parametroReal) {
        String variableAAsignar;
        String ultimoAmbito = ambito.peek().substring(1);
        if (ultimoAmbito.equals(funcion.substring(0, funcion.indexOf(":")))){
            variableAAsignar = TablaSimbolos.getNombreParametro(funcion) + getAmbitoActual();
        } else {
            variableAAsignar = TablaSimbolos.getNombreParametro(funcion) + getAmbitoActual() + ":" + funcion.substring(0, funcion.indexOf(":"));
        }
        // Cambiar el ambito al entrar a la funcion
        asignacion(parametroReal, variableAAsignar);

        // chequear que no se este entrando a la funcion en la que ya se esta
        code.append("\tCMP funcionActual, ").append(TablaSimbolos.getIdFuncion(funcion)).append("\n");
        code.append("\tJE _errorRecursion\n");

        // set en que funcion se esta entrando
        code.append("\tMOV funcionActual, ").append(TablaSimbolos.getIdFuncion(funcion)).append("\n");
        code.append("\tCALL ").append(funcion.replace(":", "_")).append("\n");
        if (ultimoAmbito.equals("main")) {
            code.append("\tMOV funcionActual, 0\n");
        } else {
            code.append("\tMOV funcionActual, ").append(TablaSimbolos.getIdFuncion(ultimoAmbito + estaAlAlcance(ultimoAmbito))).append("\n");
        }
        String tipoRetorno = TablaSimbolos.getTipoRetorno(funcion);
        if (tipoRetorno.equals(TablaSimbolos.SINGLE)) {
            pila.push("@retValSingle");
        } else if (tipoRetorno.equals(TablaSimbolos.ULONGINT)) {
            pila.push("@retValUlongint");
        }
    }

    private void retorno(String returnValue) {
        if (TablaSimbolos.getTipo(returnValue).equals(TablaSimbolos.SINGLE)) {
            asignacionFlotante(formatearOperando(returnValue), "@retValSingle");
        } else if (TablaSimbolos.getTipo(returnValue).equals(TablaSimbolos.ULONGINT)) {
            asignacionEntera(formatearOperando(returnValue), "@retValUlongint");
        }
        code.append("\tRET ").append(" \n");
    }

    private void accionControl(String token, String op1, String op2) {
        if (token.equals("UP")) {
            suma(op1, op2);
        } else if (token.equals("DOWN")) {
            resta(op1, op2);
        }

        asignacion(pila.pop(), op2);
    }

    private void bifurcacionFalso() {
        String direccionSalto = pila.pop();
        String operadorComparacion = pila.pop();
        String opDerecha = pila.pop();
        String opIzquierda = pila.pop();

        String tipoOperacion = TablaSimbolos.getTipo(opIzquierda);

        if (tipoOperacion.equals(TablaSimbolos.SINGLE)){
            code.append("\tFLD ").append(formatearOperando(opIzquierda)).append("\n");      // Cargar operando1 en ST(0)
            code.append("\tFLD ").append(formatearOperando(opDerecha)).append("\n");
            code.append("\tFCOMP\n");

            switch(operadorComparacion) {
                case "<":
                    code.append("\tJG ");
                    break;
                case "<=":
                    code.append("\tJG ");
                    break;
                case ">":
                    code.append("\tJLE ");
                    break;
                case ">=":
                    code.append("\tJL ");
                    break;
                case "!=":
                    code.append("\tJE ");
                    break;
            }
        } else if (tipoOperacion.equals(TablaSimbolos.ULONGINT)) {
            code.append("\tMOV EAX, ").append(formatearOperando(opIzquierda)).append("\n");
            code.append("\tMOV EBX, ").append(formatearOperando(opDerecha)).append("\n");
            code.append("\tCMP EAX, EBX\n");

            switch(operadorComparacion) {
                case "<":
                    code.append("\tJAE ");
                    break;
                case "<=":
                    code.append("\tJA ");
                    break;
                case ">":
                    code.append("\tJBE ");
                    break;
                case ">=":
                    code.append("\tJB ");
                    break;
                case "!=":
                    code.append("\tJE ");
                    break;
                case "=":
                    code.append("\tJNE ");
                    break;
            }
        }

        code.append("Label").append(direccionSalto).append("\n");
    }

    private void multiplicacion(String op1, String op2){
        String tipoOperandoOP1 = TablaSimbolos.getTipo(op1);
        String usoOperandoOP1 = TablaSimbolos.getUso(op1);
        String usoOperandoOP2 = TablaSimbolos.getUso(op2);

        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP1.equals("constante")){
            op1 = formatearLexemaConstante(op1);
        } else {
            op1 = formatearOperando(op1);
        }
        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP2.equals("constante")){
            op2 = formatearLexemaConstante(op2);
        } else {
            op2 = formatearOperando(op2);
        }

        if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.SINGLE)){
            operacionMultiplicacionFlotante(op1, op2);
        } else if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.ULONGINT)) {
            operacionMultiplicacionEntera(op1, op2);
        }
    }

    private void division(String op1, String op2) {
        String tipoOperandoOP1 = TablaSimbolos.getTipo(op1);
        String usoOperandoOP1 = TablaSimbolos.getUso(op1);
        String usoOperandoOP2 = TablaSimbolos.getUso(op2);

        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP1.equals("constante")){
            op1 = formatearLexemaConstante(op1);
        } else {
            op1 = formatearOperando(op1);
        }
        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP2.equals("constante")){
            op2 = formatearLexemaConstante(op2);
        } else {
            op2 = formatearOperando(op2);
        }

        if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.SINGLE)){
            operacionDivisionEntera(op1, op2);
        } else if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.ULONGINT)) {
            operacionDivisionFlotante(op1, op2);
        }
    }

    private void suma(String op1, String op2){
        String tipoOperandoOP1 = TablaSimbolos.getTipo(op1);
        String usoOperandoOP1 = TablaSimbolos.getUso(op1);
        String usoOperandoOP2 = TablaSimbolos.getUso(op2);

        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP1.equals("constante")){
            op1 = formatearLexemaConstante(op1);
        } else {
            op1 = formatearOperando(op1);
        }
        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP2.equals("constante")){
            op2 = formatearLexemaConstante(op2);
        } else {
            op2 = formatearOperando(op2);
        }

        if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.SINGLE)){
            operacionSumaFlotante(op1, op2);
        } else if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.ULONGINT)) {
            operacionSumaEntera(op1, op2);
        }
    }

    private void resta(String op1, String op2) {
        String tipoOperandoOP1 = TablaSimbolos.getTipo(op1);
        String usoOperandoOP1 = TablaSimbolos.getUso(op1);
        String usoOperandoOP2 = TablaSimbolos.getUso(op2);

        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP1.equals("constante")){
            op1 = formatearLexemaConstante(op1);
        } else {
            op1 = formatearOperando(op1);
        }
        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP2.equals("constante")){
            op2 = formatearLexemaConstante(op2);
        } else {
            op2 = formatearOperando(op2);
        }

        if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.SINGLE)){
            operacionRestaFlotante(op1, op2);
        } else if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.ULONGINT)) {
            operacionRestaEntera(op1, op2);
        }
    }
    private void asignacion(String valorAAsignar, String variableAsignada) {
        String tipoVaribale = TablaSimbolos.getTipo(variableAsignada);

        variableAsignada = formatearOperando(variableAsignada);
        valorAAsignar = formatearOperando(valorAAsignar);

        switch (tipoVaribale) {
            case TablaSimbolos.SINGLE:
                asignacionFlotante(valorAAsignar, variableAsignada);
                break;
            case TablaSimbolos.ULONGINT:
                asignacionEntera(valorAAsignar, variableAsignada);
                break;
            default:
                break;
        }
    }

    private void operacionSumaEntera(String op1, String op2){
        String aux = crearVariableAux(TablaSimbolos.ULONGINT);
        code.append("\tMOV EAX, ").append(op1).append("\n");
        code.append("\tADD EAX, ").append(op2).append("\n");
        code.append("\tMOV ").append(aux).append(", EAX\n");
        pila.push(aux);
    }
    private void operacionSumaFlotante(String op2, String op1){
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        code.append("\tFLD ").append(op1).append("\n");
        code.append("\tFADD ").append(op2).append("\n");
        code.append("\tFSTP ").append(aux).append("\n");
        pila.push(aux);
    }
    private void operacionRestaEntera(String op2, String op1){
        String aux = crearVariableAux(TablaSimbolos.ULONGINT);
        code.append("\tMOV EAX, ").append(op1).append("\n");
        code.append("\tSUB EAX, ").append(op2).append("\n");
        code.append("\tJC _errorNegativo\n");
        code.append("\tMOV ").append(aux).append(", EAX\n");
        pila.push(aux);
    }
    private void operacionRestaFlotante(String op2, String op1){
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        code.append("\tFLD ").append(op1).append("\n");
        code.append("\tFSUB ").append(op2).append("\n");
        code.append("\tFSTP ").append(aux).append("\n");
        pila.push(aux);
    }
    private void operacionMultiplicacionEntera(String op2, String op1){
        String auxLow = crearVariableAux(TablaSimbolos.ULONGINT);   // Parte baja (32 bits)
        String auxHigh = crearVariableAux(TablaSimbolos.ULONGINT); // Parte alta (32 bits)
        code.append("\tMOV EAX, ").append(op1).append("\n");
        code.append("\tMOV EBX, ").append(op2).append("\n");
        code.append("\tMUL EBX\n");
        code.append("\tMOV ").append(auxLow).append(", EAX\n"); //Chequear si esta bien
        code.append("\tMOV ").append(auxHigh).append(", EDX\n");
        pila.push(auxLow);
    }
    private void operacionMultiplicacionFlotante(String op2, String op1){
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        code.append("\tFLD ").append(op1).append("\n");
        code.append("\tFLD ").append(op2).append("\n");
        code.append("\tFMUL\n");
        code.append("\tFSTSW AX\n");
        code.append("\tTEST AX, 0800h\n");
        code.append("\tJNZ  _errorOverflow\n");
        code.append("\tFSTP ").append(aux).append("\n");
        pila.push(aux);
    }
    private void operacionDivisionEntera(String op2, String op1){
        String auxCociente = crearVariableAux(TablaSimbolos.ULONGINT);
        String auxResto = crearVariableAux(TablaSimbolos.ULONGINT); //Chequear si se utilizara
        code.append("\tMOV EAX, ").append(op1).append("\n");
        code.append("\tDIV ").append(op2).append("\n");
        code.append("\tMOV ").append(auxCociente).append(", EAX\n");
        code.append("\tMOV ").append(auxResto).append(", EDX\n");
        pila.push(auxCociente);
    }
    private void operacionDivisionFlotante(String op2, String op1){
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        code.append("\tFLD ").append(op1).append("\n");
        code.append("\tFDIV ").append(op2).append("\n");
        code.append("\tFSTP ").append(aux).append("\n");
        pila.push(aux);
    }
    private void asignacionEntera(String op2, String op1) {
        code.append("\tMOV EAX, ").append(op2).append("\n");
        code.append("\tMOV ").append(op1).append(",  EAX\n");
    }
    private void asignacionFlotante(String op2, String op1) {
        code.append("\tFLD ").append(op2).append("\n");
        code.append("\tFSTP ").append(op1).append("\n");
    }

    private void realizarConversion(String op1){
        if (TablaSimbolos.getTipo(op1).equals(TablaSimbolos.ULONGINT)){
            op1 = formatearOperando(op1);
            String aux = crearVariableAux(TablaSimbolos.SINGLE);
            code.append("\tFILD ").append(op1).append("\n");
            code.append("\tFSTP ").append(aux).append("\n");
            pila.push(aux);
        } else {
            System.out.println("La conversion debe ser de un tipo ULONGINT a un tipo SINGLE");
        }
    }

    private String crearVariableAux (String tipo){
        String varAux = "@aux" + (++contadorAux);
        CampoTablaSimbolos campoVarAux = new CampoTablaSimbolos(false, tipo);
        campoVarAux.setUso("nombre de variable");
        TablaSimbolos.agregarLexema(varAux, campoVarAux);//Agregar auxiliar a la tabla de simbolos
        return varAux;
    }
    private void generarSalida(String op1){
        String tipo = TablaSimbolos.getTipo(op1);
        op1 = formatearOperando(op1);
        switch (tipo) {
            case TablaSimbolos.SINGLE -> {
                if (!TablaSimbolos.existeLexema(f64printVariable)) {
                    CampoTablaSimbolos campoVarAux = new CampoTablaSimbolos(false, "DOUBLE");
                    campoVarAux.setUso("nombre de variable");
                    TablaSimbolos.agregarLexema(f64printVariable, campoVarAux);
                }
                code.append("\tFLD ").append(op1).append("\n");
                code.append("\tFSTP ").append(f64printVariable).append("\n");
                code.append("\tinvoke printf, cfm$(\"%.20Lf\\n\"), ").append(f64printVariable).append("\n");
            }
            case TablaSimbolos.ULONGINT -> code.append("\tinvoke printf, cfm$(\"%u\\n\"), ").append(op1).append("\n");
            case TablaToken.INLINE_STRING -> code.append("\tinvoke printf, ADDR @").append(op1.replace(' ', '_')).append("\n");
        }
    }
    private String formatearOperando(String op) {
        String opFormateado = op;

        if (opFormateado.charAt(0) != '@') {
            if (TablaSimbolos.getUso(op).equals("constante")) {
                opFormateado = formatearLexemaConstante(opFormateado);
            } else {
                opFormateado = "_" + opFormateado;
                opFormateado = opFormateado.replace(':', '_').replace('.','_');
            }
        }

        return opFormateado;
    }
    private void generarHeader() throws IOException{
        writer.write(".386\n");
        writer.write(".model flat, stdcall\n");
        writer.write(".STACK 200h\n");
        writer.write("option casemap :none\n");
        writer.write("include \\masm32\\include\\masm32rt.inc\n");
        writer.write("includelib \\masm32\\lib\\kernel32.lib\n");
        writer.write("includelib \\masm32\\lib\\masm32.lib\n");
        writer.write("dll_dllcrt0 PROTO C\n");
        writer.write("printf PROTO C : VARARG\n");
        //writer.write("    resultado DWORD ?\n");

    }
}
