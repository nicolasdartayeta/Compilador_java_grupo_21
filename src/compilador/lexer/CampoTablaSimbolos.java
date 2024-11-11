package compilador.lexer;

public class CampoTablaSimbolos {
    boolean esTipo;
    String tipo;
    Integer usos;
    Integer cantidadDeParametros;
    String tipoRetorno;
    String tipoParametro;

    public CampoTablaSimbolos(boolean esTipo, String tipo) {
        this.esTipo = esTipo;
        this.tipo = tipo;
        this.usos = 1;
        this.cantidadDeParametros = null;
        this.tipoParametro = null;
        this.tipoRetorno = null;
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

    public void setCantidadDeParametros(Integer cantidadDeParametros) {
        this.cantidadDeParametros = cantidadDeParametros;
    }

    public Integer getCantidadDeParametros() {
        return cantidadDeParametros;
    }

    public String getTipoParametro() {
        return tipoParametro;
    }

    public void setTipoParametro(String tipoParametro) {
        this.tipoParametro = tipoParametro;
    }

    public String getTipoRetorno() {
        return tipoRetorno;
    }

    public void setTipoRetorno(String tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }
}
