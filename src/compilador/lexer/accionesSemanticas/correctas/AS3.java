package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.CampoTablaSimbolos;
import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;

import java.text.StringCharacterIterator;

public class AS3 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        if (input.current() == '}') {
            // Chequear si esta en la tabla de simbolos
            if (TablaSimbolos.existeLexema(lexema.toString())) {
                TablaSimbolos.aumentarUso(lexema.toString());
            } else {
                // Agregar a la tabla de simbolos
                TablaSimbolos.agregarLexema(lexema.toString(), new CampoTablaSimbolos(false, TablaToken.INLINE_STRING));
            }

            return new Token(TablaToken.getTokenID(TablaToken.INLINE_STRING), lexema.toString(), numeroDeLinea);
        }
        return null;
    }
}
