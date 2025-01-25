package Clases;

public class Simbolo {

    protected String tipo;
    protected String nombre;
    protected Object valor;
    protected int fila;
    protected int columna;

    public Simbolo(){
        tipo = "";
        nombre = "";
        valor = null;
        fila = 0;
        columna = 0;
    }

    public Simbolo(String tipo, String nombre, Object valor, int fila, int columna){
        this.tipo = tipo;
        this.nombre = nombre;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
    }

    public String getTipo() {
        return tipo;
    }

    public int getColumna() {
        return columna;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFila() {
        return fila;
    }

    public Object getValor() {
        return valor;
    }


    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}