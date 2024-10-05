package compilador.lexer.accionesSemanticas.correctas;

import compilador.lexer.CampoTablaSimbolos;
import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.accionesSemanticas.AccionSemantica;
import compilador.lexer.token.Token;

import java.text.StringCharacterIterator;

public class AS6 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema, int numeroDeLinea) {
        input.previous();
        String lexemaAux = lexema.toString();

        if (TablaSimbolos.esPalabraReservada(lexemaAux.toUpperCase())) {
            return new Token(TablaSimbolos.getIDPalabraReservada(lexemaAux.toUpperCase()), lexemaAux, numeroDeLinea);
        }

        if (lexemaAux.length() > 15){
            //INFORMAR WARNING
            System.out.println("Linea " + numeroDeLinea + ": El identificador '" + lexemaAux + "' tiene mas de 15 caracteres, sera truncado." );
            lexemaAux = lexemaAux.substring(0,14);
        }
        int tokenId;

        if (lexemaAux.charAt(0) == 's'){
            tokenId = TablaToken.getTokenID(TablaToken.IDENTIFICADOR_SINGLE);
        } else if (lexemaAux.charAt(0) == 'u' || lexemaAux.charAt(0) == 'v' || lexemaAux.charAt(0) == 'w') {
            tokenId = TablaToken.getTokenID(TablaToken.IDENTIFICADOR_ULONGINT);
        } else if (TablaSimbolos.esUnTipo(lexemaAux)) {
            tokenId = TablaToken.getTokenID(TablaToken.IDENTIFICADOR_TIPO);
        } else if (TablaSimbolos.esTipo(lexemaAux, TablaSimbolos.FUN)) {
            tokenId = TablaToken.getTokenID(TablaToken.IDENTIFICADOR_FUN);
        } else{
            tokenId = TablaToken.getTokenID(TablaToken.IDENTIFICADOR_GENERICO);
        }

        if (TablaSimbolos.existeLexema(lexemaAux) == false){
            String tipo = null;

            if (tokenId == TablaToken.getTokenID(TablaToken.IDENTIFICADOR_SINGLE)){
                tipo = TablaSimbolos.SINGLE;
            } else if (tokenId == TablaToken.getTokenID(TablaToken.IDENTIFICADOR_ULONGINT)){
                tipo = TablaSimbolos.ULONGINT;
            }
            // Otros tipos quedan para el parser

            TablaSimbolos.agregarLexema(lexemaAux,new CampoTablaSimbolos(false, tipo));
        } else {
            TablaSimbolos.aumentarUso(lexemaAux);
        }

        return new Token(tokenId, lexemaAux, numeroDeLinea);
    }
}
