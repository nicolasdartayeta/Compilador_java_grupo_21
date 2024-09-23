package compilador;

public class Token {
    private int tokenID;

    private String lexema;

    public Token(int token, String lexema) {
        this.tokenID = token;
        this.lexema = lexema;
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

    @Override
    public String toString() {
        return "Token{" +
                "tokenID=" + tokenID +
                ", lexema='" + lexema + '\'' +
                '}';
    }
}
