package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;
import compilador.lexer.token.TokenError;

import java.text.StringCharacterIterator;

public class AS7 implements AccionSemantica {
//    @Override
//    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
//        input.previous();
//        int tokenSingle= TablaToken.getTokenID(TablaToken.CONSTANTE_SINGLE);
//
//        try {
//            float single = formatearString(lexema.toString());
//
//            if (validarNumero(single)){
//                if (TablaSimbolos.existeLexema(lexema.toString()) == false){
//                    TablaSimbolos.agregarLexema(lexema.toString(),tokenSingle);
//                }
//                return new Token(tokenSingle, lexema.toString());
//            } else {
//                return new TokenError(TablaToken.getTokenID(TablaToken.ERROR), lexema.toString(), numeroDeLinea, "La constante de pasa de rango");
//            }
//        } catch (NumberFormatException e){
//            return new Token(TablaToken.getTokenID(TablaToken.ERROR), "Formato no valido de la constante");
//        }
//    }

    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        input.previous();
        String posibleFloat = formatearString(lexema.toString());

        try {
            float nro = Float.parseFloat(posibleFloat);

            if (nro == Float.POSITIVE_INFINITY || nro == Float.NEGATIVE_INFINITY) {
                return new TokenError(TablaToken.getTokenID(TablaToken.ERROR), lexema.toString(), numeroDeLinea, "La constante se pasa de rango");
            } else {
                int tokenSingle = TablaToken.getTokenID(TablaToken.CONSTANTE_SINGLE);

                if (TablaSimbolos.existeLexema(posibleFloat) == false) {
                    TablaSimbolos.agregarLexema(posibleFloat, tokenSingle);
                }

                return new Token(tokenSingle, posibleFloat);
            }
        } catch (NumberFormatException e) {
            return new TokenError(TablaToken.getTokenID(TablaToken.ERROR), lexema.toString(), numeroDeLinea, "La constante se pasa de rango");
        }
    }

    public static String formatearString(String nro) {
      return nro.replace('s','e');
    }

//    public static boolean validarNumero(float x) {
//        boolean rangoPositivo = (x > 1.17549435e-38 && x < 3.40282347e+38);
//        boolean rangoNegativo = (x > -3.40282347e+38 && x < -1.17549435e-38);
//        boolean esCero = (x == 0.0f);
//
//        if (rangoPositivo || rangoNegativo || esCero) {
//            return true;
//        } else {
//            return false;
//        }
//    }


}
