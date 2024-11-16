package compilador.generadorCodigo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import compilador.lexer.TablaSimbolos;

public class GeneracionAssembler {
    private static ArrayList<String> polaca;
    private BufferedWriter writer;
    private static Stack<String> pila = new Stack<>();
    private int contadorAux = 0;

    public GeneracionAssembler(ArrayList<String> representacionPolaca, String rutaArchivo) throws IOException {
        this.polaca = representacionPolaca;
        this.writer = new BufferedWriter(new FileWriter(rutaArchivo));
    }

    public void generarCodigoAssembler() throws IOException{
        generarHeader();
        for (String token: polaca){
            procesarToken(token);
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


    private String getTipo(String op1, String op2, String operador){

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
        TablaSimbolos.agregarLexema(varAux);//Agregar auxiliar a la tabla de simbolos
        return varAux;
    }

    private void generarHeader() throws IOException{
        writer.write(".386\n");
        writer.write(".model flat, stdcall\n");
        writer.write("option casemap :none\n\n");
        writer.write("include \\masm32\\include\\windows.inc\n");
        writer.write("include \\masm32\\include\\kernel32.inc\n");
        writer.write("includelib \\masm32\\lib\\kernel32.lib\n\n");
        writer.write(".data\n");
        writer.write("    resultado DWORD ?\n");
        writer.write(".code\n");
        writer.write("start:\n");
    }
}
