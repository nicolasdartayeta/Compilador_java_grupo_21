package compilador.lexer.accionesSemanticas.errores;

import java.text.StringCharacterIterator;

public class ASECaracterNoPermitido extends ASE {
    @Override
    protected String getDescripcionError(StringCharacterIterator input, StringBuilder lexema) {
        return "No se puede usar el caracter '" + input.current() + "' en este contexto.";
    }
}
