package compilador.lexer.accionesSemanticas.errores;

import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;
import compilador.lexer.token.TokenError;

import java.text.StringCharacterIterator;

public abstract class ASE implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        TokenError error = new TokenError(TablaToken.getTokenID(TablaToken.TOKERROR), lexema.toString(), numeroDeLinea, "Linea " + numeroDeLinea + ": " + this.getDescripcionError(input, lexema));
        input.previous();
        return error;
    }

    protected abstract String getDescripcionError(StringCharacterIterator input, StringBuilder lexema);
}
