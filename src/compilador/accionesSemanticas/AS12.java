package compilador.accionesSemanticas;

import compilador.TablaSimbolos;
import compilador.TablaToken;
import compilador.Token;

import java.text.StringCharacterIterator;

public class AS12 implements AccionSemantica{
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        input.previous();
        long decimal = Long.parseLong(lexema.toString());
        if (validarNumero(decimal)){
            int tokenDecimal = TablaToken.getTokenID(TablaToken.CONSTANTE_DECIMAL);

            if (TablaSimbolos.existeLexema(lexema.toString()) == false){
                TablaSimbolos.agregarLexema(lexema.toString(),tokenDecimal);
            }
            return new Token(tokenDecimal, lexema.toString());
        }else {
            return new Token(TablaToken.getTokenID(TablaToken.ERROR), "Overflow de constante decimal");
        }
    }

    public static boolean validarNumero(float numero) {
        long maxValor = 68719476735L;

        // Validar si está en el rango permitido (0 a maxValor)
        if (numero >= 0 && numero <= maxValor) {
            return true;
        } else {
            return false;
        }
    }
}
