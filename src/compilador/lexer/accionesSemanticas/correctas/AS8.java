package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;

import java.text.StringCharacterIterator;

public class AS8 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        lexema.append(input.current());
        return switch (lexema.toString()) {
            case "+" -> new Token(TablaToken.getTokenID(TablaToken.SUMA), lexema.toString(), numeroDeLinea);
            case "-" -> new Token(TablaToken.getTokenID(TablaToken.RESTA), lexema.toString(), numeroDeLinea);
            case "*" -> new Token(TablaToken.getTokenID(TablaToken.MULTIPLICACION), lexema.toString(), numeroDeLinea);
            case "=" -> new Token(TablaToken.getTokenID(TablaToken.IGUAL), lexema.toString(), numeroDeLinea);
            case "[" -> new Token(TablaToken.getTokenID(TablaToken.CORCHETE_L), lexema.toString(), numeroDeLinea);
            case "]" -> new Token(TablaToken.getTokenID(TablaToken.CORCHETE_R), lexema.toString(), numeroDeLinea);
            case "(" -> new Token(TablaToken.getTokenID(TablaToken.PARENTESIS_L), lexema.toString(), numeroDeLinea);
            case ")" -> new Token(TablaToken.getTokenID(TablaToken.PARENTESIS_R), lexema.toString(), numeroDeLinea);
            case "," -> new Token(TablaToken.getTokenID(TablaToken.COMA), lexema.toString(), numeroDeLinea);
            case "." -> new Token(TablaToken.getTokenID(TablaToken.PUNTO), lexema.toString(), numeroDeLinea);
            case ";" -> new Token(TablaToken.getTokenID(TablaToken.PUNTO_Y_COMA), lexema.toString(), numeroDeLinea);
            default -> null;
        };

    }
}
