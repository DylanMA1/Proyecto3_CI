package Clases;

/**
 * Clase encargada de realizar el análisis semántico del código.
 * Contiene métodos para verificar la validez de las construcciones del lenguaje,
 * como funciones, breaks, returns, y compatibilidad de tipos.
 */
public class Semantico {

    private TablaSimbolos tablaSimbolos;

    /**
     * Constructor de la clase Semantico.
     *
     * @param tablaSimbolos La tabla de símbolos que se utilizará para el análisis semántico.
     */
    public Semantico(TablaSimbolos tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
    }

    /**
     * Inicializa el analisis semántico del código.
     */

    public void iniciarAnalisisSemantico() {
        verificarReturns();
        verificarBreaks();
    }

    /**
     * Verifica la validez de las sentencias de retorno en las funciones definidas en la tabla de símbolos.
     *
     */
    public void verificarReturns() {
        Simbolo funcion;
        Simbolo simbolo;
        boolean hasReturns;

        // Recorremos la tabla de símbolos para buscar funciones
        for (int i = 0; i < tablaSimbolos.getEntries().size(); i++) {
            simbolo = tablaSimbolos.getEntries().get(i);

            // Si encontramos una función, verificamos sus returns
            if (simbolo.getTipoToken().equals("FUNCTION")) {
                funcion = simbolo;
                hasReturns = false;

                // Recorremos la tabla de símbolos en reversa para buscar returns dentro del mismo ámbito
                for (int x = i - 1; i < tablaSimbolos.getEntries().size(); x--) {
                    simbolo = tablaSimbolos.getEntries().get(x);

                    // Si encontramos un return, verificamos su validez
                    if (simbolo.getTipoToken().equals("RETURN")) {
                        hasReturns = true;

                        // Verificamos que el return tenga el mismo tipo que la función y esté en el mismo ámbito
                        if (!(simbolo.getAmbito().equals(funcion.getAmbito())) && !(simbolo.getTipo().equals(funcion.getTipo())) && !(funcion.getTipo().equals("UNDEFINED"))) {
                            System.err.println("Error semántico: la funcion: " + funcion.getNombre() + " de tipo: " + funcion.getTipo() + " no puede devolver un parametro de tipo: " + simbolo.getTipo());
                            break;
                        }
                        // Si la función tiene tipo VOID pero devuelve algo, es un error
                        else if (simbolo.getTipo().equals("VOID")) {
                            System.err.println("Error semántico: la funcion: " + funcion.getNombre() + " debe devolver un parametro de tipo: " + funcion.getTipo());
                            break;
                        }
                        // Si la función es de tipo UNDEFINED pero tiene return, es un error
                        else if (funcion.getTipo().equals("UNDEFINED")) {
                            System.err.println("Error semántico: la funcion: " + funcion.getNombre() + " no debe devolver ningun parametro");
                            break;
                        }

                    }
                    // Si llegamos al inicio o encontramos otra función, verificamos si la función anterior tenía returns
                    else if (x == 0 || simbolo.getTipoToken().equals("FUNCTION")) {
                        if (!(funcion.getTipo().equals("UNDEFINED")) && !hasReturns) {
                            System.err.println("Error semántico: la funcion: " + funcion.getNombre() + " debe devolver un parametro de tipo: " + funcion.getTipo());
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Verifica la validez de las sentencias 'break' dentro de la tabla de símbolos.
     *
     */
    public void verificarBreaks() {
        Simbolo simbolo;
        Boolean breakDetected;

        // Recorrer la tabla de símbolos
        for (int i = 0; i < tablaSimbolos.getEntries().size(); i++) {
            simbolo = tablaSimbolos.getEntries().get(i);

            // Si el símbolo es un IF o SWITCH, buscar breaks dentro de su alcance
            if (simbolo.getTipoToken().equals("IF") || simbolo.getTipoToken().equals("SWITCH")) {

                // Recorrer los símbolos dentro del alcance del IF o SWITCH
                for (int x = i + 1; x < tablaSimbolos.getEntries().size(); x++) {
                    simbolo = tablaSimbolos.getEntries().get(x);

                    // Si se encuentra un break fuera de un bucle válido, imprimir error
                    if (simbolo.getTipoToken().equals("BREAK") && simbolo.getScope() < 3) {
                        System.err.println("Error semántico: break fuera de bucle");
                    }
                    // Si se encuentra un bloque de control o función, salir del bucle interno
                    else if (simbolo.getTipoToken().equals("CYCLE") || simbolo.getTipoToken().equals("IF") ||
                            simbolo.getTipoToken().equals("WHILE") || simbolo.getTipoToken().equals("FOR") ||
                            simbolo.getTipoToken().equals("FUNCTION") || simbolo.getTipoToken().equals("SWITCH")
                            || simbolo.getTipoToken().equals("BREAK")) {
                        break;
                    }
                }
            }

            // Verificar breaks en el alcance global (scope == 1)
            if (simbolo.getTipoToken().equals("BREAK") && simbolo.getScope() == 1) {
                System.err.println("Error semántico: break fuera de bucle");
            }
        }
    }

    /**
     * Comprueba si dos tipos de datos son compatibles.
     *
     * @param tipo1 El primer tipo.
     * @param tipo2 El segundo tipo.
     * @return True si son compatibles, False en caso contrario.
     */
    public boolean esTipoCompatible(TipoDatos tipo1, TipoDatos tipo2) {
        return tipo1.esCompatible(tipo2);
    }
}