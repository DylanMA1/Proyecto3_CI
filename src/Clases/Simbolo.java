package Clases;

public class Simbolo {

    protected String tipo;
    protected String nombre;
    protected String valor;
    protected int fila;
    protected int columna;

    public Simbolo(){
        tipo = "";
        nombre = "";
        valor = "";
        fila = 0;
        columna = 0;
    }

    public Simbolo(String tipo, String nombre, String valor, int fila, int columna){
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

    public String getValor() {
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

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}