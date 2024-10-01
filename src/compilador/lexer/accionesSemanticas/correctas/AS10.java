package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;

import java.text.StringCharacterIterator;

public class AS10 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
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
