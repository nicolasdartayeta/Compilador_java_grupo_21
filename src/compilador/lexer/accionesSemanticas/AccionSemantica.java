package compilador.lexer.accionesSemanticas;

import compilador.lexer.token.Token;

import java.text.StringCharacterIterator;

public interface AccionSemantica {
    Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea);
}
