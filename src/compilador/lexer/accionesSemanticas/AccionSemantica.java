package compilador.lexer.accionesSemanticas;

import compilador.lexer.Token;

import java.text.StringCharacterIterator;

public interface AccionSemantica {
    Token ejecutar(StringCharacterIterator input, StringBuilder lexema);
}
