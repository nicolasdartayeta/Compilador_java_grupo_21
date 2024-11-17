package compilador.generadorCodigo;

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
        try {
            this.writer = new BufferedWriter(new FileWriter(rutaArchivo));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generarCodigoAssembler() throws IOException{
        try {
            generarHeader();
            generarData();
            generarCodigo();

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generarCodigo() throws IOException {
        writer.write(".code\n");
        writer.write("start:\n");
        for (String token: polaca){
            System.out.println("Procesando token: " + token);
            procesarToken(token);
        }
        writer.write("invoke ExitProcess, 0\n");
        writer.write("end start");
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
                    System.out.println(pila);
                    asignacion();
                    break;
                case "-":
                    //Chequear si la suma va a ser entera o flotante y llamar a funcion correspondiente
                    operacionRestaEntera(formatearOperando(pila.pop()), formatearOperando(pila.pop()));
                    break;
                case "BI":
                    writer.write("JMP " + formatearOperando(pila.pop()) + "\n");
                    break;
                case "BF":
                    break;
                case "tos":
                    realizarConversion(formatearOperando(pila.pop()));
                    break;
                case "outf":
                    procesar
                    break;
                default:
                    pila.push(token);
                    break;
            }
        }
    }

    private void asignacion() throws IOException {
        // Ver si es asignacion entera o flotante
        String valorAAsignar = formatearOperando(pila.pop());
        String variableAsignada = formatearOperando(pila.pop());

        String tipoVaribale = TablaSimbolos.getTipo(variableAsignada);

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

    private String getTipo(String op1, String op2, String operador) {
        return null;
    }

    private void generarData() throws IOException {
        writer.write(".data\n");
        List<String> usosAAgregar = Arrays.asList("nombre de variable");

        for (String uso : usosAAgregar) {
            List<String> lexemas = TablaSimbolos.getEntradasPorUso(uso);
            for (String lexema : lexemas) {
                String tipo = TablaSimbolos.getTipo(lexema);
                lexema = lexema.replace(':', '_');
                switch (tipo) {
                    case TablaSimbolos.SINGLE:
                        writer.write("\t _" + lexema + " dw 0\n");
                        break;
                    case TablaSimbolos.ULONGINT:
                        writer.write("\t _" + lexema+" dd 0\n");
                        break;
                    case TablaToken.INLINE_STRING:
                        writer.write("\t _" + lexema+" db \"" + lexema + "\", 0\n");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void operacionSumaEntera(String op1, String op2) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.ULONGINT);
        writer.write("MOV EAX, _" + op1 + "\n");
        writer.write("ADD EAX, _" + op2 + "\n");
        writer.write("MOV " + aux + ", EAX\n");
        pila.push(aux);
    }
    private void operacionSumaFlotante(String op2, String op1) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        writer.write("FLD _" + op1 + "\n");
        writer.write("FADD _" + op2 + "\n");
        writer.write("FSTP _" + aux + "\n");
        pila.push(aux);
    }
    private void operacionRestaEntera(String op2, String op1) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.ULONGINT);
        writer.write("MOV EAX, _" + op1 + "\n");
        writer.write("SUB EAX, _" + op2 + "\n");
        writer.write("MOV " + aux + ", EAX\n");
        pila.push(aux);
    }
    private void operacionRestaFlotante(String op2, String op1) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        writer.write("FLD _" + op1 + "\n");
        writer.write("FSUB _" + op2 + "\n");
        writer.write("FSTP _" + aux + "\n");
        pila.push(aux);
    }
    private void operacionMultiplicacionEntera(String op2, String op1) throws IOException{
        String auxLow = crearVariableAux(TablaSimbolos.ULONGINT);   // Parte baja (32 bits)
        String auxHigh = crearVariableAux(TablaSimbolos.ULONGINT); // Parte alta (32 bits)
        writer.write("MOV EAX, _" + op1 + "\n");
        writer.write("MUL _" + op2 + "\n");
        writer.write("MOV " + auxLow + ", EAX\n"); //Chequear si esta bien
        writer.write("MOV " +  auxHigh + ", EDX\n");
        pila.push(auxLow);
    }
    private void operacionMultiplicacionFlotante(String op2, String op1) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        writer.write("FLD _" + op1 + "\n");
        writer.write("FMUL _" + op2 + "\n");
        writer.write("FSTP " + aux + "\n");
        pila.push(aux);
    }
    private void operacionDivisionEntera(String op2, String op1) throws IOException{
        String auxCociente = crearVariableAux(TablaSimbolos.ULONGINT);
        String auxResto = crearVariableAux(TablaSimbolos.ULONGINT); //Chequear si se utilizara
        writer.write("MOV EAX, _" + op1 + "\n");
        writer.write("DIV _" + op2 + "\n");
        writer.write("MOV " + auxCociente + ", EAX\n");
        writer.write("MOV " + auxResto + ", EDX\n");
        pila.push(auxCociente);
    }
    private void operacionDivisionFlotante(String op2, String op1) throws IOException{
        String aux = crearVariableAux(TablaSimbolos.SINGLE);
        writer.write("FLD _" + op1 + "\n");
        writer.write("FDIV _" + op2 + "\n");
        writer.write("FSTP _" + aux + "\n");
        pila.push(aux);
    }
    private void asignacionEntera(String op2, String op1) throws IOException{
        writer.write("MOV EAX, _" + op2 + "\n");
        writer.write("MOV _" + op1 + ",  EAX\n");
    }
    private void asignacionFlotante(String op2, String op1) throws IOException{
        writer.write("FLD _" + op2 + "\n");
        writer.write("FSTP _" + op1 + "\n");
    }
    private void comparacionMayorEntera(String op2, String op1) throws IOException{
        writer.write("MOV EAX, _" + op1 + "\n");
        writer.write("CMP EAX, _" + op2 + "\n");
        writer.write("PUSHF" + "\n"); //Almacena flags en la pila
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
        //TablaSimbolos.agregarLexema(varAux,tipo);//Agregar auxiliar a la tabla de simbolos
        return varAux;
    }
    private String formatearOperando(String op) {
        String opFormateado = op;
        if ((TablaSimbolos.getUso(op) != "constante") && (opFormateado.charAt(0) != '@')){
            opFormateado= "_"+opFormateado;
            opFormateado.replaceAll(":","_");
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
