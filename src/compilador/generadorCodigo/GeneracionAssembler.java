package compilador.generadorCodigo;

import java.util.ArrayList;
import java.util.Stack;

public class GeneracionAssembler {
    private static ArrayList<String> polaca;
    private static StringBuilder assemblerGenerado = new StringBuilder();
    private static Stack<String> pilaAux = new Stack<>();

    public GeneracionAssembler(ArrayList<String> representacionPolaca) {
        this.polaca = representacionPolaca;
    }




}
