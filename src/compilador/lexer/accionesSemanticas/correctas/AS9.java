package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;

import java.text.StringCharacterIterator;

public class AS9 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        input.previous();
        return new Token(TablaToken.getTokenID(TablaToken.DIVISION), lexema.toString(), numeroDeLinea);
    }
}
