package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;

import java.text.StringCharacterIterator;

public class AS1 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        lexema.delete(0, lexema.length());
        return null;
    }
}
