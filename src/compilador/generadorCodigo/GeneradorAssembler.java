package compilador.generadorCodigo;

import compilador.lexer.CampoTablaSimbolos;
import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private String f64printVariable = "@f64printVariable";

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
            TablaSimbolos.imprimirTabla();
            writer.write(data.toString());
            writer.write(code.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR GENERANDO ASSEMBLER");
        }
    }

    private void generarData() throws IOException {
        data.append(".data\n");
        List<String> usosAAgregar = List.of("nombre de variable", "string", "constante");

        for (String usoAAgregar : usosAAgregar) {
            List<String> lexemas = TablaSimbolos.getEntradasPorUso(usoAAgregar);
            for (String lexema : lexemas) {
                String tipo = TablaSimbolos.getTipo(lexema);
                String uso = TablaSimbolos.getUso(lexema);
                String lexemaViejo = lexema;


                if (uso.equals("constante")) {
                    switch (tipo){
                        case TablaSimbolos.SINGLE:
                            data.append("\t").append(formatearLexemaSingle(lexema)).append(" REAL4 ").append(lexema.replace('s', 'e')).append("\n");
                            break;
                        case TablaSimbolos.ULONGINT:
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
                            data.append("\t").append(lexema).append(" db \"").append(lexemaViejo).append("\", 0\n");
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private String formatearLexemaSingle(String lexema) {
        return "_"+lexema.replace(".","f").replace("-", "m").replace("+", "");
    }


    private void generarCodigo() throws IOException {
        code.append(".code\n");
        code.append("start:\n");
        for (int i = 0; i < polaca.size(); i++) {
            procesarToken(polaca.get(i), i);
        }
        code.append("invoke ExitProcess, 0\n");
        code.append("end start");
    }

    private void procesarToken(String token, int indice){
        String ambito = estaAlAlcance(token);
        if (ambito != null){
            pila.push(token + ambito);
        } else {
            switch (token) {
                case "+":
                    suma();
                    break;
                case ":=":
                    asignacion();
                    break;
                case "-":
                    resta();
                    break;
                case "*":
                    multiplicacion();
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
                case "ret":
                    break;
                default:
                    if (token.startsWith("_L")){
                        String label = "Label" + token.substring(2) + ":";
                        code.append(label).append("\n");
                    } else {
                        pila.push(token);
                    }
                    break;
            }
        }
    }

    private void bifurcacionFalso() {
        System.out.println(pila);
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
            code.append("\tMOV EAX, ").append(formatearOperando(opDerecha)).append("\n");
            code.append("\tCMP EAX, EBX").append(formatearOperando(opIzquierda)).append(" ").append(formatearOperando(opDerecha)).append("\n");

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
            }
        }


        code.append("Label").append(direccionSalto).append("\n");
    }

    private void multiplicacion(){
        String op1 = pila.pop();
        String op2 = pila.pop();

        String tipoOperandoOP1 = TablaSimbolos.getTipo(op1);
        String usoOperandoOP1 = TablaSimbolos.getUso(op1);
        String usoOperandoOP2 = TablaSimbolos.getUso(op2);

        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP1.equals("constante")){
            op1 = formatearLexemaSingle(op1);
        } else {
            op1 = formatearOperando(op1);
        }
        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP2.equals("constante")){
            op2 = formatearLexemaSingle(op2);
        } else {
            op2 = formatearOperando(op2);
        }

        if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.SINGLE)){
            operacionMultiplicacionFlotante(op1, op2);
        } else if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.ULONGINT)) {
            operacionMultiplicacionEntera(op1, op2);
        }
    }

    private void suma(){
        String op1 = pila.pop();
        String op2 = pila.pop();

        String tipoOperandoOP1 = TablaSimbolos.getTipo(op1);
        String usoOperandoOP1 = TablaSimbolos.getUso(op1);
        String usoOperandoOP2 = TablaSimbolos.getUso(op2);

        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP1.equals("constante")){
            op1 = formatearLexemaSingle(op1);
        } else {
            op1 = formatearOperando(op1);
        }
        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP2.equals("constante")){
            op2 = formatearLexemaSingle(op2);
        } else {
            op2 = formatearOperando(op2);
        }

        if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.SINGLE)){
            operacionSumaFlotante(op1, op2);
        } else if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.ULONGINT)) {
            operacionSumaEntera(op1, op2);
        }
    }

    private void resta() {
        String op1 = pila.pop();
        String op2 = pila.pop();

        String tipoOperandoOP1 = TablaSimbolos.getTipo(op1);
        String usoOperandoOP1 = TablaSimbolos.getUso(op1);
        String usoOperandoOP2 = TablaSimbolos.getUso(op2);

        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP1.equals("constante")){
            op1 = formatearLexemaSingle(op1);
        } else {
            op1 = formatearOperando(op1);
        }
        if (tipoOperandoOP1.equals(TablaSimbolos.SINGLE) && usoOperandoOP2.equals("constante")){
            op2 = formatearLexemaSingle(op2);
        } else {
            op2 = formatearOperando(op2);
        }

        if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.SINGLE)){
            operacionRestaFlotante(op1, op2);
        } else if (tipoOperandoOP1 != null && tipoOperandoOP1.equals(TablaSimbolos.ULONGINT)) {
            operacionRestaFlotante(op1, op2);
        }
    }
    private void asignacion() {
        // Ver si es asignacion entera o flotante
        String valorAAsignar = pila.pop();
        String variableAsignada = pila.pop();

        String tipoVaribale = TablaSimbolos.getTipo(variableAsignada);

        variableAsignada = formatearOperando(variableAsignada);
        if (tipoVaribale.equals(TablaSimbolos.SINGLE) && TablaSimbolos.getUso(valorAAsignar).equals("constante")){
            valorAAsignar = formatearLexemaSingle(valorAAsignar);
        } else {
            valorAAsignar = formatearOperando(valorAAsignar);
        }

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
        code.append("\tMUL ").append(op2).append("\n");
        code.append("\tMOV ").append(auxLow).append(", EAX\n"); //Chequear si esta bien
        code.append("\tMOV ").append(auxHigh).append(", EDX\n");
        pila.push(auxLow);
    }
    private void operacionMultiplicacionFlotante(String op2, String op1){
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        code.append("\tFLD ").append(op1).append("\n");
        code.append("\tFMUL ").append(op2).append("\n");
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
    private void generarSalto(){

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
        if (tipo.equals(TablaSimbolos.SINGLE)){
            if (!TablaSimbolos.existeLexema(f64printVariable)){
                CampoTablaSimbolos campoVarAux = new CampoTablaSimbolos(false, "DOUBLE");
                campoVarAux.setUso("nombre de variable");
                TablaSimbolos.agregarLexema(f64printVariable, campoVarAux);
            }
            code.append("\tFLD ").append(op1).append("\n");
            code.append("\tFSTP ").append(f64printVariable).append("\n");
            code.append("\tinvoke printf, cfm$(\"%.20Lf\\n\"), ").append(f64printVariable).append("\n");
        } else if (tipo.equals(TablaSimbolos.ULONGINT)) {
            code.append("\tinvoke printf, cfm$(\"%u\\n\"), ").append(op1).append("\n");
        } else if (tipo.equals("INLINE_STRING")){
            code.append("\tinvoke printf, ADDR ").append(op1).append("\n");
        }
    }
    private String formatearOperando(String op) {
        String opFormateado = op;
        if (Character.isDigit(op.charAt(0)) || (op.charAt(0) == '-' && Character.isDigit(op.charAt(1)))) {
            opFormateado = formatearLexemaSingle(opFormateado);
        } else if ((opFormateado.charAt(0) != '@')){
            opFormateado= "_" + opFormateado;
            opFormateado = opFormateado.replace(':','_');
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
