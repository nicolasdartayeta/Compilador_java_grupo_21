package compilador.lexer.accionesSemanticas;

import compilador.lexer.Token;

import java.text.StringCharacterIterator;

public class AS4 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        lexema.append(input.current());
        return null;
    }
}
