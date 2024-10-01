package compilador.lexer.token;

public class TokenError extends Token {

    private String descripcionError;

    public TokenError(int token, String lexema, int numeroDeLinea, String descripcionError) {
        super(token, lexema, numeroDeLinea);
        this.descripcionError = descripcionError;
    }

    @Override
    public boolean isError() {
        return true;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    @Override
    public String toString() {
        return "TokenError{" +
                "tokenName='" + tokenName + '\'' +
                ", tokenID=" + tokenID +
                ", lexema='" + lexema + '\'' +
                ", numeroDeLinea=" + numeroDeLinea +
                ", descripcionError='" + descripcionError + '\'' +
                '}';
    }
}
