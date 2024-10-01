package compilador.lexer.token;

import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;

public class Token{
    protected int tokenID;
    protected int numeroDeLinea;
    protected String lexema;
    protected String tokenName;

    public Token(int token, String lexema, int numeroDeLinea) {
        this.tokenID = token;
        this.lexema = lexema;
        this.numeroDeLinea = numeroDeLinea;
        try {
            this.tokenName = TablaToken.getTokenNameFromId(this.tokenID);
        } catch (Exception e) {
            try {
                this.tokenName = TablaSimbolos.getPalabraReservadaFromId(this.tokenID);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    public int getTokenID() {
        return tokenID;
    }

    public String getLexema() {
        return lexema;
    }

    public boolean isError() {
        return false;
    }

    public int getNumeroDeLinea() {
        return numeroDeLinea;
    }

    public String getTokenName() {
        return tokenName;
    }

    public String toString() {
        return "Token{" +
                "tokenName=" + this.tokenName +
                ", tokenID=" + tokenID +
                ", lexema='" + lexema + '\'' +
                '}';
    }
}
