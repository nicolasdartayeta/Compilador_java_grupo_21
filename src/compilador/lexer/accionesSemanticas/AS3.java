package compilador.lexer.accionesSemanticas;

import compilador.lexer.TablaToken;
import compilador.lexer.Token;

import java.text.StringCharacterIterator;

public class AS3 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        if (input.current() == '}') {
            return new Token(TablaToken.getTokenID(TablaToken.INLINE_STRING), lexema.toString());
        }

        return null;
    }
}
