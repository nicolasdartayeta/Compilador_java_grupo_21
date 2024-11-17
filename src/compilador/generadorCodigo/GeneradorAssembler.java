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
    private StringBuilder code;
    private StringBuilder data;

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
        List<String> usosAAgregar = List.of("nombre de variable");

        for (String uso : usosAAgregar) {
            List<String> lexemas = TablaSimbolos.getEntradasPorUso(uso);
            System.out.println(lexemas);
            for (String lexema : lexemas) {
                String tipo = TablaSimbolos.getTipo(lexema);
                lexema = formatearOperando(lexema);
                switch (tipo) {
                    case TablaSimbolos.SINGLE:
                        data.append("\t").append(lexema).append(" dw 0\n");
                        break;
                    case TablaSimbolos.ULONGINT:
                        data.append("\t").append(lexema).append(" dd 0\n");
                        break;
                    case TablaToken.INLINE_STRING:
                        data.append("\t").append(lexema).append(" db \"").append(lexema).append("\", 0\n");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void generarCodigo() throws IOException {
        code.append(".code\n");
        code.append("start:\n");
        for (String token: polaca){
            procesarToken(token);
        }
        code.append("invoke ExitProcess, 0\n");
        code.append("end start");
    }

    private void procesarToken(String token) throws  IOException{
        String ambito = estaAlAlcance(token);
        if (ambito != null){
            pila.push(token + ambito);
        } else {
            switch (token) {
                case "+":
                    //Chequear si la suma va a ser entera o flotante y llamar a funcion correspondiente
                    operacionSumaEntera(formatearOperando(pila.pop()), formatearOperando(pila.pop()));
                    break;
                case ":=":
                    asignacion();
                    break;
                case "-":
                    //Chequear si la suma va a ser entera o flotante y llamar a funcion correspondiente
                    operacionRestaEntera(formatearOperando(pila.pop()), formatearOperando(pila.pop()));
                    break;
                case "*":
                    operacionMultiplicacionEntera(formatearOperando(pila.pop()), formatearOperando(pila.pop()));
                    break;
                case "BI":
                    code.append("JMP ").append(formatearOperando(pila.pop())).append("\n");
                    break;
                case "BF":
                    break;
                case "tos":
                    realizarConversion(formatearOperando(pila.pop()));
                    break;
                case "outf":
                    break;
                default:
                    pila.push(token);
                    break;
            }
        }
    }

    private void asignacion() throws IOException {
        // Ver si es asignacion entera o flotante
        String valorAAsignar = pila.pop();
        String variableAsignada = pila.pop();

        String tipoVaribale = TablaSimbolos.getTipo(variableAsignada);

        valorAAsignar = formatearOperando(valorAAsignar);
        variableAsignada = formatearOperando(variableAsignada);

        if (tipoVaribale != null){
            switch (tipoVaribale){
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
    }

    private void operacionSumaEntera(String op1, String op2) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.ULONGINT);
        code.append("MOV EAX, ").append(op1).append("\n");
        code.append("ADD EAX, ").append(op2).append("\n");
        code.append("MOV ").append(aux).append(", EAX\n");
        pila.push(aux);
    }
    private void operacionSumaFlotante(String op2, String op1) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        code.append("FLD ").append(op1).append("\n");
        code.append("FADD ").append(op2).append("\n");
        code.append("FSTP ").append(aux).append("\n");
        pila.push(aux);
    }
    private void operacionRestaEntera(String op2, String op1) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.ULONGINT);
        code.append("MOV EAX, ").append(op1).append("\n");
        code.append("SUB EAX, ").append(op2).append("\n");
        code.append("MOV ").append(aux).append(", EAX\n");
        pila.push(aux);
    }
    private void operacionRestaFlotante(String op2, String op1) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        code.append("FLD ").append(op1).append("\n");
        code.append("FSUB ").append(op2).append("\n");
        code.append("FSTP ").append(aux).append("\n");
        pila.push(aux);
    }
    private void operacionMultiplicacionEntera(String op2, String op1) throws IOException{
        String auxLow = crearVariableAux(TablaSimbolos.ULONGINT);   // Parte baja (32 bits)
        String auxHigh = crearVariableAux(TablaSimbolos.ULONGINT); // Parte alta (32 bits)
        code.append("MOV EAX, ").append(op1).append("\n");
        code.append("MUL ").append(op2).append("\n");
        code.append("MOV ").append(auxLow).append(", EAX\n"); //Chequear si esta bien
        code.append("MOV ").append(auxHigh).append(", EDX\n");
        pila.push(auxLow);
    }
    private void operacionMultiplicacionFlotante(String op2, String op1) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        code.append("FLD ").append(op1).append("\n");
        code.append("FMUL ").append(op2).append("\n");
        code.append("FSTP ").append(aux).append("\n");
        pila.push(aux);
    }
    private void operacionDivisionEntera(String op2, String op1) throws IOException{
        String auxCociente = crearVariableAux(TablaSimbolos.ULONGINT);
        String auxResto = crearVariableAux(TablaSimbolos.ULONGINT); //Chequear si se utilizara
        code.append("MOV EAX, ").append(op1).append("\n");
        code.append("DIV ").append(op2).append("\n");
        code.append("MOV ").append(auxCociente).append(", EAX\n");
        code.append("MOV ").append(auxResto).append(", EDX\n");
        pila.push(auxCociente);
    }
    private void operacionDivisionFlotante(String op2, String op1) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        code.append("FLD ").append(op1).append("\n");
        code.append("FDIV ").append(op2).append("\n");
        code.append("FSTP ").append(aux).append("\n");
        pila.push(aux);
    }
    private void asignacionEntera(String op2, String op1) throws IOException{
        code.append("MOV EAX, ").append(op2).append("\n");
        code.append("MOV ").append(op1).append(",  EAX\n");
    }
    private void asignacionFlotante(String op2, String op1) throws IOException{
        code.append("FLD ").append(op2).append("\n");
        code.append("FSTP ").append(op1).append("\n");
    }
    private void comparacionMayorEntera(String op2, String op1) throws IOException{
        code.append("MOV EAX, ").append(op1).append("\n");
        code.append("CMP EAX, ").append(op2).append("\n");
        code.append("PUSHF" + "\n"); //Almacena flags en la pila
    }
    private void realizarConversion(String op1){
        if (TablaSimbolos.getTipo(op1) == TablaSimbolos.ULONGINT){
            TablaSimbolos.cambiarTipo(op1,TablaSimbolos.SINGLE);
            pila.push(op1);
        } else {
            System.out.println("La conversion debe ser de un tipo ULONGINT a un tipo SINGLE");
        }
    }
    private void generarSalto(){

    }
    private String crearVariableAux(String tipo) throws IOException {
        String varAux = "@aux" + (++contadorAux);
        CampoTablaSimbolos campoVarAux = new CampoTablaSimbolos(false, tipo);
        campoVarAux.setUso("nombre de variable");
        TablaSimbolos.agregarLexema(varAux, campoVarAux);//Agregar auxiliar a la tabla de simbolos
        return varAux;
    }
    private void generarSalida(String op1) throws IOException{
        String tipo = TablaSimbolos.getTipo(op1);
        if (tipo.equals(TablaSimbolos.SINGLE)){
            writer.write()
        }
    }
    private String formatearOperando(String op) {
        String opFormateado = op;
        if ((!TablaSimbolos.getUso(opFormateado).equals("constante")) && (opFormateado.charAt(0) != '@')){
            opFormateado= "_" + opFormateado;
            opFormateado = opFormateado.replace(':','_');
        }
        return opFormateado;
    }
    private void generarHeader() throws IOException{
        writer.write(".386\n");
        writer.write(".model flat, stdcall\n");
        writer.write(".STACK 200h\n");
        writer.write("option casemap :none\n\n");
        writer.write("include \\masm32\\include\\windows.inc\n");
        writer.write("include \\masm32\\include\\kernel32.inc\n");
        writer.write("includelib \\masm32\\lib\\kernel32.lib\n\n");
        //writer.write("    resultado DWORD ?\n");

    }
}
