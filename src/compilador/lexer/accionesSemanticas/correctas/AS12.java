package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;
import compilador.lexer.token.TokenError;

import java.text.StringCharacterIterator;

public class AS12 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        input.previous();

        try {
            long decimal = Long.parseUnsignedLong(lexema.toString());

            int tokenDecimal = TablaToken.getTokenID(TablaToken.CONSTANTE_DECIMAL);

            if (TablaSimbolos.existeLexema(lexema.toString()) == false) {
                TablaSimbolos.agregarLexema(lexema.toString(), tokenDecimal);
            }

            return new Token(tokenDecimal, lexema.toString(), numeroDeLinea);
        } catch (NumberFormatException e) {
            return new TokenError(TablaToken.getTokenID(TablaToken.ERROR), lexema.toString(), numeroDeLinea, "La constante se pasa de rango");
        }
    }
}
