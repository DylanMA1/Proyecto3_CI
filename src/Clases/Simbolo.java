package Clases;

public class Simbolo {

    protected String tipo;
    protected String nombre;
    protected Object valor;

    public Simbolo(){
        tipo = "";
        nombre = "";
        valor = null;
    }

    public Simbolo(String tipo, String nombre, Object valor){
        this.tipo = tipo;
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public Object getValor() {
        return valor;
    }


    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}