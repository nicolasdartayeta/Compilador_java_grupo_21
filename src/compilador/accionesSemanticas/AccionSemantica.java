package compilador.accionesSemanticas;

import compilador.Token;

import java.text.StringCharacterIterator;

public interface AccionSemantica {
    Token ejecutar(StringCharacterIterator input, StringBuilder lexema);
}
