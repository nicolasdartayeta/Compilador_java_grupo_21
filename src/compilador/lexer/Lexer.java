package compilador.lexer;

import compilador.lexer.accionesSemanticas.*;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.StringCharacterIterator;

public class Lexer {
    public StringCharacterIterator input;
    private int current_index = 0;

    private final int[][] matrizTransicionEstado = {
        {9,16,2,3,17,2,16,17,8,9,10,12,12,14,14,14,-1,-1},
        {8,16,2,3,17,2,16,17,8,9,10,12,12,14,14,14,-1,-1},
        {8,16,2,3,17,2,16,17,8,8,10,12,12,14,14,14,-1,-1},
        {3,16,2,3,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {3,16,2,3,17,2,16,17,16,16,10,17,13,17,16,17,-1,-1},
        {6,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {6,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {4,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {16,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {16,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {16,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {16,16,2,16,16,2,16,16,16,16,10,17,17,17,16,17,-1,-1},
        {16,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {16,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {16,16,2,16,17,2,16,17,16,16,10,17,17,15,16,17,-1,-1},
        {16,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {16,16,2,16,17,2,16,17,11,11,10,17,17,17,16,17,-1,-1},
        {7,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {1,16,2,16,17,0,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {17,2,5,16,17,5,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {17,16,2,3,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {10,16,16,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {17,16,16,16,17,2,16,17,16,16,16,17,17,17,16,17,-1,-1},
        {0,16,16,16,17,2,16,17,16,16,17,17,17,17,16,17,-1,-1},
        {0,16,16,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {0,16,16,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {17,16,2,16,17,2,16,17,16,16,10,17,17,17,16,17,-1,-1},
        {16,16,17,16,17,17,16,17,16,16,17,17,16,17,16,17,-1,-1},
    };
    private final AccionSemantica[][] matrizDeAccionesSemanticas = {
            {new AS4(),new AS9(),null,new AS5(),new ASE(),null,new AS2(),null,new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new ASE(),null},
            {new AS4(),new AS9(),null,new AS5(),new ASE(),null,new AS2(),null,new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new ASE(),null},
            {new AS4(),new AS9(),null,new AS5(),new ASE(),null,new AS2(),null,new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new AS5(),new ASE(),null},
            {new AS4(),new AS9(),null,new AS5(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),new ASE(),new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS4(),new AS9(),null,new AS5(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS5(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS4(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS4(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS4(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS8(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS8(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS8(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS8(),new AS9(),null,new AS6(),new AS10(),null,new AS2(),new AS10(),new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS8(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS8(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS8(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new AS5(),new AS7(),new ASE(),new ASE(),null},
            {new AS8(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS8(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS5(),new AS5(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS4(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS4(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new ASE(),null,null,new AS6(),new ASE(),null,new AS2(),null,new AS7(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new ASE(),new AS9(),null,new AS5(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new AS1(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new ASE(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS3(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {null,new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new ASE(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {null,new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {null,new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),null,new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {new ASE(),new AS9(),null,new AS6(),new ASE(),null,new AS2(),null,new AS12(),new AS11(),new AS5(),new ASE(),new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
            {null,new AS9(),new ASE(),new AS6(),new ASE(),new ASE(),new AS2(),new ASE(),new AS12(),new AS11(),new ASE(),new ASE(),new AS7(),new ASE(),new AS7(),new ASE(),new ASE(),null},
    };

    public Lexer(String filePath) {
        try {
            // Lee el archivo .txt y convierte su contenido a String
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Inicializa el input del lexer con el contenido del archivo
            this.input = new StringCharacterIterator(fileContent);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
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
        } else if (c == '(') {
            return 9;
        } else if (c == ')') {
            return 10;
        } else if (c == '=') {
            return 11;
        } else if (c == '+') {
            return 12;
        } else if (c == '*') {
            return 13;
        } else if (c == '-') {
            return 14;
        } else if (c == ',') {
            return 15;
        } else if (c == '.') {
            return 16;
        } else if (c == '!') {
            return 17;
        } else if (c == '/') {
            return 18;
        } else if (c == '#') {
            return 19;
        } else if (c == '_') {
            return 20;
        } else if (c == '{') {
            return 21;
        } else if (c == '}') {
            return 22;
        } else if (c == '\n') {
            return 23;
        } else if (c == '\t') {
            return 24;
        } else if (c == ' ') {
            return 25;
        } else if (c == StringCharacterIterator.DONE) {
            return 27;
        } else {
            return 26;
        }
    }

    public int yylex() {
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
                    token = accionSemantica.ejecutar(input, lexema);
                }

                charLeido = input.next();

                if (token != null) {
                    tokenFound = true;
                }
            }
        } else {
            tokenFound = true;
            token = new Token(TablaToken.getTokenID(TablaToken.EOF), TablaToken.EOF);
        }

        System.out.println(token);
        return token.getTokenID();
    }
}
