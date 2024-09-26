package compilador.lexer.accionesSemanticas;

import compilador.lexer.TablaToken;
import compilador.lexer.Token;

import java.text.StringCharacterIterator;

public class AS2 implements AccionSemantica {

    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
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
