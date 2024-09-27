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

        int tokenId = TablaToken.getTokenID(TablaToken.IDENTIFICADOR_GENERICO);
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
