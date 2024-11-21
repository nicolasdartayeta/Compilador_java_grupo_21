package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.CampoTablaSimbolos;
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

        if (TablaSimbolos.existeLexema(lexema.toString()) == false) {
            TablaSimbolos.agregarLexema(lexema.toString(), new CampoTablaSimbolos(false, TablaSimbolos.ULONGINT));
        } else {
            TablaSimbolos.aumentarUso(lexema.toString());
        }

        try {
            long decimal = Integer.parseUnsignedInt(lexema.toString());

            int tokenDecimal = TablaToken.getTokenID(TablaToken.CONSTANTE_DECIMAL);



            return new Token(tokenDecimal, lexema.toString(), numeroDeLinea);
        } catch (NumberFormatException e) {
            return new TokenError(TablaToken.getTokenID(TablaToken.TOKERROR), lexema.toString(), numeroDeLinea, "Linea "+ numeroDeLinea +": La constante se pasa de rango");
        }
    }
}
