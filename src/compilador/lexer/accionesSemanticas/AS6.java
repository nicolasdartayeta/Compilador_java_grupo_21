package compilador.lexer.accionesSemanticas;

import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.Token;

import java.text.StringCharacterIterator;

public class AS6 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        input.previous();

        if (TablaSimbolos.esPalabraReservada(lexema.toString().toUpperCase())) {
            return new Token(TablaSimbolos.getIDPalabraReservada(lexema.toString().toUpperCase()), lexema.toString());
        }

        return new Token(TablaToken.getTokenID(TablaToken.IDENTIFICADOR), lexema.toString());

//        return null;
    }
}
