package compilador.accionesSemanticas;

import compilador.TablaToken;
import compilador.Token;

import java.text.StringCharacterIterator;

public class AS10 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        char c = input.current();

        if (c == '=') {
            lexema.append(input.current());
            if (lexema.toString().equals("!=")) {
                return new Token(TablaToken.getTokenID(TablaToken.DESIGUAL), lexema.toString());
            } else if (lexema.toString().equals(":=")){
                return new Token(TablaToken.getTokenID(TablaToken.ASIGNACION), lexema.toString());
            }
        }

        input.previous();
        return new Token(TablaToken.getTokenID(TablaToken.ERROR), "No puede venir un" + c + "despues de " + lexema.toString());
    }
}
