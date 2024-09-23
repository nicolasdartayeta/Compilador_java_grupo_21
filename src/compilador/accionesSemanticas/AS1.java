package compilador.accionesSemanticas;

import compilador.Token;

import java.text.StringCharacterIterator;

public class AS1 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        lexema = new StringBuilder();
        return null;
    }
}
