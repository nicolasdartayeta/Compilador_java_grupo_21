package compilador.lexer.token;

public class TokenError extends Token {
    private int numeroDeLinea;
    private String descripcionError;

    public TokenError(int token, String lexema, int numeroDeLinea, String descripcionError) {
        super(token, lexema);
        this.numeroDeLinea = numeroDeLinea;
        this.descripcionError = descripcionError;
    }

    @Override
    public boolean isError() {
        return true;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    public int getNumeroDeLinea() {
        return numeroDeLinea;
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
