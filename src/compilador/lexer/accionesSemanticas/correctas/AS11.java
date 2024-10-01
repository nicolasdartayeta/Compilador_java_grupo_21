package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;
import compilador.lexer.token.TokenError;

import java.text.StringCharacterIterator;

public class AS11 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        input.previous();
        try {
            long numero = Long.parseUnsignedLong(lexema.toString(),8);

            int tokenOctal = TablaToken.getTokenID(TablaToken.CONSTANTE_OCTAL);

            if (TablaSimbolos.existeLexema(lexema.toString()) == false){
                TablaSimbolos.agregarLexema(lexema.toString(),tokenOctal);
            }

            return new Token(tokenOctal, lexema.toString(), numeroDeLinea);
        } catch (NumberFormatException e) {
            return new TokenError(TablaToken.getTokenID(TablaToken.ERROR), lexema.toString(), numeroDeLinea, "La constante se pasa de rango");
        }
    }
}
