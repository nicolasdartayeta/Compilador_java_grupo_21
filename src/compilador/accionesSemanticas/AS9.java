package compilador.accionesSemanticas;

import compilador.TablaToken;
import compilador.Token;

import java.text.StringCharacterIterator;

public class AS9 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        input.previous();
        return new Token(TablaToken.getTokenID(TablaToken.DIVISION), lexema.toString());
    }
}
