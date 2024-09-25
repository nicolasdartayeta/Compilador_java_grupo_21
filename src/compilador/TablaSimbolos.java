package compilador;

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

    public static boolean esPalabraReservada(String palabraReservada) {
        return tablaPalabrasReservadas.containsKey(palabraReservada);
    }

    private static HashMap<String, Integer> createTablaPalabrasReservadas() {
        HashMap<String, Integer> tabla = new HashMap<String, Integer>();

        tabla.put(STRUCT, 1);
        tabla.put(FOR, 2);
        tabla.put(UP, 3);
        tabla.put(DOWN, 4);
        tabla.put(SINGLE, 5);
        tabla.put(ULONGINT, 6);
        tabla.put(IF, 7);
        tabla.put(THEN, 8);
        tabla.put(ELSE, 9);
        tabla.put(BEGIN, 10);
        tabla.put(END, 11);
        tabla.put(END_IF, 12);
        tabla.put(OUTF, 13);
        tabla.put(TYPEDEF, 14);
        tabla.put(FUN, 15);
        tabla.put(RET, 16);

        return tabla;
    }

    public static void agregarLexema(String lexema, int token){
        tablaSimbolos.put(lexema,token);
    };

    public static boolean existeLexema(String lexema){
        return tablaSimbolos.containsKey(lexema);
    };
}
