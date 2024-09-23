package compilador.accionesSemanticas;

import compilador.Token;

import java.text.StringCharacterIterator;

public class AS5 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        lexema.append(input.current());
        return null;
    }
}
