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
    private ArrayList<String> polaca;
    private BufferedWriter writer;
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
                String op2 = pila.pop();
                String op1 = pila.pop();
                operacionSuma(op1,op2);
                break;
            case "-":

        }
    }


    private String getTipo(String op1, String op2, String operador) {
        return null;
    }
    private void generarCodigo() {
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

    private void operacionSuma(String op1, String op2) throws IOException{
        String aux = crearVariableAux();
        writer.write("MOV EAX, _" + op1 + "\n");
        writer.write("ADD EAX, _" + op2 + "\n");
        writer.write("MOV " + aux + ", EAX\n");
        pila.push(aux);
    }

    private String crearVariableAux() throws IOException {
        String varAux = "@aux" + (++contadorAux);
        //TablaSimbolos.agregarLexema(varAux);//Agregar auxiliar a la tabla de simbolos
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
