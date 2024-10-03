package compilador.lexer;

public class CampoTablaSimbolos {
    boolean esTipo;
    String tipo;
    Integer usos;

    public CampoTablaSimbolos(boolean esTipo, String tipo) {
        this.esTipo = esTipo;
        this.tipo = tipo;
        this.usos = 1;
    }

    public boolean esTipo() {
        return esTipo;
    }

    public void setEsTipo(boolean esTipo) {
        this.esTipo = esTipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getUsos() {
        return usos;
    }

    public void setUsos(Integer usos) {
        this.usos = usos;
    }

    public void aumentarUso() {
        usos++;
    }

    public void decrementarUso() {
        usos--;
    }
}
