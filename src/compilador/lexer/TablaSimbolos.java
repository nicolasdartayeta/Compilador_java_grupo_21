package compilador.lexer;

import compilador.parser.Parser;

import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {

    public static final String STRUCT = "STRUCT";
    public static final String FOR = "FOR";
    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String SINGLE = "SINGLE";
    public static final String ULONGINT = "ULONGINT";
    public static final String IF = "IF";
    public static final String THEN = "THEN";
    public static final String ELSE = "ELSE";
    public static final String BEGIN = "BEGIN";
    public static final String END = "END";
    public static final String END_IF = "END_IF";
    public static final String OUTF = "OUTF";
    public static final String TYPEDEF = "TYPEDEF";
    public static final String FUN = "FUN";
    public static final String RET = "RET";
    public static final String TOS = "TOS";

    private static final HashMap<String, CampoTablaSimbolos> tablaSimbolos = new HashMap<String, CampoTablaSimbolos>();
    private static final HashMap<String, Integer> tablaPalabrasReservadas = createTablaPalabrasReservadas();

    public static Integer getIDPalabraReservada(String palabraReservada) {
        if (esPalabraReservada(palabraReservada)){
            return tablaPalabrasReservadas.get(palabraReservada);
        }
        return null;
    }

    public static String getPalabraReservadaFromId(int id) {
        for (String key: tablaPalabrasReservadas.keySet()) {
            if (tablaPalabrasReservadas.get(key).equals(id)) {
                return key;
            }
        }
        throw new IllegalArgumentException("No hay una palabra reservada con ese id");
    }

    public static boolean esPalabraReservada(String palabraReservada) {
        return tablaPalabrasReservadas.containsKey(palabraReservada);
    }

    private static HashMap<String, Integer> createTablaPalabrasReservadas() {
        HashMap<String, Integer> tabla = new HashMap<String, Integer>();

        tabla.put(STRUCT, (int) Parser.STRUCT);
        tabla.put(FOR, (int) Parser.FOR);
        tabla.put(UP, (int) Parser.UP);
        tabla.put(DOWN, (int) Parser.DOWN);
        tabla.put(SINGLE, (int) Parser.SINGLE);
        tabla.put(ULONGINT, (int) Parser.ULONGINT);
        tabla.put(IF, (int) Parser.IF);
        tabla.put(THEN, (int) Parser.THEN);
        tabla.put(ELSE, (int) Parser.ELSE);
        tabla.put(BEGIN, (int) Parser.BEGIN);
        tabla.put(END, (int) Parser.END);
        tabla.put(END_IF, (int) Parser.END_IF);
        tabla.put(OUTF, (int) Parser.OUTF);
        tabla.put(TYPEDEF, (int) Parser.TYPEDEF);
        tabla.put(FUN, (int) Parser.FUN);
        tabla.put(RET, (int) Parser.RET);
        tabla.put(TOS, (int) Parser.TOS);

        return tabla;
    }

    public static void agregarLexema(String lexema, CampoTablaSimbolos campoTablaSimbolos){
        tablaSimbolos.put(lexema, campoTablaSimbolos);
    };

    public static void eliminarLexema(String lexema){
        tablaSimbolos.remove(lexema);
    };

    public static void imprimirTabla() {
        // Encabezado
        System.out.println("---------------------------------------------");
        System.out.printf("%-15s %-10s %-10s %-10s\n", "Simbolo", "Es Tipo", "Tipo", "Usos");
        System.out.println("---------------------------------------------");

        // Iterar sobre la tabla e imprimir cada entrada
        for (Map.Entry<String, CampoTablaSimbolos> entry : tablaSimbolos.entrySet()) {
            String simbolo = entry.getKey();
            CampoTablaSimbolos campos = entry.getValue();

            System.out.printf("%-15s %-10s %-10s %-10d\n",
                    simbolo,
                    campos.esTipo(),
                    campos.getTipo(),
                    campos.getUsos()
            );
        }

        System.out.println("---------------------------------------------");
    }

    public void cambiarValor(String lexema){}

    public static boolean existeLexema(String lexema){
        return tablaSimbolos.containsKey(lexema);
    };

    public static int getCantidadDeUsos(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = tablaSimbolos.get(lexema);

        if (campoTablaSimbolos == null) {
            throw new IllegalArgumentException("No se puede aumentar el lexema " + lexema);
        }

        return campoTablaSimbolos.getUsos();
    }

    public static void aumentarUso(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = tablaSimbolos.get(lexema);

        if (campoTablaSimbolos == null) {
            throw new IllegalArgumentException("No se puede aumentar el lexema " + lexema);
        }

        campoTablaSimbolos.aumentarUso();
    }

    public static void decrementarUso(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = tablaSimbolos.get(lexema);

        if (campoTablaSimbolos == null) {
            throw new IllegalArgumentException("No se puede aumentar el lexema " + lexema);
        }

        campoTablaSimbolos.decrementarUso();
    }

    public static boolean esUnTipo(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = tablaSimbolos.get(lexema);

        if (campoTablaSimbolos != null) {
            return campoTablaSimbolos.esTipo();
        }

        return false;
    }

    public static boolean esTipo(String lexema, String tipo) {
        CampoTablaSimbolos campoTablaSimbolos = tablaSimbolos.get(lexema);

        if (campoTablaSimbolos != null && campoTablaSimbolos.getTipo() != null) {
            return campoTablaSimbolos.getTipo().equals(tipo);
        }

        return false;
    }

    public static String getTipo(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = tablaSimbolos.get(lexema);

        if (campoTablaSimbolos != null) {
            return campoTablaSimbolos.getTipo();
        }

        throw new IllegalArgumentException("No existe el lexema " + lexema);
    }

    public static void convertirATipo(String lexema, String tipo) {
        CampoTablaSimbolos campoTablaSimbolos = tablaSimbolos.get(lexema);
        if (campoTablaSimbolos != null) {
            campoTablaSimbolos.setTipo(tipo);
            campoTablaSimbolos.setEsTipo(true);
        } else {
        throw new IllegalArgumentException("No se encuentra el lexema " + lexema + "en la tabla de simbolos");
        }
    }

    public static void cambiarTipo(String lexema, String tipo) {
        CampoTablaSimbolos campoTablaSimbolos = tablaSimbolos.get(lexema);
        if (campoTablaSimbolos != null) {
            campoTablaSimbolos.setTipo(tipo);
        } else {
            throw new IllegalArgumentException("No se encuentra el lexema " + lexema + "en la tabla de simbolos");
        }
    }
}
