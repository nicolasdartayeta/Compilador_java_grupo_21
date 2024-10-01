package compilador.lexer;

import compilador.parser.Parser;

import java.util.HashMap;

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

    private static HashMap<String, Integer> tablaSimbolos = new HashMap<String, Integer>();
    private static HashMap<String, Integer> tablaPalabrasReservadas = createTablaPalabrasReservadas();

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

    public static void agregarLexema(String lexema, int token){
        tablaSimbolos.put(lexema,token);
    };

    public static boolean existeLexema(String lexema){
        return tablaSimbolos.containsKey(lexema);
    };
}
