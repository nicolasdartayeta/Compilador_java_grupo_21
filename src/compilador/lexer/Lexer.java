package compilador.lexer;

import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.StringCharacterIterator;

public class Lexer {
    public StringCharacterIterator input;
    private int current_index = 0;
    private int numeroDeLinea = 1;

    private final int[][] matrizTransicionEstado = CSVAMatriz.leerMatrizDeTransicion("src/compilador/lexer/matrizTransicion.csv", 30, 18);

    private final AccionSemantica[][] matrizDeAccionesSemanticas = CSVAMatriz.leerMatrizDeAccionesSemanticas("src/compilador/lexer/accionesSemanticas.csv", 30, 18);

    public Lexer(String filePath) {
        try {
            // Lee el archivo .txt y convierte su contenido a String
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Inicializa el input del lexer con el contenido del archivo
            this.input = new StringCharacterIterator(fileContent);
        } catch (IOException e) {
            System.err.println("Error al leer el  archivo: " + e.getMessage());
        }
    }

    public int getNumeroDeLinea() {
        return numeroDeLinea;
    }

    private int getIndexFromChar(char c) {
        if (c == '0')
            return 0;
        else if (Character.isDigit(c) && Character.getNumericValue(c) < 8) {
            return 1;
        } else if (Character.isDigit(c) && Character.getNumericValue(c) < 10) {
            return 2;
        } else if (Character.isLetter(c) && c != 's') {
            return 3;
        } else if (Character.isLetter(c) && c == 's') {
            return 4;
        } else if (c == '<') {
            return 5;
        } else if (c == '>') {
            return 6;
        } else if (c == ':') {
            return 7;
        } else if (c == ';') {
            return 8;
        } else if (c == '[') {
            return 9;
        } else if (c == ']') {
            return 10;
        } else if (c == '(') {
            return 11;
        } else if (c == ')') {
            return 12;
        } else if (c == '=') {
            return 13;
        } else if (c == '+') {
            return 14;
        } else if (c == '*') {
            return 15;
        } else if (c == '-') {
            return 16;
        } else if (c == ',') {
            return 17;
        } else if (c == '.') {
            return 18;
        } else if (c == '!') {
            return 19;
        } else if (c == '/') {
            return 20;
        } else if (c == '#') {
            return 21;
        } else if (c == '_') {
            return 22;
        } else if (c == '{') {
            return 23;
        } else if (c == '}') {
            return 24;
        } else if (c == '\n') {
            numeroDeLinea++;
            return 25;
        }else if (c == '\r') {
            return 25;
        } else if (c == '\t') {
            return 26;
        } else if (c == ' ') {
            return 27;
        } else if (c == StringCharacterIterator.DONE) {
            return 29;
        } else {
            return 28;
        }
    }

    public Token getNextToken() {
        Token token = null;
        boolean tokenFound = false;
        int estadoActual = 0;
        StringBuilder lexema = new StringBuilder();

        char charLeido = input.current();

        if (charLeido != StringCharacterIterator.DONE) {
            while (!tokenFound) {
                int fila = getIndexFromChar(charLeido);

                AccionSemantica accionSemantica = matrizDeAccionesSemanticas[fila][estadoActual];
                estadoActual = matrizTransicionEstado[fila][estadoActual];

                if (accionSemantica != null) {
                    token = accionSemantica.ejecutar(input, lexema, numeroDeLinea);
                }

                charLeido = input.next();

                if (token != null) {
                    tokenFound = true;
                }
            }
        } else {
            tokenFound = true;
            token = new Token(TablaToken.getTokenID(TablaToken.EOF), TablaToken.EOF, numeroDeLinea);
        }

        return token;
    }
}
