package Clases;

/**
 * Enumeración que representa los diferentes tipos de datos que pueden utilizarse en el sistema.
 */
public enum TipoDatos {
    INTEGER,  // Representa un tipo de dato entero.
    FLOAT,    // Representa un tipo de dato flotante.
    BOOL,     // Representa un tipo de dato booleano.
    CHAR,     // Representa un tipo de dato de carácter.
    STRING,   // Representa un tipo de dato de cadena de caracteres.
    IDENTIFIER, // Representa un identificador.
    UNDEFINED,  // Representa un tipo de dato indefinido.
    VOID;       // Representa un tipo de dato vacío o sin retorno.

    /**
     * Verifica si el tipo de dato actual es compatible con otro tipo de dato.
     * @param otro Tipo de dato a comparar.
     * @return true si ambos tipos son iguales, false en caso contrario.
     */
    public boolean esCompatible(TipoDatos otro) {
        return this == otro;
    }
}