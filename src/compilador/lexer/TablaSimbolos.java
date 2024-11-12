package compilador.lexer;

import compilador.parser.Parser;

import java.util.HashMap;
import java.util.List;
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
        System.out.println("-----------------");
        System.out.println("Tabla de simbolos");
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("%-20s %-30s %-10s %-15s %-7s %-15s %-15s %-40s\n", "Simbolo", "Ambito", "Es Tipo", "Tipo", "Usos", "T. retorno", "T. parametro", "Campos");
        System.out.println("-----------------------------------------------------------------------------");

        // Iterar sobre la tabla e imprimir cada entrada
        for (Map.Entry<String, CampoTablaSimbolos> entry : tablaSimbolos.entrySet()) {
            String simbolo = entry.getKey();
            CampoTablaSimbolos campos = entry.getValue();

            System.out.printf("%-20s %-30s %-10s %-15s %-7d %-15s %-15s %-40s\n",
                    simbolo,
                    campos.getAmbito(),
                    campos.esTipo(),
                    campos.getTipo(),
                    campos.getUsos(),
                    campos.getTipoRetorno(),
                    campos.getTipoParametro(),
                    campos.getCampos()
            );
        }

        System.out.println("-----------------------------------------------------------------------------");
    }

    private static CampoTablaSimbolos getCampoTablaSimbolos(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = tablaSimbolos.get(lexema);

        if (campoTablaSimbolos == null) {
            throw new IllegalArgumentException("No existe la entrada " +  lexema + " en la tabla de simbolos");
        }

        return campoTablaSimbolos;
    }

    public static int getCantidadDeParametros(String lexema){
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);

        return campoTablaSimbolos.getCantidadDeParametros();
    }

    public static void setCantidadDeParametros(String lexema, Integer cantidad){
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);
        campoTablaSimbolos.setCantidadDeParametros(cantidad);
    }

    public static String getTipoParametro(String lexema){
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);

        return campoTablaSimbolos.getTipoParametro();
    }

    public static void setTipoParametro(String lexema, String tipo){
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);
        campoTablaSimbolos.setTipoParametro(tipo);
    }

    public static String getTipoRetorno(String lexema){
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);

        return campoTablaSimbolos.getTipoRetorno();
    }

    public static void setTipoRetorno(String lexema, String tipo){
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);
        campoTablaSimbolos.setTipoRetorno(tipo);
    }


    public static boolean existeLexema(String lexema){
        return tablaSimbolos.containsKey(lexema);
    };

    public static int getCantidadDeUsos(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);

        return campoTablaSimbolos.getUsos();
    }

    public static void aumentarUso(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);

        campoTablaSimbolos.aumentarUso();
    }

    public static void decrementarUso(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);

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
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);

        return campoTablaSimbolos.getTipo();
    }

    public static void convertirATipo(String lexema, String tipo) {
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);
        campoTablaSimbolos.setTipo(tipo);
        campoTablaSimbolos.setEsTipo(true);
    }

    public static void cambiarTipo(String lexema, String tipo) {
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);
        campoTablaSimbolos.setTipo(tipo);
    }

    public static String getAmbito(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);

        return campoTablaSimbolos.getAmbito();
    }
    public static void setAmbito(String lexema, String ambito) {
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);

        campoTablaSimbolos.setAmbito(ambito);
    }

    public static void deleteEntrada(String lexema) {
        tablaSimbolos.remove(lexema);
    }

    public static void addCampo(String lexema, String tipo, String nombre) {
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);
        campoTablaSimbolos.agregarCampo(tipo, nombre);
    }

    public static void agregarCampos(String lexema, List<String> tipo, List<String> nombre) {
        if (tipo.size() != nombre.size()) {
            throw new IllegalArgumentException("No se pasaron la misma cantidad de tipos que de nombres");
        }

        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);

        for (int i = 0; i < tipo.size(); i++) {
            campoTablaSimbolos.agregarCampo(tipo.get(i), nombre.get(i));
        }
    }

    public static List<CampoTablaSimbolos.Campo> getCamposTablaSimbolos(String lexema) {
        CampoTablaSimbolos campoTablaSimbolos = getCampoTablaSimbolos(lexema);
        return campoTablaSimbolos.getCampos();
    }
}
