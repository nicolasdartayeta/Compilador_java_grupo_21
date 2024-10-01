package compilador.lexer.token;

import compilador.lexer.TablaSimbolos;
import compilador.lexer.TablaToken;

public class Token{
    protected int tokenID;

    protected String lexema;
    protected String tokenName;

    public Token(int token, String lexema) {
        this.tokenID = token;
        this.lexema = lexema;
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


    public void setTokenID(int tokenID) {
        this.tokenID = tokenID;
    }


    public String getLexema() {
        return lexema;
    }


    public void setLexema(String lexema) {
        this.lexema = lexema;
    }


    public boolean isError() {
        return false;
    }


    public String toString() {
        return "Token{" +
                "tokenName=" + this.tokenName +
                ", tokenID=" + tokenID +
                ", lexema='" + lexema + '\'' +
                '}';
    }
}
