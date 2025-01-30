package Clases;


public class Simbolo {

    protected String tipo;
    protected String tipoToken;
    protected String nombre;
    protected Integer arraySize;
    protected String ambito;
    protected Object valor;

    public Simbolo(){
        tipo = null;
        tipoToken = null;
        nombre = "";
        valor = null;
        arraySize = 0;
        ambito = "";
    }

    public Simbolo(String tipo, String tipoToken, String nombre, Object valor, Integer arraySize, String ambito){
        this.tipo = tipo;
        this.tipoToken = tipoToken;
        this.nombre = nombre;
        this.valor = valor;
        this.arraySize = arraySize;
        this.ambito = ambito;
    }



    public String getTipo() {
        return tipo;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public String getNombre() {
        return nombre;
    }

    public Object getValor() {
        return valor;
    }
    public Integer getArraySize() {
        return arraySize;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }
}