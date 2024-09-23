package compilador.accionesSemanticas;

import compilador.TablaSimbolos;
import compilador.TablaToken;
import compilador.Token;

import java.text.StringCharacterIterator;

public class AS7 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        input.previous();
        return new Token(TablaToken.getTokenID(TablaToken.CONSTANTE_SINGLE), lexema.toString());
    }
}
