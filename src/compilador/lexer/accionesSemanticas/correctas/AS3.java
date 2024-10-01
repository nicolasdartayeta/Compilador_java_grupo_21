package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;

import java.text.StringCharacterIterator;

public class AS3 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        if (input.current() == '}') {
            int tokenComentario = TablaToken.getTokenID(TablaToken.INLINE_STRING);
            TablaSimbolos.agregarLexema(lexema.toString(),tokenComentario);
            return new Token(tokenComentario, lexema.toString(), numeroDeLinea);
        }
        return null;
    }
}
