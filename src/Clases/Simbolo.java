package Clases;

/**
 * La clase Simbolo representa un símbolo dentro de un lenguaje de programación o sistema de análisis.
 * Contiene información sobre el tipo, nombre, valor, ámbito y otras características del símbolo.
 */
public class Simbolo {

    protected String tipo;
    protected String tipoToken;
    protected String nombre;
    protected Integer arraySize;
    protected Object valor;
    protected Integer scope;
    protected String ambito;

    /**
     * Constructor por defecto que inicializa los atributos con valores predeterminados.
     */
    public Simbolo(){
        tipo = null;
        tipoToken = null;
        nombre = "";
        valor = null;
        arraySize = 0;
        scope = 0;
        ambito = "";
    }

    /**
     * Constructor que permite inicializar un símbolo con valores específicos.
     * @param tipo Tipo del símbolo.
     * @param tipoToken Tipo del token asociado al símbolo.
     * @param nombre Nombre del símbolo.
     * @param valor Valor del símbolo.
     * @param arraySize Tamaño del array si aplica.
     * @param scope Nivel de alcance del símbolo.
     * @param ambito Nombre del ámbito donde se encuentra el símbolo.
     */
    public Simbolo(String tipo, String tipoToken, String nombre, Object valor, Integer arraySize, int scope, String ambito){
        this.tipo = tipo;
        this.tipoToken = tipoToken;
        this.nombre = nombre;
        this.valor = valor;
        this.arraySize = arraySize;
        this.scope = scope;
        this.ambito = ambito;
    }

    /**
     * Obtiene el tipo del símbolo.
     * @return Tipo del símbolo.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene el tipo de token asociado al símbolo.
     * @return Tipo del token.
     */
    public String getTipoToken() {
        return tipoToken;
    }

    /**
     * Obtiene el nombre del símbolo.
     * @return Nombre del símbolo.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el valor del símbolo.
     * @return Valor del símbolo.
     */
    public Object getValor() {
        return valor;
    }

    /**
     * Obtiene el tamaño del array si el símbolo representa un array.
     * @return Tamaño del array.
     */
    public Integer getArraySize() {
        return arraySize;
    }

    /**
     * Obtiene el nivel de alcance del símbolo.
     * @return Nivel de alcance del símbolo.
     */
    public int getScope() {
        return scope;
    }

    /**
     * Obtiene el nombre del ámbito donde se encuentra el símbolo.
     * @return Nombre del ámbito.
     */
    public String getAmbito() {
        return ambito;
    }

    /**
     * Establece el nivel de alcance del símbolo.
     * @param scope Nuevo nivel de alcance.
     */
    public void setScope(int scope) {
        this.scope = scope;
    }

    /**
     * Establece el nombre del ámbito donde se encuentra el símbolo.
     * @param ambito Nuevo nombre del ámbito.
     */
    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }
}