package compilador.lexer.accionesSemanticas;

import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;
import compilador.lexer.Token;

import java.text.StringCharacterIterator;

public class AS6 implements AccionSemantica {
    @Override
    public Token ejecutar(StringCharacterIterator input, StringBuilder lexema) {
        input.previous();
        String lexemaAux = lexema.toString();

        if (TablaSimbolos.esPalabraReservada(lexema.toString().toUpperCase())) {
            return new Token(TablaSimbolos.getIDPalabraReservada(lexema.toString().toUpperCase()), lexemaAux);
        }

        int tokenId;

        if (lexemaAux.charAt(0) == 's'){
            tokenId = TablaToken.getTokenID(TablaToken.IDENTIFICADOR_SINGLE);
        } else if (lexemaAux.charAt(0) == 'u' || lexemaAux.charAt(0) == 'v' || lexemaAux.charAt(0) == 'w'){
            tokenId = TablaToken.getTokenID(TablaToken.IDENTIFICADOR_ULONGINT);
        } else{
            tokenId = TablaToken.getTokenID(TablaToken.IDENTIFICADOR_GENERICO);
        }

        if (TablaSimbolos.existeLexema(lexemaAux) == false){
            if (lexemaAux.length() > 15){
                //INFORMAR WARNING
                lexemaAux.substring(0,14);
            }
            TablaSimbolos.agregarLexema(lexemaAux,tokenId);
        }
        return new Token(tokenId, lexemaAux);
    }
}
