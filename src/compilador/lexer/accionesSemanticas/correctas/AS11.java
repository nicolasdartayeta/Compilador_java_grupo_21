package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.CampoTablaSimbolos;
import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;
import compilador.lexer.token.TokenError;

import javax.sound.midi.SysexMessage;
import java.text.StringCharacterIterator;

public class AS11 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        input.previous();
        try {
            long numero = Integer.parseUnsignedInt(lexema.toString(),8);
            int tokenOctal = TablaToken.getTokenID(TablaToken.CONSTANTE_OCTAL);
            String lexemaDecimal = String.valueOf(numero);
            if (TablaSimbolos.existeLexema(lexemaDecimal) == false){
                TablaSimbolos.agregarLexema(lexemaDecimal, new CampoTablaSimbolos(false, TablaSimbolos.ULONGINT));
            } else {
                TablaSimbolos.aumentarUso(lexemaDecimal);
            }

            return new Token(tokenOctal, lexemaDecimal, numeroDeLinea);
        } catch (NumberFormatException e) {
            return new TokenError(TablaToken.getTokenID(TablaToken.TOKERROR), lexema.toString(), numeroDeLinea, "Linea "+ numeroDeLinea +": La constante se pasa de rango");
        }
    }
}
