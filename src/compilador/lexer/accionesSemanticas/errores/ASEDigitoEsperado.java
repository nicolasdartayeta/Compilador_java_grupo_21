package compilador.lexer.accionesSemanticas.errores;

import java.text.StringCharacterIterator;

public class ASEDigitoEsperado extends ASE{
    @Override
    protected String getDescripcionError(StringCharacterIterator input, StringBuilder lexema) {
        return "Se esperaba un dígito luego del '" + lexema + "' en el token de constante. Se leyó '" + input.current() + "'";
    }
}
