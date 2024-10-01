package compilador.lexer.accionesSemanticas.errores;

import java.text.StringCharacterIterator;

public class ASEAsignacion extends ASE {
    @Override
    protected String getDescripcionError(StringCharacterIterator input, StringBuilder lexema) {
        return "Se esperaba el signo '=' luego del ':' en el token de asignacion. Se leyó '" + input.current() + "'";
    }
}
