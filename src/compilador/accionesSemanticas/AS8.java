package compilador.accionesSemanticas;

import compilador.TablaToken;
import compilador.Token;

import java.text.StringCharacterIterator;

public class AS8 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        return switch (lexema.toString()) {
            case "+" -> new Token(TablaToken.getTokenID(TablaToken.SUMA), lexema.toString());
            case "-" -> new Token(TablaToken.getTokenID(TablaToken.RESTA), lexema.toString());
            case "*" -> new Token(TablaToken.getTokenID(TablaToken.MULTIPLICACION), lexema.toString());
            case "=" -> new Token(TablaToken.getTokenID(TablaToken.IGUAL), lexema.toString());
            case "(" -> new Token(TablaToken.getTokenID(TablaToken.PARENTESIS_L), lexema.toString());
            case ")" -> new Token(TablaToken.getTokenID(TablaToken.PATENTESIS_R), lexema.toString());
            case "," -> new Token(TablaToken.getTokenID(TablaToken.COMA), lexema.toString());
            case "." -> new Token(TablaToken.getTokenID(TablaToken.PUNTO), lexema.toString());
            case ";" -> new Token(TablaToken.getTokenID(TablaToken.PUNTO_Y_COMA), lexema.toString());
            default -> null;
        };

    }
}
