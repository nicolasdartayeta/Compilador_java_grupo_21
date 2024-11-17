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

    public GeneradorAssembler(ArrayList<String> representacionPolaca, String rutaArchivo){
        this.polaca = representacionPolaca;
        try {
            this.writer = new BufferedWriter(new FileWriter(rutaArchivo));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generarCodigoAssembler(){
        try {
            generarHeader();
            generarData();
            for (String token: polaca){
                procesarToken(token);
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void procesarToken(String token) throws  IOException{

        switch(token){
            case "+":
                //Chequear si la suma va a ser entera o flotante y llamar a funcion correspondiente
                operacionSumaEntera(pila.pop(),pila.pop());
                break;
            case "-":
                //Chequear si la suma va a ser entera o flotante y llamar a funcion correspondiente
                operacionRestaEntera(pila.pop(),pila.pop());
                break;
            case "BI":
                writer.write("JMP " + pila.pop() + "\n");
                break;
            case BF:

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
                switch (tipo) {
                    case TablaSimbolos.SINGLE:
                        writer.write("\t" + lexema + " dw 0\n");
                        break;
                    case TablaSimbolos.ULONGINT:
                        writer.write("\t" + lexema+" dd 0\n");
                        break;
                    case TablaToken.INLINE_STRING:
                        writer.write("\t" + lexema+" db \"" + lexema + "\", 0\n");
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
    private void generarSalto(){

    }
    private String crearVariableAux(String tipo) throws IOException {
        String varAux = "@aux" + (++contadorAux);
        //TablaSimbolos.agregarLexema(varAux,tipo);//Agregar auxiliar a la tabla de simbolos
        return varAux;
    }
    private void generarHeader() throws IOException{
        writer.write(".386\n");
        writer.write(".model flat, stdcall\n");
        writer.write("option casemap :none\n\n");
        writer.write("include \\masm32\\include\\windows.inc\n");
        writer.write("include \\masm32\\include\\kernel32.inc\n");
        writer.write("includelib \\masm32\\lib\\kernel32.lib\n\n");
        //writer.write("    resultado DWORD ?\n");
        //writer.write(".code\n");
        //writer.write("start:\n");
    }
}
