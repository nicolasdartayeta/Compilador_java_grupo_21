package compilador.lexer.accionesSemanticas;

import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.Token;

import java.text.StringCharacterIterator;

public class AS11 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        input.previous();
        long numero = Long.parseLong(lexema.toString(),8);

        if (validarNumero(numero)){
            int tokenOctal = TablaToken.getTokenID(TablaToken.CONSTANTE_OCTAL);

            if (TablaSimbolos.existeLexema(lexema.toString()) == false){
                TablaSimbolos.agregarLexema(lexema.toString(),tokenOctal);
            }
            return new Token(tokenOctal, lexema.toString());
        } else {
            return new Token(TablaToken.getTokenID(TablaToken.ERROR), "Overflow de la constante octal");
        }
    }

    public static boolean validarNumero(float numero) {
        // Rango máximo en octal: 037777777777 (en decimal: 68719476735)
        long maxValor = Long.parseLong("37777777777", 8);

        // Validar si está en el rango permitido
        if (numero >= 0 && numero <= maxValor) {
            return true;
        } else {
            return false;
        }
    }
}
