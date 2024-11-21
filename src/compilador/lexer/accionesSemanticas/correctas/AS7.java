package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.CampoTablaSimbolos;
import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;
import compilador.lexer.token.TokenError;

import java.text.StringCharacterIterator;

public class AS7 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        input.previous();
        String posibleFloat = formatearString(lexema.toString());

        try {
            float nro = Float.parseFloat(posibleFloat);
            int tokenSingle = TablaToken.getTokenID(TablaToken.CONSTANTE_SINGLE);

            if (!TablaSimbolos.existeLexema(lexema.toString())) {
                TablaSimbolos.agregarLexema(lexema.toString(), new CampoTablaSimbolos(false, TablaSimbolos.SINGLE));
            } else {
                TablaSimbolos.aumentarUso(lexema.toString());
            }

            if (nro == Float.POSITIVE_INFINITY || nro == Float.NEGATIVE_INFINITY || nro == -0.0f) {
                return new TokenError(TablaToken.getTokenID(TablaToken.TOKERROR), lexema.toString(), numeroDeLinea, "Linea "+ numeroDeLinea +": La constante se pasa de rango");
            } else {
                return new Token(tokenSingle, lexema.toString(), numeroDeLinea);
            }
        } catch (NumberFormatException e) {
            return new TokenError(TablaToken.getTokenID(TablaToken.TOKERROR), lexema.toString(), numeroDeLinea, "Linea "+ numeroDeLinea +": La constante se pasa de rango");
        }
    }

    public static String formatearString(String nro) {
      return nro.replace('s','e');
    }
}
