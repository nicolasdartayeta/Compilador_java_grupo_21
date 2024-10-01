package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;

import java.text.StringCharacterIterator;

public class AS2 implements AccionSemantica {

    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        if (input.current() == '='){
            lexema.append(input.current());
            if (lexema.toString().equals("<=")) {
                return new Token(TablaToken.getTokenID(TablaToken.MENOR_O_IGUAL), lexema.toString());
            } else if (lexema.toString().equals(">=")) {
                return new Token(TablaToken.getTokenID(TablaToken.MAYOR_O_IGUAL), lexema.toString());
            }
        } else {
            input.previous();
            if (lexema.toString().equals("<")) {
                return new Token(TablaToken.getTokenID(TablaToken.MENOR), lexema.toString());
            } else if (lexema.toString().equals(">")) {
                return new Token(TablaToken.getTokenID(TablaToken.MAYOR), lexema.toString());
            }
        }
        return null;
    }
}
