package compilador.accionesSemanticas;

import compilador.TablaSimbolos;
import compilador.TablaToken;
import compilador.Token;

import java.text.StringCharacterIterator;

public class AS7 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        input.previous();
        int tokenSingle= TablaToken.getTokenID(TablaToken.CONSTANTE_SINGLE);

        try {
            float single = formatearStringToFloat(lexema.toString());

            if (validarNumero(single)){
                if (TablaSimbolos.existeLexema(lexema.toString()) == false){
                    TablaSimbolos.agregarLexema(lexema.toString(),tokenSingle);
                }
                return new Token(tokenSingle, lexema.toString());
            } else {
                return new Token(TablaToken.getTokenID(TablaToken.ERROR), "Overflow de la constante");
            }
        } catch (NumberFormatException e){
            return new Token(TablaToken.getTokenID(TablaToken.ERROR), "Formato no valido de la constante");
        }
    }

    public static float formatearStringToFloat(String nro) throws NumberFormatException{
      nro = nro.replace('s','e');
      return Float.parseFloat(nro);
    }

    public static boolean validarNumero(float x) {
        boolean rangoPositivo = (x > 1.17549435e-38 && x < 3.40282347e+38);
        boolean rangoNegativo = (x > -3.40282347e+38 && x < -1.17549435e-38);
        boolean esCero = (x == 0.0f);

        if (rangoPositivo || rangoNegativo || esCero) {
            return true;
        } else {
            return false;
        }
    }


}
