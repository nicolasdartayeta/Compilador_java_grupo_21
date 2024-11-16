package compilador.generadorCodigo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class GeneracionAssembler {
    private static ArrayList<String> polaca;
    private BufferedWriter writer;
    private static Stack<String> pilaAux = new Stack<>();

    public GeneracionAssembler(ArrayList<String> representacionPolaca, String rutaArchivo) throws IOException {
        this.polaca = representacionPolaca;
        this.writer = new BufferedWriter(new FileWriter(rutaArchivo));
    }

    public void generarCodigoAssembler() throws IOException{
        generarHeader();
    }

    public void generarHeader() throws IOException{
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
