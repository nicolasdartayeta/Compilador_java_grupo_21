package compilador.lexer;

import compilador.parser.Parser;

import java.util.HashMap;

public class TablaToken {
    public static final String IDENTIFICADOR_GENERICO = "IDENTIFICADOR";
    public static final String IDENTIFICADOR_ULONGINT = "IDENTIFICADOR_ULONGINT";
    public static final String IDENTIFICADOR_SINGLE = "IDENTIFICADOR_SINGLE";
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
    public static final String PARENTESIS_R = "PARENTESIS_R";
    public static final String COMA = "COMA";
    public static final String PUNTO = "PUNTO";
    public static final String PUNTO_Y_COMA = "PUNTO_Y_COMA";
    public static final String INLINE_STRING = "INLINE_STRING";
    public static final String ERROR = "ERROR";
    public static final String EOF = "EOF";

    private static HashMap<String, Integer> tablaTokens = createTablaTokens();

    private static HashMap<String, Integer> createTablaTokens() {
        HashMap<String, Integer> tabla = new HashMap<String, Integer>();

        tabla.put(IDENTIFICADOR_GENERICO, (int) Parser.IDENTIFICADOR_GENERICO);
        tabla.put(IDENTIFICADOR_ULONGINT, (int) Parser.IDENTIFICADOR_ULONGINT);
        tabla.put(IDENTIFICADOR_SINGLE, (int) Parser.IDENTIFICADOR_SINGLE);
        tabla.put(CONSTANTE_DECIMAL, (int) Parser.CONSTANTE_DECIMAL);
        tabla.put(CONSTANTE_OCTAL, (int) Parser.CONSTANTE_OCTAL);
        tabla.put(CONSTANTE_SINGLE, (int) Parser.CONSTANTE_SINGLE);
        tabla.put(SUMA, (int) Parser.SUMA);
        tabla.put(RESTA, (int) Parser.RESTA);
        tabla.put(MULTIPLICACION, (int) Parser.MULTIPLICACION);
        tabla.put(DIVISION, (int) Parser.DIVISION);
        tabla.put(ASIGNACION, (int) Parser.ASIGNACION);
        tabla.put(MAYOR_O_IGUAL, (int) Parser.MAYOR_O_IGUAL);
        tabla.put(MENOR_O_IGUAL, (int) Parser.MENOR_O_IGUAL);
        tabla.put(MAYOR, (int) Parser.MAYOR);
        tabla.put(MENOR, (int) Parser.MENOR);
        tabla.put(IGUAL, (int) Parser.IGUAL);
        tabla.put(DESIGUAL, (int) Parser.DESIGUAL);
        tabla.put(PARENTESIS_L, (int) Parser.PARENTESIS_L);
        tabla.put(PARENTESIS_R, (int) Parser.PARENTESIS_R);
        tabla.put(COMA, (int) Parser.COMA);
        tabla.put(PUNTO, (int) Parser.PUNTO);
        tabla.put(PUNTO_Y_COMA, (int) Parser.PUNTO_Y_COMA);
        tabla.put(INLINE_STRING, (int) Parser.INLINE_STRING);
        tabla.put(ERROR, (int) Parser.ERROR);
        tabla.put(EOF, 0);
        return tabla;
    }

    public static int getTokenID(String token){
        return tablaTokens.get(token);
    }
}
