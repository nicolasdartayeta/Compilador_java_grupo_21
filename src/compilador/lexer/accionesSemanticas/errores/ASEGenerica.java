package compilador.lexer.accionesSemanticas.errores;

import java.text.StringCharacterIterator;

public class ASEGenerica extends ASE {
    @Override
    protected String getDescripcionError(StringCharacterIterator input, StringBuilder lexema) {
        return "Se tenia '" + lexema.toString() + "' y se esperaba otra cosa que '" + input.current() + "'";
    }
}
