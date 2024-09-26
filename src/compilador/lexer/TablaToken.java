package compilador.lexer;

import java.util.HashMap;

public class TablaToken {
    public static final String IDENTIFICADOR = "IDENTIFICADOR";
    public static final String CONSTANTE_DECIMAL = "CONSTANTE_DECIMAL";
    public static final String CONSTANTE_OCTAL = "CONSTANTE_OCTAL";
    public static final String CONSTANTE_SINGLE = "CONSTANTE_SINGLE";
    public static final String SUMA = "SUMA";
    public static final String RESTA = "RESTA";
    public static final String MULTIPLICACION = "MULTIPLICACION";
    public static final String DIVISION = "DIVISION";
    public static final String ASIGNACION = "ASIGNACION";
    public static final String MAYOR_O_IGUAL = "MAYOR_O_IGUAL";
    public static final String MENOR_O_IGUAL = "MENOR_O_IGUAL";
    public static final String MAYOR = "MAYOR";
    public static final String MENOR = "MENOR";
    public static final String IGUAL = "IGUAL";
    public static final String DESIGUAL = "DESIGUAL";
    public static final String PARENTESIS_L = "PARENTESIS_L";
    public static final String PATENTESIS_R = "PATENTESIS_R";
    public static final String COMA = "COMA";
    public static final String PUNTO = "PUNTO";
    public static final String PUNTO_Y_COMA = "PUNTO_Y_COMA";
    public static final String INLINE_STRING = "INLINE_STRING";
    public static final String ERROR = "ERROR";
    public static final String EOF = "EOF";

    private static HashMap<String, Integer> tablaTokens = createTablaTokens();

    private static HashMap<String, Integer> createTablaTokens() {
        HashMap<String, Integer> tabla = new HashMap<String, Integer>();

        tabla.put(IDENTIFICADOR, 17);
        tabla.put(CONSTANTE_DECIMAL, 18);
        tabla.put(CONSTANTE_OCTAL, 19);
        tabla.put(CONSTANTE_SINGLE, 20);
        tabla.put(SUMA, 21);
        tabla.put(RESTA, 22);
        tabla.put(MULTIPLICACION, 23);
        tabla.put(DIVISION, 24);
        tabla.put(ASIGNACION, 25);
        tabla.put(MAYOR_O_IGUAL, 26);
        tabla.put(MENOR_O_IGUAL, 27);
        tabla.put(MAYOR, 28);
        tabla.put(MENOR, 29);
        tabla.put(IGUAL, 30);
        tabla.put(DESIGUAL, 31);
        tabla.put(PARENTESIS_L, 32);
        tabla.put(PATENTESIS_R, 33);
        tabla.put(COMA, 34);
        tabla.put(PUNTO, 35);
        tabla.put(PUNTO_Y_COMA, 36);
        tabla.put(INLINE_STRING, 37);
        tabla.put(ERROR, 38);
        tabla.put(EOF, 0);
        return tabla;
    }

    public static int getTokenID(String token){
        return tablaTokens.get(token);
    }
}
