package compilador.accionesSemanticas;

import compilador.TablaSimbolos;
import compilador.TablaToken;
import compilador.Token;

import java.text.StringCharacterIterator;

public class AS3 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        if (input.current() == '}') {
            int tokenComentario = TablaToken.getTokenID(TablaToken.INLINE_STRING);
            TablaSimbolos.agregarLexema(lexema.toString(),tokenComentario);
            return new Token(tokenComentario, lexema.toString());
        }
        return null;
    }
}
