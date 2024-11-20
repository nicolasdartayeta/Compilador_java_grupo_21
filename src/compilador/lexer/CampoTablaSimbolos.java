package compilador.lexer;

import java.util.ArrayList;

public class CampoTablaSimbolos {
    public record Campo(String tipo, String nombre){
        public Campo(String tipo, String nombre){
            this.tipo = tipo;
            this.nombre = nombre;
        }
    }
    String ambito;
    boolean esTipo;
    String tipo;
    Integer usos;
    Integer cantidadDeParametros;
    String tipoRetorno;
    String tipoParametro;
    String nombreParametro;
    public ArrayList<Campo> campos;
    String uso;

    public CampoTablaSimbolos(boolean esTipo, String tipo) {
        this.ambito = null;
        this.esTipo = esTipo;
        this.tipo = tipo;
        this.usos = 1;
        this.cantidadDeParametros = null;
        this.tipoParametro = null;
        this.tipoRetorno = null;
        this.campos = null;
        this.uso = null;
        this.nombreParametro = null;
    }

    public String getAmbito() {return ambito;}

    public void setAmbito(String ambito) {this.ambito = ambito;}

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

    public void agregarCampo(String tipo, String nombre) {
        if (campos == null) {
            campos = new ArrayList<>();
        }
        campos.add(new Campo(tipo, nombre));
    }

    public ArrayList<Campo> getCampos() {
        return campos;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }
}
